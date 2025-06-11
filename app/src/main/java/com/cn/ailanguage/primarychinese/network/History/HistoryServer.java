package com.cn.ailanguage.primarychinese.network.History;


import com.cn.ailanguage.primarychinese.Bean.DeteleBean;
import com.cn.ailanguage.primarychinese.Bean.HistoryBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface HistoryServer {

    @GET
    Observable<HistoryBean> getHome(@Url  String url, @Query("uid") String uid,@Query("pages") String pages,@Query("pageCount") String pageCount,@Query("sign") String sign);


}
