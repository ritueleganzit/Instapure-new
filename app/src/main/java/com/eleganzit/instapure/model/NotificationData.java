package com.eleganzit.instapure.model;

public class NotificationData {
    String content, img, time;
    boolean isExpanded;

    public NotificationData(String content, String img, String time, boolean isExpanded) {
        this.content = content;
        this.img = img;
        this.time = time;
        this.isExpanded = isExpanded;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
