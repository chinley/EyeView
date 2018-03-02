package com.example.rory_zhong.eyeview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserLogin extends AppCompatActivity implements View.OnClickListener{
    private EditText etuame,etpassword;
    private Button btlogin,btregister;
    private String ULmusername,ULmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etuame=(EditText)findViewById(R.id.Etname);
        etpassword=(EditText)findViewById(R.id.Etpassword);
        btlogin=(Button)findViewById(R.id.BtLogin);
        btregister=(Button)findViewById(R.id.BtRegister);
        btlogin.setOnClickListener(this);
        btregister.setOnClickListener(this);


    }

    public void onClick(View v) {
        ULmusername=etuame.getText().toString();
        ULmpassword=etpassword.getText().toString();

        switch (v.getId()){
            case R.id.BtLogin: //实现用户登录
                if (!(ULmusername.equals(""))&& !(ULmpassword.equals(""))) {

                   try{
                       BmobQuery<myUser> query = new BmobQuery<myUser>();
                       query.addWhereEqualTo("name", ULmusername);
                       query.findObjects(new FindListener<myUser>() {
                           @Override
                           public void done(List<myUser> user, BmobException e) {

                               if (e == null) {
                                   for (myUser myuser : user) {
                                       if (ULmpassword.equals(myuser.getPassword())) {
                                           //用户名，密码匹配成功 修改登录信息
                                           Toast.makeText(UserLogin.this, "登录成功", Toast.LENGTH_LONG).show();
                                           //登录成功，更新登录状态
                                           myUser u=new myUser();
                                           u.setIsLogin(true);
                                           u.update(myuser.getObjectId(), new UpdateListener() {
                                                       @Override
                                                       public void done(BmobException e) {
                                                           if (e == null) {
                                                               Log.i("UserLogin","更新登录状态成功");
                                                           } else {
                                                               Log.e("UserLogin","更新登录状态失败："+e.getMessage()+","+e.getErrorCode());
                                                           }
                                                       }
                                                   });
                                           ///////登录成功 需返回用户ID
                                           Intent intent1=new Intent();
                                           intent1.putExtra("userId",myuser.getObjectId());//传递数据
                                           intent1.putExtra("userName",myuser.getName());
                                           Log.i("用户id:",myuser.getObjectId());
                                           setResult(1,intent1);
                                           finish();
                                           return;
                                       }
                                       else {
                                           Toast.makeText(UserLogin.this, "密码输入错误", Toast.LENGTH_LONG).show();
                                           return;
                                       }
                                   }
                                   Toast.makeText(UserLogin.this, "用户名未注册", Toast.LENGTH_LONG).show();
                               }
                               else {//异常情况
                                   Toast.makeText(UserLogin.this, "登录失败", Toast.LENGTH_LONG).show();
                               }
                           }
                       });
                   }catch (Exception e){
                       Log.e("UserLogin.class:","查询登录用户异常");
                   }

                } else Toast.makeText(UserLogin.this, "账户名或密码不能为空", Toast.LENGTH_SHORT).show();
                break;

            case R.id.BtRegister://实现用户注册

                if (!(ULmusername.equals(""))&& !(ULmpassword.equals(""))) {
                    //检查用户名是否存在

                    try{
                        BmobQuery<myUser> query = new BmobQuery<myUser>();
                        query.addWhereEqualTo("name", ULmusername);
                        query.findObjects(new FindListener<myUser>() {
                            @Override
                            public void done(List<myUser> user, BmobException e) {//查询用户名是否重复

                                if (e == null) {
                                    for (myUser myuser : user) {
                                        Toast.makeText(UserLogin.this, "用户名已被注册", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    myUser bu = new myUser();
                                    bu.setName(ULmusername);
                                    bu.setPassword(ULmpassword);
                                    bu.setIsLogin(true);//注册后默认登录
                                    bu.save(new SaveListener<String>() {//save:存储新数据
                                        @Override
                                        public void done(String objectId, BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(UserLogin.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                ///////////////////返回my界面
                                                Intent intent1=new Intent();
                                                intent1.putExtra("userName",ULmusername);
                                                intent1.putExtra("userId",objectId);
                                                setResult(1,intent1);
                                                finish();

                                            } else {
                                                Toast.makeText(UserLogin.this, "注册失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else
                                    Toast.makeText(UserLogin.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (Exception e){
                        Log.e("UserLogin.class:","查询注册用户异常");
                    }
                }
                else
                    Toast.makeText(UserLogin.this, "用户名和密码和不能为空", Toast.LENGTH_SHORT).show();

                break;//case break

        }
    }
}