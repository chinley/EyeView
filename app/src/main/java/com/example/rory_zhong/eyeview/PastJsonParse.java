package com.example.rory_zhong.eyeview;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chinley on 2018/1/1.
 */

public class PastJsonParse {
    public static List<PastBean> getItemList(String json){
        List<PastBean> ItemList=new ArrayList<>();
        //拿到itemList数组
        JsonParser parser = new JsonParser();
        JsonObject rootObejct = parser.parse(json).getAsJsonObject();
        JsonElement project = rootObejct.get("itemList");

        Gson gson=new Gson();
        try{
            if(project.isJsonArray()){//如果是数组
                Type listType=new TypeToken<List<PastBean>>(){}.getType();
                ItemList=gson.fromJson(project,listType);
                Log.i("解析分类数据成功","");
            }
        }catch (Exception e){
            Log.e("解析分类数据异常","");
        }

        return ItemList;
    }
}
