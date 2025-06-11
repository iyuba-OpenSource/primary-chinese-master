package com.cn.ailanguage.primarychinese.Bean;

import java.util.List;

public class LeaderBean {

    /**
     * result : 7
     * myimgSrc : http://static1.iyuba.cn/uc_server/head/2023/10/9/9/57/12/94b8489c-3e78-4e23-a701-b05d9432c681-m.jpg
     * myid : 14688606
     * myranking : 6
     * data : [
     * {"uid":7215434,"scores":90,"name":"iyuba_O","count":1,"ranking":1,"sort":1,"vip":"21","imgSrc":"http://static1.iyuba.cn/uc_server/head/2023/2/9/14/39/13/620c2f44-d675-48ee-b14a-008aa96bcd71-m.jpg"},
     * {"uid":2888726,"scores":90,"name":"iyuba56","count":1,"ranking":2,"sort":2,"vip":"0","imgSrc":"http://static1.iyuba.cn/uc_server/head/2022/3/2/15/18/7/80a49d4a-0dbd-459c-8d8f-36959b73508b-m.jpg"},
     * {"uid":6288093,"scores":70,"name":"zhutao1015","count":1,"ranking":3,"sort":3,"vip":"21","imgSrc":"http://static1.iyuba.cn/uc_server/head/2023/3/13/19/6/11/4050fb3a-4926-426d-89b3-a346dcdb0d74-m.jpg"},
     * {"uid":9030248,"scores":59,"name":"get诺基亚","count":1,"ranking":4,"sort":4,"vip":"21","imgSrc":"http://static1.iyuba.cn/uc_server/head/2024/0/8/11/38/27/76e2431e-6f69-4839-93f6-3fc61aebbc0d-m.jpg"},
     * {"uid":15075520,"scores":8,"name":"iyuba63777713","count":1,"ranking":5,"sort":5,"vip":"0","imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"},
     * {"uid":14710868,"scores":0,"name":"二者来我撒口","count":1,"ranking":6,"sort":6,"vip":"0","imgSrc":"http://static1.iyuba.cn/uc_server/head/2023/8/18/10/8/29/9839d495-a0f3-4dc0-a7ab-efe7f7d4e5a3-m.jpg"},
     * {"uid":6307010,"scores":0,"name":"178010451621","count":1,"ranking":7,"sort":7,"vip":"21","imgSrc":"http://static1.iyuba.cn/uc_server/head/2024/0/2/18/42/22/fea0ef20-72f6-4e44-8e2e-43850e0660b1-m.jpg"}]
     * myname : 徐文慧的账户
     * myscores : 0
     * mycount : 0
     * vip : 0
     * message : Success
     */

    private int result;
    private String myimgSrc;
    private int myid;
    private int myranking;
    private String myname;
    private int myscores;
    private int mycount;
    private String vip;
    private String message;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMyimgSrc() {
        return myimgSrc;
    }

    public void setMyimgSrc(String myimgSrc) {
        this.myimgSrc = myimgSrc;
    }

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }

    public int getMyranking() {
        return myranking;
    }

    public void setMyranking(int myranking) {
        this.myranking = myranking;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public int getMyscores() {
        return myscores;
    }

    public void setMyscores(int myscores) {
        this.myscores = myscores;
    }

    public int getMycount() {
        return mycount;
    }

    public void setMycount(int mycount) {
        this.mycount = mycount;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
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
         * uid : 7215434
         * scores : 90
         * name : iyuba_O
         * count : 1
         * ranking : 1
         * sort : 1
         * vip : 21
         * imgSrc : http://static1.iyuba.cn/uc_server/head/2023/2/9/14/39/13/620c2f44-d675-48ee-b14a-008aa96bcd71-m.jpg
         */

        private int uid;
        private int scores;
        private String name;
        private int count;
        private int ranking;
        private int sort;
        private String vip;
        private String imgSrc;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }
    }
}
