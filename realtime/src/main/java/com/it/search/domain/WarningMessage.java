package com.it.search.domain;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-31 17:05
 */
public class WarningMessage {
    private String id;
    private String type;
    private String title;
    private String url;
    private String infomessage;
    private String SensitiveWords;

    public WarningMessage() {
    }

    public WarningMessage(String id, String type, String title, String url, String infomessage, String sensitiveWords) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.url = url;
        this.infomessage = infomessage;
        SensitiveWords = sensitiveWords;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "WarningMessage{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", infomessage='" + infomessage + '\'' +
                ", SensitiveWords='" + SensitiveWords + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSensitiveWords() {
        return SensitiveWords;
    }

    public void setSensitiveWords(String sensitiveWords) {
        SensitiveWords = sensitiveWords;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfomessage() {
        return infomessage;
    }

    public void setInfomessage(String infomessage) {
        this.infomessage = infomessage;
    }
}
