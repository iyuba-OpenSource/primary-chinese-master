package com.cn.ailanguage.primarychinese.network.Uid;
import com.cn.ailanguage.primarychinese.Bean.UidBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UidServer {
    @GET
    Observable<UidBean> getHome(@Url String url, @Query("platform") String platform, @Query("format") String format, @Query("protocol") int protocol, @Query("id") int id, @Query("myid") int myid, @Query("appid") int appid, @Query("sign") String sign);

}
