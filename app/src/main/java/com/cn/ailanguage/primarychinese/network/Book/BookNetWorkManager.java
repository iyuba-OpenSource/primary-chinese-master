package com.cn.ailanguage.primarychinese.network.Book;



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
public class BookNetWorkManager {

    private static BookNetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile BookServer request = null;


    public static BookNetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (BookNetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new BookNetWorkManager();
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

    public static BookServer getRequest() {
        if (request == null) {
            synchronized (BookServer.class) {
                request = retrofit.create(BookServer.class);
            }
        }
        return request;
    }



}
