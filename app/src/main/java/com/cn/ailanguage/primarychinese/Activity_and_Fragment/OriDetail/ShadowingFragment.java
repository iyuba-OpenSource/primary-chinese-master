package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;

import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.BookIconActivity.isNetworkConnected;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.cn.ailanguage.primarychinese.Activity_and_Fragment.DealWithSentence;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.Dialog;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.PcmTool;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.PcmToolMp3;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Sentence_read;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.ShadowingAdapter2;
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

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 评测页面
 */
public class ShadowingFragment extends Fragment implements MainContract.MainView, ShadowingContract.ShadowingView, ShadowingAdapter2.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String audioCacheFilePath;
    private Thread recordingAudioThread;
    private AudioRecord mAudioRecord;
    private Boolean isRecord = false;
    ShadowingAdapter2 adapter;
    int recoder = 0;//1代表开始录音，0代表结束录音
    private View rootview;
    private RecyclerView recyclerView;
    private List<Sentence_read> sentenceList;
    private MainPresenter mainPresenter;
    private ShadowingAdapter2 shadowingAdapter2;
    private String message, voaid;
    private Button combine, upload;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private TextView lefttime, righttime, score;
    private Handler mHandler = new Handler();
    //N是原句，l是自己的录音
    private MediaPlayer mediaPlayerN = new MediaPlayer(), mediaPlayerl = new MediaPlayer();

    List<String> ChineseList;
    // 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
    public static final int SAMPLE_RATE_INHZ = 44100;

    // 声道数。CHANNEL_IN_MONO and CHANNEL_IN_STEREO. 其中CHANNEL_IN_MONO是可以保证在所有设备能够使用的。
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;

    // 返回的音频数据的格式。 ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, and ENCODING_PCM_FLOAT.
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    Timer timer = new Timer();
    private int GET_RECODE = 1;
    private Looper Looper;

    public ShadowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShadowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShadowingFragment newInstance(String param1, String param2) {
        ShadowingFragment fragment = new ShadowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainNetWorkManager.getInstance().init();
        mainPresenter = new MainPresenter();
        mainPresenter.attchView(this);
        mainPresenter.getHome("detail", Constant.VOAID);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        rootview = inflater.inflate(R.layout.fragment_shadowing, container, false);
        recyclerView = rootview.findViewById(R.id.shadowing);
        combine = rootview.findViewById(R.id.hecheng);
        upload = rootview.findViewById(R.id.shangchuan);
        seekBar = rootview.findViewById(R.id.seekBar_shadowing);
        lefttime = rootview.findViewById(R.id.lefttime_shadowing);
        righttime = rootview.findViewById(R.id.righttime_shadowing);
        score = rootview.findViewById(R.id.shadowing_score);
        //绑定
        LinearLayoutManager linearLayoutManagerChinese = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManagerChinese);

        Constant.SENTENCE = new ArrayList<>();

        //连接
        message = Constant.MUSIC;
        voaid = Constant.VOAID;

        ShadowingPresenter shadowingPresenter;
        ShadowingNetWorkManager.getInstance().init();
        shadowingPresenter = new ShadowingPresenter();
        shadowingPresenter.attchView(this);
        //seekbar与media绑定
        boolean isConnected = isNetworkConnected(getContext());

//        boolean isConnected = true;
        if (isConnected) {
            //音频准备
            prepareNmediaplay();
//            prepare();
        } else {
            // 设备未联网
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setProgress(0);
                lefttime.setText("00:00");
                seekBar.setMax(mediaPlayer.getDuration()); // 在 MediaPlayer 准备完成后更新 SeekBar 的最大值为音频文件的总时长
            }
        });
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
                seekBar.setProgress(0);
                lefttime.setText("00:00");
                mHandler.removeCallbacksAndMessages(null);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress); // 当用户拖动 SeekBar 时，将 MediaPlayer 的播放进度设置为当前进度
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        //上传
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "nothing");
                String uid = sharedPreferences.getString("uid", "nothing");
                if (uid.equals("nothing") && isConnected) {
                    Dialog dialog = new Dialog(getContext(), "请先登录", "去登录", "取消");
                    dialog.ShowDialog();

                } else if(isConnected) {//联网
                    if (combine.getText().equals("试听")) {
                        String adress = Constant.COMBINEMUSIC.replace("http://ai.aienglish.ltd/voa/", "");
                        int score = 0;//分数
                        for (int i = 0; i < Constant.SCORE.size(); i++) {
                            score = score + Integer.parseInt(Constant.SCORE.get(i));
                        }
                        shadowingPresenter.getHome("android", "json", "60003", "primarychinese", uid, username, Constant.VOAID, "0", "0", String.valueOf(score / Constant.SCORE.size()), "4", adress, "1", "292");
                        Constant.UPLOAD = 1;//更新排行榜
                    } else {
                        Toast.makeText(getContext(), "请先合成", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        //合成
        combine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "nothing");
                if (uid.equals("nothing") && isConnected) {
                    Dialog dialog = new Dialog(getContext(), "请先登录", "去登录", "取消");
                    dialog.ShowDialog();

                } else if(isConnected) {
                    if (combine.getText().equals("合成")) {
                        if (!Constant.SCORE.contains("nothing")) {
                            //全部句子都好了，可以合成
                            String adress = "";
                            for (int i = 0; i < Constant.BACK_ADRESS.size(); i++) {
                                adress = adress + Constant.BACK_ADRESS.get(i) + ",";
                            }
                            adress = adress.replace("http://ai.aienglish.ltd/voa/", "");

                            shadowingPresenter.getMusic(adress, "primarychinese");

                        } else {
                            Toast.makeText(getContext(), "全部句子都测评后才能合成", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //如果已经合成了，就试听
                        if (recoder == 0 && !mediaPlayerN.isPlaying() && !mediaPlayerl.isPlaying()) {//mic结束，原句和自己的录音都没有播放
//                            prepare();
                            //试听
                            // 假设 mediaPlayer 是一个 MediaPlayer 的实例，并且已经设置了音频文件的路径

                            prepare();
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                                updateSeekBar();
                            } else {
                                mediaPlayer.pause();
                            }

                        } else {
                            Toast.makeText(getContext(), "请先暂停录音或者播放哦~", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }

        });

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    Handler handlerm = new Handler();
    Runnable runnablem = new Runnable() {
        @Override
        public void run() {

            if (adapter == null) {
                return;
            }
            if (mediaPlayerN != null && mediaPlayerN.isPlaying()) {

                mediaPlayerN.pause();// 到时间后暂停播放

            }
            adapter.setIsbegin(false);
            adapter.notifyDataSetChanged();
        }
    };

    private void getChange() {
        Constant.BACK_ADRESS = new ArrayList<>(Collections.nCopies(Constant.SENTENCE.size(), "nothing"));
        Constant.SCORE = new ArrayList<>(Collections.nCopies(Constant.SENTENCE.size(), "nothing"));
        adapter = new ShadowingAdapter2(getContext(), Constant.SENTENCE);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setCallBack(new ShadowingAdapter2.CallBack() {
            //播放音频
            @Override
            public void clickBegin(int position) throws IOException {
                //终止合成音频
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                if (adapter.isIslisten()) {
                    clickListen(position);
                }
                if (adapter.isIsmic()) {
                    clickMic(position);
                }
                if (mediaPlayerl != null && mediaPlayerl.isPlaying()) {
                    mediaPlayerl.pause();
                }
                if (mediaPlayerN != null) {
                    if (adapter != null && !adapter.isisbegin()) {
                        //定时器，写入播放暂停时间
                        mediaPlayerN.seekTo((int) Float.parseFloat(Constant.SENTENCE.get(position).getTiming()) * 1000);
                        mediaPlayerN.start();
                        handlerm.postDelayed(runnablem, (long) (Float.parseFloat(Constant.SENTENCE.get(position).getEndTiming()) * 1000 - Float.parseFloat(Constant.SENTENCE.get(position).getTiming()) * 1000));

                        //播放段落
                        adapter.setIsbegin(true);
                        adapter.setChoosePosition(position);
                        adapter.notifyDataSetChanged();


                    } else {
                        if (runnablem != null) {
                            handlerm.removeCallbacks(runnablem);
                        }
                        mediaPlayerN.pause();
                        handlerm.post(runnablem);

                    }
                }
            }
            //麦克风
            @Override
            public void clickMic(int position) throws IOException {
                //终止合成音频
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                if (adapter.isisbegin()) {
                    clickBegin(position);
                }
                if (adapter.isIslisten()) {
                    clickListen(position);
                }
                if (!(PackageManager.PERMISSION_GRANTED == ContextCompat.
                        checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO))) {
                    showDialog();
                } else {
                    if (!adapter.isIsmic()) {
                        adapter.setIsmic(true);
                        adapter.setChoosePosition(position);
                        adapter.notifyDataSetChanged();
                        startRecordAudio();
                    } else {
                        adapter.setIsmic(false);
                        adapter.setChoosePosition(position);
                        adapter.notifyDataSetChanged();


                        stopRecordAudio(position);
                    }
                }


            }
            //播放录音
            @Override
            public void clickListen(int position) throws IOException {

                //终止合成音频
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }

                if (adapter.isisbegin()) {
                    clickBegin(position);
                }
                if (adapter.isIsmic()) {
                    clickMic(position);
                }
                //

                if (!adapter.isIslisten()) {
                    adapter.setislisten(true);
                    adapter.setChoosePosition(position);
                    adapter.notifyDataSetChanged();
                    if (!Constant.BACK_ADRESS.get(position).equals("nothing")) {
                        String audio = Constant.BACK_ADRESS.get(position);
                        mediaPlayerl = MediaPlayer.create(requireActivity(), Uri.parse(audio));
                        mediaPlayerl.start();
                        mediaPlayerl.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                adapter.setislisten(false);
                                adapter.setChoosePosition(position);  //当前点击的位置
                                adapter.notifyDataSetChanged();  //刷新列表数据   每个操作的类里2都要用
                            }
                        });
                    }
                } else {
                    adapter.setislisten(false);
                    adapter.setChoosePosition(position);
                    adapter.notifyDataSetChanged();

                    if (Constant.BACK_ADRESS.get(position) != null) {
                        mediaPlayerl.pause();
                    }
                }
//                // 方法一
//                if (!adapter.isIslisten()) {
//                    adapter.setislisten(true);
//                    adapter.setChoosePosition(position);
//                    adapter.notifyDataSetChanged();
//
//                    if (Constant.BACK_ADRESS.get(position) != null && !Constant.BACK_ADRESS.get(position).equals("nothing")) {
//                        final String audio = Constant.BACK_ADRESS.get(position);
//                        String modifiedUrl = "http://".concat(audio.substring("https://".length()));
//                        MediaPlayer medi = new MediaPlayer();
//
//                        medi.setDataSource(audio); // 设置音频文件路径
////                        medi.prepare();
//                        medi.start();
////                            medi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                @Override
//                                public void onCompletion(MediaPlayer mp) {
//
//                                    // 在音频准备完成后获取持续时间并开始播放
//                                    mp.release();
//                                }
//                            });
//
//                    }
//
//                } else {
//                    adapter.setislisten(false);
//                    adapter.setChoosePosition(position);
//                    adapter.notifyDataSetChanged();
//                }
////


//                if (!adapter.isIslisten()) {
//                    adapter.setislisten(true);
//                    adapter.setChoosePosition(position);
//                    adapter.notifyDataSetChanged();
//                    if (!Constant.BACK_ADRESS.get(position).equals("nothing")) {
//                        String audio = Constant.BACK_ADRESS.get(position);
//                        mediaPlayerl = MediaPlayer.create(requireActivity(), Uri.parse(audio));
//                        mediaPlayerl.prepare();
//                        mediaPlayerl.start();
//                        mediaPlayerl.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mediaPlayer) {
//                                adapter.setislisten(false);
//                                adapter.setChoosePosition(position);  //当前点击的位置
//                                adapter.notifyDataSetChanged();  //刷新列表数据   每个操作的类里2都要用
//                            }
//                        });
//                    }
//                } else {
//                    adapter.setislisten(false);
//                    adapter.setChoosePosition(position);
//                    adapter.notifyDataSetChanged();
//
//                    if (Constant.BACK_ADRESS.get(position) != null) {
//                        mediaPlayerl.pause();
//                    }
//                }
            }
            //item显示
            @Override
            public void clickItem(int position) throws IOException {
                //终止合成音频
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                if (adapter.isIsmic()) {
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
                    adapter.setIsmic(false);
                }
                if (adapter.isIslisten()) {
                    clickListen(position);
                }
                if (adapter.isisbegin()) {

                    clickBegin(position);
                    adapter.setIsbegin(false);

                }
                handlerm.removeCallbacks(runnablem);//每点击一个新的句子要清空计时器!!!!

                adapter.setHide(true);
                adapter.setShow(false);
                adapter.setChoosePosition(position);
                adapter.notifyDataSetChanged();
            }

        });

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("系统信息");
        builder.setMessage("录音上传用户录音，语言测评打分，是否同意开启权限?");
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮后的逻辑处理
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

    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 0); // 每秒更新一次进度条
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = 0;
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
            long totalDuration = mediaPlayer.getDuration();
            String totalDurationstr = StringUtils.leftPad(String.valueOf(totalDuration / 1000 / 60), 2, "0") + ":" + StringUtils.leftPad(String.valueOf(totalDuration / 1000 % 60), 2, "0");
            if (totalDuration >= 0) {
                righttime.setText(String.valueOf(totalDurationstr));
            } else {
                righttime.setText("00:00");
            }
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            mHandler.postDelayed(this, 200);
        }
    };

    protected void prepare() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        Log.d("fang0012", "prepare: "+Constant.COMBINEMUSIC);
        try {
            mediaPlayer.setDataSource(Constant.COMBINEMUSIC);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 开始录音，返回临时缓存文件（.pcm）的文件路径
     */
    protected String startRecordAudio() {
        //记录一下是哪个item开始录音的
        Constant.ITEERECODER = Constant.ITEMING;
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
//            Log.w(TAG,e.getLocalizedMessage());
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
                .addFormDataPart("sentence", Constant.SENTENCE.get(positon).getSentence())
                .addFormDataPart("paraId", Constant.SENTENCE.get(positon).getParaId())
                .addFormDataPart("newsId", Constant.SENTENCE.get(positon).getVoaid())
                .addFormDataPart("IdIndex", Constant.SENTENCE.get(positon).getIdIndex())
                .addFormDataPart("type", "primarychinese")
                .addFormDataPart("appId", "292")
                .addFormDataPart("wordId", "0")
                .addFormDataPart("flg", "1")
                .addFormDataPart("userId", uid + "")
                .addFormDataPart("file", "/mp3_" + System.currentTimeMillis() + ".mp3", fileBody).build();
        shadowingPresenter.getEvaluating(requestBody);
    }

    private void prepareNmediaplay() {
        try {
            mediaPlayerN.setDataSource(Constant.MUSIC);
            mediaPlayerN.prepareAsync();
        } catch (Exception e) {

        }
    }


    @Override
    public void getHome(MainBean mainBean) {
        List<MainBean.InfoBean> daily = mainBean.getInfo();
        ChineseList = new ArrayList<>();
        for (int i = 0; i < daily.size(); i++) {
            ChineseList.add(daily.get(i).getSentence());
        }
        Constant.SENTENCE = daily;
        getChange();
        Constant.WORDCOLOER = new ArrayList<>();

        for (int i = 0; i < daily.size(); i++) {
            List<Float> sublist = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                sublist.add(3.0F);
            }
            Constant.WORDCOLOER.add(sublist);
        }


    }

    @Override
    public void getHome(ShadowingBean shadowingBean) {
        Constant.BORE = 1;
        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getEvaluating(EvaBean evaBean) {
        adapter.setShow(true);
        adapter.setChoosePosition(adapter.getChoosePosition());
        for (int i = 0; i < evaBean.getData().getWords().size(); i++) {
            Constant.WORDCOLOER.get(adapter.getChoosePosition()).set(i, Float.parseFloat(evaBean.getData().getWords().get(i).getScore()));
        }
        Constant.BACK_ADRESS.set(adapter.getChoosePosition(), "http://ai.aienglish.ltd/voa/" + String.valueOf(evaBean.getData().getUrl()));

        Constant.SCORE.set(adapter.getChoosePosition(), String.valueOf(evaBean.getData().getScores()));
        adapter.notifyDataSetChanged();

    }

    @Override
    public void getMusic(MusicBean musicBean) {
        Toast.makeText(getContext(), "合成成功", Toast.LENGTH_SHORT).show();
        combine.setText("试听");
        Constant.COMBINEMUSIC = "http://ai.aienglish.ltd/voa/" + musicBean.getURL();

        Log.d("fang0012", "getMusic: "+Constant.COMBINEMUSIC);
        int sum = 0;
        for (int i = 0; i < Constant.SCORE.size(); i++) {
            sum = sum + Integer.parseInt(Constant.SCORE.get(i));
        }
        score.setText("分数:" + String.valueOf(sum / Constant.SCORE.size()));
    }

    @Override
    public void getLeader(LeaderBean leaderBean) {

    }

    @Override
    public void getLeaderUser(LeaderUserBean leaderuserBean) {

    }

    @Override
    public void onItemClick(View view, int position) {

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //当界面被隐藏的时候
        if (hidden) {
            handlerm.removeCallbacks(runnablem);
            handlerm.post(runnablem);


            if (mediaPlayerN != null) {
                if (mediaPlayerN.isPlaying()) {
                    mediaPlayerN.pause();

                }
            }
            if (mediaPlayerl != null) {
                if (mediaPlayerl.isPlaying()) {
                    mediaPlayerl.pause();
                    adapter.setislisten(false);
                    adapter.notifyDataSetChanged();

                }
            }
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                }
            }
            if (adapter != null) {
                if (adapter.isIsmic()) {
                    adapter.setIsmic(false);
                    adapter.notifyDataSetChanged();
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
            }


        }

    }

    public void shadowingreals() {
        handlerm.removeCallbacks(runnablem);
        handlerm.post(runnablem);
        if (mediaPlayerN != null) {
            if (mediaPlayerN.isPlaying()) {
                mediaPlayerN.pause();

            }
        }
        if (mediaPlayerl != null) {
            if (mediaPlayerl.isPlaying()) {
                mediaPlayerl.pause();
                adapter.setislisten(false);
                adapter.notifyDataSetChanged();

            }
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}