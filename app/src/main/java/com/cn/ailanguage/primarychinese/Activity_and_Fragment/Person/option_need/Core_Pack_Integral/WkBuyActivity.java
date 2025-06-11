package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.Core_Pack_Integral;

import static java.lang.Long.parseLong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.LoginActivity;
import com.cn.ailanguage.primarychinese.Bean.BuyBean;
import com.cn.ailanguage.primarychinese.Bean.GetBuyBean;
import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.View.BuyContract;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Buy.BuyNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.BuyPresenter;
import com.cn.ailanguage.primarychinese.presenter.UidPresenter;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.cn.ailanguage.primarychinese.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class WkBuyActivity extends AppCompatActivity implements BuyContract.BuyView, UidContract.UidView {

    private TextView vipB, vipQ, name, vipState,vipH;//主按钮
    private LinearLayout optionB, optionQ,optionH;//选项
    private LinearLayout q1, q6, q12, q36, b1, b6, b12, b36,h1,h3,h6,h12;//选项按钮
    private LinearLayout moreBen, moreQuan,moreHuan;
    private String WIDtatal_fee, WIDsubject, WIDbody, code = null;
    private Button payfor_now;
    private long dayIn = 0;
    private int firstB = 1, firstQ = 1;//用于判断是不是第一次点击本站和全站
    private BuyPresenter buyPresenter;
    private ImageView avatar, back;
    private int amount, product_id = -1;
    private UidPresenter uidPresenter;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        vipB = findViewById(R.id.vipBen);
        vipQ = findViewById(R.id.vipQuan);
        vipH=findViewById(R.id.vipHuan);
        optionB = findViewById(R.id.benvipmonth1);
        optionQ = findViewById(R.id.quanvipmonth1);
        optionH=findViewById(R.id.huanvipmonth1);
        moreBen = findViewById(R.id.benvipshuoing);
        moreQuan = findViewById(R.id.quanvipshuoing);
        moreHuan=findViewById(R.id.huanvipshuoing);

        //接口
        BuyNetWorkManager.getInstance().init();
        buyPresenter = new BuyPresenter();
        buyPresenter.attchView(this);
        UidNetWorkManager.getInstance().init();
        uidPresenter = new UidPresenter();
        uidPresenter.attchView(this);

        SharedPreferences sharedPreference = getSharedPreferences("ceshi", Context.MODE_PRIVATE);


        name = findViewById(R.id.vipusername);
        vipState = findViewById(R.id.vipstate);
        avatar = findViewById(R.id.avatar);
        startlayout();
        //点击返回按钮
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击全站会员
        vipQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int customColor = getResources().getColor(R.color.pay);
                vipQ.setBackgroundColor(customColor);
                optionQ.setVisibility(View.VISIBLE);
                optionB.setVisibility(View.INVISIBLE);
                optionH.setVisibility(View.INVISIBLE);
                vipB.setBackgroundColor(Color.WHITE);
                vipH.setBackgroundColor(Color.WHITE);
                moreBen.setVisibility(View.INVISIBLE);
                moreHuan.setVisibility(View.INVISIBLE);
                moreQuan.setVisibility(View.VISIBLE);

                q1.performClick();

            }
        });
        //点击本站会员
        vipB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int customColor = getResources().getColor(R.color.pay);
                vipB.setBackgroundColor(customColor);
                optionB.setVisibility(View.VISIBLE);
                optionQ.setVisibility(View.INVISIBLE);
                optionH.setVisibility(View.INVISIBLE);
                vipQ.setBackgroundColor(Color.WHITE);
                vipH.setBackgroundColor(Color.WHITE);
                moreBen.setVisibility(View.VISIBLE);
                moreHuan.setVisibility(View.INVISIBLE);
                moreQuan.setVisibility(View.INVISIBLE);
                b1.performClick();
            }
        });
        //点击黄金会员
        vipH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int customColor = getResources().getColor(R.color.pay);
                vipH.setBackgroundColor(customColor);
                optionH.setVisibility(View.VISIBLE);
                optionB.setVisibility(View.INVISIBLE);
                optionQ.setVisibility(View.INVISIBLE);
                vipQ.setBackgroundColor(Color.WHITE);
                vipB.setBackgroundColor(Color.WHITE);
                moreBen.setVisibility(View.INVISIBLE);
                moreHuan.setVisibility(View.VISIBLE);
                moreQuan.setVisibility(View.INVISIBLE);
                h1.performClick();
            }
        });
        //b1,b6,b12,b36选项
        b1 = findViewById(R.id.b1);
        b6 = findViewById(R.id.b6);
        b12 = findViewById(R.id.b12);
        b36 = findViewById(R.id.b36);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                b6.setBackgroundColor(Color.WHITE);
                b12.setBackgroundColor(Color.WHITE);
                b36.setBackgroundColor(Color.WHITE);
//                WIDtatal_fee = 0.01 + "";
                WIDtatal_fee = 30 + "";
                amount = 1;
                product_id = 10;
                WIDsubject = "本应用会员会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b6.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                b1.setBackgroundColor(Color.WHITE);
                b12.setBackgroundColor(Color.WHITE);
                b36.setBackgroundColor(Color.WHITE);
                WIDtatal_fee = 69 + "";
                amount = 6;
                product_id = 10;
                WIDsubject = "本应用会员会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b12.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                b6.setBackgroundColor(Color.WHITE);
                b1.setBackgroundColor(Color.WHITE);
                b36.setBackgroundColor(Color.WHITE);
                WIDtatal_fee = 99 + "";
                amount = 12;
                product_id = 10;
                WIDsubject = "本应用会员会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        b36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b36.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                b6.setBackgroundColor(Color.WHITE);
                b12.setBackgroundColor(Color.WHITE);
                b1.setBackgroundColor(Color.WHITE);

                WIDtatal_fee = 199 + "";
                amount = 36;
                product_id = 10;
                WIDsubject = "本应用会员会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        q1 = findViewById(R.id.q1);
        q6 = findViewById(R.id.q6);
        q12 = findViewById(R.id.q12);
        q36 = findViewById(R.id.q36);
        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q1.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                q6.setBackgroundColor(Color.WHITE);
                q12.setBackgroundColor(Color.WHITE);
                q36.setBackgroundColor(Color.WHITE);
                WIDtatal_fee = 0.01 + "";
                amount = 1;
                product_id = 0;
                WIDsubject = "全站会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        q6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q6.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                q1.setBackgroundColor(Color.WHITE);
                q12.setBackgroundColor(Color.WHITE);
                q36.setBackgroundColor(Color.WHITE);
                WIDtatal_fee = 198 + "";
                amount = 6;
                product_id = 0;
                WIDsubject = "全站会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        q12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q12.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                q1.setBackgroundColor(Color.WHITE);
                q6.setBackgroundColor(Color.WHITE);
                q36.setBackgroundColor(Color.WHITE);

                WIDtatal_fee = 298 + "";
                amount = 12;
                product_id = 0;
                WIDsubject = "全站会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;

            }
        });
        q36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q36.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                q1.setBackgroundColor(Color.WHITE);
                q6.setBackgroundColor(Color.WHITE);
                q12.setBackgroundColor(Color.WHITE);
                WIDtatal_fee = 588 + "";
                amount = 36;
                product_id = 0;
                WIDsubject = "全站会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        //黄金会员点击事件
        h1 = findViewById(R.id.h1);
        h6 = findViewById(R.id.h6);
        h12 = findViewById(R.id.h12);
        h3 = findViewById(R.id.h3);
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                h6.setBackgroundColor(Color.WHITE);
                h12.setBackgroundColor(Color.WHITE);
                h3.setBackgroundColor(Color.WHITE);
                WIDtatal_fee = 98 + "";
//                WIDtatal_fee = 0.01 + "";
                amount = 1;
                product_id = 0;
                WIDsubject = "黄金会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        h6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h6.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                h1.setBackgroundColor(Color.WHITE);
                h12.setBackgroundColor(Color.WHITE);
                h3.setBackgroundColor(Color.WHITE);
                WIDtatal_fee = 518 + "";
                amount = 6;
                product_id = 0;
                WIDsubject = "黄金会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        h12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h12.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                h1.setBackgroundColor(Color.WHITE);
                h6.setBackgroundColor(Color.WHITE);
                h3.setBackgroundColor(Color.WHITE);

                WIDtatal_fee = 998 + "";
                amount = 12;
                product_id = 0;
                WIDsubject = "黄金会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;

            }
        });
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h3.setBackgroundColor(Color.parseColor("#B4FDDB95"));
                h1.setBackgroundColor(Color.WHITE);
                h6.setBackgroundColor(Color.WHITE);
                h12.setBackgroundColor(Color.WHITE);
                WIDtatal_fee = 288 + "";
                amount = 3;
                product_id = 0;
                WIDsubject = "黄金会员";
                WIDbody = "花费" + WIDtatal_fee + "元购买" + WIDsubject;
            }
        });
        //支付按钮
        payfor_now = findViewById(R.id.payfor_now);
        payfor_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = sharedPreference.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Intent intent = new Intent(WkBuyActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("xwh89", uid);
                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                    simpleDateFormat.applyPattern("yyyy-MM-dd");
                    code = MD5.md5(uid + "iyuba" + simpleDateFormat.format(System.currentTimeMillis()));
                    buyPresenter.getBuy(292, Integer.parseInt(uid), code, WIDtatal_fee, amount, product_id, WIDbody, WIDsubject);
                }
            }
        });
    }


    //打开界面的布局
    private void startlayout() {
        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }
        onResume();
        //第一次进入页面，设置连接参数初始化，默认是本应用会员1个月
        WIDtatal_fee = Constant.wkPrice;
        amount = Constant.wkId;
        product_id = 200;//这个可能要改一下
        WIDsubject = "微课";
        try {
            WIDbody =  URLEncoder.encode(Constant.wkBody,  "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        payfor_now.performClick();
        firstQ = 0;

        SharedPreferences sharedPreference4 = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreference4.getString("uid", "nothing");
        String imagePic = sharedPreference4.getString("image", "nothing");
        if (!uid.equals("nothing")) {
            name.setText(Constant.USERNAME);
            Glide.with(WkBuyActivity.this)
                    .load(imagePic)  // 替换为实际的本地图片资源ID
                    .circleCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar);
            if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME.equals("0")) {
                vipState.setText("非会员");
            } else {
                vipState.setText(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME);
            }
        }
    }

    @Override
    public void getBuy(BuyBean buyBean) {
        if (buyBean.getResult().equals("200")) {
            //支付
            Runnable payRunnable = () -> {
                PayTask alipay = new PayTask(this);
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
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

    }
}