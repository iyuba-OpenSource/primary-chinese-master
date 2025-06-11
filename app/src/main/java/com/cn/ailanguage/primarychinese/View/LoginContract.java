package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.LoginBean;
import com.cn.ailanguage.primarychinese.Bean.OneClickBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface LoginContract {
    interface LoginView extends LoadingView {
        //账号登录
        void getHome(LoginBean loginBean);
        //一键登录
        void getOneClick(OneClickBean oneClickBean);
    }

    interface LoginPresenter extends IBasePresenter<LoginView> {

        //账号登录
        void getHome(String username, String password, String app, String taken, String format, String appid, String protocol, String sign);
        //一键登录
        void getOneClick(String appId,String appkey,String opToken,String token,String operator);
    }

    interface LoginModel extends BaseModel {
        //账号登录
        Disposable getHome(String username, String password, String app, String taken, String format, String appid, String protocol, String sign, LoginCallback loginCallback);
        //一键登录
        Disposable getOneClick(String appId,String appkey,String opToken,String token,String operator,OneClickCallback oneClickCallback);
    }
    interface LoginCallback {

        void success(LoginBean loginBean);

        void error(Exception e);
    }
    interface OneClickCallback {

        void success(OneClickBean oneClickBean);

        void error(Exception e);
    }
}