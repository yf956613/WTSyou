package com.qingye.wtsyou.fragment.campaign;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.campaign.CreateSupportActivity;
import com.qingye.wtsyou.activity.campaign.CreateVoteActivity;
import com.qingye.wtsyou.activity.search.SearchCampaignActivity;
import com.qingye.wtsyou.activity.campaign.ShowAllActivity;
import com.qingye.wtsyou.activity.campaign.SupportAllActivity;
import com.qingye.wtsyou.adapter.campaign.LoopShowContentAdapter;
import com.qingye.wtsyou.modle.ImageLoopContent;

import java.util.ArrayList;
import java.util.List;

import wzp.demo.imageloop.widget.ImageLoopViewPager;
import wzp.demo.imageloop.widget.PageIndicatorView;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {
    private RelativeLayout relativeLoopImage;
    private ImageLoopViewPager vpNews;
    private PageIndicatorView pageIndicatorView;

    private ImageView ivCreate;
    private Dialog dialog;//发起活动用全屏dialog展示
    private LinearLayout llSearch;
    private LinearLayout llShowMore,llSupportMore;

    private List<View> viewList = new ArrayList<>();
    private LoopShowContentAdapter loopAdapter;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_activity);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static ActivityFragment createInstance() {
        return new ActivityFragment();
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void initView() {

        relativeLoopImage = findViewById(R.id.relative_loopImage);
        vpNews = findViewById(R.id.vp_news);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);

        final List<ImageLoopContent> list = new ArrayList<>();
        for (int i = 1;i < 5;i ++) {
            ImageLoopContent imageContent = new ImageLoopContent();
            imageContent.setId(i);
            imageContent.setThumb("/img_h.png");
            list.add(imageContent);
        }

        //图片轮播改变ui需要开启新线程
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    View view = getLayoutInflater().inflate(R.layout.loop_show, null);
                    //final int j = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//点击事件

                        }
                    });
                    viewList.add(view);
                }
                pageIndicatorView.setCount(viewList.size());
                vpNews.setPage(viewList.size());

                loopAdapter = new LoopShowContentAdapter(context, viewList, list);
                vpNews.setAdapter(loopAdapter);
            }
        });

        ActivityOfficialFragment activityOfficialFragment = new ActivityOfficialFragment();
        ActivityHotShowFragment activityHotShowFragment = new ActivityHotShowFragment();
        ActivitySupportFragment activitySupportFragment = new ActivitySupportFragment();
        //注意这里是调用getChildFragmentManager()方法
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_official,activityOfficialFragment);
        transaction.replace(R.id.list_show,activityHotShowFragment);
        transaction.replace(R.id.list_support,activitySupportFragment);
        transaction.commit();

        ivCreate = findView(R.id.iv_create);
        llSearch = findView(R.id.ll_search);
        llShowMore = findViewById(R.id.ll_show_more);
        llSupportMore = findViewById(R.id.ll_support_more);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivCreate.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        llShowMore.setOnClickListener(this);
        llSupportMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_create:
                //创建对话框
                dialog = new Dialog(getContext(),R.style.dialog1);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.support_or_vote,null);
                //给Dialog中的子view设置事件监听
                view.findViewById(R.id.ll_support).setOnClickListener(this);
                view.findViewById(R.id.ll_vote).setOnClickListener(this);
                view.findViewById(R.id.rl_close).setOnClickListener(this);
                dialog.setContentView(view);
                //自定义宽高（高度一般不用调整，在xml调整好就可以了，这里我只调整了宽度）
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(params);
                //show之前设置返回键无效，触摸屏无效
                dialog.setCancelable(false);
                //显示对话框
                dialog.show();
                break;
            case R.id.ll_support:
                toActivity(CreateSupportActivity.createIntent(context));
                break;
            case R.id.ll_vote:
                toActivity(CreateVoteActivity.createIntent(context));
                break;
            case R.id.rl_close:
                dialog.dismiss();
                break;
            case R.id.ll_search:
                toActivity(SearchCampaignActivity.createIntent(context));
                break;
            case R.id.ll_show_more:
                toActivity(ShowAllActivity.createIntent(context));
                break;
            case R.id.ll_support_more:
                toActivity(SupportAllActivity.createIntent(context));
                break;
            default:
                break;
        }
    }
}
