package view;

import javax.swing.*;
import java.awt.*;

public class Message extends JDialog {
    // 再来一局按钮
    public JButton button1 = new JButton("再来一局");
    // 返回大厅按钮
    public JButton button2 = new JButton("返回大厅");

    // 用于装两个按钮
    public JPanel jPanel = new JPanel();

    // 游戏页面
    JFrame gameJFrame;

    // 公共属性类
    CommonAttribute attr = CommonAttribute.getCommonAttribute();


    /**
     * 负责人：阿布
     * 功能： 构造器，初始化弹窗
     * 参数： jFrame：弹窗放置区域   s 弹窗内容
     */
    public Message(JFrame jFrame, String s) {
        super(jFrame,s,true);
        gameJFrame = jFrame;

        Container c=getContentPane();// 获取窗体容器，Container（容器），content（内容），pane（窗格）
        c.add(new JLabel(s), BorderLayout.CENTER);// 设置一个标签，label（标签）
        setLayout(new BorderLayout());


        setBounds(840,430,220,100);// 设置窗体坐标，x、y、长、宽，bounds（界限）

        button1.setBounds(20, 110,50, 30);

        button2.setBounds(80, 110,50, 30);

        button1.addActionListener((e) -> {
            gameJFrame.dispose();
            attr.init();
            new GameJFrame();
        });

        button2.addActionListener(e -> {
            gameJFrame.dispose();
            attr.stopMusic();
            new HallJFrame();
        });

        jPanel.add(button1);
        jPanel.add(button2);
        c.add(jPanel, BorderLayout.SOUTH);

    }
}
