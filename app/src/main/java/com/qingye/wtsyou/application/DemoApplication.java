/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.qingye.wtsyou.application;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.util.StringUtil;

/**Application
 * @author Lemon
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
	}

	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this) ;
	}


}
