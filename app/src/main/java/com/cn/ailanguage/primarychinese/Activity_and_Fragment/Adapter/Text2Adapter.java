package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.network.Main.Constant;

import java.util.List;

public class Text2Adapter extends RecyclerView.Adapter<Text2Adapter.Text2ViewHolder> {
    Context context;
    List<String> chinese, pinyin;
    List<Integer> flaging;
    MediaPlayer mediaPlayer;
    private int curPosition = 0;

    boolean haveBold = false;

    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }

    public Text2Adapter(Context context, List<String> chinese, List<String> pinyin, List<Integer> flaging, MediaPlayer mediaPlayer) {
        this.chinese = chinese;
        this.context = context;
        this.pinyin = pinyin;
        this.flaging = flaging;
        this.mediaPlayer = mediaPlayer;
        Constant.BOOKCLICK=0;
    }


    @NonNull
    @Override
    public Text2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_text_inner, parent, false);
        return new Text2ViewHolder(itemView);
    }

    @SuppressLint({"RestrictedApi", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull Text2ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        Log.e("lhz", "text2adapter: " + position );
        int cnt = 0;
        Typeface boldTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
//判断要不要加粗文字
        if (Constant.EWHIC == flaging.get(position)) {
            cnt ++ ;
            if (cnt >= 2)
            {
                holder.textViewc.setTypeface(null, Typeface.NORMAL);
                holder.textViewp.setTypeface(null, Typeface.NORMAL);
            }
            else
            {
                holder.textViewc.setTypeface(boldTypeface);
                holder.textViewp.setTypeface(boldTypeface);
            }

        } else {
            holder.textViewc.setTypeface(null, Typeface.NORMAL);
            holder.textViewp.setTypeface(null, Typeface.NORMAL);
        }
        //判断是否为古诗格式，
        if (com.cn.ailanguage.primarychinese.network.Home.Constant.POEM == 2) {

            //设置拼音和汉字整体大方块与左右间距
            int marginInDp = 1;
            float scale = context.getResources().getDisplayMetrics().density;
            int marginInPixels = (int) (marginInDp * scale + 0.5f);
            holder.linearLayout.setPadding(marginInPixels, 0, marginInPixels, 0);
            //根据大小给出
            if (Constant.CHINESESIZE == 20) {
                //中等字号
                holder.textViewc.setTextSize(Constant.CHINESESIZE - 2);
                holder.textViewp.setTextSize(Constant.PINYINSIZE - 6);
                holder.textViewp.setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, context.getResources().getDisplayMetrics()));
                holder.textViewc.setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, context.getResources().getDisplayMetrics()));
            } else if (Constant.CHINESESIZE > 20) {
                //大号字
                holder.textViewc.setTextSize(Constant.CHINESESIZE - 2);
                holder.textViewp.setTextSize(Constant.PINYINSIZE - 9);
                holder.textViewp.setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics()));
                holder.textViewc.setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics()));
            } else {
                //小号字
                holder.textViewc.setTextSize(Constant.CHINESESIZE - 1);
                holder.textViewp.setTextSize(Constant.PINYINSIZE - 3);
                holder.textViewp.setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()));
                holder.textViewc.setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()));
            }

        }
        else {
            //不是古诗
            //设置一下中方格linearlayout边距
            int marginInDp = 5;
            float scale = context.getResources().getDisplayMetrics().density;
            int marginInPixels = (int) (marginInDp * scale + 0.5f);
            holder.linearLayout.setPadding(marginInPixels, 0, marginInPixels, 0);

            holder.textViewc.setTextSize(Constant.CHINESESIZE);
            holder.textViewp.setTextSize(Constant.PINYINSIZE);
        }

        //设置是否隐藏拼音
        if (Constant.SHOW == 1) {
            holder.textViewp.setText(pinyin.get(position));
        } else {
            holder.textViewp.setText(" ");
        }
        holder.textViewc.setText(chinese.get(position));
//        Log.d("xwh888", chinese.get(position));
    }
    @Override
    public int getItemCount() {
        return chinese.size();

    }

    public static class Text2ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewp, textViewc;
        LinearLayout linearLayout;

        public Text2ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.line1f);
            textViewp = itemView.findViewById(R.id.textPinyi);
            textViewc = itemView.findViewById(R.id.textChinses);
        }
    }


}
