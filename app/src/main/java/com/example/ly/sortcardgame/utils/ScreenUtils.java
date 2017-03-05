package com.example.ly.sortcardgame.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by ly on 2011/10/26.
 */
public class ScreenUtils {

    public static float getScreenDensity(Context context){
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        return windowManager.getDefaultDisplay().get
        return context.getResources().getDisplayMetrics().density;

    }

    public static int[] getWindowSize(Context context){
        int result[] = new int[2];
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        result[0] = windowManager.getDefaultDisplay().getWidth();
        result[1] = windowManager.getDefaultDisplay().getHeight();
        return result;
    }
}
