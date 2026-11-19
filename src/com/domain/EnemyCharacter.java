package com.domain;

//敌方角色
public class EnemyCharacter extends Character {
   public String skill;//技能只有一个
   public boolean defending;//是否防御

    //构造方法
    public EnemyCharacter(){
        super();
    }

    public EnemyCharacter(String name,int Hp,int atk,int def,String skill) {
        super(name, Hp, atk, def);
        this.skill = skill;
    }


    //重写takeDamage方法
    @Override
    public void takeDamage(int damage){
        if (defending){//如果处于防御状态，伤害减半
            damage = damage / 2 > 1 ? damage / 2 : 1;
            //防御状态只持续一回合
            defending = false;
        }
        //扣血
        super.takeDamage(damage);//调用父类的takeDamage方法扣血
    }

}
