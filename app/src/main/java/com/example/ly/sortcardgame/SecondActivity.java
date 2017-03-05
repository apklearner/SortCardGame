package com.example.ly.sortcardgame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.ly.sortcardgame.utils.PictureUtils;

/**
 * Created by ly on 2011/10/27.
 */
public class SecondActivity extends Activity {

    private ImageView ivOrigin;
    private String imagePath;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ivOrigin = (ImageView) findViewById(R.id.iv_origin);
        imagePath = getIntent().getStringExtra("image");


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;

        if(TextUtils.isEmpty(imagePath)){
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.a1,options);
//            bitmap = PictureUtils.getCompressBitmap(imagePath);
        }else {
            bitmap = PictureUtils.getCompressBitmap(imagePath);
//            bitmap = BitmapFactory.decodeFile(imagePath,options);
        }
        if(bitmap!=null) ivOrigin.setImageBitmap(bitmap);

    }

}
