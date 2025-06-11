package com.cn.ailanguage.primarychinese.network.Buy;


import com.cn.ailanguage.primarychinese.Bean.BuyBean;
import com.cn.ailanguage.primarychinese.Bean.GetBuyBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface BuyServer {


    //支付宝支付
    @GET
    Observable<BuyBean> getBuy(@Url String url, @Query("app_id") int app_id, @Query("userId") int userId,
                                    @Query("code") String code, @Query("WIDtotal_fee") String WIDtotal_fee, @Query("amount") int amount,
                                    @Query("product_id") int product_id, @Query("WIDbody") String WIDbody, @Query("WIDsubject") String WIDsubject);
    @GET
    Observable<GetBuyBean> getBackBuy(@Url String url, @Query("date") String date);
}
