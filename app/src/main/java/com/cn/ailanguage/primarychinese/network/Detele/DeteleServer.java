package com.cn.ailanguage.primarychinese.network.Detele;


import com.cn.ailanguage.primarychinese.Bean.DeteleBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface DeteleServer {

    @GET
    Observable<DeteleBean> getHome(@Url  String url, @Query("protocol") int protocol, @Query("username") String username, @Query("password") String password, @Query("format") String format, @Query("sign") String sign);


}
