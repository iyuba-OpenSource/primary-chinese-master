package com.cn.ailanguage.primarychinese.network.Detele;



import com.cn.ailanguage.primarychinese.network.Home.Constant;
import com.cn.ailanguage.primarychinese.network.SSLSocketFactoryUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的框架
 * 网络请求管理者
 */
public class DeteleNetWorkManager {

    private static DeteleNetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile DeteleServer request = null;


    public static DeteleNetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (DeteleNetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new DeteleNetWorkManager();
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

    public static DeteleServer getRequest() {
        if (request == null) {
            synchronized (DeteleServer.class) {
                request = retrofit.create(DeteleServer.class);
            }
        }
        return request;
    }



}
