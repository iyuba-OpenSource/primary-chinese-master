package com.cn.ailanguage.primarychinese.Activity_and_Fragment;

import java.util.List;

public class Sentence{
    private List<String> chinese;
    private List<String> pinyin;
    public Sentence(List<String> chinese, List<String> pinyin) {
        this.chinese=chinese;
        this.pinyin=pinyin;
    }

    public List<String> getChinsese() {
        return chinese;
    }
    public List<String> getPinyin() {
        return pinyin;
    }

    public void clearAndNullify() {
        if (chinese != null) {
            chinese.clear();
            chinese = null;
        }
        if (pinyin != null) {
            pinyin.clear();
            pinyin = null;
        }
    }

}
