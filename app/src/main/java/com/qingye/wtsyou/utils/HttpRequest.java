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
import com.qingye.wtsyou.model.CreateSupportRequest;
import com.qingye.wtsyou.model.CreateVoteRequest;
import com.qingye.wtsyou.model.DepositApplyRequest;
import com.qingye.wtsyou.model.FocusStars;
import com.qingye.wtsyou.model.OrderApply;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.manager.HttpManager;
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

	/**登录
	 * @param accountType
	 * @param loginId
	 * @param password
	 * @param photo
	 * @param nickname
	 * @param listener
	 */
	public static void postLogin(final int requestCode ,final String accountType,final String loginId, final String password,
							  final String photo, final String nickname, final OnHttpResponseListener listener) {

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


		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.LOGIN, requestCode, listener);
	}

	/**注册
	 * @param accountType
	 * @param loginId
	 * @param authCode
	 * @param password
	 * @param listener
	 */
	public static void postRegister(final int requestCode ,final String accountType,final String loginId, final String authCode,
									final String password, final OnHttpResponseListener listener) {
		Map<String,Object> params = new HashMap<>();
		params.put(Constant.TYPE, accountType);
		params.put(Constant.LOGIN_ID, loginId);
		params.put(Constant.AUTHCODE, authCode);
		params.put(Constant.PASSWORD, password);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.REGISTER, requestCode, listener);
	}

	/**注销登录
	 */
	public static void postLoginOut(final int requestCode,
										final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.LOGINOUT, requestCode, listener);
	}

	/**获取验证码
	 * @param mobile
	 */
	public static void getGetVerifyCode(final int requestCode ,final String mobile,
								 final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(mobile, URL_BASE + URLConstant.VERIFYCODE, requestCode, listener);
	}

	/**全部明星
	 * @param listener
	 */
	public static void postAllStars(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.ALLSTARS, requestCode, listener);
	}

	/**关注明星
	 * @param focusStarsRequestList
	 * @param listener
	 */
	public static void postFocusStars(final int requestCode, final List<FocusStars> focusStarsRequestList, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.FUNSLIST, focusStarsRequestList);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.FOCUSSTARS, requestCode, listener);
	}

	/**取消关注明星
	 * @param focusStarsRequestList
	 * @param listener
	 */
	public static void postCancelStars(final int requestCode, final List<FocusStars> focusStarsRequestList, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.FUNSLIST, focusStarsRequestList);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.CANCELFOCUSSTARS, requestCode, listener);
	}

	/**已关注的明星
	 * @param listener
	 */
	public static void postFocuedStars(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.FOCUEDSTARS, requestCode, listener);
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

	/**明星周往期排行榜
	 * @param page
	 * @param pageSize
	 * @param desc
	 * @param keywords
	 * @param periods
	 * @param maxPeriods
	 * @param listener
	 */
	public static void postHistoryStarsWeekRank(final int requestCode, int page, int pageSize, boolean desc, String keywords,
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

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.HISTORYSTARSWEEKRANKING, requestCode, listener);
	}

	/**粉丝周往期贡献榜
	 * @param page
	 * @param pageSize
	 * @param desc
	 * @param keywords
	 * @param periods
	 * @param maxPeriods
	 * @param listener
	 */
	public static void postHistoryFansWeekRank(final int requestCode, int page, int pageSize, boolean desc, String keywords,
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

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.HISTORYFANSWEEKRANKING, requestCode, listener);
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

	/**活动首页
	 * @param listener
	 */
	public static void postCampaignHome(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.CAMPAIGNHOME, requestCode, listener);
	}

	/**查询投票
	 * @param activityStates
	 * @param relevanceStar
	 * @param cityName
	 * @param activityProperty
	 * @param createUserId
	 * @param listener
	 */
	public static void postVoteQuery(final int requestCode, String activityStates, String relevanceStar, String cityName,
								 String activityProperty, String createUserId, final OnHttpResponseListener listener) {

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

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.VOTEQUERY, requestCode, listener);
	}

	/**查询众筹
	 * @param activityStates
	 * @param relevanceStar
	 * @param cityName
	 * @param activityProperty
	 * @param createUserId
	 * @param listener
	 */
	public static void postCrowdQuery(final int requestCode, String activityStates, String relevanceStar, String cityName,
								   String activityProperty, String createUserId, final OnHttpResponseListener listener) {

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

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.CROWDQUERY, requestCode, listener);
	}

	/**查询演唱会
	 * @param activityStates
	 * @param relevanceStar
	 * @param cityName
	 * @param activityProperty
	 * @param createUserId
	 * @param parts
	 * @param listener
	 */
	public static void postConcertQuery(final int requestCode, String activityStates, String relevanceStar, String cityName,
								   String activityProperty, String createUserId, String parts, final OnHttpResponseListener listener) {

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

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.CONCERTQUERY, requestCode, listener);
	}

	/**查询应援
	 * @param userId
	 * @param activityId
	 * @param activityProperty
	 */
	public static void postSupportQuery(final int requestCode, String userId, String activityId,
										String activityProperty, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		if (userId != null) {
			params.put(Constant.USEID,userId);
		}
		if (activityId != null) {
			params.put(Constant.ACTIVITYID,activityId);
		}
		if (activityProperty != null) {
			params.put(Constant.ACTIVITYPROPERTY,activityProperty);
		}

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.SUPPORTQUERY, requestCode, listener);
	}

	/**获取投票详情
	 * @param listener
	 */
	public static void getVoteDetailed(final int requestCode, final String uuid,
										   final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(uuid, URL_BASE + URLConstant.VOTEDETIALED, requestCode, listener);
	}

	/**发起投票
	 * @param name
	 * @param relevanceStar
	 * @param address
	 * @param description
	 * @param listener
	 */
	public static void postCreateVote(final int requestCode, final String name, final String relevanceStar,
									final POI address, final String description, final OnHttpResponseListener listener) {

		CreateVoteRequest createVoteRequest = new CreateVoteRequest();
		createVoteRequest.setName(name);
		createVoteRequest.setRelevanceStar(relevanceStar);
		createVoteRequest.setAddress(address);
		createVoteRequest.setDescription(description);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(createVoteRequest), URL_BASE + URLConstant.CREATEVOTE, requestCode, listener);
	}

	/**投票
	 * @param listener
	 */
	public static void getVote(final int requestCode, final String uuid,
									   final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(uuid, URL_BASE + URLConstant.VOTE, requestCode, listener);
	}

	/**获取众筹详情
	 * @param listener
	 */
	public static void getCrowdDetailed(final int requestCode, final String uuid,
									   final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(uuid, URL_BASE + URLConstant.CROWDDETIALED, requestCode, listener);
	}

	/**获取售票详情
	 * @param listener
	 */
	public static void getSaleDetailed(final int requestCode, final String uuid,
										final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(uuid, URL_BASE + URLConstant.SALEDETIALED, requestCode, listener);
	}

	/**获取应援类型
	 * @param listener
	 */
	public static void getSupportType(final int requestCode, final String type,
							   final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(type, URL_BASE + URLConstant.SUPPORTTYPE, requestCode, listener);
	}

	/**发起应援
	 * @param name
	 * @param activityName
	 * @param activityIcon
	 * @param name
	 * @param detail
	 * @param relevanceStar
	 * @param settingGoalsPricefinal
	 * @param listener
	 */
	public static void postCreateSupport(final int requestCode, final String activityName, final String activityIcon, final String name,
									  final String detail, final String relevanceStar, final BigDecimal settingGoalsPricefinal,
									  final int WHICH, final OnHttpResponseListener listener) {

		CreateSupportRequest createSupportRequest = new CreateSupportRequest();
		createSupportRequest.setActivityName(activityName);
		createSupportRequest.setActivityIcon(activityIcon);
		createSupportRequest.setName(name);
		createSupportRequest.setDetail(detail);
		createSupportRequest.setRelevanceStar(relevanceStar);
		if (WHICH == 1) {
			createSupportRequest.setSettingGoalsPrice(settingGoalsPricefinal);
		}

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(createSupportRequest), URL_BASE + URLConstant.CREATESUPPORT, requestCode, listener);
	}

	/**获取应援详情
	 * @param listener
	 */
	public static void getSupportDetailed(final int requestCode, final String uuid,
									   final OnHttpResponseListener listener) {

		HttpManager.getInstance().getByJsonStr(uuid, URL_BASE + URLConstant.SUPPORTETIALED, requestCode, listener);
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

	/**
	 * 提现申请
	 *
	 * @param amount
	 * @param channel
	 */
	public static void postWithdraw(final int requestCode, final String userId, final double amount, final IdName channel,
											final OnHttpResponseListener listener) {

		DepositApplyRequest depositApplyRequest = new DepositApplyRequest();
		depositApplyRequest.setAmount(amount);
		depositApplyRequest.setChannel(channel);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(depositApplyRequest), URL_BASE + URLConstant.GETPAYMENTCONFIG, requestCode, listener);
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

	/**我的投票
	 * @param listener
	 */
	public static void postMyVote(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.MYVOTEQUERY, requestCode, listener);
	}

	/**我的众筹
	 * @param listener
	 */
	public static void postMyCrowd(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.MYCROWDQUERY, requestCode, listener);
	}

	/**我的演唱会
	 * @param listener
	 */
	public static void postMyConcert(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.MYCONCERTQUERY, requestCode, listener);
	}

	/**我的应援
	 * @param listener
	 */
	public static void postMySupport(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.MYSUPPORTQUERY, requestCode, listener);
	}

	/**获取签到记录
	 * @param listener
	 */
	public static void getSignRecord(final int requestCode,
									 final OnHttpResponseListener listener) {

		String params = "";

		HttpManager.getInstance().getByJsonStr(params, URL_BASE + URLConstant.GETSIGN, requestCode, listener);
	}

	/**签到
	 * @param listener
	 */
	public static void postSign(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.SIGN, requestCode, listener);
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

	/**查询收货地址列表
	 * @param listener
	 */
	public static void postGetAddress(final int requestCode, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.GETRECEIVINGADDRESS, requestCode, listener);
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

	/**反馈
	 * @param content
	 * @param listener
	 */
	public static void postFeedBack(final int requestCode, String content, final OnHttpResponseListener listener) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.CONTENT, content);

		HttpManager.getInstance().postByJsonStr(JSON.toJSONString(params), URL_BASE + URLConstant.FEEDBACK, requestCode, listener);
	}

	/**筹资列表
	 * @param paymentState
	 * @param activityId
	 */
	public static String postCrowdMoneyDetailed(String paymentState, String activityId) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAYMENTSTATE, paymentState);
		params.put(Constant.ACTIVITYID, activityId);

		return JSON.toJSONString(params);
	}

	/**筹资参与列表
	 * @param activityId
	 */
	public static String postCrowdFans(String activityId) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.ACTIVITYID, activityId);

		return JSON.toJSONString(params);
	}

	/**粉丝列表
	 * @param starUuid
	 */
	public static String postFansList(String starUuid) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.STARSUUID, starUuid);

		return JSON.toJSONString(params);
	}

	/**查询优惠券
	 * @param couponStates
	 */
	public static String postCardList(String[] couponStates) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.CARDSTATES, couponStates);

		return JSON.toJSONString(params);
	}

	/**查询门票
	 * @param paymentState
	 * @param parts
	 */
	public static String postTicketList(String paymentState, String parts) {

		Map<String,Object> params = new HashMap<>();
		params.put(Constant.PAYMENTSTATE, paymentState);
		params.put(Constant.PARTS, parts);

		return JSON.toJSONString(params);
	}
}