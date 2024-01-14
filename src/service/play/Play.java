package service.play;

import domain.Poker;
import domain.PokerType;
import service.judge.Free;
import service.judge.Limit;
import service.rule.GameRules;

import java.util.ArrayList;

// 人机和玩家的出牌判断
public class Play {

    /**
     * 负责人：.(点号)
     * 功能：人机出牌
     *      判断是否为无限制出牌  GameRules.examine()
     *      若为无限制出牌，则按照拟定的出牌顺序出  Free.freePoker()
     *      若为限制出牌，则在手牌中寻找有无牌能出，能出就直接出  Limit.robotLimitPokers()
     * 参数： previous: 要跟的牌，没有则为null  pokers: 表示当前机器人的手牌
     * 返回值：ArrayList<Poker>  空集合表示没牌出
     */
    public static ArrayList<Poker> robot(ArrayList<Poker> previous, ArrayList<Poker> pokers) {
        if(GameRules.examine(previous)){
            return Free.freePoker(pokers);
        }else {
            return Limit.robotLimitPokers(previous,pokers);
        }
    }

    /**
     * 负责人：.(点号)
     * 功能：玩家出牌判断
     *      判断是否符合牌型  c0
     *      判断是否是无限制出牌  previous.size() == 0
     *      若为无限制出牌，则直接返回
     *      若为限制出牌，则先比较牌型是否相同，再比较poker是否比previous大   Limit.limitPokers()
     *      最后返回poker
     * 参数： previous 要跟的牌      pokers    要出的牌
     * @return
     */
    public static ArrayList<Poker> player(ArrayList<Poker> previous, ArrayList<Poker> pokers) {
        if(GameRules.judge(pokers) != PokerType.c0){
            if(GameRules.examine(previous)){
                return pokers;
            }
            else {
                if(Limit.limitPokers(previous,pokers)){
                    return pokers;
                }
            }
        }
        return new ArrayList<Poker>();
    }
}
