package com.eleganzit.instapure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryListData {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("package")
    @Expose
    private String _package;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
