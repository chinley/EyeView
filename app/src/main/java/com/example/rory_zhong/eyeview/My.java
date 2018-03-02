package com.example.rory_zhong.eyeview;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class My extends Fragment implements View.OnClickListener{


    //本界面有5个控件
    private ImageView mIvuserpicture;//头像
    private TextView mTvusername;//用户名
    private ImageView mIvcollection;
    private ImageView mIvlogout;
    private View view;
    private TextView menu;
    private String name;


    myUser currentUser=new myUser();
    boolean userloading;//用户是否登录

    private FragmentInteraction listener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentInteraction)
        {
            listener = (FragmentInteraction)context;
        }
        else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }

    }
    public interface FragmentInteraction {
        void process(String str);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        view = inflater.inflate(R.layout.activity_my,container,false);
        //初始化界面：
        findView();//绑定控件
        userloading=false;//用户默认未登录
        mTvusername.setText(R.string.please_login);
        mIvlogout.setVisibility(View.INVISIBLE);//退出按钮开始不可见

        //改变标题栏的字体
        menu = (TextView) view.findViewById(R.id.textView_my);
        AssetManager assets = getActivity().getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        menu.setTypeface(fromAsset);

        return view;
    }
    //绑定控件
    private void findView(){
        //用户名
        mTvusername=(TextView)view.findViewById(R.id.tvUsername);
        //头像
        mIvuserpicture=(ImageView)view.findViewById(R.id.userPicture);
        mIvuserpicture.setOnClickListener(this);
        //我的收藏
        mIvcollection=(ImageView)view.findViewById(R.id.Ivcollection);
        mIvcollection.setOnClickListener(this);
        //退出按钮
        mIvlogout=(ImageView)view.findViewById(R.id.Ivlogout);
        mIvlogout.setOnClickListener(this);
    }

    public void onClick(View view){

        switch (view.getId()){
            case R.id.userPicture:
                if(userloading==false){
                    Intent intent1=new Intent(getActivity(),UserLogin.class);
                    startActivityForResult(intent1,1);//若登陆成功 要求返回用户名 以用户名为依据定位用户
                }
                else{//换头像
                    showChoosePicDialog();
                }
                break;
            case R.id.Ivcollection:
                if(userloading==false){//未登录时
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                }
                else{//已登录
                    Intent collectionIntent = new Intent(getActivity(),MyCollection.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userID",currentUser.getObjectId());
                    collectionIntent.putExtras(bundle);
                    getActivity().startActivity(collectionIntent);
                }
                break;
            case R.id.Ivlogout:
                currentUser.setIsLogin(false);
                userloading=false;
                mTvusername.setText("请登录");
                mIvlogout.setVisibility(View.INVISIBLE);//退出按钮不可见
                Toast.makeText(getActivity(),"用户已退出登录",Toast.LENGTH_LONG).show();
                //联网刷新登录状态，从sharePreferences中获取用户id，将用户信息更新
                SharedPreferences sp=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
                String userId=sp.getString("userId","");
                //更新登录状态
                myUser u=new myUser();
                u.setIsLogin(false);
                u.update(userId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.i("My","退出登录状态成功");
                        } else {
                            Log.e("My","退出登录状态失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                break;
        }
    }

    //从本地相册获取图片
    private void getPicFromLocal() {
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, 2);
    }

    //弹窗选择设置图像
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 选择本地照片
                        getPicFromLocal();
                        break;
                }
            }
        });
        builder.create().show();
    }



    //登录数据回传适配器
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if(data!=null){
            if(requestCode==1){
                if(resultCode==1){//登录和注册返回id和用户名
                    mTvusername.setText(data.getStringExtra("userName"));
                    currentUser.setObjectId(data.getStringExtra("userId"));
                    //HomePage homePage=HomePage.newInstance(data.getStringExtra("userinfo"));
                    userloading=true;
                    mIvlogout.setVisibility(View.VISIBLE);//退出按钮可见
                    //listener.process(data.getStringExtra("userinfo"));//这个可以不使用，已改用SharedPreferences
                    SharedPreferences sp=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("userId",data.getStringExtra("userId"));//获取SharedPreferences的id，在homePage的视频封面点击事件中
                    editor.putString("userName",data.getStringExtra("userName"));
                    editor.commit();
                }

            }
            if (requestCode == 2) {//头像从相册获取后设置头像
                String picturePath="";
                if (data != null) {
                    Uri uri = data.getData();

                    ContentResolver cr = this.getContext().getContentResolver();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        mIvuserpicture.setImageBitmap(bitmap);//显示图片
                        //获取图片路径
                        final Uri selectedImage = data.getData();
                        picturePath = selectedImage.getPath();
                        picturePath=picturePath.substring(5,picturePath.length());
                        Log.i("图片路径",picturePath);
                        //实现上传图片至bmob

                        final BmobFile bmobFile = new BmobFile(new File(picturePath));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                    Log.i("上传文件成功:" ,bmobFile.getFileUrl());
                                }else{
                                    Log.e("上传文件失败：",e.getMessage());
                                }
                            }
                            @Override
                            public void onProgress(Integer value) {
                                // 返回的上传进度（百分比）
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                }
            }
        }
    }
}
