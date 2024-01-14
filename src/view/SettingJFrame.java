package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SettingJFrame extends JFrame implements MouseListener {

    JButton robot = new JButton("托管");
    JButton music = new JButton("音乐");
    JButton exit = new JButton("返回大厅");

    JLabel robotImg = new JLabel(new ImageIcon("image\\switch1.png"));

    JLabel musicImg = new JLabel(new ImageIcon("image\\switch1.png"));

    // 公共属性类
    CommonAttribute attr = CommonAttribute.getCommonAttribute();

    /**
     * 负责人：旅箔
     * 功能：构造器，初始化设置页面
     * 参数：void
     */
    public SettingJFrame() {
        init();
    }

    /**
     * 负责人：旅箔
     * 功能：初始化设置界面
     *      初始化界面   initJFrame();
     *      添加组件   initView();
     * 参数：void
     * 返回值：void
     */
    private void init() {
        //初始化界面
        initJFrame();
        //在这个界面中添加内容
        initView();
        //让当前界面显示出来
        this.setVisible(true);
    }

    /**
     * 负责人：旅箔
     * 功能：初始化组件
     * 参数：void
     * 返回值：void
     */
    private void initView() {
        // 托管按钮
        // 设置凸起
        if (!attr.robot) {
            robotImg.setIcon(new ImageIcon("image\\switch2.png"));
        }
        robot.setBorder(BorderFactory.createRaisedBevelBorder());
        // 设置字体大小
        robot.setFont(new java.awt.Font("宋体",  Font.BOLD,  24));
        // 设置背景颜色
        robot.setBackground(Color.orange);
        // 设置大小及位置
        robot.setBounds(220, 80, 128, 47);
        //去掉按钮文字周围的焦点框
        robot.setFocusPainted(false);
        robot.addMouseListener(this);
        this.getContentPane().add(robot);

        // 托管状态切换组件
        robotImg.setVisible(true);
        robotImg.setBounds(368, 80, 80, 40);
        this.getContentPane().add(robotImg);

        // 音乐按钮
        if (!attr.musicSwitch) {
            musicImg.setIcon(new ImageIcon("image\\switch2.png"));
        }
        music.setBorder(BorderFactory.createRaisedBevelBorder());
        // 设置字体大小
        music.setFont(new java.awt.Font("宋体",  Font.BOLD,  24));
        // 设置背景颜色
        music.setBackground(Color.orange);
        // 设置大小及位置
        music.setBounds(220, 160, 128, 47);
        //去掉按钮文字周围的焦点框
        music.setFocusPainted(false);
        music.addMouseListener(this);
        this.getContentPane().add(music);

        // 音乐状态切换组件
        musicImg.setVisible(true);
        musicImg.setBounds(368, 160, 80, 40);
        this.getContentPane().add(musicImg);

        // 退出游戏按钮
        exit.setBorder(BorderFactory.createRaisedBevelBorder());
        // 设置字体大小
        exit.setFont(new java.awt.Font("宋体",  Font.BOLD,  24));
        // 设置背景颜色
        exit.setBackground(Color.orange);
        // 设置大小及位置
        exit.setBounds(220, 240, 192, 47);
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
     * 负责人：旅箔
     * 功能：初始化布局，设置宽高、标题、关闭没事、页面位置等
     * 参数：void
     * 返回值：void
     */
    private void initJFrame() {
        this.setSize(633, 423);//设置宽高
        this.setTitle("斗地主游戏设置");//设置标题
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.setLocationRelativeTo(null);//居中
        this.setAlwaysOnTop(true);//置顶
        this.setLayout(null);//取消内部默认布局
    }

    /**
     * 负责人：旅箔
     * 功能：鼠标点击事件回调
     *      点击托管按钮，切换托管功能
     *      点击音乐按钮，切换开关音乐
     *      点击返回大厅按钮，弹出游戏大厅
     * 参数 e： 鼠标点击事件的元素
     * 返回值：void
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == robot) {
            if (attr.robot) {
                attr.robot = false;
                robotImg.setIcon(new ImageIcon("image\\switch2.png"));
            } else {
                attr.robot = true;
                robotImg.setIcon(new ImageIcon("image\\switch1.png"));
            }
        } else if (e.getSource() == music) {
            if (attr.musicSwitch) {
                attr.stopMusic();
                attr.musicSwitch = false;
                musicImg.setIcon(new ImageIcon("image\\switch2.png"));
            } else {
                attr.setMusic();
                attr.musicSwitch = true;
                musicImg.setIcon(new ImageIcon("image\\switch1.png"));
            }
        } else if (e.getSource() == exit) {
            //关闭当前登录界面
            this.dispose();
            attr.stopMusic();
            //打开游戏的主界面
            new HallJFrame();
        }
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
