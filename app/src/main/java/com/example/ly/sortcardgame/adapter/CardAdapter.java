package com.example.ly.sortcardgame.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ly.sortcardgame.R;

import java.util.ArrayList;

/**
 * Created by ly on 2011/10/26.
 */
public class CardAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Bitmap> listbp = new ArrayList<>();
    private int itemWidth;
    private int itemHeight;

    public CardAdapter(Context context,ArrayList<Bitmap> listbp,int[] itemSize) {
        this.context = context;
        this.listbp = listbp;
        itemWidth = itemSize[0];
        itemHeight = itemSize[1];
    }

    @Override
    public int getCount() {
        return listbp.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.item.setImageBitmap(listbp.get(position));
        return convertView;
    }


    private class ViewHolder  {
        ImageView item;
        ViewHolder(View view){
            item = (ImageView) view.findViewById(R.id.iv_item_gv);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) item.getLayoutParams();
            if(itemHeight!=0 && itemWidth!=0){
                params.height = itemHeight;
                params.width = itemWidth;
                item.setLayoutParams(params);
//                item.setOnTouchListener(this);
            }
        }

//        @Override
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//        }public boolean onTouch(View v, MotionEvent event) {
//
//            return false;
//        }
    }
}
