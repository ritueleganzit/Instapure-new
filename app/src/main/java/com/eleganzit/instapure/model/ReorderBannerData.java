package com.eleganzit.instapure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReorderBannerData {
    @SerializedName("banner_id")
    @Expose
    private String bannerId;
    @SerializedName("image_title")
    @Expose
    private String imageTitle;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("description")
    @Expose
    private String description;

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
