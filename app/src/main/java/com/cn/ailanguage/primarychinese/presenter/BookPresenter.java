package com.cn.ailanguage.primarychinese.presenter;

import android.util.Log;

import com.cn.ailanguage.primarychinese.Bean.AllWordBean;
import com.cn.ailanguage.primarychinese.Bean.BookBean;
import com.cn.ailanguage.primarychinese.Model.BookModel;
import com.cn.ailanguage.primarychinese.View.BookContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class BookPresenter extends BasePresenter<BookContract.BookView,BookContract.BookModel> implements BookContract.BookPresenter{
    @Override
    public void getHome(String type,int category,String sign) {
        Disposable disposable = model.getHome(type,category,sign,new BookContract.BookCallback() {
            @Override
            public void success(BookBean dataBean) {
                System.out.println("======");
                Log.d("xwh", "success: ");
                view.getHome(dataBean);
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
    public void getAllWord(String series,String result) {
        Disposable disposable = model.getAllWord(series,result,new BookContract.WordCallback() {
            @Override
            public void success(AllWordBean dataBean) {
                System.out.println("======");
                Log.d("xwh", "success: ");
                view.getAllWord(dataBean);
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
    protected BookContract.BookModel initModel() {
        return new BookModel();
    }

    @Override
    public void attchView(V view) {

    }
}
