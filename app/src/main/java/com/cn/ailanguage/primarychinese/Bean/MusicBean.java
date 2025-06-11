package com.cn.ailanguage.primarychinese.Bean;

public class MusicBean {

    @Override
    public String toString() {
        return "MusicBean{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }

    /**
     * result : 1
     * message : merge success
     * URL : wav5/202401/primarychinese/20240108/17047005529288084.mp3
     */


    private String result;
    private String message;
    private String URL;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
