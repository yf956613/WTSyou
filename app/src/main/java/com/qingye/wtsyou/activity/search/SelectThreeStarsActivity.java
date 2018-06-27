package com.qingye.wtsyou.activity.search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.home.SelectStarsAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.fragment.home.SelectedStarsFragment;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.home.SelectStarsView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class SelectThreeStarsActivity extends BaseHttpRecyclerActivity <EntityStars,SelectStarsView,SelectStarsAdapter> implements
        View.OnClickListener, View.OnLongClickListener, OnBottomDragListener, OnHttpPageCallBack<EntityPageData, EntityStars> {

    private ImageView ivBack,ivSearch;
    private TextView tvHead;
    private TextView tvConfirm;

    private HttpPageModel<EntityPageData, EntityStars> mEntityPageDataHttpModel;

    //全部明星
    private List<EntityStars> starsList = new ArrayList<>();
    //选择的明星，包含重新选择
    private Map<String,EntityStars> MapselectedStars = new HashMap<>();
    private List<EntityStars> selectedStars = new ArrayList<>();

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context,List<EntityStars> selectStars) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SELECTED_STARS, (Serializable) selectStars);//放进数据流中
        return new Intent(context, SelectThreeStarsActivity.class).putExtras(bundle);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stars,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        selectedStars = (List<EntityStars>) intent.getSerializableExtra(Constant.SELECTED_STARS);

        //明星列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        mEntityPageDataHttpModel.setPageSize(24);
        allStars();

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

        //实例化一个GridLayoutManager，列数为3
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rvBaseRecycler.setLayoutManager(layoutManager);

    }

    @Override
    public void initView() {
        super.initView();

        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.backwirtih);
        ivSearch = findView(R.id.iv_right);
        ivSearch.setImageResource(R.mipmap.search);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("选择明星");
        tvConfirm = findView(R.id.tv_confirm);
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

        setList(new AdapterCallBack<SelectStarsAdapter>() {

            @Override
            public SelectStarsAdapter createAdapter() {
                return new SelectStarsAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void getListAsync(final int page) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //allStars();
            }
        }, 1000);
    }

    @Override
    public List<EntityStars> parseArray(String json) {
        return starsList;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(SearchStarsActivity.createIntent(context));
                break;
            case R.id.tv_confirm:
                if (MapselectedStars.size() > 0 && MapselectedStars.size() < 4) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.SELECTED_STARS, (Serializable) selectedStars);//放进数据流中

                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);

                    finish();
                }

                break;
            default:
                break;
        }

    }

    //类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private static final int REQUEST_TO_SELECT_STARS = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_SELECT_STARS:
                if (data != null) {
                    EntityStars stars = (EntityStars) data.getExtras().getSerializable(Constant.SELECTED_STARS);
                    for (int i = 0;i < starsList.size();i ++) {
                        if (starsList.get(i).getUuid().equals(stars.getUuid())) {
                            rvBaseRecycler.scrollToPosition(i);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    //传递选择的明星
    public void selectedStars(List<EntityStars> selectStars) {
        SelectedStarsFragment selectedStarsFragment = new SelectedStarsFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SELECTED_STARS, (Serializable) selectStars);
        selectedStarsFragment.setArguments(bundle);
        //把碎片添加到碎片中
        transaction.replace(R.id.list_selected_stars,selectedStarsFragment);
        transaction.commitAllowingStateLoss();
    }

    //更新选择的明星
    public void getSelectedStars() {
        //selectedStars更新后的map
        selectedStars.clear();
        for (Map.Entry<String, EntityStars> entry : MapselectedStars.entrySet()) {
            selectedStars.add(entry.getValue());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //此处可选择多个明星
        if (starsList.get(position).getIsFollow()) {
            starsList.get(position).setIsFollow(false);
        } else {
            starsList.get(position).setIsFollow(true);
        }

        //更新界面
        setList(starsList);

        //选择的明星
        if (starsList.get(position).getIsFollow()) {
            MapselectedStars.put(starsList.get(position).getUuid(),starsList.get(position));
        } else {//移除的时候看看有没有已经在里面，因为上一个界面会传值过来
            MapselectedStars.remove(starsList.get(position).getUuid());
        }

        if (MapselectedStars.size() < 1) {
            showShortToast(R.string.leastOne);
            starsList.get(position).setIsFollow(true);
            MapselectedStars.put(starsList.get(position).getUuid(),starsList.get(position));
        }
        else if (MapselectedStars.size() > 3){
            showShortToast(R.string.mostThree);
            starsList.get(position).setIsFollow(false);
            MapselectedStars.remove(starsList.get(position).getUuid());
        }

        putSelectedMap(starsList);
        getSelectedStars();
        selectedStars(selectedStars);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
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

    private void putSelectedMap(List<EntityStars> list) {
        for (EntityStars entityStars : list) {

            if (entityStars.getIsFollow()) {
                MapselectedStars.put(entityStars.getUuid(),entityStars);
            }
        }
    }

    public void allStars() {
        /*setProgressBar();
        progressBar.show();*/

        //明星列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.ALLSTARS, this);
    }

    public List<EntityStars> setDefault(List<EntityStars> starsList) {
        for (int i = 0;i < starsList.size();i ++) {
            starsList.get(i).setIsFollow(false);

            if (selectedStars.size() > 0) {
                for (int j = 0;j < selectedStars.size();j ++) {
                    if (selectedStars.get(j).getUuid().equals(starsList.get(i).getUuid())) {
                        starsList.get(i).setIsFollow(true);
                        MapselectedStars.put(starsList.get(i).getUuid(),starsList.get(i));
                    }
                }
            }
        }

        return starsList;
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
        String keywords = null;
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

        starsList.addAll(setDefault(list));
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(starsList);
        selectedStars(selectedStars);

    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
    }

    @Override
    public void loadMoreSuccessPagingList(List<EntityStars> list) {
        starsList.addAll(setDefault(list));
        srlBaseHttpRecycler.finishLoadmore();

        setList(starsList);
        selectedStars(selectedStars);

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
