package com.cn.ailanguage.primarychinese.presenter;

import android.util.Log;

import com.cn.ailanguage.primarychinese.Bean.AdEntryBean;
import com.cn.ailanguage.primarychinese.Model.InitModel;
import com.cn.ailanguage.primarychinese.View.InitContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class InitPresenter extends BasePresenter<InitContract.InitView, InitContract.InitModel>
        implements InitContract.InitPresenter {


    @Override
    protected InitContract.InitModel initModel() {
        return new InitModel();
    }

    @Override
    public void attchView(V view) {

    }

    @Override
    public void getAdEntryAll(String appId, int flag, String uid) {

        Disposable disposable = model.getAdEntryAll(appId, flag, uid, new InitContract.Callback() {
            @Override
            public void success(List<AdEntryBean> adEntryBeans) {

                Log.e("qxy", "AD  " + adEntryBeans );

                if (adEntryBeans.size() != 0) {

                    AdEntryBean adEntryBean = adEntryBeans.get(0);
                    if (adEntryBean.getResult().equals("1")) {

                        view.getAdEntryAllComplete(adEntryBean);
                    } else if (adEntryBean.getResult().equals("-1")) {

                        view.getAdEntryAllComplete(adEntryBean);
                    }
                }
            }

            @Override
            public void error(Exception e) {

                Log.e("qxy", "AD  " + e );

            }
        });
        addSubscribe(disposable);
    }
}