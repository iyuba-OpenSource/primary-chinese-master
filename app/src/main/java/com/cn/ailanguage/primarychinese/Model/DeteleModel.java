package com.cn.ailanguage.primarychinese.Model;

import com.cn.ailanguage.primarychinese.Bean.DeteleBean;
import com.cn.ailanguage.primarychinese.View.DeteleContract;
import com.cn.ailanguage.primarychinese.network.Detele.DeteleNetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DeteleModel implements DeteleContract.DeteleModel{

    @Override
    public Disposable getHome(int protocol, String username, String password, String format, String sign, DeteleContract.DeteleCallback deteleCallback) {
        return DeteleNetWorkManager
                .getRequest()
                .getHome("http://api.iyuba.com.cn/v2/api.iyuba?",protocol,username,password,format,sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DeteleBean>() {
                    @Override
                    public void accept(DeteleBean deteleBean) throws Exception {
                        deteleCallback.success(deteleBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        deteleCallback.error((Exception) throwable);
                    }
                });
    }
}

