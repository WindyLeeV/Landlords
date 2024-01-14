package service.judge;

import domain.Model;
import domain.Poker;
import domain.PokerType;
import service.ai.Robot1;
import service.rule.GameRules;

import java.util.ArrayList;

// 限制出牌
public class Limit {

    /**
     * 负责人：巴别
     * 功能：限制出牌的出牌策略(机器人出牌策略)
     *      判断需要跟的牌的牌型  GameRules.judge()
     *      找出是否有比previous大的牌组(找出符合牌型的牌组，然后利用 JudgePokers() 进行判断)
     *      将牌组加入ArrayList集合，无匹配的牌组则返回一个空的ArrayList集合
     * 参数：previous  表示需要跟的牌   pokers: 表示当前机器人的手牌
     * 返回值：ArrayList<Poker>  空表示无牌可出
     */
    public static ArrayList<Poker> robotLimitPokers(ArrayList<Poker> previous, ArrayList<Poker> pokers) {
        // 判断要跟的牌的牌型
        PokerType type = GameRules.judge(previous);
        Model model = new Model();
        // 第一次获取要出的牌，有bug
        ArrayList<Poker> robot = Robot1.robot(pokers, model, type, true);
        if (robot.size() == 0) {
            // 无同类型牌组
            robot = Robot1.robot(pokers, model, PokerType.c4, false);
            return robot;
        }
        int i = 0;
        while (!JudgePokers(previous, robot, type)) {
            // 再次获取要出的牌
            robot = Robot1.robot(pokers, model, type, false);
            if (robot.size() == 0) {
                // 无同类型牌组
                robot = Robot1.robot(pokers, model, PokerType.c4, false);
                return robot;
            }
        }
        return robot;
    }

    /**
     * 负责人：巴别
     * 功能： 玩家跟牌判断
     *      根据 previous 判断 list 是否能出
     *      判断牌型是否相同
     *      判断list是否比previous大
     * 参数：previous  表示需要跟的牌   list 表示要出的牌
     * 返回值：boolean   ture表示能出   false表示不能出
     */
    public static boolean limitPokers(ArrayList<Poker> previous,ArrayList<Poker> list) {
        PokerType preType = GameRules.judge(previous);
        PokerType listType = GameRules.judge(list);
        // list牌型不符合规则，直接返回false
        if (listType == PokerType.c0) {
            return false;
        }
        PokerType type;
        if (preType == listType || listType == PokerType.c4) {
            if (listType == PokerType.c4) {
            }
            type = preType;
            return JudgePokers(previous, list, type);
        }
        return false;
    }

    /**
     * 负责人：巴别
     * 功能：比较now是否比old大
     * 参数：old:  要跟的牌    now:  要出的牌   type 牌组的类型
     * 返回值：boolean   true表示now大于old，即当前牌能出
     */
    public static boolean JudgePokers(ArrayList<Poker> old, ArrayList<Poker> now, PokerType type) {
        boolean flag = false;
        // 要出的牌是炸弹，且要跟的牌不是炸弹，可直接出
        if (GameRules.judge(now) == PokerType.c4 && GameRules.judge(old) != PokerType.c4) {
            return true;
        }
        switch(type) {
            case c1:
            case c2:
            case c3:
                // 单牌、对子、三不带比较大小
                flag = Robot1.getValue(now.get(0)) > Robot1.getValue(old.get(0));
                break;
            case c31:
            case c32:
            case c411:
            case c111222:
            case c11122234:
                // 三带一、三带二、四代一、飞机不带翅膀、飞机带两单比较大小
                if (now.size() <= 3 || old.size() <= 3) {
                    flag = false;
                } else {
                    flag = Robot1.getValue(now.get(2)) > Robot1.getValue(old.get(2));
                }
                break;
            case c4:
                // 炸弹比较
                // 要跟的牌是王炸
                if (old.size() == 2) {
                    flag = false;
                    break;
                }
                if (now.size() == 2) {
                    flag = true;
                    break;
                }
                if (now.size() != 4) {
                    break;
                }
                // 普通炸弹
                flag = Robot1.getValue(now.get(0)) > Robot1.getValue(old.get(0));
                break;
            case c422:
                // 四带二比较
                Poker ol;
                Poker no;
                if (now.size() != 8) {
                    break;
                }
                if (Robot1.getValue(old.get(0)) == Robot1.getValue(old.get(3))) {
                    ol = old.get(0);
                } else {
                    ol = old.get(4);
                }
                if (Robot1.getValue(now.get(0)) == Robot1.getValue(old.get(3))) {
                    no = old.get(0);
                } else {
                    no = old.get(4);
                }
                flag = Robot1.getValue(no) > Robot1.getValue(ol);
                break;
            case c123:
            case c112233:
                // 顺子、连对比较
                if (old.size() != now.size()) {
                    flag = false;
                    break;
                }
                flag = Robot1.getValue(now.get(0)) > Robot1.getValue(old.get(0));
                break;
            case c1112223344:
                // 飞机带两翅膀比较
                Poker ol1;
                Poker no1;
                if (now.size() != 10) {
                    break;
                }
                if (Robot1.getValue(old.get(2)) == Robot1.getValue(old.get(5))) {
                    ol1 = old.get(2);
                } else {
                    ol1 = old.get(4);
                }
                if (Robot1.getValue(now.get(2)) == Robot1.getValue(now.get(5))) {
                    no1 = now.get(2);
                } else {
                    no1 = now.get(4);
                }
                flag = Robot1.getValue(no1) > Robot1.getValue(ol1);
                break;
        }
        return flag;
    }
}
