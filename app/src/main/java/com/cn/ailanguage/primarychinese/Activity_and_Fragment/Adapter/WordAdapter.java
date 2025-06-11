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
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.network.Main.Constant;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private List<MainBean.WordsBean> words;
    private Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    public WordAdapter(Context context, List<MainBean.WordsBean> words) {
        this.words = words;
        this.context = context;
    }

    @Override
    public WordAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.word_small, parent, false);
        return new WordAdapter.WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.WordViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pinyin.setText(words.get(position).getPhonetic());
        holder.chinese.setText(words.get(position).getWord());
        //点击时的反应，出声
        holder.linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 手指按下时的处理逻辑
//                        Log.d("xwh0", words.get(position).getWord());
                        playAudio(words.get(position).getSound());

                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 手指移动时的处理逻辑

                        break;
                    case MotionEvent.ACTION_UP:
                        // 手指抬起时的处理逻辑
                        break;
                }
                return true; // 返回true表示已经处理了该触摸事件
            }
        });
    }
    //初始化adapter
    private void playAudio(String audioUrl) {
        if (mediaPlayer.isPlaying() || mediaPlayer != null) {
            mediaPlayer.pause();
        }
        try {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 播放完成后的处理逻辑
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


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
}
