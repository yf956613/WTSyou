package com.qingye.wtsyou.adapter.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.HotTopic;

import java.util.List;

/**
 * Created by pm89 on 2018/6/22.
 */
public class HotTopicAdapter extends RecyclerView.Adapter<HotTopicAdapter.ViewHolder> {
    private final Context context;
    private List<HotTopic> items;

    private OnItemClick mOnItemClick;
    private OnItemChildClick onItemChildClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public void setOnItemChildClick(OnItemChildClick onItemChildClick) {
        this.onItemChildClick = onItemChildClick;
    }

    public HotTopicAdapter(List<HotTopic> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_conversation_search_hot, parent, false);
        return new ViewHolder(v, mOnItemClick, onItemChildClick);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HotTopic item = items.get(position);
        //TODO Fill in your logic for binding the view.

        holder.tvNo.setText("" + (position + 1));
        holder.tvHotTopic.setText("#" + item.getTitle() + "#");
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClick mOnItemClick;
        private OnItemChildClick mOnItemChildClick;

        TextView tvNo;
        TextView tvHotTopic;

        public ViewHolder(View itemView, OnItemClick onItemClick, OnItemChildClick onItemChildClick) {
            this(itemView);
            this.mOnItemClick = onItemClick;
            this.mOnItemChildClick = onItemChildClick;
        }

        public ViewHolder(View itemView) {
            super(itemView);

            tvNo = (TextView) itemView.findViewById(R.id.tv_no);
            tvHotTopic = (TextView) itemView.findViewById(R.id.tv_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item:
                    if (mOnItemClick != null) {
                        mOnItemClick.ItemClick(v, getAdapterPosition());
                    }
                    break;
            }
        }
    }

    public interface OnItemClick {
        void ItemClick(View v, int position);
    }


    public interface OnItemChildClick {

    }
}

