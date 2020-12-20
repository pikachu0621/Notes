package com.pikachu.record.sql.data;

import android.content.Context;

import com.pikachu.record.BuildConfig;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 创建数据库、创建数据库表、包含增删改查的操作以及数据库的升级
 * Created by Mr.sorrow on 2017/5/5.
 */

public class ToolSqlMaster {


    private static String DB_NAME = "record.db";

    private Context context;
    //多线程中要被共享的使用volatile关键字修饰
    private volatile static ToolSqlMaster manager = new ToolSqlMaster();
    private static DaoMaster sDaoMaster;
    private static DaoMaster.DevOpenHelper sHelper;
    private static DaoSession sDaoSession;

    /**
     * 单例模式获得操作数据库对象
     *
     * @return
     */
    public static ToolSqlMaster getInstance(String dbName) {
        DB_NAME=dbName;
        return manager;
    }

    private ToolSqlMaster() {
        setDebug();
    }

    public ToolSqlMaster init(Context context) {
        this.context = context;
        return this;
    }

    /**
     * 判断是否有存在数据库，如果没有则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (sDaoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            sDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return sDaoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (sDaoSession == null) {
            if (sDaoMaster == null) {
                sDaoMaster = getDaoMaster();
            }
            sDaoSession = sDaoMaster.newSession();
        }
        return sDaoSession;
    }

    /**
     * 打开输出日志，默认关闭
     */
    public void setDebug() {
        if (BuildConfig.DEBUG) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper() {
        if (sHelper != null) {
            sHelper.close();
            sHelper = null;
        }
    }

    public void closeDaoSession() {
        if (sDaoSession != null) {
            sDaoSession.clear();
            sDaoSession = null;
        }
    }








    /**
     * 添加多条数据，需要开辟新的线程
     */
   /* public void insertMultStudent(  final List<Object> objects) {
        manager.getDaoSession().runInTx(() -> {
            for (Student student : students) {
                manager.getDaoSession().insertOrReplace(student);
            }
        });
    }
    */

}