package com.hui.carbon;

import android.app.Application;

import com.hui.carbon.db.DBManager;

/* 表示全局应用的类*/
public class UniteApp extends Application {

    public String account = "游客"; //记录用户名

    public float money = (float) 0.0; //用户余额

    public int stepCount = 0;  // 步数

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        DBManager.initDB(getApplicationContext());
    }


}
