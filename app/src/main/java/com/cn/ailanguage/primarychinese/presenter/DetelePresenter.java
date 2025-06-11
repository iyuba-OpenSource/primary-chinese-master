package com.cn.ailanguage.primarychinese.presenter;

import android.util.Log;

import com.cn.ailanguage.primarychinese.Bean.DeteleBean;
import com.cn.ailanguage.primarychinese.Model.DeteleModel;
import com.cn.ailanguage.primarychinese.View.DeteleContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class DetelePresenter extends BasePresenter<DeteleContract.DeteleView, DeteleContract.DeteleModel> implements DeteleContract.DetelePresenter{
    @Override
    public void getHome(int protocol, String username, String password, String format, String sign) {
        Disposable disposable = model.getHome(protocol,username,password,format,sign,new DeteleContract.DeteleCallback() {
            @Override
            public void success(DeteleBean deteleBean) {
                System.out.println("======");
                Log.d("xwh", "success: ");
                view.getHome(deteleBean);
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
    protected DeteleContract .DeteleModel initModel() {
        return new DeteleModel();
    }

    @Override
    public void attchView(V view) {

    }
}
