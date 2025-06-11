package com.cn.ailanguage.primarychinese.network.Main;
import com.cn.ailanguage.primarychinese.Bean.MainBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MainServer {
    @GET
    Observable<MainBean> getHome(@Url String url, @Query("type") String type, @Query("voaid") String voaid);

}
