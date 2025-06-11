package com.cn.ailanguage.primarychinese.network.Main;



import com.cn.ailanguage.primarychinese.network.SSLSocketFactoryUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的框架
 * 网络请求管理者
 */
public class MainNetWorkManager {

    private static MainNetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile MainServer request = null;


    public static MainNetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (MainNetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new MainNetWorkManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * 初始化必要对象和参数
     */
    public void init() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MainServer getRequest() {
        if (request == null) {
            synchronized (MainServer.class) {
                request = retrofit.create(MainServer.class);
            }
        }
        return request;
    }



}
