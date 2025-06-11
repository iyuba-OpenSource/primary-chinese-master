package com.cn.ailanguage.primarychinese.View;

import android.os.Bundle;

import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface UidContract {
    interface UidView extends LoadingView {
        void onCreate(Bundle savedInstanceState);
        //登录
        void getHome(UidBean uidBean);
        //注册
    }

    interface UidPresenter extends IBasePresenter<UidView> {
        void getHome(String platform, String format,int protocol,int id,int myid,int appid,String sign);
    }

    interface UidModel extends BaseModel {
        Disposable getHome(String platform, String format,int protocol,int id,int myid,int appid,String sign,UidContract.UidCallback uidCallback);
    }
    interface UidCallback {

        void success(UidBean UidBean);

        void error(Exception e);
    }
}