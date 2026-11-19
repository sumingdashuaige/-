package com.domain;

import java.util.Random;

public class User {
    private String id;//用户无法设置，是自动生成的，格式为：heima+5位数字的随机数
    private String username;//用户名唯一；长度必须在3～16位；只能由字母、数字组成，不能是纯数字
    private String password;//长度3～8位；只能是字母加数字的组合，不能有其他字母
    private boolean status;//false:未激活 true:已激活
    private String phone;//手机号唯一；长度必须为11位；只能是数字，1开头，不能有其他字符

    public User() {
        id = createID();//调用方法设置id
        status = true;//默认是激活状态
    }
    public User(String username, String password) {
        id = createID();
        this.username = username;
        this.password = password;
        status = true;
    }

    //生成id
    public String createID(){
        StringBuilder sb = new StringBuilder("heima");

        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            int num = r.nextInt(10);
            sb.append(num);
        }
        return sb.toString();
    }

    //getters and setters
    public String getId() {
        return id;
    }
    /*public void setId(String id) {
        this.id = id;
    }*/
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}
}
