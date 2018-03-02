package com.example.rory_zhong.eyeview;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDownload extends AppCompatActivity implements View.OnClickListener{

    private ImageView downVideo1;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_download);
        findView();

        //改变标题栏的字体
        AssetManager assets =this.getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        textView.setTypeface(fromAsset);
    }

    //绑定控件
    private void findView(){
        downVideo1=(ImageView)this.findViewById(R.id.downVideo1);
        downVideo1.setOnClickListener(this);

        textView = (TextView) this.findViewById(R.id.downloadTitle);


    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.downVideo1:
                startActivity(new Intent(this,videoDetail.class));

                break;

        }

    }
}
