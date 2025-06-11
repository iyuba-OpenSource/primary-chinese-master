package com.cn.ailanguage.primarychinese.network.Login;

import com.cn.ailanguage.primarychinese.Bean.LoginBean;
import com.cn.ailanguage.primarychinese.Bean.OneClickBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface LoginServer {
    @GET
    Observable<LoginBean> getHome(@Url String url, @Query("username") String username, @Query("password") String password, @Query("app") String app, @Query("taken") String taken, @Query("format") String format, @Query("appid") String appid, @Query("protocol") String protocol, @Query("sign") String sign);
    @GET
    Observable<OneClickBean> getOneClick(@Url String url, @Query("appId") String appId, @Query("appkey") String appkey, @Query("opToken") String opToken, @Query("token") String token, @Query("operator") String operator);


}
