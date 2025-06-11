package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Top_page;

import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.HomeFragment.isNetSystemUsable;

import static java.lang.Long.parseLong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alipay.sdk.app.PayTask;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant;
import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.LoginActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.BuyActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.BuyiyubiActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Pome.PomeFragment;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Word.WordFragment;
import com.cn.ailanguage.primarychinese.Bean.BuyBean;
import com.cn.ailanguage.primarychinese.Bean.GetBuyBean;
import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.R;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.HomeFragment;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.PersonFragment;

import com.cn.ailanguage.primarychinese.View.BuyContract;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Buy.BuyNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.BuyPresenter;
import com.cn.ailanguage.primarychinese.presenter.UidPresenter;
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
import com.iyuba.imooclib.event.ImoocPayCourseEvent;
import com.iyuba.imooclib.event.ImoocBuyIyubiEvent;
import com.iyuba.imooclib.event.ImoocPlayEvent;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *   主 MainAcitvity
 */
public class MainFragmentActivity extends AppCompatActivity implements BuyContract.BuyView,  UidContract.UidView {

    //主界面
    private ImageView bookhome, person, pome, word,weike;//四个会变色图片
    HomeFragment homeFragment;
    PersonFragment personFragment;
    WordFragment wordFragment;
    PomeFragment pomeFragment;
    private MobClassFragment mobClassFragment;
    private TextView bookhome_text, person_text, pome_text, word_text,weike_text;//四个变色字
    private LinearLayout linearLayout, bookline, personline, wordline, pomeline,weikeline;//四个图片和字的整体框
    //isHomeVisible，isPersonVisible。。。。。判断是不是选中当前的框块
    private boolean isHomeVisible = false;
    private boolean isPersonVisible = false;
    private boolean isWordVisible = false;
    private boolean isPomeVisible = false;
    private boolean isWeikeVisible = false;

    // 不感觉很难受
    private String WIDtatal_fee, WIDsubject, WIDbody, code = null;
    private int amount = 1, product_id = 200;
    private BuyPresenter buyPresenter;
    private UidPresenter uidPresenter;
    private long dayIn = 1;
    private TextView vipState;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        EventBus.getDefault().register(this);

        bookhome = findViewById(R.id.bookhome);
        person = findViewById(R.id.person);
        pome = findViewById(R.id.pome);
        word = findViewById(R.id.word);
        pome_text = findViewById(R.id.pome_text);
        word_text = findViewById(R.id.word_text);
        bookhome_text = findViewById(R.id.bookhome_text);
        person_text = findViewById(R.id.person_text);
        linearLayout = findViewById(R.id.line_main);
        bookline = findViewById(R.id.bookhome_line);
        wordline = findViewById(R.id.word_line);
        personline = findViewById(R.id.person_line);
        pomeline = findViewById(R.id.pome_line);
        weikeline=findViewById(R.id.weike_line);
        weike_text=findViewById(R.id.media_text);
        weike=findViewById(R.id.meida);



//        // 获取个人信息
//        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
//        String uid = sharedPreferences.getString("uid", "nothing");
//        if(!uid.equals("nothing")){
//            String sign = MD5.md5("20001" + uid + "iyubaV2");
//            uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign);
//        }
        //微课登录情况
        clickTab();

        //实现用户uid登录
        BuyNetWorkManager.getInstance().init();
        buyPresenter = new BuyPresenter();
        buyPresenter.attchView(this);

        UidNetWorkManager.getInstance().init();
        uidPresenter = new UidPresenter();
        uidPresenter.attchView(this);
        // 获取个人信息
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "nothing");
        if(!uid.equals("nothing")){
            String sign = MD5.md5("20001" + uid + "iyubaV2");
            uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign);
        }
        boolean isConnected = isNetSystemUsable(this);
        Log.e("qxy", "isCOnn"  + isConnected);
        if (isNetworkAvailable()) {
            portControl();
        }
        else
        {
            Log.e("qxy", "Nonet " );
            bookline.performClick();
        }



    }
    private void clickTab() {
        //点击书本按钮
        bookline.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                // 设置状态栏颜色
                Window window = getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(MainFragmentActivity.this, R.color.homecolor));
                }
                //底下tab变换，有两个角变色
                Drawable drawable = ContextCompat.getDrawable(MainFragmentActivity.this, R.drawable.main_item_bottom);
                linearLayout.setBackground(drawable);

                isHomeVisible = true;
                isPersonVisible = false;
                isPomeVisible = false;
                isWordVisible = false;
                isWeikeVisible=false;
                //字变色//换图片
                weike.setImageResource(R.drawable.media);
                bookhome.setImageResource(R.drawable.bookhome);
                pome.setImageResource(R.drawable.pome_colse);
                word.setImageResource(R.drawable.word_close);
                person.setImageResource(R.drawable.person_colse);
                bookhome_text.setTextColor(Color.parseColor("#FFA500"));
                person_text.setTextColor(R.color.black);
                pome_text.setTextColor(R.color.black);
                word_text.setTextColor(R.color.black);
                weike_text.setTextColor(R.color.black);
                replaceFragment();
            }
        });
        //点击登陆按钮
        personline.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                // 设置 Drawable 背景
                Drawable drawable = ContextCompat.getDrawable(MainFragmentActivity.this, R.drawable.main_item_bottom_2);
                linearLayout.setBackground(drawable);
                Window window = getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(MainFragmentActivity.this, R.color.homecolor));
                }
                isHomeVisible = false;
                isPersonVisible = true;
                isPomeVisible = false;
                isWordVisible = false;
                isWeikeVisible=false;
                weike.setImageResource(R.drawable.media);
                bookhome.setImageResource(R.drawable.bookhome_close);
                pome.setImageResource(R.drawable.pome_colse);
                word.setImageResource(R.drawable.word_close);
                person.setImageResource(R.drawable.person);
                person_text.setTextColor(Color.parseColor("#FFA500"));
                bookhome_text.setTextColor(R.color.black);
                pome_text.setTextColor(R.color.black);
                word_text.setTextColor(R.color.black);
                weike_text.setTextColor(R.color.black);

                replaceFragment();
            }


        });
        //点击生词按钮
        wordline.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override

            public void onClick(View v) {
                Drawable drawable = ContextCompat.getDrawable(MainFragmentActivity.this, R.drawable.main_item_bottom_2);
                linearLayout.setBackground(drawable);
                Window window = getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(MainFragmentActivity.this, R.color.homecolor));
                }
                isHomeVisible = false;
                isPersonVisible = false;
                isPomeVisible = false;
                isWordVisible = true;
                isWeikeVisible=false;
                weike.setImageResource(R.drawable.media);
                bookhome.setImageResource(R.drawable.bookhome_close);
                pome.setImageResource(R.drawable.pome_colse);
                word.setImageResource(R.drawable.word);
                person.setImageResource(R.drawable.person_colse);
                word_text.setTextColor(Color.parseColor("#FFA500"));
                bookhome_text.setTextColor(R.color.black);
                pome_text.setTextColor(R.color.black);
                person_text.setTextColor(R.color.black);
                weike_text.setTextColor(R.color.black);
                replaceFragment();
            }
        });
        //点击古诗按钮
        pomeline.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override

            public void onClick(View v) {

                Window window = getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(MainFragmentActivity.this, R.color.homecolor));
                }
                Drawable drawable = ContextCompat.getDrawable(MainFragmentActivity.this, R.drawable.main_item_bottom);
                linearLayout.setBackground(drawable);

                isHomeVisible = false;
                isPersonVisible = false;
                isPomeVisible = true;
                isWordVisible = false;
                isWeikeVisible=false;
                weike.setImageResource(R.drawable.media);
                bookhome.setImageResource(R.drawable.bookhome_close);
                pome.setImageResource(R.drawable.pome);
                word.setImageResource(R.drawable.word_close);
                person.setImageResource(R.drawable.person_colse);
                pome_text.setTextColor(Color.parseColor("#FFA500"));
                bookhome_text.setTextColor(R.color.black);
                word_text.setTextColor(R.color.black);
                person_text.setTextColor(R.color.black);
                weike_text.setTextColor(R.color.black);
                replaceFragment();
            }
        });
        //微课按钮
        weikeline.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override

            public void onClick(View v) {
                Window window = getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(MainFragmentActivity.this, R.color.app_color));
                }
                Drawable drawable = ContextCompat.getDrawable(MainFragmentActivity.this, R.drawable.main_item_bottom_2);
                linearLayout.setBackground(drawable);

                isHomeVisible = false;
                isPersonVisible = false;
                isPomeVisible =false;
                isWordVisible = false;
                isWeikeVisible= true;
                weike.setImageResource(R.drawable.media_begin);
                bookhome.setImageResource(R.drawable.bookhome_close);
                pome.setImageResource(R.drawable.pome_colse);
                word.setImageResource(R.drawable.word_close);
                person.setImageResource(R.drawable.person_colse);
                weike_text.setTextColor(Color.parseColor("#FFA500"));
                pome_text.setTextColor(R.color.black);
                bookhome_text.setTextColor(R.color.black);
                word_text.setTextColor(R.color.black);
                person_text.setTextColor(R.color.black);
                replaceFragment();

                // 获取个人信息
                SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "nothing");

                if(!uid.equals("nothing")){


                    Log.e("qxy", "uid: " + uid );
                    String sign = MD5.md5("20001" + uid + "iyubaV2");
                    uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign);
                }

            }
        });



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void portControl() {
        String tabs[] = {"weike", "bookhome", "pome", "word"};
        OkHttpClient client = new OkHttpClient();
        CountDownLatch latch = new CountDownLatch(4); // 创建CountDownLatch，初始值为4


        for (int i = 0; i < 4; i++) {
            final int index = i; // 存储当前循环索引

            String url = "http://m.iyuba.cn/m_login/getRegisterAll.jsp?appId=" + tabs[i] + "&appVersion=" +  Constant.version;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    // 处理失败
                    e.printStackTrace();
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseData = response.body().string();

                    JSONObject jsonObject = null;
                    String res = null;
                    try {
                        jsonObject = new JSONObject(responseData);
                        res = jsonObject.getString("result");

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    // 根据当前循环索引获取对应的标签
                    String tab = tabs[index];

                    if (res.equals("1")) {
                        // 根据标签设置可见性
                        switch (tab) {
                            case "weike":
                                weikeline.setVisibility(View.GONE);
                                break;
                            case "bookhome":
                                bookline.setVisibility(View.GONE);
                                break;
                            case "pome":
                                pomeline.setVisibility(View.GONE);
                                break;
                            case "word":
                                wordline.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                    }

                    latch.countDown(); // 请求完成时，调用countDown()方法

                }
            });
        }

        try {
            latch.await(); // 等待所有请求完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        clickTab();

        //默认点击书本按钮


        Log.e("qxy", "portControl: " + bookline.getVisibility() + wordline.getVisibility() + pome.getVisibility() + weikeline.getVisibility() + personline.getVisibility());
        if (bookline.getVisibility() != View.GONE)
        {
            Log.e("qxy", "bookl: " );
            bookline.performClick();
        }
        else if (wordline.getVisibility() != View.GONE) wordline.performClick();
        else if (pomeline.getVisibility() != View.GONE) pomeline.performClick();
        else if (weikeline.getVisibility() != View.GONE) weikeline.performClick();
        else personline.performClick();

    }

    @Override
    public void getHome(UidBean uidBean) {

        Log.e("qxy", "getHome  " + uidBean.toString() );
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);

        User user = new User();
        user.vipStatus = String.valueOf(uidBean.getVipStatus());

        Log.e("qxy", "vipStatus: "+  uidBean.getVipStatus());
        if(!sharedPreferences.getString("uid", "nothing").equals("nothing")){
            user.uid = Integer.parseInt(sharedPreferences.getString("uid", "nothing"));


        }
        user.credit = Integer.parseInt(sharedPreferences.getString("credit", "0"));
        user.name = sharedPreferences.getString("username", "nothing");
        user.imgUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=" + sharedPreferences.getString("uid", "nothing") + "&size=big";
        user.email = sharedPreferences.getString("email", "nothing");
        user.mobile = sharedPreferences.getString("mobile", "nothing");
        user.iyubiAmount = Integer.parseInt(sharedPreferences.getString("amount", "0"));
        IyuUserManager.getInstance().setCurrentUser(user);



        //更新个人数据
        long days = parseLong(uidBean.getExpireTime());
        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL = days;
        dayIn = 1;
        Date date = new Date( days * 1000);
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTime = sdf.format(date);
        int result = date.compareTo(currentDate);
        if (result >= 0) {
            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = formattedTime;
        } else {
            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = "0";
        }
        if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME.equals("0")) {
            vipState.setText("非会员");
        } else {
            vipState.setText(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME);
        }
    }


    //微课购买黄金会员
    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyVIPEvent event) {

        Log.e("qxy", "huangjin: " );
            SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
            String uid = sharedPreferences.getString("uid", "nothing");
            if(!uid.equals("nothing")){
                Log.d("qxy", "buHy ");

                Intent intent = new Intent(this, BuyActivity.class);
                intent.putExtra("source", "weike");
                startActivity(intent);
            }else{
                Toast.makeText(MainFragmentActivity.this, "您还未登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }


    }


    // 微课播放
    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocPlayEvent event) {

        Log.e("qxy", "play onEvent: " + event );

    }

    // 购买 iyubi
    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyIyubiEvent event) {

        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "nothing");

        if(!uid.equals("nothing")){
            Log.d("qxy", "buiy ");
            Intent intent = new Intent(this, BuyiyubiActivity.class);
            intent.putExtra("source", "weike");
            startActivity(intent);



        }else{

            Toast.makeText(MainFragmentActivity.this, "您还未登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }


    //微课直购
    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocPayCourseEvent event) {

        Log.e("qxy", "ImoocPayCourseEvent: ");
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "nothing");
        if (uid.equals("nothing")) {

            Intent intent = new Intent(MainFragmentActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Log.d("qxy", "buZy ");

            amount = event.courseId;
            WIDtatal_fee = event.price + "";
            product_id = 200;
            WIDsubject = " ";
            WIDbody = " ";


//            WIDtatal_fee = 199 + "";
//            amount = 36;
//            WIDsubject = "本应用会员会员";
//            WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;



            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
            simpleDateFormat.applyPattern("yyyy-MM-dd");
            code = MD5.md5(uid + "iyuba" + simpleDateFormat.format(System.currentTimeMillis()));



            Log.e("qxy", "buy1:  uid：" + Integer.parseInt(uid)+" code："+ code+" "+ WIDtatal_fee+ " "+ amount+ " "+ product_id+ " "+ WIDbody+" "+  WIDsubject);
            buyPresenter.getBuy(292, Integer.parseInt(uid), code, WIDtatal_fee, amount, product_id, WIDbody, WIDsubject);
        }
        // TODO Go Pay Order to buy course

//        com.cn.ailanguage.primarychinese.network.Main.Constant.wkId = event.courseId;
//        com.cn.ailanguage.primarychinese.network.Main.Constant.wkPrice = event.price;
//        com.cn.ailanguage.primarychinese.network.Main.Constant.wkBody = event.body;
//        Intent intent = new Intent(this, WkBuyActivity.class);//wkBuy需要重新写一下，闪退。我的原意是向进入那个和buyactivity的一样页面时，强制点击购买并且设置钱数
//        startActivity(intent);
        Log.d("qxy", "onEvent: ");
    }

    public void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        personFragment = (PersonFragment) fragmentManager.findFragmentByTag("person");
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag("home");
        wordFragment = (WordFragment) fragmentManager.findFragmentByTag("word");
        pomeFragment = (PomeFragment) fragmentManager.findFragmentByTag("pome");
        mobClassFragment=(MobClassFragment) fragmentManager.findFragmentByTag("weike");
        //添加各个fragment，一次性全部添加fragment
        if (personFragment == null) {
            personFragment = PersonFragment.newInstance(null, null);
        }
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance(null, null);
        }
        if (pomeFragment == null) {
            pomeFragment = PomeFragment.newInstance(null, null);
        }
        if (wordFragment == null) {
            wordFragment = WordFragment.newInstance(null, null);
        }
        //微课
        if (mobClassFragment == null) {
            ArrayList<Integer> tempList = new ArrayList<>();

            tempList.add(26);


            Bundle bundle = MobClassFragment.buildArguments(26, false, tempList);
            mobClassFragment = MobClassFragment.newInstance(bundle);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!personFragment.isAdded()) {
            transaction.add(R.id.content, personFragment, "person");
        }
        if (!homeFragment.isAdded()) {
            transaction.add(R.id.content, homeFragment, "home");
        }
        if (!pomeFragment.isAdded()) {
            transaction.add(R.id.content, pomeFragment, "pome");
        }
        if (!wordFragment.isAdded()) {
            transaction.add(R.id.content, wordFragment, "word");
        }
        if (!mobClassFragment.isAdded()) {
            transaction.add(R.id.content, mobClassFragment, "weike");
        }
        //根据isPerson这些判断哪个fragment可见
        if (isPersonVisible) {
            // personFragment可见
            transaction.hide(pomeFragment);
            transaction.hide(wordFragment);
            transaction.show(personFragment);
            transaction.hide(homeFragment);
            transaction.hide(mobClassFragment);
        } else if (isWordVisible) {
            transaction.hide(pomeFragment);
            transaction.show(wordFragment);
            transaction.hide(personFragment);
            transaction.hide(homeFragment);
            transaction.hide(mobClassFragment);
        }else if(isPomeVisible){
            transaction.show(pomeFragment);
            transaction.hide(wordFragment);
            transaction.hide(personFragment);
            transaction.hide(homeFragment);
            transaction.hide(mobClassFragment);
        } else if(isWeikeVisible){
            transaction.show(mobClassFragment);
            transaction.hide(wordFragment);
            transaction.hide(personFragment);
            transaction.hide(homeFragment);
            transaction.hide(pomeFragment);
        }else {
            if (isHomeVisible) {
                transaction.hide(pomeFragment);
                transaction.hide(wordFragment);
                transaction.hide(personFragment);
                transaction.show(homeFragment);
                transaction.hide(mobClassFragment);

            } else {
                // Fragment都未被添加到事务中，默认显示textFragment
                transaction.add(R.id.content, homeFragment, "home");
                transaction.show(homeFragment);
                isHomeVisible = true;
            }
        }
        transaction.commit();
    }
    @Override       //这里是实现了自动更新
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // 获取个人信息
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "nothing");
        if (!uid.equals("nothing")) {
            Log.e("qxy", "uidqxy: " + uid);
            String sign = MD5.md5("20001" + uid + "iyubaV2");
            uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign);
        }

    }

    @Override
    public void getBuy(BuyBean buyBean) {
        if (buyBean.getResult().equals("200")) {
            //支付
            Runnable payRunnable = () -> {
                PayTask alipay = new PayTask(MainFragmentActivity.this);
                Log.e("qxy", "getBuy1: "+ buyBean.getAlipayTradeStr() );

                Map<String, String> result = alipay.payV2(buyBean.getAlipayTradeStr(), true);
                if (result.get("resultStatus").equals("9000")) {
                    buyPresenter.getBackBuy(result.toString());

                    // 获取个人信息
                    SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                    String uid = sharedPreferences.getString("uid", "nothing");
                    if(!uid.equals("nothing")){
                        String sign = MD5.md5("20001" + uid + "iyubaV2");
                        uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign);
                    }
                } else {
                    toast(result.get("memo"));
                    hideLoading();
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();

        }
    }

    @Override
    public void getBackBuy(GetBuyBean getBuyBean) {
        //实现用户uid登录
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "nothing");
        String sign = MD5.md5("20001" + uid + "iyubaV2");
        uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign);    }

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
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}