package service;

import domain.Poker;
import service.play.Play;
import view.CommonAttribute;
import view.GameJFrame;
import view.Message;


import java.awt.*;
import java.util.*;

public class PlayerOperation extends Thread {
    //游戏主界面
    GameJFrame gameJFrame;

    //倒计时
    int i;

    // 游戏结束提示框
    public Message message;

    // 公共属性类
    CommonAttribute attr = CommonAttribute.getCommonAttribute();

    // 构造器
    public PlayerOperation(GameJFrame m, int i) {
        this.gameJFrame = m;
        this.i = i;
    }

    /**
     * 负责人：阿布
     * 功能：1.抢地主功能：玩家点击抢地主，获得地主牌，将牌重新排序
     *      2.玩家出牌：根据trun的值判断谁出牌，根据gameJFrame.robot的值判断是否由机器人帮玩家出牌(托管功能)
     *      3.出牌循环：玩家每点击一次出牌，进行一轮循环，出牌成功判断当前牌是否为空(isWIn方法)，牌为空则游戏结束，跳出循环
     */
    @Override
    public void run() {
        while (i > -1 && attr.isRun) {
            attr.time[1].setText("倒计时:" + i--);
            sleep(1);
        }
        if (i == -1){
            attr.time[1].setText("不抢");
        }
        attr.landlord[0].setVisible(false);
        attr.landlord[1].setVisible(false);
        if (attr.time[1].getText().equals("抢地主")) {
            // 设置中间玩家(自己)为地主
            lord(1);
        } else if ((int)(Math.random() * 2) == 0) {
            // 设置右边玩家为地主
            lord(2);
        } else {
            // 设置左边玩家为地主
            lord(0);
        }
        // 整理完牌才能点击牌，防止出bug
        for (Poker poker2 : attr.playerList.get(1)){
            poker2.setCanClick(true);// 可被点击
        }
        // 关闭出牌按钮
        turnOn(false);
        for (int i = 0; i < 3; i++) {
            attr.time[i].setText("不要");
            attr.time[i].setVisible(false);
        }
        // 开始游戏 根据地主不同，顺序不同,trun后续值会变化，直接用dizhuFlag则会该变dizhuFlag的值
        // 根据trun值确定谁出牌
        attr.trun = attr.dizhuFlag;
        while(true) {
            if (attr.time[0].getText().equals("不要") && attr.time[2].getText().equals("不要")) {
                attr.previous.clear();
            }
            if (attr.trun == 1) {
                // 无限制出牌，只显示出牌按钮
                attr.publishCard[1].setEnabled(!attr.time[0].getText().equals("不要") || !attr.time[2].getText().equals("不要"));
                // 判断是否是托管
                if (attr.robot) {
                    //电脑代替出牌
                    computer1();
                }else {
                    // 玩家出牌
                    turnOn(true);
                    timeWait(30, 1);
                    turnOn(false);

                }
                // 有一方获胜，终止游戏
                if (isWin()) {
                    break;
                }
                attr.trun = (attr.trun + 1) % 3;
            }
            if (attr.trun == 2) {
                // 右边人机出牌
                computer2();
                attr.trun = (attr.trun + 1) % 3;
                // 有一方获胜，终止游戏
                if (isWin()) {
                    break;
                }
            }
            if (attr.trun == 0) {
                // 左边人机出牌
                computer0();
                // 有一方获胜，终止游戏
                attr.trun = (attr.trun + 1) % 3;
                if (isWin()) {
                    break;
                }
            }
        }
    }

    /**
     * 负责人：阿布
     * 功能：定义一个方法用来暂停N秒，因为线程中的sleep方法有异常，直接调用影响阅读
     * 参数：i 时间，单位为秒
     * 返回值：void
     */
    public void sleep(int i) {
        try {
            Thread.sleep((long)i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 负责人：阿布
     * 功能：抢地主的后续操作
     *      将第i个玩家设置成地主
     *      获得地主牌
     *      翻开地主牌
     *      对地主手中的牌进行重新排序
     * 参数：i:  表示第几个玩家   0表示左边玩家  1表示中间玩家  2表示右边玩家
     * 返回值：void
     */
    public void lord(int i) {
        attr.playerList.get(i).addAll(attr.lordList);
        lockLordList(attr.lordList);
        if (i == 1) {
            openlord(true);
            attr.publishCard[1].setEnabled(false);
        } else {
            openlord(false);
        }
        sleep(1);
        // 重新排序
        order(attr.playerList.get(i));
        // 重新显示出来
        rePosition(gameJFrame, attr.playerList.get(i), i);
        setlord(i);
    }

    /**
     * 负责人：阿布
     * 功能：将地主底牌在上方显示出来
     * 参数： list    地主底牌
     * 返回值：void
     */
    public void lockLordList(ArrayList<Poker> list) {
        ArrayList<Poker> pokers = new ArrayList<>();
        int i = 0;
        for (Poker poker : list) {
            Poker poker1 = new Poker(gameJFrame, poker.getName(), false);
            gameJFrame.container.add(poker1);
            poker1.setVisible(true);
            gameJFrame.container.setComponentZOrder(poker1, 0);
            poker1.setUp(true);
            poker1.turnFront();
            poker1.setLocation(310 + (70 * i++), 5);
            pokers.add(poker1);
        }
    }
    /**
     * 负责人：阿布
     * 功能：翻开地主牌
     * 参数： is:  是否翻开地主牌
     * 返回值：void
     */
    public void openlord(boolean is) {
        for (int i = 0; i < 3; i++) {
            if (is)
                attr.lordList.get(i).turnFront();
            else {
                attr.lordList.get(i).turnRear();
            }
            attr.lordList.get(i).setCanClick(true);
        }
    }

    /**
     * 负责人：阿布
     * 功能：设定地主
     * 参数： i: 第几个人是地主
     * 返回值： void
     */
    public void setlord(int i) {
        Point point = new Point();
        if (i == 1) {
            point.x = 80;
            point.y = 430;
            attr.dizhuFlag = 1;
        }
        if (i == 0) {
            point.x = 80;
            point.y = 20;
            attr.dizhuFlag = 0;
        }
        if (i == 2) {
            point.x = 700;
            point.y = 20;
            attr.dizhuFlag = 2;
        }
        attr.dizhu.setLocation(point);
        attr.dizhu.setVisible(true);
    }




    /**
     * 负责人：阿布
     * 功能：设置出牌和不要两个按钮
     * 参数：flag: ture表示显示出牌和不要  false表示不显示
     * 返回值：void
     */
    public void turnOn(boolean flag) {
        attr.publishCard[0].setVisible(flag);
        attr.publishCard[1].setVisible(flag);
        attr.publishCard[2].setVisible(flag);
    }

    /**
     * 负责人：阿布
     * 功能：延时，模拟时钟
     * 参数：n: 玩家的倒计时    player: 表示出牌玩家
     * 返回值：void
     */
    public void timeWait(int n, int player) {
        if (attr.currentList.size() != 0 && attr.currentList.get(player).size() > 0)
            hideCards(attr.currentList.get(player));
        if (attr.currentList.size() == 0) {
            for (int j = 0; j < 3; j++) {
                attr.currentList.add(new ArrayList<>());
            }
        }
        if (player == 1 && !attr.robot) {
            int i = n;
            while (!attr.nextPlayer && i >= 0) {
                attr.time[player].setText("倒计时:" + i);
                attr.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            if (i == -1) {
                // 如果自己超时
                if (attr.time[0].getText().equals("不要") && attr.time[2].getText().equals("不要")) {
                    attr.previous.clear();
                }
                ArrayList<Poker> robot = Play.robot(attr.previous, attr.playerList.get(1));
                if (robot.size() != 0) {
                    attr.previous.clear();
                    attr.previous.addAll(robot);
                }
                movePoker(robot, 1);
            }
            attr.nextPlayer = false;
        } else {
            for (int i = n; i >= 0; i--) {
                sleep(1);
                attr.time[player].setText("倒计时:" + i);
                attr.time[player].setVisible(true);
            }
        }
        attr.time[player].setVisible(false);
    }

    /**
     * 负责人：阿布
     * 功能： 隐藏之前出过的牌
     * 参数 list:  需要隐藏的牌组
     * 返回值: void
     */
    public static void hideCards(ArrayList<Poker> list) {
        for (Poker poker : list) {
            poker.setVisible(false);
        }
    }
    /**
     * 负责人：阿布
     * 功能：电脑零号出牌，左边人机
     * 参数：void
     * 返回值：void
     */
    public void computer0() {
        timeWait(1, 0);
        if (attr.time[1].getText().equals("不要") && attr.time[2].getText().equals("不要")) {
            attr.previous.clear();
        }
        ArrayList<Poker> robot = Play.robot(attr.previous, attr.playerList.get(0));
        if (robot.size() != 0) {
            attr.previous.clear();
            attr.previous.addAll(robot);
        }
        movePoker(robot, 0);
    }

    /**
     * 负责人：阿布
     * 功能：电脑一号出牌，玩家托管才会执行
     * 参数：void
     * 返回值：void
     */
    public void computer1() {
        timeWait(1, 1);
        if (attr.time[0].getText().equals("不要") && attr.time[2].getText().equals("不要")) {
            attr.previous.clear();
        }
        ArrayList<Poker> robot = Play.robot(attr.previous, attr.playerList.get(1));
        if (robot.size() != 0) {
            attr.previous.clear();
            attr.previous.addAll(robot);
        }
        movePoker(robot, 1);
    }

    /**
     * 负责人：阿布
     * 功能：电脑二号出牌，右边人机
     * 参数：void
     * 返回值：void
     */
    public void computer2() {
        timeWait(1, 2);
        if (attr.time[0].getText().equals("不要") && attr.time[1].getText().equals("不要")) {
            attr.previous.clear();
        }
        ArrayList<Poker> robot = Play.robot(attr.previous, attr.playerList.get(2));
        if (robot.size() != 0) {
            attr.previous.clear();
            attr.previous.addAll(robot);
        }
        movePoker(robot, 2);
    }

    /**
     * 负责人：阿布
     * 功能：将要出的牌从牌堆中移动到出牌区，根据玩家不同，单独设计
     * 参数： list   要移动的牌组     role 哪个玩家移动
     * 返回值：void
     */
    public void movePoker(ArrayList<Poker> list, int role) {
        // 定位出牌
        attr.currentList.get(role).clear();
        if (list.size() != 0) {
            attr.previous.clear();
            attr.previous.addAll(list);
        }
        attr.playerList.get(role).removeAll(list);
        if (list.size() > 0) {
            Point point = new Point();
            if (role == 0)
                point.x = 200;
            if (role == 2)
                point.x = 550;
            if (role == 1) {
                point.x = (770 / 2) - (attr.currentList.get(1).size() + 1) * 15 / 2;
            }
            point.y = (400 / 2) - (list.size() + 1) * 15 / 2; // 屏幕中部
            ArrayList<Poker> temp = new ArrayList<>();
            // 将要出的牌在手牌里面找出来
            for (Poker poker : attr.playerList.get(role)) {
                for (Poker poker1 : list) {
                    if (poker1.getName().equals(poker.getName())) {
                        temp.add(poker);
                    }
                }
            }
            for (Poker poker : temp) {
                poker.setCanClick(false);
                move(poker, poker.getLocation(), point);
                point.y += 15;
                gameJFrame.container.setComponentZOrder(poker, 0);
                attr.currentList.get(role).add(poker);
                attr.playerList.get(role).remove(poker);
            }
            rePosition(gameJFrame, attr.playerList.get(role), role);
        } else {
            attr.time[role].setVisible(true);
            attr.time[role].setText("不要");
        }
        for (Poker poker : attr.currentList.get(role))
            poker.turnFront();
    }

    /**
     * 负责人：阿布
     * 功能：移动牌，带动画
     *      利用线程，每5毫秒移动一次
     * 参数：poker: 将要移动的牌    from: 牌当前的位置     to: 牌要移动到的位置
     * 返回值：void
     */
    public static void move(Poker poker, Point from, Point to) {
        if (to.x != from.x) {
            double k = (1.0) * (to.y - from.y) / (to.x - from.x);
            double b = to.y - to.x * k;
            int flag;
            if (from.x < to.x)
                flag = 20;
            else {
                flag = -20;
            }
            for (int i = from.x; Math.abs(i - to.x) > 20; i += flag) {
                double y = k * i + b;

                poker.setLocation(i, (int) y);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        poker.setLocation(to);
    }

    /**
     * 负责人：阿布
     * 功能：给传进来的牌组排序
     * 参数： list:  需要排序的牌
     * 返回值：void
     */
    public static void order(ArrayList<Poker> list) {
        //此时可以改为lambda表达式
        list.sort((o1, o2) -> {
            //获取花色
            //1-黑桃 2-红桃 3-梅花 4-方块 5-大小王 (1-♠ 2-♥ 3-♣ 4-♦ 5-王)
            int a1 = Integer.parseInt(o1.getName().substring(0, 1));
            int a2 = Integer.parseInt(o2.getName().substring(0, 1));

            //获取牌上的数字,用于排序
            //3-3 ... 11-J 12-Q 13-K 14-A 15-2
            int b1 = Integer.parseInt(o1.getName().substring(2));
            int b2 = Integer.parseInt(o2.getName().substring(2));

            //额外计算大小王
            if (a1 == 5) {
                b1 += 100;
            }
            if (a2 == 5) {
                b2 += 100;
            }

            //倒序排列
            int flag = b2 - b1;

            //如果牌的价值一样，则按照花色排序
            if (flag == 0) {
                return a2 - a1;
            } else {
                return flag;
            }
        });
    }

    /**
     * 负责人：阿布
     * 功能：重新按需摆放牌
     *      玩家0(左边的玩家):
     *      玩家1(中间的玩家):
     *      玩家2(右边的玩家):
     * 参数：m: 页面容器  list:需要重新摆放的牌   flag: 第几个玩家需要重新摆放牌
     * 返回值：void
     */
    public static void rePosition(GameJFrame m, ArrayList<Poker> list, int flag) {
        Point p = new Point();
        if (flag == 0) {
            p.x = 50;
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2;
        }
        if (flag == 1) {
            p.x = (800 / 2) - (list.size() + 1) * 21 / 2;
            p.y = 450;
        }
        if (flag == 2) {
            p.x = 700;
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2;
        }
        for (Poker poker : list) {
            PlayerOperation.move(poker, poker.getLocation(), p);
            m.container.setComponentZOrder(poker, 0);
            if (flag == 1)
                p.x += 21;
            else
                p.y += 15;
        }
    }

    /**
     * 负责人：阿布
     * 功能：判断是否胜利，根据传进来的手牌是否为空判断
     * 参数：pokers: 当前玩家的手牌集合
     * 返回值：boolean true表示胜利  false表示失败
     */
    public boolean isWin() {
        for (int i = 0; i < 3; i++) {
            if (attr.playerList.get(i).size() == 0) {
                String s;
                if (i == 1) {
                    s = "恭喜你，胜利了!";
                } else {
                    s = "恭喜电脑" + i + "胜利了!";
                }
                for (int j = 0; j < attr.playerList.get((i + 1) % 3).size(); j++)
                    attr.playerList.get((i + 1) % 3).get(j).turnFront();
                for (int j = 0; j < attr.playerList.get((i + 2) % 3).size(); j++)
                    attr.playerList.get((i + 2) % 3).get(j).turnFront();
                message = new Message(gameJFrame, s);
                message.setVisible(true);
                message.button1.setVisible(true);
                message.button2.setVisible(true);
                message.jPanel.setVisible(true);
                return true;
            }
        }
        return false;
    }
}
