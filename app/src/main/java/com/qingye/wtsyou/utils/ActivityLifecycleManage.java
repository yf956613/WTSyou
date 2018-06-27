package com.qingye.wtsyou.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by win10ToSSD on 2017/9/29.
 */

/**
 * 提供一系列针对Activity实例的操作
 * 当Activity被创建时，会自动添加到mActivitys集合，而销毁时则移除。
 * 另外，调用finish并不会立刻执行onActivityDestroyed（或onDestroy），
 * 虽然从日志输出上看几乎是finish之后立刻onDestroy。
 * 因此在实例finish之后短时间内依然可以从mActivitys当中获得该实例
 */
public class ActivityLifecycleManage implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "生命周期";
    public static ActivityLifecycleManage lifecycleManage;

    private boolean mIsPrintLifecycleLog = false;

    /**
     * 最近被创建的Activity
     */
    private Activity mRecentActivity = null;


    /**
     * 当前创建且未被finish的activity
     */
    private WeakHashMap<Activity, String> mActivitys = new WeakHashMap<>();

    //    /**
    //     * 当前存在的所有Activity实例，Activity可能已执行onDestroy，但内存当中依然存在该实例
    //     */
    //    private WeakHashMap<Activity, String> mActivityInstances = new WeakHashMap<>();

    private ActivityLifecycleManage() {
    }

    /**
     * 是否打印生命周期日志
     *
     * @param b
     */
    public void setIsPrintLifecycleLog(boolean b) {
        mIsPrintLifecycleLog = b;
    }

    public static ActivityLifecycleManage using(Application application) {
        lifecycleManage = new ActivityLifecycleManage();
        application.registerActivityLifecycleCallbacks(lifecycleManage);
        return lifecycleManage;
    }

    public static ActivityLifecycleManage getInstance() {
        if (lifecycleManage == null) {
            throw new RuntimeException("getInstance: " +
                    "未启用ActivityLifecycleManage，获取Application实例调用ActivityLifecycleManage.using(Application " +
                    "application)以启用Activity生命周期管理");
        }
        return lifecycleManage;
    }

    /**
     * 获取最近被创建的Activity
     *
     * @return
     */
    public Activity getRecentActivity() {
        return mRecentActivity;
    }

    /**
     * 给指定的未被finish的activity添加tag，可调用public void finishActivity(String tag)结束所有指定tag的Activity
     *
     * @param activity
     * @param tag
     */
    public void setTag(Activity activity, String tag) {
        boolean finishing = activity.isFinishing();
        if (!finishing)
            mActivitys.put(activity, tag);
    }

    /**
     * 结束指定tag的Activity
     *
     * @param tag
     */
    public void finishActivity(String tag) {
        Set<Map.Entry<Activity, String>> entries = mActivitys.entrySet();
        Iterator<Map.Entry<Activity, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Activity, String> next = iterator.next();
            String value = next.getValue();
            if (tag == value) {
                next.getKey().finish();
                continue;
            }
            if (tag != null && tag.equals(value)) {
                next.getKey().finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        Set<Map.Entry<Activity, String>> entries = mActivitys.entrySet();
        Iterator<Map.Entry<Activity, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Activity key = iterator.next().getKey();
            if (key.getClass().equals(cls)) key.finish();
        }
    }

    /**
     * 获取指定tag的Activity
     *
     * @param TAG
     * @return
     */
    public List<Activity> getActivityFoTag(final String TAG) {
        List<Activity> activityList = new ArrayList<>();
        Set<Map.Entry<Activity, String>> entries = mActivitys.entrySet();
        for (Map.Entry<Activity, String> entry : entries) {
            String tag = entry.getValue();
            if (TAG == tag) {
                activityList.add(entry.getKey());
                continue;
            }
            if (TAG!=null&&TAG.equals(tag)){
                activityList.add(entry.getKey());
            }
        }
        return activityList;
    }

    /**
     * 关闭所有Activity
     */
    public void finishAll(){
        Set<Map.Entry<Activity, String>> entries = mActivitys.entrySet();
        Iterator<Map.Entry<Activity, String>> iterator = entries.iterator();
        while(iterator.hasNext()){
            iterator.next().getKey().finish();
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (mIsPrintLifecycleLog) Log.e(TAG, "onActivityCreated: " + activity.getLocalClassName());
        mActivitys.put(activity, null);
        mRecentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mIsPrintLifecycleLog) Log.e(TAG, "onActivityStarted: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //if (mIsPrintLifecycleLog) Log.e(TAG, "onActivityResumed: " + activity.getLocalClassName());

    }

    @Override
    public void onActivityPaused(Activity activity) {
        //if (mIsPrintLifecycleLog) Log.e(TAG, "onActivityPaused: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //if (mIsPrintLifecycleLog) Log.e(TAG, "onActivityStopped: " + activity.getLocalClassName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (mIsPrintLifecycleLog) Log.e(TAG, "onActivitySaveInstanceState: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mIsPrintLifecycleLog) Log.e(TAG, "onActivityDestroyed: " + activity.getLocalClassName());
        mActivitys.remove(activity);
    }
}
