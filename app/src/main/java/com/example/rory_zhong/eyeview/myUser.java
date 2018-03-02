package com.example.rory_zhong.eyeview;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class myUser extends BmobObject {
    private String name;
    private String password;
    private boolean isLogin;
    private int count_like;
    private List<String> likeVideoID;
    private List<String> likeVideoImage;
    private List<String> likeVideoTitle;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getIsLogin(){return isLogin;}
    public void setIsLogin(boolean isLogin){this.isLogin=isLogin;}
    public int getCount_like() {
        return count_like;
    }
    public void setCount_like(int count_like) {
        this.count_like = count_like;
    }

    public List<String> getLikeVideoID() {
        return likeVideoID;
    }

    public void setLikeVideoID(List<String> likeVideoID) {
        this.likeVideoID = likeVideoID;
    }

    public List<String> getlikeVideoImage() {
        return likeVideoImage;
    }

    public void setlikeVideoImage(List<String> likeVideoImage) {
        likeVideoImage = likeVideoImage;
    }

    public List<String> getLikeVideoTitle() {
        return likeVideoTitle;
    }

    public void setLikeVideoTitle(List<String> likeVideoTitle) {
        this.likeVideoTitle = likeVideoTitle;
    }
}
