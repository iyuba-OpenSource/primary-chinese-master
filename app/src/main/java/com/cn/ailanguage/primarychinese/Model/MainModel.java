package com.cn.ailanguage.primarychinese.Model;

import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainModel implements MainContract.MainModel {

    @Override
    public Disposable getHome(String type, String voaid,MainContract.MainCallback mainCallback) {
        return MainNetWorkManager
                .getRequest()
                .getHome("http://iuserspeech.iyuba.cn:9001/japanapi/getChineseInfo.jsp?",type,voaid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MainBean>() {
                    @Override
                    public void accept(MainBean mainBean) throws Exception {
                        mainCallback.success(mainBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mainCallback.error((Exception) throwable);
                    }
                });
    }
}