package com.cn.ailanguage.primarychinese.presenter;
import android.util.Log;

import com.cn.ailanguage.primarychinese.Bean.LoginBean;
import com.cn.ailanguage.primarychinese.Bean.OneClickBean;
import com.cn.ailanguage.primarychinese.Model.LoginModel;
import com.cn.ailanguage.primarychinese.View.LoginContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginContract.LoginView,LoginContract.LoginModel> implements LoginContract.LoginPresenter{
    @Override
    public void getHome(String username, String password,String app,String taken,String format,String appid,String protocol,String sign) {
        Disposable disposable = model.getHome(username,password,app,taken,format,appid,protocol,sign,new LoginContract.LoginCallback() {
            @Override
            public void success(LoginBean loginBean) {
                System.out.println("======"+loginBean);
                view.getHome(loginBean);

                Log.e("lhz", "success: " + loginBean );
            }

            @Override
            public void error(Exception e) {
                Log.d("xwh111", e+"*");
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
    @Override
    public void getOneClick(String appId,String appkey,String opToken,String token,String operator) {
        Disposable disposable = model.getOneClick(appId,appkey,opToken,token,operator,
                new LoginContract.OneClickCallback() {
                    @Override
                    public void success(OneClickBean oneClickBean) {
                        System.out.println("======"+oneClickBean);
                        view.getOneClick(oneClickBean);
                    }
                    @Override
                    public void error(Exception e) {
                        Log.d("xwh111", e+"*");
                        if (e instanceof UnknownHostException) {

                            view.toast("请求超时");
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    protected LoginContract.LoginModel initModel() {
        return new LoginModel();
    }

    @Override
    public void attchView(V view) {

    }
}
