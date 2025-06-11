package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Top_page;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.network.Book.Constant;
import com.umeng.commonsdk.UMConfigure;

public class MyApplication extends Application {


    @SuppressLint("StaticFieldLeak")
    private static Context context ;

    private String channel;
    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        channel= com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.channel;
        //友盟预初始化
        UMConfigure.preInit(this, "6548467358a9eb5b0a004a53", channel);
        UMConfigure.setLogEnabled(false);

        Constant.URL_PROTOCOLUSE = "http://iuserspeech.iyuba.cn:9001/api/protocoluse666.jsp?company=1&apptype=" + getString(R.string.app_name);

        Constant.URL_PROTOCOLPRI = "http://iuserspeech.iyuba.cn:9001/api/protocolpri.jsp?company=1&apptype=" + getString(R.string.app_name);
        Log.d("xwh6", "onCreate: ");
    }

    public static Context getContext() {
        return context;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        context = null;
    }

}
