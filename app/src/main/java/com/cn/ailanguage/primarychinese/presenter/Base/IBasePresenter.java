package com.cn.ailanguage.primarychinese.presenter.Base;




import com.cn.ailanguage.primarychinese.View.BaseView;

import io.reactivex.disposables.Disposable;

public interface IBasePresenter<V extends BaseView> {


    void attchView(V view);

    void detachView();

    void unSubscribe();

    void addSubscribe(Disposable disposable);//Disposable代表一个取消或者添加的订阅。
}
