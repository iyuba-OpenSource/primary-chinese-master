package com.cn.ailanguage.primarychinese.network.Main;

import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.MainBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constant {

    /**
     * appid
     */
    public final static int APPID = 222;
    public static String oriendboaid ="0"; // 用户首页下拉刷新   最后一个位置得voaid

    /**
     * 简称
     */
    public final static String NAME = "concept";

    public static String DOMAIN = "qomolama.cn";

    public static String DOMAIN_LONG = "qomolama.com.cn";


    public static String API_URL = "http://api." + DOMAIN;


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
    public static int  flag=1;//等于1的时候代表顺序，2的时候循环，3乱序，0默认没有
    public static int COMPETE=0;//0代表播放未完成，1代表播放完成
    public static int SPEED= 10;//播放速度


    //这是原文
    public static float CHINESESIZE= 20.0F;
    public static float PINYINSIZE= 15.0F;
    //原文2
    public static float CHINESENSIZE2= 19.0F;
    public static float PINYINSIZE2= 12.0F;

    //生词
    public static float WORDPINYINSIZE= 15.0F;
    public static float WORDCHINESENSIZE= 35.0F;
    //选中大，中，小的颜色变化
    public static int BIGCOLOR= 0;
    public static int NORMALCOLOR=1;
    public static int SMALLCOLOR= 0;
    //隐藏拼音
    public static int SHOW=1;//1的时候隐藏，0出现
    //判断有没有生词
    public static int WORD=1;
    //判断是不是登录了
    public static int TURN=0;//为一的话需要更新数据

    //用户信息
        //personactivity

    //存放用户名
    public static String USERNAME="nothing";
    //判断有没有删除过
    public  static int DETELE=0;
    public static String INTERAL="0"; //积分
    public static String CORN="0"; //银币
    public static String PACK="0"; //钱包


    //MusicADApter
    public static String MUSIC;
    public static String VOAID;
    public static String TITLE;//储存古诗和诗歌的标题
    //shadowingAdapter
    public static int PICK_OLD=0;
    public static int PICK_ING=1;
    //本机的录音地址
    public static  String ADRESS;
    public static  String ADRESS_END;

    //上传后返回的音频地址
    public static  List<String> BACK_ADRESS;
    //背诵后的音频地址
    public static  List<String> BACK_ADRESS_BEISONG;
    //测评后获得的句子分数
    public static  List<String> SCORE;
    //背诵后的总成绩
    public static  int SCOREBEISONG;
    //存一下背诵的成绩
    public static  List<String> SCORE_BEISONG;
    public static int TOUCH_ING_ITEM=1;
    //记录一下播放到第几句了
    public static int EWHIC=0;
    //判上一次点击了哪个
    public static int ITEMING=-1;
    public static int ITEERECODER=0;
    //每个字，变色，得分
    public static List<List<Float>> WORDCOLOER;
    //背诵后每个字的颜色情况
    public static List<List<Float>> WORDCOLOER_BEISONG;
    public static String COMBINEMUSIC;
    //shadowingadapter
    public static List<Integer> SHOWMIC;
    public static List<Integer> ITEM;
    public static List<Integer> SHOWBEGIN;
    public static List<MainBean.InfoBean> SENTENCE;//保存一下文章列表
    public static List<MainBean.InfoBean> SENTENCE_BEISONG;
    //判断是不是合成，合成后刷新排行榜
    public static int BORE=0;
    //保存古诗的作者和标题,标题就是上面的TITLE，不另外存了
    public static String POMEAUTHOR;//存古诗和诗歌的作者
    public static int WORDFLAG=0;
    public static int UPLOAD=0;//判断有没有上传，如果上传也就是1，那就更新排行榜
    //判断排行榜有没有10个数据了
    public static int ITEMNUME=10;
    public static int SCROLL=0;//判断要不要滚动

    //判断点击了bookline，主要是为了控制土司，防止上滑时这一课还没有完全号
    public  static  int BOOKCLICK=0;
    //begin——mic点击的次数
    public  static  int MIcCLICK=-1;
    //判断一下背诵有没有完成
    public  static  int BEISONG_FINISH=0;
    //wk的
    public static int wkId;
    public static String vipStatus;
    public static String wkPrice;

    public static String wkBody;

    public static boolean canChange = true;


    //广告
    public static final String AD_ADS1 = "ads1";//倍孜
    public static final String AD_ADS2 = "ads2";//创见
    public static final String AD_ADS3 = "ads3";//头条穿山甲
    public static final String AD_ADS4 = "ads4";//广点通优量汇
    public static final String AD_ADS5 = "ads5";//快手
    public static final String AD_ADS6 = "ads6";//瑞狮

}
