package com.cn.ailanguage.primarychinese.Bean;

import java.util.List;

public class HistoryBean {

    /**
     * result : 200
     * data : [{"voaid":"10120","score":"20","time":"2023-12-04 10:27:12.0","type":"完成听力","srid":"101"},{"voaid":"10110","score":"12","time":"2023-12-04 10:27:12.0","type":"完成听力","srid":"101"},{"voaid":"10100","score":"17","time":"2023-12-04 10:27:12.0","type":"完成听力","srid":"101"},{"voaid":"10090","score":"18","time":"2023-12-04 10:19:42.0","type":"完成听力","srid":"101"},{"voaid":"10070","score":"5","time":"2023-12-04 10:19:41.0","type":"完成听力","srid":"101"},{"voaid":"10060","score":"19","time":"2023-12-04 10:19:41.0","type":"完成听力","srid":"101"},{"voaid":"10050","score":"11","time":"2023-12-04 10:19:41.0","type":"完成听力","srid":"101"},{"voaid":"10040","score":"4","time":"2023-12-04 10:19:13.0","type":"完成听力","srid":"101"},{"voaid":"10030","score":"15","time":"2023-12-04 10:19:13.0","type":"完成听力","srid":"101"},{"voaid":"10020","score":"10","time":"2023-12-04 10:18:58.0","type":"完成听力","srid":"101"},{"voaid":"26487","score":"16","time":"2023-12-01 15:58:57.0","type":"完成阅读","srid":"102"},{"voaid":"26485","score":"4","time":"2023-12-01 14:47:40.0","type":"完成听力","srid":"101"},{"voaid":"15995","score":"11","time":"2023-12-01 14:11:30.0","type":"完成听力","srid":"101"},{"voaid":"26481","score":"7","time":"2023-12-01 13:57:40.0","type":"完成听力","srid":"101"},{"voaid":"15993","score":"5","time":"2023-11-30 16:09:26.0","type":"完成听力","srid":"101"},{"voaid":"13763","score":"5","time":"2023-11-30 16:03:12.0","type":"完成听力","srid":"101"},{"voaid":"13765","score":"17","time":"2023-11-30 16:00:45.0","type":"完成听力","srid":"101"},{"voaid":"15991","score":"14","time":"2023-11-30 15:35:06.0","type":"跟读合成发布","srid":"103"},{"voaid":"313116","score":"13","time":"2023-11-30 14:37:48.0","type":"完成听力","srid":"101"},{"voaid":"313135","score":"17","time":"2023-11-30 14:32:42.0","type":"完成听力","srid":"101"}]
     * message : success
     */

    private int result;
    private String message;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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
         * voaid : 10120
         * score : 20
         * time : 2023-12-04 10:27:12.0
         * type : 完成听力
         * srid : 101
         */

        private String voaid;
        private String score;
        private String time;
        private String type;
        private String srid;

        public String getVoaid() {
            return voaid;
        }

        public void setVoaid(String voaid) {
            this.voaid = voaid;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSrid() {
            return srid;
        }

        public void setSrid(String srid) {
            this.srid = srid;
        }
    }
}
