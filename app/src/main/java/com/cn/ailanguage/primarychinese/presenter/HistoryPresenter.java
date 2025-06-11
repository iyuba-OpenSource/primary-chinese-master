package com.cn.ailanguage.primarychinese.presenter;

import com.cn.ailanguage.primarychinese.Bean.HistoryBean;
import com.cn.ailanguage.primarychinese.Model.HistoryModel;
import com.cn.ailanguage.primarychinese.View.HistoryContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class HistoryPresenter extends BasePresenter<HistoryContract.HistoryView, HistoryContract.HistoryModel> implements HistoryContract.HistoryPresenter{
    @Override
    public void getHome(String uid,String pages,String pageCount,String sign) {
        Disposable disposable = model.getHome(uid,pages,pageCount,sign,new HistoryContract.HistoryCallback() {
            @Override
            public void success(HistoryBean historyBean) {
                System.out.println("======");
                view.getHome(historyBean);
            }


            //
            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
//
    @Override
    protected HistoryContract.HistoryModel initModel() {
        return new HistoryModel();
    }

    @Override
    public void attchView(V view) {

    }
}
