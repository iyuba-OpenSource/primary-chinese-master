package com.cn.ailanguage.primarychinese.presenter;

import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Model.MainModel;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<MainContract.MainView,MainContract.MainModel> implements MainContract.MainPresenter{
    @Override
    public void getHome(String type,String voaid) {
        Disposable disposable = model.getHome(type,voaid, new MainContract.MainCallback() {
            @Override
            public void success(MainBean mainBean) {
                System.out.println("======"+mainBean.getInfo());
                view.getHome(mainBean);
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
    protected MainContract.MainModel initModel() {
        return new MainModel();
    }

    @Override
    public void attchView(V view) {

    }
}
