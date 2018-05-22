package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.adapter.my.DeliveryAddressAdapter;
import com.qingye.wtsyou.basemodel.EntityBase;
import com.qingye.wtsyou.modle.DeliveryAddress;
import com.qingye.wtsyou.modle.EntityContent;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.my.DeliveryAddressView;
import com.qingye.wtsyou.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

public class AddressActivity extends BaseHttpRecyclerActivity<DeliveryAddress,DeliveryAddressView,DeliveryAddressAdapter> implements View.OnClickListener,OnBottomDragListener,
        DeliveryAddressView.OnItemChildClickListener{

    private ImageView ivLeft;
    private TextView tvHead;
    private Button btnCreate;

    private DeliveryAddressAdapter deliveryAddressAdapter;

    String deleteUuid = null;

    private RelativeLayout relativelayout;
    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private List<DeliveryAddress> deliveryAddressList = new ArrayList<>();

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
            return new Intent(context, AddressActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
            return this; //必须return this;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.ACTION_ADDRESS_REFRESH)) {
                getAddress();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_ADDRESS_REFRESH);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        getAddress();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部
    }

    @Override
    public void initView() {
        super.initView();

        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("我的收货地址");
        btnCreate = findViewById(R.id.btn_create_address);

        relativelayout = findViewById(R.id.relativelayout);
        swipeRefresh = findViewById(R.id.swipe_refresh_widget);
    }

    public void onResume() {
        //getAddress();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);

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

    //刷新
    private void initHrvsr(){
        //设置刷新时动画的颜色，可以设置4个
        swipeRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("swipeRefresh", "invoke onRefresh...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAddress();
                        showShortToast(R.string.getSuccess);
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
        // 设置子视图是否允许滚动到顶部
        swipeRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return relativelayout.getScrollY() > 0;
            }
        });
    }

    @Override
    public void setList(final List<DeliveryAddress> list) {

        setList(new AdapterCallBack<DeliveryAddressAdapter>() {

            @Override
            public DeliveryAddressAdapter createAdapter() {

                deliveryAddressAdapter = new DeliveryAddressAdapter(context);
                deliveryAddressAdapter.setOnItemChildClickListener(AddressActivity.this);

                return deliveryAddressAdapter;
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });

    }

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
                            deleteAddress(deleteUuid);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Override
    public void initData() {//必须调用
        super.initData();

    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<DeliveryAddress> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        ivLeft.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_create_address:
                toActivity(CreateAddressActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    public void getAddress() {
        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postGetAddress(0, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityContent entityContent =  JSON.parseObject(resultJson,EntityContent.class);
                        if(entityContent.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);
                            deliveryAddressList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityContent.getContent())
                                    ,new TypeToken<List<DeliveryAddress>>(){}.getType());

                            setList(deliveryAddressList);

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entityContent.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityContent.getMessage());
                            }

                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);
        }
    }

    public void deleteAddress(String uuid) {

        setProgressBar();
        progressBar.show();

        //检查网络
        if (NetUtil.checkNetwork(this)) {

            HttpRequest.postDeleteAddress(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功//showShortToast(R.string.getSuccess);
                            showShortToast(R.string.deleteAddressSuccess);

                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_ADDRESS_REFRESH);
                            // 发送广播
                            sendBroadcast(intent);

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entityBase.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityBase.getMessage());
                            }

                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);
        }

    }

    public void setDefaultAddress(String uuid) {

        //检查网络
        if (NetUtil.checkNetwork(this)) {

            HttpRequest.postSetDefaultAddress(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功//showShortToast(R.string.getSuccess);

                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_ADDRESS_REFRESH);
                            // 发送广播
                            sendBroadcast(intent);
                        }else{//显示失败信息
                            if (entityBase.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityBase.getMessage());
                            }

                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);
        }

    }

    @Override
    public void onEditClick(View view, int position) {
        toActivity(EditAddressActivity.createIntent(context, deliveryAddressList.get(position)));
    }

    @Override
    public void onDeleteClick(View view, int position) {

        deleteUuid = deliveryAddressList.get(position).getUuid();
        toActivity(BottomMenuWindow.createIntent(context, SEX_ITEM)
                .putExtra(BottomMenuWindow.INTENT_TITLE, "是否删除该地址？"), REQUEST_TO_BOTTOM_MENU, false);
    }

    @Override
    public void onDefaultClick(View view, int position) {
        for (int i = 0;i < deliveryAddressList.size();i ++) {
            if (i == position) {
                deliveryAddressList.get(i).setDefault(true);
                setDefaultAddress(deliveryAddressList.get(position).getUuid());
            } else {
                deliveryAddressList.get(i).setDefault(false);
            }
        }
        setList(deliveryAddressList);
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }
}
