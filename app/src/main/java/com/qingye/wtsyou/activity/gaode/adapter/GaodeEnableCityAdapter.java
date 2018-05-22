package com.qingye.wtsyou.activity.gaode.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.qingye.wtsyou.R;

/**
 * GaodeTipAddressAdapter -- 高德地图可选城市
 *
 * @author wujingshan
 *         created at 2018/4/28 9:36
 * @company 极客软创（厦门）科技有限公司
 */
public class GaodeEnableCityAdapter extends ABaseAdapter {
    private Context mContext;
    private List<String> mList;

    public GaodeEnableCityAdapter(Context context, List<String> list) {
        super(context);
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_enable_city;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {
        TextView tv_city = holder.obtainView(convertView, R.id.tv_city);

        String city = mList.get(position);

        tv_city.setText(city+"");
        return convertView;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
