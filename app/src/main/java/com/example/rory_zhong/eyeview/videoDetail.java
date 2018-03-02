package com.example.rory_zhong.eyeview;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static com.example.rory_zhong.eyeview.R.id.com_time;

//io.vov.vitamio.widget.
public class videoDetail extends AppCompatActivity implements View.OnClickListener{

    private TextView textView;
    private JZVideoPlayerStandard myVideo;
    private TextView titleText;
    private TextView typeText;
    private TextView downloadText;
    private TextView shareText;
    private TextView commentText;
    private TextView likeText;
    private ImageView the_author_image;
    private TextView BtLike;

    private ProgressBar videoLoading;
    private String uri;
    private String title;
    private String type;
    private String download;
    private String share;
    private String comment;
    private String like;
    private String image;
    private Integer videoId;

    private EditText editTextInput;//输入评论信息
    private ImageView submitComment;
    private String commentData;
    private CommentBean commentBean;
    private Integer theComNum=0;

    private ListView commentListView;
    private ArrayList<HashMap<String,Object>> listItem;
    private commentAdapter adapter;
    private CommentBean getCommentBean;
    private String userName;
    private String userId;
    private String videoFirstImage;

    //JZVideoPlayerStandard的设置
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        //获取uriS
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//获取传递的数据包
        String uriS=bundle.getString("the_video_url");
        //String uriS="http://baobab.kaiyanapp.com/api/v1/playUrl?vid=66068&editionType=normal&source=aliyun";
        uri=uriS;

        title=bundle.getString("the_video_title");
        type=bundle.getString("the_video_type");
        download=bundle.getString("theVideoDownload");
        share=bundle.getString("theVideoShare");
        comment=bundle.getString("theVideoComment");
        like=bundle.getString("theVideoLike");
        image=bundle.getString("the_author_image");
        String Id=bundle.getString("videoId");
        videoId=Integer.valueOf(Id);
        userId=bundle.getString("userId");//从HomePage得到用户id和用户名
        userName=bundle.getString("userName");
        videoFirstImage=bundle.getString("videoFirstImage");

        findView();

        listItem=Comment();
        adapter=new commentAdapter(this,listItem);
        commentListView.setAdapter(adapter);

        //改变标题栏的字体
        AssetManager assets =this.getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        textView.setTypeface(fromAsset);
    }

    //绑定控件
    private void findView(){

        textView = (TextView) this.findViewById(R.id.video_detail);//绑定标题
        myVideo=(JZVideoPlayerStandard)this.findViewById(R.id.the_video);
        //videoLoading=(ProgressBar)this.findViewById(R.id.video_loading);
        commentListView=(ListView)this.findViewById(R.id.comment_listView);

        titleText=(TextView)this.findViewById(R.id.the_video_title);
        typeText=(TextView)this.findViewById(R.id.the_video_type);
        downloadText=(TextView)this.findViewById(R.id.theVideoDownload);
        shareText=(TextView)this.findViewById(R.id.theVideoShare);
        commentText=(TextView)this.findViewById(R.id.theVideoComment);
        likeText=(TextView)this.findViewById(R.id.theVideoLike);
        the_author_image=(ImageView)this.findViewById(R.id.the_author_image);
        the_author_image.setOnClickListener(this);
        BtLike=(TextView)this.findViewById(R.id.btLike);//点赞按钮
        BtLike.setOnClickListener(this);

        editTextInput=(EditText)this.findViewById(R.id.editTextInput);
        submitComment=(ImageView)this.findViewById(R.id.submitComment);
        submitComment.setOnClickListener(this);

        titleText.setText(title);
        typeText.setText(type);
        downloadText.setText(download);
        shareText.setText(share);
        commentText.setText(comment);
        likeText.setText(like);



        //显示作者图片
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_pic)
                .showImageOnFail(R.drawable.load_erro_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(image,the_author_image,options);

        myVideo.setUp(uri, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,"");
        myVideo.startVideo();



    }

    public void onClick(View view){
        String myTime;
        SimpleDateFormat formatter;
        Date curDate;

        switch (view.getId()){
            case R.id.the_author_image:
                //refresh();测试用
                //Toast.makeText(this,"点击作者头像",Toast.LENGTH_SHORT).show();
                break;
            case R.id.submitComment:
                //获取当前时间
                formatter   =   new   SimpleDateFormat   ("MM月dd日 HH:mm");
                curDate =  new Date(System.currentTimeMillis());
                myTime = formatter.format(curDate);
                commentData=editTextInput.getText().toString();
                if(commentData.equals("")||commentData.equals("")){
                    Toast.makeText(this,"内容为空！",Toast.LENGTH_SHORT).show();
                    break;
                }
                commentBean=new CommentBean();//新建一个评论类
                commentBean.setCommentData(commentData);
                commentBean.setCommentTime(myTime);
                commentBean.setMyIcon(image);//需获取当前用户的头像，暂未实现
                commentBean.setMyName(userName);
                commentBean.setVideoId(videoId);
                commentBean.setType("comment");
                //commentBean.setCommentNum(1);
                commentBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            Log.i("添加数据成功，objectId:",s);
                        }else{
                            Log.e("添加数据失败：",e.getMessage());
                        }
                    }
                });

                editTextInput.setText("");
                Toast.makeText(this,"评论成功",Toast.LENGTH_SHORT).show();
                hidSoftKeyboard(editTextInput);

            break;
            case R.id.btLike:

                try{
                    BmobQuery<myUser> bmobQuery = new BmobQuery<myUser>();
                    bmobQuery.getObject(userId, new QueryListener<myUser>() {
                        @Override
                        public void done(myUser object,BmobException e) {
                            if(e==null){
                                myUser p1 = new myUser();
                                p1.setCount_like(object.getCount_like()+1);
                                //p1.setValue("likeVideoID.0",videoId);
                                p1.addUnique("likeVideoID",videoId.toString());// 添加单个数据
                                p1.addUnique("likeVideoImage", videoFirstImage);
                                p1.addUnique("likeVideoTitle",title);
                                p1.setIsLogin(true);
                                p1.update(userId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(videoDetail.this,"收藏视频成功！",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(videoDetail.this,"收藏视频失败",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{

                            }
                        }
                    });

                }catch(Exception e){Log.e("UserLogin.class:","收藏功能异常");}
                break;
        }


        refresh();//刷新评论

    }

    public void refresh(){
        final ArrayList<HashMap<String,Object>> myListItem=new ArrayList<HashMap<String,Object>>();
        //获取对应videoId的评论信息
        BmobQuery<CommentBean> query=new BmobQuery<CommentBean>();
        query.addWhereEqualTo("videoId",videoId);
        query.setLimit(500);//限制为500条数据
        try {
            query.findObjects(new FindListener<CommentBean>() {
                @Override
                public void done(List<CommentBean> list, BmobException e) {
                    if(e==null){
                        theComNum=list.size();//获取总评论数
                        Log.i("这个视频的评论数：",String.valueOf(theComNum));
                        for(CommentBean commentBean:list){
                            commentBean.getCommentData();
                            commentBean.getCommentTime();
                            commentBean.getMyIcon();
                            commentBean.getMyName();
                        }

                        getCommentBean=new CommentBean();//新建一个评论类
                        for(int a=theComNum-1;a>=0;a--){
                            getCommentBean=list.get(a);
                            HashMap<String,Object> map=new HashMap<String,Object>();
                            map.put("comment_author_image",getCommentBean.getMyIcon());
                            map.put("com_author_name",getCommentBean.getMyName());
                            map.put("com_time",getCommentBean.getCommentTime());
                            map.put("comment",getCommentBean.getCommentData());
                            myListItem.add(map);
                            //Log.i("刷新后：",String.valueOf(myListItem.size()));
                        }

                    }

                    else {
                        Log.e("bmob","获取评论失败"+e.getMessage()+","+e.getErrorCode());
                    }
                    Log.i("后端评论大小",String.valueOf(myListItem.size()));
                    listItem.clear();
                    listItem.addAll(myListItem);//未解决传回数组的bug：第一次调用获取后端数据能正常传回，但刷新时不能传回，数组出了done函数就为空了，因此只能重复代码在done函数里刷新listView
                    adapter.notifyDataSetChanged();//刷新评论
                }
            });

        }catch (Exception e){
            Log.e("videoDetail:","查询评论异常");
        }
    }

    public ArrayList<HashMap<String,Object>> Comment(){
        final ArrayList<HashMap<String,Object>> myListItem=new ArrayList<HashMap<String,Object>>();
        //获取对应videoId的评论信息
        BmobQuery<CommentBean> query=new BmobQuery<CommentBean>();
        query.addWhereEqualTo("videoId",videoId);
        query.setLimit(500);//限制为500条数据
        try {
            query.findObjects(new FindListener<CommentBean>() {
                @Override
                public void done(List<CommentBean> list, BmobException e) {
                    if(e==null){
                        theComNum=list.size();//获取总评论数
                        Log.i("这个视频的评论数：",String.valueOf(theComNum));
                        for(CommentBean commentBean:list){
                            commentBean.getCommentData();
                            commentBean.getCommentTime();
                            commentBean.getMyIcon();
                            commentBean.getMyName();
                        }

                        for(int a=theComNum-1;a>=0;a--){
                            getCommentBean=list.get(a);
                            HashMap<String,Object> map=new HashMap<String,Object>();
                            map.put("comment_author_image",getCommentBean.getMyIcon());
                            map.put("com_author_name",getCommentBean.getMyName());
                            map.put("com_time",getCommentBean.getCommentTime());
                            map.put("comment",getCommentBean.getCommentData());
                            myListItem.add(map);
                        }

                    }else {
                        Log.e("bmob","获取评论失败"+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });

        }catch (Exception e){
            Log.e("videoDetail:","查询评论异常");
        }
        return myListItem;
    }

    private void hidSoftKeyboard(TextView v){
        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm.isActive()){

            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );

        }
    }

    //////////////////////////////////////////////////
    private class commentAdapter extends BaseAdapter{
        private LayoutInflater myInflater;      //用来导入布局
        ArrayList<HashMap<String,Object>> myListItem;

        //声明构造函数
        public commentAdapter(Context context, ArrayList<HashMap<String,Object>> myListItem){
            this.myInflater=LayoutInflater.from(context);
            this.myListItem=myListItem;
        }


        @Override
        public int getCount(){//返回适配器中的数据条目数
            return myListItem.size();
        }

        @Override
        public Object getItem(int position){//返回数据集合中与指定索引position对应的数据项
            return myListItem.get(position);
        }

        @Override
        public long getItemId(int position){//返回指定索引对应的行id
            return position;
        }
        //利用convertView+ViewHolder来重写getView()
        class ViewHolder{
            private ImageView comment_author_image;
            private TextView com_author_name;
            private TextView com_time;
            private TextView comment;
        }
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent){
            final ViewHolder myHolder;

            if(convertView==null) {
                myHolder = new ViewHolder();
                convertView = myInflater.inflate(R.layout.comment_item, null);

                myHolder.comment_author_image = (ImageView) convertView.findViewById(R.id.comment_author_image);
                myHolder.com_author_name = (TextView) convertView.findViewById(R.id.com_author_name);
                myHolder.com_time = (TextView) convertView.findViewById(com_time);
                myHolder.comment = (TextView) convertView.findViewById(R.id.comment);

                convertView.setTag(myHolder);
            }
            else{
                myHolder=(ViewHolder)convertView.getTag();
            }

            myHolder.com_author_name.setText((String)myListItem.get(position).get("com_author_name"));
            myHolder.com_time.setText((String)myListItem.get(position).get("com_time"));
            myHolder.comment.setText((String)myListItem.get(position).get("comment"));
            //加载作者图片
            String authorImageUrl=(String)myListItem.get(position).get("comment_author_image");
            DisplayImageOptions options=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading_pic)
                    .showImageOnFail(R.drawable.load_erro_pic)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(authorImageUrl,myHolder.comment_author_image,options);

            return convertView;//返回视图
        }

    }
}


