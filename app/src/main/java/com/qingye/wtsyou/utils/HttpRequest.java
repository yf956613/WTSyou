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

package com.qingye.wtsyou.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.MD5Util;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.StringUtil;

/**HTTP请求工具类
 * @author Lemon
 * @use 添加请求方法xxxMethod >> HttpRequest.xxxMethod(...)
 * @must 所有请求的url、请求方法(GET, POST等)、请求参数(key-value方式，必要key一定要加，没提供的key不要加，value要符合指定范围)
 *       都要符合后端给的接口文档
 */
public class HttpRequest {
	//	private static final String TAG = "HttpRequest";


	/**添加请求参数，value为空时不添加
	 * @param list
	 * @param key
	 * @param value
	 */
	public static void addExistParameter(List<Parameter> list, String key, Object value) {
		if (list == null) {
			list = new ArrayList<Parameter>();
		}
		if (StringUtil.isNotEmpty(key, true) && StringUtil.isNotEmpty(value, true) ) {
			list.add(new Parameter(key, value));
		}
	}



	/**基础URL，这里服务器设置可切换*/
	public static final String URL_BASE = SettingUtil.getCurrentServerAddress();
	public static final String PAGE_NUM = "pageNum";




	//示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//user<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String RANGE = "range";

	public static final String ID = "id";
	public static final String USER_ID = "userId";
	public static final String CURRENT_USER_ID = "currentUserId";

	/**登陆
	 * @param accountType
	 * @param loginId
	 * @param password
	 * @param listener
	 */
	public static void login(final String accountType,String loginId, final String password,
							 final int requestCode, final OnHttpResponseListener listener) {

		Map<String ,String> params=new HashMap<>();
		params.put(Constant.ACCOUNT_TYPE, accountType);
		params.put(Constant.LOGIN_ID, loginId);
		params.put(Constant.PASSWORD, password);


		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.LOGIN, requestCode, listener);
	}
}