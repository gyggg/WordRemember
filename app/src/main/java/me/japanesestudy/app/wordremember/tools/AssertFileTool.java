package me.japanesestudy.app.wordremember.tools;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by guyu on 2018/1/9.
 */

public class AssertFileTool {
    /**
     * 从Apk本地Assets复制文件到指定文件夹
     *
     * @param context
     * @param filepath
     * @param fileName
     * @return
     */
    public static boolean copyFileFromAssets(Context context, String filepath,
                                             String fileName) {
        boolean result = false;
        try {
            // 检查 SQLite 数据库文件是否存在
            if (true || (new File(filepath + fileName)).exists() == false) {
                // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
                File f = new File(filepath);
                // 如 database 目录不存在，新建该目录
                if (!f.exists()) {
                    f.mkdir();
                }
                try {
                    // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
                    InputStream is = context.getAssets().open(fileName);
                    // 输出流
                    OutputStream os = new FileOutputStream(filepath + "/" + fileName);
                    // 文件写入
                    byte[] buffer = new byte[1024];
                    int length;
                    int kb = 0;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                        kb++;
                    }
                    System.out.println("kb:" + kb);
                    // 关闭文件流
                    os.flush();
                    os.close();
                    is.close();
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readTextFromAssertFile(Context context, String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
