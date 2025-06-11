package com.cn.ailanguage.primarychinese.Bean;
import com.google.gson.annotations.SerializedName;

public class GetBuyBean {

    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}