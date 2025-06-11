package com.cn.ailanguage.primarychinese.Model;

import com.cn.ailanguage.primarychinese.Bean.UidBean;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UidModel implements UidContract.UidModel{

    @Override
    public Disposable getHome(String platform, String format, int protocol, int id, int myid, int appid, String sign, UidContract.UidCallback uidCallback) {
        return UidNetWorkManager
                .getRequest()
                .getHome("http://api.iyuba.com.cn/v2/api.iyuba?",platform,format,protocol,id,myid,appid,sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UidBean>() {
                    @Override
                    public void accept(UidBean mainBean) throws Exception {
                        uidCallback.success(mainBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uidCallback.error((Exception) throwable);
                    }
                });

    }

}