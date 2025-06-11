package com.cn.ailanguage.primarychinese.network.Buy;



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
public class BuyNetWorkManager {

    private static BuyNetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile BuyServer request = null;


    public static BuyNetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (BuyNetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new BuyNetWorkManager();
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

    public static BuyServer getRequest() {
        if (request == null) {
            synchronized (BuyServer.class) {
                request = retrofit.create(BuyServer.class);
            }
        }
        return request;
    }



}
