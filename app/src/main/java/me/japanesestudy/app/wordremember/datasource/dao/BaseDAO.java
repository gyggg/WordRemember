package me.japanesestudy.app.wordremember.datasource.dao;

import android.content.Context;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import me.japanesestudy.app.wordremember.datasource.entity.IBaseEntity;
import me.japanesestudy.app.wordremember.tools.AssertFileTool;
import me.japanesestudy.app.wordremember.tools.SQLiteTool;

/**
 * Created by guyu on 2018/1/9.
 */

public class BaseDAO<T extends IBaseEntity> {
    protected static DbManager db = null;
    //static database config
    public static final String DB_NAME = "word.db";
    public static final String SQL_NAME = "main.sql";
    public static String DB_DIR_PATH = null;
    protected static Context context;
    protected Class<T> type;

    public BaseDAO(){
        onCreate();
    }

    public static boolean dbIsExisted () {
        return new File(DB_DIR_PATH, DB_NAME).exists();
    }

    public static void init(Context context) {
        BaseDAO.context = context;
        DB_DIR_PATH = context.getFilesDir().getPath() + "/";
        if(!dbIsExisted()) {
            try {
                SQLiteTool.createDatabaseFromSQL(new File(DB_DIR_PATH, DB_NAME), AssertFileTool.readTextFromAssertFile(context, SQL_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.print("");
    }

    public static String getDbPath() {
        return DB_DIR_PATH + DB_NAME;
    }

    private synchronized void onCreate(){
        if(db == null) {
            DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                    .setDbName(DB_NAME) //设置数据库名，默认xutils.db
                    .setDbDir(new File(DB_DIR_PATH)) //设置数据库路径，默认存储在app的私有目录
                    .setDbVersion(2) //设置数据库的版本号
                    .setDbOpenListener(new DbManager.DbOpenListener() { //设置数据库打开的监听
                        @Override
                        public void onDbOpened(DbManager db) {
                            //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                            db.getDatabase().enableWriteAheadLogging();
                        }
                    })
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() { //设置数据库更新的监听
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        }
                    })
                    .setTableCreateListener(new DbManager.TableCreateListener() { //设置表创建的监听
                        @Override
                        public void onTableCreated(DbManager db, TableEntity<?> table) {
                            Log.i("JAVA", "onTableCreated：" + table.getName());
                        }
                    });
            //.setAllowTransaction(true) //设置是否允许事务，默认true
            db = x.getDb(daoConfig);
        }
        //初始化类型
        type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public List<T> findAll() throws DbException {
        List<T> all = db.selector(type).findAll();
        return all;
    }

    public T findById(Object key) throws DbException {
        T one = db.findById(type, key);
        return one;
    }

    public void deleteById(Object key) throws DbException {
        db.deleteById(type, key);
    }

    public T save(T obj) throws DbException {
        Object key = obj.getKey();
        if(key == null) {
            insert(obj);
        } else {
            update(obj);
        }
        return obj;
    }

    public List<T> save(List<T> objs) throws DbException {
        for(T obj: objs) {
            Object key = obj.getKey();
            if(key == null) {
                insert(obj);
            } else {
                update(obj);
            }
        }
        return objs;
    }

    public T insert(T obj) throws DbException {
        db.saveBindingId(obj);
        return obj;
    }

    public T update(T obj) throws DbException {
        db.saveOrUpdate(obj);
        return obj;
    }

    public Class<T> getType() {
        return type;
    }

    public Selector<T> selector() throws DbException {
        return db.selector(getType());
    }

    public long lines() throws DbException {
        return db.selector(getType()).count();
    }
}