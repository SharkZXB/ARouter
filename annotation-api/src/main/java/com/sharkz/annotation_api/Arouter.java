package com.sharkz.annotation_api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * ================================================
 * 作    者：SharkZ
 * 邮    箱：229153959@qq.com
 * 创建日期：2020/10/18  11:16
 * 描    述 路由表操作对象
 * 修订历史：
 * ================================================
 */
public class Arouter {

    private static Arouter instance;
    private Context mContext;
    private static Map<String, Class<? extends Activity>> activityMap;

    /**
     * 获取到单例
     *
     * @return
     */
    public static Arouter getInstance() {
        if (instance == null) {
            synchronized (Arouter.class) {
                instance = new Arouter();
                activityMap = new ArrayMap<>();
            }

        }
        return instance;
    }

    /**
     * 添加 Activity到 路由表
     *
     * @param activityName 路径名
     * @param cls          com.sharkz.login.LoginActivity.class 全路径class name
     */
    public void putActivity(String activityName, Class cls) {
        if (cls != null && !TextUtils.isEmpty(activityName)) {
            activityMap.put(activityName, cls);
        }
    }

    /**
     * 初始化 必须在Application 中初始化
     *
     * @param context app
     */
    public void init(Context context) {
        mContext = context;
        // 这里的 com.shark.router 就是注解处理器里面的 自动生成代码的包名 一定要一样 否则获取不到数据
        List<String> className = getAllActivityUtils("com.shark.router");
        for (String cls : className) {
            try {
                Class<?> aClass = Class.forName(cls);
                if (IRouter.class.isAssignableFrom(aClass)) {
                    IRouter iRouter = (IRouter) aClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 界面 跳转
     *
     * @param activityName 目标 Activity的路径
     */
    public void jumpActivity(String activityName) {
        jumpActivity(activityName, null);
    }

    /**
     * 界面跳转
     *
     * @param activityName 目标Activity的绝对路径
     * @param bundle       传递的参数
     */
    public void jumpActivity(String activityName, Bundle bundle) {
        Intent intent = new Intent();
        Class<? extends Activity> aCls = activityMap.get(activityName);
        if (aCls == null) {
            Log.e("wangjitao", " error -- > can not find activityName " + activityName);
            return;
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(mContext, aCls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // TODO 是不是很熟悉了
        mContext.startActivity(intent);
    }

    /**
     * 从 DexFile 里面获取 添加到路由表的数据
     *
     * @param packageName
     * @return
     */
    public List<String> getAllActivityUtils(String packageName) {
        List<String> list = new ArrayList<>();
        String path;
        try {
            path = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 0).sourceDir;
            DexFile dexFile = null;
            dexFile = new DexFile(path);
            Enumeration enumeration = dexFile.entries();
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                if (name.contains(packageName)) {
                    list.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

