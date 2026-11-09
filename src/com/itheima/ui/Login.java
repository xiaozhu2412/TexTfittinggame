package com.itheima.ui;

import com.itheima.domain.User;

import java.lang.classfile.instruction.BranchInstruction;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Login {

    public void start(){

        System.out.println("游戏的登陆注册页面打开了");

        ArrayList<User> list  = new ArrayList<>();

        //ctrl+ alt + T
        while (true) {
            System.out.println("╔════════════════════════════════╗");
            System.out.println("    🎮 欢迎来到文字格斗游戏 🎮   ");
            System.out.println("╚════════════════════════════════╝");
            System.out.println("请选择操作：1登录 2注册 3退出");

            Scanner sc = new Scanner(System.in);
            String choose = sc.next();

            switch(choose){
                case "1" -> login(list);
                case "2"-> register(list);
                case "3"-> {
                    System.out.println("用户选择了退出操作");
                    System.exit(0);
                }
                default-> System.out.println("输入有误");
            }
        }

    }
    public void login(ArrayList<User> list){
        System.out.println("用户选择了登录操作");

        //用户名是否存在
        //不存在 提示未注册
        //存在 是否被禁用，提示联系客服
        //存在 验证验证码
        //验证是否正确 (3次)

        //用户名是否存在
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = sc.next();
        //不存在 提示未注册
        if (!contains(list,username)){
            System.out.println("用户名不存在，请注册");
            return;
        }
        //存在 是否被禁用，提示联系客服
        int index = findIndex(list, username);
        User u =list.get(index);
        if (!u.isStatus()){
            System.out.println("用户已禁用，请联系客服,xxx:xxxxxx");
            return;
        }

        //存在 验证验证码


        //验证密码
        String rightPassword = u.getPassword();

        for(int i=0 ;i<3;i++){
            System.out.println("请输入密码");
            String password = sc.next();
            while (true) {
                //生成验证码
                String rightCode = getCode();
                System.out.println("正确的验证码为："+ rightCode);

                System.out.println("请输入验证码");
                String code = sc.next();


                if (rightCode.equalsIgnoreCase(code)) {
                    System.out.println("验证码正确");
                    break;
                } else {

                    System.out.println("验证码错误");
                    continue;
                }
            }

            //密码
            if(rightPassword.equals(password)){
                System.out.println("登录成功，游戏启动");

                FightingGame fg = new FightingGame();
                fg.gameStart(u.getUsername());

                break;
            }else{
                System.out.println("密码错误");
                if(i==2){
                    //三次机会用完
                    u.setStatus(false);//ban账号
                    System.out.println("账号已封禁，请联系客服,xxx:xxxxxx");
                }else{
                    System.out.println("密码错误，还剩下"+ (2-i)+"次机会");
                }
            }

        }




    }

    public void register(ArrayList<User> list){
        System.out.println("用户选择了注册操作");
        //1.创建用户对象
        User u = new User();
        //2.键盘录入用户名，检验用户名字是否符合要求，u.setUsername()
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("请输入用户名：");
            String username = sc.next();
            u.setUsername(username);
            //用户名唯一
            //长度必须在3 ~ 16位
            //只能由字母、数字组成，不能是纯数字
            if(!checkUsername(username)) {
                System.out.println("只能由字母、数字组成，不能是纯数字");
                continue;
            }
            if (!checkLen(3, 16, username)){
                System.out.println("用户名长度必须在3 ~ 16位");
                continue;
            }
            if(contains(list,username)){
                System.out.println("用户名已存在,请重新输入");
                continue;
            }

            //当代码执行到这里，表示用户名符合要求
            u.setUsername(username);
            break;


        }


        //3.键盘录入密码，检验密码是否符合要求,u.setPsaaword()
        while (true) {
            System.out.println("请输入密码");
            String password1=sc.next();
            System.out.println("请再次输入密码：");
            String password2=sc.next();

            if(!checkLen(3,8,password1)){
                System.out.println("密码长度必须在3 ~ 8位");
                continue;
            }

            if(!checkPassword(password1)){
                System.out.println("不可有数字字母以外的其他字符");
                continue;
            }
            if(!password1.equals(password2)){
                System.out.println("两次输入的密码不一致");
                continue;
            }
            u.setPassword(password1);
            break;

        }
        //4.把User对象添加到集合中
        list.add(u);
        //5.提示成功
        System.out.println("注册成功");

    }

    //判断用户名是否唯一
    //找username所在的索引
    public int findIndex(ArrayList<User> list,String username){
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            if (u.getUsername().equals(username)){
                System.out.println("找到了");
                return i;
            }
        }
        System.out.println("没有找到");
        return -1;

    }

    public boolean contains(ArrayList<User> list,String username){
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            if (u.getUsername().equals(username)){
                return true;
            }
        }

        return false;
    }

    public int[] getCount(String userInfo){
        int charCount = 0;
        int numCount = 0;
        int otherCount = 0;

        for (int i = 0; i < userInfo.length(); i++) {
            char c = userInfo.charAt(i);
            if (c>='a'&&c<='z'||c>='A'&&c<='Z'){
                charCount++;
            }else if (c>='0'&&c<='9'){
                numCount++;
            }else {
                otherCount++;
            }
        }
        return new int[]{charCount, numCount, otherCount};

    }


    //检验用户名是否符合要求
    // 只能由字母、数字组成，不能是纯数字
    public boolean checkUsername(String username){
        int[] count = getCount(username);
        return count[0]>0 && count[2]==0;

    }

    public boolean checkPassword(String password){
        int[] count = getCount(password);
        return count[2]==0;
    }

    //判断字符串长度
    //字符串 指定范围
    //boolean
    public boolean checkLen(int minLen ,int maxLen,String str){
        return str.length() >= minLen && str.length() <= maxLen;

    }

    public static String getCode() {

        ArrayList<Character> list = new ArrayList<>();
        for (int i=0;i<26;i++){
            list.add((char)('a'+i));
            list.add((char)('A'+i));
        }

        //集合中随机抽取字母四次
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i=0;i<4;i++){
            int index = r.nextInt(list.size());
            char c = list.get(index);
            sb.append(c);
        }
        //生成随机数字
        sb.append(r.nextInt(10));

        System.out.println(sb);
        //数字位置可以是任意的
        //sb变成字符串，调用toCharrArry方法
        char[] arr = sb.toString().toCharArray();
        //把最大索引数据和一个索引进行交换
        int i=r.nextInt(arr.length);
        char temp = arr[i];
        arr[i] = arr[arr.length-1];
        arr[arr.length-1] = temp;

        String code = new String(arr);


        return code;
    }

}

