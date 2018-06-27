package com.qingye.wtsyou.application;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.qingye.wtsyou.manager.HttpManager;
import com.qingye.wtsyou.utils.ActivityLifecycleManage;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import zuo.biao.library.base.BaseApplication;

/**
 * Created by pm89 on 2018/6/23.
 */

public class DemoApplication extends BaseApplication {
    private static final String TAG = "DemoApplication";

    private static DemoApplication context;
    public static DemoApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        ActivityLifecycleManage.using(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //初始化请求方法，主要添加context
        new HttpManager(getApplicationContext());

        UMShareAPI.get(this);

        UMConfigure.init(this,"5aec10b7a40fa348ff000190"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");

        PlatformConfig.setQQZone("1105549191","GjcAUP03KwEKwJYl");
        PlatformConfig.setWeixin("wx9b9d665af16b3d63", "2f8753bd2d2f5179c9beb83dfe6d7cfa");
        PlatformConfig.setSinaWeibo("3693555751","f62b50beef82d9aba2d520ac8cb3fc01", "https://api.weibo.com/oauth2/default.html");

        JPushInterface.init(this);
        JPushInterface.setAlias(this,0,"lln654321");
        Set<String> set = new HashSet<>();
        set.add("lln123456");
        JPushInterface.addTags(this,0,set);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }
}
