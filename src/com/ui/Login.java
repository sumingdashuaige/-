package com.ui;

import com.domain.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Login {
    public void start() {

        ArrayList<User> list = new ArrayList<>();

        //启动登录界面
        System.out.println("登录界面已启动");

        while (true) { //ctrl+alt+T 选择对应语句包裹代码
            System.out.println("===================== ");
            System.out.println(" 欢迎来到文字格斗游戏 ");
            System.out.println("===================== ");
            System.out.println("请选择操作：1登录 2注册 3忘记密码");

            Scanner sc = new Scanner(System.in);
            String choose = sc.next();

            switch (choose){
                case"1":login(list);break;
                case"2":register(list);break;
                case"3":{System.out.println("选择了忘记密码");
                    forgetPassword(list);break;
                }
                default:System.out.println("输入错误，请重新输入");break;
            }
        }


    }

    public void login(ArrayList<User> list){//登录:从集合中获取用户信息-->验证
        System.out.println("选择了登录");
        //判断用户名是否存在
        //  不存在：提示用户注册
        //  存在：禁用状态：提示用户联系管理员
        //  存在：正常状态：验证验证码是否正确
        //      验证密码是否正确（三次机会）

        //1用户输入用户名
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = sc.next();
        //2判断用户名是否存在
        if (!contains(list,username)){
            System.out.println("用户名不存在，请先注册！");
            return;
        }
        //3判断用户是否禁用
        int index = findIndex(list,username);
        User u = list.get(index);
        if (!u.isStatus()){
            System.out.println("用户已被禁用，请联系管理员XXX-XXXXX！");
            //结束登录、回到选择页面
            return;
        }

        //4让用户继续输入验证码、密码
        //验证是否正确
        String rightPassword = u.getPassword();
        for (int i = 0; i < 3; i++) {

            System.out.println("请输入密码：");
            String password = sc.next();

            while (true) {
                String rightCode = getCode();
                System.out.println("验证码为："+rightCode);

                System.out.println("请输入验证码：");
                String code = sc.next();
                //先生成验证码

                if (rightCode.equalsIgnoreCase(code)){
                    System.out.println("验证码正确");
                    break;//验证码正确，跳出循环
                }else{
                    System.out.println("验证码错误");
                    continue;//验证码错误，重新生成验证码
                }
            }

            if(rightPassword.equals(password)){
                System.out.println("登录成功，游戏启动~");
                //创建FightingGame对象，调用方法启动游戏
                FightingGame fg = new FightingGame();
                fg.startGame(username);
                break;
            }else {
                System.out.println("密码错误");
                if(i==2){
                    //三次机会用完
                    u.setStatus(false);//禁用用户状态
                    System.out.println("当前用户"+username+"已被禁用，请联系管理员XXX-XXXXX！");
                    return;
                }
                else {
                    System.out.println("密码错误，还剩下"+(2-i)+"次机会~");
                }

            }
            
        }
    }


    public void register(ArrayList<User> list) {//注册：键盘录入用户信息-->对象-->集合
        System.out.println("选择了注册");

        //1创建用户对象
        User u = new User();

        //2键盘录入用户信息、校验、u.setUsername()
        //用户名
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入用户名：");
            String username = sc.next();

            //长度必须在3～16位；
            if (!checkLen(3, 16, username)) {
                System.out.println("用户名长度必须在3～16位");
                continue;
            }

            //只能由字母、数字组成，不能是纯数字
            if (!checkUsername(username)) {
                System.out.println("用户名只能由字母、数字组成，不能是纯数字");
                continue;
            }

            //用户名唯一
            if (contains(list,username)){
                System.out.println("用户名已存在");
                continue;
            }
            u.setUsername(username);
            break;
        }

        //手机号
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入手机号：");
            String phone = sc.next();
            //手机号纯数字
            if (!checkPhone(phone)) {
                System.out.println("手机号格式错误，只能是数字");
                continue;
            }
            //11位数字
            if (!checkLen(11, 11, phone)) {
                System.out.println("手机号格式错误，长度必须为11位");
                continue;
            }
            //手机号以1开头
            if(!phone.startsWith("1")){
                System.out.println("手机号格式错误，必须以1开头");
                continue;
            }
            //手机号唯一
            if (contains2(list,phone)){
                System.out.println("手机号已存在");
                continue;
            }
            u.setPhone(phone);
            break;
        }

        //密码
        while (true) {
            //3键盘录入密码、校验、u.setPassword()
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入密码：");
            String password1 = sc.next();
            System.out.println("请再次输入密码：");
            String password2 = sc.next();
            //长度3～8位
            if (!checkLen(3, 8, password1)) {
                System.out.println("密码长度必须在3～8位");
                continue;
            }

            // 只能是字母加数字的组合，不能有其他字母
            if (!checkPassword(password1)) {
                System.out.println("密码只能是字母加数字的组合，不能有其他字母");
                continue;
            }

            //密码确认
            if (!password1.equals(password2)) {
                System.out.println("两次密码不一致");
                continue;
            }
            u.setPassword(password1);
            break;

        }
        //4将用户对象添加到集合中
        list.add(u);
        //5提示注册成功
        System.out.println("注册成功");
    }


    public void forgetPassword(ArrayList<User> list){
        //1输入用户名，查询用户是否存在
        //2如果不存在，提示当前用户名未注册、结束方法
        //3如果存在，输入手机号，并验证手机号是否正确
        //手机号正确，输入新密码、修改密码、提示修改成功
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入用户名：");
            String username = sc.next();
            int index = findIndex(list, username);
            if (index == -1) {
                System.out.println("当前用户名未注册");
                return;
            }
            System.out.println("请输入手机号：");
            String phone = sc.next();
            if (!list.get(index).getPhone().equals(phone)) {
                System.out.println("手机号错误");
                continue;
            }
            System.out.println("验证成功");
            while (true) {
                //3键盘录入密码、校验、u.setPassword()
                /*Scanner sc = new Scanner(System.in);
                System.out.println("请输入密码：");*/
                System.out.println("请输入新密码：");
                String password1 = sc.next();
                System.out.println("请再次输入密码：");
                String password2 = sc.next();
                //长度3～8位
                if (!checkLen(3, 8, password1)) {
                    System.out.println("密码长度必须在3～8位");
                    continue;
                }
                // 只能是字母加数字的组合，不能有其他字母
                if (!checkPassword(password1)) {
                    System.out.println("密码只能是字母加数字的组合，不能有其他字母");
                    continue;
                }
                //密码确认
                if (!password1.equals(password2)) {
                    System.out.println("两次密码不一致");
                    continue;
                }
                list.get(index).setPassword(password1);
                System.out.println("密码修改成功");
                break;

            }
            break;

        }

    }

    //在集合中找username所在的索引
    public int findIndex(ArrayList<User> list,String username){
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            if (u.getUsername().equals(username)){
                return i;
            }
        }
        return -1;
    }

    //判断用户名是否唯一
    public boolean contains(ArrayList<User> list,String username){
            for (int i = 0; i < list.size(); i++) {
                    User u = list.get(i);
                    if (u.getUsername().equals(username)){
                        return true;
                    }
                }
                return false;
            }

    //判断手机号是否唯一
    public boolean contains2(ArrayList<User> list, String phone) {
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            if (u.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }



    public int[] getInfo(String username){
        int charcount = 0;
        int numcount = 0;
        int othercount = 0;

        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if(c>='a'&&c<='z'||c>='A'&&c<='Z'){
                //是字母
                charcount++;
            }else if(c>='0'&&c<='9'){
                //是数字
                numcount++;
            }else{
                othercount++;
            }
        }
        return new int[]{charcount,numcount,othercount};
    }

    //只能由字母、数字组成，不能是纯数字
    //字母至少有一个
    //数字可有可无
    //其他字符不能有
    public boolean checkUsername(String username){
        int[] arr = getInfo(username);
        return arr[0]>=1 && arr[1]>=0 && arr[2]==0;
    }

    public boolean checkPhone(String phone){
        //手机号纯数字
        int[] arr = getInfo(phone);
        return arr[0]==0 && arr[1]==phone.length() && arr[2]==0;
    }


    //只能由字母、数字组成，不能是纯数字
    //字母至少有一个
    //数字至少有一个
    //其他字符不能有
    public boolean checkPassword(String password){
        int[] arr = getInfo(password);
        return arr[0]>=1 && arr[1]>=1 && arr[2]==0;
    }

    //判断用户名长度是否符合要求
    //字符串、指定范围（最小 最大）
    public boolean checkLen(int minLen,int maxLen,String str){
        /*if(str.length() >= minLen && str.length() <= maxLen){
            System.out.println("用户名长度符合要求");
            return true;
        }else{
            System.out.println("用户名长度不符合要求");
            return false;
        }*/
        return str.length() >= minLen && str.length() <= maxLen;
    }



    //获取验证码
    public static String getCode(){
        /*
        长度为5
        由4位大写或者小写字母和1位数字组成，同一个字母可重复数字可以出现在任意位置
        比如：a0a1K
        */

        //1把所有大小写字母放进容器中
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char)('a'+i));
            list.add((char)('A'+i));
        }

        //2从集合中随机取4个字母
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            //随机获取集合中的索引
            int index = r.nextInt(list.size());
            char c = list.get(index);
            sb.append(c);
        }


        //3随机生成1个数字
        sb.append(r.nextInt(10));

        //4数字的位置任意
        //先把sb变成字符串，调用toCharArray方法，变成字符数组
        char[] arr = sb.toString().toCharArray();
        //把最大索引数据跟随机索引交换
        int i = r.nextInt(arr.length);
        //交换
        //最大索引：arr.length-1
        //随机索引：i
        char temp = arr[arr.length-1];
        arr[arr.length-1] = arr[i];
        arr[i] = temp;
        //5把字符数组变成字符串
        String code = new String(arr);
        return code;
    }
}
