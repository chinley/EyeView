package com.example.rory_zhong.eyeview;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class launch extends Activity {
    /**
     * Called when the activity is first created.
     */

    private TextView textView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().setFormat(PixelFormat.RGBA_8888);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_launch);

        textView = (TextView) this.findViewById(R.id.launchText);
        //改变标题栏的字体
        AssetManager assets = this.getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, "font/courbd.ttf");
        textView.setTypeface(fromAsset);


        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                Intent mainIntent = new Intent(launch.this, MainActivity.class);
                launch.this.startActivity(mainIntent);
                launch.this.finish();
            }
        }, 1000); //for release

    }
}