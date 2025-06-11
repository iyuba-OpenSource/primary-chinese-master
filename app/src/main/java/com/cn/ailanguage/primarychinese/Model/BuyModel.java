package com.cn.ailanguage.primarychinese.Model;

import android.util.Log;

import com.cn.ailanguage.primarychinese.Bean.BuyBean;
import com.cn.ailanguage.primarychinese.Bean.GetBuyBean;
import com.cn.ailanguage.primarychinese.Bean.HistoryBean;
import com.cn.ailanguage.primarychinese.View.BuyContract;
import com.cn.ailanguage.primarychinese.View.HistoryContract;
import com.cn.ailanguage.primarychinese.network.Buy.BuyNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Buy.BuyServer;
import com.cn.ailanguage.primarychinese.network.History.HistoryNetWorkManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BuyModel implements BuyContract.BuyModel {



    @Override
    public Disposable getBuy(int app_id, int userId, String code, String WIDtotal_fee, int amount, int product_id, String WIDbody, String WIDsubject, BuyContract.CallBackBuy callBackBuy) {
        return BuyNetWorkManager
                .getRequest()
                .getBuy("http://vip.iyuba.cn/alipay.jsp",app_id,userId,code,WIDtotal_fee,amount,product_id,WIDbody,WIDsubject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyBean>() {
                    @Override
                    public void accept(BuyBean buyBean) throws Exception {
                        callBackBuy.successBuy(buyBean);
                        Log.d("xwh00-", buyBean.getMessage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBackBuy.errorBuy((Exception) throwable);
                    }
                });
    }
    @Override
    public Disposable getBackBuy(String data, BuyContract.CallBackBackBuy callBackBackBuy) {
        return BuyNetWorkManager
                .getRequest()
                .getBackBuy("http://vip.iyuba.cn/notifyAliNew.jsp",data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetBuyBean>() {
                    @Override
                    public void accept(GetBuyBean getBuyBean) throws Exception {
                        callBackBackBuy.successBuy(getBuyBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBackBackBuy.errorBuy((Exception) throwable);
                    }
                });
    }
}
