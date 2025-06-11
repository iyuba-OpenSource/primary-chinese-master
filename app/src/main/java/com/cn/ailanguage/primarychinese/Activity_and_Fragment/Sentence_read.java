package com.cn.ailanguage.primarychinese.Activity_and_Fragment;

import java.util.List;

public class Sentence_read {
    private List<SingleChinese> chinese;
    private List<String> pinyin;
    public Sentence_read(List<SingleChinese> chinese, List<String> pinyin) {
        this.chinese=chinese;
        this.pinyin=pinyin;
    }

    public List<SingleChinese> getChinsese() {
        return chinese;
    }
    public List<String> getPinyin() {
        return pinyin;
    }
// 


}
