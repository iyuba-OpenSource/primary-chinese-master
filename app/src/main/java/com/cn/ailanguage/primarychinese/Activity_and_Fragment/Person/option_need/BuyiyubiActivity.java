package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need;

import static java.lang.Long.parseLong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.alipay.sdk.app.PayTask;
import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.LoginActivity;
import com.cn.ailanguage.primarychinese.Bean.BuyBean;
import com.cn.ailanguage.primarychinese.Bean.GetBuyBean;
import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.BuyContract;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Buy.BuyNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.BuyPresenter;
import com.cn.ailanguage.primarychinese.presenter.UidPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class BuyiyubiActivity extends AppCompatActivity implements BuyContract.BuyView, UidContract.UidView {
    private ImageView back;
    private TextView vipB, vipQ, name, vipState,vipH;//主按钮

    private Button buy_small;
    private Button buy_more_small;
    private Button buy_normal;
    private Button buy_more;
    private Button buy_most;

    private String code = null;

    private String WIDtatal_fee = null;

    private int amount = 0;

    private long dayIn = 0;


    private int product_id = -1;

    private String WIDbody = null;

    private String WIDsubject = null;


    private BuyPresenter buyPresenter;
    private UidPresenter uidPresenter;




    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyiyubi);



        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }


        //接口
        BuyNetWorkManager.getInstance().init();
        buyPresenter = new BuyPresenter();
        buyPresenter.attchView(this);
        UidNetWorkManager.getInstance().init();
        uidPresenter = new UidPresenter();
        uidPresenter.attchView(this);


        //点击返回按钮
        back = findViewById(R.id.back_icon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SharedPreferences sharedPreference = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreference.getString("uid", "nothing");


        buy_small = findViewById(R.id.buy_small);
        buy_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = sharedPreference.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Intent intent = new Intent(BuyiyubiActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {

                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                    simpleDateFormat.applyPattern("yyyy-MM-dd");

                    code = MD5.md5(uid + "iyuba" + simpleDateFormat.format(System.currentTimeMillis()));
                    WIDtatal_fee = 19.9 + "";
                    amount = 210;
                    product_id = 1;
                    WIDsubject = "爱语币";
                    WIDbody = "花费" + WIDtatal_fee + "元购买" + amount + WIDsubject;

                    Log.e("qxy", "buy: " + Integer.parseInt(uid) + " " + code + " " + WIDtatal_fee + " " + amount + " " + product_id + " " + WIDbody + " " + WIDsubject);
                    buyPresenter.getBuy(292, Integer.parseInt(uid), code, WIDtatal_fee, amount, product_id, WIDbody, WIDsubject);
                }

            }
        });


        buy_more_small = findViewById(R.id.buy_more_small);
        buy_more_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String uid = sharedPreference.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Intent intent = new Intent(BuyiyubiActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {

                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                    simpleDateFormat.applyPattern("yyyy-MM-dd");
                    code = MD5.md5(uid + "iyuba" + simpleDateFormat.format(System.currentTimeMillis()));
                    WIDtatal_fee = 59.9 + "";
                    amount = 650;
                    product_id = 1;
                    WIDsubject = "爱语币";
                    WIDbody = "花费" + WIDtatal_fee + "元购买" + amount + WIDsubject;

                    buyPresenter.getBuy(292, Integer.parseInt(uid), code, WIDtatal_fee, amount, product_id, WIDbody, WIDsubject);
                }
            }
        });



        buy_normal = findViewById(R.id.buy_normal);
        buy_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = sharedPreference.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Intent intent = new Intent(BuyiyubiActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {

                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                    simpleDateFormat.applyPattern("yyyy-MM-dd");
                    code = MD5.md5(uid + "iyuba" + simpleDateFormat.format(System.currentTimeMillis()));
                    WIDtatal_fee = 99.9 + "";
                    amount = 1100;
                    product_id = 1;
                    WIDsubject = "爱语币";
                    WIDbody = "花费" + WIDtatal_fee + "元购买" + amount + WIDsubject;

                    buyPresenter.getBuy(292, Integer.parseInt(uid), code, WIDtatal_fee, amount, product_id, WIDbody, WIDsubject);
                }
            }
        });

        buy_more = findViewById(R.id.buy_more);
        buy_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = sharedPreference.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Intent intent = new Intent(BuyiyubiActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {

                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                    simpleDateFormat.applyPattern("yyyy-MM-dd");
                    code = MD5.md5(uid + "iyuba" + simpleDateFormat.format(System.currentTimeMillis()));
                    WIDtatal_fee = 599 + "";
                    amount = 6600;
                    product_id = 1;
                    WIDsubject = "爱语币";
                    WIDbody = "花费" + WIDtatal_fee + "元购买" + amount + WIDsubject;

                    buyPresenter.getBuy(292, Integer.parseInt(uid), code, WIDtatal_fee, amount, product_id, WIDbody, WIDsubject);
                }
            }
        });

        buy_most = findViewById(R.id.buy_most);
        buy_most .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = sharedPreference.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Intent intent = new Intent(BuyiyubiActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {

                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                    simpleDateFormat.applyPattern("yyyy-MM-dd");
                    code = MD5.md5(uid + "iyuba" + simpleDateFormat.format(System.currentTimeMillis()));
                    WIDtatal_fee = 999 + "";
                    amount = 12000;
                    product_id = 1;
                    WIDsubject = "爱语币";
                    WIDbody = "花费" + WIDtatal_fee + "元购买" + amount + WIDsubject;

//                vipBuypresenter.getVip(appid, Integer.parseInt(Constant.uid), code, WIDtatal_fee, amount, product_id, WIDbody, WIDsubject);
                    buyPresenter.getBuy(292, Integer.parseInt(uid), code, WIDtatal_fee, amount, product_id, WIDbody, WIDsubject);
                }
            }
        });






    }

    @Override
    public void getHome(UidBean uidBean) {
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
//        Log.d("xwh777", formattedTime);
    }

    @Override
    public void getBuy(BuyBean buyBean) {
        if (buyBean.getResult().equals("200")) {
            //支付
            Runnable payRunnable = () -> {
                PayTask alipay = new PayTask(BuyiyubiActivity.this);
                Log.e("qxy", "getBuy: "+ buyBean.getAlipayTradeStr() );
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

        uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign);
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
}