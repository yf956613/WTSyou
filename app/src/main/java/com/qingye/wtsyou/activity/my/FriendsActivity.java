package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.FansMainActivity;
import com.qingye.wtsyou.adapter.search.SearchFansAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.Fans;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.search.SearchFansView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class FriendsActivity extends BaseHttpRecyclerActivity<Fans,SearchFansView,SearchFansAdapter> implements
        View.OnClickListener, OnBottomDragListener, SearchFansView.OnItemChildClickListener, OnHttpPageCallBack<EntityPageData, Fans> {

    private ImageView ivBack;
    private TextView tvHead;
    private EditText edtSearch;
    private RelativeLayout rlCancel;

    private LinearLayout noMessage;

    private CustomDialog progressBar;

    private HttpPageModel<EntityPageData, Fans> mEntityPageDataHttpModel;

    private List<Fans> friendsList = new ArrayList<>();

    private SearchFansAdapter searchFansAdapter;

    private String keywords = null;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
            return new Intent(context, FriendsActivity.class);
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
            if (action.equals(BroadcastAction.ACTION_FRIEND_REFRESH)) {
                friendsQuery();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fans,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_FRIEND_REFRESH);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        //好友列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        friendsQuery();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(true);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部*/
    }

    @Override
    public void initView() {
        super.initView();
        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("我的好友");

        edtSearch = findView(R.id.edit_search_content);
        edtSearch.setHint("搜索好友");
        if (edtSearch.getTag() instanceof TextWatcher) {
            edtSearch.removeTextChangedListener((TextWatcher) edtSearch.getTag());
        }
        TextWatcher watcher = new TextWatcher() {
            private CharSequence temp ;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s ;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {

                } else {
                    keywords = null;
                    friendsQuery();
                }
            }
        };
        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEND
                        || keyCode == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    // do some your things
                    if (edtSearch.getText().toString().isEmpty()) {
                        keywords = null;
                        friendsQuery();
                    } else {
                        keywords = edtSearch.getText().toString().trim();
                        friendsQuery();
                    }
                }
                return false;
            }
        });
        rlCancel = findView(R.id.rl_cancel);

        noMessage = findView(R.id.noMessage);
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

    @Override
    public void setList(final List<Fans> list) {

        setList(new AdapterCallBack<SearchFansAdapter>() {

            @Override
            public SearchFansAdapter createAdapter() {
                searchFansAdapter = new SearchFansAdapter(context);
                searchFansAdapter.setOnItemChildClickListener(FriendsActivity.this);

                return searchFansAdapter;
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
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
    public List<Fans> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        ivBack.setOnClickListener(this);
        rlCancel.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.rl_cancel:
                keywords = null;
                edtSearch.setText("");
                //好友列表
                mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.MYFOCUSFRIEND, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toActivity(FansMainActivity.createIntent(context, friendsList.get(position).getUserId()));
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

    public void focusFans(Fans fans, final TextView focus, final TextView can) {

        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getGetFocusFriend(0, fans.getUserId(), new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.focusSuccess);

                            focus.setVisibility(View.GONE);
                            can.setVisibility(View.VISIBLE);

                            progressBarDismiss();

                        }else{//显示失败信息

                            showShortToast(entityBase.getMessage());
                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });

        } else {
            showProgressDialog(R.string.checkNetwork);
        }
    }

    public void cancelFans(Fans fans, final TextView focus, final TextView can) {

        if (NetUtil.checkNetwork(context)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getGetCancelFocusFriend(0, fans.getUserId(), new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.cancelFocusSuccess);

                            focus.setVisibility(View.VISIBLE);
                            can.setVisibility(View.GONE);

                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_FRIEND_REFRESH);
                            // 发送广播
                            sendBroadcast(intent);

                            progressBarDismiss();

                        }else{//显示失败信息

                            showShortToast(entityBase.getMessage());
                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });

        } else {
            showProgressDialog(R.string.checkNetwork);
        }
    }

    public void friendsQuery() {
        /*setProgressBar();
        progressBar.show();*/

        //好友列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.MYFOCUSFRIEND, this);
    }

    public List<Fans> setFollow(List<Fans> fans) {
        for (int i = 0;i < fans.size();i ++) {
            fans.get(i).setFollow(true);
        }

        return fans;
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<Fans> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<Fans>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String request = HttpRequest.postKeywordPageData(keywords, page, pageSize);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();

        if (friendsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshSuccessPagingList(List<Fans> list) {
        friendsList.clear();
        friendsList.addAll(setFollow(list));
        setList(friendsList);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        if (friendsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();

        if (friendsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreSuccessPagingList(List<Fans> list) {
        friendsList.addAll(setFollow(list));
        setList(friendsList);
        srlBaseHttpRecycler.finishLoadmore();

        if (friendsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (friendsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (friendsList.size() > 0) {
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
        //已关注的明星列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.MYFOCUSFRIEND, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //已关注的明星列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.MYFOCUSFRIEND, this);
    }

    @Override
    public void onFocus(View view, int position, TextView focus, TextView rank) {
        focusFans(friendsList.get(position),focus,rank);
    }

    @Override
    public void onCancelFocus(View view, int position, TextView focus, TextView rank) {
        cancelFans(friendsList.get(position),focus,rank);
    }
}
