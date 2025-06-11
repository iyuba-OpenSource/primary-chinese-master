package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.DataBean;
import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface HomeContract {

    interface HomeView extends LoadingView {
        //主页书籍
        void getHome(DataBean dataBean);
        //主页生词情况
        void getWord(WordBean wordBean);
        //主页古诗
        void getPome(PomeBean pomeBean);
    }
    interface HomePresenter extends IBasePresenter<HomeView> {
        //主页书籍
        void getHome( int series,String type);
        //主页生词
        void getWord(String series);
        //主页古诗
        void getPome(String series,String type,String flag);
    }
    interface HomeModel extends BaseModel {
        //主页书籍
        Disposable getHome( int series, String type, HomeCallback homeCallback);
        //主页生词
        Disposable getWord(String series,WordCallback wordCallback);
        //主页古诗
        Disposable getPome(String series,String type,String flag,PomeCallback pomeCallback);
    }
    interface HomeCallback {
        void success(DataBean dataBean);
        void error(Exception e);
    }
    interface WordCallback{
        void success(WordBean wordBean);
        void error(Exception e);
    }
    interface PomeCallback{
        void success(PomeBean pomeBean);
        void error(Exception e);
    }


    // add

}



