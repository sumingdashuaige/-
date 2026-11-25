package com.domain;

public class Consumable {
    public String name;
    public int effect;


    //构造方法
    public Consumable() {}
    public Consumable(String name, int effect) {
        this.name = name;
        this.effect = effect;
    }

    public String show(){
        return name + "（HP+" + effect + "）";
    }

}
