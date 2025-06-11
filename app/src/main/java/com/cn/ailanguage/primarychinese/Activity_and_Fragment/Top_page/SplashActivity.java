package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Top_page;

import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.channel;
import static com.youdao.sdk.nativeads.YouDaoInterstitialActivity.getActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.PrivacyActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.TermActivity;


import com.cn.ailanguage.primarychinese.Bean.AdEntryBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.SQLBase.CreateBase;
import com.cn.ailanguage.primarychinese.View.InitContract;
import com.cn.ailanguage.primarychinese.databinding.SplashActivityBinding;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.InitPresenter;
import com.iyuba.dlex.bizs.DLManager;

import com.iyuba.imooclib.IMooc;
import com.iyuba.module.dl.BasicDLDBManager;
import com.iyuba.module.favor.BasicFavor;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.favor.data.local.BasicFavorInfoHelper;
import com.iyuba.module.movies.data.local.db.DBManager;
import com.iyuba.module.privacy.PrivacyInfoHelper;
import com.mob.MobSDK;
import com.mob.secverify.SecVerify;
import com.umeng.commonsdk.UMConfigure;
import com.yd.saas.base.interfaces.AdViewSpreadListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.manager.YdConfig;
import com.youdao.sdk.common.OAIDHelper;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.common.YoudaoSDK;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.YouDaoNative;
import com.yd.saas.ydsdk.YdSpread;

import cn.smssdk.gui.util.Const;

public class SplashActivity extends AppCompatActivity implements YouDaoNative.YouDaoNativeNetworkListener, InitContract.InitView, AdViewSpreadListener {
    //这是启动5s页
    private static final String PREFS_NAME = "MyAppPreferences";
    private static final String AGREED_KEY = "AgreedPrivacyPolicy";
    private boolean isFirstLaunch;
    private TextView jump;
    private int flag = 0;
    private CountDownTimer countDownTimer;
    Handler handler = new Handler();
    private SplashActivityBinding binding;

    private InitPresenter initPresenter;

    private String adkey = "0585";


    private boolean isAdCLick = false;

    private AdEntryBean.DataDTO dataDTO;
    private AdEntryBean adEntryBean;

    LinearLayout ad_view;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CreateBase helper;
        super.onCreate(savedInstanceState);
        binding = SplashActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ad_view = findViewById(R.id.csj_initad);
//        cover_line = binding.initLl.findViewById(R.id.init_cover);


        initView();

        // ad
//        NetWorkManager.getInstance().init();
        NetWorkManager.getInstance().initDev();
        initPresenter=new InitPresenter();
        initPresenter.attchView(this);


//        Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
//        startActivity(intent);
//        finish();
//        jump = findViewById(R.id.jump);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean agreed = prefs.getBoolean(AGREED_KEY, false);

        //顶端变色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }

        String channel = Constant.channel;
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //记录是不是第一次登录
        //        弹窗
        if (!agreed)
        {//第一次弹出
            String message = "    请你务必审慎阅读、充分理解“隐私协议”和“使用条款”各条款，包括但不限于:为了更好的向你提供服务，我们需要收集你的设备标识、操作日志等信息用于分析、优化应用性能。\n    你可阅读《隐私协议》和《使用条款》了解详细信息。如果你同意，请点击下面按钮开始接受我们的服务。\n";

            SpannableString spannableString = new SpannableString(message);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SplashActivity.this, PrivacyActivity.class);
                    startActivity(intent);
                }
            };
            spannableString.setSpan(clickableSpan, message.indexOf("隐私协议"), message.indexOf("隐私协议") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // 将 SpannableString 设置为消息内容
            TextView textView = new TextView(this);
            textView.setText(spannableString);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            builder.setView(textView);

            //使用条款
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    // 在此处添加跳转逻辑，例如跳转到其他界面
                    Intent intent = new Intent(SplashActivity.this, TermActivity.class);
                    startActivity(intent);
                }
            };
            spannableString.setSpan(clickableSpan1, message.indexOf("使用条款"), message.indexOf("使用条款") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // 将 SpannableString 设置为消息内容
            TextView textView1 = new TextView(this);
            textView1.setText(spannableString);
            textView1.setMovementMethod(LinkMovementMethod.getInstance());
            builder.setView(textView1);

            // 设置肯定按钮，即确认按钮
            builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    prefs.edit().putBoolean(AGREED_KEY, true).apply();
                    //统计用户  友盟初始化
                    isAdCLick = true;


                    UMConfigure.init(SplashActivity.this, UMConfigure.DEVICE_TYPE_PHONE, channel);
                    MobSDK.submitPolicyGrantResult(true); // 验证码
                    dialog.dismiss();  // 关闭对话框
                    gotoMainActivity(true);//跳转进入5s计时器
                    onResume();

                }
            });

            // 设置否定按钮，即取消按钮
            builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();//退出app
                    System.exit(0);
                    isFirstLaunch = true;
                    dialog.dismiss();  // 关闭对话框
                }
            });
            //设置标题什么其他的，位置调整
            AlertDialog dialog = builder.create();
            TextView title = new TextView(this);
            title.setText("隐私协议和使用条款");
            title.setPadding(10, 30, 10, 40);
            title.setGravity(Gravity.CENTER);
            title.setTextSize(18);
            title.setTextColor(Color.BLACK);
            dialog.setCustomTitle(title);
            dialog.setCancelable(false);
            dialog.show();

        } else {//不是第一次弹，直接进入5s
//            countDownTimer.cancel();
            UMConfigure.init(SplashActivity.this, UMConfigure.DEVICE_TYPE_PHONE, channel);
            gotoMainActivity(false);


        }
    }


    //关闭操作
    private void gotoMainActivity(boolean isFirstLaunch) {
        //建议提前调用预登录接口，可以加快免密登录过程，提高用户体验
        SecVerify.preVerify(new com.mob.secverify.OperationCallback() {

            @Override
            public void onComplete(Object o) {
                Constant.SecVerify = "true";
            }

            @Override
            public void onFailure(com.mob.secverify.common.exception.VerifyException e) {
                Constant.SecVerify = "false";
            }
        });

        BasicFavorInfoHelper.init(this);
        IMooc.init(this
                , String.valueOf(201), "voa");
        IMooc.setEnableShare(false);
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "nothing");
        Constant.uid = uid;


        if (isFirstLaunch)
        {
            accept();
        }
        else
        {
            NetWorkManager.getInstance().initDev();
            initPresenter = new InitPresenter();
            initPresenter.attchView(this);


            if(!Constant.uid.equals("nothing")){
                initPresenter.getAdEntryAll(Constant.APPID + "", 1, Constant.uid);
                Log.d("lhz", "gotoMainActivity: " + uid);
            }else {
                initPresenter.getAdEntryAll(Constant.APPID+"", 1, "0");
                Log.d("lhz", "gotoMainActivity: " + "0");

            }

        }
    }

    private void initView()
    {

        System.loadLibrary("msaoaidsec");
        OAIDHelper.getInstance().init(this);

        //禁止有道获取id等信息
        YouDaoAd.getYouDaoOptions().setCanObtainAndroidId(false);
        YouDaoAd.getNativeDownloadOptions().setConfirmDialogEnabled(true);
        YouDaoAd.getYouDaoOptions().setAppListEnabled(false);
        YouDaoAd.getYouDaoOptions().setPositionEnabled(false);
        YouDaoAd.getYouDaoOptions().setSdkDownloadApkEnabled(true);
        YouDaoAd.getYouDaoOptions().setDeviceParamsEnabled(false);
        YouDaoAd.getYouDaoOptions().setWifiEnabled(false);

        //有道
        YoudaoSDK.init(this);
        YdConfig.getInstance().init(this, "292");
        //集成的广告流
        PrivacyInfoHelper.init(this);
        PrivacyInfoHelper.getInstance().putApproved(true);
//        EventBus.getDefault().register(this);
        //有道广告
        //有道处理下载类广告问题    信息流




        NetWorkManager.getInstance().init();
        NetWorkManager.getInstance().initDev();

//        IHeadline.setDebug(false);
//        IHeadline.init(getApplicationContext(), 291 + "", getString(R.string.app_name));
//        IHeadline.setEnableShare(true);
//        IHeadline.setEnableGoStore(false);
//        IHeadline.setEnableSmallVideoTalk(true);//小视频配音

        //db
        BasicDLDBManager.init(this);
        BasicFavor.init(getApplicationContext(), 292 + "");
        BasicFavorDBManager.init(this);
        DLManager.init(this, 5);
        DBManager.init(this);
//        HLDBManager.init(this);

        //瑞狮广告
//        VlionSdkConfig config = new VlionSdkConfig
//                .Builder()
//                .setAppId("A0078")//媒体在平台申请的 APP_ID
//                .setAppKey("73f1d7142763542122324aefec3f9c4c")//媒体在平台申请的 APP_KEY
//                .setEnableLog(true)//测试阶段打开，可以通过日志排查问题，上线时去除该调用
//                .setPrivateController(createPrivateController)//隐私信息控制设置，此项必须设置！！
//                .build();
        //个性化推荐广告
//        VlionSDk.setPersonalizedAdState(true);
        //初始化广告SDK
//        VlionSDk.init(getApplication(), config);

    }

    // 跳转到主界面
//
    // 如果用户已经同意隐私协议
// 创建一个 5 秒的倒计时计时器
    private void accept() {

        Log.e("lhz", "accept: " );


        countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 更新倒计时文本
//                jump.setText("跳过(" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                // 倒计时结束后跳转到指定的 Activity
                Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
        // 点击右上角的倒计时文本可以立即跳转
//        jump.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                countDownTimer.cancel();
//                UMConfigure.init(SplashActivity.this, UMConfigure.DEVICE_TYPE_PHONE, channel);
//                gotoMainActivity(false);
////                Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
////                startActivity(intent);
////                finish();
//            }
//        });
    }

    @Override
    public void onNativeLoad(NativeResponse nativeResponse) {

    }

    @Override
    public void onNativeFail(NativeErrorCode nativeErrorCode) {

    }
    @Override
    protected void onResume() {
        super.onResume();

//        if (isAdCLick) {//点击了就直接跳转mainactivity
//
//            Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
//            startActivity(intent);
//            finish();
//        }


    }
    @Override
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {


        dataDTO = adEntryBean.getData();
        String type = dataDTO.getType();
        if (adEntryBean.getResult().equals("1")){
            if (type.equals("web")) {
                Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                finish();
                //                Glide.with(SplashActivity.this).load("http://static3.iyuba.cn/dev" + dataDTO.getStartuppic()).into(binding.initIv);
            } else if (type.equals(Constant.AD_ADS1) || type.equals(Constant.AD_ADS2) || type.equals(Constant.AD_ADS3)
                    || type.equals(Constant.AD_ADS4) || type.equals(Constant.AD_ADS5) || type.equals(Constant.AD_ADS6)) {

                Log.d("lhz", "进来请求:"  + type);
                YdSpread mSplashAd = new YdSpread.Builder(SplashActivity.this)
                        .setKey(adkey)
                        .setContainer(ad_view)
                        .setSpreadListener(this)
                        .setCountdownSeconds(4)
                        .setSkipViewVisibility(true)
                        .build();
                mSplashAd.requestSpread();


            } else {
                Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                finish();
            }

        }
        else {
            Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
            startActivity(intent);
            finish();
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
    public void onAdDisplay() {

    }

    @Override
    public void onAdClose() {

        Log.e("lhz", "onAdClose: ");
        if (!isAdCLick) {

            Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
            startActivity(intent);
            finish();
            isAdCLick=true;
        }
    }

    @Override
    public void onAdClick(String s) {
        isAdCLick=true;

    }

    @Override
    public void onAdFailed(YdError ydError) {
        Log.e("lhz", "onAdFailed: " + ydError);
//        Toast.makeText(this, ydError.getMsg(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
        startActivity(intent);
        finish();

    }
}
