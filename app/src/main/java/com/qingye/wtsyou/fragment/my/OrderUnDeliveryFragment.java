package com.qingye.wtsyou.fragment.my;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.my.OrderDetailedActivity;
import com.qingye.wtsyou.adapter.my.OrderDetailedAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityOrder;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.my.OrderView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class OrderUnDeliveryFragment extends BaseHttpRecyclerFragment<EntityOrder,OrderView,OrderDetailedAdapter>
        implements CacheCallBack<EntityOrder>, OnHttpPageCallBack<EntityPageData, EntityOrder>,
        OrderView.OnItemChildClickListener{

    private OrderDetailedAdapter orderDetailedAdapter;

    private LinearLayout noMessage;

    private HttpPageModel<EntityPageData, EntityOrder> mEntityPageDataHttpModel;

    private CustomDialog progressBar;

    private List<EntityOrder> orderDetailedList = new ArrayList<>();

    private String mobile;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static OrderUnDeliveryFragment createInstance() {

        return new OrderUnDeliveryFragment();
    }

    public OrderUnDeliveryFragment() {
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
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        orderQuery();

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

        noMessage = findViewById(R.id.noMessage);
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

    @Override
    public void setList(final List<EntityOrder> list) {

        setList(new AdapterCallBack<OrderDetailedAdapter>() {

            @Override
            public OrderDetailedAdapter createAdapter() {
                orderDetailedAdapter = new OrderDetailedAdapter(context);
                orderDetailedAdapter.setOnItemChildClickListener(OrderUnDeliveryFragment.this);

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

    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        toActivity(OrderDetailedActivity.createIntent(context, orderDetailedList.get(position).getId()));
    }

    public void orderQuery() {
        /*setProgressBar();
        progressBar.show();*/

        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.ORDERQUERY, this);
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
        String state = "unShip";
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
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.ORDERQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //订单列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.ORDERQUERY, this);
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

    }

    @Override
    public void onTakeClick(View view, int position) {

    }
}
