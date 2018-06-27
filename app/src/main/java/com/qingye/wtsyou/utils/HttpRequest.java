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

import com.qingye.wtsyou.basemodel.IdName;
import com.qingye.wtsyou.basemodel.POI;
import com.qingye.wtsyou.manager.HttpManager;
import com.qingye.wtsyou.model.CreateSupportRequest;
import com.qingye.wtsyou.model.CreateVoteRequest;
import com.qingye.wtsyou.model.DepositApplyRequest;
import com.qingye.wtsyou.model.Id;
import com.qingye.wtsyou.model.OrderApply;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.util.JSON;
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

	/**注销登录
	 */
	public static void postLoginOut(final int requestCode,
										final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.LOGINOUT, requestCode, listener);
	}

	/**关注好友
	 * @param userId
	 */
	public static void getGetFocusFriend(final int requestCode ,final String userId,
										final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(userId, URL_BASE + URLConstant.FOCUSFRIEND, requestCode, listener);
	}

	/**取消关注好友
	 * @param userId
	 */
	public static void getGetCancelFocusFriend(final int requestCode ,final String userId,
										 final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(userId, URL_BASE + URLConstant.CANCELFOCUSFRIEND, requestCode, listener);
	}

	/**明星排行榜
	 * @param page
	 * @param pageSize
	 * @param desc
	 * @param keywords
	 * @param periods
	 * @param maxPeriods
	 * @param listener
	 */
	public static void postStarsRank(final int requestCode, int page, int pageSize, boolean desc, String keywords,
									 String periods, String maxPeriods, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);
		params.put(Constant.DESC, desc);
		if (keywords != null) {
			params.put(Constant.KEYWORDS, keywords);
		}
		if (periods != null) {
			params.put(Constant.PERIODS, periods);
		}
		if (maxPeriods != null) {
			params.put(Constant.MAXPERIODS, maxPeriods);
		}

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.STARSRANKING, requestCode, listener);
	}

	/**粉丝贡献榜
	 * @param page
	 * @param pageSize
	 * @param desc
	 * @param keywords
	 * @param periods
	 * @param maxPeriods
	 * @param listener
	 */
	public static void postFansRank(final int requestCode, int page, int pageSize, boolean desc, String keywords,
									String periods, String maxPeriods, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);
		params.put(Constant.DESC, desc);
		if (keywords != null) {
			params.put(Constant.KEYWORDS, keywords);
		}
		if (periods != null) {
			params.put(Constant.PERIODS, periods);
		}
		if (maxPeriods != null) {
			params.put(Constant.MAXPERIODS, maxPeriods);
		}

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.FANSRANKING, requestCode, listener);
	}

	/**明星周往期排行榜详情
	 * @param page
	 * @param pageSize
	 * @param desc
	 * @param keywords
	 * @param periods
	 * @param maxPeriods
	 * @param listener
	 */
	public static void postHistoryStarsRank(final int requestCode, int page, int pageSize, boolean desc, String keywords,
												String periods, String maxPeriods, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);
		params.put(Constant.DESC, desc);
		if (keywords != null) {
			params.put(Constant.KEYWORDS, keywords);
		}
		if (periods != null) {
			params.put(Constant.PERIODS, periods);
		}
		if (maxPeriods != null) {
			params.put(Constant.MAXPERIODS, maxPeriods);
		}

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.HISTORYSTARSRANKING, requestCode, listener);
	}

	/**粉丝周往期排行榜详情
	 * @param page
	 * @param pageSize
	 * @param desc
	 * @param keywords
	 * @param periods
	 * @param maxPeriods
	 * @param listener
	 */
	public static void postHistoryFansRank(final int requestCode, int page, int pageSize, boolean desc, String keywords,
											String periods, String maxPeriods, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);
		params.put(Constant.DESC, desc);
		if (keywords != null) {
			params.put(Constant.KEYWORDS, keywords);
		}
		if (periods != null) {
			params.put(Constant.PERIODS, periods);
		}
		if (maxPeriods != null) {
			params.put(Constant.MAXPERIODS, maxPeriods);
		}

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.HISTORYFANSRANKING, requestCode, listener);
	}

	/**打榜
	 * @param uuid
	 * @param listener
	 */
		public static void postHit(final int requestCode, String uuid, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.HIT + "/" + uuid, requestCode, listener);
	}

	/**首页
	 * @param listener
	 */
	public static void postHomeList(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.HOMELIST, requestCode, listener);
	}

	/**根据条件查询活动
	 * @param activityStates
	 * @param relevanceStar
	 * @param cityName
	 * @param listener
	 */
	public static void postCampaignList(final int requestCode, String activityStates, String relevanceStar, String cityName,
										final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		if (activityStates != null) {
			params.put(Constant.ACTIVITYSTATES, activityStates);
		}
		if (relevanceStar != null) {
			params.put(Constant.RELEVANCESTAR, relevanceStar);
		}
		if (cityName != null) {
			params.put(Constant.CITYNAME, cityName);
		}

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.CAMPAIGNQUERY, requestCode, listener);
	}

	/**投票
	 * @param listener
	 */
	public static void getVote(final int requestCode, final String uuid,
									   final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(uuid, URL_BASE + URLConstant.VOTE, requestCode, listener);
	}

	/**获取应援类型
	 * @param listener
	 */
	public static void getSupportType(final int requestCode, final String type,
							   final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(type, URL_BASE + URLConstant.SUPPORTTYPE, requestCode, listener);
	}

	/**
	 * 获取支付信息配置-充值
	 *
	 * @param amount
	 * @param channel
	 */
	public static void postGetPaymentConfig(final int requestCode, final Double amount, final IdName channel,
	                                  final OnHttpResponseListener listener) {

		DepositApplyRequest depositApplyRequest = new DepositApplyRequest();
		depositApplyRequest.setAmount(amount);
		depositApplyRequest.setChannel(channel);

        HttpManager.getInstance().postByJsonStr(JSON.toJSONString(depositApplyRequest), URL_BASE + URLConstant.GETPAYMENTCONFIG, requestCode, listener);
    }

	/**
	 * 获取支付信息配置-订单
	 *
	 * @param amount
	 * @param channel
	 */
	public static void postGetPaymentConfigOrder(final int requestCode, final String uuid, final BigDecimal carrieFree, final BigDecimal amount, final BigDecimal number,
												 final IdName channel, final String phone, final POI poi,final String receiver, final String SkuId, final OnHttpResponseListener listener) {
		OrderApply orderApply = new OrderApply();
		orderApply.setActivityId(uuid);
		orderApply.setCarrierFee(carrieFree);
		orderApply.setChannel(channel);
		orderApply.setMobile(phone);
		orderApply.setPrice(amount);
		orderApply.setQty(number);
		if (poi != null) {
			orderApply.setReveiveAddress(poi);
		}
		if (receiver != null) {
			orderApply.setReveiver(receiver);
		}
		orderApply.setSkuId(SkuId);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(orderApply), URL_BASE + URLConstant.GETPAYMENTCONFIGORDER, requestCode, listener);
	}

	/**查询爱心
	 * @param listener
	 */
	public static void postHeartDetailed(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.HEARTQUERY, requestCode, listener);
	}

	/**查询钻石
	 * @param listener
	 */
	public static void postDiamondDetailed(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.DIAMONDQUERY, requestCode, listener);
	}

	/**查询金币
	 * @param listener
	 */
	public static void postCoinDetailed(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.COINQUERY, requestCode, listener);
	}

	/**获取签到记录
	 * @param listener
	 */
	public static void getSignRecord(final int requestCode,
									 final OnHttpResponseListener listener) {

		String params = "";

		HttpManager.getInstance().getByJsonStr(params, URL_BASE + URLConstant.GETSIGN, requestCode, listener);
	}



	/**个人信息
	 * @param listener
	 */
	public static void getPersonalMessage(final int requestCode,
										  final OnHttpResponseListener listener) {

		String params = "";

		HttpManager.getInstance().getByJsonStr(params, URL_BASE + URLConstant.GETPERSONALMESSAGE, requestCode, listener);
	}

	/**修改个人资料
	 * @param fullname
	 * @param nickname
	 * @param mobile
	 * @param sex
	 * @param photo
	 * @param autograph
	 * @param birthday
	 * @param backgruoud
	 * @param areaName
	 * @param listener
	 */
	public static void postModifyPersonalMessage(final int requestCode, String fullname, String nickname, String mobile, String sex,
												 String photo, String autograph, String birthday, String backgruoud,
												 String areaName, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.FULLNAME, fullname);
		params.put(Constant.NICKNAME, nickname);
		params.put(Constant.MOBILE, mobile);
		params.put(Constant.SEX, sex);
		params.put(Constant.PHOTO, photo);
		params.put(Constant.AUTOGRAPH, autograph);
		params.put(Constant.BIRTHDAY, birthday);
		params.put(Constant.BACKGROUND, backgruoud);
		params.put(Constant.AREANAME, areaName);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.MODIFYPERSONALMESSAGE, requestCode, listener);
	}

	/**获取指定收货地址
	 * @param uuid
	 * @param listener
	 */
	public static void postGetAppointAddress(final int requestCode, String uuid, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.GETAPPOINTRECEIVEADDRESS + "/" + uuid, requestCode, listener);
	}

	/**添加、修改收货地址
	 * @param name
	 * @param mobile
	 * @param poi
	 * @param isDefault
	 * @param listener
	 */
	public static void postSaveAddress(final int requestCode ,final String uuid, final String name, final String mobile, final POI poi,
									final Boolean isDefault, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		if (uuid != null) {
			params.put(Constant.UUID, uuid);
		}
		params.put(Constant.NAME, name);
		params.put(Constant.MOBILE, mobile);
		params.put(Constant.POI, poi);
		if (isDefault != null) {
			params.put(Constant.ISDEFAULT, isDefault);
		}

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.SAVERECEIVEADDRESS, requestCode, listener);
	}

	/**设置默认收货地址
	 * @param uuid
	 * @param listener
	 */
	public static void postSetDefaultAddress(final int requestCode, String uuid, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.SETDEFAULTRECEIVEADDRESS +  "/" + uuid, requestCode, listener);
	}

	/**删除收货地址
	 * @param uuid
	 * @param listener
	 */
	public static void postDeleteAddress(final int requestCode, String uuid, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.DELETERECEIVEADDRESS + "/" + uuid, requestCode, listener);
	}

	/**七牛获取token
	 * @param listener
	 */
	public static void getQiNiuToken(final int requestCode,
									 final OnHttpResponseListener listener) {

		String params = "";

		HttpManager.getInstance().getByJsonStr(params, URL_BASE + URLConstant.GETQINIUTOKEN, requestCode, listener);
	}

	/**登录
	 * @param accountType
	 * @param loginId
	 * @param password
	 * @param photo
	 * @param nickname
	 */
	public static String postLogin(final String accountType,final String loginId, final String password,
								 final String photo, final String nickname) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.ACCOUNT_TYPE, accountType);
		params.put(Constant.LOGIN_ID, loginId);
		if (password != null) {
			params.put(Constant.PASSWORD, password);
		}
		if (photo != null) {
			params.put(Constant.PHOTO, photo);
		}
		if (nickname != null) {
			params.put(Constant.NICKNAME, nickname);
		}

		return JSON.toJSONString(params);

	}

	/**注册
	 * @param accountType
	 * @param loginId
	 * @param authCode
	 * @param password
	 */
	public static String postRegister(final String accountType,final String loginId, final String authCode,
									final String password) {
		Map<String,Object> params = new HashMap<>();
		params.put(Constant.TYPE, accountType);
		params.put(Constant.LOGIN_ID, loginId);
		params.put(Constant.AUTH_CODE, authCode);
		params.put(Constant.PASSWORD, password);

		return JSON.toJSONString(params);
	}

	/**获取其他类型验证码
	 * @param type
	 * @param target
	 */
	public static String postGetVerifyCode(final String type, final String target) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.TYPE, type);
		params.put(Constant.TARGET, target);

		return JSON.toJSONString(params);
	}

	/**忘记密码
	 * @param checkIndex
	 * @param code
	 * @param type
	 * @param target
	 * @param firstPwd
	 * @param secondPwd
	 */
	public static String postModify(final String checkIndex, final String code, final String type, final String target
	    , String firstPwd, String secondPwd) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.CHECK_INDEX, checkIndex);
		params.put(Constant.CODE, code);
		params.put(Constant.TYPE, type);
		params.put(Constant.TARGET, target);
		params.put(Constant.FIRST_PWD, firstPwd);
		params.put(Constant.SECOND_PWD, secondPwd);

		return JSON.toJSONString(params);
	}

	/**绑定手机、邮箱
	 * @param checkIndex
	 * @param code
	 * @param type
	 * @param target
	 */
	public static String postBind(final String checkIndex, final String code, final String type, final String target) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.CHECK_INDEX, checkIndex);
		params.put(Constant.CODE, code);
		params.put(Constant.TYPE, type);
		params.put(Constant.TARGET, target);

		return JSON.toJSONString(params);
	}

	/**筹资列表
	 * @param paymentState
	 * @param activityId
	 * @param page
	 * @param pageSize
	 */
	public static String postCrowdMoneyDetailed(String paymentState, String activityId, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAYMENTSTATE, paymentState);
		params.put(Constant.ACTIVITYID, activityId);
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**参与列表
	 * @param activityId
	 * @param page
	 * @param pageSize
	 */
	public static String postJoinFans(String activityId, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.ACTIVITYID, activityId);
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**粉丝列表
	 * @param starUuid
	 * @param keywords
	 * @param page
	 * @param pageSize
	 */
	public static String postFansList(String starUuid, String keywords, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.STARSUUID, starUuid);
		if (keywords != null) {
			params.put(Constant.KEYWORDS, keywords);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**查询优惠券
	 * @param couponStates
	 * @param page
	 * @param pageSize
	 */
	public static String postCardList(String[] couponStates, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.CARDSTATES, couponStates);
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**查询门票
	 * @param paymentState
	 * @param parts
	 * @param showFail
	 * @param activityProperty
	 * @param page
	 * @param pageSize
	 */
	public static String postTicketList(String paymentState, String parts, boolean showFail, String activityProperty, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAYMENTSTATE, paymentState);
		params.put(Constant.PARTS, parts);
		params.put(Constant.SHOWFAIL, showFail);
		params.put(Constant.ACTIVITYPROPERTY, activityProperty);
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**分页查询，已关注的明星
	 * @param page
	 * @param pageSize
	 */
	public static String postPageData(int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**关键词分页查询，明星列表，好友列表
	 * @param page
	 * @param pageSize
	 * @param keywords
	 */
	public static String postKeywordsPageData(String keywords, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		if (keywords != null) {
			params.put(Constant.KEYWORDS, keywords);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**关键词分页查询，明星列表，好友列表
	 * @param page
	 * @param pageSize
	 * @param keywords
	 */
	public static String postKeywordPageData(String keywords, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		if (keywords != null) {
			params.put(Constant.KEYWORD, keywords);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**查询应援
	 * @param userId
	 * @param activityId
	 * @param activityProperty
	 * @param keywords
	 * @param page
	 * @param pageSize
	 */
	public static String postSupportQuery(String userId, String activityId, String activityProperty, String keywords, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();

		if (userId != null) {
			params.put(Constant.USERID,userId);
		}
		if (activityId != null) {
			params.put(Constant.ACTIVITYID,activityId);
		}
		if (activityProperty != null) {
			params.put(Constant.ACTIVITYPROPERTY, activityProperty);
		}
		if (keywords != null) {
			params.put(Constant.KEYWORDS, keywords);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**关注明星
	 * @param uuid
	 * @param listener
	 */
	public static void postFocusStars(final int requestCode, String uuid, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.FOCUSSTARS + uuid, requestCode, listener);
	}

	/**取消关注明星
	 * @param uuid
	 * @param listener
	 */
	public static void postCancelStars(final int requestCode, final String uuid, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.CANCELFOCUSSTARS + uuid, requestCode, listener);
	}

	/**覆盖关注明星
	 * @param starUuidList
	 */
	public static String postFocusStarsOverlap(final List<String> starUuidList) {

		Map<String,Object> params = new HashMap<>();
		params.put("starUuids", starUuidList);

		return JSON.toJSONString(params);
	}

	/**活动首页
	 */
	public static String postCampaignHome() {

		Map<String,Object> params = new HashMap<>();


		return JSON.toJSONString(params);
	}

	/**查询投票
	 * @param activityStates
	 * @param relevanceStar
	 * @param cityName
	 * @param activityProperty
	 * @param createUserId
	 * @param page
	 * @param pageSize
	 */
	public static String postVoteQuery(String activityStates, String relevanceStar, String cityName,
									 String activityProperty, String createUserId, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();

		if (activityStates != null) {
			params.put(Constant.ACTIVITYSTATES,activityStates);
		}
		if (relevanceStar != null) {
			params.put(Constant.RELEVANCESTAR,relevanceStar);
		}
		if (cityName != null) {
			params.put(Constant.CITYNAME,cityName);
		}
		if (activityProperty != null) {
			params.put(Constant.ACTIVITYPROPERTY,activityProperty);
		}
		if (createUserId != null) {
			params.put(Constant.CREATEUSERID,createUserId);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**查询众筹
	 * @param activityStates
	 * @param relevanceStar
	 * @param cityName
	 * @param activityProperty
	 * @param createUserId
	 * @param page
	 * @param pageSize
	 */
	public static String postCrowdQuery(String activityStates, String relevanceStar, String cityName,
									  String activityProperty, String createUserId, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();

		if (activityStates != null) {
			params.put(Constant.ACTIVITYSTATES,activityStates);
		}
		if (relevanceStar != null) {
			params.put(Constant.RELEVANCESTAR,relevanceStar);
		}
		if (cityName != null) {
			params.put(Constant.CITYNAME,cityName);
		}
		if (activityProperty != null) {
			params.put(Constant.ACTIVITYPROPERTY,activityProperty);
		}
		if (createUserId != null) {
			params.put(Constant.CREATEUSERID,createUserId);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**查询演唱会
	 * @param activityStates
	 * @param relevanceStar
	 * @param cityName
	 * @param activityProperty
	 * @param createUserId
	 * @param parts
	 * @param page
	 * @param pageSize
	 */
	public static String postConcertQuery(String activityStates, String relevanceStar, String cityName,
										String activityProperty, String createUserId, String parts, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();

		if (activityStates != null) {
			params.put(Constant.ACTIVITYSTATES,activityStates);
		}
		if (relevanceStar != null) {
			params.put(Constant.RELEVANCESTAR,relevanceStar);
		}
		if (cityName != null) {
			params.put(Constant.CITYNAME,cityName);
		}
		if (activityProperty != null) {
			params.put(Constant.ACTIVITYPROPERTY,activityProperty);
		}
		if (createUserId != null) {
			params.put(Constant.CREATEUSERID,createUserId);
		}
		if (parts != null) {
			params.put(Constant.PARTS,parts);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**搜索活动
	 * @param keywords
	 */
	public static String postCampaignSearch(String keywords) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.KEYWORDS, keywords);

		return JSON.toJSONString(params);
	}

	/**提现申请
	 * @param amount
	 * @param type
	 */
	public static String postWithdraw(int amount, String type) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.AMOUNT, amount);
		params.put(Constant.TYPE, type);

		return JSON.toJSONString(params);
	}

	/**
	 * 提现申请
	 *
	 * @param amount
	 * @param channel
	 */
	/*public static void postWithdraw(final int requestCode, final String userId, final double amount, final IdName channel,
									final OnHttpResponseListener listener) {

		DepositApplyRequest depositApplyRequest = new DepositApplyRequest();
		depositApplyRequest.setAmount(amount);
		depositApplyRequest.setChannel(channel);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(depositApplyRequest), URL_BASE + URLConstant.GETPAYMENTCONFIG, requestCode, listener);
	}*/

	/**发起应援
	 * @param name
	 * @param activityName
	 * @param activityIcon
	 * @param name
	 * @param detail
	 * @param relevanceStar
	 * @param settingGoalsPricefinal
	 * @param chatingRooms
	 */
	public static String postCreateSupport(final String activityName, final String activityIcon, final String name,
										   final String detail, final List<Id> relevanceStar, final BigDecimal settingGoalsPricefinal,
										   final int WHICH, final String[] chatingRooms) {

		CreateSupportRequest createSupportRequest = new CreateSupportRequest();
		createSupportRequest.setActivityName(activityName);
		createSupportRequest.setActivityIcon(activityIcon);
		createSupportRequest.setName(name);
		createSupportRequest.setDetail(detail);
		createSupportRequest.setRelevanceStars(relevanceStar);
		if (WHICH == 1) {
			createSupportRequest.setSettingGoalsPrice(settingGoalsPricefinal);
		}
		if (chatingRooms != null) {
			createSupportRequest.setChatingRooms(chatingRooms);
		}

		return JSON.toJSONString(createSupportRequest);
	}

	/**发起投票
	 * @param name
	 * @param relevanceStar
	 * @param address
	 * @param description
	 * @param chatingRooms
	 */
	public static String postCreateVote(final String name, final String relevanceStar,
									  final POI address, final String description, final String[] chatingRooms) {

		CreateVoteRequest createVoteRequest = new CreateVoteRequest();
		createVoteRequest.setName(name);
		createVoteRequest.setRelevanceStar(relevanceStar);
		createVoteRequest.setAddress(address);
		createVoteRequest.setDescription(description);
		if (chatingRooms != null) {
			createVoteRequest.setChatingRooms(chatingRooms);
		}

		return JSON.toJSONString(createVoteRequest);
	}

	/**反馈
	 * @param content
	 */
	public static String postFeedBack(String content) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.CONTENT, content);

		return JSON.toJSONString(params);
	}

	/**二次关联聊天室
	 * @param activityId
	 * @param chatingRooms
	 */
	public static String postAssociateConversation(String activityId, String[] chatingRooms) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.ACTIVITYID, activityId);
		params.put(Constant.CHATINTROOMLIST, chatingRooms);

		return JSON.toJSONString(params);
	}

	/**签到
	 */
	public static String postSign() {

		Map<String,Object> params = new HashMap<>();

		return JSON.toJSONString(params);
	}

	/**我的活动
	 * @param  activityProperty
	 * @param parts
	 * @param page
	 * @param pageSize
	 */
	public static String postMyCampaign(String activityProperty, String parts, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.ACTIVITYPROPERTY, activityProperty);
		if (parts != null) {
			params.put(Constant.PARTS, parts);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**周往期排行榜
	 * @param page
	 * @param pageSize
	 * @param desc
	 * @param keywords
	 * @param periods
	 * @param maxPeriods
	 */
	public static String postRankQuery(int page, int pageSize, boolean desc, String keywords,
												String periods, String maxPeriods) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);
		params.put(Constant.DESC, desc);
		if (keywords != null) {
			params.put(Constant.KEYWORDS, keywords);
		}
		if (periods != null) {
			params.put(Constant.PERIODS, periods);
		}
		if (maxPeriods != null) {
			params.put(Constant.MAXPERIODS, maxPeriods);
		}

		return JSON.toJSONString(params);
	}

	/**查询订单
	 * @param state
	 * @param paymentState
	 * @param paymentStates
	 * @param parts
	 * @param showFail
	 * @param page
	 * @param pageSize
	 */
	public static String postOrderList(String state, String[] paymentStates, String parts, Boolean showFail, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		if (state != null) {
			params.put(Constant.STATE, state);
		}
		/*if (paymentState != null) {
			params.put(Constant.PAYMENTSTATE, paymentState);
		}*/
		if (paymentStates != null) {
			params.put(Constant.PAYMENTSTATES, paymentStates);
		}
		params.put(Constant.PARTS, parts);
		params.put(Constant.SHOWFAIL, showFail);
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**订单取消
	 * @param id
	 */
	public static String postCancelOrder(String id) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.ID, id);

		return JSON.toJSONString(params);
	}

	/**查询收货地址列表
	 */
	public static String postGetAddress() {

		Map<String,Object> params = new HashMap<>();

		return JSON.toJSONString(params);
	}

	/**查询消息列表
	 * @param noticeType
	 * @param needCount
	 * @param page
	 * @param pageSize
	 */
	public static String postGetMessage(String[] noticeType, String needCount, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.NOTICE_TYPE, noticeType);
		params.put(Constant.NEED_COUNT, needCount);
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**查询聊天室
	 * @param name
	 * @param ownerId
	 * @param adminUserId
	 * @param personId
	 * @param status
	 * @param topRecommend
	 * @param hotRecommend
	 * @param page
	 * @param pageSize
	 * @param desc
	 * @param excludeMyself
	 * @param sortKey
	 */
	public static String postConversationQueryList(String name, String[] starId, String ownerId, String adminUserId, String personId, String status
			, String topRecommend, String hotRecommend, int page, int pageSize, String desc, String excludeMyself, String sortKey) {

		Map<String,Object> params = new HashMap<>();
		if (name != null) {
			params.put(Constant.NAME, name);
		}
		if (starId != null) {
			params.put(Constant.STARIDS, starId);
		}
		if (ownerId != null) {
			params.put(Constant.OWNER_ID, ownerId);
		}
		if (adminUserId != null) {
			params.put(Constant.ADMINUSER_ID, adminUserId);
		}
		if (personId != null) {
			params.put(Constant.PERSONL_ID, personId);
		}
		if (status != null) {
			params.put(Constant.STATUS, status);
		}
		if (topRecommend != null) {
			params.put(Constant.TOP_RECOMMEND, topRecommend);
		}
		if (hotRecommend != null) {
			params.put(Constant.HOT_RECOMMEND, hotRecommend);
		}
		if (excludeMyself != null) {
			params.put(Constant.EXCLUDE_MYSELF, excludeMyself);
		}
		if (sortKey != null) {
			params.put(Constant.SORT_KEY, sortKey);
		}
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);
		params.put(Constant.DESC, desc);

		return JSON.toJSONString(params);
	}

	/**查询聊天室
	 * @param name
	 * @param ownerId
	 * @param adminUserId
	 * @param personId
	 * @param status
	 * @param topRecommend
	 * @param hotRecommend
	 * @param desc
	 * @param excludeMyself
	 * @param sortKey
	 */
	public static String postConversationQueryList(String name, String[] starId, String ownerId, String adminUserId, String personId, String status
			, String topRecommend, String hotRecommend, String desc, String excludeMyself, String sortKey) {

		Map<String,Object> params = new HashMap<>();
		if (name != null) {
			params.put(Constant.NAME, name);
		}
		if (starId != null) {
			params.put(Constant.STARIDS, starId);
		}
		if (ownerId != null) {
			params.put(Constant.OWNER_ID, ownerId);
		}
		if (adminUserId != null) {
			params.put(Constant.ADMINUSER_ID, adminUserId);
		}
		if (personId != null) {
			params.put(Constant.PERSONL_ID, personId);
		}
		if (status != null) {
			params.put(Constant.STATUS, status);
		}
		if (topRecommend != null) {
			params.put(Constant.TOP_RECOMMEND, topRecommend);
		}
		if (hotRecommend != null) {
			params.put(Constant.HOT_RECOMMEND, hotRecommend);
		}
		if (excludeMyself != null) {
			params.put(Constant.EXCLUDE_MYSELF, excludeMyself);
		}
		if (sortKey != null) {
			params.put(Constant.SORT_KEY, sortKey);
		}
		params.put(Constant.DESC, desc);

		return JSON.toJSONString(params);
	}

	/**查询粉丝主页关注明星
	 * @param userId
	 */
	public static String postFansFocusStarsQuery(String userId) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.USERID,userId);

		return JSON.toJSONString(params);
	}

	/**查询聊天室
	 * @param ownerId
	 */
	public static String postConversationQueryList(String ownerId) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.OWNER_ID, ownerId);

		return JSON.toJSONString(params);
	}

	/**聊天室内排行榜排序
	 * @param sortKey
	 * @param desc
	 * @param page
	 * @param pageSize
	 */
	public static String postGetRankConversation(String sortKey, String desc, int page, int pageSize) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.SORT_KEY, sortKey);
		params.put(Constant.DESC, desc);
		params.put(Constant.PAGE, page);
		params.put(Constant.PAGESIZE, pageSize);

		return JSON.toJSONString(params);
	}

	/**新建聊天室
	 * @param name
	 * @param coverImage
	 * @param backgroundImage
	 * @param notice
	 * @param stars
	 */
	public static String postCreateChatRoom(String name, String coverImage, String backgroundImage,
											String notice, List<Id> stars) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.NAME, name);
		params.put(Constant.COVER_IMAGE, coverImage);
		params.put(Constant.BACKGROUND_IMAGE, backgroundImage);
		params.put(Constant.NOTICE, notice);
		params.put(Constant.STARS, stars);

		return JSON.toJSONString(params);
	}

	/**重新支付订单、确认收货
	 */
	public static String postNoParams() {

		Map<String,Object> params = new HashMap<>();

		return JSON.toJSONString(params);
	}
}