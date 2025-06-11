package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;

import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.BookIconActivity.isNetworkConnected;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.BeisongAdapter;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.Dialog;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.PcmTool;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.PcmToolMp3;
import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderUserBean;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Bean.MusicBean;
import com.cn.ailanguage.primarychinese.Bean.ShadowingBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.View.ShadowingContract;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Shadowing.ShadowingNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.MainPresenter;
import com.cn.ailanguage.primarychinese.presenter.ShadowingPresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BeisongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeisongFragment extends Fragment implements View.OnClickListener, MainContract.MainView, ShadowingContract.ShadowingView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Handler handler = new Handler();
    String audioCacheFilePath;
    private int GET_RECODE = 1;
    MediaPlayer mediaPlayer;

    private Thread recordingAudioThread;
    private AudioRecord mAudioRecord;
    private Boolean isRecord = false;
    private View rootView;
    private String textFormat;
    private TextView shouzi, geju, wuxu, moreTime, score, right_num, wrong_num, all_num, tishi;
    private ImageView begin_mic, light_beisong;
    List<String> Chinese = new ArrayList<>();
    private LinearLayout no_score, have_score;
    BeisongAdapter beisongAdapter;
    private RecyclerView recycleWords;
    private ImageView begin_music;
    private Runnable clearTextRunnable;
    private int rightNum, wrongNum, allNum;
    int recoder = 0;
    public static final int SAMPLE_RATE_INHZ = 44100;
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private int beisong = 1; // geju 1, shouzi 2, wuxu 3;
    // 在类的成员变量中添加一个标志位
    private boolean isClickEnabled = true;
    private boolean canChange = true;


    public BeisongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment BeisongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BeisongFragment newInstance(String param1, String param2) {
        BeisongFragment fragment = new BeisongFragment();
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
    }

    TextView pome_title, pome_author;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.beisong_layout, container, false);
        recycleWords = rootView.findViewById(R.id.beisong_recycle);
        pome_author = rootView.findViewById(R.id.pome_auther);
        pome_title = rootView.findViewById(R.id.pome_title);
        wuxu = rootView.findViewById(R.id.wuxu);
        shouzi = rootView.findViewById(R.id.shouzi);
        geju = rootView.findViewById(R.id.geju);
        moreTime = rootView.findViewById(R.id.beisong_moreTime);
        begin_mic = rootView.findViewById(R.id.begin_mic);
        no_score = rootView.findViewById(R.id.no_score);
        have_score = rootView.findViewById(R.id.have_score);
        score = rootView.findViewById(R.id.score_beisong);
        all_num = rootView.findViewById(R.id.all_beisong);
        right_num = rootView.findViewById(R.id.right_beisong);
        wrong_num = rootView.findViewById(R.id.wrong_beishu);
        begin_music = rootView.findViewById(R.id.begin_music);
        light_beisong = rootView.findViewById(R.id.light_beisong);
        tishi = rootView.findViewById(R.id.tishi);
        Constant.MIcCLICK = -1;//begin_mic点击的次数
        Constant.SCOREBEISONG = 0;//成绩
        Constant.BEISONG_FINISH = 0;//判断有没有全部背诵完成
        Constant.BACK_ADRESS_BEISONG = new ArrayList<>(Collections.nCopies(Constant.SENTENCE.size(), "nothing"));
        //绑定
        LinearLayoutManager linearLayoutManagerChinese = new LinearLayoutManager(getContext());
        recycleWords.setLayoutManager(linearLayoutManagerChinese);

        MainPresenter mainPresenter;
        MainNetWorkManager.getInstance().init();
        mainPresenter = new MainPresenter();
        mainPresenter.attchView(this);

        boolean isConnected = isNetworkConnected(getContext());
        if (isConnected) {
            //如果联网
            no_score.setVisibility(View.VISIBLE);
            mainPresenter.getHome("detail", Constant.VOAID);
        } else {
            no_score.setVisibility(View.INVISIBLE);
            have_score.setVisibility(View.INVISIBLE);
        }

        // 点击开始录音
        begin_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Dialog dialog = new Dialog(getContext(), "请先登录", "去登录", "取消");
                    dialog.ShowDialog();
                }
                else if (!(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)))
                {
                    showDialog();
                }
                else
                {
                    canChange = false;

                    if (!isClickEnabled) return; // 如果不允许点击，则直接返回

                    isClickEnabled = false; // 点击后立即禁用点击


                    // 原有的点击处理逻辑...
                    if (Constant.MIcCLICK == -1) {
                        begin_mic.setImageResource(R.mipmap.beisong_stop);
                        if (beisongAdapter != null) {
                            beisongAdapter.notifyDataSetChanged();
                            startRecordAudio(0);
                        }
                    } else {
                        stopRecordAudio(Constant.MIcCLICK);
                        if (Constant.MIcCLICK < Chinese.size()) {
                            startRecordAudio(Constant.MIcCLICK);
                        }
                    }

                    Constant.MIcCLICK = Constant.MIcCLICK + 1;

                    // 使用postDelayed在3秒后重新启用点击
                    begin_mic.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isClickEnabled = true;
                        }
                    }, 1000); // 2000毫秒后执行
                }

            }
        });
        //三种背诵形式
        wuxu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canChange)
                {
                    beisong = 3;
                    if (Chinese.size() != 0) {
                        Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.pack_background);
                        wuxu.setBackground(background);
                        geju.setBackgroundColor(Color.parseColor("#F18E1B"));
                        shouzi.setBackgroundColor(Color.parseColor("#F18E1B"));

                        beisongAdapter = new BeisongAdapter(getContext(), wuxuChange(Chinese));
                        recycleWords.setAdapter(beisongAdapter);
                    }
                }
            }
        });
        shouzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canChange) {
                    beisong = 2;
                    Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.pack_background);
                    shouzi.setBackground(background);
                    geju.setBackgroundColor(Color.parseColor("#F18E1B"));
                    wuxu.setBackgroundColor(Color.parseColor("#F18E1B"));
                    if (Chinese.size() != 0) {
                        beisongAdapter = new BeisongAdapter(getContext(), shouziChange(Chinese));
//                    beisongAdapter = new BeisongAdapter(getContext(), shouziChange(Chinese));
                        recycleWords.setAdapter(beisongAdapter);
                    }
                }

            }
        });
        geju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canChange)
                {
                    beisong = 1;
                    if (Chinese.size() != 0) {
                        Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.pack_background);
                        geju.setBackground(background);
                        shouzi.setBackgroundColor(Color.parseColor("#F18E1B"));
                        wuxu.setBackgroundColor(Color.parseColor("#F18E1B"));
                        beisongAdapter = new BeisongAdapter(getContext(), gejuChange(Chinese));
//                    beisongAdapter = new BeisongAdapter(getContext(), Chinese);
                        recycleWords.setAdapter(beisongAdapter);
                    }
                }
            }
        });
        //提示灯泡
        clearTextRunnable = new Runnable() {
            @Override
            public void run() {
                tishi.setText(""); // 清空文字
            }
        };
        light_beisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.MIcCLICK != -1) {
                    tishi.setText(Chinese.get(Constant.MIcCLICK));
                    if (handler != null) {
                        handler.removeCallbacks(clearTextRunnable);
                    }
                    // 使用 Handler 延迟2秒后让文字消失
                    handler = new Handler();
                    handler.postDelayed(clearTextRunnable, 2000); // 2000毫秒即2秒
                }
            }
        });
        //重新录音
        moreTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canChange = true;
                no_score.setVisibility(View.VISIBLE);
                have_score.setVisibility(View.GONE);
                begin_mic.setImageResource(R.mipmap.beisong_begin);
                Constant.SCOREBEISONG = 0;//成绩
                Constant.BEISONG_FINISH = 0;//判断有没有全部背诵完成
                Constant.MIcCLICK = -1;//begin_mic点击的次数
                rightNum = 0;
                wrongNum = 0;
                allNum = 0;//对的个数错的个数
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                mediaPlayer = null;
                geju.performClick();
            }
        });
        //点击播放完成后的录音
        begin_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    begin_music.setImageResource(R.mipmap.beisong_finish_stop);
                    String adress = "";
                    for (int i = 0; i < Constant.BACK_ADRESS_BEISONG.size(); i++) {
                        adress = adress + Constant.BACK_ADRESS_BEISONG.get(i) + ",";
                    }
                    adress = adress.replace("https://ai.aienglish.ltd/voa/", "");
                    shadowingPresenter.getMusic(adress, "primarychinese");
                }

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    begin_music.setImageResource(R.mipmap.beisong_finish_begin);
                } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    begin_music.setImageResource(R.mipmap.beisong_finish_stop);
                }
            }
        });
        return rootView;
    }

    public List<String> gejuChanging(List<String> args) {
        List<String> outputList = new ArrayList<>();
        int numList = 0;//判断到了list的第几句
        for (String item : args) {
            numList ++ ;
            if (countPunctuation(item) != 1) {
                //如果一句不止一个标点符号
                int num = 1;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < item.length(); i++) {
                    if (isPunctuation(item.charAt(i))) {
                        num++;
                    }
                    if (num % 2 == 0 && numList > Constant.MIcCLICK) {
                        //偶数，不显示
                        if (isChineseCharacter(item.charAt(i))) {
                            sb.append("\u3000");
                        } else if (isPunctuation(item.charAt(i))) {
                            sb.append(item.charAt(i));
                        }
                    } else {
                        sb.append(item.charAt(i));
                    }
                }
                outputList.add(sb.toString());
            } else if (countPunctuation(item) == 1) {
                //一句话只有一个标点
//                Log.e("qxy", "gejuChanging: " +  );
                StringBuilder sb = new StringBuilder();

                Log.e("lhz", "gejuChanging: " + numList + " " + Constant.MIcCLICK);
                if (numList % 2 == 1 && numList > Constant.MIcCLICK) {
                    //偶数句，隐藏
                    for (int i = 0; i < item.length(); i++) {
                        if (isChineseCharacter(item.charAt(i))) {
                            sb.append("\u3000");
//                            sb.append("__");
                        } else {
                            sb.append(item.charAt(i));
                        }
                    }
                } else {
                    for (int i = 0; i < item.length(); i++) {
                        sb.append(item.charAt(i));
                    }
                }
                outputList.add(sb.toString());
            }

        }

        return outputList;

    }

    public List<String> gejuChange(List<String> args) {
        List<String> outputList = new ArrayList<>();
        int numList = 0;//判断到了list的第几句
        for (String item : args) {
            if (countPunctuation(item) != 1) {
                //如果一句不止一个标点符号
                int num = 1;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < item.length(); i++) {
                    if (isPunctuation(item.charAt(i))) {
                        num++;
                    }
                    if (num % 2 == 0) {
                        //偶数，不显示
                        if (isChineseCharacter(item.charAt(i))) {
                            sb.append("\u3000");
                        } else if (isPunctuation(item.charAt(i))) {
                            sb.append(item.charAt(i));
                        }
                    } else {
                        sb.append(item.charAt(i));
                    }
                }
                outputList.add(sb.toString());
            } else if (countPunctuation(item) == 1) {
                //一句话只有一个标点
                numList++;
                StringBuilder sb = new StringBuilder();
                if (numList % 2 == 1) {
                    //偶数句，隐藏
                    for (int i = 0; i < item.length(); i++) {
                        if (isChineseCharacter(item.charAt(i))) {
                            sb.append("\u3000");
//                            sb.append("__");
                        } else {
                            sb.append(item.charAt(i));
                        }
                    }
                } else {
                    for (int i = 0; i < item.length(); i++) {
                        sb.append(item.charAt(i));
                    }
                }
                outputList.add(sb.toString());
            }

        }

        return outputList;
    }


    public List<String> shouziChanging(List<String> args) {
        int numList = 0;
        List<String> outputList = new ArrayList<>();
        for (String item : args) {
            StringBuilder sb = new StringBuilder();
            numList ++ ;
            if (numList > Constant.MIcCLICK)
            {
                sb.append(item.charAt(0));
                for (int i = 1; i < item.length(); i++) {
                    if (isChineseCharacter(item.charAt(i))) {
                        if (isPunctuation(item.charAt(i - 1))) {
                            sb.append(item.charAt(i));
                        } else {
                            sb.append("\u3000");
                        }
                    } else if (isPunctuation(item.charAt(i))) {
                        sb.append(item.charAt(i));
                    }
                }
            }
            else
            {
                for (int i = 0; i < item.length(); i++) {
                    sb.append(item.charAt(i));
                }
            }

            outputList.add(sb.toString());
        }

        return outputList;
    }
    public List<String> shouziChange(List<String> args) {
        List<String> outputList = new ArrayList<>();
        for (String item : args) {
            StringBuilder sb = new StringBuilder();
            sb.append(item.charAt(0));
            for (int i = 1; i < item.length(); i++) {
                if (isChineseCharacter(item.charAt(i))) {
                    if (isPunctuation(item.charAt(i - 1))) {
                        sb.append(item.charAt(i));
                    } else {
                        sb.append("\u3000");
                    }
                } else if (isPunctuation(item.charAt(i))) {
                    sb.append(item.charAt(i));
                }
            }
            outputList.add(sb.toString());
        }

        return outputList;
    }


    public List<String> wuxuChange(List<String> inputList) {
        List<String> outputList = new ArrayList<>();
        for (String item : inputList) {
            StringBuilder sb = new StringBuilder();
            for (char c : item.toCharArray()) {
                if (c >= '\u4e00' && c <= '\u9fff') {  // 判断是否是中文字符
                    sb.append("\u3000");
                } else {
                    sb.append(c);
                }
            }
            outputList.add(sb.toString());
        }

        return outputList;
    }

    public List<String> wuxuChanging(List<String> inputList) {
        int numList = 0;

        List<String> outputList = new ArrayList<>();
        for (String item : inputList) {

            StringBuilder sb = new StringBuilder();
            numList ++ ;
            Log.e("lhz", "wuxuChanging: " + numList + " " + Constant.MIcCLICK);
            if (numList > Constant.MIcCLICK)
            {
                for (char c : item.toCharArray()) {
                    if (c >= '\u4e00' && c <= '\u9fff') {  // 判断是否是中文字符
                        sb.append("\u3000");
                    } else {
                        sb.append(c);
                    }
                }
            }
            else
            {
                for (int i = 0; i < item.length(); i++) {
                    sb.append(item.charAt(i));
                }
            }
            outputList.add(sb.toString());
        }

        return outputList;
    }

    private static int countPunctuation(String sentence) {
        int count = 0;
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (isPunctuation(c)) {
                count++;
            }
        }
        return count;
    }

    public static boolean isChineseCharacter(char c) {
        String str = Character.toString(c);
        return str.matches("[\u4e00-\u9fa5]");
    }

    public static boolean isPunctuation(char c) {
        return Pattern.matches("\\p{Punct}", String.valueOf(c));
    }


    @Override
    public void getHome(MainBean mainBean) {
        //对数据进行处理

        List<MainBean.InfoBean> list = mainBean.getInfo();
        pome_title.setText(Constant.TITLE);
        pome_author.setText(Constant.POMEAUTHOR);
        //是古诗
        for (int i = 0; i < list.size(); i++) {
            Chinese.add(list.get(i).getSentence());

        }
        beisongAdapter = new BeisongAdapter(getContext(), gejuChange(Chinese));

        recycleWords.setAdapter(beisongAdapter);
        Constant.SENTENCE_BEISONG = list;
        Constant.WORDCOLOER_BEISONG = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<Float> sublist = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                sublist.add(3.0F);
            }
            Constant.WORDCOLOER_BEISONG.add(sublist);
        }
    }

    /**
     * 开始录音，返回临时缓存文件（.pcm）的文件路径
     */
    protected String startRecordAudio(int postion) {
        //记录一下有没有开始录音，为试听的时候点击麦克风做准备
        recoder = 1;
        audioCacheFilePath = getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + "jerboa_audio_cache.pcm";
        try {
            // 获取最小录音缓存大小，
            int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);

            // 开始录音
            isRecord = true;
            mAudioRecord.startRecording();

            // 创建数据流，将缓存导入数据流
            this.recordingAudioThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    File file = new File(audioCacheFilePath);
                    /*
                     *  以防万一，看一下这个文件是不是存在，如果存在的话，先删除掉
                     */
                    if (file.exists()) {
                        file.delete();
                    }

                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (fos == null) {
                        return;
                    }

                    byte[] data = new byte[minBufferSize];
                    int read1;

                    if (fos != null) {
                        while (isRecord && !recordingAudioThread.isInterrupted()) {
                            read1 = mAudioRecord.read(data, 0, minBufferSize);
                            if (AudioRecord.ERROR_INVALID_OPERATION != read1) {
                                try {
                                    fos.write(data);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    try {
                        // 关闭数据流
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.recordingAudioThread.start();
        } catch (IllegalStateException e) {
        } catch (SecurityException e) {
        }
        return audioCacheFilePath;
    }

    /**
     * 停止录音
     */
//    停止录制，在停止录制的同时把音频上传
    private ShadowingPresenter shadowingPresenter;

    public void stopRecordAudio(int positon) {

        Log.e("lhz", "stopRecordAudio: " + positon);
        recoder = 0;
        try {
            isRecord = false;
            if (mAudioRecord != null) {
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
                recordingAudioThread.interrupt();
                recordingAudioThread = null;
            }
        } catch (Exception e) {
        }


        //将pcm变成wav文件
        File wavFilePath = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PODCASTS) + "/wav_" + System.currentTimeMillis() + ".wav");
        PcmTool ptwUtil = new PcmTool();

        if (wavFilePath.exists()) {
            wavFilePath.delete();
        }

        try {
            wavFilePath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ptwUtil.pcmToWav(String.valueOf(audioCacheFilePath), String.valueOf(wavFilePath), true);
        //将wav变成mp3
        File mp3FilePath = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PODCASTS) + "/mp3_" + System.currentTimeMillis() + ".mp3");
        PcmToolMp3 ptwUtilmp3 = new PcmToolMp3();
        try {
            ptwUtilmp3.convertWavToMp3(String.valueOf(wavFilePath), String.valueOf(mp3FilePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "nothing");

        ShadowingNetWorkManager.getInstance().init();
        shadowingPresenter = new ShadowingPresenter();
        shadowingPresenter.attchView((ShadowingContract.ShadowingView) this);

        //post 请求
        MediaType type = MediaType.parse("application/octet-stream");
        RequestBody fileBody = RequestBody.create(type, mp3FilePath);
        try {
            Log.d("xwh890", String.valueOf(fileBody.contentLength()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("sentence", Constant.SENTENCE_BEISONG.get(positon).getSentence())
                .addFormDataPart("paraId", Constant.SENTENCE_BEISONG.get(positon).getParaId())
                .addFormDataPart("newsId", Constant.SENTENCE_BEISONG.get(positon).getVoaid())
                .addFormDataPart("IdIndex", Constant.SENTENCE_BEISONG.get(positon).getIdIndex())
                .addFormDataPart("type", "primarychinese")
                .addFormDataPart("appId", "292")
                .addFormDataPart("wordId", "0")
                .addFormDataPart("flg", "1")
                .addFormDataPart("userId", uid + "")
                .addFormDataPart("file", "/mp3_" + System.currentTimeMillis() + ".mp3", fileBody).build();
        shadowingPresenter.getEvaluating(requestBody);


    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("系统信息");
        builder.setMessage("录音上传用户录音，语言测评打分，是否同意开启权限?");
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮后的逻辑处理，申请麦克风
                if (!(PackageManager.PERMISSION_GRANTED == ContextCompat.
                        checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO))) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, GET_RECODE);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击取消按钮后的逻辑处理
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void getEvaluating(EvaBean evaBean) {
        allNum = allNum + evaBean.getData().getWords().size();
        List<Float> list = new ArrayList<>();
        for (int i = 0; i < evaBean.getData().getWords().size(); i++) {
            list.add(Float.valueOf(evaBean.getData().getWords().get(i).getScore()));
            if (Float.valueOf(evaBean.getData().getWords().get(i).getScore()) < 2.0) {
                wrongNum++;
            } else {
                rightNum++;
            }
        }

        Log.e("lhz", "getEvaluating: " + list );
        Constant.WORDCOLOER_BEISONG.set(Constant.MIcCLICK - 1, list);
        Constant.BACK_ADRESS_BEISONG.set(Constant.MIcCLICK - 1, "https://ai.aienglish.ltd/voa/" + String.valueOf(evaBean.getData().getUrl()));
        Constant.SCOREBEISONG = Constant.SCOREBEISONG + evaBean.getData().getScores();

        if (beisong == 1)
        {
            beisongAdapter = new BeisongAdapter(getContext(), gejuChanging(Chinese));
        }
        else if (beisong == 2)
        {
            beisongAdapter = new BeisongAdapter(getContext(), shouziChanging((Chinese)));
        }
        else
        {
            beisongAdapter = new BeisongAdapter(getContext(), wuxuChanging(Chinese));
        }
//                    beisongAdapter.notifyDataSetChanged();
        recycleWords.setAdapter(beisongAdapter);


        if (Constant.MIcCLICK == Chinese.size()) {//如果所有句的背诵已经完成
            if (Chinese.size() != 0) {
                Constant.BEISONG_FINISH = 1;
            }
            if (recoder == 1) {
                //防止mic还开着
                recoder = 0;
                try {
                    isRecord = false;
                    if (mAudioRecord != null) {
                        mAudioRecord.stop();
                        mAudioRecord.release();
                        mAudioRecord = null;
                        recordingAudioThread.interrupt();
                        recordingAudioThread = null;
                    }
                } catch (Exception e) {
                }
            }
//            改变布局，以及布局一些数据
            have_score.setVisibility(View.VISIBLE);
            score.setText(String.valueOf(Constant.SCOREBEISONG / Chinese.size()) + "分");
            no_score.setVisibility(View.GONE);
            all_num.setText("本内容共计" + String.valueOf(allNum) + "个字");
            wrong_num.setText(String.valueOf(wrongNum));
            right_num.setText(String.valueOf(rightNum));
//            beisongAdapter = new BeisongAdapter(getContext(), gejuChange(Chinese));
            beisongAdapter = new BeisongAdapter(getContext(), Chinese);
            recycleWords.setAdapter(beisongAdapter);
//            beisongAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getHome(ShadowingBean shadowingBean) {
    }

    @Override
    public void getMusic(MusicBean musicBean) {
        Constant.COMBINEMUSIC = "http://ai.aienglish.ltd/voa/" + musicBean.getURL();
        prepare();
    }

    protected void prepare() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(Constant.COMBINEMUSIC);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                begin_music.setImageResource(R.mipmap.beisong_finish_begin);
            }
        });
    }

    @Override
    public void getLeader(LeaderBean leaderBean) {

    }

    @Override
    public void getLeaderUser(LeaderUserBean leaderuserBean) {

    }

    @Override
    public void onClick(View v) {

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

    //当hide这个界面时，音频停止
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                begin_music.setImageResource(R.mipmap.beisong_finish_begin);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // 当 Fragment 被暂停时停止 MediaPlayer 播放
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}