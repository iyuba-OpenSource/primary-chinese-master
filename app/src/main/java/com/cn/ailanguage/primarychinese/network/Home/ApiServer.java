package com.cn.ailanguage.primarychinese.network.Home;


import com.cn.ailanguage.primarychinese.Bean.DataBean;
import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;


import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiServer {

    @GET
    Observable<DataBean> getHome(@Url  String url, @Query("series") int series,@Query("type") String type);

    @GET
    Observable<WordBean> getWord(@Url  String url, @Query("series") String  series);

    @GET
    Observable<PomeBean> getPome(@Url  String url, @Query("series") String series, @Query("type") String type,@Query("flag") String flag);
}
