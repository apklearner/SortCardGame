package com.example.ly.sortcardgame.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by ly on 2011/10/27.
 */
public class PictureUtils {

   public static int calculateInSample(BitmapFactory.Options options,int reqWidth,int reqHeight){
       int height = options.outHeight;
       int width = options.outWidth;
       int inSampleSize = 1;
       if (height > reqHeight || width > reqWidth) {
           int heightRatio = Math.round((float)height/(float)reqHeight);
           int widthRatio = Math.round((float)width/(float)reqWidth);
           inSampleSize = heightRatio>widthRatio?widthRatio:heightRatio;
       }
       return inSampleSize;
   }

    public static Bitmap getCompressBitmap(String imagepath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagepath,options);
        options.inSampleSize = calculateInSample(options,720,1280);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagepath,options);
    }
}
