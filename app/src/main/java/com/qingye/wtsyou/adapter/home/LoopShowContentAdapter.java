package com.qingye.wtsyou.adapter.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.ImageLoopContent;

import java.io.File;
import java.util.List;

/**
 * Created by pm89 on 2018/3/9.
 */

public class LoopShowContentAdapter extends PagerAdapter {

    private Context context;
    private List<ImageLoopContent> contentList;
    private List<View> viewList;


    public LoopShowContentAdapter(Context context, List<View> viewList, List<ImageLoopContent> contentList) {
        this.context = context;
        this.viewList = viewList;
        this.contentList = contentList;

        Log.d("LoopShowContentAdapter", contentList.toString());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.ivPic = (ImageView) view.findViewById(R.id.iv_pic);
            view.setTag(holder);
        }
        if (position < contentList.size()) {
            //String url = contentList.get(position).getThumb();

            int url = R.mipmap.img_h;

            //if (url != null) {
                Log.d("LoopShowContentAdapter", "instantiateItem---" + url);

                /*if (!url.startsWith("/")) {
                    url = UrlConstants.HOST + "/" + url;
                } else {
                    url = UrlConstants.HOST + url;
                }*/

                Glide.with(context)
                        .load(url)
                        .into(holder.ivPic);
            //}

            container.addView(view);
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(ivList.get(position));
        Log.d("LoopShowContentAdapter", "destroyItem---" + position);
//        container.removeView(container.getChildAt(position));
        container.removeView(viewList.get(position));
    }

    @Override
    public int getCount() {
        if (contentList == null) {
            return  0;
        }
        return contentList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    class ViewHolder {
        ImageView ivPic;
    }
}
