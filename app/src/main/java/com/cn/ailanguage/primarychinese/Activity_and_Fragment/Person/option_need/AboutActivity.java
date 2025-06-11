package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Bean.DeteleBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.DeteleContract;
import com.cn.ailanguage.primarychinese.network.Detele.DeteleNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.presenter.DetelePresenter;

public class AboutActivity extends AppCompatActivity implements DeteleContract.DeteleView{
    private TextView myTextView;
    private LinearLayout detele;
    private DetelePresenter detelePresenter;

    ImageView back;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        myTextView = findViewById(R.id.myTextview);
        back=findViewById(R.id.back_webview);

        DeteleNetWorkManager.getInstance().init();
        detelePresenter = new DetelePresenter();
        detelePresenter.attchView(this);
        //为返回按钮设置点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }
        //弹出框，销户
        detele = findViewById(R.id.detele);
        detele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        deteleDecorate();
    }
    //更新布局
    private  void deteleDecorate(){
        if(!Constant.USERNAME.equals("nothing")){
            //已登录
            detele.setVisibility(View.VISIBLE);
        }else{
            detele.setVisibility(View.GONE);
        }
    }
    private void showLogoutDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        LayoutInflater inflater2 = getLayoutInflater();
        View dialogView2 = inflater2.inflate(R.layout.detele_user_one, null);
        builder2.setView(dialogView2);

        final AlertDialog dialog2 = builder2.create();
        dialog2.show();

        Button confirmButton2 = dialogView2.findViewById(R.id.confirmButton);
        Button cancelButton2 = dialogView2.findViewById(R.id.cancelButton);

        confirmButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 处理密码验证逻辑
                showPasswordDialog();
                dialog2.dismiss();
            }
        });

        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }

    //核对密码
    private void showPasswordDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        LayoutInflater inflater2 = getLayoutInflater();
        View dialogView2 = inflater2.inflate(R.layout.detele_user_tow, null);
        builder2.setView(dialogView2);

        final AlertDialog dialog2 = builder2.create();
        dialog2.show();

        Button confirmButton2 = dialogView2.findViewById(R.id.confirmButton);
        Button cancelButton2 = dialogView2.findViewById(R.id.cancelButton);
        EditText passwordEditText2 = dialogView2.findViewById(R.id.passwordEditText);

        confirmButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 处理密码验证逻辑
                String password = MD5.md5(passwordEditText2.getText().toString());
                // 在这里验证密码的逻辑，并执行相应的操作

                    // 输入的密码正确，继续执行退出登录操作
                    String sign1 = MD5.md5("11005" + Constant.USERNAME + password + "iyubaV2");
                Log.e("qxy", "zhuxiao: " + Constant.USERNAME + " " + passwordEditText2.getText().toString());

                detelePresenter.getHome(11005, Constant.USERNAME, password, "json", sign1);
                    dialog2.dismiss();
            }
        });

        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }

    @Override
    public void getHome(DeteleBean deteleBean) {
        if(deteleBean.getMessage().equals("success")){
            SharedPreferences sharedPreferences = getSharedPreferences("ceshi", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uid", "nothing");
            editor.commit();
            finish();
            Constant.USERNAME = "nothing";
            Toast.makeText(AboutActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
            Log.e("qxy", "zhuxiao:  suc" );
            deteleDecorate();
        }else{
            Log.e("qxy", "zhuxiao:  fail" );
            Toast.makeText(AboutActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
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
    public void onResume(){
        deteleDecorate();
        super.onResume();
    }
}
