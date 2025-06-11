package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.network.Main.Constant;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadAdapter extends PagerAdapter {
    private Context mcontext;
    private Handler handler;
    private List<String> imageList;
    private List<String> positionsTime;
    private Runnable stopPlaybackRunnable;
    private Handler mHandler = new Handler();
    private Integer ratioX, ratioY;
    private String mString, sound;
    List<MainBean.InfoBean> list;
    MediaPlayer mediaPlayer=new MediaPlayer();
    private ArrayList<View> viewContainter = new ArrayList<>();

    public ReadAdapter(Context mcontext, ArrayList<View> viewContainter, List<String> imageList, List<MainBean.InfoBean> list) {
        this.mcontext = mcontext;
        this.imageList = imageList;
        this.list = list;
        this.viewContainter = viewContainter;
        //初始化音频
        playAudio();

    }


    @Override
    public int getCount() {
        return viewContainter.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = viewContainter.get(position);
        // 准备音频
        ImageView imageView = view.findViewById(R.id.imageView);
        String imageUrl = "http://staticvip.iyuba.cn/images/voa" + imageList.get(position);
        //绘制背景图片
        Glide.with(mcontext)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap mutableBitmap = resource.copy(Bitmap.Config.ARGB_8888, true); // 创建可修改的 Bitmap

                        Canvas canvas = new Canvas(mutableBitmap); // 创建 Canvas 对象
                        Paint paint = new Paint(); // 创建画笔对象
                        paint.setColor(Color.RED); // 设置画笔颜色
                        paint.setStyle(Paint.Style.STROKE); // 设置画笔样式为描边
                        paint.setStrokeWidth(5f); // 设置描边宽度
//                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        // 定义一个框的坐标数组（示例中包含两个框）
                        for (int p = 0; p < list.size(); p++) {
                            if(imageList.get(position).equals(list.get(p).getImgPath())){
                                String str = list.get(p).getPosition();

                                String tuples = str.replaceAll("[,\\(\\)]", " ");
                                String[] num = tuples.split("\\s+");
                                for (int j = 1; j < num.length; j = j + 4) {
                                    // 获取框的坐标信息
                                    int left = Integer.parseInt(num[j]);
                                    int top = Integer.parseInt(num[j + 1]);
                                    int right = Integer.parseInt(num[j + 2]);
                                    int bottom = Integer.parseInt(num[j + 3]);

                                    // 在指定的坐标范围内绘制边框
                                    canvas.drawRect(left, top, right, bottom, paint);
                                }
                            }
                        }

                        imageView.setImageBitmap(mutableBitmap); // 将修改后的 Bitmap 显示在 ImageView 中
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Do nothing
                    }
                });

//         设置触摸监听器
        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int flag = -1;
                int begin = 0, end = 0;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (stopPlaybackRunnable != null) {
                        handler.removeCallbacks(stopPlaybackRunnable);  // 移除之前的 Runnable
                    }
                    float x = event.getX();
                    float y = event.getY();
                    float width = imageView.getWidth();
                    float height = imageView.getHeight();
                    ratioX = (int) (x / width * 750);
                    ratioY = (int) (y / height * 1060);
                    for (int i = 0; i < list.size(); i++) {
                        String str = list.get(i).getPosition();
                        String tuples = str.replaceAll("[,\\(\\)]", " ");
                        String[] num = tuples.split("\\s+");
                        if (Integer.parseInt(num[1]) <= ratioX && ratioX <= Integer.parseInt(num[3]) && Integer.parseInt(num[2]) <= ratioY && ratioY <= Integer.parseInt(num[4]) && list.get(i).getImgPath().equals(imageList.get(position))) {
                            if (flag == -1) {
                                flag = i;
                                begin = (int) Float.parseFloat(list.get(flag).getTiming()) * 1000;
                                end = (int) Float.parseFloat(list.get(flag).getEndTiming()) * 1000;
                            }
                            flag = i;
                            if ((int) Float.parseFloat(list.get(flag).getTiming()) * 1000 < begin) {
                                begin = (int) Float.parseFloat(list.get(flag).getTiming()) * 1000;
                            }
                            if ((int) Float.parseFloat(list.get(flag).getEndTiming()) * 1000 > end) {
                                end = (int) Float.parseFloat(list.get(flag).getEndTiming()) * 1000;
                            }
                        }
                    }

                    if (flag == -1) {
                        mediaPlayer.start();
                        mediaPlayer.pause();
//                       return false;
                    } else {
                        mediaPlayer.seekTo(begin);
                        mediaPlayer.start();
                        handler = new Handler();
                        stopPlaybackRunnable = new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.pause();
                                handler.removeCallbacks(stopPlaybackRunnable);
                            }
                        };
                        int stopDelay = end - begin+500;// 停止播放的延迟时间，单位为毫秒
                        handler.postDelayed(stopPlaybackRunnable, stopDelay);  // 延迟 1 秒执行 Runnable
                        return true;
                    }
                }
                return false;
            }

        });
        //添加view
        container.addView(view);
        return view;
    }

    //获取mediaplay
    public MediaPlayer getMediaPlayer() {
        if(mediaPlayer!=null){
            return mediaPlayer;
        }else{
            mediaPlayer=new MediaPlayer();
            return mediaPlayer;
        }

    }

    private void playAudio() {
        //获取音频
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(Constant.MUSIC);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void realss() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

        if (handler != null) {
            handler.removeCallbacks(stopPlaybackRunnable);
            handler = null;
        }


    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewContainter.get(position));
//        mediaPlayer.release();
        mHandler.removeCallbacksAndMessages(null);
    }
}
