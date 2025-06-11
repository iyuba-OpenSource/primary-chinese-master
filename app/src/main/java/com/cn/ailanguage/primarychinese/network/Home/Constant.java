package com.cn.ailanguage.primarychinese.network.Home;

import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;

import java.util.List;

public class Constant {

    /**
     * appid
     */
    public final static int APPID = 222;

    /**
     * 简称
     */
    public final static String NAME = "concept";

    public static String DOMAIN = "qomolama.cn";

    public static String DOMAIN_LONG = "qomolama.com.cn";


    public static String API_URL = "http://api." + DOMAIN;
    public static String URL_DEV = "http://dev." + DOMAIN;

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
    //用于确定是不是古诗，方便居中和分段
    public static int POEM;
    public static String VOAID; //获取文章的voaid
    public static List<WordBean.DataBean.WordsBean> WORDBEAN;
    public static int LIVEINGWORD=0;//判断有没有生词
    //证明换过书了changbookhome是主页的，changbook是生词的,cahngebookpome是古诗的
    public static int CHANGEBOOKHOME=0;
    public static int CHANGEBOOK=0;//1是代表书籍改变了，0是没改变书本
    public static int CHANGEBOOKPOME=0; //1是代表书籍改变了，0是没改变书本

}
