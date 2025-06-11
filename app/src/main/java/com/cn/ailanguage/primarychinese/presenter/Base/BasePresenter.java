package com.cn.ailanguage.primarychinese.presenter.Base;





import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.View.BaseView;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;
import com.umeng.vt.diff.V;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends BaseView, M extends BaseModel> implements IBasePresenter<V> {

    protected V view;
    protected M model;
    protected CompositeDisposable compositeDisposable;
//这里的compositeDisposable指的就是管理presenter中订阅对象的容器
    public BasePresenter() {
        this.model = initModel();
    }

    protected abstract M initModel();
//initModel作为一个抽象方法，并将model初始化
    public void attchView(V view) {

        WeakReference<V> viewWeakReference = new WeakReference<>(view);
        this.view = viewWeakReference.get();
    }//创建对view对象的弱引用，然后用get方法赋给this。view


    public void detachView() {

        unSubscribe();
    }

    public void unSubscribe() {

        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    public void addSubscribe(Disposable disposable) {

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public abstract void attchView(com.umeng.vt.diff.V view);
}
