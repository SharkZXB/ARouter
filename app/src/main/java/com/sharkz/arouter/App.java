package com.sharkz.arouter;

import android.app.Application;

import com.sharkz.annotation_api.Arouter;

/**
 * ================================================
 * 作    者：SharkZ
 * 邮    箱：229153959@qq.com
 * 创建日期：2020/10/18  11:22
 * 描    述
 * 修订历史：
 * ================================================
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Arouter.getInstance().init(this);
    }
}
