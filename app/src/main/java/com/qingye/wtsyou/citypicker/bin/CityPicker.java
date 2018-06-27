package com.qingye.wtsyou.citypicker.bin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.qingye.wtsyou.citypicker.bean.BaseCity;
import com.qingye.wtsyou.citypicker.bean.GpsCityEvent;
import com.qingye.wtsyou.citypicker.bean.OnDestoryEvent;
import com.qingye.wtsyou.citypicker.bean.Options;
import com.qingye.wtsyou.citypicker.callback.IOnCityPickerCheckedCallBack;
import com.qingye.wtsyou.citypicker.finals.KEYS;
import com.qingye.wtsyou.citypicker.ui.CityPickerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Todo
 * @Author desmond
 * @Date 2017/5/25
 * @Pacakge com.desmond.citypicker.bin
 */

public class CityPicker
{
    Options options;
    static CityPicker instance;
    IOnCityPickerCheckedCallBack callback;

    public static BaseCity gpsCity ;

    private CityPicker()
    {
        EventBus.getDefault().register(this);
    }

    @MainThread
    public static CityPicker with(Context context)
    {
        if (instance == null)
        {
            synchronized (CityPicker.class)
            {
                if (instance == null)
                    instance = new CityPicker();
            }
        }
        instance.options = new Options(context.getApplicationContext());
        return instance;
    }

    /**
     * 是否需要显示当前城市
     * @param useGpsCity 如果为false那么就隐藏当前城市，并且调用setGpsCityByBaidu()或setGpsCityByAMap()都不会生效
     * @return
     */
    public CityPicker setUseGpsCity(boolean useGpsCity)
    {
        this.options.setUseGpsCity(useGpsCity);
        return this;
    }
    /**
     * 设置定位城市(百度定位)
     *
     * @param name 城市中文名称
     * @param code 百度城市code
     * @return
     */
    @MainThread
    public static void setGpsCityByBaidu(@Nullable String name, @Nullable String code)
    {
        BaseCity baseCity = new BaseCity();
        baseCity.setCityName(name);
        baseCity.setCodeByBaidu(code);
        setGpsCity(baseCity);
    }

    /**
     * 设置定位城市(高德定位)
     * @param name 城市中文名称
     * @param code 高德城市code
     * @return
     */
    public static void setGpsCityByAMap(@Nullable String name, @Nullable String code)
    {
        BaseCity baseCity = new BaseCity();
        baseCity.setCityName(name);
        baseCity.setCodeByAMap(code);
        setGpsCity(baseCity);
    }

    /**
     * 设置定位城市
     *
     * @param baseCity
     * @return
     */
    @MainThread
    public static void setGpsCity(BaseCity baseCity)
    {
        gpsCity = baseCity;
        EventBus.getDefault().post(new GpsCityEvent(baseCity));
    }

    /**
     * 自定义热门城市，输入数据库中的城市id（_id）
     *
     * @param ids
     * @return
     */
    @MainThread
    public CityPicker setHotCitiesId(String... ids)
    {
        instance.options.setHotCitiesId(ids);
        return this;
    }

    /**
     * 设置最多显示历史点击城市数量，0为不显示历史城市
     *
     * @param max
     * @return
     */
    @MainThread
    public CityPicker setMaxHistory(@IntRange(from = 0) int max)
    {
        instance.options.setMaxHistory(max);
        return this;
    }

    /**
     * 自定义城市基础数据列表，必须放在项目的assets文件夹下，并且表结构同citypicker项目下的assets中的数据库表结构相同
     *
     * @param name
     * @return
     * @deprecated 该方法当前为beta版本，不推荐使用
     */
    @MainThread
    @Deprecated
    public CityPicker setCustomDBName(@Nullable String name)
    {
        instance.options.setCustomDBName(name);
        return this;
    }

    /**
     * 设置标题栏背景
     *
     * @param res
     * @return
     */
    @MainThread
    public CityPicker setTitleBarDrawable(@DrawableRes int res)
    {
        instance.options.setTitleBarDrawable(res);
        return this;
    }

    /**
     * 设置返回按钮图片
     *
     * @param res
     * @return
     */
    @MainThread
    public CityPicker setTitleBarBackBtnDrawable(@DrawableRes int res)
    {
        instance.options.setTitleBarBackBtnDrawable(res);
        return this;
    }

    /**
     * 设置搜索框背景
     *
     * @param res
     * @return
     */
    @MainThread
    public CityPicker setSearchViewDrawable(@DrawableRes int res)
    {
        instance.options.setSearchViewDrawable(res);
        return this;
    }

    /**
     * 设置搜索框字体颜色
     *
     * @param res
     * @return
     */
    @MainThread
    public CityPicker setSearchViewTextColor(@ColorRes int res)
    {
        instance.options.setSearchViewTextColor(res);
        return this;
    }

    /**
     * 设置搜索框字体大小(sp)
     *
     * @param size
     * @return
     */
    @MainThread
    public CityPicker setSearchViewTextSize(int size)
    {
        instance.options.setSearchViewTextSize(size);
        return this;
    }


    /**
     * 设置右边检索栏字体颜色
     *
     * @param res
     * @return
     */
    @MainThread
    public CityPicker setIndexBarTextColor(@ColorRes int res)
    {
        instance.options.setIndexBarTextColor(res);
        return this;
    }

    /**
     * 设置右边检索栏字体大小(sp)
     *
     * @param size
     * @return
     */
    @MainThread
    public CityPicker setIndexBarTextSize(int size)
    {
        instance.options.setIndexBarTextSize(size);
        return this;
    }

    /**
     * 是否使用沉浸式状态栏，默认使用
     *
     * @param arg0
     * @return
     */
    @MainThread
    public CityPicker setUseImmerseBar(boolean arg0)
    {
        instance.options.setUseImmerseBar(arg0);
        return this;
    }


    @MainThread
    public void open()
    {
        Intent intent = new Intent(instance.options.getContext(), CityPickerActivity.class);
        intent.putExtra(KEYS.OPTIONS, options);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instance.options.getContext().startActivity(intent);
    }


    /**
     * 注册选择结果回调
     *
     * @param callback
     * @return
     */
    public CityPicker setOnCityPickerCallBack(IOnCityPickerCheckedCallBack callback)
    {
        this.callback = callback;
        return this;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void whenCityPickerChecked(BaseCity baseCity)
    {
        if (this.callback != null)
            this.callback.onCityPickerChecked(baseCity);
        destroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void whenCityPickerClosed(OnDestoryEvent event)
    {
        destroy();
    }

    public void destroy()
    {
        EventBus.getDefault().unregister(this);
        instance = null;
        options = null;
        gpsCity = null;
    }

}
