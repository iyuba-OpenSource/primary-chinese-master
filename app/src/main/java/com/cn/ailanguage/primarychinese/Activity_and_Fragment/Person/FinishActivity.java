package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person;

import static java.lang.Long.parseLong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.UidPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FinishActivity extends AppCompatActivity implements UidContract.UidView{
    private EditText password_finish, username_finish;
    private Button login_finish;
    private UidPresenter uidPresenter;
    private String username, password, phone;

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        login_finish = findViewById(R.id.login_finish);
        password_finish = findViewById(R.id.password_finish);
        username_finish = findViewById(R.id.username_finish);
        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }
        //获取用户信息
        UidNetWorkManager.getInstance().init();
        uidPresenter = new UidPresenter();
        uidPresenter.attchView(this);
        //获取intent传递过来的电话phone
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        //登录
        login_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = username_finish.getText().toString();
                password = password_finish.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    if (isValidPassword(password)) {
                        if (isValidUsername(username)) {
                            try {
                                //判断是不是注册成功
                                if (isResult(username, password) == 1) {
                                } else {
                                    toast("注册失败");
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            toast("请输入正确的用户名");
                        }
                    } else {
                        toast("请输入正确的密码");
                    }
                } else {
                    toast("用户名或者密码不能为空");
                }
            }
        });
    }

    @Override
    public void getHome(UidBean uidBean) {
        String days = uidBean.getExpireTime();
        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL=parseLong(days);
        Date date = new Date(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL * 1000);
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTime = sdf.format(date);
        int result = date.compareTo(currentDate);
        if (result > 0) {
            // 会员在有效期内

            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = formattedTime;
        }else {
            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = "0";
        }
//        Log.d("xwh898", formattedTime+"*"+result);
        Constant.PACK = String.valueOf(uidBean.getMoney());
        Constant.INTERAL = uidBean.getCredits();
        Constant.CORN = String.valueOf(uidBean.getAmount());
        Constant.USERNAME = uidBean.getUsername();
        finish();
    }

    public int isResult(String username, String password) throws InterruptedException {
        final int[] result = {0};
        String sign = MD5.md5("11002" + username + MD5.md5(password) + "iyubaV2");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                //http发送网络请求
                StringBuilder sb = new StringBuilder("");
                sb.append("http://api.iyuba.com.cn/v2/api.iyuba?protocol=11002&username=")
                        .append(username)
                        .append("&password=").append(MD5.md5(password))
                        .append("&mobile=").append(phone)
                        .append("&sign=").append(sign)
                        .append("&format=json");
                http:
//api.iyuba.com.cn/v2/api.iyuba?protocol=11002&username=徐文慧的账户&password=0da77e57795ab3ec470841af34ff9db6&mobile=19558764744&sign=c638b58457ddabbb179f29dd8b5aa750&format=json
                try {
                    URL url = new URL(sb.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//设置请求方式
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //取得输入流，并使用Reader读取
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    //得到结果
                    StringBuilder response = new StringBuilder("");
                    String tempLine;
                    while ((tempLine = reader.readLine()) != null) {
                        response.append(tempLine);
                    }
                    // 结果转json
                    JSONObject jo = new JSONObject(response.toString());
                    int resultCode = jo.getInt("result");
                    if (resultCode == 111) {
                        result[0] = 1;
                        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uid", jo.getString("uid"));
                        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.IMAGE = jo.getString("imgSrc");
                        editor.commit();
                        String sign1 =MD5.md5("20001" +jo.getString("uid") + "iyubaV2");
                        uidPresenter.getHome("android", "json", 20001, Integer.parseInt(jo.getString("uid")), Integer.parseInt(jo.getString("uid")), 292, sign1);

                    } else if (resultCode == 114) {
                        toast("用户名太长或者太短");
                    } else if (resultCode == 112) {
                        toast("用户名被占用");
                    } else if (resultCode == 115) {
                        toast("手机号注册过");
                    } else {
                        toast("注册失败");
                    }
//                    成功 -> result:111
//                    result : 00 sign不正确
//                    114  用户名太长或者太短
//                    112 用户名被占用
//                    110 注册失败
//                    115 手机号注册过了
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // 启动 t 线程
        t.start();
        // 阻塞主线程（UI进程） 直到Thread 结束
        t.join();
        return result[0];
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    //吐司的一个小方法
    public void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FinishActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidUsername(String username) {
        String regex = "^[\u4E00-\u9FA5A-Za-z0-9]{3,15}$";
        return username.matches(regex);
    }

    public boolean isValidPassword(String password) {
        String regex = "^[A-Za-z0-9_]{6,15}$";
        return password.matches(regex);
    }
}
