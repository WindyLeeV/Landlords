package service.judge;

import domain.Model;
import domain.Poker;
import domain.PokerType;
import service.ai.Robot1;
import service.rule.GameRules;

import java.util.ArrayList;

// 人机的自由出牌
public class Free {

    /**
     * 负责人：小白
     * 功能：按照按牌型找牌  Robot1.robot()
     *      根据pokers手牌找到符合的牌返回
     * 参数 pokers: 表示当前机器人的手牌
     * 返回值：ArrayList  返回要出的牌，理论上不能为空，因为最小的牌是单牌，如果没牌可出就是手里就是没牌，手里没牌已经是获胜了
     */
    public static ArrayList<Poker> freePoker(ArrayList<Poker> pokers) {
        Model model = new Model();
        ArrayList<Poker> a = Robot1.robot(pokers, model, PokerType.c0, true);
        if (GameRules.judge(a) == PokerType.c4) {
            a = Robot1.robot(pokers, model, PokerType.c0, false);
        }
        return a;
    }
}
