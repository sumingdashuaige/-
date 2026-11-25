package com.domain;

import java.util.ArrayList;

//我方英雄角色
public class HeroCharacter extends Character {

    public int Mp;//蓝条
    public int maxMp;//最大蓝条
    public ArrayList<String> skillList;//技能列表
    public ArrayList<Consumable> packageList;//消耗品列表

    //构造方法
    public HeroCharacter(){
        super();
        skillList = new ArrayList<>();
        packageList = new ArrayList<>();
    }

    public HeroCharacter(String name,int Hp,int atk,int def,int Mp) {
        super(name, Hp, atk, def);
        this.maxMp = Mp;
        this.Mp = Mp;
        skillList = new ArrayList<>();
        packageList = new ArrayList<>();
    }

    public void heal2(int amount){
        Mp += amount;
        if (Mp > maxMp){
            Mp = maxMp;
        }
    }

    public void takeMp(int damage2){
        Mp -= damage2;
        if (Mp < 0){
            Mp = 0;
        }
    }


    //行为：用来遍历技能列表
    public String showSkill(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i< skillList.size(); i++) {
            sb.append(skillList.get(i));
            if(i != skillList.size() -1 ){
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    //行为：用来遍历消耗品列表
    public String showPackage(){
        if (packageList.isEmpty()){
            return "（背包为空）";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i< packageList.size(); i++) {
            sb.append(i + 1).append(".").append(packageList.get(i).show());
            if(i != packageList.size() -1 ){
                sb.append("  ");
            }
        }
        return sb.toString();
    }

    //行为：往背包添加消耗品
    public void addConsumable(Consumable c){
        packageList.add(c);
    }

    //行为：从背包中消耗消耗品
    public Consumable useConsumable(int index){
        if (index < 0 || index >= packageList.size()){
            return null;
        }
        Consumable c = packageList.get(index);
        heal(c.effect);
        packageList.remove(index);
        return c;
    }

    @Override
    public String show()
    {
        return name + " " + "【当前生命：" + Hp + " 攻击：" + atk + " 防御：" + def + " 蓝条：" + Mp + " 】";
    }


}
