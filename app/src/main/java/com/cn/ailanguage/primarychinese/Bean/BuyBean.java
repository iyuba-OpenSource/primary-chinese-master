package com.cn.ailanguage.primarychinese.Bean;
import com.google.gson.annotations.SerializedName;
public class BuyBean {

    @Override
    public String toString() {
        return "VipParseBean{" +
                "alipayTradeStr='" + alipayTradeStr + '\'' +
                ", result='" + result + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @SerializedName("alipayTradeStr")
    private String alipayTradeStr;
    @SerializedName("result")
    private String result;
    @SerializedName("message")
    private String message;

    public String getAlipayTradeStr() {
        return alipayTradeStr;
    }

    public void setAlipayTradeStr(String alipayTradeStr) {
        this.alipayTradeStr = alipayTradeStr;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
