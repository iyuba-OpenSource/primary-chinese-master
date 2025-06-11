package com.cn.ailanguage.primarychinese.Model;

import android.util.Log;

import com.cn.ailanguage.primarychinese.Bean.AdEntryBean;
import com.cn.ailanguage.primarychinese.View.InitContract;

import java.util.List;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.functions.Consumer;
public class InitModel implements InitContract.InitModel {


    @Override
    public Disposable getAdEntryAll(String appId, int flag, String uid, InitContract.Callback callback) {

        return NetWorkManager
                .getRequestForDev()
                .getAdEntryAll(appId, flag, uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AdEntryBean>>() {
                    @Override
                    public void accept(List<AdEntryBean> adEntryBeans) throws Exception {
                        Log.e("lhz", "AD: " + adEntryBeans.toString() );
                        callback.success(adEntryBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}