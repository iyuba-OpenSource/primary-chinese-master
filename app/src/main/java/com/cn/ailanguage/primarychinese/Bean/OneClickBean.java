package com.cn.ailanguage.primarychinese.Bean;

import com.google.gson.annotations.SerializedName;

public class OneClickBean {

    private int isLogin;
    private ResDTO res;
    private UserinfoDTO userinfo;

    @Override
    public String toString() {
        return "OneClickBean{" +
                "isLogin=" + isLogin +
                ", res=" + res +
                ", userinfo=" + userinfo +
                '}';
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }

    public ResDTO getRes() {
        return res;
    }

    public void setRes(ResDTO res) {
        this.res = res;
    }

    public UserinfoDTO getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoDTO userinfo) {
        this.userinfo = userinfo;
    }

    public static class ResDTO {
        @Override
        public String toString() {
            return "ResDTO{" +
                    "isValid=" + isValid +
                    ", phone='" + phone + '\'' +
                    ", valid=" + valid +
                    '}';
        }

        private int isValid;
        private String phone;
        private Boolean valid;

        public int getIsValid() {
            return isValid;
        }

        public void setIsValid(int isValid) {
            this.isValid = isValid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Boolean getValid() {
            return valid;
        }

        public void setValid(Boolean valid) {
            this.valid = valid;
        }
    }

    public static class UserinfoDTO {
        @SerializedName("Amount")
        private int amount;
        private int credits;
        private String email;
        private int expireTime;
        private String imgSrc;
        private int isteacher;
        private int jiFen;
        private String message;
        private String mobile;
        private int money;
        private String nickname;
        private String result;
        private String uid;
        private String username;
        private int vipStatus;

        @Override
        public String toString() {
            return "UserinfoDTO{" +
                    "amount=" + amount +
                    ", credits=" + credits +
                    ", email='" + email + '\'' +
                    ", expireTime=" + expireTime +
                    ", imgSrc='" + imgSrc + '\'' +
                    ", isteacher=" + isteacher +
                    ", jiFen=" + jiFen +
                    ", message='" + message + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", money=" + money +
                    ", nickname='" + nickname + '\'' +
                    ", result='" + result + '\'' +
                    ", uid='" + uid + '\'' +
                    ", username='" + username + '\'' +
                    ", vipStatus=" + vipStatus +
                    '}';
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getCredits() {
            return credits;
        }

        public void setCredits(int credits) {
            this.credits = credits;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(int expireTime) {
            this.expireTime = expireTime;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public int getIsteacher() {
            return isteacher;
        }

        public void setIsteacher(int isteacher) {
            this.isteacher = isteacher;
        }

        public int getJiFen() {
            return jiFen;
        }

        public void setJiFen(int jiFen) {
            this.jiFen = jiFen;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getVipStatus() {
            return vipStatus;
        }

        public void setVipStatus(int vipStatus) {
            this.vipStatus = vipStatus;
        }
    }
}
