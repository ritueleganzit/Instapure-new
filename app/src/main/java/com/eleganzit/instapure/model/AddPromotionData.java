package com.eleganzit.instapure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPromotionData {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("promo_type")
    @Expose
    private String promoType;
    @SerializedName("feed_id")
    @Expose
    private String feedId;
    @SerializedName("points")
    @Expose
    private Integer points;

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

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
