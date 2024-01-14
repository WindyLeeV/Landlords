package view;

import domain.Poker;
import service.play.Play;
import service.PlayerOperation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class GameJFrame extends JFrame implements ActionListener {
    //获取界面中的隐藏容器
    //现在统一获取了，后面直接用就可以了
    public Container container = null;

    // 用来统计是否为无限制出牌，每不要一次，count++，每出牌一次，将count设为0，count == 2 表示无限制出牌
    public int count = 2;

    // 托管按钮
    JButton trustRobot = new JButton(new ImageIcon("image\\robot1.png"));

    //牌盒，装所有的牌
    public ArrayList<Poker> pokerList = new ArrayList<>();


    //用户操作，涉及多线程的知识
    PlayerOperation po;


    // 公共属性类
    CommonAttribute attr = CommonAttribute.getCommonAttribute();
    /**
     * 构造器
     * 负责人：旅箔
     * 功能：初始化页面
     *      设置图标
     *      初始化界面  initJFrame();
     *      展示组件  initView();
     *      初始化牌  new Thread(this::initCard).start();
     *      出牌前的准备  initGame();
     * 参数：void
     * 返回值：void
     */
    public GameJFrame() {
        //设置图标
        setIconImage(Toolkit.getDefaultToolkit().getImage("image\\dizhu.png"));
        //设置界面
        initJFrame();
        //添加组件
        initView();
        //界面显示出来
        //先展示界面再发牌，因为发牌里面有动画，界面不展示出来，动画无法展示
        this.setVisible(true);
        //初始化牌
        //准备牌，洗牌，发牌
        new Thread(this::initCard).start();

        //打牌之前的准备工作
        //展示抢地主和不抢地主两个按钮并且再创建三个集合用来装三个玩家准备要出的牌
        initGame();
    }

    /**
     * 负责人：旅箔
     * 功能：设置界面大小、标题、关闭模式、页面位置、背景图片等
     * 参数：void
     * 返回值：void
     */
    public void initJFrame() {
        //设置标题
        this.setTitle("斗地主");
        //设置大小
        this.setSize(830, 620);
        //设置关闭模式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口无法进行调节
        this.setResizable(false);
        //界面居中
        this.setLocationRelativeTo(null);
        //获取界面中的隐藏容器，以后直接用无需再次调用方法获取了
        container = this.getContentPane();
        //取消内部默认的居中放置
        container.setLayout(null);
        //设置背景图片
        JLabel background = new JLabel(new ImageIcon("image\\bg.png"));
        background.setBounds(0, 0, 830, 620);
        container.add(background);
    }

    /**
     * 负责人：旅箔
     * 功能：初始化界面
     *      创建抢地主和不抢按钮、设置位置、添加点击事件、添加到landlord数组中管理、添加到页面中
     *      创建出牌和不要按钮、设置位置、添加点击事件、添加到publishCard数组中管理、添加到页面中
     *      创建三个玩家前方的提示文字：倒计时
     *      创建地主图标
     * 参数：void
     * 返回值：void
     */
    public void initView() {
        //创建抢地主的按钮
        JButton robBut = new JButton("抢地主");
        //设置位置
        robBut.setBounds(320, 400, 75, 20);
        //添加点击事件
        robBut.addActionListener(this);
        //设置隐藏
        robBut.setVisible(false);
        //添加到数组中统一管理
        attr.landlord[0] = robBut;
        //添加到界面中
        container.add(robBut);
        // 将按钮置于最顶层
        container.setComponentZOrder(robBut, 0);

        //创建不抢的按钮
        JButton noBut = new JButton("不     抢");
        //设置位置
        noBut.setBounds(420, 400, 75, 20);
        //添加点击事件
        noBut.addActionListener(this);
        //设置隐藏
        noBut.setVisible(false);
        //添加到数组中统一管理
        attr.landlord[1] = noBut;
        //添加到界面中
        container.add(noBut);
        // 将按钮置于最顶层
        container.setComponentZOrder(noBut, 0);

        //创建出牌的按钮
        JButton outCardBut = new JButton("出牌");
        outCardBut.setBounds(280, 400, 60, 20);
        outCardBut.addActionListener(this);
        outCardBut.setVisible(false);
        attr.publishCard[0] = outCardBut;
        container.add(outCardBut);
        container.setComponentZOrder(outCardBut, 0);

        // 创建提示按钮
        JButton prompt = new JButton("提示");
        prompt.setBounds(370, 400, 60, 20);
        prompt.addActionListener(this);
        prompt.setVisible(false);
        attr.publishCard[2] = prompt;
        container.add(prompt);
        container.setComponentZOrder(prompt, 0);

        //创建不要的按钮
        JButton noCardBut = new JButton("不要");
        noCardBut.setBounds(460, 400, 60, 20);
        noCardBut.addActionListener(this);
        noCardBut.setVisible(false);
        attr.publishCard[1] = noCardBut;
        container.add(noCardBut);
        container.setComponentZOrder(noCardBut, 0);


        //创建三个玩家前方的提示文字：倒计时
        //每个玩家一个
        //左边的电脑玩家是0
        //中间的自己是1
        //右边的电脑玩家是2
        for (int i = 0; i < 3; i++) {
            attr.time[i] = new JTextField("倒计时:");
            attr.time[i].setEditable(false);
            attr.time[i].setVisible(false);
            container.add(attr.time[i]);
            container.setComponentZOrder(attr.time[i], 0);
        }
        attr.time[0].setBounds(140, 230, 60, 20);
        attr.time[1].setBounds(374, 360, 60, 20);
        attr.time[2].setBounds(620, 230, 60, 20);


        //创建地主图标
        attr.dizhu = new JLabel(new ImageIcon("image\\dizhu.png"));
        attr.dizhu.setVisible(false);
        attr.dizhu.setSize(40, 40);
        container.add(attr.dizhu);
        container.setComponentZOrder(attr.dizhu, 0);

        // 设置托管按钮
        if (attr.robot) {
            trustRobot.setIcon(new ImageIcon("image\\robot2.png"));
        }
        trustRobot.setBounds(680, 470, 60, 60);
        trustRobot.setVisible(true);
        trustRobot.addActionListener(this);
        container.add(trustRobot);
        container.setComponentZOrder(trustRobot, 0);


    }

    /**
     * 负责人：旅箔
     * 功能：初始化牌
     *      把所有的牌，包括大小王都添加到牌盒pokerList当中
     *      洗牌 Collections.shuffle()方法
     *      拿出三张牌放入lordList作为地主底牌，并在后续牌中随机选中一张作为地主牌
     *      创建三个集合用来装三个玩家的牌
     *      剩余牌平均发给三个玩家(装进三个集合中)
     *      将三个集合放入playerList里面
     *      给牌排序，调用service里面的方法
     *      牌发完后显示抢地主按钮
     * 参数：void
     * 返回值：void
     */
    public void initCard() {
        //准备牌
        //把所有的牌，包括大小王都添加到牌盒cardList当中
        initPokers();

        //洗牌
        shuffle(pokerList);

        // 发牌
        dealPokers();

        //展示抢地主和不抢地主两个按钮
        initLandlord();

        //展示自己前面的倒计时文本
        attr.time[1].setVisible(true);
        //倒计时30秒
        po = new PlayerOperation(this, 30);
        //开启倒计时
        po.start();
    }

    /**
     * 负责人：旅箔
     * 功能：初始化一副牌
     * 参数：void
     * 返回值：void
     */
    public void initPokers() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 3; j <= 15; j++) {
                if ((i == 5) && (j >= 5)) {
                    break;
                } else {
                    Poker poker = new Poker(this, i + "-" + j, false);
                    poker.setLocation(350, 150);

                    pokerList.add(poker);
                    container.add(poker);
                }
            }
        }
    }

    /**
     * 负责人：旅箔
     * 功能：洗牌
     * 参数：list:  需要打乱顺序的集合
     * 返回值：void
     */
    public void shuffle(ArrayList<Poker> list) {
        Collections.shuffle(list);
    }

    /**
     * 负责人：旅箔
     * 功能：发牌
     *      创建三个集合装玩家的牌
     *      遍历牌堆，发放三张地主牌和三个玩家的手牌
     *      发完后给牌排个序
     * 参数：void
     * 返回值void
     */
    public void dealPokers() {
        //创建三个集合用来装三个玩家的牌，并把三个小集合放到大集合中方便管理
        ArrayList<Poker> player0 = new ArrayList<>();
        ArrayList<Poker> player1 = new ArrayList<>();
        ArrayList<Poker> player2 = new ArrayList<>();

        // 发牌
        for (int i = 0; i < pokerList.size(); i++) {
            //获取当前遍历的牌
            Poker poker = pokerList.get(i);

            //发三张底牌
            if (i <= 2) {
                container.setComponentZOrder(poker, 0);
                //移动牌
                PlayerOperation.move(poker, poker.getLocation(), new Point(310 + (70 * i), 5));
                //把底牌添加到集合中
                attr.lordList.add(poker);
                continue;
            }
            //给三个玩家发牌
            if (i % 3 == 0) {
                //给左边的电脑发牌
                PlayerOperation.move(poker, poker.getLocation(), new Point(50, 60 + i * 5));
                player0.add(poker);
            } else if (i % 3 == 1) {
                //给中间的自己发牌
                PlayerOperation.move(poker, poker.getLocation(), new Point(180 + i * 7, 450));
                player1.add(poker);
                //把自己的牌展示正面
                poker.turnFront();

            } else {
                //给右边的电脑发牌
                PlayerOperation.move(poker, poker.getLocation(), new Point(700, 60 + i * 5));
                player2.add(poker);
            }
            //把三个装着牌的小集合放到大集合中方便管理
            attr.playerList.add(player0);
            attr.playerList.add(player1);
            attr.playerList.add(player2);

            //把当前的牌至于最顶端，这样就会有牌依次错开且叠起来的效果
            container.setComponentZOrder(poker, 0);

            //给牌排序
            for (int j = 0; j < 3; j++) {
                //排序
                PlayerOperation.order(attr.playerList.get(j));
                //重新摆放顺序
                PlayerOperation.rePosition(this, attr.playerList.get(j), j);
            }

        }
    }

    /**
     * 负责人：旅箔
     * 功能：初始化抢地主和不抢按钮
     * 参数：void
     * 返回值：void
     */
    public void initLandlord() {
        attr.landlord[0].setVisible(true);
        attr.landlord[1].setVisible(true);
    }

    /**
     * 负责人：旅箔
     * 功能：初始化三个集合用于装玩家出的牌，放入currentList
     *  参数：void
     *  返回值：void
     */
    private void initGame() {
        //创建三个集合用来装三个玩家准备要出的牌
        for (int i = 0; i < 3; i++) {
            ArrayList<Poker> list = new ArrayList<>();
            //添加到大集合中方便管理
            attr.currentList.add(list);
        }
    }


    /**
     * 负责人：旅箔
     * 功能：获取点击元素，根据获取的点击元素进行操作，
     *      点击抢地主，显示框显示抢地主
     *      点击抢地主，显示框显示不抢地主
     *      点击不要按钮，显示卡显示不要
     *      点击托管按钮，将robot取反
     *      点击出牌按钮，进行出牌逻辑判断
     * 参数：e 点击事件元素
     * 返回值：void
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == trustRobot) {
            if (!attr.robot) {
                attr.robot = true;
                trustRobot.setIcon(new ImageIcon("image\\robot2.png"));
            } else {
                attr.robot = false;
                trustRobot.setIcon(new ImageIcon("image\\robot1.png"));
            }
        }
        // 点击提示按钮回调
        if (e.getSource() == attr.publishCard[2]) {
            // 遍历手牌
            ArrayList<Poker> robot = Play.robot(attr.previous, attr.playerList.get(1));
            for (Poker poker : attr.playerList.get(1)) {
                if (poker.isClicked()) {
                    poker.movePoker();
                }
                for (Poker r : robot) {
                    // 找出人机出牌返回的牌，将其设置为被点击起来的状态
                    if (r.getName().equals(poker.getName())) {
                        if (!poker.isClicked()) {
                            poker.movePoker();
                            poker.setClicked(true);
                        }
                    }
                }
            }
        }
        if (e.getSource() == attr.landlord[0]) {
            //点击抢地主
            attr.time[1].setText("抢地主");
            attr.isRun = false;
        } else if (e.getSource() == attr.landlord[1]) {
            //点击不抢
            attr.time[1].setText("不抢");
            attr.isRun = false;
        } else if (e.getSource() == attr.publishCard[1]) {
            //点击不要
            this.attr.nextPlayer = true;
            attr.currentList.get(1).clear();
            attr.time[1].setText("不要");
            for (Poker poker : attr.playerList.get(1)) {
                if (poker.isClicked()) {
                    poker.movePoker();
                }
            }
        } else if (e.getSource() == attr.publishCard[0]) {
            //点击出牌

            //创建一个临时的集合，用来存放当前要出的牌
            ArrayList<Poker> c = new ArrayList<>();
            //获取中自己手上所有的牌
            ArrayList<Poker> user = attr.playerList.get(1);
            //遍历手上的牌，把要出的牌都放到临时集合中
            for (Poker use : user) {
                if (use.isClicked()) {
                    c.add(use);
                }
            }

            // 判断是否能出牌，可以出牌则原样返回，不能则返回空的集合
            ArrayList<Poker> player = Play.player(attr.previous, c);
            if (attr.time[0].getText().equals("不要") && attr.time[2].getText().equals("不要")) {
                attr.previous.clear();
            }
            // 出牌
            if (player.size() != 0) {
                //把当前要出的牌，放到大集合中统一管理
                attr.currentList.set(1, c);
                attr.previous.clear();
                attr.previous.addAll(c);
                //在手上的牌中，去掉已经出掉的牌
                user.removeAll(c);

                //计算坐标并移动牌
                //移动的目的是要出的牌移动到上方
                Point point = new Point();
                point.x = (770 / 2) - (c.size() + 1) * 15 / 2;
                point.y = 300;
                for (Poker poker : c) {
                    PlayerOperation.move(poker, poker.getLocation(), point);
                    point.x += 15;
                }

                //重新摆放剩余的牌
                PlayerOperation.rePosition(this,user, 1);
                //隐藏文本提示
                attr.time[1].setVisible(false);
                //下一个玩家可玩
                this.attr.nextPlayer = true;
            }
        }
    }
}
