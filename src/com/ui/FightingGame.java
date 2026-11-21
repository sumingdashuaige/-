package com.ui;

import com.domain.EnemyCharacter;
import com.domain.HeroCharacter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FightingGame {
    //开始游戏
    public void startGame(String userName){
    //1显示游戏标题
        System.out.println("==============================");
        System.out.println("欢迎" + userName + "来到文字战斗游戏！");
        System.out.println("==============================");


    //2创建玩家角色（名字+属性分配）
        HeroCharacter player = createHeroCharacter(userName);
    //3显示创建角色的信息
        System.out.println("角色创建成功");
        System.out.println("⭐初始属性："+ player.show());
        System.out.println("⭐初始技能："+ player.showSkill());

    //4创建多个敌人列表
    //name hp atk def skill
    //初级战士 80 15 10 猛击
    //敏捷刺客 60 20 5 快速攻击
    //重装坦克 120 10 20 防御姿态
    //神秘法师 70 25 8 火球术（180%伤害）
        ArrayList<EnemyCharacter> enemyList = new ArrayList<>();
        enemyList.add(new EnemyCharacter("初级战士",80,15,10,"猛击"));
        enemyList.add(new EnemyCharacter("敏捷刺客",60,20,5,"快速攻击"));
        enemyList.add(new EnemyCharacter("重装坦克",120,10,20,"防御姿态"));
        enemyList.add(new EnemyCharacter("神秘法师",70,25,8,"火球术"));


    //5准备战斗
        int count = 1;//正在跟第几个敌人战斗
        int wins = 0;//玩家胜利的次数

    //玩家一次跟多个敌人战斗，直到生命为0，游戏结束
        //外循环：跟多个敌人战斗
        while (player.isAlive()){
            //5.1重置敌人属性，敌人属性每场HP+10，ATK+3，DEF+2（敌人越来越难打）（第二场的时候开始增加）
            if(wins != 0){
                //获取每个敌人信息
                for (int i = 0; i < enemyList.size(); i++) {
                    EnemyCharacter c = enemyList.get(i);
                    //增加属性点
                    c.maxHp += 10;
                    c.Hp = c.maxHp;
                    c.atk += 3;
                    c.def += 2;
                    //每场战斗之前，如果有减伤的buff，我们需要清空
                    c.defending = false;
                }
            }
            //5.2随机选择敌人
            Random r = new Random();
            int index = r.nextInt(enemyList.size());
            EnemyCharacter enemy = enemyList.get(index);
            System.out.println(enemy.show());


            //5.3开始跟抽取到敌人进行战斗
            System.out.println("============================================================");
            System.out.println("⚔️第" + count + "场战斗开始！对手：" + enemy.name);

            //回合制（你打我一下，我打你一下）
            //内循环：跟单个敌人进行多轮战斗，直到有一方的血量为0才会结束
            int round = 1;//记录跟单个敌人战斗的回合数
            //内循环：跟单个敌人进行多轮战斗
            while (player.isAlive()){
                //显示双方的状态
                System.out.println("==============================");
                System.out.println("当前回合：" + round);
                //打印敌我双方血条
                System.out.println(getHpBar(player.name,player.Hp,player.maxHp));
                System.out.println(getHpBar(enemy.name,enemy.Hp,enemy.maxHp));


                // 5.4玩家回合：选择行动（1普通攻击／2强力一击／3生命汲取）
                // 我打敌人一下
                // 判断敌人的血量是否为0，如果为0，结束当前的战斗，继续跟下一个敌人进行战斗（结束内循环，继续执行外循环）
                // 敌人打我一下
                // 判断我的血量是否为0，如果为0，结束整个游戏
                // 如果我的血量不为o.继续执行内循环，开始下一个回合round++;
                playerTurn(player,enemy);

                //5.5判断敌人hp是否为0
                if(!enemy.isAlive()){
                    //🎉恭喜你击败了敏捷刺客！
                    System.out.println("🎆恭喜你击败了" + enemy.name + "！");
                    wins++;
                    break;
                }

                //5.6敌方回合
                enemyTurn(enemy,player);

                //5.7判断玩家hp是否为0
                if(!player.isAlive()){
                    //游戏结束
                    System.out.println("💀游戏结束，你被 " + enemy.name + " 击败了！");
                    break;
                }
                //如果玩家hp不为0，回合数+1，继续下一个回合round++;
                round++;


            }

            //5.8内循环结束，玩家胜利（继续战斗）；玩家失败（游戏结束）
            if(player.isAlive()){
                //恢复玩家血量
                int recover = r.nextInt(21) + 20;
                player.heal(recover);
                //恢复玩家Mp：maxMp的30%
                int recoverMp = (int)(player.maxMp * 0.3);
                player.heal2(recoverMp);
                //战斗结束!你回复了 点生命值！回复了 点Mp！
                System.out.println("💚战斗结束!你回复了" + recover + "点生命值！回复了" + recoverMp + "点Mp！");
                //当前胜场
                System.out.println("🏆当前胜场：" + wins);
                System.out.println("============================================================");

            //5.9每胜利三场，玩家属性增加
            if(player.isAlive() && wins > 0 && wins % 3 == 0){
                System.out.println("✨恭喜你获得属性提升！");
                //最大生命值提升
                player.maxHp += 30;
                player.Hp  += 30;
                //攻击力提升
                player.atk += 5;
                //防御力提升
                player.def += 3;
                System.out.println("最大生命值提升30点、攻击力提升5点、防御力提升3点！");
                System.out.println("你当前的属性为：" + player.show());
            }

            //5.10询问玩家是否继续战斗
                System.out.println("是否继续战斗？（y.是 n.否）");
                Scanner sc = new Scanner(System.in);
                String choose = sc.next();
                if("y".equalsIgnoreCase(choose)){
                    count++;
                    continue;
                }else if("n".equalsIgnoreCase(choose)){
                    break;
                }else {
                    System.out.println("没有这个选项，默认继续战斗");
                    count++;
                    continue;
                }

            }

        }

        //游戏结束
        System.out.println("==============================");
        System.out.println("游戏结束，你共胜利了" + wins + "场！");
        System.out.println("感谢游玩，再见！");
        System.exit(0);

    }

    //定义方法：打印敌我双方血条
    public String getHpBar(String name,int hp,int maxHp){
        //满血打印20个方块
        int barLength = 20;

        //计算在不同的血量当中，一共打印多少个方块
        //最大血量：200
        //当前血量：100
        int filled = (int)(hp * 1.0 * barLength / maxHp);

        StringBuilder sb = new StringBuilder();
        sb.append(name).append("：[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled){
                sb.append("█");
            }else{
                sb.append("░");
            }
        }
        sb.append("]").append(hp).append("/").append(maxHp);
        return sb.toString();
    }

    //玩家回合：
    //选择行动（1普通攻击／2强力一击／3生命汲取）
    public void playerTurn(HeroCharacter player,EnemyCharacter enemy){
        System.out.println("====你的回合====");
        System.out.println("1.普通攻击");
        System.out.println("2.强力一击");
        System.out.println("3.生命汲取");
        System.out.println("请选择您的行动（1-3）：");
        Scanner sc = new Scanner(System.in);
        String choose = sc.next();
        //判断用户输入是否有效
        switch (choose){
            default:
                System.out.println("异常操作，默认普通攻击");
            case "1":
                //System.out.println("1.普通攻击");
                //伤害=我方攻击力-对方防御力
                int damage1 = calculateDamage(player.atk,enemy.def);
                //⚔️你对敏捷刺客使用了普通攻击，造成31点伤害！
                System.out.println("⚔️你对" + enemy.name + "使用了普通攻击，造成" + damage1 + "点伤害！");
                //敌人受到伤害
                enemy.takeDamage(damage1);
                break;
            case "2":
                //System.out.println("2.强力一击");
                if (player.Mp > 10){
                    //消耗10点Mp
                    player.takeMp(10);
                    int damage2 = calculateDamage((int) (player.atk * 1.8), enemy.def);
                    System.out.println("💥你消耗了10点Mp，对" + enemy.name + "使用了强力一击，造成" + damage2 + "点伤害！");
                    //敌人受到伤害
                    enemy.takeDamage(damage2);
                }else {
                    System.out.println("你没有足够的Mp，使用强力一击失败！");
                }
                break;
            case "3":
                //System.out.println("3.生命汲取");
                if (player.Mp > 10){
                    //消耗10点Mp
                    player.takeMp(10);
                    //计算恢复的血量
                    Random r = new Random();
                    int recover = r.nextInt(51) + 20;
                    //恢复生命量
                    player.heal(recover);
                    //提示：消耗10HP，你使用了生命汲取，恢复了recover点生命值！
                    System.out.println("消耗10HP，你使用了生命汲取，恢复了" + recover + "点生命值！");
                }else {
                    System.out.println("你没有足够的Mp，使用生命汲取失败！");
                }
                break;

        }
    }

    //敌方回合：
    public void enemyTurn(EnemyCharacter enemy,HeroCharacter player){
        System.out.println("====" + enemy.name + "的回合====");
        //计算当前是普通攻击50%、还是技能攻击50%--猛击 快速攻击 防御姿态 火球术
        String action = "普通攻击";

        Random r = new Random();//0-1随机概率
        int chance = r.nextInt(2);
        if (chance == 0){
            action = "普通攻击";
        }else{
            action = enemy.skill;
        }

        //根据不同情况，采取不同攻击手段
        switch (action){
            case "普通攻击":
                int damage1 = calculateDamage(enemy.atk,player.def);
                System.out.println("⚔️" + enemy.name + "使用了普通攻击，对你造成" + damage1 + "点伤害！");
                //玩家受到伤害
                player.takeDamage(damage1);
                break;
            case "猛击":
                int damage2 = calculateDamage((int) (enemy.atk * 1.5), player.def);
                System.out.println("💥" + enemy.name + "使用了猛击，对你造成" + damage2 + "点伤害！");
                player.takeDamage(damage2);
                break;
            case "快速攻击":
                int damage3 = 0;
                for (int i = 0; i < 2; i++) {
                    int temp = calculateDamage(enemy.atk / 2,player.def);
                    damage3 += temp;
                }
                System.out.println("⚔️" + enemy.name + "使用了快速攻击，对你造成" + damage3 + "点伤害！");
                player.takeDamage(damage3);
                break;
            case "防御姿态":
                enemy.defending = true;
                System.out.println("🛡️" + enemy.name + "使用了防御姿态，对你的伤害减半！");
                break;
            case "火球术":
                int damage4 = calculateDamage((int) (enemy.atk * 1.8), player.def);
                System.out.println("🔥" + enemy.name + "使用了火球术，对你造成" + damage4 + "点伤害！");
                player.takeDamage(damage4);
                break;

        }


    }


    //作用：用来计算双方战斗的时候，造成的伤害
    //普通攻击的调用方式：calculateDamage（我方攻击力，对方的防御力）；
    //技能攻击的调用方式：calculateDamage（我方攻击力*百分比，对方的防御力）；
    public int calculateDamage(int atk,int def){
        int damage = atk - def;
        if (damage < 0){
            damage = 0;
        }
        return damage;
    }

    //创建玩家角色
    //参数：用户名
    //返回：玩家角色
    public HeroCharacter createHeroCharacter(String userName){
        System.out.println("创建您的角色");
        System.out.println("您的角色名为：" + userName);

        //属性分配
        int points = 20;

        /*System.out.println("您有" + points + "点属性点");
        System.out.println("生命值（每点+10Hp）");
        Scanner sc = new Scanner(System.in);
        int hpPoint = sc.nextInt();

        if (hpPoint < 0){
            System.out.println("无效输入，默认分配0点");
            hpPoint = 0;
        }
        if(hpPoint > points){
            System.out.println("属性点不足，默认全部分配生命值");
            hpPoint = points;
        }
        //分配属性点
        points -= hpPoint;*/

        //提示
        System.out.println("请分配属性点：" + points + "点");
        System.out.println("1.生命值（每点+10Hp）");
        System.out.println("2.攻击力（每点+2atk）");
        System.out.println("3.防御力（每点+1def）");

        Scanner sc = new Scanner(System.in);

        String[] attributes = {"生命值","攻击力","防御力"};
        //定义数组记录三个属性分配的属性点
        int[] values = new int[3];

        //利用一个循环分配属性点
        for (int i = 0; i < attributes.length; i++) {
            System.out.println("分配点数到" + attributes[i] + "（剩余点数" + points + "）");
            //获取用户输入（要分配的属性点）
            int input = sc.nextInt();

            //判断输入是否有效
            if (input < 0){
                System.out.println("无效输入，默认分配0点");
                input = 0;
            }
            if(input > points){
                System.out.println("属性点不足，默认全部分配");
                input = points;
            }

            //分配属性点
            points -= input;
            //记录分配的属性点
            values[i] = input;


        }

        //已知用户分配的属性点--->values[i]
        //直接创建玩家角色
        HeroCharacter player = new HeroCharacter(userName,
                100+values[0]*10,
                10+values[1]*2,
                0+values[2]*1,
                100
        );

        //添加玩家技能
        player.skillList.add("普通攻击");
        player.skillList.add("强力一击");
        player.skillList.add("生命汲取");

        return player;
    }

}
