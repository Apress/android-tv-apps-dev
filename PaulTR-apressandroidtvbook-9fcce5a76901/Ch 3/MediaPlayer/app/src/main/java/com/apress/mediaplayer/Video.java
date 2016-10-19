package com.apress.mediaplayer;

import java.io.Serializable;

/**
 * Created by Paul on 10/14/15.
 */
public class Video implements Serializable {

    private String title;
    private String description;
    private String videoUrl;
    private String category;
    private String poster;

    @Override
    public String toString() {
        return "Video {" +
                "title=\'" + title + "\'" +
                ", description=\'" + description + "\'" +
                ", videoUrl=\'" + videoUrl + "\'" +
                ", category=\'" + category + "\'" +
                ", poster=\'" + poster + "\'" +
                "}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
