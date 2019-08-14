package com.eleganzit.instapure.model;

import java.io.Serializable;

public class PhotosData implements Serializable
{
    String photo_id,photo;

    public PhotosData(String photo_id, String photo) {
        this.photo_id = photo_id;
        this.photo = photo;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
