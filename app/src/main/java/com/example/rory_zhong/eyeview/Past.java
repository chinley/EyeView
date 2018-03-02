package com.example.rory_zhong.eyeview;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Past extends Fragment implements View.OnClickListener{

    private View view;
    private ImageView pic1;
    private ImageView pic2;
    private ImageView pic3;
    private ImageView pic4;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        view = inflater.inflate(R.layout.activity_past,container,false);
        findView();
        textView = (TextView) view.findViewById(R.id.textView_past);
        AssetManager assets = getActivity().getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        textView.setTypeface(fromAsset);

        return view;
    }

    //绑定控件
    private void findView(){

        pic1=(ImageView)view.findViewById(R.id.pastPic1);
        pic1.setOnClickListener(this);
        pic2=(ImageView)view.findViewById(R.id.pastPic2);
        pic2.setOnClickListener(this);
        pic3=(ImageView)view.findViewById(R.id.pastPic3);
        pic3.setOnClickListener(this);
        pic4=(ImageView)view.findViewById(R.id.pastPic4);
        pic4.setOnClickListener(this);

    }

    public void onClick(View view){
        Intent intent1=new Intent(getActivity(),Past2.class);
        switch (view.getId()){
            case R.id.pastPic1:
                intent1.putExtra("picId",String.valueOf(0));
                startActivity(intent1);
                break;
            case R.id.pastPic2:
                intent1.putExtra("picId",String.valueOf(1));
                startActivity(intent1);
                break;
            case R.id.pastPic3:
                intent1.putExtra("picId",String.valueOf(2));
                startActivity(intent1);
                break;
            case R.id.pastPic4:
                intent1.putExtra("picId",String.valueOf(3));
                startActivity(intent1);
                break;

        }

    }


}
