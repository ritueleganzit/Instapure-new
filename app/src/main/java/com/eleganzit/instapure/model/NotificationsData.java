package com.eleganzit.instapure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsData {
    @SerializedName("notification_id")
    @Expose
    private String notificationId;
    @SerializedName("notification_name")
    @Expose
    private String notificationName;
    @SerializedName("notification_text")
    @Expose
    private String notificationText;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("created_date")
    @Expose
    private String created_date;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
