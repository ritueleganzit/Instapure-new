package com.eleganzit.instapure.model;

public class ProductInfo {

    String product_id,product_type,media_url,title,date,description;

    public ProductInfo(String product_id, String product_type, String media_url, String title, String date, String description) {
        this.product_id = product_id;
        this.product_type = product_type;
        this.media_url = media_url;
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
