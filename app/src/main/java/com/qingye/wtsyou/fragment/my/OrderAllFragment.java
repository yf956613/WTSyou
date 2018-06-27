package com.qingye.wtsyou.fragment.my;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.my.OrderDetailedActivity;
import com.qingye.wtsyou.adapter.my.OrderDetailedAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityClassContent;
import com.qingye.wtsyou.model.EntityOrder;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.EntityRepay;
import com.qingye.wtsyou.model.EntityStringContent;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.my.OrderView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class OrderAllFragment extends BaseHttpRecyclerFragment<EntityOrder,OrderView,OrderDetailedAdapter>
        implements CacheCallBack<EntityOrder>, OnHttpPageCallBack<EntityPageData, EntityOrder>,
        OrderView.OnItemChildClickListener, View.OnClickListener {

    private OrderDetailedAdapter orderDetailedAdapter;

    private LinearLayout noMessage;

    private View popUpView;
    private PopupWindow payWayWin;
    private RadioGroup radioGroup;
    private RadioButton rbtWei,rbtAli;
    private TextView tvSelect;
    private Button btnClose;//支付弹窗

    private HttpPageModel<EntityPageData, EntityOrder> mOrderListHttpModel;
    private HttpModel<EntityClassContent> mCancelHttpModel;
    private HttpModel<EntityRepay> mRepayHttpModel;
    private HttpModel<EntityStringContent> mTakeHttpModel;

    private CustomDialog progressBar;

    private List<EntityOrder> orderDetailedList = new ArrayList<>();

    private String mobile;
    private String cancelId = null;
    private String rePayId = null;
    private String takeId = null;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static OrderAllFragment createInstance() {

        return new OrderAllFragment();
    }

    public OrderAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_order);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //订单列表
        mOrderListHttpModel = new HttpPageModel<>(EntityPageData.class);
        orderQuery();
        //取消订单
        mCancelHttpModel = new HttpModel<>(EntityClassContent.class);
        //付款
        mRepayHttpModel = new HttpModel<>(EntityRepay.class);
        //收货
        mTakeHttpModel = new HttpModel<>(EntityStringContent.class);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部*/

        return view;
    }

    @Override
    public void initView() {
        super.initView();

        noMessage = findView(R.id.noMessage);

        popUpView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_pay_way, null);
        payWayWin = new PopupWindow(popUpView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        payWayWin.setOutsideTouchable(true);
        radioGroup = (RadioGroup) popUpView.findViewById(R.id.radio_group);
        rbtWei = (RadioButton) popUpView.findViewById(R.id.rbtn_wei);
        rbtAli = (RadioButton) popUpView.findViewById(R.id.rbtn_ali);
        tvSelect = (TextView) popUpView.findViewById(R.id.tv_select);
        btnClose = (Button) popUpView.findViewById(R.id.btn_close);
        radioGroup.setOnCheckedChangeListener(new WayGrouplistener());
    }

    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }

            progressBar = null;
        }
    }

    private void setProgressBar() {
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void progressBarDismiss() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
                progressBar.cancel();
            }
        }
    }

    //radiobutton按钮监听
    class WayGrouplistener implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == rbtWei.getId()) {
                rbtWei.setChecked(true);
            }
            if (checkedId == rbtAli.getId()) {
                rbtAli.setChecked(true);
            }
        }
    }

    @Override
    public void setList(final List<EntityOrder> list) {

        setList(new AdapterCallBack<OrderDetailedAdapter>() {

            @Override
            public OrderDetailedAdapter createAdapter() {
                orderDetailedAdapter = new OrderDetailedAdapter(context);
                orderDetailedAdapter.setOnItemChildClickListener(OrderAllFragment.this);

                return orderDetailedAdapter;
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }

    @Override
    public void getListAsync(int page) {

    }

    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private static final String[] SEX_ITEM = {"取消", "确定"};

    private static final int REQUEST_TO_BOTTOM_MENU = 31;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_BOTTOM_MENU:
                if (data != null) {
                    int selectedPosition = data.getIntExtra(BottomMenuWindow.RESULT_ITEM_ID, -1);
                    if (selectedPosition >= 0 && selectedPosition < SEX_ITEM.length) {
                        if (selectedPosition == 1) {
                            cancelOrder();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initData() {//必须调用
        super.initData();

    }

    @Override
    public List<EntityOrder> parseArray(String json) {
        return null;
    }

    @Override
    public Class<EntityOrder> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(EntityOrder data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 0;
    }


    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    @Override
    public void initEvent() {//必须调用
        super.initEvent();

        tvSelect.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        toActivity(OrderDetailedActivity.createIntent(context, orderDetailedList.get(position).getId()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select:
                if (rbtWei.isChecked()) {

                }
                else if (rbtAli.isChecked()) {
                    //检查网络
                    if (NetUtil.checkNetwork(context)) {
                        rePayOrder();
                    } else {
                        showShortToast(R.string.checkNetwork);
                    }
                }
                payWayWin.dismiss();
                break;
            case R.id.btn_close:
                payWayWin.dismiss();
                break;
        }
    }

    public void orderQuery() {
        /*setProgressBar();
        progressBar.show();*/

        mOrderListHttpModel.refreshPost(URL_BASE + URLConstant.ORDERQUERY, this);
    }

    public void cancelOrder() {
        String request = HttpRequest.postCancelOrder(cancelId);
        mCancelHttpModel.post(request, URL_BASE + URLConstant.ORDERCANCEL, 1,this);
    }

    public void rePayOrder() {
        String[] perms = {android.Manifest.permission.READ_PHONE_STATE,};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            String request = HttpRequest.postNoParams();
            mRepayHttpModel.post(request, URL_BASE + URLConstant.ORDERREPAY + rePayId, 2,this);
        } else {
            EasyPermissions.requestPermissions(this, "支付需要手机。。。。权限",
                    0, perms);
        }
    }

    public void takeOrder() {
        String request = HttpRequest.postNoParams();
        mTakeHttpModel.post(request, URL_BASE + URLConstant.ORDERTAKE + takeId, 3,this);
    }

    public static final int SDK_PAY_FLAG = 100;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //showShortToast("支付回调=>>>" + msg);
            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    break;
                }
                default:
                    break;
            }
        }
    };

    private void aliPay(final String parameters) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    showShortToast("获取支付参数成功，开始调用支付");

                    if (parameters == null) {
                        showShortToast("未调用支付，支付参数为空");
                        return;
                    }
                    PayTask alipay = new PayTask(getActivity());
                    Map<String, String> result = alipay.payV2(parameters, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<EntityOrder> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<EntityOrder>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String state = null;
        String paymentStates[] = null;
        String parts = "activity";
        Boolean showFail = true;

        String request = HttpRequest.postOrderList(state, paymentStates, parts,
                showFail, page, pageSize);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();

        if (orderDetailedList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshSuccessPagingList(List<EntityOrder> list) {
        orderDetailedList.clear();

        orderDetailedList.addAll(list);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(orderDetailedList);

        if (orderDetailedList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();

        if (orderDetailedList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreSuccessPagingList(List<EntityOrder> list) {
        orderDetailedList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        setList(orderDetailedList);

        if (orderDetailedList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (orderDetailedList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (orderDetailedList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        //订单列表
        mOrderListHttpModel.refreshPost(URL_BASE + URLConstant.ORDERQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //订单列表
        mOrderListHttpModel.loadMorePost(URL_BASE + URLConstant.ORDERQUERY, this);
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobile));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] granResults) {
        switch (requestCode) {
            case 1:
                if (granResults.length > 0 && granResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    showShortToast("您没有打开拨号权限");
                }
                break;
            default:
        }
    }

    @Override
    public void onCancelClick(View view, int position) {
        cancelId = orderDetailedList.get(position).getId();
        toActivity(BottomMenuWindow.createIntent(context, SEX_ITEM)
                .putExtra(BottomMenuWindow.INTENT_TITLE, "是否取消该订单？"), REQUEST_TO_BOTTOM_MENU, false);
    }

    @Override
    public void onCallClick(View view, int position) {
        mobile = "10086";
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CALL_PHONE},1);
        }else {
            call();
        }
    }

    @Override
    public void onRemindClick(View view, int position) {
        showShortToast(R.string.remindSuccess);
    }

    @Override
    public void onPayClick(View view, int position) {
        rePayId = orderDetailedList.get(position).getId();
        payWayWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        payWayWin.update(0, 0, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onTakeClick(View view, int position) {
        takeId = orderDetailedList.get(position).getId();
        takeOrder();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                EntityClassContent.Content entityClassContent = mCancelHttpModel.getData().getContent();
                showShortToast(entityClassContent.getMessage());
                orderQuery();
                break;
            case 2:
                EntityRepay.Content repay = mRepayHttpModel.getData().getContent();
                String parameters = repay.getParameters().get(0).getValue();
                aliPay(parameters);
                orderQuery();
                break;
            case 3:
                showShortToast(R.string.takeSuccess);
                break;
        }
    }
}
