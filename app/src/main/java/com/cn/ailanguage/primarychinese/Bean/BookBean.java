package com.cn.ailanguage.primarychinese.Bean;

import java.util.List;

public class BookBean {

    /**
     * result : 1
     * total : 3
     * data : [{"Category":"338","CreateTime":"2023-02-20 02:02:11.0","isVideo":"0","pic":"http://static2.iyuba.cn/images/voaseries/466.jpg","KeyWords":"小学语文, 汉字，拼音, 课文","version":"0","DescCn":"Primary Chinese","SeriesCount":"3","SeriesName":"小学语文一年级上","UpdateTime":"2023-02-20 02:02:20.0","HotFlg":"0","haveMicro":"0","Id":"466"},{"DescCn":"Primary Chinese","Category":"338","SeriesName":"小学语文一年级下","CreateTime":"2023-02-20 02:02:11.0","UpdateTime":"2023-02-20 02:02:20.0","isVideo":"0","HotFlg":"0","haveMicro":"0","Id":"467","pic":"http://static2.iyuba.cn/images/voaseries/467.jpg","KeyWords":"小学语文, 汉字，拼音, 课文","version":"0"},{"DescCn":"Primary Chinese","Category":"338","SeriesName":"小学语文二年级上","CreateTime":"2023-02-20 02:02:11.0","UpdateTime":"2023-02-20 02:02:20.0","isVideo":"0","HotFlg":"0","haveMicro":"0","Id":"468","pic":"http://static2.iyuba.cn/images/voaseries/468.jpg","KeyWords":"小学语文, 汉字，拼音, 课文","version":"0"}]
     */

    private String result;
    private int total;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Category : 338
         * CreateTime : 2023-02-20 02:02:11.0
         * isVideo : 0
         * pic : http://static2.iyuba.cn/images/voaseries/466.jpg
         * KeyWords : 小学语文, 汉字，拼音, 课文
         * version : 0
         * DescCn : Primary Chinese
         * SeriesCount : 3
         * SeriesName : 小学语文一年级上
         * UpdateTime : 2023-02-20 02:02:20.0
         * HotFlg : 0
         * haveMicro : 0
         * Id : 466
         */

        private String Category;
        private String CreateTime;
        private String isVideo;
        private String pic;
        private String KeyWords;
        private String version;
        private String DescCn;
        private String SeriesCount;
        private String SeriesName;
        private String UpdateTime;
        private String HotFlg;
        private String haveMicro;
        private String Id;

        public String getCategory() {
            return Category;
        }

        public void setCategory(String Category) {
            this.Category = Category;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(String isVideo) {
            this.isVideo = isVideo;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getKeyWords() {
            return KeyWords;
        }

        public void setKeyWords(String KeyWords) {
            this.KeyWords = KeyWords;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescCn() {
            return DescCn;
        }

        public void setDescCn(String DescCn) {
            this.DescCn = DescCn;
        }

        public String getSeriesCount() {
            return SeriesCount;
        }

        public void setSeriesCount(String SeriesCount) {
            this.SeriesCount = SeriesCount;
        }

        public String getSeriesName() {
            return SeriesName;
        }

        public void setSeriesName(String SeriesName) {
            this.SeriesName = SeriesName;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }

        public String getHotFlg() {
            return HotFlg;
        }

        public void setHotFlg(String HotFlg) {
            this.HotFlg = HotFlg;
        }

        public String getHaveMicro() {
            return haveMicro;
        }

        public void setHaveMicro(String haveMicro) {
            this.haveMicro = haveMicro;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }
    }
}
