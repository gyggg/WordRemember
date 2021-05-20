package me.japanesestudy.app.wordremember.tools;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by guyu on 2018/1/10.
 */

public class SQLiteTool {
    public static void createDatabaseFromSQL(File dbFile, String sql) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        String[] s = sql.split(";");
        for (int i = 0; i < s.length - 1; i++) {
            if (!TextUtils.isEmpty(s[i])) {
                try {
                    db.execSQL(s[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        db.close();
    }
}
