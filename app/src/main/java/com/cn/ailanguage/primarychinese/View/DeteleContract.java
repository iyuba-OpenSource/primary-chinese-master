package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.DeteleBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface DeteleContract {
    interface DeteleView extends LoadingView {

        void getHome(DeteleBean deteleBean);
    }

    interface DetelePresenter extends IBasePresenter<DeteleView> {

        void getHome(int protocol, String username,String password,String format,String sign);
    }

    interface DeteleModel extends BaseModel {

        Disposable getHome(int protocol, String username,String password,String format,String sign, DeteleContract.DeteleCallback deteleCallback);
    }

    interface DeteleCallback {

        void success(DeteleBean deteleBean);

        void error(Exception e);
    }}