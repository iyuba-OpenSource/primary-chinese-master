package com.cn.ailanguage.primarychinese.Bean;

import java.util.List;

public class MainBean {


    /**
     * info : [
     * {"voaid":"1","ParaId":"1","IdIndex":"1","Timing":"2.0","EndTiming":"8.2","Sentence":"天地人","Position":"(165,320),(554,447)","ImgPath":"/202303/1_1.jpg","SentencePhonetic":"tiān dì rén"},
     * {"voaid":"1","ParaId":"2","IdIndex":"1","Timing":"11.2","EndTiming":"17.1","Sentence":"你我他","Position":"(162,492),(559,641)","ImgPath":"/202303/1_1.jpg","SentencePhonetic":"nǐ wǒ tā"}]
     * words : [{"voaid":"1","wordId":"1","word":"天","phonetic":"tiān","sound":"http://static2.iyuba.cn/sounds/word/chinese/1/tian_1.mp3","bookid":"466","updatetime":"2023-12-25 18:57:32.0"},{"voaid":"1","wordId":"2","word":"地","phonetic":"dì","sound":"http://static2.iyuba.cn/sounds/word/chinese/1/di_4.mp3","bookid":"466","updatetime":"2023-12-25 18:57:32.0"},{"voaid":"1","wordId":"3","word":"人","phonetic":"rén","sound":"http://static2.iyuba.cn/sounds/word/chinese/1/ren_2.mp3","bookid":"466","updatetime":"2023-12-25 18:57:32.0"},{"voaid":"1","wordId":"4","word":"你","phonetic":"nǐ","sound":"http://static2.iyuba.cn/sounds/word/chinese/1/ni_3.mp3","bookid":"466","updatetime":"2023-12-25 18:57:32.0"},{"voaid":"1","wordId":"5","word":"我","phonetic":"wǒ","sound":"http://static2.iyuba.cn/sounds/word/chinese/1/wo_3.mp3","bookid":"466","updatetime":"2023-12-25 18:57:32.0"},{"voaid":"1","wordId":"6","word":"他","phonetic":"tā","sound":"http://static2.iyuba.cn/sounds/word/chinese/1/ta_1.mp3","bookid":"466","updatetime":"2023-12-25 18:57:32.0"}]
     * result : 200
     */

    private String result;
    private List<InfoBean> info;
    private List<WordsBean> words;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public List<WordsBean> getWords() {
        return words;
    }

    public void setWords(List<WordsBean> words) {
        this.words = words;
    }

    public static class InfoBean {
        /**
         * voaid : 1
         * ParaId : 1
         * IdIndex : 1
         * Timing : 2.0
         * EndTiming : 8.2
         * Sentence : 天地人
         * Position : (165,320),(554,447)
         * ImgPath : /202303/1_1.jpg
         * SentencePhonetic : tiān dì rén
         */

        private String voaid;
        private String ParaId;
        private String IdIndex;
        private String Timing;
        private String EndTiming;
        private String Sentence;
        private String Position;
        private String ImgPath;
        private String SentencePhonetic;

        public String getVoaid() {
            return voaid;
        }

        public void setVoaid(String voaid) {
            this.voaid = voaid;
        }

        public String getParaId() {
            return ParaId;
        }

        public void setParaId(String ParaId) {
            this.ParaId = ParaId;
        }

        public String getIdIndex() {
            return IdIndex;
        }

        public void setIdIndex(String IdIndex) {
            this.IdIndex = IdIndex;
        }

        public String getTiming() {
            return Timing;
        }

        public void setTiming(String Timing) {
            this.Timing = Timing;
        }

        public String getEndTiming() {
            return EndTiming;
        }

        public void setEndTiming(String EndTiming) {
            this.EndTiming = EndTiming;
        }

        public String getSentence() {
            return Sentence;
        }

        public void setSentence(String Sentence) {
            this.Sentence = Sentence;
        }

        public String getPosition() {
            return Position;
        }

        public void setPosition(String Position) {
            this.Position = Position;
        }

        public String getImgPath() {
            return ImgPath;
        }

        public void setImgPath(String ImgPath) {
            this.ImgPath = ImgPath;
        }

        public String getSentencePhonetic() {
            return SentencePhonetic;
        }

        public void setSentencePhonetic(String SentencePhonetic) {
            this.SentencePhonetic = SentencePhonetic;
        }
    }

    public static class WordsBean {
        /**
         * voaid : 1
         * wordId : 1
         * word : 天
         * phonetic : tiān
         * sound : http://static2.iyuba.cn/sounds/word/chinese/1/tian_1.mp3
         * bookid : 466
         * updatetime : 2023-12-25 18:57:32.0
         */

        private String voaid;
        private String wordId;
        private String word;
        private String phonetic;
        private String sound;
        private String bookid;
        private String updatetime;

        public String getVoaid() {
            return voaid;
        }

        public void setVoaid(String voaid) {
            this.voaid = voaid;
        }

        public String getWordId() {
            return wordId;
        }

        public void setWordId(String wordId) {
            this.wordId = wordId;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getBookid() {
            return bookid;
        }

        public void setBookid(String bookid) {
            this.bookid = bookid;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }
    }
}