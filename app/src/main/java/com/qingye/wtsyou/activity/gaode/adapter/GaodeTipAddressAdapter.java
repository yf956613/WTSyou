package com.qingye.wtsyou.activity.gaode.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.qingye.wtsyou.R;

import java.util.List;

/**
 * GaodeTipAddressAdapter -- 高德地图搜索提示
 *
 * @author wujingshan
 *         created at 2018/4/28 9:36
 * @company 极客软创（厦门）科技有限公司
 */
public class GaodeTipAddressAdapter extends ABaseAdapter {
    private Context mContext;
    private List<Tip> mList;

    public GaodeTipAddressAdapter(Context context, List<Tip> list) {
        super(context);
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {
        TextView poi_field_id = holder.obtainView(convertView, R.id.poi_field_id);
        TextView poi_value_id = holder.obtainView(convertView, R.id.poi_value_id);

        Tip tip = mList.get(position);

        poi_field_id.setText(tip.getName());
        poi_value_id.setText(tip.getDistrict());
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
