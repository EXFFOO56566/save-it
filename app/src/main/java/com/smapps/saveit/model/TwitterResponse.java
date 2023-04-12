package com.smapps.saveit.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TwitterResponse implements Serializable {

    @SerializedName("videos")
    private ArrayList<TwitterResponseModel> videos;

    public ArrayList<TwitterResponseModel> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<TwitterResponseModel> videos) {
        this.videos = videos;
    }
}
