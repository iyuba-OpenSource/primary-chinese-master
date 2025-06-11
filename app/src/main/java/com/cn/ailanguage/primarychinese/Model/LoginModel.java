package com.cn.ailanguage.primarychinese.Model;

import com.cn.ailanguage.primarychinese.Bean.LoginBean;
import com.cn.ailanguage.primarychinese.Bean.OneClickBean;
import com.cn.ailanguage.primarychinese.View.LoginContract;
import com.cn.ailanguage.primarychinese.network.Login.LoginNetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginModel implements LoginContract.LoginModel {

    @Override
    public Disposable getHome(String username, String password, String app, String taken, String format, String appid, String protocol,String sign, LoginContract.LoginCallback loginCallback) {
        return LoginNetWorkManager
                .getRequest()
                .getHome("http://api.iyuba.com.cn/v2/api.iyuba?", username,password,app,taken,format,appid,protocol, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean mainBean) throws Exception {
                        loginCallback.success(mainBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loginCallback.error((Exception) throwable);
                    }
                });
    }
    @Override
    public Disposable getOneClick(String appId,String appkey,String opToken,String token,String operator,
                                  LoginContract.OneClickCallback oneClickCallback) {
        return LoginNetWorkManager
                .getRequest()
                .getOneClick("http://api.iyuba.com.cn/v2/api.iyuba?protocol=10010",appId,appkey,opToken,token,operator)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OneClickBean>() {
                    @Override
                    public void accept(OneClickBean oneClickBean) throws Exception {
                        System.out.println("the first");
                        System.out.println(oneClickBean);

                        oneClickCallback.success(oneClickBean);
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        oneClickCallback.error((Exception) throwable);
                    }
                });
    }
}
