package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderUserBean;
import com.cn.ailanguage.primarychinese.Bean.MusicBean;
import com.cn.ailanguage.primarychinese.Bean.ShadowingBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.ShadowingContract;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Shadowing.ShadowingNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.ShadowingPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;

public class LeaderSmallActivity extends AppCompatActivity implements ShadowingContract.ShadowingView {
    private TextView name, score, title, total_socre, data;
    private ImageView image, begin, backButton, back;
    private CardView cardView;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //private Button ;
    String nameString, imageString, music, uid;
    ShadowingPresenter shadowingPresenter;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_small);
        name = findViewById(R.id.name_leader);
        score = findViewById(R.id.score_leader);
        image = findViewById(R.id.image_leader);
        begin = findViewById(R.id.begin_leader);
        total_socre = findViewById(R.id.total_score);
        data = findViewById(R.id.leader_data);
        cardView = findViewById(R.id.card);
        ShadowingNetWorkManager.getInstance().init();
        shadowingPresenter = new ShadowingPresenter();
        shadowingPresenter.attchView((ShadowingContract.ShadowingView) this);

        cardView.setVisibility(View.GONE);
        Glide.with(this)
                .load(R.mipmap.person) // 加载图片的地址
                .circleCrop()
                .placeholder(R.mipmap.person) // 占位图，图片正在加载时显示
                .error(R.mipmap.person) // 错误图，图片加载失败时显示
                .into(image); // 将图片设置到ImageView中
        //得到intent传递的参数
        Intent intent = getIntent();
        if (intent != null) {
            nameString = intent.getStringExtra("name");
            imageString = intent.getStringExtra("image");
            uid = String.valueOf(intent.getStringExtra("uid"));
        }


        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }



        Glide.with(this)
                .load(imageString) // 加载图片的地址
                .circleCrop()
                .placeholder(R.mipmap.person) // 占位图，图片正在加载时显示
                .error(R.mipmap.person) // 错误图，图片加载失败时显示
                .into(image); // 将图片设置到ImageView中
        name.setText(nameString);
        // 获取当前日期


        backButton = findViewById(R.id.back_leader);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }

                finish();
            }
        });
        //标题
        title = findViewById(R.id.title_leader);
        title.setText(String.valueOf(Constant.TITLE));


//开始播放按按钮
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    begin.setImageResource(R.mipmap.stop);
                } else {
                    mediaPlayer.pause();
                    begin.setImageResource(R.mipmap.begin);
                }

            }
        });
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

        Log.d("xwh00", uid);
        shadowingPresenter.getLeaderUser("4", "primarychinese", Constant.VOAID, uid, MD5.md5(uid + "getWorksByUserId" + formattedDate));
    }

    @Override
    public void getLeaderUser(LeaderUserBean leaderuserBean) {
        //获取个人信息
        score.setText("分数:" + leaderuserBean.getData().get(0).getScore());
        total_socre.setText("总分:" + leaderuserBean.getData().get(0).getScore());
        String a = String.valueOf(leaderuserBean.getData().get(0).getCreateDate()).substring(0, 10);
        data.setText(a);
        music = "https://ai.aienglish.ltd/voa/" + leaderuserBean.getData().get(0).getShuoShuo();
        Log.d("xwh89", music);
        prepareMusic(music);

    }

    public void prepareMusic(String one) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }else{
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(one);
            mediaPlayer.prepareAsync();
//            Log.d("xwh9", music);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayerl) {
                mediaPlayerl.pause();
                begin.setImageResource(R.mipmap.begin);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // 在这里处理播放出错的逻辑
                cardView.setVisibility(View.GONE);
                return false;
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 媒体资源已准备好进行播放
                cardView.setVisibility(View.VISIBLE);
//                mediaPlayer.start();
            }
        });

    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        super.onDestroy();
    }


    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void getHome(ShadowingBean shadowingBean) {

    }

    @Override
    public void getEvaluating(EvaBean evaBean) {

    }

    @Override
    public void getMusic(MusicBean musicBean) {

    }

    @Override
    public void getLeader(LeaderBean leaderBean) {

    }


}