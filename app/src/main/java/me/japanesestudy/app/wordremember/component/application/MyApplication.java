package me.japanesestudy.app.wordremember.component.application;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DatabaseFilesProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.facebook.stetho.inspector.protocol.module.Database;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.japanesestudy.app.wordremember.datasource.dao.BaseDAO;
import me.japanesestudy.app.wordremember.tools.MDriver;
import me.japanesestudy.app.wordremember.tools.PreferenceTool;
import me.japanesestudy.app.wordremember.tools.ToastTool;
import okhttp3.OkHttpClient;

/**
 * Created by guyu on 2018/1/9.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        BaseDAO.init(this);
        PreferenceTool.init(this);
        AppConfig.init(this);
        ToastTool.init(this);
        initStethoSQLite();
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    void initStethoSQLite() {
                /*
        ＊Stetho 通过 databaseFilesProvider 提供数据库名称
        * 默认使用SqlDriber执行数据库操作，sql  通过 openDatabase 打开数据库
        *  File databaseFile = mContext.getDatabasePath(databaseName);
        *  继承Driver，重写 openDatabase() ，见MDriver
        *  修改 DefaultInspectorModulesBuilder.finish()中的Database使用的Driver
        * */

        final DatabaseFilesProvider databaseFilesProvider = new DatabaseFilesProvider() {
            @Override
            public List<File> getDatabaseFiles() {
                List<File> databaseFiles = new ArrayList();
                for (String filename : MyApplication.this.databaseList()) {
                    databaseFiles.add(new File(filename));
                }
                //添加自己的数据库地址
                databaseFiles.add(new File(BaseDAO.getDbPath()));
                return databaseFiles;
            }

        };
        Stetho.DefaultInspectorModulesBuilder builder = new Stetho.DefaultInspectorModulesBuilder(this).databaseFiles(databaseFilesProvider);
        //替换DataBase
        ArrayList<ChromeDevtoolsDomain> list = (ArrayList) builder.finish();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Database) {
                Database database = new Database();
                database.add(new MDriver(this, databaseFilesProvider));
                list.set(i, database);
            }
        }


        final Iterable<ChromeDevtoolsDomain> fixded = list;

        Stetho.initialize(new Stetho.Initializer(this) {
            @Override
            protected Iterable<DumperPlugin> getDumperPlugins() {
                return new Stetho.DefaultDumperPluginsBuilder(MyApplication.this).finish();
            }

            @Override
            protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                return fixded;
            }
        });
    }
}
