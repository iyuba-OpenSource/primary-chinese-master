package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.Dialog;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.DialogVip;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.network.Main.Constant;

import java.io.IOException;
import java.util.List;

public class ShadowingAdapter2 extends RecyclerView.Adapter<ShadowingAdapter2.ShadowingViewHolder> {

    //    @NonNull
    public static Context context;
    private boolean isbegin = false;

    public boolean isisbegin() {
        return isbegin;
    }

    public void setIsbegin(boolean isbegin) {
        this.isbegin = isbegin;
    }

    private int choosePosition = 0;

    public int getChoosePosition() {
        return choosePosition;
    }

    public void setChoosePosition(int choosePosition) {
        this.choosePosition = choosePosition;
    }

    private boolean islisten = false;

    public boolean isIslisten() {
        return islisten;
    }

    public void setislisten(boolean islisten) {
        this.islisten = islisten;
    }

    private boolean isShow = false;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    private boolean isHide = false;

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    private boolean ismic = false;

    public boolean isIsmic() {
        return ismic;
    }

    public void setIsmic(boolean ismic) {
        this.ismic = ismic;
    }

    public MainBean.InfoBean getItem(int position) {
        return datas.get(position);
    }

    public CallBack getCallBack(CallBack callBack) {
        return ShadowingAdapter2.callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private static CallBack callBack;     //最后得callback接口


    private List<MainBean.InfoBean> datas;
    //1.定义变量接收接口
    private OnItemClickListener mOnItemClickListener;


    //为了使唯一一个item显示，将点击事件传到ShadowingFranmt
    public interface OnItemClickListener {
        void onItemClick(View view, int position);//单击
    }

    public void setOnItemClickListener(ShadowingAdapter2.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public ShadowingAdapter2(Context context, List<MainBean.InfoBean> datas) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public ShadowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.shadowing_for, parent, false);
        return new ShadowingAdapter2.ShadowingViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ShadowingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textContent.setText(datas.get(position).getSentence());
        holder.textContent.setTextSize(Constant.CHINESESIZE);
        ChangeColor(holder, position);
        if (choosePosition == position) {
            holder.liniamge.setVisibility(View.VISIBLE);
            holder.image_microphone.setVisibility(View.VISIBLE);
            holder.image_begin.setVisibility(View.VISIBLE);
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(String.valueOf(position + 1) + "/" + +datas.size());

            if (!Constant.SCORE.get(position).equals("nothing")) {
                if (Integer.parseInt(Constant.SCORE.get(position)) < 0) {
                    holder.text_score.setText("0");
                } else {
                    holder.text_score.setText(Constant.SCORE.get(position));
                }
                holder.text_score.setVisibility(View.VISIBLE);
                holder.linelisten.setVisibility(View.VISIBLE);
            } else {
                holder.text_score.setVisibility(View.INVISIBLE);
                holder.linelisten.setVisibility(View.INVISIBLE);
            }
        } else {
            //没被选中的item
            holder.textView.setVisibility(View.GONE);
            holder.liniamge.setVisibility(View.GONE);
        }
        //音乐播放得状态
        if (isbegin) {
            if (choosePosition == position) {
                holder.image_begin.setImageResource(R.mipmap.stop);

            } else {
                holder.image_begin.setImageResource(R.mipmap.begin);
            }
        } else {
            holder.image_begin.setImageResource(R.mipmap.begin);
        }
        if (ismic) {
            if (choosePosition == position) {
                holder.image_microphone.setImageResource(R.mipmap.mircophone_on);

            } else {
                holder.image_microphone.setImageResource(R.mipmap.microphone);
            }
        } else {

            holder.image_microphone.setImageResource(R.mipmap.microphone);
        }
        //有录音 录音按钮得状态
        if (islisten) {

            if (choosePosition == position) {

                holder.image_listen.setImageResource(R.mipmap.listening);

            } else {
                holder.image_listen.setImageResource(R.mipmap.listen);
            }
        } else {

            holder.image_listen.setImageResource(R.mipmap.listen);
        }
    }

    private void ChangeColor(ShadowingViewHolder holder, int position) {
        //变色逻辑
        int j = 0;
        String text = datas.get(position).getSentence();
        SpannableString spannableString = new SpannableString(text);
        int i = 0;
//        Log.d("ChangeColor: ", text.length() + "*" + Constant.WORDCOLOER.size());
        while (i < text.length() - 1) {
            if (!isPunctuationOrChineseCharacter(text.charAt(i))) {
                //如果不是标点
                int color = getColorForIndex(j, position); // 根据索引获取颜色，你可以自定义这个方法 getColorForIndex(i)
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                spannableString.setSpan(colorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                i = i + 1;
                j = j + 1;

            }
            if (isPunctuationOrChineseCharacter(text.charAt(i))) {
                //如果是标点，判断是不是第一个
                if (i == 0) {
                    int color = getColorForIndex(j, position); // 根据索引获取颜色，你可以自定义这个方法 getColorForIndex(i)
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                    spannableString.setSpan(colorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    i = i + 1;
                } else {
                    //如果是标点
                    int color = getColorForIndex(j - 1, position); // 根据索引获取颜色，你可以自定义这个方法 getColorForIndex(i)
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                    spannableString.setSpan(colorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    i = i + 1;
                }

            }


        }
        holder.textContent.setText(spannableString);
        holder.textContent.setTextSize(Constant.CHINESESIZE);
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

    private int getColorForIndex(int i, int postionq) {
        if (Constant.WORDCOLOER.get(postionq).get(i) < 2) {
            //红色
            return Color.RED;
        } else if (Constant.WORDCOLOER.get(postionq).get(i) >= 4) {
            int rgbValue = 0x1F9000; // 十六进制值
            int alpha = 255; // alpha值，0-255范围，代表透明度，255表示完全不透明
            int customColor = Color.argb(alpha, Color.red(rgbValue), Color.green(rgbValue), Color.blue(rgbValue));
            return customColor;
        } else {
            return Color.BLACK;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //绑定控件
    public static class ShadowingViewHolder extends RecyclerView.ViewHolder {
        TextView textView, text_score, textContent;
        ImageView image_listen, image_microphone, image_begin;
        LinearLayout linearLayout, liniamge, linelisten, linemic, linebegin, lineAll;

        ShadowingViewHolder(@NonNull View itemView) {
            super(itemView);
            lineAll = itemView.findViewById(R.id.line_all);
            textContent = itemView.findViewById(R.id.text89);
            textView = itemView.findViewById(R.id.countText);
            image_begin = itemView.findViewById(R.id.image_begin);
            image_listen = itemView.findViewById(R.id.image_listen);
            image_microphone = itemView.findViewById(R.id.image_microphone);
            text_score = itemView.findViewById(R.id.text_score);
            liniamge = itemView.findViewById(R.id.line_image);
            linelisten = itemView.findViewById(R.id.listen_line);
            linemic = itemView.findViewById(R.id.image_microphone_line);
            linebegin = itemView.findViewById(R.id.image_begin_line);

            // 原文英语句子
            lineAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBack != null) {
                        try {
                            callBack.clickItem(getAdapterPosition());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
            //麦克风
            linemic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int count = 0;
                    for (String item : Constant.SCORE) {
                        if (!item.equals("nothing")) {
                            count++;
                        }
                    }
                    //登录有没有登录
                    SharedPreferences sharedPreferences = context.getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                    String uid = sharedPreferences.getString("uid", "nothing");
                    if (uid.equals("nothing")) {
                        Dialog dialog = new Dialog(context, "未登录，请先登录", "去登录", "取消");
                        dialog.ShowDialog();
                    } else {
                        //判断是不是会员
                        if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME.equals("0") && count >= 3) {
                            DialogVip dialogVip = new DialogVip(context, "会员才可以测评3句以上哦,请先购买会员", "去购买", "取消");
                            dialogVip.ShowDialog();
                        } else {
                            if (callBack != null) {
                                try {
                                    callBack.clickMic(getAdapterPosition());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            });
            //原句播放按钮
            linebegin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBack != null) {
                        try {
                            callBack.clickBegin(getAdapterPosition());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
            //录音播放
            linelisten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBack != null) {
                        try {
                            callBack.clickListen(getAdapterPosition());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

        }
    }


    public interface CallBack {
        void clickBegin(int position) throws IOException;

        void clickMic(int position) throws IOException;

        void clickListen(int position) throws IOException;

        void clickItem(int position) throws IOException;
    }

}
