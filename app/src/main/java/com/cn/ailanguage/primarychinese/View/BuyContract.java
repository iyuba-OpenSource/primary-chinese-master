package com.cn.ailanguage.primarychinese.View;

import com.cn.ailanguage.primarychinese.Bean.BuyBean;
import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.GetBuyBean;
import com.cn.ailanguage.primarychinese.Model.BaseModel;
import com.cn.ailanguage.primarychinese.presenter.Base.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface BuyContract {
    interface BuyView extends LoadingView {
        void getBuy(BuyBean buyBean);
        void getBackBuy(GetBuyBean getBuyBean);

    }

    interface BuyPresenter extends IBasePresenter<BuyContract.BuyView> {
        void getBuy(int app_id, int userId, String code, String WIDtotal_fee, int amount, int product_id, String WIDbody, String WIDsubject);
        void getBackBuy(String date);
    }

    interface BuyModel extends BaseModel {
        //上传合成
        Disposable getBuy(int app_id, int userId, String code, String WIDtotal_fee, int amount, int product_id, String WIDbody, String WIDsubject, BuyContract.CallBackBuy callBackBuy);

        Disposable getBackBuy(String date,BuyContract.CallBackBackBuy callBackBackBuy);
          }

    interface CallBackBuy {

        void successBuy(BuyBean buyBean);

        void errorBuy(Exception e);

    }
    interface CallBackBackBuy {

        void successBuy(GetBuyBean getBuyBean);

        void errorBuy(Exception e);

    }

}
