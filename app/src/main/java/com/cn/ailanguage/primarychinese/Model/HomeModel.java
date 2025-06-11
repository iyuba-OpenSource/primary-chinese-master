package com.cn.ailanguage.primarychinese.Model;

import com.cn.ailanguage.primarychinese.Bean.DataBean;
import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;
import com.cn.ailanguage.primarychinese.View.HomeContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeModel implements HomeContract.HomeModel {

    @Override
    public Disposable getHome(int series, String type, HomeContract.HomeCallback homeCallback) {
        return NetWorkManager
                .getRequest()
                .getHome("http://iuserspeech.iyuba.cn:9001/japanapi/getChineseInfo.jsp?",series,type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataBean>() {
                    @Override
                    public void accept(DataBean dataBean) throws Exception {
                        homeCallback.success(dataBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        homeCallback.error((Exception) throwable);
                    }
                });
    }
    @Override
    public Disposable getWord(String series, HomeContract.WordCallback wordCallback) {
        return NetWorkManager
                .getRequest()
                .getWord("http://iuserspeech.iyuba.cn:9001/japanapi/getChineseWord.jsp?",series)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordBean>() {
                    @Override
                    public void accept(WordBean wordBean) throws Exception {
                        wordCallback.success(wordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        wordCallback.error((Exception) throwable);
                    }
                });
    }
    @Override
    public Disposable getPome(String series, String type,String flag, HomeContract.PomeCallback pomeCallback) {
        return NetWorkManager
                .getRequest()
                .getPome("http://iuserspeech.iyuba.cn:9001/japanapi/getChineseInfo.jsp?",series,type,flag)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PomeBean>() {
                    @Override
                    public void accept(PomeBean pomeBean) throws Exception {
                        pomeCallback.success(pomeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        pomeCallback.error((Exception) throwable);
                    }
                });
    }
}
