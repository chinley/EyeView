package com.example.rory_zhong.eyeview;

import cn.bmob.v3.BmobObject;

/**
 * Created by chinley on 2018/1/1.
 */

public class shareData extends BmobObject {

    private String title;
    private String shareTime;
    private String shareNum;
    private String commentNum;
    private String like;
    private String myIcon;
    private Integer videoId;
    private String type;
    private String downloadNum;
    private String videoFirstImage;
    private String videoUrl;
    private String content;

    private String  username;


    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    public void setShareTime(String shareTime){this.shareTime=shareTime;}
    public String getShareTime(){
        return this.shareTime;
    }

    public void setShareNum(String shareNum){
        this.shareNum = shareNum;
    }
    public String getShareNum(){
        return this.shareNum;
    }

    public void setDownloadNum(String downloadNum){
        this.downloadNum = downloadNum;
    }
    public String getDownloadNum(){
        return this.downloadNum;
    }

    public void setCommentNum(String commentNum){
        this.commentNum = commentNum;
    }
    public String getCommentNum(){
        return this.commentNum;
    }

    public void setLike(String like){
        this.like = like;
    }
    public String getLike(){
        return this.like;
    }

    public void setType(String type){
        this.type = type;
    }
    public String getType(){return this.type;}



    public void setMyIcon(String myIcon){
        this.myIcon = myIcon;
    }
    public String  getMyIcon(){
        return this.myIcon;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String  getUsername(){
        return this.username;
    }

    public void setVideoFirstImage(String videoFirstImage){
        this.videoFirstImage = videoFirstImage;
    }
    public String  getVideoFirstImage(){
        return this.videoFirstImage;
    }

    public void setVideoUrl(String videoUrl){this.videoUrl=videoUrl;};
    public String getVideoUrl(){return this.videoUrl;}

    public void setVideoId(String videoUrl){this.videoId=videoId;};
    public Integer getVideoId(){return this.videoId;}

    public void setContent(String content){this.content = content;}
    public String getContent(){return this.content;}

    public shareData(){}


}

