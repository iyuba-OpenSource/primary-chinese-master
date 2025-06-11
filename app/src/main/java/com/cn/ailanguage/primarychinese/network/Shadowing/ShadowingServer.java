package com.cn.ailanguage.primarychinese.network.Shadowing;


import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderUserBean;
import com.cn.ailanguage.primarychinese.Bean.MusicBean;
import com.cn.ailanguage.primarychinese.Bean.ShadowingBean;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ShadowingServer {
    //上传合成后的录音
    @GET
    Observable<ShadowingBean> getHome(@Url String url, @Query("platform") String platform, @Query("format") String format, @Query("protocol") String protocol, @Query("topic") String topic, @Query("userid") String userid,@Query("username") String username, @Query("voaid") String voaid, @Query("idIndex") String idIndex, @Query("paraid") String paraid, @Query("score") String score, @Query("shuoshuotype") String shuoshuotype, @Query("content") String content, @Query("rewardVersion") String rewardVersion, @Query("appid") String appid);

    //上传录音，获得颜色什么的
    @POST("")
    Observable<EvaBean> getEvaluating(@Url String url, @Body RequestBody requestBody);

    //合成录音
    @GET
    Observable<MusicBean> getMusic(@Url String url, @Query("audios") String audios, @Query("type") String type);

    //    platform,format,protocol,topic,userid,username,voaid,idIndex,paraid,score,shuoshuotype,content,rewardVersion,appid
    //排行榜
    @GET
    Observable<LeaderBean> getMusic(@Url String url, @Query("uid") String uid, @Query("type") String type,@Query("total") String total,@Query("start") String start,@Query("topic") String topic,@Query("topicid") String topicid,@Query("sign") String sign);
    @GET
    Observable<LeaderUserBean> getLeaderUser(@Url String url, @Query("shuoshuoType") String shuoshuoType, @Query("topic") String topic,@Query("topicId") String topicId,@Query("uid") String uid,@Query("sign") String sign);
}
