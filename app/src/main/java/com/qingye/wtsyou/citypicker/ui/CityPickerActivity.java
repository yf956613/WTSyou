package com.qingye.wtsyou.citypicker.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.jaeger.library.StatusBarUtil;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.citypicker.bean.BaseCity;
import com.qingye.wtsyou.citypicker.bean.GpsCityEvent;
import com.qingye.wtsyou.citypicker.bean.OnDestoryEvent;
import com.qingye.wtsyou.citypicker.bean.Options;
import com.qingye.wtsyou.citypicker.bin.CityPicker;
import com.qingye.wtsyou.citypicker.finals.KEYS;
import com.qingye.wtsyou.citypicker.presenter.CityPickerPresenter;
import com.qingye.wtsyou.citypicker.pull2refresh.RefreshRecyclerView;
import com.qingye.wtsyou.citypicker.pull2refresh.callback.IOnItemClickListener;
import com.qingye.wtsyou.citypicker.tools.PxConvertUtil;
import com.qingye.wtsyou.citypicker.tools.Res;
import com.qingye.wtsyou.citypicker.tools.SysUtil;
import com.qingye.wtsyou.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import zuo.biao.library.util.Log;

import static com.qingye.wtsyou.citypicker.presenter.CityPickerPresenter.LISHI_REMEN;


/**
 *
 */
public class CityPickerActivity extends AppCompatActivity implements View.OnClickListener,
        IOnItemClickListener,
        WaveSideBar.OnSelectIndexItemListener,
        AdapterView.OnItemClickListener,
        TextWatcher,
        AMapLocationListener
{
    protected View title;
    /**
     * 返回按钮
     */
    protected ImageView titleBackIb;
    /**
     * 搜索框
     */
    protected AutoCompleteTextView titleSearchEt;


    /**
     * 搜索框清空按钮
     */
    protected ImageButton searchClearIb;

    /**
     * 列表
     */
    protected RefreshRecyclerView contentRrv;
    /**
     * 检索栏
     */
    protected WaveSideBar contentWsb;

    /**
     * 定位城市+历史城市+热门城市布局
     */
    protected View headerView;
    /**
     * 历史城市标题
     */
    protected TextView historyTitleTv;
    /**
     * 历史城市容器
     */
    protected GridLayout historyGroupGl;
    /**
     * 热门城市标题
     */
    protected TextView hotTitleTv;
    /**
     * 热门城市容器
     */
    protected GridLayout hotGroupGl;
    /**
     * 自动定位view
     */
    protected TextView gpsTv;


    protected CityPickerAdapter adapter;
    protected CityPickerPresenter cityPickerPresenter;
    protected SearchAdapter searchAdapter;

    /**
     * 自定义城市列表数据源
     */
    protected List<BaseCity> datas;

    /**
     * 右边拼音首字母检索列表
     */
    protected List<String> pyIndex;

    /**
     * 城市定位
     */
    protected BaseCity gpsCity;

    /**
     * 是否需要显示城市定位
     */
    protected boolean useGpsCity;

    /**
     * 热门城市列表
     */
    protected List<BaseCity> hotCities;

    /**
     * 自定义热门城市ids
     */
    protected String[] hotCitiesId;


    protected List<BaseCity> historyCitys;

    /**
     * 最大历史城市数量
     */
    protected int maxHistory;


    protected int headerCityWidth;

    protected Options options;

    /**
     * 索引缓存
     */
    protected HashMap<String, Integer> indexPosMap;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_picker);
        init(savedInstanceState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息

                //高德定位
                CityPicker.setGpsCityByAMap(amapLocation.getCity(),amapLocation.getCityCode());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 获取传入的参数
     */
    protected void receiveDatas()
    {
        options = getIntent().getParcelableExtra(KEYS.OPTIONS);
        if (options == null)
            options = new Options(this.getApplicationContext());
        options.setContext(this.getApplicationContext());

        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

        useGpsCity = options.isUseGpsCity();
        gpsCity = CityPicker.gpsCity;
        hotCitiesId = options.getHotCitiesId();
        maxHistory = options.getMaxHistory();
    }

    protected void init(Bundle savedInstanceState)
    {
        setStatusBar();

        receiveDatas();

        registerViews();

        setViewStyle();

        EventBus.getDefault().register(this);

        cityPickerPresenter = new CityPickerPresenter(this.getApplicationContext(), options.getCustomDBName());

        datas = cityPickerPresenter.getCitysSort();
        pyIndex = cityPickerPresenter.getIndex();


        //城市列表适配
        adapter = new CityPickerAdapter(this.getApplicationContext(), options.getIndexBarTextColor());
        contentRrv.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        contentRrv.setAdapter(adapter);
        contentRrv.addHeaderView(getHeaderView());
        contentRrv.setOnItemClickListener(CityPickerActivity.this);
        contentRrv.disablePullLable();
        adapter.setData(datas);
        adapter.notifyDataSetChanged();

        //设置索引
        indexPosMap = new HashMap<>(pyIndex.size());
        contentWsb.setIndexItems(pyIndex.toArray(new String[pyIndex.size()]));
        contentWsb.setOnSelectIndexItemListener(this);

        //搜索结果适配
        searchAdapter = new SearchAdapter(this.getApplicationContext(), datas, cityPickerPresenter);
        titleSearchEt.setAdapter(searchAdapter);
        titleSearchEt.setOnItemClickListener(this);
    }


    /**
     * 修改页面样式
     */
    protected void setViewStyle()
    {
        title.setBackgroundDrawable(options.getTitleBarDrawable());

        titleSearchEt.setTextSize(options.getSearchViewTextSize());
        titleSearchEt.setTextColor(options.getSearchViewTextColor());
        titleSearchEt.setBackgroundDrawable(options.getSearchViewDrawable());

        titleBackIb.setImageDrawable(options.getTitleBarBackBtnDrawable());

        contentWsb.setTextColor(options.getIndexBarTextColor());
        contentWsb.setTextSize(options.getIndexBarTextSize());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && options.isUseImmerseBar())
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = SysUtil.getStatusBarHeight(this.getApplicationContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight + Res.dimenPx(this.getApplicationContext(), R.dimen.title_bar_height));
            title.setPadding(0, statusBarHeight + title.getPaddingTop(), 0, 0);
            title.setLayoutParams(params);
        }

    }


    /**
     * 设置列表的头部view
     *
     * @return
     */
    protected View getHeaderView()
    {
        if (headerView != null) return headerView;

        headerView = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.city_picker_header, contentRrv.getRecyclerView(), false);
        gpsTv = findById(headerView, R.id.c_p_header_gps_tv);
        historyTitleTv = findById(headerView, R.id.c_p_header_historytitle_tv);
        historyGroupGl = findById(headerView, R.id.c_p_header_historygroup_gl);
        hotTitleTv = findById(headerView, R.id.c_p_header_hottitle_tv);
        hotGroupGl = findById(headerView, R.id.c_p_header_hotgroup_gl);

        //动态计算每个按钮的宽度：（屏幕宽度-右边距-左边距）/每行按钮个数-单个按钮的右边距
        headerCityWidth = (SysUtil.getScreenWidth(this.getApplicationContext()) - historyGroupGl.getPaddingRight() - historyGroupGl.getPaddingLeft()) / historyGroupGl.getColumnCount() - PxConvertUtil.dip2px(this, 10);

        setHeaderViewValue();
        return headerView;
    }

    /**
     * 为头部布局填充内容及值
     */
    protected void setHeaderViewValue()
    {
        setGpsCityStatus(gpsCity);

        // 填充历史城市
        maxHistory = maxHistory > CityPickerPresenter.MAX_HEADER_CITY_SIZE ? CityPickerPresenter.MAX_HEADER_CITY_SIZE : maxHistory;
        historyCitys = cityPickerPresenter.getHistoryCity(maxHistory);
        if (historyCitys != null && historyCitys.size() > 0)
        {
            historyTitleTv.setVisibility(View.VISIBLE);
            historyGroupGl.setVisibility(View.VISIBLE);

            //动态创建button填充到布局中
            historyGroupGl.removeAllViews();
            for (int i = 0; i < historyCitys.size(); i++)
            {
                BaseCity city = historyCitys.get(i);
                Button btn = getNewButton();
                btn.setText(city.getCityName());
                btn.setOnClickListener(this);
                btn.setTag(city);
                btn.setId(R.id.header_city_button);
                historyGroupGl.addView(btn);
            }
        } else
        {
            historyTitleTv.setVisibility(View.GONE);
            historyGroupGl.setVisibility(View.GONE);
        }

        // 填充热门城市
        /*//如果用户没有输入自定义热门城市就从本地数据库中获取默认的热门城市
        if (hotCitiesId == null || hotCitiesId.length == 0)
            hotCities = cityPickerPresenter.getHotCity(CityPickerPresenter.MAX_HEADER_CITY_SIZE);
        else
            hotCities = cityPickerPresenter.getHotCityById(hotCitiesId);

        if (hotCities != null && !hotCities.isEmpty())
        {
            hotTitleTv.setVisibility(View.VISIBLE);
            hotGroupGl.setVisibility(View.VISIBLE);

            //动态创建button填充到布局中
            hotGroupGl.removeAllViews();
            for (int i = 0; i < hotCities.size(); i++)
            {
                BaseCity city = hotCities.get(i);
                Button btn = getNewButton();
                btn.setText(city.getCityName());
                btn.setOnClickListener(this);
                btn.setTag(city);
                btn.setId(R.id.header_city_button);
                hotGroupGl.addView(btn);
            }
        } else*/
        {
            hotTitleTv.setVisibility(View.GONE);
            hotGroupGl.setVisibility(View.GONE);
        }
    }

    /**
     * 动态创建一个空的Button
     *
     * @return
     */
    protected Button getNewButton()
    {
        int dp10 = PxConvertUtil.dip2px(this.getApplicationContext(), 10);
        int dp3 = PxConvertUtil.dip2px(this.getApplicationContext(), 3);
        Button btn = new Button(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = PxConvertUtil.dip2px(this.getApplicationContext(), 40);
        //没有使用权重的原因是当只有一个button的时候宽度会充满全屏
        //这里根据屏幕宽度动态计算button的宽度
        params.width = headerCityWidth;
        params.bottomMargin = dp10;
        params.rightMargin = dp10;
        btn.setLayoutParams(params);

        btn.setPadding(dp3, dp3, dp3, dp3);
        btn.setGravity(Gravity.CENTER);
        btn.setBackgroundResource(R.drawable.button_selector);
        btn.setEllipsize(TextUtils.TruncateAt.END);
        btn.setMaxLines(1);
        btn.setTextColor(getResources().getColor(R.color.black));
        btn.setTextSize(14);
        return btn;
    }


    protected void registerViews()
    {
        title = findById(R.id.title_root_rl);
        titleBackIb = findById(R.id.title_back_ib);
        searchClearIb = findById(R.id.title_searchclear_ib);
        contentRrv = findById(R.id.c_p_content_rrv);
        contentWsb = findById(R.id.c_p_content_wsb);
        titleSearchEt = findById(R.id.title_txt_et);
        titleBackIb.setOnClickListener(this);
        searchClearIb.setOnClickListener(this);
        titleSearchEt.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == R.id.title_back_ib)// 返回按钮
        {
            onBackPressed();

        } else if (i == R.id.title_searchclear_ib)//搜索框清空按钮
        {
            titleSearchEt.setText("");
        } else if (i == R.id.c_p_header_gps_tv)// 点击gps定位
        {
            whenCitySelected(gpsCity);

        } else if (i == R.id.header_city_button)// 点击热门城市或历史城市
        {
            whenCitySelected((BaseCity) v.getTag());
        }

    }

    protected <T extends View> T findById(int id)
    {
        return (T) findViewById(id);
    }

    protected <T extends View> T findById(View view, int id)
    {
        return (T) view.findViewById(id);
    }

    @Override
    public void onItemClick(Object obj, int position)
    {
        whenCitySelected((BaseCity) obj);
    }

    /**
     * 城市选择完成后执行的操作
     *
     * @param city
     */
    protected void whenCitySelected(BaseCity city)
    {
        cityPickerPresenter.saveHistoryCity(city);
        EventBus.getDefault().post(city);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SELECTED_ADDRESS, city);//放进数据流中
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    /**
     * 手指在右边索引上滑动的回调
     *
     * @param index
     */
    @Override
    public void onSelectIndexItem(String index)
    {
        if (LISHI_REMEN.equals(index))
        {
            scrollTo(0);
            return;
        }

        // 行号先从索引缓存中获取，如果缓存中没有再遍历基础数据List
        Integer pos = indexPosMap.get(index);
        if (pos != null)
        {
            scrollTo(pos.intValue() + adapter.getHeaderSize());
            return;
        }

        for (int i = 0; i < datas.size(); i++)
        {
            if (!datas.get(i).getCityPYFirst().equals(index)) continue;
            indexPosMap.put(index, i);
            scrollTo(i + adapter.getHeaderSize());
            return;
        }
    }

    protected void scrollTo(int line)
    {
        ((LinearLayoutManager) contentRrv.getRecyclerView().getLayoutManager()).scrollToPositionWithOffset(line, 0);
    }

    /**
     * 搜索结果的listview点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        BaseCity baseCity = (BaseCity) parent.getAdapter().getItem(position);
        titleSearchEt.setText(baseCity.getCityName());
        whenCitySelected(baseCity);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        searchClearIb.setVisibility(s.length() <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * 城市定位成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void whenLocationSucc(GpsCityEvent event)
    {
        setGpsCityStatus(event.gpsCity);
    }

    private void setGpsCityStatus(BaseCity gpsCity)
    {
        if (gpsTv == null) return;
        if (!useGpsCity)
        {
            gpsTv.setVisibility(View.GONE);
            return;
        } else
            gpsTv.setVisibility(View.VISIBLE);

        this.gpsCity = gpsCity;
        //设置自动定位城市
        if (gpsCity != null)
        {
            gpsTv.setText(gpsCity.getCityName());
            gpsTv.setOnClickListener(this);
        } else
        {
            gpsTv.setText(Res.string(this, R.string.location_city_lodding));
            gpsTv.setOnClickListener(null);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().post(new OnDestoryEvent());
        EventBus.getDefault().unregister(this);
    }

    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
		/*StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));*/
    }
}
