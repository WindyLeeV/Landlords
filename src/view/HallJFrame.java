package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HallJFrame extends JFrame implements MouseListener {

    JButton machine = new JButton("人机模式");
    JButton players = new JButton("游戏规则");
    JButton setting = new JButton("设置");
    JButton exit = new JButton("退出游戏");

    // 游戏规则
    String[] s = {"游戏规则\n", "1）游戏人数：3人(玩家自己和两个人机)。\n", "2）游戏牌数：一共54张，带大小王，一人17张，三张作为底牌，地主未确定千万家不能看到底牌。\n",
            "3）出牌规则：\n", "\t1.发牌：一副牌54张，带大小王，一人17张，三张作为底牌，地主未确定千万家不能看到底牌。\n", "\t2.抢地主：发完牌后进入抢地主阶段，抢地主时可以选择“抢地主”、“不抢”，直到有一位玩家选择抢地主（若玩家不抢，人机必有一位会抢地主），由此玩家（或人机）当选地主并进入出牌阶段。\n",
            "\t3.出牌：由地主先出牌，然后按逆时针依次出牌，轮到玩家跟牌时，玩家可以选择“不出”或者出比上一家打的牌。\n", "\t 4.胜负判定：地主打完牌为地主获胜，某一个农民打完牌则为所有农民获胜。\n",
            "4）牌型：\n", "\t火箭：即双王（大王和小王），最大的牌\n", "\t炸弹：四张相同的数值牌（如四个7）\n", "\t单牌：单个牌（如红桃5）\n",
            "\t对牌：数值相同的牌（如梅花4加方块4）\n", "\t三张牌：数值相同的三张牌（如3个J）\n", "\t三带一（三带二）：数值相同的三张牌+一张单牌或一对牌。例如：333+6,333+99\n",
            "\t顺子：五张或更多的连续单牌。不包括2和大小王（如34567、10JQK1）\n", "\t连对：三对或更多的连续对牌。不包括2和大小王（如334455、JJQQKK11）\n", "\t飞机不带翅膀：两个连续三张牌。不包括2点和双王（如333444）\n",
            "\t飞机带翅膀：飞机不带翅膀+同数量的单牌或对牌（如333444+7+9、333444+66+77）（飞机中如果含有炸弹，则翅膀中不能带炸弹中的牌）\n", "\t注意：当玩家所出的牌为最后一手牌时，也必须符合以上出牌规则，不能出现444555+6的牌型。\n",
            "5）牌型大小：\n", "\t1.火箭最大，可以打任意其他的牌。\n", "\t2.炸弹比火箭小，比其他的牌大。都是炸弹时按牌的分值比大小。\n", "\t3.除火箭和炸弹外，其他牌必须要牌型相同且张数相同才能比大小。\n",
            "\t4.单牌按分值比大小，依次是大王>小王>2>1>K>Q>J>10>9>8>7>6>5>4>3。\n", "\t5.对牌，三张牌都是按比分值比大小。\n", "\t6.顺子按最大的一张比大小。\n", "\t7.飞机带翅膀和四带二按其中的三顺和四张部分比较大小，带的牌不影响大小。\n",
            "\t8.注意：以上牌型比较均不分花色。\n", "\t9.玩家右边的机器人按钮为托管按钮，点击切换托管状态，处于托管状态下，将由机器人帮你出牌"};

    // 公共属性类
    CommonAttribute attr = CommonAttribute.getCommonAttribute();

    public HallJFrame() {
        init();
    }

    /**
     * 负责人：阿布
     * 功能：初始化页面
     *      初始化界面  initJFrame();
     *      添加组件   initView();
     * 参数：void
     * 返回值：void
     */
    public void init() {
        //初始化界面
        initJFrame();
        //在这个界面中添加内容
        initView();
        //让当前界面显示出来
        this.setVisible(true);
    }

    /**
     * 负责人：阿布
     * 功能：初始化视图、组件等，参考原型图
     * 参数：void
     * 返回值：void
     */
    private void initView() {

        // 人机模式按钮
        // 设置凸起
        machine.setBorder(BorderFactory.createRaisedBevelBorder());
        // 设置字体大小
        machine.setFont(new java.awt.Font("宋体",  Font.BOLD,  24));
        // 设置背景颜色
        machine.setBackground(Color.orange);
        // 设置大小及位置
        machine.setBounds(252, 60, 128, 47);
        //去掉按钮文字周围的焦点框
        machine.setFocusPainted(false);
        machine.addMouseListener(this);
        this.getContentPane().add(machine);


        // 多人模式按钮
        // 设置凸起
        players.setBorder(BorderFactory.createRaisedBevelBorder());
        // 设置字体大小
        players.setFont(new java.awt.Font("宋体",  Font.BOLD,  24));
        // 设置背景颜色
        players.setBackground(Color.orange);
        // 设置大小及位置
        players.setBounds(252, 120, 128, 47);
        //去掉按钮文字周围的焦点框
        players.setFocusPainted(false);
        players.addMouseListener(this);
        this.getContentPane().add(players);


        // 设置按钮
        setting.setBorder(BorderFactory.createRaisedBevelBorder());
        // 设置字体大小
        setting.setFont(new java.awt.Font("宋体",  Font.BOLD,  24));
        // 设置背景颜色
        setting.setBackground(Color.orange);
        // 设置大小及位置
        setting.setBounds(252, 180, 128, 47);
        //去掉按钮文字周围的焦点框
        setting.setFocusPainted(false);
        setting.addMouseListener(this);
        this.getContentPane().add(setting);


        // 退出游戏按钮
        exit.setBorder(BorderFactory.createRaisedBevelBorder());
        // 设置字体大小
        exit.setFont(new java.awt.Font("宋体",  Font.BOLD,  24));
        // 设置背景颜色
        exit.setBackground(Color.orange);
        // 设置大小及位置
        exit.setBounds(252, 240, 128, 47);
        //去掉按钮文字周围的焦点框
        exit.setFocusPainted(false);
        exit.addMouseListener(this);
        this.getContentPane().add(exit);

        // 添加背景图片
        JLabel background = new JLabel(new ImageIcon("image\\login\\background.png"));
        background.setBounds(0, 0, 633, 423);
        this.getContentPane().add(background);

        // 设置窗口图标
        setIconImage(Toolkit.getDefaultToolkit().getImage("image\\dizhu.png"));
    }

    /**
     * 负责人：阿布
     * 功能：初始化布局，页面大小、标题关闭模式、页面位置等
     * 参数：void
     * 返回值：void
     */
    private void initJFrame() {
        this.setSize(633, 423);//设置宽高
        this.setTitle("斗地主游戏大厅");//设置标题
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.setLocationRelativeTo(null);//居中
        this.setAlwaysOnTop(true);//置顶
        this.setLayout(null);//取消内部默认布局
        if (attr.musicSwitch) {
            attr.setMusic();
        } else {
            attr.stopMusic();
        }
    }


    /**
     * 负责人：阿布
     * 功能：鼠标点击回调
     *      点击人机模式，进入人机游戏界面
     *      点击多人模式，弹出弹框
     *      点击设置，进入设置页面
     *      点击退出游戏，退出程序
     * 参数： e： 鼠标点击事件的元素
     * 返回值：void
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == machine) {
            //关闭当前登录界面
            this.dispose();
            //打开游戏的主界面
            new GameJFrame();
        } else if (e.getSource() == players) {
            // 弹框提示
            showJDialog(s);
        } else if (e.getSource() == setting) {
            //关闭当前登录界面
            this.setVisible(false);
            //打开游戏的主界面
            new SettingJFrame();
        } else if (e.getSource() == exit) {
            // 关闭程序
            System.exit(0);
        }
    }

    /**
     * 负责人：阿布
     * 功能：弹框
     * 参数： content： 弹框显示的内容
     * 返回值：void
     */
    public void showJDialog(String[] content) {
        //创建一个弹框对象
        JDialog jDialog = new JDialog();
        //给弹框设置大小
        jDialog.setSize(1250, 800);
        //让弹框置顶
        jDialog.setAlwaysOnTop(true);
        //让弹框居中
        jDialog.setLocationRelativeTo(null);
        //弹框不关闭永远无法操作下面的界面
        jDialog.setModal(true);

        // 创建文本区域组件
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("宋体",  Font.BOLD,  24));   // 设置字体
        for (String s : content) {
            textArea.append(s);
            textArea.setLineWrap(true);// 自动换行
        }

        // 创建滚动面板, 指定滚动显示的视图组件(textArea), 垂直滚动条一直显示, 水平滚动条从不显示
        JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //让弹框展示出来
        jDialog.add(scrollPane);
        jDialog.setVisible(true);
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
}
