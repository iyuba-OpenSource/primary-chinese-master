package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person;

import static java.lang.Long.parseLong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant;
import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Bean.OneClickBean;
import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Top_page.BaseActivity;
import com.cn.ailanguage.primarychinese.Bean.LoginBean;
import com.cn.ailanguage.primarychinese.View.LoginContract;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Login.LoginNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.LoginPresenter;
import com.cn.ailanguage.primarychinese.presenter.UidPresenter;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.PrivacyActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.TermActivity;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoginActivity extends BaseActivity implements LoginContract.LoginView, UidContract.UidView {
    private Button login;
    private EditText username, password;
    private CheckBox checkBox;
    private TextView postuser;
    private LoginPresenter loginPresenter;
    private UidPresenter uidPresenter;

    private String userstring = null, passwordstring = null, sign = null, sign1 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        postuser = findViewById(R.id.postuser);
        //登录连接
        LoginNetWorkManager.getInstance().init();
        loginPresenter = new LoginPresenter();
        loginPresenter.attchView(this);
        //个人信息
        UidNetWorkManager.getInstance().init();
        uidPresenter = new UidPresenter();
        uidPresenter.attchView(this);
        //立即注册
        postuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivtiy.class);
                startActivity(intent);
                finish();
            }
        });
        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }
        //隐私协议和使用条款设置可点击
        checkBox = findViewById(R.id.checkbox);
        // 隐私协议点击
        SpannableString spannableString = new SpannableString(checkBox.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, PrivacyActivity.class);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
// 将SpannableString对象设置到TextView中
        checkBox.setText(spannableString);
// 设置TextView可点击，以便触发ClickableSpan的点击事件
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
        //使用条款点击
        // 隐私协议点击
        SpannableString spannableString1 = new SpannableString(checkBox.getText());
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, TermActivity.class);
                startActivity(intent);
            }
        };
        spannableString1.setSpan(clickableSpan1, 12, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
// 将SpannableString对象设置到TextView中
        checkBox.setText(spannableString1);
// 设置TextView可点击，以便触发ClickableSpan的点击事件
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());


        //登陆事件编写
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    try {
                        userstring = URLEncoder.encode(username.getText().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    passwordstring = MD5.md5(password.getText().toString());
                    sign = MD5.md5("11001" + username.getText().toString() + passwordstring + "iyubaV2");
                    Constant.PASSWORD = passwordstring;
                    loginPresenter.getHome(userstring, passwordstring, "primarychinese", "", "json", "292", "11001", sign);



//
                } else {
                    Toast.makeText(LoginActivity.this, "请同意隐私协议和使用条款", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //忘记密码
        TextView forget = findViewById(R.id.forget);
        forget.setMovementMethod(LinkMovementMethod.getInstance());
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理点击事件，例如打开浏览器跳转到指定URL
                Uri uri = Uri.parse("http://m.iyuba.cn/m_login/inputPhonefp.jsp");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    //登录完成后调用一下20001接口
    @Override
    public void getHome(UidBean uidBean) {
        // 假设你有一个整数变量表示天数
        String days = uidBean.getExpireTime();

        Log.e("lhz", "day: " + days );
        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.vipStatus = String.valueOf(uidBean.getVipStatus());


        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL =parseLong(days);
        Date date = new Date(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL * 1000);
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTime = sdf.format(date);
        int result = date.compareTo(currentDate);
        if (result > 0) {
            // 会员在有效期内

            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = formattedTime;
        } else {
            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = "0";
        }
        com.cn.ailanguage.primarychinese.network.Main.Constant.PACK = String.valueOf(uidBean.getMoney());
        com.cn.ailanguage.primarychinese.network.Main.Constant.INTERAL = uidBean.getCredits();
        com.cn.ailanguage.primarychinese.network.Main.Constant.CORN = String.valueOf(uidBean.getAmount());
        com.cn.ailanguage.primarychinese.network.Main.Constant.USERNAME = uidBean.getUsername();
        Log.d("xwh888", com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME);
        com.cn.ailanguage.primarychinese.network.Main.Constant.TURN = 1;

        //微课登录
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
// 最近我也是刚写完初稿 我们暂时没要交初稿 中期检查应该会要求交
        User user = new User();
//        user.vipStatus = String.valueOf(Constant.VIPTIME);
        user.vipStatus = uidBean.getVipStatus();

        Log.e("qxy", "vipStatus " +  uidBean.getVipStatus());
        if (Constant.VIPTIME.equals("0")) {
            user.vipExpireTime = Long.parseLong(sharedPreferences.getString("expireTime", "nothing"));
        }
        user.uid = Integer.parseInt(sharedPreferences.getString("uid", "nothing"));
        user.credit = Integer.parseInt(sharedPreferences.getString("credit", "nothing"));
        user.name = sharedPreferences.getString("username", "nothing");
        user.imgUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=" + sharedPreferences.getString("uid", "nothing") + "&size=big";
        user.email = sharedPreferences.getString("email", "nothing");
        user.mobile = sharedPreferences.getString("mobile", "nothing");
        user.iyubiAmount = Integer.parseInt(sharedPreferences.getString("amount", "nothing"));
        IyuUserManager.getInstance().setCurrentUser(user);
        finish();

    }

    @Override
    public void getHome(LoginBean loginBean) {
        if (loginBean.getResult().equals("101")) {
            //获取uid
            SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uid", String.valueOf(loginBean.getUid()));
            editor.putString("password", String.valueOf(passwordstring));
            editor.putString("image", String.valueOf(loginBean.getImgSrc()));
            editor.putString("username", String.valueOf(loginBean.getUsername()));
            editor.putString("expireTime",String.valueOf(loginBean.getExpireTime()));
            editor.putString("credit", String.valueOf(loginBean.getCredits()));
            editor.putString("email", String.valueOf(loginBean.getCredits()));
            editor.putString("mobile", String.valueOf(loginBean.getCredits()));
            editor.putString("amount", String.valueOf(loginBean.getAmount()));
            editor.commit();
            String sign1 = MD5.md5("20001" + String.valueOf(loginBean.getUid()) + "iyubaV2");
            uidPresenter.getHome("android", "json", 20001, Integer.parseInt(String.valueOf(loginBean.getUid())), Integer.parseInt(String.valueOf(loginBean.getUid())), 292, sign1);
            com.cn.ailanguage.primarychinese.network.Main.Constant.USERNAME = String.valueOf(loginBean.getUsername());
        } else {
            Toast.makeText(this, "请输入正确的用户名和密码", Toast.LENGTH_SHORT).show();
        }

    }

    //
    @Override
    public void getOneClick(OneClickBean oneClickBean) {

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
