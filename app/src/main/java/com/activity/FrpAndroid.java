package com.activity;


import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.activity.greendao.DaoMaster;
import com.activity.greendao.DaoSession;

public class FrpAndroid extends Application {

    public static final String TAG = "FrpAndroid";

    public static Context mContext;
    public static String FILENAME = "config.ini";

    public static String serverConfig = "";

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        setupDatabase();
    }

    private void setupDatabase() {
        //创建数据库shop.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shop.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static String getServerConfig(String name) {
        return serverConfig.replace("frpc.log", name + "frpc.log");
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
