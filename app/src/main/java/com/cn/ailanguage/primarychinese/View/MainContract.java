package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface MainContract {
    interface MainView extends LoadingView {

        void getHome(MainBean mainBean);
    }

    interface MainPresenter extends IBasePresenter<MainView> {

        void getHome(String type,String voaid);
    }

    interface MainModel extends BaseModel {

        Disposable getHome(String type,String voaid,MainContract.MainCallback mainCallback);
    }

    interface MainCallback {

        void success(MainBean mainBean);

        void error(Exception e);
    }}
