package com.qingye.wtsyou.fragment.conversation;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.conversation.ConversationTopAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.fragment.campaign.ActivityHotShowFragment;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.conversation.ConversationTopView;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationTopFragment extends BaseHttpRecyclerFragment<ChatingRoom,ConversationTopView,ConversationTopAdapter> implements CacheCallBack<ChatingRoom> {

    private HttpModel<EntityPageData> mEntityPageDataHttpModel;

    private  List<ChatingRoom> chatingRoomList =  new ArrayList<>();

    private CustomDialog progressBar;

    String name = null;
    String ownerId = null;
    String adminUserId = null;
    String personId = null;
    String status = null;
    String topRecommend = "true";
    String hotRecommend = null;
    String desc = "false";
    String excludeMyself = null;
    String sortKey = null;

    int size = 0;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static ActivityHotShowFragment createInstance() {

        return new ActivityHotShowFragment();
    }

    public ConversationTopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_converesation_top);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //聊天室列表
        mEntityPageDataHttpModel = new HttpModel<>(EntityPageData.class);
        conversationQuery();

        initCache(this);

        //获取传来的数据
        /*Bundle bundle = getArguments();
        chatingRoomList = (List<ChatingRoom>) bundle.getSerializable(Constant.TOPRECOMMEND);*/

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvBaseRecycler.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        setList(chatingRoomList);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
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
    public void setList(final List<ChatingRoom> list) {
        setList(new AdapterCallBack<ConversationTopAdapter>() {

            @Override
            public ConversationTopAdapter createAdapter() {
                return new ConversationTopAdapter(context);
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
    public List<ChatingRoom> parseArray(String json) {
        return null;
    }

    @Override
    public Class<ChatingRoom> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(ChatingRoom data) {
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

    public void conversationQuery() {
        setProgressBar();
        progressBar.show();

        //聊天室列表
        String request = HttpRequest.postConversationQueryList(name, null, ownerId, adminUserId, personId, status,
                topRecommend,hotRecommend, desc, excludeMyself, sortKey);
        mEntityPageDataHttpModel.post(request, URL_BASE + URLConstant.CONVERSATIONQUERY,1,this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                EntityPageData.Content entityPageDatafirst = mEntityPageDataHttpModel.getData().getContent();
                chatingRoomList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageDatafirst.getData())
                        ,new TypeToken<List<ChatingRoom>>(){}.getType());
                /*if (chatingRoomList.size() < 6) {
                    size = 6 - chatingRoomList.size();

                    sortKey = "userCount";
                    //聊天室列表
                    String request = HttpRequest.postConversationQueryList(name, null, ownerId, adminUserId, personId, status,
                            topRecommend,hotRecommend, desc, excludeMyself, sortKey);
                    mEntityPageDataHttpModel.post(request, URL_BASE + URLConstant.CONVERSATIONQUERY,2,this);
                } else {*/
                    setList(chatingRoomList);
                //}
                break;
            /*case 2:
                hotRecommend = "false";
                EntityPageData.Content entityPageDataSecond = mEntityPageDataHttpModel.getData().getContent();
                List<ChatingRoom> chatingRooms = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageDataSecond.getData())
                        ,new TypeToken<List<ChatingRoom>>(){}.getType());
                if (chatingRooms.size() < size) {
                    chatingRoomList.addAll(chatingRooms);
                } else {
                    for (int i = 0; i < size; i ++) {
                        chatingRoomList.add(chatingRooms.get(i));
                    }
                }
                setList(chatingRoomList);

                break;*/
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

}
