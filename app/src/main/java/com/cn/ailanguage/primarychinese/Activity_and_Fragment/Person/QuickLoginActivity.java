package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Bean.LoginBean;
import com.cn.ailanguage.primarychinese.Bean.OneClickBean;
import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.LoginContract;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Login.LoginNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.LoginPresenter;
import com.cn.ailanguage.primarychinese.presenter.UidPresenter;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class QuickLoginActivity extends AppCompatActivity implements LoginContract.LoginView , UidContract.UidView{
    private EditText username,password;
    private ImageView back;
    LoginPresenter loginPresenter;
    UidPresenter uidPresenter;
    public interface OnClickActivityListener {
        void onClickActivity();
    }
    private OnClickActivityListener mOnClickActivityListener;
    /**
     * 绑定接口
     *
     * @param
     */
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof OnClickActivityListener) {
            mOnClickActivityListener = (OnClickActivityListener) fragment;
        }
    }
    String phone,userstring,passwordMore;
    private Button login;
    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_login);
        username=findViewById(R.id.username_quick);
        password=findViewById(R.id.password_quick);
        login=findViewById(R.id.login_quick);
        back=findViewById(R.id.back_quick);

        //个人uid信息
        LoginNetWorkManager.getInstance().init();
        loginPresenter = new LoginPresenter();
        loginPresenter.attchView(this);
        //获取vip信息
        UidNetWorkManager.getInstance().init();
        uidPresenter = new UidPresenter();
        uidPresenter.attchView(this);

        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone"); // 获取传递的信息
        // 默认姓名
        int randomNum = (int)(Math.random() * 9000 + 1000);
        String randomStr = String.valueOf(randomNum);
        String namefake="user_o"+randomStr+phone.substring(phone.length() - 4);
        username.setText(namefake);
        password.setText(phone.substring(phone.length() - 6));
        //登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()){
                    toast("用户名不能为空");
                }else{
                    if(password.getText().toString().isEmpty()){
                        toast("密码不能为空");
                    }else{
                        try {
                            passwordMore=password.getText().toString();
                            isResult(String.valueOf(username.getText()),passwordMore);
                            try {
                                 userstring = URLEncoder.encode(username.getText().toString(), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                            String passwordstringback = MD5.md5(password.getText().toString());
                            String sign = MD5.md5("11001" + username.getText().toString() + passwordstringback + "iyubaV2");
                            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.PASSWORD=passwordstringback;
                            loginPresenter.getHome(userstring, passwordstringback, "primarychinese", "", "json", "292", "11001", sign);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        });
        //返回键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickActivityListener!=null){
                    mOnClickActivityListener.onClickActivity();
                }
                Log.d("xwh2", Constant.USERNAME);
                finish();

            }
        });
    }


    //吐司的一个小方法
    public void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QuickLoginActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
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
//                        Constant.FINISH = 1;
                        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uid", jo.getString("uid"));
                        editor.putString("image", jo.getString("imgSrc"));
                        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.IMAGE = jo.getString("imgSrc");
                        editor.commit();
                        finish();
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
    //返回滑动
    @Override
    public void onBackPressed() {
        back.performClick();
    }
    @Override
    public void getHome(LoginBean loginBean) {
        if (loginBean.getResult().equals("101")) {
            //获取uid
            SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uid", String.valueOf(loginBean.getUid()));
            editor.putString("password", String.valueOf(passwordMore));
            editor.putString("image", String.valueOf(loginBean.getImgSrc()));
            editor.putString("username", String.valueOf(loginBean.getUsername()));
            editor.putString("username", String.valueOf(loginBean.getUsername()));
            editor.putString("expireTime",String.valueOf(loginBean.getExpireTime()));
            editor.putString("credit", String.valueOf(loginBean.getCredits()));
            editor.putString("email", String.valueOf(loginBean.getCredits()));
            editor.putString("mobile", String.valueOf(loginBean.getCredits()));
            editor.putString("amount", String.valueOf(loginBean.getAmount()));
            editor.commit();
            com.cn.ailanguage.primarychinese.network.Main.Constant.USERNAME=String.valueOf(loginBean.getUsername());
            String sign1 = MD5.md5("20001" + String.valueOf(loginBean.getUid()) + "iyubaV2");
            uidPresenter.getHome("android", "json", 20001, Integer.parseInt(String.valueOf(loginBean.getUid())), Integer.parseInt(String.valueOf(loginBean.getUid())), 292, sign1);
            com.cn.ailanguage.primarychinese.network.Main.Constant.TURN=1;
            Log.d("xwh890", "12 "+com.cn.ailanguage.primarychinese.network.Main.Constant.TURN);
        } else {
//            Toast.makeText(this, "请输入正确的用户名和密码", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void getHome(UidBean uidBean) {

        long days = parseLong(uidBean.getExpireTime());
        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL=days;
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
        Constant.PACK = String.valueOf(uidBean.getMoney());
        Constant.INTERAL = uidBean.getCredits();
        Constant.CORN = String.valueOf(uidBean.getAmount());
        Constant.USERNAME = uidBean.getUsername();
        //微课登录
        SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        User user = new User();
        user.vipStatus = String.valueOf(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME);
        if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME.equals("0")) {
            user.vipExpireTime = Long.parseLong(sharedPreferences.getString("expireTime", "nothing"));
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
//        back.performClick();
    }
    @Override
    public void getOneClick(OneClickBean oneClickBean) {

    }
    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }
}
