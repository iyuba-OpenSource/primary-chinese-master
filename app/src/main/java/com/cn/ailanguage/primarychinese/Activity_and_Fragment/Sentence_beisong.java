package com.cn.ailanguage.primarychinese.Activity_and_Fragment;

import java.util.List;

public class Sentence_beisong{
    private List<String> chinese;
    public Sentence_beisong(List<String> chinese) {
        this.chinese=chinese;
    }

    public List<String> getChinsese() {
        return chinese;
    }

    public void clearAndNullify() {
        if (chinese != null) {
            chinese.clear();
            chinese = null;
        }
    }

}
