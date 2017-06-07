package com.sunpeng.lifecycle;

import android.app.Application;

/**
 * Created by sunpeng on 17/6/2.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // AppLifecycleCallback 实现 ActivityLifecycleCallbacks 接口方法
        this.registerActivityLifecycleCallbacks(new AppLifecycleCallback());
    }
}
