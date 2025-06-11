package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.network.Main.Constant;

import java.io.IOException;
import java.util.List;

public class WordHomeSmallAdapter extends RecyclerView.Adapter<WordHomeSmallAdapter.WordViewHolder> {
    private List<WordBean.DataBean.WordsBean> words;
    private Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private WordHomeAdapter.OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(WordHomeAdapter.OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public WordHomeSmallAdapter(Context context, List<WordBean.DataBean.WordsBean> words) {
        this.words = words;
        this.context = context;
    }

    @Override
    public WordHomeSmallAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_word, parent, false);
        return new WordHomeSmallAdapter.WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHomeSmallAdapter.WordViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pinyin.setText(words.get(position).getPhonetic());
        holder.chinese.setText(words.get(position).getWord());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareMedia(words.get(position).getSound());
            }
        });

    }
    @Override
    public int getItemCount() {
        return words.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {
        TextView pinyin, chinese;
        LinearLayout linearLayout;

        WordViewHolder(@NonNull View itemView) {
            super(itemView);
            pinyin = itemView.findViewById(R.id.pinyin);
            chinese = itemView.findViewById(R.id.chinese);
            linearLayout = itemView.findViewById(R.id.liney);
        }
    }
    private  void prepareMedia(String music){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

// 设置错误监听器
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // 在发生错误时处理逻辑
                return false;
            }
        });

// 设置准备完成监听器
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 准备完成后开始播放
                mp.start();
            }
        });

// 设置播放完成监听器
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 播放完成后的处理逻辑
            }
        });

        mediaPlayer.prepareAsync(); // 异步准备媒体资源
    }
}
