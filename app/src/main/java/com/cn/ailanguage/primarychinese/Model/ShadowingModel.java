package com.cn.ailanguage.primarychinese.Model;

import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderUserBean;
import com.cn.ailanguage.primarychinese.Bean.MusicBean;
import com.cn.ailanguage.primarychinese.Bean.ShadowingBean;
import com.cn.ailanguage.primarychinese.View.ShadowingContract;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;
import com.cn.ailanguage.primarychinese.network.Shadowing.ShadowingNetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class ShadowingModel implements ShadowingContract.ShadowingModel {

    @Override
    public Disposable getHome(String platform, String format, String protocol, String topic, String userid, String username,String voaid,String idIndex,String paraid,String score,String shuoshuotype,String content,String rewardVersion,String appid, ShadowingContract.ShadowingCallback shadowingCallback) {
        return ShadowingNetWorkManager
                .getRequest()
                .getHome("http://voa.iyuba.cn/voa/UnicomApi",platform,format,protocol,topic,userid,username,voaid,idIndex,paraid,score,shuoshuotype,content,rewardVersion,appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ShadowingBean>() {
                    @Override
                    public void accept(ShadowingBean shadowingBean) throws Exception {
                       shadowingCallback.success(shadowingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        shadowingCallback.error((Exception) throwable);
                    }
                });
    }


    @Override
    public Disposable getEvaluating(RequestBody requestBody, ShadowingContract.CallBackEvaluating callBackEvaluating) {
        return ShadowingNetWorkManager
                .getRequest()
//        https://iuserspeech.iyuba.cn:444/test/ai/              sentence, paraId, newsId, IdIndex, type, appId, wordId, flg, userId, file
                .getEvaluating("https://ai.aienglish.ltd/testcn/ai/?flg=1", requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EvaBean>() {

                    @Override
                    public void accept(EvaBean evaBean) throws Exception {
                        callBackEvaluating.successEvaluating(evaBean);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBackEvaluating.errorEvaluating((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getMusic(String audios, String type, ShadowingContract.CallBackMusic callBackMusic) {
        return ShadowingNetWorkManager
                .getRequest()
                .getMusic("https://ai.aienglish.ltd/test/merge/",audios,type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MusicBean>() {
                    @Override
                    public void accept(MusicBean musicBean) throws Exception {
                        callBackMusic.success(musicBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBackMusic.error((Exception) throwable);
                    }
                });
    }
    @Override
    public Disposable getLeader(String uid,String type,String total,String start,String topic,String topicid,String sign, ShadowingContract.CallBackLeader callBackLeader) {
        return ShadowingNetWorkManager
                .getRequest()
                .getMusic("http://daxue.iyuba.cn/ecollege/getTopicRanking.jsp",uid,type,total,start,topic,topicid,sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LeaderBean>() {
                    @Override
                    public void accept(LeaderBean leaderBean) throws Exception {
                        callBackLeader.success(leaderBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBackLeader.error((Exception) throwable);
                    }
                });
    }
    @Override
    public Disposable getLeaderUser(String shuoshuoType,String topic,String topicId,String uid,String sign,ShadowingContract.CallBackLeaderUser callBackLeaderUser) {
        return ShadowingNetWorkManager
                .getRequest()
                .getLeaderUser("https://apps.iyuba.cn/voa/getWorksByUserId.jsp",shuoshuoType,topic,topicId,uid,sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LeaderUserBean>() {
                    @Override
                    public void accept(LeaderUserBean leaderBean) throws Exception {
                        callBackLeaderUser.success(leaderBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBackLeaderUser.error((Exception) throwable);
                    }
                });
    }

}
