package view;

import domain.Poker;

import javax.swing.*;
import java.util.ArrayList;

public class CommonAttribute extends JFrame {

    //管理抢地主和不抢两个按钮
    public JButton[] landlord = new JButton[2];

    //管理出牌和不要两个按钮
    public JButton[] publishCard = new JButton[3];

    //游戏界面中地主的图标
    public JLabel dizhu;

    //表示第几位玩家是地主
    public int dizhuFlag;

    // 表示第几位玩家出牌
    public int trun;

    // 是否托管
    public boolean robot = false;

    // 是否开启背景音乐
    public boolean musicSwitch = true;

    public boolean first = true;


    // 记录要跟的牌
    public ArrayList<Poker> previous = new ArrayList<>();

    //集合嵌套集合   大集合中有三个小集合   小集合中装着每一个玩家当前要出的牌
    //0索引：左边的电脑玩家
    //1索引：中间的自己
    //2索引：右边的电脑玩家
    public ArrayList<ArrayList<Poker>> currentList = new ArrayList<>();

    //集合嵌套集合   大集合中有三个小集合   小集合中装着每一个玩家的牌
    //0索引：左边的电脑玩家
    //1索引：中间的自己
    //2索引：右边的电脑玩家
    public ArrayList<ArrayList<Poker>> playerList = new ArrayList<>();

    //地主底牌
    public ArrayList<Poker> lordList = new ArrayList<>();


    //三个玩家前方的文本提示
    //0索引：左边的电脑玩家
    //1索引：中间的自己
    //2索引：右边的电脑玩家
    public JTextField[] time = new JTextField[3];

    //下一位玩家出牌
    public boolean nextPlayer = false;

    //是否能走
    public boolean isRun = true;

    // 单例
    private static final CommonAttribute commonAttribute = new CommonAttribute();

    /**
     * 负责人：阿布
     * 功能：私有构造器
     */
    private CommonAttribute() {

    }

    /**
     * 负责人：阿布
     * 功能：但会一个CommonAttribute实例，单例设计模式，饿汉式
     * 参数：void
     * 返回值：返回一个CommonAttribute类，无论谁何时调用，返回的都是同一个CommonAttribute
     */
    public static CommonAttribute getCommonAttribute() {
        return commonAttribute;
    }

    /**
     * 负责人：阿布
     * 功能：点击再来一局，初始化数据
     * 参数：void
     * 返回值：void
     */
    public void init() {
        dizhu = new JLabel();
        dizhuFlag = 0;
        trun = 0;
        previous.clear();
        currentList.clear();
        playerList.clear();
        lordList.clear();
        nextPlayer = false;
        isRun = true;
    }


    /**
     * 负责人：旅箔
     * 功能：设置背景音乐
     * 参数：void
     * 返回值：void
     */
    public void setMusic() {
        GlobalSound ins = GlobalSound.getInstance();
        ins.Start();
    }

    /**
     * 负责人：旅箔
     * 功能：关闭背景音乐
     * 参数：void
     * 返回值：void
     */
    public void stopMusic() {
        GlobalSound ins = GlobalSound.getInstance();
        ins.Stop();
    }



}
