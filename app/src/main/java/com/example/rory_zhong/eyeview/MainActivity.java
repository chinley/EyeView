package com.example.rory_zhong.eyeview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.bmob.v3.Bmob;

import static com.example.rory_zhong.eyeview.R.id.fragmentLay;
import static com.example.rory_zhong.eyeview.R.layout.activity_main;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,My.FragmentInteraction{

    private LinearLayout tabHome0;
    private LinearLayout tabCommunity0;
    private LinearLayout tabPast0;
    private LinearLayout tabMy0;

    private ImageView tabHome;
    private ImageView tabCommunity;
    private ImageView tabPast;
    private ImageView tabMy;

    private FrameLayout myFrame;
    private FragmentManager myFM;

    private Fragment h;
    private Fragment c;
    private Fragment p;
    private Fragment m;
    private String userName="unknown";

    @Override
    public void process(String str) {
        if (str != null) {
            userName=str;//从我的界面传递过来的用户名，fragment2activity
            Log.i("MainActivity",userName);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(activity_main);
        //初始化Bmob
        Bmob.initialize(this, "ffc2bce284bdbc5da259cf69cd4b75b7");
        myFM = getSupportFragmentManager();
        findView();
        tabHome.performClick();

        int id = getIntent().getIntExtra("id", 0);
        if (id == 1) {
            tabCommunity.performClick();




        }
    }


    //重写onAttachFragment，解决fragment重叠问题，参照：http://blog.csdn.net/whitley_gong/article/details/51987911
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (h == null && fragment instanceof HomePage)
            h = fragment;
        if (c == null && fragment instanceof Community)
            c = fragment;
        if (p == null && fragment instanceof Past)
            p = fragment;
        if (m == null && fragment instanceof My)
            m = fragment;
    }



    //UI组件初始化与事件绑定
    private void findView() {
        tabHome = (ImageView) this.findViewById(R.id.homePage);
        tabCommunity = (ImageView)this.findViewById(R.id.community);
        tabPast = (ImageView)this.findViewById(R.id.past);
        tabMy = (ImageView)this.findViewById(R.id.my);

        myFrame = (FrameLayout) findViewById(fragmentLay);

        tabHome.setOnClickListener(this);
        tabCommunity.setOnClickListener(this);
        tabPast.setOnClickListener(this);
        tabMy.setOnClickListener(this);


    }
    //重置图片的选中状态
    public void selected(){
        tabHome.setSelected(false);
        tabCommunity.setSelected(false);
        tabPast.setSelected(false);
        tabMy.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(h!=null){
            transaction.hide(h);
        }
        if(c!=null){
            transaction.hide(c);
        }
        if(p!=null){
            transaction.hide(p);
        }
        if(m!=null) {
            transaction.hide(m);
        }
    }

    //重置底部菜单栏的图标
    private void resetImg() {
        tabHome.setImageResource(R.drawable.homepage);
        tabCommunity.setImageResource(R.drawable.community);
        tabPast.setImageResource(R.drawable.past);
        tabMy.setImageResource(R.drawable.my);

    }

    public void onClick(View v) {
        FragmentTransaction myTransaction = myFM.beginTransaction();
        hideAllFragment(myTransaction);
        switch(v.getId()){
            case R.id.homePage:

                selected();
                resetImg();
                tabHome.setSelected(true);
                tabHome.setImageResource(R.drawable.homepage2);

                if(h==null){
                    HomePage homePage=new HomePage();
                    myTransaction.add(fragmentLay,homePage);
                }else{
                    myTransaction.show(h);
                }
                break;

            case R.id.community:
                selected();
                resetImg();
                tabCommunity.setSelected(true);
                tabCommunity.setImageResource(R.drawable.community2);

                if(c==null){
                    c = new Community();
                    myTransaction.add(fragmentLay,c);
                }else{
                    myTransaction.show(c);
                }
                break;

            case R.id.past:
                selected();
                resetImg();
                tabPast.setSelected(true);
                tabPast.setImageResource(R.drawable.past2);

                if(p==null){
                    p = new Past();
                    myTransaction.add(fragmentLay,p);
                }else{
                    myTransaction.show(p);
                }
                break;

            case R.id.my:
                selected();
                resetImg();
                tabMy.setSelected(true);
                tabMy.setImageResource(R.drawable.my2);

                if(m==null){
                    m = new My();
                    myTransaction.add(fragmentLay,m);
                }else{
                    myTransaction.show(m);
                }
                break;
        }

        myTransaction.commit();
    }
}
