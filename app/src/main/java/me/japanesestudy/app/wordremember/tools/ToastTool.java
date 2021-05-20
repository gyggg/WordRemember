package me.japanesestudy.app.wordremember.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by guyu on 2018/1/10.
 */

public class ToastTool {
    private static Context context;
    public static void init(Context context) {
        ToastTool.context = context;
    }
    public static void shortShow(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
