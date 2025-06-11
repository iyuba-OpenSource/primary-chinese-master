package com.cn.ailanguage.primarychinese.Model;

import com.cn.ailanguage.primarychinese.Bean.HistoryBean;
import com.cn.ailanguage.primarychinese.Bean.LoginBean;
import com.cn.ailanguage.primarychinese.View.HistoryContract;
import com.cn.ailanguage.primarychinese.View.LoginContract;
import com.cn.ailanguage.primarychinese.network.History.HistoryNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Login.LoginNetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HistoryModel implements HistoryContract.HistoryModel {

    @Override
    public Disposable getHome(String uid,String pages,String pageCount,String sign, HistoryContract.HistoryCallback historyCallback) {
        return HistoryNetWorkManager
                .getRequest()
                .getHome("http://iuserspeech.iyuba.cn:9001/credits/getuseractionrecord.jsp?",uid,pages,pageCount,sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HistoryBean>() {
                    @Override
                    public void accept(HistoryBean historyBean) throws Exception {
                        historyCallback.success(historyBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        historyCallback.error((Exception) throwable);
                    }
                });
    }
}
