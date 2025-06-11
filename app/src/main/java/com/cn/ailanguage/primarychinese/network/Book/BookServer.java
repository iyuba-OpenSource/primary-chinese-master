package com.cn.ailanguage.primarychinese.network.Book;


import com.cn.ailanguage.primarychinese.Bean.AllWordBean;
import com.cn.ailanguage.primarychinese.Bean.BookBean;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface BookServer {

    @GET
    Observable<BookBean> getHome(@Url  String url, @Query("type") String type, @Query("category") int category, @Query("sign") String sign);

    @GET
    Observable<AllWordBean> getAllWord(@Url  String url, @Query("series") String series, @Query("result") String result);

}
