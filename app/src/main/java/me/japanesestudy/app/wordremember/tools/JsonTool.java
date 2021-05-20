package me.japanesestudy.app.wordremember.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/8/11.
 */

public class JsonTool {

    private static Gson gson = new GsonBuilder().create();
    public static <T> T getInstance(String jsonString, Type classOfT) {
        try {
            T instance = gson.fromJson(jsonString, classOfT);
            return instance;
        } catch (Exception e) {
            return null;
        }
    }
    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }
    public static String toJsonString(Object object, Type classOfT) {
        return gson.toJson(object, classOfT);
    }
}
