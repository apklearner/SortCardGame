package com.example.ly.sortcardgame;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ly.sortcardgame.adapter.CardAdapter;
import com.example.ly.sortcardgame.utils.MathUtils;
import com.example.ly.sortcardgame.utils.PictureUtils;
import com.example.ly.sortcardgame.utils.ScreenUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,  AdapterView.OnItemClickListener {
    private GridView gridView;
    private Button btn_3x3,btn_4x4,btn_5x5,btn_6x6,btn_p,btn_c,btn_r,btn_o;
    private TextView tvStep;
    private int showMode = 3;//3表示3x3 4表示4x4
    private int screenWidth,screenHeight;
    private ArrayList<Bitmap> listbp = new ArrayList<>();
    private ArrayList<Integer> listInt = new ArrayList<>();
    private CardAdapter adapter;
    private int maxX,maxY;
    private Bitmap oriBitmap;
    private Bitmap blankParent;
    private Bitmap lastBitmap;
    private int currentBlank;
    private Spinner spinner;
    private BitmapFactory.Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRes();
        initGridView();
        initSpinner();
        solveBpList(maxX,maxY);
        /** ----------------------------------------------------- */
        adapter = new CardAdapter(this,listbp,new int[]{itemWidth,itemHeight});
        gridView.setAdapter(adapter);
        oriBitmap = null;
        System.gc();
        /**-----------------------------------------------------------*/



        /***/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
//            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);

            }

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);

            }
        }




    }
    private void initRes(){
        gridView = (GridView) findViewById(R.id.gv);
        btn_3x3 = (Button) findViewById(R.id.btn_1);
        btn_4x4 = (Button) findViewById(R.id.btn_2);
        btn_5x5 = (Button) findViewById(R.id.btn_3);
        btn_6x6 = (Button) findViewById(R.id.btn_4);
        btn_p = (Button) findViewById(R.id.btn_p);
//        btn_c = (Button) findViewById(R.id.btn_c);
        btn_r = (Button) findViewById(R.id.btn_r);
        btn_o = (Button) findViewById(R.id.btn_o);
        tvStep = (TextView) findViewById(R.id.tv_step);
        spinner = (Spinner) findViewById(R.id.sp);

        screenWidth = ScreenUtils.getWindowSize(this)[0];
        screenHeight = ScreenUtils.getWindowSize(this)[1];

        btn_3x3.setOnClickListener(this);
        btn_4x4.setOnClickListener(this);
        btn_5x5.setOnClickListener(this);
        btn_6x6.setOnClickListener(this);
        btn_p.setOnClickListener(this);
//        btn_c.setOnClickListener(this);
        btn_r.setOnClickListener(this);
        btn_o.setOnClickListener(this);


        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;

        //没有空白图 蛋疼
        blankParent = BitmapFactory.decodeResource(getResources(),R.drawable.b01,options);
        gridView.setOnItemClickListener(this);
        oriBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.a1,options);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("准备中...");
    }










    private void initGridView(){
         maxX = MathUtils.getMaxTogetValue(screenWidth);
         maxY = MathUtils.getMaxTogetValue(screenHeight*3/4);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) gridView.getLayoutParams();
        params.height  = maxY;
        params.width = maxX;
        gridView.setLayoutParams(params);
        gridView.setNumColumns(3);
    }

    private boolean isSpinnerMode ;
    private void initSpinner(){
        String[] data = new String[]{"更多","6x6","8x8","10x10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0) return;
                currentStep = 0;
                tvStep.setText(currentStep+"");
                switch (position){
                    case 1:
                        showMode = 6;
                        break;
                    case 2:
                        showMode = 8;
                        break;
                    case 3:
                        showMode = 10;
                        break;
                }
//                gridView.setNumColumns(showMode);
//                solveBpList(maxX,maxY);
                new MyAsyTask().execute("");
                spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private int itemWidth;
    private int itemHeight;

    private void solveBpList(int maxX,int maxY){

        if(TextUtils.isEmpty(currentImagePath)){
            oriBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.a1,options);
        }else {
            if(PictureUtils.getCompressBitmap(currentImagePath)!=null){
                oriBitmap = PictureUtils.getCompressBitmap(currentImagePath);
            }else {
                Toast.makeText(this,"获取图片失败",Toast.LENGTH_LONG).show();
                return;
            }
        }

        listbp.clear();
        listInt.clear();
        Bitmap bitmap =scaleBitmap(oriBitmap);
        itemWidth = maxX/showMode;
        itemHeight = maxY/showMode;

        for(int i =0;i<showMode;i++){
            for(int j =0 ;j<showMode;j++){
                Bitmap bitmap1  = Bitmap.createBitmap(bitmap,j*itemWidth,i*itemHeight,itemWidth,itemHeight);
                listbp.add(bitmap1);
                int _position = MathUtils.getIntPosition(new int[]{i,j},showMode);
                listInt.add(_position);

            }
        }

        Bitmap bitmapt = Bitmap.createBitmap(blankParent,0,0,itemWidth,itemHeight);
        lastBitmap = listbp.get(listbp.size()-1);
        listbp.remove(listbp.size()-1);
        listbp.add(bitmapt);
        currentBlank = showMode*showMode-1;
//        adapter = new CardAdapter(this,listbp,new int[]{itemWidth,itemHeight});
//        gridView.setAdapter(adapter);
//        oriBitmap = null;
//        System.gc();
    }

    /**
     * 缩放图片到指定大小
     * @param bitmap
     * @return
     */
    private Bitmap scaleBitmap(Bitmap bitmap){
        Bitmap newBitmap = null;
        if(bitmap.getHeight()>=bitmap.getWidth()){
//            Log.i("223","bitmapH--->>>"+bitmap.getHeight());
//            Log.i("223","bitmapW--->>>"+bitmap.getWidth());
            Matrix matrix = new Matrix();
            matrix.postScale((maxX+0f)/bitmap.getWidth(),(maxY +0f)/bitmap.getHeight());
            newBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//            Log.i("223","nbitmapH--->>>"+newBitmap.getHeight());
//            Log.i("223","nbitmapW--->>>"+newBitmap.getWidth());
        }else{
//            Log.i("123","bitmapH--->>>"+bitmap.getHeight());
//            Log.i("123","bitmapW--->>>"+bitmap.getWidth());
            Matrix matrix = new Matrix();
            matrix.postScale((maxY+0f)/bitmap.getWidth(),(maxX+0f)/bitmap.getHeight());
            matrix.postRotate(270);
            newBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//            Log.i("123","nbitmapH--->>>"+newBitmap.getHeight());
//            Log.i("123","nbitmapW--->>>"+newBitmap.getWidth());
        }

        return  newBitmap;
    }

    private int currentStep = 0;

    @Override
    public void onClick(View v) {
        isComplete = false;
        switch (v.getId()){
            case R.id.btn_1:
                currentStep = 0;
                tvStep.setText(currentStep+"");
                showMode = 3;
//                gridView.setNumColumns(3);
//                solveBpList(maxX,maxY);
                new MyAsyTask().execute("");
                break;
            case R.id.btn_2:
                currentStep = 0;
                tvStep.setText(currentStep+"");
                showMode = 4;
//                gridView.setNumColumns(4);
//                solveBpList(maxX,maxY);
                new MyAsyTask().execute("");
                break;
            case R.id.btn_3:
                currentStep = 0;
                tvStep.setText(currentStep+"");
                showMode = 5;
//                gridView.setNumColumns(5);
//                solveBpList(maxX,maxY);
                new MyAsyTask().execute("");
                break;
            case R.id.btn_4:
                currentStep = 0;
                tvStep.setText(currentStep+"");
                showMode = 6;
                gridView.setNumColumns(6);
//                solveBpList(maxX,maxY);
                break;
            case R.id.btn_c:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = Uri.fromFile(new File(TEMP_IMAGE_PATH));
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,RESULT_CAMERA);
                break;
            case R.id.btn_p:
                Intent intent1 = new Intent(Intent.ACTION_PICK,null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_TYPE);
                startActivityForResult(intent1,RESULT_IMAGE);
                break;
            case R.id.btn_r:
                currentStep = 0;
                tvStep.setText(currentStep+"");
                gridView.setNumColumns(showMode);
//                solveBpList(maxX,maxY);
                new MyAsyTask().execute("123");
//                if(showMode<=6){
//                    currentBlank =  MathUtils.randomChages(300,showMode*showMode-1,listbp,listInt,showMode);
//                }else {
//                    currentBlank =  MathUtils.randomChages(500,showMode*showMode-1,listbp,listInt,showMode);
//                }

                /**  ------------------------------------------------------------  */
//                adapter = new CardAdapter(this,listbp,new int[]{itemWidth,itemHeight});
//                gridView.setAdapter(adapter);
//                oriBitmap = null;
//                System.gc();
                /** ------------------------------------------------------*/
//                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_o:
//                ivOrign.setImageBitmap(oriBitmap);
//                ivOrign.setVisibility(View.VISIBLE);
                Intent intent2 = new Intent(this,SecondActivity.class);
                intent2.putExtra("image",currentImagePath);
                startActivity(intent2);
                break;
        }
    }


    private boolean isComplete ;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(isComplete) return;
        int blankInts[] = MathUtils.getGridInts(currentBlank,showMode);
        if(position != currentBlank){
            int move[] = MathUtils.getGridInts(position,showMode);
            if(MathUtils.isNearBlank(blankInts,move)){
                MathUtils.reversBitmap(listbp,currentBlank,position);
                MathUtils.reversPosition(listInt,currentBlank,position);
                currentBlank = position;
                if(currentBlank == showMode *showMode -1){
                    if(MathUtils.checkOrder(listInt,showMode)){
                        listbp.remove(listInt.size()-1);
                        listbp.add(lastBitmap);
                        isComplete =true;
                        Toast.makeText(this,"完成",Toast.LENGTH_LONG).show();
                    }
                }
                currentStep+=1;
                tvStep.setText(currentStep+"");
                adapter.notifyDataSetChanged();

            }
        }

    }


    private static final int RESULT_IMAGE = 100;
    private static final String IMAGE_TYPE = "image/*";
    public static String TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath()+ "/temp.png";
    private static final int RESULT_CAMERA = 200;

    private String currentImagePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_IMAGE &&data != null){
                Cursor cursor = this.getContentResolver().query(data.getData(),null,null,null,null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                if(PictureUtils.getCompressBitmap(imagePath)!=null){
//                    oriBitmap = BitmapFactory.decodeFile(imagePath,options);
//                    oriBitmap = PictureUtils.getCompressBitmap(imagePath);
                    currentImagePath = imagePath;
                }
            }else if(requestCode == RESULT_CAMERA){
                if(BitmapFactory.decodeFile(TEMP_IMAGE_PATH)!=null){
                    oriBitmap = BitmapFactory.decodeFile(TEMP_IMAGE_PATH);
                }
            }
            gridView.setNumColumns(showMode);
//            solveBpList(maxX,maxY);
            new MyAsyTask().execute("");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                showDismissDialog();
                return  true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void showDismissDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("sure to exit?");
        builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }


    private class MyAsyTask extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            solveBpList(maxX,maxY);
            if(!TextUtils.isEmpty(params[0])){
                if(showMode<=6){
                    currentBlank =  MathUtils.randomChages(600,showMode*showMode-1,listbp,listInt,showMode);
                }else {
                    currentBlank =  MathUtils.randomChages(1000,showMode*showMode-1,listbp,listInt,showMode);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            gridView.setNumColumns(showMode);
            adapter = new CardAdapter(MainActivity.this,listbp,new int[]{itemWidth,itemHeight});
            gridView.setAdapter(adapter);
            oriBitmap = null;
            System.gc();
            progressDialog.hide();
        }
    }

    private ProgressDialog progressDialog;
    private void showLoadingDialog(){
        progressDialog .show();
    }

    private void hideLoadingDialog(){
        progressDialog.hide();
    }
}
