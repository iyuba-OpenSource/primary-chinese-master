package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cn.ailanguage.primarychinese.R;


public class BuyPrivacyActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView backwebview;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_privacy);
        webView=findViewById(R.id.privacy_policy_webview);
        backwebview=findViewById(R.id.back_webview);

        //为返回按钮设置点击事件
        backwebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }
//        设置超链接
        webView.getSettings().setJavaScriptEnabled(true);
// 处理页面加载进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 显示加载进度
            }
        });
        webView.loadUrl("http://iuserspeech.iyuba.cn:9001/api/vipServiceProtocol.jsp?company=%E5%8C%97%E4%BA%AC%E7%88%B1%E8%AF%AD%E5%90%A7&type=app");

    }
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed(); // 如果没有上一个 Fragment，则执行默认的返回操作
        } else {
            getSupportFragmentManager().popBackStack(); // 返回上一个 Fragment
        }
    }
}
