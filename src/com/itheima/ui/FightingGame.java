package com.itheima.ui;

import com.itheima.domain.EnemyCharacter;
import com.itheima.domain.HeroCharacter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FightingGame {

    //启动
    public void gameStart(String username) {
        //1.显示标题
        System.out.println("╔════════════════════════════════╗");
        System.out.println("    🎮 欢迎来到文字格斗游戏 🎮   ");
        System.out.println("╚════════════════════════════════╝");
        System.out.println("请选择操作：1登录 2注册 3退出");

        //2.创建玩家角色 名字+属性分配
        HeroCharacter player = createPlayerCharacter(username);
        //3.显示创建角色的信息和技能列表
        System.out.println("角色创建成功！");
        System.out.println("\uD83C\uDF1F 初始属性为:"+player.show());
        System.out.println("\uD83C\uDF1F 拥有的技能:"+player.showSkills());

        //4.创建多个敌人列表
        // | name | HP | ATK | DEF | skills
        //| 初级战士 | 80     | 15     | 10     | 猛击（150%伤害）
        //| 敏捷刺客 | 60     | 20     | 5      | 快速攻击（2次50%伤害）
        //| 重装坦克 | 120    | 10     | 20     | 防御姿态（下回合伤害减半） buff（ boolean defendding）
        //| 神秘法师 | 70     | 25     | 8      | 火球术（180%伤害）
        ArrayList<EnemyCharacter> enemyList = new ArrayList<>();
        enemyList.add(new EnemyCharacter("初级战士", 80, 15, 10, "猛击"));
        enemyList.add(new EnemyCharacter("敏捷刺客", 60, 20, 5, "快速攻击"));
        enemyList.add(new EnemyCharacter("重装坦克", 120, 10, 20, "防御姿态"));
        enemyList.add(new EnemyCharacter("神秘法师", 70, 25, 8, "火球术"));

        //5.准备战斗
        int count=1;//记录和第几个敌人战斗
        int wins=0;//记录胜利了几场

        //主循环
        while (player.isAlive()){
            if(wins!=0){
                //获取敌人信息
                for (int i = 0; i < enemyList.size(); i++) {
                    EnemyCharacter c = enemyList.get(i);
                    c.maxHP=c.maxHP+10;
                    c.HP =c.maxHP;

                    c.attack+=3;

                    c.defense+=2;

                    c.defending=false;

                }
            }

            //
            Random r = new Random();
            int index=r.nextInt(enemyList.size());
            EnemyCharacter enemy = enemyList.get(index);
            System.out.println(enemy.show());

            /*═══════════════════════════════════════
⚔️ 第 1 场战斗开始！对手: 初级战士
---------------------------------------
⚔️ 第 1 回合开始！
zhangsan: [████████████████████] 100/100 HP
初级战士: [████████████████████] 80/80 HP
===== 你的回合 =====
1. 普通攻击
2. 强力一击 (消耗10HP)
3. 生命汲取 (消耗10HP，恢复生命)
选择行动 (1-3): 2
💥 消耗10HP，你对 敏捷刺客 使用了强力一击，造成 31 点伤害！
===== 敏捷刺客的回合 =====
⚔️ 敏捷刺客 对你使用了普通攻击，造成 15 点伤害！
---------------------------------------*/
            //5.3战斗开始
            System.out.println("═══════════════════════════════════════");
            System.out.println("⚔\uFE0F第"+count+"场战斗开始！对手："+enemy.name);
            //跟当前敌人是第几回合

            int round =1;
            while (player.isAlive()){
                System.out.println("---------------------------------------");
                System.out.println("️⚔\uFE0F第 "+round+" 回合开始！");
                //打印敌我双方血条
                System.out.println(getHealthBar(player.name, player.HP, player.maxHP));
                System.out.println(getHealthBar(enemy.name, enemy.HP, enemy.maxHP));

                //5.4玩家回合：选择行动（1普通攻击/2强力一击/3生命汲取）
                playerTurn(player, enemy);

                //5.5判断敌人是否被击败
                if (!enemy.isAlive()){
                    //🎉 你击败了 敏捷刺客！
                    System.out.println("🎉 你击败了 "+enemy.name+"！");

                    wins++;
                    break;
                }
                //5.6 敌人回合：选择行动（ 50%的几率普通攻击 / 50%的几率技能攻击 / 不同的敌人采取不同的技能进行攻击）
                enemyTurn(enemy, player);

                //5.7 判断玩家是否被击败
                if(!player.isAlive()){
                    System.out.println("你被"+enemy.name+"击败了，游戏结束！");
                    break;

                }
                //没有被击败，回合数+1，继续下一个回合
                round++;



               // System.exit(0);

            }

            //5.8跟第一个敌人战斗结束后，玩家胜利（继续战斗）玩家失败（游戏停止）
            if(player.isAlive()){
                //计算回复玩家血量
                int healHP=r.nextInt(21)+20;
                //恢复玩家血量
                player.heal(healHP);
                //提示
                //🎉 你击败了 敏捷刺客！
                //💚 战斗结束！你恢复了 36 点生命值
                //🏆 当前胜场: 1
                //═══════════════════════════════════════
                //继续下一场战斗？(y/n): y
                System.out.println("💚 战斗结束！你恢复了 "+healHP+" 点生命值");
                System.out.println("🏆 当前胜场: "+wins);
                System.out.println("═══════════════════════════════════════");

            }

            //5.9每胜利3场，人物属性就要增加
            if (player.isAlive() && wins>0&&wins % 3 == 0){
                //增加玩家属性

                System.out.println("你的属性增加了！");
                player.maxHP+=30;
                player.attack+=5;
                player.defense+=3;
                System.out.println("最大生命值+30，攻击力+5，防御力+3");
                System.out.println("当前属性:"+player.show());
            }
            //5.10 询问玩家是否继续
            if(player.isAlive()){
                System.out.println("继续下一场战斗？(y/n): ");
                Scanner sc = new Scanner(System.in);
                String choose = sc.next();
                if ("y".equalsIgnoreCase(choose)){
                    //战斗继续
                    count++;
                    continue;
                }else if ("n".equalsIgnoreCase(choose)){
                    System.out.println("游戏结束，感谢游玩！");
                    break;
                }else {
                    System.out.println("没有这个选项，默认游戏继续");
                    continue;
                }
            }



        }

        //6.游戏的最终结算
        System.out.println("═══════════════════════════════════════");
        System.out.println("游戏结束");
        System.out.println("你的最终胜场是: "+wins);
        System.out.println("感谢游玩！");
        //停止虚拟机
        System.exit(0);


    }


    //敌人的回合
    private void enemyTurn(EnemyCharacter enemy, HeroCharacter player) {
        System.out.println("===== "+enemy.name+"的回合 =====");

        //计算当前是普通攻击50%
        // 还是技能攻击50%----猛击 快速攻击 防御姿态 火球术
        String action = "普通攻击";

        //进行几率的计算
        Random r = new Random();
        int num =r.nextInt(10);

        if (num>=5){
            action=enemy.skill;


        }
        //根据不同情况，采取不同攻击手段
        switch (action){
            case "普通攻击":
                System.out.println("敌人采取了普通攻击");
                //计算敌人用普通攻击给我造成多少伤害
                int damage1=calculateDamage(enemy.attack, player.defense);
                //提示
                System.out.println("⚔\uFE0F"+enemy.name+" 对你使用了普通攻击，造成 "+damage1+" 点伤害！");
                //我方扣血
                player.takeDamage(damage1);

                break;
            case "猛击":
                System.out.println("敌人采取了猛击");
                int damage2=calculateDamage((int)(enemy.attack*1.5), player.defense);
                //提示
                System.out.println("💥"+enemy.name+" 对你使用了猛击，造成 "+damage2+" 点伤害！");
                player.takeDamage(damage2);
                break;
            case "快速攻击":
                System.out.println("敌人采取了快速攻击");
                int damage3 =0;
                for (int i = 0; i < 2; i++) {
                    int temp=calculateDamage(enemy.attack/2, player.defense);
                    damage3+=temp;
                }
                System.out.println("⚔\uFE0F"+enemy.name+" 对你使用了快速攻击，造成 "+damage3+" 点伤害！");
                player.takeDamage(damage3);
                break;
            case "防御姿态":
                System.out.println("敌人采取了防御姿态");
                enemy.defending=true;
                System.out.println(enemy.name+"采取了防御姿态，下回合伤害减半");

                break;
            case "火球术":
                System.out.println("敌人采取了火球术");
                int damage4=calculateDamage((int)(enemy.attack*1.8), player.defense);
                System.out.println("💥"+enemy.name+" 对你使用了火球术，造成 "+damage4+" 点伤害！");
                player.takeDamage(damage4);
                break;

        }


    }

    //定义方法打印敌我双方血条
    // zhangsan: [████████████████████] 100/100 HP
    //初级战士: [████████████████████] 80/80 HP
    public String getHealthBar(String name, int HP, int maxHP){
        int barLength = 20;
        int filled = (int) ((HP*1.0/maxHP)*barLength);

        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": [");
        
        //利用循环拼接方块和空格
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                sb.append("█");
            } else {
                sb.append(" ");
            }
        }
        sb.append("] ").append(HP).append("/").append(maxHP).append(" HP");
        return sb.toString();
    }


    //创建一个玩家角色
    //参数名字 用户名
    //返回值 结果
    public HeroCharacter createPlayerCharacter(String username){
        System.out.println("创建玩家角色");
        System.out.println("请输入角色名字"+ username);

        //属性分配
        int points =20;

        //提示
        System.out.println("请输入角色属性分配(共20点)");
        System.out.println("1.生命值（每点+10HP）");
        System.out.println("2.攻击力（每点+2ATK）");
        System.out.println("3.防御力（每点+1DEF）");

        Scanner sc = new Scanner(System.in);

        //定义数组把提示的语句存取来
        String[] attributes = {"生命值", "攻击力", "防御力"};
        //定义数组记录三个属性分配的属性点
        int[] values =new int[3];
        //利用循环分配属性点
        for (int i = 0; i < attributes.length; i++) {
            System.out.println("分配点数到"+attributes[i]+"(剩余点数："+points+")");
            //input表示当前用户键盘录入的数据
            int input = sc.nextInt();

            //如果分配的属性点数为负数
            if (input < 0) {
                System.out.println("无效输入！默认分配0点");
                input = 0;
            }
            //如果分配的属性点数超出剩余点数
            if (input > points) {
                System.out.println("剩余属性点不足！剩余属性点全部分配到："+attributes[i]);
                input = points;
            }
            //计算还要多少剩余的属性点
            points = points - input;

            //记录分配属性点
            values[i]=input;
        }

        //我们知道用户分配的属性点  values【i】
        //创建玩家角色
        HeroCharacter player = new HeroCharacter(
                username,//角色的名字
                100+values[0]*10,//生命值
                10+values[1]*2,//攻击力
                0+values[2]*1//防御力
        );

        //添加玩家的技能
        player.skillList.add("普通攻击");
        player.skillList.add("强力一击");
        player.skillList.add("生命汲取");

        //返回结果
        return player;





    }

    //5.4玩家回合：选择行动（1普通攻击/2强力一击/3生命汲取）
    public void playerTurn(HeroCharacter player, EnemyCharacter enemy){
        System.out.println("===== 你的回合 =====");
        System.out.println("1. 普通攻击");
        System.out.println("2. 强力一击");
        System.out.println("3. 生命汲取");
        System.out.println("选择行动 (1-3): ");
        Scanner sc = new Scanner(System.in);
        String choose = sc.next();
        switch (choose){
            default:
                System.out.println("没有这个操作默认使用普通攻击");
            case "1":

                int damage1 = calculateDamage(player.attack, enemy.defense);
                System.out.println("⚔\uFE0F你对 "+enemy.name+" 使用了普通攻击，造成 "+damage1+" 点伤害！");
                //扣血的操作
                enemy.takeDamage(damage1);
                break;
            case "2":
                if (player.HP > 10){

                    //消耗我方10HP
                    player.takeDamage(10);
                    //计算我方技能给地方造成了多少点伤害
                    int damage2 = calculateDamage((int)(player.attack*1.8), enemy.defense);
                    //提示我给对方造成了多少点伤害
                    //💥 消耗10HP，你对 敏捷刺客 使用了强力一击，造成 31 点伤害！
                    System.out.println("💥 消耗10HP，你对 "+enemy.name+" 使用了强力一击，造成 "+damage2+" 点伤害！");
                    //给敌人进行扣血
                    enemy.takeDamage(damage2);
                }else{
                    System.out.println("体力不足，攻击失败~~~");
                }
                break;
            case "3":

                if(player.HP > 10){
                    //消耗我方10HP
                    player.takeDamage(10);
                    //计算回复的血量
                    Random r = new Random();
                    int healHP = r.nextInt(21);
                    //真正给我方恢复血量
                    player.heal(healHP);
                    //💚消耗10HP，你使用了生命汲取，恢复了10点生命值！
                    System.out.println("💚消耗10HP，你使用了生命汲取，恢复了"+healHP+"点生命值！");

                }else{
                    System.out.println("体力不足，回复失败~~~");
                }
                break;


        }
    }

    //用来计算双方战斗时候造成的伤害
    //普通攻击调用方式
    public int calculateDamage(int attack,int defense){
        int damage = attack-defense;
        if (damage < 1){
            damage = 1;
        }
        return damage;

    }
}
