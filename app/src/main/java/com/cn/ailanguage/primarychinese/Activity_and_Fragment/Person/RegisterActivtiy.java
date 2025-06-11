package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.PrivacyActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.TermActivity;
import com.cn.ailanguage.primarychinese.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivtiy extends AppCompatActivity {
    private Button nextStep, getCode;
    private EditText usernameEdit, codeEdit;
    private int TIME = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country = "86";//这是中国区号
    private String phone;
    private CheckBox checkbox_register;
    private ImageView back;
    private static final int CODE_REPEAT = 1; //重新发送
    private TimerTask tt;
    private TextView small_check;
    private Timer tm;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）
        setContentView(R.layout.activtiy_register);
        nextStep = findViewById(R.id.nextStep);
        usernameEdit = findViewById(R.id.usernameEdit);
        codeEdit = findViewById(R.id.codeEdit);
        getCode = findViewById(R.id.getCode);
        checkbox_register = findViewById(R.id.checkbox_register);
        back=findViewById(R.id.back);
        small_check=findViewById(R.id.small_check);
        //字跳转
        String text = "我已阅读并同意隐私协议和使用条款";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivtiy.this, PrivacyActivity.class);
                startActivity(intent);

            }
        };
        //隐私协议可点击
        @SuppressLint("ResourceAsColor") ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(R.color.homecolor);
        int startIndex1 = 7;
        int endIndex1 = 11;
        spannableString.setSpan(clickableSpan1, startIndex1, endIndex1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan1, startIndex1, endIndex1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivtiy.this, TermActivity.class);
                startActivity(intent);
            }
        };
        //使用条款可点击
        @SuppressLint("ResourceAsColor") ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(R.color.homecolor);
        int startIndex2 = 12;
        spannableString.setSpan(clickableSpan2, startIndex2, startIndex2+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, startIndex2, startIndex2+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        small_check.setText(spannableString);
        small_check.setMovementMethod(LinkMovementMethod.getInstance());

        //返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }

        //发送验证码按钮，确认是不是有该手机号
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = usernameEdit.getText().toString();
                //判断手机号是不是合法
                boolean validPhoneNumber = Patterns.PHONE.matcher(phone).matches();
                //判断手机号是不是为空
                if (!TextUtils.isEmpty(phone)) {
                    //判断有没有勾选隐私协议
                    if (checkbox_register.isChecked() == true) {
                        //判断电话是不是合法
                        if (validPhoneNumber) {
                            try {
                                //判断电话有没有被注册过
                                if (!isRegister(phone)) {
                                    //60s倒计时，并且输入验证码
                                    alterWarning();
                                } else {
                                    toast("该手机号已被注册过");
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            toast("手机号不正确");
                        }
                    } else {
                        toast("请先勾选已阅读隐私协议和使用条款");
                    }
                } else {
                    toast("请先输入手机号");
                }
            }
        });
        //下一步按钮
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEdit.getText().toString();
                phone = usernameEdit.getText().toString();
                //判断手机号是不是为空
                if(!TextUtils.isEmpty(phone)){
                    //电话是不是合法
                    boolean validPhoneNumber = Patterns.PHONE.matcher(phone).matches();
                    if(!TextUtils.isEmpty(code)){
                        if (checkbox_register.isChecked() == true) {
                            if (validPhoneNumber) {
                                SMSSDK.submitVerificationCode(country, phone, code);
                            }
                        }
                        else{
                            toast("请先勾选已阅读隐私协议和使用条款");
                        }
                    }
                    else{
                        toast("请输入验证码");
                    }
                }
                else{
                    toast("请先输入手机号");
                }
            }
        });
    }

    //验证码60s等待
    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                getCode.setEnabled(true);
                nextStep.setEnabled(true);
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                TIME = 60;//时间重置
                getCode.setText("重新发送验证码");
            } else {
                getCode.setText(TIME + "s");
            }
        }
    };

    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Intent intent = new Intent(RegisterActivtiy.this, FinishActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                    finish();
                    toast("验证成功");
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功
                    toast("获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//如果你调用了获取国家区号类表会在这里回调
                    //返回支持发送验证码的国家列表
                }
            } else {//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                toast("发送失败");
            }
        }
    };

    //弹窗确认下再发验证码
    private void alterWarning() {
        //构造器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("我们将要发送到" + phone + "验证"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                //通过sdk发送短信验证（请求获取短信验证码，在监听（eh）中返回）
                SMSSDK.getVerificationCode(country, phone);
                //做倒计时操作
//                Toast.makeText(RegisterActivtiy.this, "已发送" + which, Toast.LENGTH_SHORT).show();
                getCode.setEnabled(false);
                nextStep.setEnabled(true);
                tm = new Timer();
                tt = new TimerTask() {
                    @Override
                    public void run() {
                        hd.sendEmptyMessage(TIME--);
                    }
                };
                tm.schedule(tt, 0, 1000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(RegisterActivtiy.this, "已取消" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    //吐司的一个小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivtiy.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //判断电话之前有没有注册过
    boolean isRegister(String phoneNum) throws InterruptedException {

        final boolean[] isRegFlag = {false};

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                //HttpURLConnection内部发送网络请求
                StringBuilder sb = new StringBuilder("");
                sb.append("http://api.iyuba.com.cn/v2/api.iyuba?protocol=10009&username=")
                        .append(phoneNum)
                        .append("&format=json");
                try {
                    URL url = new URL(sb.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");  // 设置请求方式
                    connection.setConnectTimeout(8000);  // 设置超时时间
                    connection.setReadTimeout(8000);

                    // 取得输入流，并使用Reader读取
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));

                    // 得到结果
                    StringBuilder response = new StringBuilder("");
                    String tempLine;
                    while ((tempLine = reader.readLine()) != null) {
                        response.append(tempLine);
                    }

                    // 结果转json
                    JSONObject jo = new JSONObject(response.toString());
                    int resultCode = jo.getInt("result");

                    if (resultCode == 101) {
                        isRegFlag[0] = true;

                    } else {
                        isRegFlag[0] = false;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (connection != null)
                        connection.disconnect();
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        // 启动 t 线程
        t.start();
        // 阻塞主线程（UI进程） 直到Thread 结束
        t.join();
        // 这个 地方需要获取 最终结果 ， 因此 在 Thread 中 使用 Runnable 线程 ，使用 Thread 进行管理
        return isRegFlag[0];
    }

    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);

    }

}
