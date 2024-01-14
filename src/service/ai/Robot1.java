package service.ai;

import domain.Model;
import domain.Poker;
import domain.PokerType;
import service.PlayerOperation;

import java.util.ArrayList;

public class Robot1 {

    /**
     * 负责人：阿布
     * 功能：ai出牌逻辑  传进来的手牌是有序的，从大到小 将牌进入model里面，必须加入全名
     *      定义一个寻找牌组的顺序规则  int[]{ 4， 3， 2， 1， 5}
     *      出牌，有无限制出牌
     *      按照一个规则来找牌
     *      无限制，直接返回
     *      有限制
     * 参数： pokers  手牌堆    model  牌组    type  表示要出的牌的类型，c0表示无限制出牌  flag 表示是否是第一次传进来手牌
     * 返回值：ArrayList  要出的牌
     */
    public static ArrayList<Poker> robot(ArrayList<Poker> pokers, Model model, PokerType type, boolean flag) {
        int[] rules = new int[]{4, 3, 2, 1, 5};
        ArrayList<Poker> copyPokers = new ArrayList<>(pokers);
        if (flag) {
            // 表示第一次选牌，初始化手牌牌组,并获取一个牌组
            getModel(copyPokers, rules, model);
        }
        return choose(model, type);
    }

    /**
     * 负责人：阿布
     * 功能：返回手牌含有符合出牌规则的牌组
     *      遍历rules
     * 参数： pokers   手牌   rules  查找规则
     * 返回值： 牌组
     */
    public static void getModel(ArrayList<Poker> pokers, int[] rules, Model model) {
        for (int rule : rules) {
            showOrder(rule, pokers, model);
        }
    }

    /**
     * 负责人：阿布
     * 功能：判断走那条判断路径
     * 参数： i：规则   pokers  手牌   model  牌组
     * 返回值：void
     */
    public static void showOrder(int i, ArrayList<Poker> pokers, Model model) {
        switch (i) {
            case 1:
                getSingle(pokers, model);
                break;
            case 2:
                getTwo(pokers, model);
                getTwoTwo(model);
                break;
            case 3:
                getThree(pokers, model);
                getPlane(model);
                break;
            case 4:
                getBomb(pokers, model);
                break;
            case 5:
                get123(pokers, model);
                break;
        }
    }

    /**
     * 负责人：.(点号)
     * 功能：找单牌
     *      遍历手牌，每张牌加入 model.a1()
     * 参数： pokers  手牌  model 牌组
     * 返回值：void
     */
    private static void getSingle(ArrayList<Poker> pokers, Model model) {
        for (Poker poker : pokers) {
            model.a1.add(poker.getName());
        }
    }

    /**
     * 负责人：hh
     * 功能：找对子
     *      遍历手牌
     * 参数： pokers  手牌  model 牌组
     * 返回值：void
     */
    private static void getTwo(ArrayList<Poker> pokers, Model model) {
        // 连续2张相同
        for (int i = 0, len = pokers.size(); i < len; i++) {
            if (i + 1 < len && getValue(pokers.get(i)) == getValue(pokers.get(i + 1))) {
                String s = pokers.get(i).getName() + ",";
                s += pokers.get(i + 1).getName();
                model.a2.add(s);
                // 得到一组对子后直接往后跳一张牌
                i = i + 1;
            }
        }
    }

    /**
     * 负责人：hh
     * 功能：找连对
     * 参数： pokers  手牌  model 牌组
     * 返回值：void
     */
    private static void getTwoTwo(Model model) {
        // 从model里面的对子找
        ArrayList<String> l = model.a2;
        if (l.size() < 3)
            return;
        Integer s[] = new Integer[l.size()];
        for (int i = 0, len = l.size(); i < len; i++) {
            String[] name = l.get(i).split(",");
            s[i] = Integer.parseInt(name[0].substring(2, name[0].length()));
        }
        // s0,1,2,3,4 13,9,8,7,6
        for (int i = 0, len = l.size(); i < len; i++) {
            if (s[i] == 15) {
                continue;
            }
            int k = i;
            for (int j = i; j < len; j++) {
                if (s[i] - s[j] == j - i)
                    k = j;
            }
            if (k - i >= 2) {
                // 说明从i到k是连队
                String ss = "";
                for (int j = i; j < k; j++) {
                    ss += l.get(j) + ",";
                }
                ss += l.get(k);
                model.a112233.add(ss);
                i = k;
            }
        }
    }

    /**
     * 负责人：小白
     * 功能：找三牌
     * 参数： pokers  手牌  model 牌组
     * 返回值：void
     */
    private static void getThree(ArrayList<Poker> pokers, Model model) {
        // 连续3张相同
        for (int i = 0, len = pokers.size(); i < len; i++) {
            if (i + 2 < len && getValue(pokers.get(i)) == getValue(pokers.get(i + 2))) {
                String s = pokers.get(i).getName() + ",";
                s += pokers.get(i + 1).getName() + ",";
                s += pokers.get(i + 2).getName();
                model.a3.add(s);
                // 得到一组三不带后直接往后跳两张牌
                i = i + 2;
            }
        }
    }

    /**
     * 负责人：小白
     * 功能：找飞机不带翅膀
     * 参数： pokers  手牌  model 牌组
     * 返回值：void
     */
    private static void getPlane(Model model) {
        // 从model里面的3带找
        ArrayList<String> l = model.a3;
        if (l.size() < 2)
            return;
        Integer s[] = new Integer[l.size()];
        for (int i = 0, len = l.size(); i < len; i++) {
            String[] name = l.get(i).split(",");
            s[i] = Integer.parseInt(name[0].substring(2, name[0].length()));
        }
        for (int i = 0, len = l.size(); i < len; i++) {
            int k = i;
            for (int j = i; j < len; j++) {
                if (s[i] - s[j] == j - i)
                    k = j;
            }
            if (k != i) {
                // 说明从i到k是飞机
                String ss = "";
                for (int j = i; j < k; j++) {
                    ss += l.get(j) + ",";
                }
                ss += l.get(k);
                model.a111222.add(ss);
                i = k;
            }
        }
    }

    /**
     * 负责人：hh
     * 功能：找炸弹
     * 参数： pokers  手牌  model 牌组
     * 返回值：void
     */
    private static void getBomb(ArrayList<Poker> pokers, Model model) {
        ArrayList<Poker> del = new ArrayList<>();// 要删除的Cards
        if (pokers.size() < 1){
            return;
        }
        // 王炸
        if (pokers.size() >= 2 && pokers.get(0).getName().charAt(0) == '5' && pokers.get(1).getName().charAt(0) == '5') {
            model.a4.add(pokers.get(0).getName() + "," + pokers.get(1).getName());// 按名字加入
            del.add(pokers.get(0));
            del.add(pokers.get(1));
        }
        pokers.removeAll(del);
        // 一般的炸弹
        for (int i = 0, len = pokers.size(); i < len; i++) {
            if (i + 3 < len && getValue(pokers.get(i)) == getValue(pokers.get(i + 3))) {
                String s = pokers.get(i).getName() + ",";
                s += pokers.get(i + 1).getName() + ",";
                s += pokers.get(i + 2).getName() + ",";
                s += pokers.get(i + 3).getName();
                model.a4.add(s);
                for (int j = i; j <= i + 3; j++)
                    del.add(pokers.get(j));
                // 得到一组炸弹后直接往后跳三张牌
                i = i + 3;
            }
        }
        pokers.removeAll(del);
    }

    /**
     * 负责人：巴别
     * 功能：找顺子
     * 参数： pokers  手牌  model 牌组
     * 返回值：void
     */
    private static void get123(ArrayList<Poker> pokers, Model model) {
        if (pokers.size() > 0 && (getValue(pokers.get(0)) < 7 || getValue(pokers.get(pokers.size() - 1)) > 10))
            return;
        if (pokers.size() < 5) {
            return;
        }
        // 先要把所有不重复的牌归为一类，防止3带，对子影响
        ArrayList<Poker> list2 = new ArrayList<>();
        ArrayList<Poker> temp = new ArrayList<>();
        ArrayList<Integer> integers = new ArrayList<>();
        for (Poker poker : list2) {
            if (integers.indexOf(getValue(poker)) < 0) {
                integers.add(getValue(poker));
                temp.add(poker);
            }
        }
        PlayerOperation.order(temp);
        for (int i = 0, len = temp.size(); i < len; i++) {
            if (getValue(temp.get(i)) == 15 || getColor(temp.get(i)) == 5) {
                continue;
            }
            int k = i;
            for (int j = i; j < len; j++) {
                if (getValue(temp.get(i)) - getValue(temp.get(j)) == j - i) {
                    k = j;
                }
            }
            if (k - i >= 4) {
                String s = "";
                for (int j = i; j < k; j++) {
                    s += temp.get(j).getName() + ",";
                }
                s += temp.get(k).getName();
                model.a123.add(s);
                i = k;
            }
        }
    }

    /**
     * 负责人：阿布
     * 功能：获取牌的大小
     * 参数： poker  扑克牌
     * 返回值：int  牌的权值
     */
    public static int getValue(Poker poker) {
        int i = Integer.parseInt(poker.getName().substring(2));
        if (Integer.parseInt(poker.getName().substring(0, 1)) == 5)
            i += 13;// 是王
        return i;
    }

    /**
     * 负责人：阿布
     * 功能：返回花色
     * 参数： poker  扑克牌
     * 返回值：int 1 2 3 4 5(王)
     */
    public static int getColor(Poker poker) {
        return Integer.parseInt(poker.getName().substring(0, 1));
    }

    /**
     * 负责人：阿布
     * 功能：选择一个能出的牌组
     * 参数： model  所有牌组
     * 返回值：ArrayList  要出的牌组
     */
    public static ArrayList<Poker> choose(Model model, PokerType type) {
        ArrayList<Poker> pokers = new ArrayList<>();
        switch (type) {
            case c0:
                // 无限制出牌
                pokers = unlimited(model);
                break;
            case c1:
                // 单牌
                acquire1(model, pokers);
                break;
            case c2:
                // 对子
                acquire2(model, pokers);
                break;
            case c3:
                // 三不带
                acquire3(model, pokers);
                break;
            case c31:
                //三带一
                if (!acquire3(model, pokers) || !acquire1(model, pokers)) {
                    pokers.clear();
                }
                break;
            case c32:
                //三带二
                if (!acquire3(model, pokers) || !acquire2(model, pokers)) {
                    pokers.clear();
                }
                break;
            case c4:
                // 炸弹
                acquire4(model, pokers);
                break;
            case c411:
                // 四带二单
                if (!acquire4(model, pokers) || !acquire1(model, pokers) || !acquire1(model, pokers)) {
                    pokers.clear();
                }
                break;
            case c422:
                // 四带二双
                if (!acquire4(model, pokers) || !acquire2(model, pokers) || !acquire2(model, pokers)) {
                    pokers.clear();
                }
                break;
            case c123:
                // 顺子
                acquire12345(model, pokers);
                break;
            case c112233:
                // 连对
                acquire112233(model, pokers);
                break;
            case c111222:
                // 飞机无翅膀
                acquire111222(model, pokers);
                break;
            case c11122234:
                // 飞机带两单
                if (!acquire111222(model, pokers) || !acquire1(model, pokers) || !acquire1(model, pokers)) {
                    pokers.clear();
                }
                break;
            case c1112223344:
                // 飞机带两双
                if (!acquire111222(model, pokers) || !acquire2(model, pokers) || !acquire2(model, pokers)) {
                    pokers.clear();
                }
                break;
            default:
                break;
        }
        return pokers;
    }

    /**
     * 负责人：阿布
     * 功能：获取一个单排牌组，并将获取的牌组从手牌牌组中移除
     * 参数： model 手牌牌组    pokers 存放获取的牌组
     * 返回值：boolean true 表示获取成功  false  表示获取失败
     */
    private static boolean acquire1(Model model, ArrayList<Poker> pokers) {
        if (model.a1.size() >= 1) {
            for (int i = model.a1.size() - 1; i >= 0; i--) {
                boolean flag = true;
                // 去除已经出现在出牌牌组里面的牌
                for (Poker poker : pokers) {
                    if (poker.getName().equals(model.a1.get(i))) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    // 该牌未在牌组里面出现，则加入出牌牌组，并在手牌所有牌组里面删除
                    pokers.add(new Poker(model.a1.get(i)));
                    model.a1.remove(i);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 负责人：阿布
     * 功能：获取一个对子牌组，并将获取的牌组从手牌牌组中移除
     * 参数： model 手牌牌组    pokers 存放获取的牌组
     * 返回值：boolean true 表示获取成功  false  表示获取失败
     */
    private static boolean acquire2(Model model, ArrayList<Poker> pokers) {
        if (model.a2.size() >= 1) {
            String[] s1 = model.a2.get(model.a2.size() - 1).split(",");
            for (int i = model.a2.size() - 1; i >= 0; i--) {
                boolean flag = true;
                // 去除已经出现在出牌牌组里面的牌
                for (Poker poker : pokers) {
                    if (poker.getName().equals(s1[0]) || poker.getName().equals(s1[1])) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    // 该牌未在牌组里面出现，则加入出牌牌组，并在手牌所有牌组里面删除
                    for (String item : s1) {
                        pokers.add(new Poker(item));
                    }
                    model.a2.remove(model.a2.size() - 1);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 负责人：阿布
     * 功能：获取一个对子牌组，并将获取的牌组从手牌牌组中移除
     * 参数： model 手牌牌组    pokers 存放获取的牌组
     * 返回值：boolean true 表示获取成功  false  表示获取失败
     */
    private static boolean acquire3(Model model, ArrayList<Poker> pokers) {
        if (model.a3.size() >= 1) {
            String[] s1 = model.a3.get(model.a3.size() - 1).split(",");
            for (String item : s1) {
                pokers.add(new Poker(item));
            }
            model.a3.remove(model.a3.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 负责人：阿布
     * 功能：获取一个炸弹牌组，并将获取的牌组从手牌牌组中移除
     * 参数： model 手牌牌组    pokers 存放获取的牌组
     * 返回值：boolean true 表示获取成功  false  表示获取失败
     */
    private static boolean acquire4(Model model, ArrayList<Poker> pokers) {
        if (model.a4.size() >= 1) {
            String[] s1 = model.a4.get(model.a4.size() - 1).split(",");
            for (String item : s1) {
                pokers.add(new Poker(item));
            }
            model.a4.remove(model.a4.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 负责人：阿布
     * 功能：获取一个顺子的牌组，并将获取的牌组从手牌牌组中移除
     * 参数： model 手牌牌组    pokers 存放获取的牌组
     * 返回值：boolean true 表示获取成功  false  表示获取失败
     */
    private static void acquire12345(Model model, ArrayList<Poker> pokers) {
        if (model.a123.size() >= 1) {
            String[] s1 = model.a123.get(model.a123.size() - 1).split(",");
            for (String item : s1) {
                pokers.add(new Poker(item));
            }
            model.a123.remove(model.a123.size() - 1);
        }
    }

    /**
     * 负责人：阿布
     * 功能：获取一个连对的牌组，并将获取的牌组从手牌牌组中移除
     * 参数： model 手牌牌组    pokers 存放获取的牌组
     * 返回值：boolean true 表示获取成功  false  表示获取失败
     */
    private static void acquire112233(Model model, ArrayList<Poker> pokers) {
        if (model.a112233.size() >= 1) {
            String[] s1 = model.a112233.get(model.a112233.size() - 1).split(",");
            for (String item : s1) {
                pokers.add(new Poker(item));
            }
            model.a112233.remove(model.a112233.size() - 1);
        }
    }

    /**
     * 负责人：阿布
     * 功能：获取一个飞机不带翅膀的牌组，并将获取的牌组从手牌牌组中移除
     * 参数： model 手牌牌组    pokers 存放获取的牌组
     * 返回值：boolean true 表示获取成功  false  表示获取失败
     */
    private static boolean acquire111222(Model model, ArrayList<Poker> pokers) {
        if (model.a111222.size() >= 1) {
            String[] s1 = model.a111222.get(model.a111222.size() - 1).split(",");
            for (String item : s1) {
                pokers.add(new Poker(item));
            }
            model.a111222.remove(model.a111222.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 负责人：阿布
     * 功能：无限制出牌的人机出牌顺序
     * 参数： model 手牌牌组      pokers  存放获取的牌组
     * 返回值：void
     */
    public static ArrayList<Poker> unlimited(Model model) {
        // 飞机带对子
        ArrayList<Poker> pokers = choose(model, PokerType.c1112223344);
        if (pokers.size() == 0) {
            // 飞机带单牌
            pokers = choose(model, PokerType.c11122234);
            if (pokers.size() == 0) {
                // 飞机不带翅膀
                pokers = choose(model, PokerType.c111222);
                if (pokers.size() == 0) {
                    // 连对
                    pokers = choose(model, PokerType.c112233);
                    if (pokers.size() == 0) {
                        // 顺子
                        pokers = choose(model, PokerType.c123);
                        if (pokers.size() == 0) {
                            // 三带二
                            pokers = choose(model, PokerType.c32);
                            if (pokers.size() == 0) {
                                // 三带一
                                pokers = choose(model, PokerType.c31);
                                if (pokers.size() == 0) {
                                    // 对子
                                    pokers = choose(model, PokerType.c2);
                                    if (pokers.size() == 0) {
                                        // 单牌
                                        pokers = choose(model, PokerType.c1);
                                        if (pokers.size() == 0) {
                                            // 炸弹
                                            pokers = choose(model, PokerType.c4);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return pokers;
    }
}
