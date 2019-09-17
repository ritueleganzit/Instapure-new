package com.eleganzit.instapure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReorderData {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("promo_type")
    @Expose
    private String promoType;
    @SerializedName("q2")
    @Expose
    private String q2;
    @SerializedName("q3")
    @Expose
    private String q3;
    @SerializedName("comments")
    @Expose
    private String comments;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    public String getQ2() {
        return q2;
    }

    public void setQ2(String q2) {
        this.q2 = q2;
    }

    public String getQ3() {
        return q3;
    }

    public void setQ3(String q3) {
        this.q3 = q3;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
