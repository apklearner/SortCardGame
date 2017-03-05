package com.example.ly.sortcardgame.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ly on 2011/10/26.
 */
public class MathUtils {

    /**
     * 取3，4，5的最大公倍数
     * @param startValue
     * @return
     */
    public static int getMaxTogetValue(int startValue){
        int result = 0;
        for(int i = startValue;i>0 ;i--){
            if(i%3==0 && i%4==0 && i%5==0){
                return i;
            }
        }

        return result;
    }

    public static int[] getGridInts(int position,int showMode){
        int rest = position %showMode;
        int div = position/showMode;
        return new int[]{div,rest};
    }

    /**
     * 是否可以交换位置
     * @param blank
     * @param move
     * @return
     */
    public static boolean isNearBlank(int[] blank,int[] move){
        if(blank[0] == move[0] && Math.abs(blank[1]-move[1]) ==1) return true;
        if(blank[1] == move[1] && Math.abs(blank[0]-move[0])==1) return true;
        return false;

    }


    public static int getIntPosition(int[] data,int showMode){
        return showMode*data[0] +data[1] ;
    }

    public static void reversBitmap(List<Bitmap> listBp,int blankPosition,int movePosition){
        Bitmap blamk = listBp.get(blankPosition);
        Bitmap moved = listBp.get(movePosition);
        listBp.set(blankPosition,moved);
        listBp.set(movePosition,blamk);
    }
    public static void reversPosition(List<Integer> listBp,int blankPosition,int movePosition){
        int blankValue = listBp.get(blankPosition);
        int moveValue = listBp.get(movePosition);
        listBp.set(blankPosition,moveValue);
        listBp.set(movePosition,blankValue);
//        Log.i("123","listInt-->>>" +listBp.toString());
    }

    /**
     * 是否是连续数字集合 应该有其他方法吧 瞎写
     * @param listInt
     * @param showMode
     * @return
     */
    public static boolean checkOrder(List<Integer> listInt,int showMode){
        if(listInt.get(0) == 0 && listInt.get(listInt.size()-1) == showMode*showMode-1) {
            for (int i = 0; i < listInt.size(); i++) {
                if(i < listInt.size()-1){
                    if(listInt.get(i) +1 !=listInt.get(i+1)){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }


    public static List<Integer> getNearPosition(int position,int showMode){
        int curr[] = getGridInts(position,showMode);
        int row = curr[0];  //  行
        int column = curr[1];  //列
        ArrayList<Integer> listT = new ArrayList<>();
        int[] left = new int[]{row,column-1};
        int[] right = new int[]{row,column+1};
        int[] top = new int[]{row-1,column};
        int[] bottom = new int[]{row+1,column};
        if(column -1 >=0) listT.add(getIntPosition(left,showMode));
        if(column +1 <=showMode -1) listT.add(getIntPosition(right,showMode));
        if(row -1 >=0) listT.add(getIntPosition(top,showMode));
        if(row+1 <=showMode -1) listT.add(getIntPosition(bottom,showMode));
        return listT;

    }


    /**
     * 返回交换后的空白位置
     * @param startPosition
     * @param showMode
     * @return
     */
    public static int randomChangePosition(int startPosition,List<Bitmap> listBp,List<Integer> listInt,int showMode){
        List<Integer> nearPosition = getNearPosition(startPosition,showMode);
        int size = nearPosition.size();
        int randomValue = (int) (Math.random()*size);
        int randomPosition = nearPosition.get(randomValue);
        reversBitmap(listBp,startPosition,randomPosition);
        reversPosition(listInt,startPosition,randomPosition);
        return randomPosition;
    }

    public static int randomChages(int changeTimes,int startPosition,List<Bitmap> listBp,List<Integer> listInt,int showMode){
        int blankPosition = startPosition;
        for(int i=0;i<changeTimes;i++){
           blankPosition = randomChangePosition(blankPosition,listBp,listInt,showMode);
        }
        return blankPosition;
    }


}
