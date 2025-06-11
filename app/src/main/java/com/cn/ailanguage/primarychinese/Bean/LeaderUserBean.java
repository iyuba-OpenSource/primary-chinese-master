package com.cn.ailanguage.primarychinese.Bean;

import java.util.List;

public class LeaderUserBean {
    /**
     * result : true
     * data : [
     * {"paraid":0,"score":0,"shuoshuotype":4,"againstCount":0,"agreeCount":0,
     * "TopicId":1,"ShuoShuo":"wav5/202401/primarychinese/20240111/17049392764177328.mp3",
     * "id":21009820,"idIndex":0,"CreateDate":"2024-01-11 10:14:40"}]
     * count : 1
     * message : 查询成功
     */

    private boolean result;
    private int count;
    private String message;
    private List<DataBean> data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * paraid : 0
         * score : 0
         * shuoshuotype : 4
         * againstCount : 0
         * agreeCount : 0
         * TopicId : 1
         * ShuoShuo : wav5/202401/primarychinese/20240111/17049392764177328.mp3
         * id : 21009820
         * idIndex : 0
         * CreateDate : 2024-01-11 10:14:40
         */

        private int paraid;
        private int score;
        private int shuoshuotype;
        private int againstCount;
        private int agreeCount;
        private int TopicId;
        private String ShuoShuo;
        private int id;
        private int idIndex;
        private String CreateDate;

        public int getParaid() {
            return paraid;
        }

        public void setParaid(int paraid) {
            this.paraid = paraid;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getShuoshuotype() {
            return shuoshuotype;
        }

        public void setShuoshuotype(int shuoshuotype) {
            this.shuoshuotype = shuoshuotype;
        }

        public int getAgainstCount() {
            return againstCount;
        }

        public void setAgainstCount(int againstCount) {
            this.againstCount = againstCount;
        }

        public int getAgreeCount() {
            return agreeCount;
        }

        public void setAgreeCount(int agreeCount) {
            this.agreeCount = agreeCount;
        }

        public int getTopicId() {
            return TopicId;
        }

        public void setTopicId(int TopicId) {
            this.TopicId = TopicId;
        }

        public String getShuoShuo() {
            return ShuoShuo;
        }

        public void setShuoShuo(String ShuoShuo) {
            this.ShuoShuo = ShuoShuo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdIndex() {
            return idIndex;
        }

        public void setIdIndex(int idIndex) {
            this.idIndex = idIndex;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String CreateDate) {
            this.CreateDate = CreateDate;
        }
    }
}
