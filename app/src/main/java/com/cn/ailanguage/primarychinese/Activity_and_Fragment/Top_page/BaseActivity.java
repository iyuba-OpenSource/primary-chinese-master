package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Top_page;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.ShareData;

public class BaseActivity extends AppCompatActivity {
    private ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化 GlobalData 实例
        shareData=new ShareData(getApplicationContext());
    }

    public ShareData getShareData() {
        return shareData;
    }
}
