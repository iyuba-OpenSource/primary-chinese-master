package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.network.Main.Constant;

import java.util.List;

import cn.smssdk.gui.util.Const;

public class BeisongAdapter extends RecyclerView.Adapter<BeisongAdapter.Text2ViewHolder> {
    Context context;
    List<String> chinese;
    List<String> chinese_init;
    private BeisongAdapter beisongAdapter;
    public BeisongAdapter(Context context, List<String> chinese) {
        this.chinese = chinese;
        this.context = context;
        Constant.BOOKCLICK = 0;
    }


    @NonNull
    @Override
    public Text2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.beisong_adapter, parent, false);
        return new Text2ViewHolder(itemView);
    }
    @SuppressLint({"RestrictedApi", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull Text2ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textViewc.setTypeface(Typeface.MONOSPACE);

//        ChangeColor(holder, position);


        Log.e("lhz", "pos: " + position  + Constant.MIcCLICK);
        // 初始化诗歌文本，设置下划线等基础样式
        if (position < Constant.MIcCLICK) {
            // 如果当前项是被点击的项，则执行颜色更新
            ChangeColor(holder, position);
        }
        else
        {
            InitPoem(holder, position);
            // 如果不是被点击的项，使用InitPoem初始化样式
        }
        if (Constant.MIcCLICK == position) {
            holder.muisc.setVisibility(View.VISIBLE);
        } else {
            holder.muisc.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return chinese.size();

    }



    // 判断是否是中文字符
    private boolean isChinese(char c) {
        return c >= '\u4e00' && c <= '\u9fa5';
    }

    // 判断是否是中文标点符号
    private boolean isChinesePunctuation(char c) {
        // 中文标点符号的 Unicode 范围：\u3000 ~ \u303f
        return (c >= '\u3000' && c <= '\u303f') || c == '\u0020';
    }

    public static class Text2ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewc;
        ImageView muisc;
        LinearLayout linearLayout;

        public Text2ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.line1f);
            textViewc = itemView.findViewById(R.id.textChinses);
            muisc = itemView.findViewById(R.id.muisc);
        }
    }
    // 初始状态
    private void InitPoem(BeisongAdapter.Text2ViewHolder holder, int position)
    {
        int max = -1, min = 99;
        //字符串处理，让空缺的字变成下划线
        SpannableString spannableString = new SpannableString(chinese.get(position));
        for (int i = 0; i < chinese.get(position).length(); i++) {
            char c = chinese.get(position).charAt(i);
            if (c == '\u3000') {
                if (max < i) {
                    max = i;
                }
                if (min > i) {
                    min = i;
                }
            }
            if (isChinese(c) || isChinesePunctuation(c)) {
                if (max > 0 && min > -1) {
                    spannableString.setSpan(new UnderlineSpan(), min, max + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    max = -1;
                    min = 99;
                }
            }
        }
        holder.textViewc.setText(spannableString);
    }


    //
    //文章变色
    private void ChangeColor(BeisongAdapter.Text2ViewHolder holder, int position) {

        Log.e("lhz", "ChangeColor: " + position );
        //变色逻辑
        int j = 0;
        String text = chinese.get(position);
        SpannableString spannableString = new SpannableString(text);
        int i = 0;
        while (i < text.length()) {
             if(text.charAt(i)==' '){
                 i=i+1;
             }
            if (!isPunctuationOrChineseCharacter(text.charAt(i))) {
                //如果不是标点
                int color = getColorForIndex(j, position); // 根据索引获取颜色，你可以自定义这个方法 getColorForIndex(i)

                Log.e("lhz", "Color:  " + i+ " " + color );
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                spannableString.setSpan(colorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                j = j + 1;
            }
            if (isPunctuationOrChineseCharacter(text.charAt(i))) {
                //如果是标点
                int color = getColorForIndex(j - 1, position); // 根据索引获取颜色，你可以自定义这个方法 getColorForIndex(i)
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                spannableString.setSpan(colorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            i = i + 1;


        }
        holder.textViewc.setText(spannableString);
    }

    private boolean isPunctuationOrChineseCharacter(char c) {
        return (c >= 33 && c <= 47) // 英文标点符号
                || (c >= 58 && c <= 64)
                || (c >= 91 && c <= 96)
                || (c >= 123 && c <= 126)
                || Character.getType(c) == Character.CONNECTOR_PUNCTUATION
                || Character.getType(c) == Character.DASH_PUNCTUATION
                || Character.getType(c) == Character.END_PUNCTUATION
                || Character.getType(c) == Character.FINAL_QUOTE_PUNCTUATION
                || Character.getType(c) == Character.INITIAL_QUOTE_PUNCTUATION
                || Character.getType(c) == Character.OTHER_PUNCTUATION
                || Character.getType(c) == Character.START_PUNCTUATION;
    }
    //应该变成哪个颜色
    private int getColorForIndex(int i, int postionq) {
        Log.d("xwh8080", i + "oo" + Constant.WORDCOLOER_BEISONG.get(postionq).size());

        Log.e("lhz", "getColorForIndex: " + Constant.WORDCOLOER_BEISONG);
        if (Constant.WORDCOLOER_BEISONG.get(postionq).size() > i && Constant.WORDCOLOER_BEISONG.get(postionq).get(i) < 2) {
            //红
            return Color.RED;
        } else if (Constant.WORDCOLOER_BEISONG.get(postionq).get(i) >= 4) {
            int rgbValue = 0x1F9000; // 十六进制值
            int alpha = 255; // alpha值，0-255范围，代表透明度，255表示完全不透明
            int customColor = Color.argb(alpha, Color.red(rgbValue), Color.green(rgbValue), Color.blue(rgbValue));
            return customColor;
        } else {
            return Color.BLACK;
        }


    }

//    public void updateColorsForSentence(int position, List<String> str) {
//
//        notifyItemChanged(position);
//
//        Log.e("lhz", "update1: " + position );
//
//    }
}
