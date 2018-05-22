package com.qingye.wtsyou.activity.gaode.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.qingye.wtsyou.activity.gaode.adapter.GaodeEnableCityAdapter;
import com.qingye.wtsyou.activity.gaode.adapter.GaodeTipAddressAdapter;
import com.qingye.wtsyou.basemodel.LngLat;
import com.qingye.wtsyou.basemodel.PCDT;
import com.qingye.wtsyou.basemodel.POI;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.utils.Constant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zuo.biao.library.base.BaseActivity;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 * Marker动画功能介绍
 */
public class GaoDeAddressSelectActivity extends BaseActivity implements Inputtips.InputtipsListener, View.OnClickListener {
    private TextView mTv_btnSure;
    private View mView_btnBack;
    private TextView mTv_citySel;
    private AMap mAMap;
    private MapView mMapView;
    Marker screenMarker = null;
    //
    private String mSelectCity = "";
    private AutoCompleteTextView mKeywordText;
    private ListView mListView_tip;
    private ListView mListView_city;

    private RelativeLayout cityView;
    private GaodeTipAddressAdapter mAdapter;
    private GaodeEnableCityAdapter mAdapter_enableCtiy;
    private List<HashMap<String, String>> mList_tip = new ArrayList<HashMap<String, String>>();
    private List<Tip> mTipList = new ArrayList<>();
    private List<String> mCityList = new ArrayList<>();
    //
    private MyLocationStyle myLocationStyle;
    private boolean mIsChangeCity = false;

    private TextView tvCloseCity;
    private ImageView ivCloseSearch;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, GaoDeAddressSelectActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gaode_address_select_activity);
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState); // 此方法必须重写
        init();
        initData();
    }

    @Override
    public void initView() {

    }

    /**
     * 获取可选城市
     */
    public void initData() {
        mCityList.add("厦门");
        mCityList.add("北京");
        mCityList.add("成都");
        mCityList.add("上海");
        mCityList.add("福州");
        mCityList.add("南京");
        mCityList.add("大连");
        mCityList.add("长沙");
        mCityList.add("郑州");
        mCityList.add("武汉");

        mAdapter_enableCtiy.notifyDataSetChanged();

        mSelectCity = mCityList.get(0);
    }

    @Override
    public void initEvent() {

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        mTv_btnSure = findViewById(R.id.tv_btnSure);
        mTv_btnSure.setOnClickListener(this);
        mView_btnBack = findViewById(R.id.view_btnBack);
        mView_btnBack.setOnClickListener(this);
        mTv_citySel = findViewById(R.id.tv_citySel);
        mTv_citySel.setOnClickListener(this);
        //
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        mAMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter();
            }
        });
        // 设置可视范围变化时的回调的接口方法
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition postion) {
                //屏幕中心的Marker跳动
                startJumpAnimation();
            }
        });
        //设置定位
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        myLocationStyle = new MyLocationStyle();
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
        //
        mListView_tip = findViewById(R.id.listView_tip);
        mListView_tip.setDividerHeight(0);
        mAdapter = new GaodeTipAddressAdapter(GaoDeAddressSelectActivity.this, mTipList);
        mListView_tip.setAdapter(mAdapter);
        mListView_tip.setVisibility(View.GONE);
        mListView_tip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showMap();
                //
                mKeywordText.setText(mTipList.get(position).getName());
                Tip tip = mTipList.get(position);
                LatLonPoint point = tip.getPoint();
                changeCamera(new LatLng(point.getLatitude(), point.getLongitude()));
            }
        });

        cityView = findViewById(R.id.city_view);
        //
        mListView_city = findViewById(R.id.listView_city);
        mListView_city.setDividerHeight(0);
        mAdapter_enableCtiy = new GaodeEnableCityAdapter(GaoDeAddressSelectActivity.this, mCityList);
        mListView_city.setAdapter(mAdapter_enableCtiy);
        mListView_city.setVisibility(View.GONE);
        mListView_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showMap();
                //
                mTv_citySel.setText(mCityList.get(position));
                mSelectCity = mCityList.get(position);
                //
                mIsChangeCity = true;
                searchTipByKeyWords(mKeywordText.getText().toString().trim());
            }
        });
        //
        mKeywordText = findViewById(R.id.input_edittext);
        mKeywordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    searchTipByKeyWords(s.toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mKeywordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    showTip();
                }
            }
        });

        showMap();

        //关闭操作
        tvCloseCity = findViewById(R.id.close_city);
        ivCloseSearch = findViewById(R.id.close_search);
        tvCloseCity.setOnClickListener(this);
        ivCloseSearch.setOnClickListener(this);
    }

    /**
     * 根据关键字搜索
     *
     * @param s
     */
    private void searchTipByKeyWords(String s) {
        String newText = s;
        InputtipsQuery inputquery = new InputtipsQuery(newText, mSelectCity);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(GaoDeAddressSelectActivity.this, inputquery);
        inputTips.setInputtipsListener(GaoDeAddressSelectActivity.this);
        inputTips.requestInputtipsAsyn();
    }

    /**
     * 获取到关键字列表
     *
     * @param tipList
     * @param rCode
     */
    @Override
    public void onGetInputtips(final List<Tip> tipList, int rCode) {
        try {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                mTipList.clear();
                mTipList.addAll(tipList);
                mAdapter.notifyDataSetChanged();
                if (mIsChangeCity) {
                    Tip tip = mTipList.get(0);
                    LatLonPoint point = tip.getPoint();
                    changeCamera(new LatLng(point.getLatitude(), point.getLongitude()));
                    mIsChangeCity = false;
                }
            } else {
                //ToastUtil.showerror(GaoDeAddressSelectActivity.this.getApplicationContext(), rCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mListView_tip.getVisibility() == View.VISIBLE) {
            showMap();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = mAMap.getCameraPosition().target;
        Point screenPosition = mAMap.getProjection().toScreenLocation(latLng);
        screenMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(LatLng latLng) {
        mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 11, 30, 0)));
    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (screenMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            Point point = mAMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this, 125);
            LatLng target = mAMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();

        } else {
            Log.e("amap", "screenMarker is null");
        }
    }


    /**
     * func:获取屏幕中心的经纬度坐标
     *
     * @return
     */
    public LatLng getMapCenterPoint() {
        int left = mMapView.getLeft();
        int top = mMapView.getTop();
        int right = mMapView.getRight();
        int bottom = mMapView.getBottom();
        // 获得屏幕点击的位置
        int x = (int) (mMapView.getX() + (right - left) / 2);
        int y = (int) (mMapView.getY() + (bottom - top) / 2);
        Projection projection = mAMap.getProjection();
        LatLng pt = projection.fromScreenLocation(new Point(x, y));
        return pt;
    }

    /**
     * dip和px转换
     */
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
//        new PoiSearch().searchPOI()
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_citySel:
                showEnableCity();
                break;
            case R.id.view_btnBack:
                /*if (mListView_tip.getVisibility() == View.VISIBLE || mListView_city.getVisibility() == View.VISIBLE) {
                    showMap();
                } else {
                    finish();
                }*/
                finish();
                break;
            case R.id.tv_btnSure://获取到poi信息
                GeocodeSearch geocoderSearch = new GeocodeSearch(this);
                LatLng mapCenterPoint = getMapCenterPoint();
                LatLonPoint latLonPoint = new LatLonPoint(mapCenterPoint.latitude, mapCenterPoint.longitude);
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                        GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult result, int i) {
                        RegeocodeAddress re = result.getRegeocodeAddress();
                        if (re != null && re.getPois() != null && re.getPois().size() > 0) {
                            PoiItem poiItem = re.getPois().get(0);

                            POI poi = new POI();
                            LngLat lnglat = new LngLat();
                            lnglat.setLat(new BigDecimal(poiItem.getLatLonPoint().getLatitude()));
                            lnglat.setLng(new BigDecimal(poiItem.getLatLonPoint().getLongitude()));
                            PCDT pcdt = new PCDT();
                            pcdt.setCity(re.getCity());
                            pcdt.setCityCode(re.getCityCode());
                            pcdt.setDistrict(re.getDistrict());
                            pcdt.setDistrictCode(re.getAdCode());
                            pcdt.setProvince(re.getProvince());
                            pcdt.setProvinceCode(re.getAdCode().substring(0, 2) + "0000");
                            pcdt.setTownship(re.getTownship());
                            //
                            poi.setLngLat(lnglat);
                            poi.setPcdt(pcdt);
                            poi.setAddress(result.getRegeocodeAddress().getFormatAddress());
                            poi.setPoiAddress(poiItem.getSnippet());
                            poi.setPoiName(poiItem.getTitle());

                            //showShortToast("获取POI信息成功=>" + poi.getAddress().toString());
                            //showShortToast(R.string.getSuccess);
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constant.SELECTED_ADDRESS,poi);//放进数据流中

                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            showShortToast(R.string.getLocationFailed);
                        }
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                    }
                });
                break;
            case R.id.close_city:
                cityView.setVisibility(View.GONE);
                break;
            case R.id.close_search:
                mKeywordText.setText("");
                showMap();
                break;
        }
    }

    private void showEnableCity() {
        cityView.setVisibility(View.VISIBLE);
        mListView_city.setVisibility(View.VISIBLE);
        mListView_tip.setVisibility(View.GONE);//隐藏提示
        hideSoftInput();//隐藏软键盘
        mTv_btnSure.setVisibility(View.VISIBLE);
        mKeywordText.clearFocus();
        mTv_citySel.setVisibility(View.VISIBLE);
    }

    private void showTip() {
        mListView_city.setVisibility(View.VISIBLE);
        mListView_tip.setVisibility(View.VISIBLE);//隐藏提示
        mTv_btnSure.setVisibility(View.GONE);
        mTv_citySel.setVisibility(View.GONE);
    }

    private void showMap() {
        cityView.setVisibility(View.GONE);
        mListView_tip.setVisibility(View.GONE);//隐藏提示
        hideSoftInput();//隐藏软键盘
        mTv_btnSure.setVisibility(View.VISIBLE);
        mKeywordText.clearFocus();
        mTv_citySel.setVisibility(View.VISIBLE);
    }

}
