package com.cn.ailanguage.primarychinese.Activity_and_Fragment;

public class SingleChinese {
//    words :[ /句子分割后的每个汉字;
//    {
//        content:"我”
//        delete: ""
//        index: "O"
//        insert: "G"
//        pron: "UO3"
//        pron2: "uo3"
//        score: "e.0"//跟读汉字分数
//        substitute_orgi : ""
//        substitute_user: ""
//        user_pron: "G UO3"
//        user_pron2: "g uo3"
//    }
//)]
    private String content,detele,index,insert,pron,pron2,score,substitute_orgi,substitute_user,user_pron,user_pron2;
    public SingleChinese(String a){
        content=a;
        detele="";
        index="0";
        insert="G";
        pron="UO3";
        pron2="uo3";
        score="0";
        substitute_orgi="";
        substitute_user="";
        user_pron="G UO3";
        user_pron2="g uo3";
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetele() {
        return detele;
    }

    public void setDetele(String detele) {
        this.detele = detele;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getPron2() {
        return pron2;
    }

    public void setPron2(String pron2) {
        this.pron2 = pron2;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSubstitute_orgi() {
        return substitute_orgi;
    }

    public void setSubstitute_orgi(String substitute_orgi) {
        this.substitute_orgi = substitute_orgi;
    }

    public String getSubstitute_user() {
        return substitute_user;
    }

    public void setSubstitute_user(String substitute_user) {
        this.substitute_user = substitute_user;
    }

    public String getUser_pron() {
        return user_pron;
    }

    public void setUser_pron(String user_pron) {
        this.user_pron = user_pron;
    }

    public String getUser_pron2() {
        return user_pron2;
    }

    public void setUser_pron2(String user_pron2) {
        this.user_pron2 = user_pron2;
    }


}
