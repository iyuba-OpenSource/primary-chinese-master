package com.cn.ailanguage.primarychinese.presenter;

import android.util.Log;

import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderUserBean;
import com.cn.ailanguage.primarychinese.Bean.MusicBean;
import com.cn.ailanguage.primarychinese.Bean.ShadowingBean;
import com.cn.ailanguage.primarychinese.Model.ShadowingModel;
import com.cn.ailanguage.primarychinese.View.ShadowingContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public class ShadowingPresenter extends BasePresenter<ShadowingContract.ShadowingView,ShadowingContract.ShadowingModel> implements ShadowingContract.ShadowingPresenter{
    @Override
    public void getEvaluating(RequestBody requestBody) {
        Disposable disposable = model.getEvaluating(requestBody, new ShadowingContract.CallBackEvaluating() {
            @Override
            public void successEvaluating(EvaBean evaBean) {
                view.getEvaluating(evaBean);


            }

            @Override
            public void errorEvaluating(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getHome(String platform, String format, String protocol, String topic, String userid,String username , String voaid,String idIndex,String paraid,String score,String shuoshuotype,String content,String rewardVersion,String appid) {
        Disposable disposable = model.getHome(platform,format,protocol,topic,userid,username,voaid,idIndex,paraid,score,shuoshuotype,content,rewardVersion,appid, new ShadowingContract.ShadowingCallback() {
            @Override
            public void success(ShadowingBean shadowingBean) {
//                System.out.println("======"+shadowingBean.getInfo());
                view.getHome(shadowingBean);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getMusic(String audios, String type) {
        Disposable disposable = model.getMusic(audios,type, new ShadowingContract.CallBackMusic() {
            @Override
            public void success(MusicBean musicBean) {

                //                System.out.println("======"+shadowingBean.getInfo());
                view.getMusic(musicBean);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getLeader(String uid, String type, String total, String start, String topic, String topicid, String sign) {
        Disposable disposable = model.getLeader(uid,type,total,start,topic,topicid,sign, new ShadowingContract.CallBackLeader() {
            @Override
            public void success(LeaderBean leaderBean) {
//                System.out.println("======"+shadowingBean.getInfo());
                view.getLeader(leaderBean);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
    @Override
    public void getLeaderUser(String shuoshuoType,String topic,String topicId,String uid,String sign) {
        Disposable disposable = model.getLeaderUser(shuoshuoType,topic,topicId,uid,sign, new ShadowingContract.CallBackLeaderUser() {
            @Override
            public void success(LeaderUserBean leaderuserBean) {
//                System.out.println("======"+shadowingBean.getInfo());
                view.getLeaderUser(leaderuserBean);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    protected ShadowingContract.ShadowingModel initModel() {
        return new ShadowingModel();
    }

    @Override
    public void attchView(V view) {

    }

//    @Override
//    public void getHome(String uid, String pages, String pageCount, String sign) {
//
//    }
}
