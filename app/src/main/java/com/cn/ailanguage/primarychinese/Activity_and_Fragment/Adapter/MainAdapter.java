package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.network.Main.Constant;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.mainViewHolder>{
    private List<MainBean.InfoBean> maindates;
    private Context mcontext;
    private Handler mhandler;
    private MediaPlayer mediaPlayer;
    private String firstTime;
    private int mHighlightedPosition = -1;
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public MainAdapter(List<MainBean.InfoBean> dates, Context context, Handler handler, MediaPlayer mediaPlayer) {
        maindates = dates;
        mcontext = context;
        mhandler=handler;

        this.mediaPlayer=mediaPlayer;
    }

    public List<MainBean.InfoBean> getDates() {
        return maindates;
    }

    public void setDates(List<MainBean.InfoBean> dates) {
        this.maindates = dates;
    }

    public Context getContext() {
        return mcontext;
    }

    public void setContext(Context context) {
        this.mcontext = context;
    }


    @NonNull
    @Override
    public mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mcontext).inflate(R.layout.item_text, parent, false);
        return new mainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mainViewHolder holder, int position) {
        MainBean.InfoBean daily = maindates.get(position);
        holder.bind(daily,position);
    }

    public void setHighlightedPosition(int position) {
        if (position == mHighlightedPosition) { // 如果点击的是已经高亮的位置，则不做处理
            return;
        }
        int prePosition = mHighlightedPosition;
        mHighlightedPosition = position;
        notifyItemChanged(prePosition);
        notifyItemChanged(mHighlightedPosition);
    }
    @Override
    public int getItemCount() {
        return maindates == null ? 0 : maindates.size();
    }
    class mainViewHolder extends RecyclerView.ViewHolder {
        TextView spokendetailText1;
        public mainViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        @SuppressLint("ResourceAsColor")
        public void bind(MainBean.InfoBean daily, int position) {
            spokendetailText1.setText(daily.getSentence());
            if (mHighlightedPosition == position) {
                spokendetailText1.setTextColor(Color.parseColor("#F2A750"));
            } else {
                spokendetailText1.setTextColor(Color.BLACK);
            }
            if(position==0){
                firstTime=daily.getTiming();
            }
            if(Constant.flag==1){
                int nextPosition = position + 1;
                if (nextPosition < getItemCount()) {
                    position = nextPosition;
                    notifyItemChanged(nextPosition);
                }
            }
            updateSubtitleColor(position, daily);
        }
        //句子变色
        private void updateSubtitleColor(int position, MainBean.InfoBean daily) {
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    if (currentPosition<=(int)(Float.parseFloat(firstTime) * 1000)) {
                        setHighlightedPosition(0);}
                    else if (currentPosition <Float.parseFloat(daily.getTiming()) * 1000 || currentPosition >Float.parseFloat(daily.getEndTiming()) * 1000) {
                    }


                    else {
                        setHighlightedPosition(position); // 设置高亮位置
                    }
                    mhandler.postDelayed(this, 1);
                }
            }, 1);
        }

    }
}