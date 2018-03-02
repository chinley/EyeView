package com.example.rory_zhong.eyeview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by rory_zhong on 2017/12/24.
 */
public class HomePage extends Fragment{

    private View view;
    private TextView textView;
    private ListView lv;
    private LinearLayout loading;
    private HomePageAdapter adapter;
    private List<HomePageBean> Datas;
    private HomePageBean data;
    private String userName="unknown";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        view = inflater.inflate(R.layout.activity_home_page,container,false);
        //改变标题栏的字体
        textView = (TextView) view.findViewById(R.id.main_page);
        AssetManager assets = getActivity().getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        textView.setTypeface(fromAsset);

        /////////////////////
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);
        ///////////////////////

        findView();
        fillData();

        return view;
    }

    //绑定控件
    private void findView(){
        lv=(ListView)view.findViewById(R.id.myListView);
        loading=(LinearLayout)view.findViewById(R.id.loading);
        //shareText=(TextView)view.findViewById(R.id.video_share_num);

        //Gson尝试，用于解析json。例子：将String转为json对象(结果：success)
        //Gson gson=new Gson();
        //String  s=gson.toJson("Hello World",String.class);
        //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    //使用AsyncHttpClient访问网络
    private void fillData(){
        //创建AsyncHttpClient实例
        AsyncHttpClient client=new AsyncHttpClient();
        //使用get方式请求
        client.get(getString(R.string.homePageUrl),new AsyncHttpResponseHandler(){
            //请求成功
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                //调用DataJsonParse工具类解析json文件
                try {
                    String json=new String(bytes,"utf-8");
                    Datas= HomePageJsonParse.getItemList(json);
                    if(Datas==null){
                        Toast.makeText(getActivity(),"解析失败",Toast.LENGTH_SHORT).show();
                        Log.e("HomePage:","解析失败");
                    }
                    else {
                        //更新界面
                        loading.setVisibility(View.INVISIBLE);
                        int time=0;
                        int minutes=0;
                        int seconds=0;
                        String minutes_str="";
                        String seconds_str="";
                        ArrayList<HashMap<String,Object>> listItem=new ArrayList<HashMap<String,Object>>();
                        for(int a=0;a<5;a++){//显示5条
                            data=Datas.get(a);
                            HashMap<String,Object> map=new HashMap<String,Object>();
                            map.put("video_author_picture",data.getData().getAuthor().getIcon());
                            Log.i("HomePage:",data.getData().getAuthor().getIcon());

                            map.put("video_title",data.getData().getTitle());
                            Log.i("HomePage:",data.getData().getTitle());

                            map.put("video_type",data.getData().getCategory());
                            Log.i("HomePage:",data.getData().getCategory());

                            map.put("videoFirstImage",data.getData().getCover().getFeed());
                            Log.i("HomePage:",data.getData().getCover().getFeed());

                            map.put("video_share_num",data.getData().getConsumption().getShareCount());
                            Log.i("分享数:",data.getData().getConsumption().getShareCount());

                            map.put("video_comment_num",data.getData().getConsumption().getReplyCount());
                            Log.i("评论数:",data.getData().getConsumption().getReplyCount());

                            map.put("video_like_num",data.getData().getConsumption().getCollectionCount());
                            Log.i("点赞数:",data.getData().getConsumption().getCollectionCount());

                            time = data.getData().getDuration();//视频时长
                            Log.i("视频时长:", time / 60 + ":" + time % 60);
                            minutes = time / 60;
                            seconds = time % 60;
                            if(minutes<10){
                                minutes_str="0"+String.valueOf(minutes);
                            }
                            else
                                minutes_str=String.valueOf(minutes);

                            if(seconds<10){
                                seconds_str="0"+String.valueOf(seconds);
                            }
                            else
                                seconds_str=String.valueOf(seconds);
                            map.put("videoDuration",minutes_str + ":" + seconds_str);
                            Log.i("视频时长:", minutes_str + ":" + seconds_str);

                            listItem.add(map);
                        }
                        adapter=new HomePageAdapter(getActivity(),listItem);
                        lv.setAdapter(adapter);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("HomePage:","解析异常");
                }
            }

            //请求失败
            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getActivity(),R.string.network,Toast.LENGTH_SHORT).show();
                Log.e("HomePage:","网络请求失败");
            }

        });
    }


///////////////////////////////////////////////////////////////////////////////////
    /**
     * Created by Rory_Zhong on 2017/12/19.
     */
//HomePage中listview的适配器
    private class HomePageAdapter extends BaseAdapter {
        private LayoutInflater myInflater;      //用来导入布局
        ArrayList<HashMap<String,Object>> myListItem;

        //声明构造函数
        public HomePageAdapter(Context context, ArrayList<HashMap<String,Object>> myListItem){
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
            private ImageView authorImageItem;
            private TextView tileTextItem;
            private TextView typeTextItem;
            private ImageView videoItem;

            private TextView shareItem;
            private TextView shareNumItem;
            private TextView commentItem;
            private TextView commentNumItem;
            private TextView likeItem;
            private TextView likeNumItem;
            private TextView videoDuration;

        }
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent){
            final ViewHolder myHolder;

            if (convertView==null){
                myHolder=new ViewHolder();
                convertView=myInflater.inflate(R.layout.home_page_item,null);

                myHolder.authorImageItem=(ImageView) convertView.findViewById(R.id.video_author_picture);
                myHolder.tileTextItem=(TextView)convertView.findViewById(R.id.video_title);
                myHolder.typeTextItem=(TextView)convertView.findViewById(R.id.video_type);
                myHolder.videoItem=(ImageView) convertView.findViewById(R.id.videoFirstImage);

                myHolder.shareItem=(TextView)convertView.findViewById(R.id.video_share);
                myHolder.shareNumItem=(TextView)convertView.findViewById(R.id.video_share_num);
                myHolder.commentItem=(TextView)convertView.findViewById(R.id.video_comment);
                myHolder.commentNumItem=(TextView)convertView.findViewById(R.id.video_comment_num);
                myHolder.likeItem=(TextView)convertView.findViewById(R.id.video_like);
                myHolder.likeNumItem=(TextView)convertView.findViewById(R.id.video_like_num);

                myHolder.videoDuration=(TextView)convertView.findViewById(R.id.videoDuration);

                convertView.setTag(myHolder);
            }
            else{
                myHolder=(ViewHolder)convertView.getTag();
            }

            myHolder.videoItem.setTag(position);
            myHolder.videoItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data=Datas.get(position);//获得对应的位置
                    Log.i("position",String.valueOf(position));
                    //Toast.makeText(getActivity(),"点击视频第一帧",Toast.LENGTH_SHORT).show();
                    Log.i("HomePage","点击视频第一帧");
                    try{
                        //跳转之前获取用户登录状态，若未登录则不跳转
                        SharedPreferences mySp=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
                        String myUserId=mySp.getString("userId","");
                        //查找当前id的登录状态
                        BmobQuery<myUser> bmobQuery = new BmobQuery<myUser>();
                        bmobQuery.getObject(myUserId, new QueryListener<myUser>() {
                            @Override
                            public void done(myUser myUser, BmobException e) {
                                Boolean loginStatus=false;
                                if(e==null){
                                    Log.i("HomePage","查询用户登录状态成功");
                                    loginStatus =myUser.getIsLogin();
                                    if(loginStatus==false){
                                        Toast.makeText(getActivity(),R.string.notLogin,Toast.LENGTH_SHORT).show();//弹出用户未登录
                                    }
                                    else{
                                        Intent intent=new Intent(getActivity(),videoDetail.class);
                                        //intent.putExtra("idKey",data.getData().getPlayUrl());
                                        Bundle bundle=new Bundle();
                                        bundle.putCharSequence("the_video_url",data.getData().getPlayUrl());
                                        bundle.putCharSequence("the_video_title",data.getData().getTitle());
                                        bundle.putCharSequence("the_video_type",data.getData().getCategory());
                                        bundle.putCharSequence("theVideoDownload",String.valueOf(Integer.valueOf(data.getData().getConsumption().getReplyCount())+18));
                                        bundle.putCharSequence("theVideoShare",data.getData().getConsumption().getShareCount());
                                        bundle.putCharSequence("theVideoComment",data.getData().getConsumption().getReplyCount());
                                        bundle.putCharSequence("theVideoLike",data.getData().getConsumption().getCollectionCount());
                                        bundle.putCharSequence("the_author_image",data.getData().getAuthor().getIcon());
                                        bundle.putCharSequence("videoId",String.valueOf(data.getData().getId()));
                                        bundle.putCharSequence("videoFirstImage",data.getData().getCover().getFeed());

                                        SharedPreferences sp=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
                                        String userId=sp.getString("userId","");
                                        String userName=sp.getString("userName","");
                                        bundle.putCharSequence("userId",userId);
                                        bundle.putCharSequence("userName",userName);
                                        Log.i("HomePage用户Id",userId);
                                        Log.i("HomePage用户名",userName);

                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        Log.i("HomePage:",data.getData().getPlayUrl());
                                    }
                                }
                                else
                                    Log.i("HomePage","查询用户登录状态异常");
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(getActivity(),R.string.notLogin,Toast.LENGTH_SHORT).show();//弹出用户未登录
                    }


                }
            });

            myHolder.shareItem.setTag(position);
            myHolder.shareItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(),"点击分享",Toast.LENGTH_SHORT).show();
                    Log.i("HomePage:","点击分享");
                    data=Datas.get(position);//获得对应的位置
                    Log.i("position:",String.valueOf(position));
                    String share=myHolder.shareNumItem.getText().toString();
                    int snum=Integer.valueOf(share)+1;

                    myHolder.shareNumItem.setText(String.valueOf(snum));

                    SharedPreferences sp=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
                    String userId=sp.getString("userId","");
                    String userName=sp.getString("userName","");
                    String userImage=sp.getString("userImage","");
                    Log.i("HomePage用户Id",userId);
                    Log.i("HomePage用户名",userName);

                    Intent intent=new Intent(getActivity(),shareDetail.class);
                    //intent.putExtra("idKey",data.getData().getPlayUrl());
                    Bundle bundle=new Bundle();
                    bundle.putCharSequence("the_video_url",data.getData().getPlayUrl());
                    bundle.putCharSequence("the_video_title",data.getData().getTitle());
                    bundle.putCharSequence("the_video_type",data.getData().getCategory());
                    bundle.putCharSequence("theVideoDownload",String.valueOf(Integer.valueOf(data.getData().getConsumption().getReplyCount())+18));
                    bundle.putCharSequence("theVideoShare",data.getData().getConsumption().getShareCount());
                    bundle.putCharSequence("theVideoComment",data.getData().getConsumption().getReplyCount());
                    bundle.putCharSequence("theVideoLike",data.getData().getConsumption().getCollectionCount());
                    bundle.putCharSequence("the_author_image",data.getData().getAuthor().getIcon());
                    bundle.putCharSequence("the_video_image",data.getData().getCover().getFeed());
                    bundle.putCharSequence("videoId",String.valueOf(data.getData().getId()));
                    bundle.putCharSequence("userId",userId);
                    bundle.putCharSequence("userName",userName);
                    Log.i("HomePage用户Id",userId);
                    Log.i("HomePage用户名",userName);

                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });

            myHolder.likeItem.setTag(position);
            myHolder.likeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(),"点击点赞",Toast.LENGTH_SHORT).show();
                    Log.i("HomePage:","点击点赞");
                    String share=myHolder.likeNumItem.getText().toString();
                    int snum=Integer.valueOf(share)+1;
                    myHolder.likeNumItem.setText(String.valueOf(snum));
                }
            });

            myHolder.commentItem.setTag(position);
            myHolder.commentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(),"点击评论",Toast.LENGTH_SHORT).show();
                    Log.i("HomePage:","点击评论");
                    String share=myHolder.commentNumItem.getText().toString();
                    int snum=Integer.valueOf(share)+1;
                    myHolder.commentNumItem.setText(String.valueOf(snum));
                }
            });

            myHolder.tileTextItem.setText((String)myListItem.get(position).get("video_title"));
            myHolder.typeTextItem.setText((String)myListItem.get(position).get("video_type"));

            myHolder.shareNumItem.setText((String)myListItem.get(position).get("video_share_num"));
            myHolder.commentNumItem.setText((String)myListItem.get(position).get("video_comment_num"));
            myHolder.likeNumItem.setText((String)myListItem.get(position).get("video_like_num"));
            myHolder.videoDuration.setText((String)myListItem.get(position).get("videoDuration"));

            //加载作者图片
            String authorImageUrl=(String)myListItem.get(position).get("video_author_picture");
            DisplayImageOptions options=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading_pic)
                    .showImageOnFail(R.drawable.load_erro_pic)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(authorImageUrl,myHolder.authorImageItem,options);
            //myHolder.authorImageItem.setImageResource((Integer)myListItem.get(position).get("video_author_picture"));

            //显示视频封面（网络）
            String videoImageUrl=(String)myListItem.get(position).get("videoFirstImage");
            DisplayImageOptions options2=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading_pic)
                    .showImageOnFail(R.drawable.load_erro_pic)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(videoImageUrl,myHolder.videoItem,options2);
            //myHolder.videoItem.setImageResource((Integer) myListItem.get(position).get("videoFirstImage"));

            //显示控制条
            //MediaController controller = new MediaController(getActivity());
            //myHolder.videoItem.setMediaController(controller);
            //myHolder.videoItem.start();


            return convertView;//返回视图
        }
    }

}
