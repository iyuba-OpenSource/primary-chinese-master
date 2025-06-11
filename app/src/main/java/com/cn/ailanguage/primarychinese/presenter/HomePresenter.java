package com.cn.ailanguage.primarychinese.presenter;

import com.cn.ailanguage.primarychinese.Bean.DataBean;
import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.Model.HomeModel;
import com.cn.ailanguage.primarychinese.View.HomeContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class HomePresenter extends BasePresenter<HomeContract.HomeView,HomeContract.HomeModel> implements HomeContract.HomePresenter{
    @Override
    public void getHome(int series, String type) {
        Disposable disposable = model.getHome(series, type, new HomeContract.HomeCallback() {
            @Override
            public void success(DataBean dataBean) {
                System.out.println("======"+dataBean.getInfo());
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
    public void getWord(String series) {
        Disposable disposable = model.getWord(series, new HomeContract.WordCallback() {
            @Override
            public void success(WordBean wordBean) {
                System.out.println("======"+wordBean.getResult());
                view.getWord(wordBean);
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
    public void getPome(String series, String type,String flag) {
        Disposable disposable = model.getPome(series, type,flag, new HomeContract.PomeCallback() {
            @Override
            public void success(PomeBean pomeBean) {
                System.out.println("======"+pomeBean.getInfo());
                view.getPome(pomeBean);
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
    protected HomeContract.HomeModel initModel() {
        return new HomeModel();
    }

    @Override
    public void attchView(V view) {

    }
}
