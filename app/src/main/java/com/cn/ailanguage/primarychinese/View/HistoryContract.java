package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.HistoryBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface HistoryContract {
    interface HistoryView extends LoadingView {

        void getHome(HistoryBean historyBean);

    }

    interface HistoryPresenter extends IBasePresenter<HistoryView> {

        void getHome(String uid,String pages,String pageCount,String sign);
    }

    interface HistoryModel extends BaseModel {

        Disposable getHome(String uid,String pages,String pageCount,String sign, HistoryContract.HistoryCallback historyCallback);
    }

    interface HistoryCallback {

        void success(HistoryBean historyBean);

        void error(Exception e);
    }}




