package com.example.feedapplication.models;

import java.io.Serializable;

public class PostModel  {
    private String post_txt,created,date;
    private int id;
    private boolean isApprovePost;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost_txt() {
        return post_txt;
    }

    public void setPost_txt(String post_txt) {
        this.post_txt = post_txt;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isApprovePost() {
        return isApprovePost;
    }

    public void setApprovePost(boolean approvePost) {
        isApprovePost = approvePost;
    }
}
