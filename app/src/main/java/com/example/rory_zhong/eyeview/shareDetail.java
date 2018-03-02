package com.example.rory_zhong.eyeview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chinley on 2018/1/1.
 */
public class shareDetail extends AppCompatActivity implements View.OnClickListener{
    private EditText shareContent;
    private ImageView userimage;
    private ImageView videoimage;
    private shareData shareData;
    private TextView username;
    private TextView usertime;

    private Uri uri;
    private String title;
    private String type;
    private String download;
    private String share;
    private String comment;
    private String like;
    private String image;
    private Integer videoId;
    private String userName;
    private String userId;
    private String videoImage;
    private String uriS;

    String myTime;
    SimpleDateFormat formatter;
    Date curDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//获取传递的数据包
        String uriS=bundle.getString("the_video_url");
        //String uriS="http://baobab.kaiyanapp.com/api/v1/playUrl?vid=66068&editionType=normal&source=aliyun";
        uri= Uri.parse(uriS);

        title=bundle.getString("the_video_title");
        type=bundle.getString("the_video_type");
        download=bundle.getString("theVideoDownload");
        share=bundle.getString("theVideoShare");
        comment=bundle.getString("theVideoComment");
        like=bundle.getString("theVideoLike");
        image=bundle.getString("the_author_image");
        Log.e("返回","视频图像"+ image);

        String Id=bundle.getString("videoId");
        videoId=Integer.valueOf(Id);
        userId=bundle.getString("userId");//从HomePage得到用户id和用户名
        userName=bundle.getString("userName");
        videoImage=bundle.getString("the_video_image");
        Log.e("返回","视频图像"+ videoImage);


        formatter   =   new   SimpleDateFormat   ("MM月dd日 HH:mm");
        curDate =  new Date(System.currentTimeMillis());
        myTime = formatter.format(curDate);
        findView();
    }

    private void findView(){
        shareContent=(EditText)findViewById(R.id.shareContent);
        userimage = (ImageView)findViewById(R.id.user_Image);
        username = (TextView)findViewById(R.id.user_Name);
        usertime = (TextView)findViewById(R.id.user_Time);
        videoimage = (ImageView)findViewById(R.id.video_image);



        username.setText(userName);
        usertime.setText(myTime);

        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_pic)
                .showImageOnFail(R.drawable.load_erro_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(videoImage,videoimage,options);

        DisplayImageOptions options2=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_pic)
                .showImageOnFail(R.drawable.load_erro_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(image,userimage,options2);




    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.share:
                try{
                    String content="";
                    content=shareContent.getText().toString();

                    shareData sharedata = new shareData();
                    sharedata.setTitle(title);
                    sharedata.setLike(like);
                    sharedata.setShareNum(share);
                    sharedata.setCommentNum(comment);
                    sharedata.setMyIcon(image);
                    sharedata.setContent(content);
                    sharedata.setUsername(userName);
                    sharedata.setType("share");
                    sharedata.setShareTime(myTime);
                    sharedata.setDownloadNum(download);
                    sharedata.setVideoFirstImage(videoImage);
                    sharedata.setVideoUrl(uriS);
                    sharedata.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){
                                Log.i("添加数据成功，objectId:",objectId);
                            }else{
                                Log.e("添加数据失败：",e.getMessage());
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("ShareDetail.class:","添加分享数据异常");
                }
                /*Intent intent2 = new Intent(this,MainActivity.class);
                intent2.putExtra("id",1);
                startActivity(intent2);*/
                Toast.makeText(this,"分享成功",Toast.LENGTH_SHORT).show();
                finish();



                break;

        }


    }

}

