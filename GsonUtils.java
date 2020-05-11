package com.hierway.vpline.utils;

import com.google.gson.Gson;

/**
 * @ClassName: GsonUtils
 * @Description:
 * @Auther: Mazy
 * @create: 2020-04-27 19:03
 */
public class GsonUtils {
    private static Gson gson = new Gson();

    public static String toJson(Object object){
        return gson.toJson(object);
    }
    public static <T> T fromJson(String json, Class<T> classOfT){
       return gson.fromJson(json,classOfT);
    }
}
