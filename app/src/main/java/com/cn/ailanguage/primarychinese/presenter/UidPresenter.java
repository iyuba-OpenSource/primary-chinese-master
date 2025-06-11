package com.cn.ailanguage.primarychinese.presenter;

import android.util.Log;

import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.Model.UidModel;;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class UidPresenter extends BasePresenter<UidContract.UidView,UidContract.UidModel> implements UidContract.UidPresenter{

    @Override
    public void getHome(String platform, String format, int protocol, int id, int myid, int appid, String sign) {
        Disposable disposable = model.getHome(platform, format,protocol,id,myid,appid,sign, new UidContract.UidCallback() {
            @Override
            public void success(UidBean uidBean) {
                System.out.println("======"+uidBean.getUsername());
                view.getHome(uidBean);

                Log.e("qxy", "success: " + uidBean.toString() );

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
    protected UidContract.UidModel initModel() {


        return new UidModel();
    }

    @Override
    public void attchView(V view) {

    }

}
