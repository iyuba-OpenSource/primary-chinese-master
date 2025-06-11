package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person;

import static java.lang.Long.parseLong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant;
import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Bean.LoginBean;
import com.cn.ailanguage.primarychinese.Bean.OneClickBean;
import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.View.LoginContract;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Login.LoginNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.LoginPresenter;
import com.cn.ailanguage.primarychinese.presenter.UidPresenter;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.mob.secverify.SecVerify;
import com.mob.secverify.VerifyCallback;
import com.mob.secverify.common.exception.VerifyException;
import com.mob.secverify.datatype.VerifyResult;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OneClickLoginActivity extends AppCompatActivity implements LoginContract.LoginView, UidContract.UidView {
    private String opToken = "";
    private String Token = "";
    private String Operator = "";
    private UidPresenter uidPresenter;
    LoginContract.LoginPresenter loginPresenter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //一键登录信息
        LoginNetWorkManager.getInstance().init();
        loginPresenter = new LoginPresenter();
        loginPresenter.attchView(this);

        //个人全部信息
        UidNetWorkManager.getInstance().init();
        uidPresenter = new UidPresenter();
        uidPresenter.attchView(this);

        if (Constant.SecVerify.equals("true")) {
            SecVerify.verify(new VerifyCallback() {
                @Override
                public void onOtherLogin() {
                    // 用户点击“其他登录方式”，处理自己的逻辑
                    Intent intent = new Intent(OneClickLoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onUserCanceled() {
                    // 用户点击“关闭按钮”或“物理返回键”取消登录，处理自己的逻辑
                    finish();
                }
                @Override
                public void onComplete(VerifyResult verifyResult) {
                    opToken = verifyResult.getOpToken();
                    // token
                    Token = verifyResult.getToken();
                    // 运营商类型，[CMCC:中国移动，CUCC：中国联通，CTCC：中国电信]
                    Operator = verifyResult.getOperator();
                    loginPresenter.getOneClick("292",
                            "38f08b09fe20a",
                            URLEncoder.encode(opToken),
                            URLEncoder.encode(Token),
                            URLEncoder.encode(Operator));
                }
                @Override
                public void onFailure(VerifyException e) {
                    //TODO处理失败的结果
                    Intent intent = new Intent(OneClickLoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Intent intent = new Intent(OneClickLoginActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void getHome(UidBean uidBean) {
        //获取个人信息
        String days = uidBean.getExpireTime();
        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL=parseLong(days);
        Date date = new Date(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL * 1000);
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTime = sdf.format(date);
        int result = date.compareTo(currentDate);
        if (result > 0) {
            // 会员在有效期内
    // 
            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = formattedTime;
        }else {
            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = "0";
        }
//        Log.d("xwh898", formattedTime+"*"+result);
        com.cn.ailanguage.primarychinese.network.Main.Constant.PACK = String.valueOf(uidBean.getMoney());
        com.cn.ailanguage.primarychinese.network.Main.Constant.INTERAL = uidBean.getCredits();
        com.cn.ailanguage.primarychinese.network.Main.Constant.CORN = String.valueOf(uidBean.getAmount());
        com.cn.ailanguage.primarychinese.network.Main.Constant.USERNAME = uidBean.getUsername();

//微课登录
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();editor.putString("expireTime",String.valueOf(uidBean.getExpireTime()));
        editor.putString("credit", String.valueOf(uidBean.getCredits()));
        editor.putString("email", String.valueOf(uidBean.getCredits()));
        editor.putString("mobile", String.valueOf(uidBean.getCredits()));
        editor.putString("amount", String.valueOf(uidBean.getAmount()));
        editor.commit();

        User user = new User();
        user.vipStatus = String.valueOf(Constant.VIPTIME);
        if (Constant.VIPTIME.equals("0")) {
            user.vipExpireTime = Long.parseLong(sharedPreferences.getString("expireTime", "0"));
        }
        user.uid = Integer.parseInt(sharedPreferences.getString("uid", "nothing"));
        user.credit = Integer.parseInt(sharedPreferences.getString("credit", "0"));
        user.name = sharedPreferences.getString("username", "nothing");
        user.imgUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=" + sharedPreferences.getString("uid", "nothing") + "&size=big";
        user.email = sharedPreferences.getString("email", "nothing");
        user.mobile = sharedPreferences.getString("mobile", "nothing");
        user.iyubiAmount = Integer.parseInt(sharedPreferences.getString("amount", "0"));
        IyuUserManager.getInstance().setCurrentUser(user);



        finish();

    }

    @Override
    public void getOneClick(OneClickBean oneClickBean) {
        String phone=oneClickBean.getRes().getPhone();
        int login=oneClickBean.getIsLogin();
        if(login==0 && phone.equals("")){
            //新用户，到用户名密码注册那边
            Intent intent=new Intent(OneClickLoginActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (login==0 && !phone.equals("")) {
            //快捷注册
            Intent intent=new Intent(OneClickLoginActivity.this,QuickLoginActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
            finish();
        } else if (login==1) {
            //已经注册完成
            SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uid", String.valueOf(oneClickBean.getUserinfo().getUid()));
            editor.putString("image", String.valueOf(oneClickBean.getUserinfo().getImgSrc()));
            editor.putString("username", String.valueOf(oneClickBean.getUserinfo().getUsername()));

            editor.commit();
            com.cn.ailanguage.primarychinese.network.Main.Constant.TURN=1;
            Log.d("xwh890", "12 "+com.cn.ailanguage.primarychinese.network.Main.Constant.TURN);
            String sign1 = MD5.md5("20001" + oneClickBean.getUserinfo().getUid() + "iyubaV2");
            uidPresenter.getHome("android", "json", 20001, Integer.parseInt(oneClickBean.getUserinfo().getUid()), Integer.parseInt(oneClickBean.getUserinfo().getUid()), 292, sign1);

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
    public void getHome(LoginBean loginBean) {

    }
    @Override
    public void onResume() {
        super.onResume();
    }


}
