package com.qingye.wtsyou.activity.search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.search.HistoryStarsSearchAdapter;
import com.qingye.wtsyou.adapter.search.SearchStarsAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.SPUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.search.SearchStarsView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class SearchStarsActivity extends BaseHttpRecyclerActivity<EntityStars,SearchStarsView,SearchStarsAdapter> implements
        View.OnClickListener, OnBottomDragListener, OnHttpPageCallBack<EntityPageData, EntityStars> {

    private EditText edtContent;
    private RelativeLayout rlCancel;
    private TextView tvEmpty;
    private LinearLayout llSearchList;
    private ConstraintLayout clHistory;

    private String keywords = "";

    //全部明星
    private List<EntityStars> starsList = new ArrayList<>();

    private HttpPageModel<EntityPageData, EntityStars> mEntityPageDataHttpModel;

    private CustomDialog progressBar;

    //搜索历史
    private RecyclerView rclHistory;
    private  HistoryStarsSearchAdapter historyStarsSearchAdapter;
    List<String> history = new ArrayList<>();
    private boolean SP = true;
    Gson gson = new Gson();

    private int count = 0;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
            return new Intent(context, SearchStarsActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
            return this; //必须return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stars,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //明星列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setScrollEnabled(false);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);

        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(true);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部*/
    }

    @Override
    public void initView() {
        super.initView();
        llSearchList = findView(R.id.list_search_content);
        clHistory = findView(R.id.clHistory);

        edtContent = findView(R.id.edit_search_content);
        edtContent.setHint("搜索爱豆");
        if (edtContent.getTag() instanceof TextWatcher) {
            edtContent.removeTextChangedListener((TextWatcher) edtContent.getTag());
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
                    llSearchList.setVisibility(View.GONE);
                    clHistory.setVisibility(View.VISIBLE);
                }
            }
        };
        edtContent.addTextChangedListener(watcher);
        edtContent.setTag(watcher);
        edtContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEND
                        || keyCode == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    // do some your things
                    if (edtContent.getText().toString().isEmpty()) {
                        showShortToast(R.string.noContent);
                    } else {
                        if (history.size() > 0) {
                            for (int i = 0;i < history.size();i ++) {
                                if (history.get(i).equals(edtContent.getText().toString().trim())) {
                                    count = 0;
                                    break;
                                } else {
                                    count = count + 1;
                                    break;
                                }
                            }
                        } else {
                            count = count + 1;
                        }

                        if (count != 0) {
                            history.add(edtContent.getText().toString().trim());
                        }

                        historyStarsSearchAdapter.notifyDataSetChanged();
                        allStars();
                        add();
                    }
                }
                return false;
            }
        });

        rlCancel = findView(R.id.rl_cancel);
        tvEmpty = findView(R.id.tv_empty);

        SP = SPUtil.contains(this, Constant.STARSSEARCHHISTORY);
        if(SP == true) {
            String str = (String) SPUtil.get(this, Constant.STARSSEARCHHISTORY, "");
            if(str != null){
                history = gson.fromJson(str, new TypeToken<List<String>>(){}.getType());
            }
        }
        //搜索历史
        rclHistory = findView(R.id.list_history);
        rclHistory.setLayoutManager(new LinearLayoutManager(this));
        historyStarsSearchAdapter = new HistoryStarsSearchAdapter(history, this);
        rclHistory.setAdapter(historyStarsSearchAdapter);
        historyStarsSearchAdapter.setOnItemChildClick(new HistoryStarsSearchAdapter.OnItemChildClick() {
            @Override
            public void deleteClick(View v, int position) {
                history.remove(position);
                historyStarsSearchAdapter.notifyDataSetChanged();
                if (history.size() != 0) {
                    add();
                } else {
                    delete();
                }
            }

            @Override
            public void historyClick(View v, int position) {
                edtContent.setText(history.get(position));
                allStars();
            }
        });
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
    public void setList(final List<EntityStars> list) {
        setList(new AdapterCallBack<SearchStarsAdapter>() {

            @Override
            public SearchStarsAdapter createAdapter() {
                return new SearchStarsAdapter(context);
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
    public List<EntityStars> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        rlCancel.setOnClickListener(this);
        tvEmpty.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_cancel:
                if (history.size() > 0) {
                    add();
                } else {
                    delete();
                }

                finish();
                break;
            case R.id.tv_empty:
                delete();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SELECTED_STARS,starsList.get(position));//放进数据流中

        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
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

    public void add() {
        //利用Gson转化成Json Array数据
        String jsonStr = gson.toJson(history);
        SPUtil.put(this,Constant.STARSSEARCHHISTORY, jsonStr);
    }

    public void delete() {
        SPUtil.remove(this,Constant.STARSSEARCHHISTORY);
        history.clear();
        historyStarsSearchAdapter.notifyDataSetChanged();
    }

    public void allStars() {
        /*setProgressBar();
        progressBar.show();*/

        starsList.clear();
        setList(starsList);
        llSearchList.setVisibility(View.VISIBLE);
        clHistory.setVisibility(View.GONE);

        //明星列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.ALLSTARS, this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<EntityStars> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<EntityStars>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        keywords = edtContent.getText().toString();
        String request = HttpRequest.postKeywordsPageData(keywords, page, pageSize);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();
    }

    @Override
    public void refreshSuccessPagingList(List<EntityStars> list) {
        starsList.clear();

        starsList.addAll(list);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(starsList);
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
    }

    @Override
    public void loadMoreSuccessPagingList(List<EntityStars> list) {
        starsList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        setList(starsList);

    }

    @Override
    public void refreshErrorPagingList() {
        showShortToast(R.string.noReturn);
    }

    @Override
    public void loadMoreErrorPagingList() {
        showShortToast(R.string.noReturn);
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);

        count = 0;

        //明星列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.ALLSTARS, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //明星列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.ALLSTARS, this);
    }
}
