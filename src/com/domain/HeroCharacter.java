package com.domain;

import java.util.ArrayList;

//我方英雄角色
public class HeroCharacter extends Character {
    public ArrayList<String> skillList;//技能列表

    //构造方法
    public HeroCharacter(){
        super();
        skillList = new ArrayList<>();
    }

    public HeroCharacter(String name,int Hp,int atk,int def) {
        super(name, Hp, atk, def);
        skillList = new ArrayList<>();
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

}
