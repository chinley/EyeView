package com.example.rory_zhong.eyeview;

import cn.bmob.v3.BmobObject;

/**
 * Created by rory_zhong on 2017/12/24.
 */

public class CommentBean extends BmobObject{
    private String myName;
    private String myIcon;
    private String commentData;
    private String commentTime;
    private Integer videoId;
    private Integer commentNum;
    private String type;

    public String getMyName(){
        return myName;
    }
    public void setMyName(String myName){
        this.myName=myName;
    }
    public String getMyIcon(){
        return myIcon;
    }
    public void setMyIcon(String myIcon){
        this.myIcon=myIcon;
    }
    public String getCommentData(){
        return commentData;
    }
    public void setCommentData(String commentData){
        this.commentData=commentData;
    }
    public String getCommentTime(){
        return commentTime;
    }
    public void setCommentTime(String commentTime){
        this.commentTime=commentTime;
    }
    public Integer getVideoId(){
        return videoId;
    }
    public void setVideoId(int videoId){
        this.videoId=videoId;
    }
    public Integer getCommentNum(){
        return commentNum;
    }
    public void setCommentNum(int commentNum){
        this.commentNum=commentNum;
    }
    public String getType(){return type;}
    public void setType(String type){
        this.type=type;
    }

}
