package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.AdEntryBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface InitContract {
    interface InitView extends LoadingView {

        void getAdEntryAllComplete(AdEntryBean adEntryBean);
    }


    interface InitPresenter extends IBasePresenter<InitView> {

        void getAdEntryAll(String appId, int flag, String uid);
    }


    interface InitModel extends BaseModel {

        Disposable getAdEntryAll(String appId, int flag, String uid, Callback callback);
    }

    interface Callback {

        void success(List<AdEntryBean> adEntryBeans);

        void error(Exception e);
    }
}
