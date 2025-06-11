package com.cn.ailanguage.primarychinese.Model;

import com.cn.ailanguage.primarychinese.Bean.AllWordBean;
import com.cn.ailanguage.primarychinese.Bean.BookBean;
import com.cn.ailanguage.primarychinese.View.BookContract;
import com.cn.ailanguage.primarychinese.network.Book.BookNetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BookModel implements BookContract.BookModel{

    @Override
    public Disposable getHome(String type,int category,String sign, BookContract.BookCallback bookCallback) {
        return BookNetWorkManager
                .getRequest()
                .getHome("https://apps.iyuba.cn/iyuba/getTitleBySeries.jsp?",type,category,sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookBean>() {
                    @Override
                    public void accept(BookBean bookBean) throws Exception {
                        bookCallback.success(bookBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        bookCallback.error((Exception) throwable);
                    }
                });
    }
    @Override
    public Disposable getAllWord(String series,String result, BookContract.WordCallback wordCallback) {
        return BookNetWorkManager
                .getRequest()
                .getAllWord("http://iuserspeech.iyuba.cn:9001/japanapi/getChineseWord.jsp?",series,result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllWordBean>() {
                    @Override
                    public void accept(AllWordBean bookBean) throws Exception {
                        wordCallback.success(bookBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        wordCallback.error((Exception) throwable);
                    }
                });
    }
}

