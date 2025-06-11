package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;

import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.HomeFragment.isNetworkConnected;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.PlaybackParams;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.Dialog;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.Text2Adapter;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.DialogVip;
import com.cn.ailanguage.primarychinese.Bean.AdEntryBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Bean.MainBean;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Sentence;
import com.cn.ailanguage.primarychinese.SQLBase.MySqlHelpter;
import com.cn.ailanguage.primarychinese.View.InitContract;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.InitPresenter;
import com.cn.ailanguage.primarychinese.presenter.MainPresenter;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.iyuba.module.toolbox.DensityUtil;
import com.yd.saas.base.interfaces.AdViewBannerListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdBanner;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 首页原文
 */
public class TextFragment extends Fragment implements View.OnClickListener, MainContract.MainView, AdViewBannerListener, InitContract.InitView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;
    Text2Adapter adapter1;
    private View rootView;
    List<Sentence> sentence = new ArrayList<>();
    private ReadFragment readFragment;
    private RecyclerView ChineseLayout, PinyinLayout;
    private static Toast currentToast;
    private SeekBar seekbar;
    private TextView lefttime, righttime, change_of_speed, pome_title, pome_author;
    private MainPresenter mainPresenter;
    private ImageView fast, slow, play, continue1;
    private MediaPlayer mediaPlayer;
    private String message;
    private String voaid;
    private List<String> sentenceTime = new ArrayList<>(), sentenceEndTime = new ArrayList<>();
    private Handler mHandler = new Handler();
    private static final String TAG = "TextFragment";

    private boolean isServiceRunning = false;
    private LinearLayout linearLayout, pome_line;
    Toast toast = null;

    //静态变量声明
    private int currentPosition;
    private MySqlHelpter mySQLiteOpenHelper;

    private SQLiteDatabase db;
    List<MainBean.InfoBean> data;
    List<List<String>> voaidID = new ArrayList<>();
    int flag = 0;

    private int aid = -1; //检测变化
    private int bid = -1; //检测变化
    private LinearLayout banner;
    private InitPresenter initPresenter;

    private String adkey = "0586";


    public TextFragment() {
        // Required empty public constructor
    }

    public static TextFragment newInstance(String param1, String param2) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        NetWorkManager.getInstance().initDev();
        initPresenter = new InitPresenter();
        initPresenter.attchView(this);

        if(!com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.uid.equals("nothing")){
            initPresenter.getAdEntryAll(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.APPID + "", 4, com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.uid);
        }else {

            Log.e("lhz", "onCreate: " + 0 );
            initPresenter.getAdEntryAll(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.APPID+"", 4, "0");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_text, container, false);
        play = rootView.findViewById(R.id.play);
        righttime = rootView.findViewById(R.id.righttime);
        lefttime = rootView.findViewById(R.id.lefttime);
        slow = rootView.findViewById(R.id.slow);
        fast = rootView.findViewById(R.id.fast);
        change_of_speed = rootView.findViewById(R.id.change_of_speed);
        seekbar = rootView.findViewById(R.id.seekBar);
        continue1 = rootView.findViewById(R.id.continue1);
        pome_author = rootView.findViewById(R.id.pome_auther);
        pome_line = rootView.findViewById(R.id.pome_line);
        pome_title = rootView.findViewById(R.id.pome_title);
        ChineseLayout = rootView.findViewById(R.id.Chinese);
        banner = rootView.findViewById(R.id.banner);

        //连接数据库
        mySQLiteOpenHelper = new MySqlHelpter(getContext());
        db = mySQLiteOpenHelper.getWritableDatabase();

        Constant.EWHIC = 0;

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset(); // 先重置MediaPlayer
        }
        //一二年纪以外显示拼音
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        String category = sharedPreferences.getString("category", "466");
        if (!(category.equals("466") || category.equals("467") || category.equals("468") || category.equals("469"))) {
            Constant.SHOW=1;
        }else{
            Constant.SHOW=0;
        }
        change_of_speed.setText(Constant.SPEED / 10.0 + "x");
        message = Constant.MUSIC;
        voaid = Constant.VOAID;
        //准备音频
        prepareMediaPlayer();
        // 设置音频流类型
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        // 设置播放完成监听器
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // 在这里处理音频播放过程中的错误逻辑
                return true;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //完成播放后恢复初始状态
                seekbar.setProgress(0);
                lefttime.setText("00:00");
                mHandler.removeCallbacks(mRunnable);//如果有，清除上一个mRunnable
                play.setBackgroundResource(R.mipmap.begin);
                //当即将完成播放，进度条到头的时候
                if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING < com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_MAX) {
                    //已经点击顺序播放
                    if (Constant.flag == 1) {
                        Constant.COMPETE = 1;
                        Intent intent = new Intent(getContext(), OriDetailActivity.class);
                        startActivity(intent);
                    }
                    //已经点击循环播放
                    else if (Constant.flag == 2 && Constant.COMPETE == 1) {
                        play.performClick();
                    }
                    //已经点击乱序播放
                    else if (Constant.flag == 3) {
                        Constant.COMPETE = 1;
                        Intent intent = new Intent(getContext(), OriDetailActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        //循环播放,顺序播放，乱序播放
        continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Dialog dialog = new Dialog(getContext(), "请先登录", "去登录", "取消");
                    dialog.ShowDialog();
                } else {
                    if (Constant.flag == 0) {
                        Constant.flag = 1;
                        continue1.setImageResource(R.mipmap.next_music_continue);//改一下图标
                        //取消上一次的toast，弹出最新的toast
                        if (currentToast != null) {
                            currentToast.cancel();
                        }
                        currentToast = Toast.makeText(getContext(), "顺序播放", Toast.LENGTH_SHORT);
                        currentToast.show();

                    } else if (Constant.flag == 1) {
                        Constant.flag = 2;
                        continue1.setImageResource(R.mipmap.continue_ing);
                        if (currentToast != null) {
                            currentToast.cancel();
                        }
                        currentToast = Toast.makeText(getContext(), "循环播放", Toast.LENGTH_SHORT);
                        currentToast.show();

                    } else if (Constant.flag == 2) {
                        Constant.flag = 3;
                        continue1.setImageResource(R.mipmap.random);
                        if (currentToast != null) {
                            currentToast.cancel();
                        }
                        currentToast = Toast.makeText(getContext(), "乱序播放", Toast.LENGTH_SHORT);
                        currentToast.show();

                    } else {
                        Constant.flag = 0;
                        continue1.setImageResource(R.mipmap.normal_icon);
                        if (currentToast != null) {
                            currentToast.cancel();
                        }
                        currentToast = Toast.makeText(getContext(), "单曲播放", Toast.LENGTH_SHORT);
                        currentToast.show();

                    }
                }

            }
        });
        //快进和后退两个按钮
        fast.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    int newPosition = 0;
                    //判断时间，判断播放到那一句了
                    for (int i = 0; i < sentenceTime.size() - 1; i++) {
                        if (Float.parseFloat(String.valueOf(sentenceEndTime.get(i))) * 1000 > (float) (currentPosition - 1000)) {
                            newPosition = (int) Float.parseFloat(String.valueOf(sentenceTime.get(i + 1))) * 1000 + 500;
                            Constant.EWHIC = i;
                            break;
                        }
                    }
                    // 跳转到新位置
                    mediaPlayer.seekTo(newPosition);
                    updateSeekBar();//更新进度条
                }
            }
        });
        //后退
        slow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    int newPosition = 0;
                    //判断时间
                    for (int i = 0; i < sentenceTime.size(); i++) {
                        if (Float.parseFloat(String.valueOf(sentenceEndTime.get(i))) * 1000 > (float) (currentPosition) && i >= 1) {
                            newPosition = (int) Float.parseFloat(String.valueOf(sentenceTime.get(i - 1))) * 1000 + 500;
                            Constant.EWHIC = i;
                            break;
                        }
                    }
                    // 跳转到新位置
                    mediaPlayer.seekTo(newPosition);
                    updateSeekBar();
                }
            }
        });

        //播放按钮点击事件
        play.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setBackgroundResource(R.mipmap.begin);
                    play.setTag("begin");
                    updateSeekBar();
                } else {
                    mediaPlayer.start();
                    play.setBackgroundResource(R.mipmap.stop);
                    play.setTag("stop");
                    updateSeekBar();
                }
            }
        });
        //  进度条拖动事件
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    //判断播放到那一句了
                    for (int i = 0; i < sentenceTime.size(); i++) {
                        if (Float.parseFloat(String.valueOf(sentenceEndTime.get(i))) * 1000 > (float) (currentPosition)) {
                            Constant.EWHIC = i;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //下拉框选择倍数
        change_of_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断有没有登录
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Dialog dialog = new Dialog(getContext(), "请先登录", "去登录", "取消");
                    dialog.ShowDialog();
                } else {
                    //判断是不是会员
                    if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME.equals("0")) {
                        DialogVip dialogVip = new DialogVip(getContext(), "会员才能倍数播放哦,请先购买会员", "去购买", "取消");
                        dialogVip.ShowDialog();
                    } else {
                        if (Constant.SPEED >= 20 || Constant.SPEED < 8) {
                            Constant.SPEED = 8;
                        } else {
                            Constant.SPEED = Constant.SPEED + 2;
                        }
                        float speed = (float) (Constant.SPEED / 10.0);
                        PlaybackParams params = mediaPlayer.getPlaybackParams();
                        params.setSpeed(speed);
                        mediaPlayer.setPlaybackParams(params);
                        change_of_speed.setText(Constant.SPEED / 10.0 + "x");
                        play.setBackgroundResource(R.mipmap.stop);
                    }

                }

            }
        });
        //判断联网情况
        boolean isConnected = isNetworkConnected(getContext());
        if (isConnected) {
            // 设备已联网
            //书内容的网络连接
            MainNetWorkManager.getInstance().init();
            mainPresenter = new MainPresenter();
            mainPresenter.attchView(this);
            mainPresenter.getHome("detail", voaid);
        } else {

            Log.e("qxy", "nnooNNeet" );
            // 设备未联网
            Combine();
        }
        return rootView;
    }
    //读取数据库
    private void Combine() {
        //判断之前有没有读取过text
        if (!isDatabaseExists("text.db")) {
            copyDatabase();
        }
        // 打开数据库连接
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getContext().getDatabasePath("text.db").getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        // 执行查询操作
        String query = "SELECT * FROM tablebookline WHERE voaid = ?";
        String[] selectionArgs = {String.valueOf(voaid)};


        Cursor cursor = db.rawQuery(query, selectionArgs);
        int columnCount = cursor.getColumnCount();
        Log.d("xwh77", "Combine: "+columnCount);
        List<List<String>> columnDataLists = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            columnDataLists.add(new ArrayList<>());
        }

        while (cursor.moveToNext()) {
            for (int i = 0; i < columnCount; i++) {
                String columnValue = cursor.getString(i);
//                Log.d("xwh88", columnValue);
                columnDataLists.get(i).add(columnValue);
            }
        }
        for (int i = 0; i < columnDataLists.size(); i++) {
            voaidID.add(columnDataLists.get(i));
        }
        cursor.close();
        db.close();
        dataDeal(voaidID);//处理布局
    }

    private void copyDatabase() {
        try {

            InputStream inputStream = getContext().getAssets().open("text.db");
            String outFileName = getContext().getDatabasePath("text.db").getPath();
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isDatabaseExists(String dbName) {
        File dbFile = getContext().getApplicationContext().getDatabasePath(dbName);
        return dbFile.exists();
    }
    //没有联网时的布局处理
    private void dataDeal(List<List<String>> lists) {
        //对古诗2和诗歌3进行重新布局
        if (com.cn.ailanguage.primarychinese.network.Home.Constant.POEM == 2) {
            pome_line.setVisibility(View.VISIBLE);
            pome_title.setText(Constant.TITLE);
            pome_title.setTextSize(Constant.CHINESESIZE - 4);
            pome_author.setTextSize(Constant.CHINESESIZE - 6);
            pome_author.setText(Constant.POMEAUTHOR);

        } else {
            if (com.cn.ailanguage.primarychinese.network.Home.Constant.POEM == 3) {
                pome_line.setVisibility(View.VISIBLE);
                pome_title.setText(Constant.TITLE);
                pome_title.setTextSize(Constant.CHINESESIZE - 4);
                pome_author.setTextSize(Constant.CHINESESIZE - 6);
                pome_author.setText(Constant.POMEAUTHOR);
            } else {
                pome_line.setVisibility(View.GONE);
            }

        }

        Log.e("qxy", "noNet da "+ voaidID.get(5) + voaidID.get(8));
        //线程细化处理数据
        DataRun dataRun = new DataRun(voaidID.get(5), voaidID.get(8));
        new Thread(dataRun).start();
    }


    //对Pinyin进行处理，对连起来的标点符号前后都加一个空格
    public static String addSpacesAfterPunctuation(String text) {
        return text.replaceAll("([”“。_,、—.!)(，?;:\\\"'])\\s*|\\s*([”“。_,.!、)(，?;:\\\"'])", " $1$2 ");
    }

    //异步准备音频、监听mediaplay开始
    private void prepareMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.setDataSource(message);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    updateSeekBar();
                    seekbar.setMax(mediaPlayer.getDuration());
                    if (Constant.flag == 1 || Constant.flag == 3) {
                        mediaPlayer.start();
                        play.setBackgroundResource(R.mipmap.stop);
                        play.setTag("stop");
                        updateSeekBar();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //更新进度条
    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 0); // 每秒更新一次进度条
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                if (adapter1 != null) {
                    if (Constant.canChange)
                    {
                        adapter1.notifyDataSetChanged();
                        Constant.canChange = false;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (Exception e) {
                // 处理IOException异常
                e.printStackTrace();
            }
            //左右进度条时间
            String currentPositionStr = StringUtils.leftPad(String.valueOf(currentPosition / 1000 / 60), 2, "0") + ":" + StringUtils.leftPad(String.valueOf(currentPosition / 1000 % 60), 2, "0");
            if (currentPosition >= 0) {
                lefttime.setText(String.valueOf(currentPositionStr));
            } else {
                lefttime.setText("00:00");
            }
            int totalDuration = mediaPlayer.getDuration();
            String totalDurationstr = StringUtils.leftPad(String.valueOf(totalDuration / 1000 / 60), 2, "0") + ":" + StringUtils.leftPad(String.valueOf(totalDuration / 1000 % 60), 2, "0");
            if (totalDuration >= 0 && (totalDuration / 1000 / 60) < 60) {
                righttime.setText(String.valueOf(totalDurationstr));
            } else if ((totalDuration / 1000 / 60) > 60) {
                prepareMediaPlayer();
            } else {

                righttime.setText("00:00");
            }
            seekbar.setProgress(mediaPlayer.getCurrentPosition());
            //监听位置，方变色处理
            for (int i = 0; i < sentenceTime.size(); i++) {
                if (currentPosition >= Float.parseFloat(sentenceEndTime.get(i)) * 1000) {
                } else {

                    if (i != aid)
                    {
                        aid = i;
                        try {
                            if (adapter1 != null) {
                                adapter1.setCurPosition(aid * 2);
                                adapter1.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                }
            }

            //判断到那一句了
            for (int i = 0; i < sentenceTime.size(); i++) {
                if (Float.parseFloat(String.valueOf(sentenceEndTime.get(i))) * 1000 > (float) (currentPosition)) {

                    if (i != bid)
                    {
                        bid = i;
                        Constant.EWHIC = bid;
                    }
                    break;
                }
            }

            mHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void getHome(MainBean mainBean) {
        //对数据进行处理
        List<String> Pinyin = new ArrayList<>(), Chinese = new ArrayList<>();
        List<MainBean.InfoBean> list = mainBean.getInfo();
        //把每个开头时间强制设置为0
        sentenceTime.add("0");
        String nextText = list.get(0).getSentence(), nextTextPing = list.get(0).getSentencePhonetic(), nextTime, nextEndTime;
        nextTextPing = nextTextPing + "  ";

        if (com.cn.ailanguage.primarychinese.network.Home.Constant.POEM == 2) {
            pome_line.setVisibility(View.VISIBLE);
            pome_title.setText(Constant.TITLE);
            pome_title.setTextSize(Constant.CHINESESIZE - 4);
            pome_author.setTextSize(Constant.CHINESESIZE - 6);
            pome_author.setText(Constant.POMEAUTHOR);
            //是古诗
            for (int i = 0; i < list.size(); i++) {
                Chinese.add(list.get(i).getSentence());
                Pinyin.add(list.get(i).getSentencePhonetic());
                sentenceEndTime.add(list.get(i).getEndTiming());
                if (i != 0) {
                    sentenceTime.add(list.get(i).getTiming());
                }
            }
        } else {
            if (com.cn.ailanguage.primarychinese.network.Home.Constant.POEM == 3) {
                pome_line.setVisibility(View.VISIBLE);
                pome_title.setText(Constant.TITLE);
                pome_title.setTextSize(Constant.CHINESESIZE - 4);
                pome_author.setTextSize(Constant.CHINESESIZE - 6);
                pome_author.setText(Constant.POMEAUTHOR);
            } else {
                pome_line.setVisibility(View.GONE);
            }
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).getParaId().equals(list.get(i + 1).getParaId())) {
                    nextText = nextText + list.get(i + 1).getSentence();
                    nextTextPing = nextTextPing + " " + list.get(i + 1).getSentencePhonetic();
                    nextTextPing = nextTextPing + " ";
                } else {
                    nextTime = list.get(i + 1).getTiming();
                    nextEndTime = String.valueOf(Float.parseFloat(nextTime) - 0.2);
                    sentenceEndTime.add(nextEndTime);
                    sentenceTime.add(nextTime);

                    Chinese.add(nextText);
                    Pinyin.add(nextTextPing);
                    nextText = list.get(i + 1).getSentence();
                    nextTextPing = list.get(i + 1).getSentencePhonetic();
                }


            }
            Chinese.add(nextText);
            Pinyin.add(nextTextPing);
            sentenceEndTime.add(list.get(list.size() - 1).getEndTiming());
        }
        //线程细化处理数据
        Log.d("xwh678", Chinese.toString()+"347");
        DataRun dataRun = new DataRun(Chinese, Pinyin);
        new Thread(dataRun).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.release(); // 释放 MediaPlayer 资源
        }
        play.setBackgroundResource(R.mipmap.begin);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        play.setBackgroundResource(R.mipmap.begin);
    }

    @Override
    public void onStop() {

        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        //变图标
        if (Constant.flag == 1) {
            play.performClick();
            continue1.setImageResource(R.mipmap.next_music_continue);
        } else if (Constant.flag == 2) {
            play.performClick();
            continue1.setImageResource(R.mipmap.continue_ing);
        } else if (Constant.flag == 3) {
            play.performClick();
            continue1.setImageResource(R.mipmap.random);
        } else {
            continue1.setImageResource(R.mipmap.normal_icon);
        }
        if (!mediaPlayer.isPlaying()) {
            play.setBackgroundResource(R.mipmap.begin);
        }
        mediaPlayer.pause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        String category = sharedPreferences.getString("category", "466");
        if (!(category.equals("466") || category.equals("467") || category.equals("468") || category.equals("469"))) {
            //一二年纪以外显示拼音
            Constant.SHOW=0;
        }else{
            Constant.SHOW=1;
        }
    }

    @Override
    public void onClick(View v) {

    }

    //传递给MusicActivity，当按返回键使停止播放
    public void textreals() {
        play.setBackgroundResource(R.mipmap.begin);

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 当前Fragment被隐藏时，停止音乐播放
            play.setBackgroundResource(R.mipmap.begin);
            if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                mediaPlayer.pause();

            }

        }
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
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {
        AdEntryBean.DataDTO dataDTO = adEntryBean.getData();
        String type = dataDTO.getType();

        Log.d("fang111",type+"");

            if (type.equals(Constant.AD_ADS1) || type.equals(Constant.AD_ADS2) || type.equals(Constant.AD_ADS3)
                    || type.equals(Constant.AD_ADS4) || type.equals(Constant.AD_ADS5)) {

                DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = DensityUtil.dp2px(requireContext(), 65);

                YdBanner mBanner = new YdBanner.Builder(requireContext())
                        .setKey(adkey)
                        .setWidth(width)
                        .setHeight(height)
                        .setMaxTimeoutSeconds(5)
                        .setBannerListener(this)
                        .build();

                mBanner.requestBanner();
            }


    }


    @Override
    public void onReceived(View view) {


        if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME.equals("0"))
        {
            banner.setVisibility(View.VISIBLE);

            banner.removeAllViews();
            banner.addView(view);
        }
    }

    @Override
    public void onAdExposure() {
//        banner.setVisibility(View.VISIBLE);

        Log.e("lhz", "onAdExposure: " );

    }

    @Override
    public void onAdClick(String s) {

    }

    @Override
    public void onClosed() {
        banner.setVisibility(View.GONE);
    }

    @Override
    public void onAdFailed(YdError ydError) {
        Log.e("lhz", "onAdFailed: " + ydError);
//        Toast.makeText(this.getContext(), ydError.getMsg(),Toast.LENGTH_SHORT).show();

    }


    class DataRun implements Runnable {

        List<String> chineseList, Pinyin;

        public DataRun(List<String> stringlist, List<String> Pinyin) {
            this.chineseList = stringlist;
            this.Pinyin = Pinyin;
        }

        @SuppressLint("NewApi")
        @Override
        public void run() {
            for (int i = 0; i < chineseList.size(); i++) {
                //对数据进行处理
                List<String> ChineseList = new ArrayList<>();
                String[] ChineseArray = null;
                String[] PinyinArray = null;
                List<String> PinyinList = new ArrayList<>();
                List<String> result = new ArrayList<>();
                ChineseArray = addSpacesAfterPunctuation(chineseList.get(i)).split(" ");
                //对中文标点进行处理
                List<String> list = new ArrayList<>();
                String regex = "[\\u4e00-\\u9fa5]+";
                Pattern pattern = Pattern.compile(regex);
                for (String str : ChineseArray) {
                    if (pattern.matcher(str).find()) {
                        String result1 = addSpaceBeforeChinese(str);
                        String[] subArr = result1.split(" "); // 使用空字符串进行拆分
                        for (String subStr : subArr) {
                            list.add(subStr);
                        }
                    } else {
                        list.add(str); // 如果不包含中文字符，直接添加到列表中
                    }
                }
                ChineseList.addAll(list);
                PinyinArray = addSpacesAfterPunctuation(Pinyin.get(i)).split(" ");
//                for (int p = 0; p < PinyinArray.length; i++) {
//                    PinyinArray[p] = PinyinArray[p].replace("......", "... ");
//                }
                for (int j = 0; j < PinyinArray.length; j++) {
                    PinyinList.add(PinyinArray[j]);
                }
                Log.d("xwh1", PinyinList + "*");
                //对包含的中文年份处理
                result = splitList(ChineseList);
                ChineseList.clear();
                ChineseList.addAll(result);

                //去除空格，合并
                ChineseList.removeIf(element -> element.trim().isEmpty());
                PinyinList.removeIf(element -> element.trim().isEmpty());

                //如果拼音g-u-hi这种
                List<String> resultList = new ArrayList<>();
                for (String item : PinyinList) {
                    if (item.contains("-")) {
                        String[] parts = item.split("(?=-)|(?<=-)");
                        resultList.addAll(Arrays.asList(parts));
                    } else {
                        resultList.add(item);
                    }
                }
                PinyinList.clear();
                //去除拼音每个单词的null
                for (String str : resultList) {
                    if (str != null) {
                        String cleanedStr = str.replace("null", "");
                        PinyinList.add(cleanedStr);
                    }
                }
                //每个拼音的标点为空
                for (int k = 0; k < PinyinList.size(); k++) {
                    String item = PinyinList.get(k);
                    //将拼音的标点符号不显示
                    if (item.length() == 1) { // 判断字符串长度是否为1
                        char c = item.charAt(0);
                        if (isPunctuationOrChineseCharacter(c)) { // 使用之前提供的判断函数
                            PinyinList.set(k, " "); // 将符合条件的字符串替换为空格
                        }
                    }
                    //对pinyin的省略号进行处理
                    if (item.equals("……")) {
                        Log.d("ppp", "run: ");
                        PinyinList.set(k, " ");
                        PinyinList.add(k, " ");
                    }
                    //去掉拼音的数字
                    if (StringUtils.isNumeric(item)) {
                        PinyinList.set(k, " ");
                    }
                }
                //拼音数据处理完成

                //中文g-u-a问题
                List<String> resultList1 = new ArrayList<>();
                for (String item : ChineseList) {
                    if (item.contains("-")) {
                        String[] parts = item.split("(?=-)|(?<=-)");
                        resultList1.addAll(Arrays.asList(parts));
                    } else {
                        resultList1.add(item);
                    }
                }
                ChineseList.clear();

                //中文再次去每个元素两端空格
                for (String str : resultList1) {
                    if (str != null) {
                        String cleanedStr = str.replace("null", "");
                        ChineseList.add(cleanedStr);
                    }
                }
                /**
                 * 每一段都分割
                 * 在每一段后面加个空数，这个数特别长
                 */
                Paint paint = new Paint();
                paint.setTextSize(Constant.PINYINSIZE); // 设置字体大小，单位为像素
                float width = paint.measureText(" "); // 获取字母 " " 的宽度

                try {
                    // 执行可能抛出异常的操作
                    // 比如添加、替换或移除Fragment的操作
                } catch (IllegalStateException e) {
                    // 在这里捕获到异常后进行处理
                    // 显示一个Toast提示用户出现了异常
                    Toast.makeText(getContext(), "发生错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (getContext() != null) {
                    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                    int screenWidth = (int) (displayMetrics.widthPixels / width);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int j = 0; j < screenWidth; j++) {
                        stringBuilder.append(" ");
                    }//得到一个长的空字符串

                    //添加到句子里面
                    ChineseList.add(String.valueOf(stringBuilder));
                    PinyinList.add(String.valueOf(stringBuilder));
                }


                //中文处理完成
                sentence.add(new Sentence(ChineseList, PinyinList));

            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    List<String> ch = new ArrayList<>();
                    List<String> en = new ArrayList<>();
                    List<Integer> flaging = new ArrayList<>();
                    for (int i = 0; i < sentence.size(); i++) {
                        ch.addAll(sentence.get(i).getChinsese());
                        en.addAll(sentence.get(i).getPinyin());
                        for (int j = 0; j < sentence.get(i).getChinsese().size(); j++) {
                            flaging.add(i);

                        }
//
                    }


                    adapter1 = new Text2Adapter(getContext(), ch, en, flaging, mediaPlayer);
                    ChineseLayout.setAdapter(adapter1);
                    //流式布局处理
                    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
                    layoutManager.setFlexWrap(FlexWrap.WRAP);
                    if (com.cn.ailanguage.primarychinese.network.Home.Constant.POEM == 2 && getContext() != null) {
                        layoutManager.setJustifyContent(JustifyContent.CENTER); // 控制水平方向的居中对齐
                        int marginInDp = 20;
                        float scale = getContext().getResources().getDisplayMetrics().density;
                        int marginInPixels = (int) (marginInDp * scale + 0.5f);
                        // 设置 RecyclerView 边距
                        ChineseLayout.setPadding(marginInPixels, 0, marginInPixels, 0);
                    }
                    ChineseLayout.setLayoutManager(layoutManager);
                    // 在显示Toast之前禁用RecyclerView的触摸事件
                    ChineseLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        boolean isLastItemVisible = false;

//                        @Override
//                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//
//                            if (newState == RecyclerView.SCROLL_STATE_IDLE && isLastItemVisible) {
//                                if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING < com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_MAX - 1) {
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            // 跳转到目标Activity
//                                            Handler handler = new Handler(Looper.getMainLooper());
//                                            handler.removeCallbacksAndMessages(null);
//
//                                            Constant.SCROLL = 1;
//                                            if (getContext() != null) {
//                                                Intent intent = new Intent(getContext(), SmallHomeActivity.class);
//                                                startActivity(intent);
//                                            }
//                                        }
//                                    }, 1000); // 延迟2秒钟后跳转
//
//
//                                }
//
//
//                            }
//                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                            int totalItemCount = layoutManager.getItemCount();

                            // 判断是否滑动到最后一项
                            isLastItemVisible = lastVisibleItemPosition == totalItemCount - 1;
                        }

                    });

                }
            });
        }
    }
    public List<String> splitList(List<String> list) {
        List<String> resultList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (String str : list) {
            // 先按照数字字符切割出多个子串
            String[] subStrings = str.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

            // 将子串和非数字字符一起加入结果列表中
            for (String subStr : subStrings) {
                if (isAllDigits(subStr)) {
                    // 如果当前子串全是数字，则加入StringBuilder中
                    sb.append(subStr);
                } else {
                    // 如果当前子串不全是数字，则将之前的数字字符串和非数字子串都加入结果列表，并清空StringBuilder
                    if (sb.length() > 0) {
                        resultList.addAll(splitDigits(sb.toString()));
                        sb.setLength(0);  // 清空StringBuilder
                    }
                    resultList.add(subStr);
                }
            }
        }

        // 最后将最后一个数字字符串加入结果列表
        if (sb.length() > 0) {
            resultList.addAll(splitDigits(sb.toString()));
        }

        return resultList;
    }

    private boolean isAllDigits(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private List<String> splitDigits(String str) {
        List<String> resultList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isDigit(c)) {
                // 如果当前字符为数字，则加入StringBuilder中
                sb.append(c);
            } else {
                // 如果当前字符不是数字，则将之前的数字字符串加入结果列表，并清空StringBuilder
                if (sb.length() > 0) {
                    resultList.add(sb.toString());
                    sb.setLength(0);  // 清空StringBuilder
                }
            }
        }

        // 最后将最后一个数字字符串加入结果列表
        if (sb.length() > 0) {
            resultList.add(sb.toString());
        }

        return resultList;
    }

    private String addSpaceBeforeChinese(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isPunctuationOrChineseCharacter(c)) {
                stringBuilder.append("  ");
            }
            stringBuilder.append(c);
            if (isPunctuationOrChineseCharacter(c)) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    private boolean isPunctuationOrChineseCharacter(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        if (c == '\u2026') { // 省略号
            return true;
        }
        if (c == '.') {
            // 如果是点号，且前面已经有两个点号，则视为省略号，返回 true
            // 这里假设省略号前面已经有两个点号
            return previousTwoCharsAreDot();
        }
        return Character.isWhitespace(c) || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || Character.getType(c) == Character.CONNECTOR_PUNCTUATION
                || Character.getType(c) == Character.DASH_PUNCTUATION
                || Character.getType(c) == Character.END_PUNCTUATION
                || Character.getType(c) == Character.FINAL_QUOTE_PUNCTUATION
                || Character.getType(c) == Character.INITIAL_QUOTE_PUNCTUATION
                || Character.getType(c) == Character.OTHER_PUNCTUATION
                || Character.getType(c) == Character.START_PUNCTUATION
                || Character.getType(c) == Character.OTHER_PUNCTUATION
                || (c >= 33 && c <= 47) // 英文标点符号
                || (c >= 58 && c <= 64)
                || (c >= 91 && c <= 96)
                || (c >= 123 && c <= 126)
                || c == '(' || c == ')'
                || c == '（' || c == '）';
    }

    private boolean previousTwoCharsAreDot() {
        // 实现判断前面两个字符是否为点号的逻辑，根据实际情况来编写
        // 返回 true 表示前面两个字符是点号
        return false;
    }

}
