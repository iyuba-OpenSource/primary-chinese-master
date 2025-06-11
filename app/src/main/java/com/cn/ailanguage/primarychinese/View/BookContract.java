package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.AllWordBean;
import com.cn.ailanguage.primarychinese.Bean.BookBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface BookContract {
    interface BookView extends LoadingView {
        //选书
        void getHome(BookBean bookBean);
        //整本书的生词
        void getAllWord(AllWordBean allWordBean);
    }

    interface BookPresenter extends IBasePresenter<BookView> {

        void getHome(String type,int category,String sign);
        void getAllWord(String series,String result);
    }

    interface BookModel extends BaseModel {

        Disposable getHome(String type,int category,String sign,BookContract.BookCallback bookCallback);
        Disposable getAllWord(String series,String result,BookContract.WordCallback wordCallback);
    }

    interface BookCallback {

        void success(BookBean bookBean);

        void error(Exception e);
    }
    interface WordCallback {

        void success(AllWordBean bookBean);

        void error(Exception e);
    }
}




