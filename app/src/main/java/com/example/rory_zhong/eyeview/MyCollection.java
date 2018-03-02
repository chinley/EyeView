package com.example.rory_zhong.eyeview;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MyCollection extends AppCompatActivity{

    private TextView textView;
    private ListView lv;
    private List<HomePageBean> Datas;
    private HomePageBean data;
    private MyCollectionAdapter adapter;
    private String userID;//用户ID号 查找后台用户收藏的视频
    private int count;//收藏数目
    private List<String> collId;//收藏的视频ID
    private List<String> collTitle;//收藏的视频标题
    private List<String> collImage;//收藏的视频截屏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        //改变标题栏的字体
        textView = (TextView) this.findViewById(R.id.textView_mycoll);
        AssetManager assets = this.getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        textView.setTypeface(fromAsset);

        collId=new ArrayList<String>();
        collTitle=new ArrayList<String>();
        collImage=new ArrayList<String>();
        lv=(ListView)findViewById(R.id.myCollectionListView);
        count=-1;

        Intent intent=getIntent();
        userID=intent.getStringExtra("userID");//返回用户ID
        BmobQuery<myUser> query = new BmobQuery<myUser>();
        query.getObject(userID, new QueryListener<myUser>() {
            @Override
            public void done(myUser object, BmobException e) {
                if(e==null){
                    count=object.getCount_like();
                    if(count==0) Toast.makeText(MyCollection.this,"用户暂无收藏视频！",Toast.LENGTH_LONG).show();
                    else{
                        for(int i=0;i<count;i++){//获取视频图片，视频标题，视频ID（用于传递至视频页）
                            // Toast.makeText(MyCollection.this,object.getlikeVideoImage().get(i)+count,Toast.LENGTH_SHORT).show();
                            collTitle.add(object.getLikeVideoTitle().get(i));
                            collImage.add(object.getlikeVideoImage().get(i));//图片
                            collId.add(object.getLikeVideoID().get(i));
                            //Toast.makeText(MyCollection.this,collTitle.get(i)+"第几条信息："+i,Toast.LENGTH_SHORT).show();
                        }
                        MyCollectionAdapter adapter=new MyCollectionAdapter();
                        lv.setAdapter(adapter);
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    class MyCollectionAdapter extends BaseAdapter {

        @Override
        public int getCount() {//得到Item的数目
            return count;
        }

        @Override
        public Object getItem(int position) {
            return position-1;//返回条目代表的对象
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(MyCollection.this,R.layout.collection_item,null);
            TextView mTitle=(TextView)view.findViewById(R.id.CollectionVideoTitle);
            mTitle.setText(collTitle.get(position));
            ImageView mImage=(ImageView)view.findViewById(R.id.CollectionVideoImage);
            //显示网络图片
            String videoImageUrl=(String)collImage.get(position);
            DisplayImageOptions options2=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading_pic)
                    .showImageOnFail(R.drawable.load_erro_pic)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(videoImageUrl,mImage);

            return view;
        }
    }

}
