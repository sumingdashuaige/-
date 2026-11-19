package com.domain;

public class Character {
    public String name;
    public int Hp;
    public int maxHp;
    public int atk;
    public int def;//防御力


    //构造方法
    public Character(){}

    public Character(String name,int Hp,int atk,int def){
        this.name = name;
        this.Hp = Hp;
        this.maxHp = Hp;
        this.atk = atk;
        this.def = def;
    }

    //1判断是否存活
    public Boolean isAlive(){
        return Hp > 0;
    }

    //2恢复血量
    //形参：恢复的血量
    public void heal(int amount){
        Hp += amount;
        if (Hp > maxHp){
            Hp = maxHp;
        }
    }

    //3受伤
    //形参：受伤的血量
    public void takeDamage(int damage){
        Hp -= damage;
        if (Hp < 0){
            Hp = 0;
        }
    }

    //4展示属性
    public String show()
    {
        return name + " " + "【当前生命：" + Hp + " 攻击：" + atk + " 防御：" + def + "】";
    }
}
