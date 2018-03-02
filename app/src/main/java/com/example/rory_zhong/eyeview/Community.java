package com.example.rory_zhong.eyeview;

import android.content.Context;
import android.content.Intent;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by chinley on 2018/1/1.
 */

public class Community extends Fragment implements View.OnClickListener{

    private View view;
    private TextView textView;
    private ImageView commuVideo1;
    private ListView listView;
    private LinearLayout loading;

    private Integer videoId;
    private Integer shareNum;
    private shareData getshareData;
    private communityAdapter adapter;
    private ArrayList<HashMap<String,Object>> listItem;
    @Nullable



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        view = inflater.inflate(R.layout.activity_community,container,false);
        textView = (TextView) view.findViewById(R.id.community_page);
        AssetManager assets = getActivity().getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        textView.setTypeface(fromAsset);
        findView();


        listItem = showShare();
        adapter=new communityAdapter(this.getActivity(),listItem);
        listView.setAdapter(adapter);
        refresh();
        return view;
    }

    //绑定控件
    private void findView(){
        listView = (ListView)view.findViewById(R.id.communityView);
        loading=(LinearLayout)view.findViewById(R.id.loading);


    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.commuVideo1:
                startActivity(new Intent(getActivity(),videoDetail.class));

                break;

        }

    }
    private  void refresh(){
        final  ArrayList<HashMap<String,Object>> sharelistItem=new ArrayList<HashMap<String,Object>>();

        //获取对应videoId的评论信息
        BmobQuery<shareData> query=new BmobQuery<shareData>();
        query.addWhereEqualTo("type","share");
        query.setLimit(100);//限制为100条数据
        try {
            query.findObjects(new FindListener<shareData>() {
                @Override
                public void done(List<shareData> list, BmobException e) {
                    if(e==null){

                        shareNum=list.size();//获取总评论数
                        Log.i("分享数：",String.valueOf(shareNum));
                        for(shareData shareData:list){
                            shareData.getMyIcon();
                            shareData.getTitle();
                            shareData.getShareTime();
                            shareData.getVideoFirstImage();
                            shareData.getUsername();
                            shareData.getVideoUrl();
                            shareData.getLike();
                            shareData.getCommentNum();
                            shareData.getContent();
                        }
                        //并通过listView显示
                        for(int a=shareNum-1;a>=0;a--){
                            getshareData=list.get(a);
                            HashMap<String,Object> map=new HashMap<String,Object>();
                            map.put("userimage",getshareData.getMyIcon());

                            map.put("username",getshareData.getUsername());

                            map.put("com_time",getshareData.getShareTime());

                            map.put("commentNum",getshareData.getCommentNum());

                            map.put("title",getshareData.getTitle());

                            map.put("likeNum",getshareData.getLike());

                            map.put("videoPhoto",getshareData.getVideoFirstImage());

                            map.put("content",getshareData.getContent());

                            sharelistItem.add(map);
                            Log.i("刷新后：",getshareData.getContent());
                        }
                        Log.e("返回","sharelistitem数"+sharelistItem.size());

                    }else {
                        Log.e("bmobrefresh","获取分享数据失败"+e.getMessage()+","+e.getErrorCode());
                    }
                    listItem.clear();
                    listItem.addAll(sharelistItem);//未解决传回数组的bug：第一次调用获取后端数据能正常传回，但刷新时不能传回，数组出了done函数就为空了，设置条件处理也出现异常，因此目前只能重复代码在done函数里刷新listView
                    adapter.notifyDataSetChanged();//刷新评论
                }
            });




        }catch (Exception e){
            Log.e("videoDetail:","查询分享数据异常");
        }

    }


    private  ArrayList<HashMap<String,Object>> showShare(){
        final  ArrayList<HashMap<String,Object>> sharelistItem=new ArrayList<HashMap<String,Object>>();

        //获取对应videoId的评论信息
        BmobQuery<shareData> query=new BmobQuery<shareData>();
        query.addWhereEqualTo("type","share");
        query.setLimit(100);//限制为100条数据
        try {
            query.findObjects(new FindListener<shareData>() {
                @Override
                public void done(List<shareData> list, BmobException e) {
                    if(e==null){

                        shareNum=list.size();//获取总评论数
                        Log.i("分享数：",String.valueOf(shareNum));
                        for(shareData shareData:list){
                            shareData.getMyIcon();
                            shareData.getTitle();
                            shareData.getShareTime();
                            shareData.getVideoFirstImage();
                            shareData.getUsername();
                            shareData.getVideoUrl();
                            shareData.getLike();
                            shareData.getCommentNum();
                            shareData.getContent();
                        }
                        //并通过listView显示
                        for(int a=shareNum-1;a>=0;a--){
                            getshareData=list.get(a);
                            HashMap<String,Object> map=new HashMap<String,Object>();
                            map.put("userimage",getshareData.getMyIcon());

                            map.put("username",getshareData.getUsername());

                            map.put("com_time",getshareData.getShareTime());

                            map.put("commentNum",getshareData.getCommentNum());

                            map.put("title",getshareData.getTitle());

                            map.put("likeNum",getshareData.getLike());

                            map.put("videoPhoto",getshareData.getVideoFirstImage());
                            map.put("content",getshareData.getContent());
                            map.put("videoId",getshareData.getContent());

                            sharelistItem.add(map);
                            Log.i("刷新后：",getshareData.getContent());

                        }


                    }else {
                        Log.e("bmobshowShare","获取分享数据失败"+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });


            Log.e("返回","sharelistitem数"+sharelistItem.size());
            return sharelistItem;
        }catch (Exception e){
            Log.e("videoDetail:","查询分享数据异常");
        }

        return sharelistItem;
    }
    private class communityAdapter extends BaseAdapter {
        private LayoutInflater myInflater;      //用来导入布局
        ArrayList<HashMap<String,Object>> myListItem;

        //声明构造函数
        public communityAdapter(Context context, ArrayList<HashMap<String,Object>> myListItem){
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
            private ImageView userimage;
            private TextView username;
            private TextView com_time;
            private TextView commentNum;
            private TextView title;
            private TextView likeNum;
            private ImageView videoImage;
            private TextView contentView;



        }
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent){
            final ViewHolder myHolder;



            if(convertView==null) {
                myHolder = new ViewHolder();
                convertView = myInflater.inflate(R.layout.community_item, null);

                myHolder.userimage = (ImageView) convertView.findViewById(R.id.user_Image);
                myHolder.username = (TextView) convertView.findViewById(R.id.user_Name);
                myHolder.com_time = (TextView) convertView.findViewById(R.id.user_Time);
                myHolder.commentNum = (TextView) convertView.findViewById(R.id.video_comment_num);
                myHolder.likeNum = (TextView) convertView.findViewById(R.id.video_like_num);
                myHolder.videoImage=(ImageView) convertView.findViewById(R.id.commuVideo1);
                myHolder.contentView=(TextView)convertView.findViewById(R.id.content);

                convertView.setTag(myHolder);
            }
            else{
                myHolder=(Community.communityAdapter.ViewHolder)convertView.getTag();
            }

            myHolder.likeNum.setText((String)myListItem.get(position).get("likeNum"));
            myHolder.com_time.setText((String)myListItem.get(position).get("com_time"));
            myHolder.username.setText((String)myListItem.get(position).get("username"));
            myHolder.commentNum.setText((String)myListItem.get(position).get("commentNum"));
            myHolder.contentView.setText((String)myListItem.get(position).get("content"));
            //加载作者图片
            String userImageUrl=(String)myListItem.get(position).get("userimage");
            DisplayImageOptions options=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading_pic)
                    .showImageOnFail(R.drawable.load_erro_pic)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(userImageUrl,myHolder.userimage,options);
            //加载视频图片
            String videoImageUrl2=(String)myListItem.get(position).get("videoPhoto");
            DisplayImageOptions options2=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading_pic)
                    .showImageOnFail(R.drawable.load_erro_pic)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(videoImageUrl2,myHolder.videoImage,options2);



            return convertView;//返回视图
        }


    }


}
