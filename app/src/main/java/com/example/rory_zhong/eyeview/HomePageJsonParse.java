package com.example.rory_zhong.eyeview;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


//解析每日推荐数据的工具类
public class HomePageJsonParse {
    public static List<HomePageBean> getItemList(String json){
        List<HomePageBean> ItemList=new ArrayList<>();
        HomePageBean homePageBean;
        //先拿到itemList数组
        JsonParser parser = new JsonParser();
        JsonObject rootObejct = parser.parse(json).getAsJsonObject();
        JsonArray projectArray = rootObejct.getAsJsonArray("itemList");
        //循环遍历数组
        try {
            for(JsonElement home:projectArray){
                homePageBean=new Gson().fromJson(home,new TypeToken<HomePageBean>(){}.getType());
                //根据条件过滤
                if((homePageBean.getType()).equals("video")){//通过查看json数据，数组里面的每个json对象都具有type名称，但有些的值不同，video的话才适用
                    if((homePageBean.getData().getLibrary()).equals("DAILY")) //获取的是每日精选的视频，可更改DAILY，用于分类数据的获取
                        ItemList.add(homePageBean);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("ItemListJsonParse:","解析异常！");
        }

        return ItemList;
    }
}
