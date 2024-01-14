package domain;

import view.GameJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Poker extends JLabel implements MouseListener {
    //游戏的主界面
    GameJFrame gameJFrame;
    // ♠  ♥ ♣ ♦
    // 牌的名字，例如1-3，表示♠3 - 14 15   5-1 小王  5-2 大王
    String name;
    // 表示正反面
    boolean up;
    // 表示是否可以点击
    boolean canClick = false;
    // 表示目前该牌是否被点击选择了
    boolean clicked = false;


    /**
     * 构造器
     * 负责人：
     * 功能：初始化扑克
     * 参数：m: 表示扑克所处的容器
     *      name: 扑克的名字
     *      up: 扑克是否被选中
     * 返回值：void
     */
    public Poker(GameJFrame m, String name, boolean up) {
        this.gameJFrame = m;
        this.name = name;
        this.up = up;
        //判断当前的牌是显示正面还是背面
        if (this.up){
            this.turnFront();
        }else {
            this.turnRear();
        }
        //设置牌的宽高大小
        this.setSize(71, 96);
        //把牌显示出来
        this.setVisible(true);
        //给每一张牌添加鼠标监听
        this.addMouseListener(this);
    }

    public Poker(String name) {
        this.name = name;
    }

    /**
     *  负责人：
     *  功能：将扑克牌设置成正面，显示当前牌，并将up(表示正反面的熟悉)设为true
     *  参数：void
     *  返回值：void
     */
    public void turnFront() {
        this.setIcon(new ImageIcon("image\\poker\\" + name + ".png"));
        this.up = true;
    }

    /**
     *  负责人：
     *  功能：将扑克牌设置成反面，显示反面的牌，并将up(表示正反面的熟悉)设为false
     *  参数：void
     *  返回值：void
     */
    public void turnRear() {
        this.setIcon(new ImageIcon("image\\poker\\rear.png"));
        this.up = false;
    }

    /**
     * 负责人：阿布
     * 功能：切换牌的选中和未选中
     * 参数：void
     * 返回值：void
     */
    public void movePoker() {
        if (canClick) {
            Point from = this.getLocation();
            int step;
            if (clicked){
                step = 20;
            }else {
                step = -20;
            }
            clicked = !clicked;
            Point to = new Point(from.x, from.y + step);
            this.setLocation(to);
        }
    }

    /**
     *  负责人：
     *  功能：点击扑克牌，选择该牌(扑克牌向上移动20像素)或者退选该牌(扑克牌向下移动20像素)
     *      将扑克牌的clicked置反
     *  参数：e 事件点击元素
     *  返回值：void
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        movePoker();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public void setGameJFrame(GameJFrame gameJFrame) {
        this.gameJFrame = gameJFrame;
    }

    public GameJFrame getGameJFrame() {
        return gameJFrame;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    @Override
    public String toString() {
        return "Poker{" +
                "gameJFrame=" + gameJFrame +
                ", name='" + name + '\'' +
                ", up=" + up +
                ", canClick=" + canClick +
                ", clicked=" + clicked +
                '}';
    }
}
