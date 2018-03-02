package com.example.rory_zhong.eyeview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class Past2 extends AppCompatActivity {

    private ImageView Past2Video5;
    private TextView textView;
    private LinearLayout past2Loading;
    private List<PastBean> myPastList;
    private List<PastBean.DataBeanX.ItemListBean> myList;
    private PastBean pastBean;
    private PastBean.DataBeanX.ItemListBean itemListBean;
    private Past2Adapter adapter;
    private int num=0;
    private ListView lv;
    private ArrayList<HashMap<String,Object>> listItem=new ArrayList<HashMap<String,Object>>();

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                myPastList = PastJsonParse.getItemList((msg.obj).toString());
                pastBean = myPastList.get(num);
                myList=pastBean.getData().getItemList();
                Log.i("获取的数据", pastBean.getData().getHeader().getTitle());
                past2Loading.setVisibility(View.INVISIBLE);
                textView.setText(pastBean.getData().getHeader().getTitle());
                //
                int time=0;
                int minutes=0;
                int seconds=0;
                String minutes_str="";
                String seconds_str="";

                for(int a=0;a<myList.size();a++){
                    itemListBean=myList.get(a);
                    HashMap<String,Object> map=new HashMap<String,Object>();
                    map.put("video_author_picture",itemListBean.getData().getAuthor().getIcon());
                    Log.i("Past2",itemListBean.getData().getAuthor().getIcon());

                    map.put("video_title",itemListBean.getData().getTitle());
                    Log.i("Past2",itemListBean.getData().getTitle());

                    map.put("video_type",itemListBean.getData().getCategory());
                    Log.i("Past2",itemListBean.getData().getCategory());

                    map.put("videoFirstImage",itemListBean.getData().getCover().getFeed());
                    Log.i("Past2",itemListBean.getData().getCover().getFeed());

                    map.put("video_share_num",itemListBean.getData().getConsumption().getShareCount());

                    map.put("video_comment_num",itemListBean.getData().getConsumption().getReplyCount());

                    map.put("video_like_num",itemListBean.getData().getConsumption().getCollectionCount());

                    time = itemListBean.getData().getDuration();//视频时长
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
                adapter=new Past2Adapter(Past2.this,listItem);
                Log.i("测试数组大小",String.valueOf(listItem.size()));
                lv.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past2);

        //改变标题栏的字体
        AssetManager assets = this.getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        past2Loading=(LinearLayout)this.findViewById(R.id.Past2loading);
        textView=(TextView)this.findViewById(R.id.past2Title);
        textView.setTypeface(fromAsset);

        Intent intent=getIntent();
        num=Integer.valueOf(intent.getStringExtra("picId"));
        Log.i("传过来的值",intent.getStringExtra("picId"));
        lv=(ListView)this.findViewById(R.id.past2_listView);
        fillData();

        /////////////////////
    }

    //使用HttpURLConnection访问网络
    private void fillData() {
        new Thread() {
            HttpURLConnection conn = null; //连接对象
            InputStream is = null;
            String resultData = "";

            public void run() {
                try {
                    URL url = new URL(getString((R.string.pastUrl))); //URL对象
                    conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接
                    conn.setDoInput(true); //允许输入流，即允许下载
                    conn.setDoOutput(true); //允许输出流，即允许上传
                    conn.setUseCaches(false); //不使用缓冲
                    conn.setRequestMethod("GET"); //使用get请求
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        is = conn.getInputStream();   //获取输入流，此时才真正建立链接
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader bufferReader = new BufferedReader(isr);
                        String inputLine = "";
                        while ((inputLine = bufferReader.readLine()) != null) {
                            resultData += inputLine + "\n";
                        }

                    }
                    Log.i("得到的json",resultData);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = resultData;
                    handler.sendMessage(msg);

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            }
        }.start();
    }


    /////////////////////////////////////////////////////
    //Past2中listview的适配器
    private class Past2Adapter extends BaseAdapter {
        private LayoutInflater myInflater;      //用来导入布局
        ArrayList<HashMap<String, Object>> myListItem;

        //声明构造函数
        public Past2Adapter(Context context, ArrayList<HashMap<String, Object>> myListItem) {
            this.myInflater = LayoutInflater.from(context);
            this.myListItem = myListItem;
        }

        @Override
        public int getCount() {//返回适配器中的数据条目数
            return myListItem.size();
        }

        @Override
        public Object getItem(int position) {//返回数据集合中与指定索引position对应的数据项
            return myListItem.get(position);
        }

        @Override
        public long getItemId(int position) {//返回指定索引对应的行id
            return position;
        }

        //利用convertView+ViewHolder来重写getView()
        class ViewHolder {
            private ImageView ImageItem;
            private TextView tileItem;
            private TextView timeItem;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder myHolder;

            if (convertView == null) {
                myHolder = new ViewHolder();
                convertView = myInflater.inflate(R.layout.past2_item, null);
                myHolder.ImageItem = (ImageView) convertView.findViewById(R.id.past2videoFirstImage);
                myHolder.tileItem = (TextView) convertView.findViewById(R.id.past2_video_title);
                myHolder.timeItem = (TextView) convertView.findViewById(R.id.past2videoDuration);

                convertView.setTag(myHolder);
            } else {
                myHolder = (ViewHolder) convertView.getTag();
            }
            myHolder.ImageItem.setTag(position);
            myHolder.ImageItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转之前获取用户登录状态，若未登录则不跳转
                    SharedPreferences mySp=Past2.this.getSharedPreferences("data",Context.MODE_PRIVATE);
                    String myUserId=mySp.getString("userId","");
                    //查找当前id的登录状态
                    BmobQuery<myUser> bmobQuery = new BmobQuery<myUser>();
                    bmobQuery.getObject(myUserId, new QueryListener<myUser>() {
                        @Override
                        public void done(myUser myUser, BmobException e) {
                            Boolean loginStatus = false;
                            if (e == null) {
                                Log.i("HomePage", "查询用户登录状态成功");
                                loginStatus = myUser.getIsLogin();
                                if (loginStatus == false) {
                                    Toast.makeText(Past2.this, R.string.notLogin, Toast.LENGTH_SHORT).show();//弹出用户未登录
                                } else {
                                    itemListBean=myList.get(position);
                                    //Toast.makeText(Past2.this,"视频封面的点击事件"+String.valueOf(position),Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(Past2.this,videoDetail.class);
                                    //intent.putExtra("idKey",data.getData().getPlayUrl());
                                    Bundle bundle=new Bundle();
                                    bundle.putCharSequence("the_video_url",itemListBean.getData().getPlayUrl());
                                    bundle.putCharSequence("the_video_title",itemListBean.getData().getTitle());
                                    bundle.putCharSequence("the_video_type",itemListBean.getData().getCategory());
                                    bundle.putCharSequence("theVideoDownload",String.valueOf(Integer.valueOf(itemListBean.getData().getConsumption().getReplyCount())+18));
                                    bundle.putCharSequence("theVideoShare",String.valueOf(itemListBean.getData().getConsumption().getShareCount()));
                                    bundle.putCharSequence("theVideoComment",String.valueOf(itemListBean.getData().getConsumption().getReplyCount()));
                                    bundle.putCharSequence("theVideoLike",String.valueOf(itemListBean.getData().getConsumption().getCollectionCount()));
                                    bundle.putCharSequence("the_author_image",itemListBean.getData().getAuthor().getIcon());
                                    bundle.putCharSequence("videoId",String.valueOf(itemListBean.getData().getId()));
                                    bundle.putCharSequence("videoFirstImage",itemListBean.getData().getCover().getFeed());

                                    SharedPreferences sp=Past2.this.getSharedPreferences("data",Context.MODE_PRIVATE);
                                    String userId=sp.getString("userId","");
                                    String userName=sp.getString("userName","");
                                    bundle.putCharSequence("userId",userId);
                                    bundle.putCharSequence("userName",userName);
                                    Log.i("HomePage用户Id",userId);
                                    Log.i("HomePage用户名",userName);

                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }else {
                                Toast.makeText(Past2.this, R.string.notLogin, Toast.LENGTH_SHORT).show();//弹出用户未登录
                            }
                        }
                    });

                }///////点击事件截止
            });
            ///////////////////
            myHolder.tileItem.setText((String)myListItem.get(position).get("video_title"));
            myHolder.timeItem.setText((String)myListItem.get(position).get("videoDuration"));
            //显示视频封面（网络）
            String videoImageUrl=(String)myListItem.get(position).get("videoFirstImage");
            DisplayImageOptions options2=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading_pic)
                    .showImageOnFail(R.drawable.load_erro_pic)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(15))
                    .build();
            ImageLoader.getInstance().displayImage(videoImageUrl,myHolder.ImageItem,options2);

            return convertView;//返回视图
        }
    }
}
