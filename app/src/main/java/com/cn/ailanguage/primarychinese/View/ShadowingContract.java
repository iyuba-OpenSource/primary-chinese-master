package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderUserBean;
import com.cn.ailanguage.primarychinese.Bean.MusicBean;
import com.cn.ailanguage.primarychinese.Bean.ShadowingBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public interface ShadowingContract {
    interface ShadowingView extends LoadingView {
        //上传合成
        void getHome(ShadowingBean shadowingBean);

        //上传录音
        void getEvaluating(EvaBean evaBean);

        //合成后的音乐地址
        void getMusic(MusicBean musicBean);
        //排行榜
        void getLeader(LeaderBean leaderBean);
        void getLeaderUser(LeaderUserBean leaderuserBean);

    }

    interface ShadowingPresenter extends IBasePresenter<ShadowingView> {
        //评测录音
        void getEvaluating(RequestBody requestBody);

        //上传合成
        void getHome(String platform, String format, String protocol, String topic, String username,String userid,  String voaid,String idIndex,String paraid,String score,String shuoshuotype,String content,String rewardVersion,String appid);

        //合成
        void getMusic(String audios, String type);
        //排行榜
        void getLeader(String uid,String type,String total,String start,String topic,String topicid,String sign);
        //排行榜的用户信息
        void getLeaderUser(String shuoshuoType,String topic,String topicId,String uid,String sign);
    }

    interface ShadowingModel extends BaseModel {
        //上传合成
        Disposable getHome(String platform, String format, String protocol, String topic, String userid,String username, String voaid,String idIndex,String paraid,String score,String shuoshuotype,String content,String rewardVersion,String appid, ShadowingContract.ShadowingCallback shadowingCallback);

        //评测录音
        Disposable getEvaluating(RequestBody requestBody, ShadowingContract.CallBackEvaluating callBackEvaluating);

        //合成
        Disposable getMusic(String audios, String type, ShadowingContract.CallBackMusic callBackMusic);
        //排行榜
        Disposable getLeader(String uid,String type,String total,String start,String topic,String topicid,String sign,ShadowingContract.CallBackLeader callBackLeader);
        Disposable getLeaderUser(String shuoshuoType,String topic,String topicId,String uid,String sign,ShadowingContract.CallBackLeaderUser callBackLeaderUser);
    }

    interface CallBackEvaluating {

        void successEvaluating(EvaBean evaBean);

        void errorEvaluating(Exception e);

    }

    interface ShadowingCallback {

        void success(ShadowingBean shadowingBean);

        void error(Exception e);
    }

    interface CallBackMusic {

        void success(MusicBean musicBean);

        void error(Exception e);
    }
    interface CallBackLeader {

        void success(LeaderBean leaderBean);

        void error(Exception e);
    }
    interface CallBackLeaderUser {

        void success(LeaderUserBean leaderuserBean);

        void error(Exception e);
    }

}




