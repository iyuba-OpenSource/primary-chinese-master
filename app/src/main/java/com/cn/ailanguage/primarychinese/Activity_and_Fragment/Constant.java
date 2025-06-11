package com.cn.ailanguage.primarychinese.Activity_and_Fragment;

import com.cn.ailanguage.primarychinese.Bean.AllWordBean;
import com.cn.ailanguage.primarychinese.Bean.DataBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;

import java.util.List;

public class Constant {
    public static int  MUSIC_MAX = 0;
    //textFragmnet,顺序播放的时候记录有没有越界
    public static int MUSIC_ING=0;

    public static int flag = 0;


    /**
     * appid
     */
    public final static int APPID = 292;

    /**
     * 简称
     */
    public final static String NAME = "concept";

    public static String DOMAIN = "qomolama.cn";

    public static String DOMAIN_LONG = "qomolama.com.cn";


    public static String API_URL = "http://api." + DOMAIN;
    public static String VIPTIME="0";
    public static Long VIPTIMESMALL= Long.valueOf(0);


    /**
     * 隐私协议的确认状态
     */
    public final static String SP_PRIVACY = "PRIVACY";

    public final static String SP_KEY_PRIVACY_STATE = "PRIVACY_STATE";


    /**
     * 用户协议
     */
    public static String URL_PROTOCOLUSE;


    /**
     * 隐私政策
     */
    public static String URL_PROTOCOLPRI;
    //关于用户的个人信息
    //loginactivity
    public static  String IMAGE;
    public static  String PASSWORD;
    //标识有没有预备登录
    public static String SecVerify;
    public static List<AllWordBean.DataBean> WORDALL;
    //一共选中了多少对的
    public static int RIGHT=0;
    //存一下课文名字，方便后续one，tow，three的标题写名字
    public static String VOAIDNAME;
    //记录一下句子
    public static String SENTENCE="nothing";

    public static String vipStatus;


    public static int version = 2;
    public static String channel = "huawei";

    public static String uid;

    //广告
    public static final String AD_ADS1 = "ads1";//倍孜
    public static final String AD_ADS2 = "ads2";//创见
    public static final String AD_ADS3 = "ads3";//头条穿山甲
    public static final String AD_ADS4 = "ads4";//广点通优量汇
    public static final String AD_ADS5 = "ads5";//快手
    public static final String AD_ADS6 = "ads6";//瑞狮
}
