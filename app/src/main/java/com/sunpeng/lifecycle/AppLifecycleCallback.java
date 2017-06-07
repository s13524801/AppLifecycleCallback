package com.sunpeng.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by sunpeng on 17/6/2.
 */
public class AppLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "AppLifecycleCallback";

    private static final int APP_STATUS_UNKNOWN = -1;
    private static final int APP_STATUS_LIVE = 0;

    private int appStatus = APP_STATUS_UNKNOWN;

    private boolean isForground = true;
    private int appCount = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityStackManager.getInstance().addActivity(activity);

        if (appStatus == APP_STATUS_UNKNOWN) {
            appStatus = APP_STATUS_LIVE;
            startLauncherActivity(activity);
        }

        if (savedInstanceState != null && savedInstanceState.getBoolean("saveStateKey", false)) {
            Log.e(TAG, "localTime --> " + savedInstanceState.getLong("localTime"));
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        appCount++;
        if (!isForground) {
            isForground = true;
            Log.e(TAG, "app into forground");
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // 弱引用持有当前 Activity 实例
        ActivityStackManager.getInstance().setCurrentActivity(activity);
        // Activity 页面栈方式
        ActivityStackManager.getInstance().setTopActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        appCount--;
        if (!isForgroundAppValue()) {
            isForground = false;
            Log.d(TAG, "app into background ");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        outState.putBoolean("saveStateKey", true);
        outState.putLong("localTime", System.currentTimeMillis());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityStackManager.getInstance().removeActivity(activity);
    }

    private boolean isForgroundAppValue() {
        return appCount > 0;
    }

    private static void startLauncherActivity(Activity activity) {
        try {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
            String launcherClassName = launchIntent.getComponent().getClassName();
            String className = activity.getComponentName().getClassName();

            if (TextUtils.isEmpty(launcherClassName) || launcherClassName.equals(className)) {
                return;
            }

            Log.e(TAG, "launcher ClassName --> " + launcherClassName);
            Log.e(TAG, "current ClassName --> " + className);

            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(launchIntent);
            activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
