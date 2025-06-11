package com.cn.ailanguage.primarychinese.presenter;

import com.cn.ailanguage.primarychinese.Bean.BuyBean;
import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.GetBuyBean;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Model.BuyModel;
import com.cn.ailanguage.primarychinese.Model.MainModel;
import com.cn.ailanguage.primarychinese.View.BuyContract;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.presenter.Base.BasePresenter;
import com.umeng.vt.diff.V;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class BuyPresenter extends BasePresenter<BuyContract.BuyView,BuyContract.BuyModel> implements BuyContract.BuyPresenter{
    @Override
    public void getBuy(int app_id, int userId, String code, String WIDtotal_fee, int amount, int product_id, String WIDbody, String WIDsubject) {
        Disposable disposable = model.getBuy(app_id,userId,code,WIDtotal_fee,amount,product_id,WIDbody,WIDsubject, new BuyContract.CallBackBuy() {

            @Override
            public void successBuy(BuyBean buyBean) {
                System.out.println("======1"+buyBean.getMessage());
                view.getBuy(buyBean);
            }

            @Override
            public void errorBuy(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
    @Override
    public void getBackBuy(String date) {
        Disposable disposable = model.getBackBuy(date, new BuyContract.CallBackBackBuy() {

            @Override
            public void successBuy(GetBuyBean getBuyBean) {
                System.out.println("======1"+getBuyBean.getCode());
                view.getBackBuy(getBuyBean);
            }

            @Override
            public void errorBuy(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    protected BuyContract.BuyModel initModel() {
        return new BuyModel();
    }

    @Override
    public void attchView(V view) {

    }
}
