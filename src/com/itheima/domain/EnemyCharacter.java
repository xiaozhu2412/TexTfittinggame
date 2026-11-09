package com.itheima.domain;

public class EnemyCharacter extends Character {

    public String skill;
    public boolean defending;//是否有减伤buff

    public EnemyCharacter() {
        super();
    }

    public EnemyCharacter(String name, int HP, int attack, int defense, String skill) {
        super(name, HP, attack, defense);
        this.skill = skill;
    }

    @Override
    public void takeDamage(int damage) {

        if (defending) {
            damage  = damage/2>1?damage/2:1;
            //持续一个回合
            defending = false;
        }
        //扣血
        super.takeDamage(damage);
        }
    }




