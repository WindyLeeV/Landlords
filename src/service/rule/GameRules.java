package service.rule;

import domain.Poker;
import domain.PokerType;
import service.ai.Robot1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// 定义出牌的规则
public class GameRules {

    /**
     * 负责人：Windy
     * 功能：判断当前是否为无限制出牌
     * 参数：void
     * 返回值：boolean   true表示无限制出牌    false表示有限制出牌
     */
    public static boolean examine(ArrayList<Poker> previous) {
        return previous.size() == 0;
    }


    /**
     * 负责人：Windy
     * 功能：判断是否符合规则
     * 参数： list  牌组
     * 返回值：PokerType 牌型   返回null表示不符合出牌规则
     */
    public static PokerType judge(ArrayList<Poker> list) {
        int len = list.size();
        if (0  < len && len< 4)    return lessFour(list,len);
        else if ( len ==4)  return  equalFour(list);
        else if ( len >= 5) return  moreThanFour(list,len);
        return PokerType.c0;
    }

    // Set a function to judge what is the value of poker    3 - 3  4- 4 .... A - 14, 2 - 15, SG - 16, BG - 17
    public static int PV(Poker card){
        String inputString = card.getName();
        int first = inputString.charAt(0);
        if (first != 5) {
            String thirdChar = inputString.substring(2); // Index 2 corresponds to the third character (0-based index)
            return Integer.parseInt(thirdChar);
        }
        else {
            if ((int) inputString.charAt(2) == 3)   return 16;
            else return 17;
        }
    }

    /**
     * 负责人：Windy
     * 功能：判断小于四张牌的牌组是什么牌型
     * 参数： list  牌组
     * 返回值：PokerType 牌型   返回c0表示不符合出牌规则
     */
    public static PokerType lessFour(ArrayList<Poker> list,int len) {
        if (len == 1)   return PokerType.c1;
        if (len == 2){
            if (Robot1.getValue(list.get(0)) == Robot1.getValue(list.get(1))) {
                return PokerType.c2;
            }
            if (Robot1.getColor(list.get(1)) == 5) {
                return PokerType.c4;
            } else{
                return PokerType.c0;
            }
        }
        if (len == 3)   {
            if (PV(list.get(0)) == PV(list.get(1))&& PV(list.get(1))==PV(list.get(2)))return PokerType.c3;
            else return PokerType.c0;
        }
        else return PokerType.c0;
    }

    /**
     * 负责人：Windy
     * 功能：判断等于四张牌的牌组是什么牌型
     * 参数： list  牌组
     * 返回值：PokerType 牌型   返回c0表示不符合出牌规则
     */
    public static PokerType equalFour(ArrayList<Poker> list) {
        if (Robot1.getValue(list.get(0)) == Robot1.getValue(list.get(3))){
            return PokerType.c4;
        } else if (Robot1.getValue(list.get(0)) == Robot1.getValue(list.get(2)) || Robot1.getValue(list.get(1)) == Robot1.getValue(list.get(3))) {
            return PokerType.c31;
        } else {
            return PokerType.c0;
        }
    }

    /**
     * 负责人：Windy
     * 功能：判断大于四张牌的牌组是什么牌型
     * 参数： list  牌组
     * 返回值：PokerType 牌型   返回c0表示不符合出牌规则
     */
    // Count how many times does a value appears.
    static Map<Integer, Integer> hashMap = new HashMap<>();
    public static PokerType moreThanFour(ArrayList<Poker> list,int len) {
        // 判断顺子
        boolean isIncreasing = true;
        for (int i = 0; i < len - 1; i++) {
            if (PV(list.get(i)) - 1 != PV(list.get(i+1)) || PV(list.get(i)) > 14) {
                isIncreasing = false;
                break; // No need to continue checking if we found a non-increasing pair
            }
        }


        // 判断三带二，飞机，连的顺子
        for (int i = 1; i <= 17; i++) {
            hashMap.put(i, 0);
        }
        for (Poker card : list){
            // Check if the key exists in the HashMap
            if (hashMap.containsKey(PV(card))) {
                // Increase the value associated with the key by 1
                int currentValue = hashMap.get(PV(card));
                hashMap.put(PV(card), currentValue + 1);
            }
        }
        int countKey1 = 0;
        int countKey2 = 0;
        int countKey3 = 0;
        int countKey4 = 0;

        ArrayList<Integer> array2 = new ArrayList<>();
        int[] array3 = new int[4];
        boolean increase2 = true;
        boolean increase3 = true;

        int count3=0;

        for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
            int key = entry.getValue();
            if (key == 1) {
                countKey1++;
            }else if (key == 2){
                countKey2++;
                array2.add(entry.getKey());
            } else if (key == 3) {
                countKey3++;
                array3[count3] = entry.getKey();
                count3++;
            }  else if (key == 4){
                countKey4++;
            }
        }

        // 判断连3是否连续
        if (array3[0] + 1 != array3[1] && array3[1] == 15) increase3 = false;
        // 判断连2是否连续
        for (int i = 1; i < array2.size(); i++) {
            if (array2.get(i) != array2.get(i - 1) + 1) {
                increase2 = false;
                break; // No need to continue checking if it's not increasing
            }
        }

        // 顺子
        if (isIncreasing)   return PokerType.c123;

        //　四代几与三带二
        else if ((countKey1==0&&countKey2==1&&countKey3==0&&countKey4==1)||(countKey1==2&&countKey2==0&&countKey3==0&&countKey4==1)) return PokerType.c411;
        else if (countKey1==0&&countKey2==2&&countKey3==0&&countKey4==1) return PokerType.c422;
        else if (countKey1==0&&countKey2==1&&countKey3==1&&countKey4==0) return PokerType.c32;

        // 连对与飞机
        else if (countKey1==0&&countKey2 >= 3&&countKey3==0&&countKey4==0&&increase2)   return PokerType.c112233;
        else if (countKey1==0&&countKey2==0&&countKey3==2&&countKey4==0&&increase3)    return PokerType.c111222;
        else if (countKey1==2&&countKey2==0&&countKey3==2&&countKey4==0&&increase3)  return PokerType.c11122234;
        else if (countKey1==0&&countKey2==2&&countKey3==2&&countKey4==0&&increase3)  return PokerType.c1112223344;
        return PokerType.c0;
    }

}
