package com.qingye.wtsyou.fragment.home;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.home.FansMainActivity;
import com.qingye.wtsyou.adapter.home.FansChartsAdapter;
import com.qingye.wtsyou.modle.EntityPageData;
import com.qingye.wtsyou.modle.FansCharts;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.home.FansChartsView;
import com.qingye.wtsyou.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChartsFansFragment extends BaseHttpRecyclerFragment<FansCharts,FansChartsView,FansChartsAdapter> implements CacheCallBack<FansCharts> {

    //名字
    private TextView tvSecName,tvFirName,tvThiName;
    //贡献值
    private TextView tvSecValue,tvFirValue,tvThiValue;
    //关注
    private TextView tvSecFocus,tvFirFocus,tvThiFocus;
    //打榜
    private TextView tvSecCan,tvFirCan,tvThiCan;
    //图片
    private ImageView ivSecImg,ivFirImg,ivThiImg;

    private final int currentPage = 1;
    private final int pageSize = 10;
    private final int totalPage = 0;
    //是否降序
    private final Boolean desc = false;

    private CustomDialog progressBar;

    private List<FansCharts> fansChartsList = new ArrayList<>();
    private List<FansCharts> fansTopChartsList = new ArrayList<>();
    private List<FansCharts> fansOtherChartsList = new ArrayList<>();

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static HomeChartsFansFragment createInstance() {

        return new HomeChartsFansFragment();
    }

    public HomeChartsFansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home_fans_charts);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        rankingQuery();

        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //initData();
        //initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部

        srlBaseHttpRecycler.autoRefresh();

        return view;
    }

    @Override
    public void initView() {
        super.initView();

        //名字
        tvSecName.findViewById(R.id.tv_second_name);
        tvFirName.findViewById(R.id.tv_first_name);
        tvThiName.findViewById(R.id.tv_third_name);
        //贡献值
        tvSecValue.findViewById(R.id.tv_second_value);
        tvFirValue.findViewById(R.id.tv_first_value);
        tvThiValue.findViewById(R.id.tv_third_value);
        //图片
        ivSecImg.findViewById(R.id.iv_second_img);
        ivFirImg.findViewById(R.id.iv_first_img);
        ivThiImg.findViewById(R.id.iv_third_img);
        //关注
        tvSecFocus.findViewById(R.id.tv_second_focus);
        tvFirFocus.findViewById(R.id.tv_first_focus);
        tvThiFocus.findViewById(R.id.tv_third_focus);
        //打榜
        tvSecCan.findViewById(R.id.tv_second_cancel);
        tvFirCan.findViewById(R.id.tv_first_cancel);
        tvThiCan.findViewById(R.id.tv_third_cancel);
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
    public void setList(final List<FansCharts> list) {

        setList(new AdapterCallBack<FansChartsAdapter>() {

            @Override
            public FansChartsAdapter createAdapter() {
                return new FansChartsAdapter(context);
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

    @Override
    public void initData() {
        super.initData();

        FansCharts first = fansTopChartsList.get(0);
        //名字
        tvFirName.setText(first.getUserName());
        //贡献
        tvFirValue.setText(first.getScore());
        //图片
        String url1 = first.getUserPhoto();
        if (url1 != null) {
            Glide.with(context)
                    .load(url1)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivFirImg);
        }
        if (first.getFollow()) {
            tvFirFocus.setVisibility(View.GONE);
            tvFirCan.setVisibility(View.VISIBLE);
        } else {
            tvFirFocus.setVisibility(View.VISIBLE);
            tvFirCan.setVisibility(View.GONE);
        }

        FansCharts second = fansTopChartsList.get(1);
        //名字
        tvSecName.setText(second.getUserName());
        //贡献
        tvSecValue.setText(second.getScore());
        //图片
        String url2 = second.getUserPhoto();
        if (url2 != null) {
            Glide.with(context)
                    .load(url2)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivSecImg);
        }
        if (second.getFollow()) {
            tvSecFocus.setVisibility(View.GONE);
            tvSecCan.setVisibility(View.VISIBLE);
        } else {
            tvSecFocus.setVisibility(View.VISIBLE);
            tvSecCan.setVisibility(View.GONE);
        }

        FansCharts third = fansTopChartsList.get(2);
        //名字
        tvThiName.setText(third.getUserName());
        //贡献
        tvThiValue.setText(third.getScore());
        //图片
        String url3 = second.getUserPhoto();
        if (url3 != null) {
            Glide.with(context)
                    .load(url3)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivThiImg);
        }
        if (third.getFollow()) {
            tvThiFocus.setVisibility(View.GONE);
            tvThiCan.setVisibility(View.VISIBLE);
        } else {
            tvThiFocus.setVisibility(View.VISIBLE);
            tvThiCan.setVisibility(View.GONE);
        }
    }

    @Override
    public List<FansCharts> parseArray(String json) {
        return null;
    }

    @Override
    public Class<FansCharts> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(FansCharts data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 10;
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }

    public void rankingQuery() {
        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            String keywords = null;
            String periods = null;
            String maxPeriods = null;

            HttpRequest.postFansRank(0, currentPage, pageSize, desc, keywords, periods, maxPeriods, new OnHttpResponseListener() {

                        @Override
                        public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                            if(!StringUtil.isEmpty(resultJson)){

                                EntityPageData entityPageData =  JSON.parseObject(resultJson,EntityPageData.class);

                                if(entityPageData.isSuccess()){
                                    //成功
                                    //showShortToast(R.string.getSuccess);
                                    fansTopChartsList.clear();
                                    fansOtherChartsList.clear();
                                    fansChartsList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                            ,new TypeToken<List<FansCharts>>(){}.getType());

                                    for (FansCharts fansCharts : fansChartsList) {
                                        if (fansCharts.getRanking() == 1 || fansCharts.getRanking() == 2
                                                || fansCharts.getRanking() == 3) {
                                            fansTopChartsList.add(fansCharts);
                                        } else {
                                            fansOtherChartsList.add(fansCharts);
                                        }
                                    }

                                    progressBarDismiss();

                                    setList(fansOtherChartsList);
                                }else{//显示失败信息
                                    if (entityPageData.getCode().equals("401")) {
                                        showShortToast(R.string.tokenInvalid);
                                        toActivity(MainActivity.createIntent(context));
                                    } else {
                                        showShortToast(entityPageData.getMessage());
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

            progressBarDismiss();
        }
    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toActivity(FansMainActivity.createIntent(context, id));
    }
}
