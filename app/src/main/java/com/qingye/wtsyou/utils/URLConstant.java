package com.qingye.wtsyou.utils;

/**
 * Created by pm89 on 2018/4/2.
 */

public class URLConstant {
    //登录
    public static final String LOGIN = "AppLoginService/login";
    //注册
    public static final String REGISTER = "AppLoginService/register";
    //注销登录
    public static final String LOGINOUT =  "AppLoginService/logout";
    //验证码
    public static final String VERIFYCODE = "AppLoginService/getAuthCode/";
    //第一次登录
    public static final String ISFIRSTLOGIN = "AppMemberService/isFirstLogin";


    //全部明星
    public static final String ALLSTARS = "AppStarService/query";
    //粉丝关注明星
    public static final String FOCUSSTARS = "AppFunsService/follows";
    //粉丝取消关注
    public static final String CANCELFOCUSSTARS = "AppFunsService/cancel";
    //粉丝已关注的明星
    public static final String FOCUEDSTARS = "AppFunsService/query";


    //获取实时明星排行榜
    public static final String STARSRANKING = "AppHitRankingListService/queryStarRankingList";
    //获取实时粉丝贡献榜
    public static final String FANSRANKING = "AppHitRankingListService/queryFunsRankingList";
    //往期明星排行榜
    public static final String HISTORYSTARSWEEKRANKING = "AppHitRankingListService/queryStarWeekRankingList";
    //往期粉丝贡献榜
    public static final String HISTORYFANSWEEKRANKING = "AppHitRankingListService/queryFunsWeekRankingList";
    //往期指定周期明星排行榜
    public static final String HISTORYSTARSRANKING = "AppHitRankingListService/queryHisStroyRankingListForStar";
    //往期指定周期粉丝贡献榜
    public static final String HISTORYFANSRANKING = "AppHitRankingListService/queryHisStroyRankingListForFuns";


    //打榜
    public static final String HIT = "AppHitService/hit";
    //首页
    public static final String HOMELIST = "AppHomeService/getHomeList";
    //首页明星粉丝列表
    public static final String FANSLIST = "AppFunsService/query";


    //根据条件查询活动
    public static final String CAMPAIGNQUERY = "AppActivityService/query";
    //活动首页
    public static final String CAMPAIGNHOME = "AppActivityService/getActivityHome";
    //投票查询
    public static final String VOTEQUERY = "AppVoteActivityService/query";
    //众筹查询
    public static final String CROWDQUERY = "AppCrowdActivityService/query";
    //演唱会查询
    public static final String CONCERTQUERY = "AppConcertActivityService/query";
    //应援查询
    public static final String SUPPORTQUERY = "AppSupportActivityService/query";
    //发起投票
    public static final String CREATEVOTE = "AppVoteActivityService/create";
    //发起应援
    public static final String CREATESUPPORT = "AppSupportActivityService/save";
    //获取指定投票详情
    public static final String VOTEDETIALED = "AppVoteActivityService/get/";
    //投票
    public static final String VOTE = "AppVoteActivityService/vote/";
    //应援
    public static final String SUPPORT = "AppSupportActivityService/support/";
    //获取指定众筹详情
    public static final String CROWDDETIALED= "AppCrowdActivityService/get/";
    //获取众筹金钱明细
    public static final String CROWDMONEYDETAILED = "AppOrderService/queryCrowdList";
    //获取众筹人数明细
    public static final String CROWDFANSDETAILED = "AppCrowdActivityService/queryCrowdDetail";
    //获取售票详情
    public static final String SALEDETIALED= "AppConcertActivityService/get/";
    //获取应援类型
    public static final String SUPPORTTYPE = "AppActivityService/getActivityType/";
    //获取指定应援详情
    public static final String SUPPORTETIALED = "AppSupportActivityService/get/";


    //充值前获取后台获取配置信息
    public static final String GETPAYMENTCONFIG = "AppDepositService/apply";
    //订单前获取后台获取配置信息
    public static final String GETPAYMENTCONFIGORDER = "AppOrderService/apply";
    //提现请求
    public static final String WITHDRAW = "AppWithdrawalsService/apply";
    //爱心查询
    public static final String HEARTQUERY =  "AppLoveLedgerService/query";
    //钻石查询
    public static final String DIAMONDQUERY = "AppDiamondLedgerService/query";
    //金币查询
    public static final String COINQUERY = "AppGoldLedgerService/query";
    //我参与的投票
    public static final String MYVOTEQUERY = "AppVoteActivityService/queryMyVoteActivity";
    //我参与的众筹
    public static final String MYCROWDQUERY = "AppCrowdActivityService/queryMyCrowdActivity";
    //我参与的演唱会
    public static final String MYCONCERTQUERY = "AppConcertActivityService/queryMyConcertActivity";
    //我参与的应援
    public static final String MYSUPPORTQUERY = "AppSupportActivityService/queryMySupportActivity";


    //获取签到记录
    public static final String GETSIGN = "AppSignService/get";
    //签到
    public static final String SIGN = "AppSignService/sign";
    //个人资料
    public static final String GETPERSONALMESSAGE = "AppMemberService/get";
    //修改个人资料
    public static final String MODIFYPERSONALMESSAGE = "AppMemberService/modify";


    //获取收货地址
    public static final String GETRECEIVINGADDRESS = "AppReceivingAddressService/getAll";
    //获取指定收货地址
    public static final String GETAPPOINTRECEIVEADDRESS = "AppReceivingAddressService/get";
    //添加、修改收货地址
    public static final String SAVERECEIVEADDRESS = "AppReceivingAddressService/save";
    //设置默认地址
    public static final String SETDEFAULTRECEIVEADDRESS = "AppReceivingAddressService/setDefault";
    //删除收货地址
    public static final String DELETERECEIVEADDRESS = "AppReceivingAddressService/remove";

    //卡券
    public static final String CARDQUERY = "AppCouponService/queryCoupons";


    //门票
    public static final String TICKETQUERY = "AppOrderService/query";


    //反馈
    public static final String FEEDBACK = "AppFeedbackService/feedback";


    //获取七牛token
    public static final String GETQINIUTOKEN = "AppQiniuService/st";
}
