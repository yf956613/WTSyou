package com.qingye.wtsyou.adapter.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;

import java.util.List;

/**
 * Created by pm89 on 2018/5/31.
 */
public class HistoryStarsSearchAdapter extends RecyclerView.Adapter<HistoryStarsSearchAdapter.ViewHolder> {
    private final Context context;
    private List<String> items;

    private OnItemClick mOnItemClick;
    private OnItemChildClick onItemChildClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public void setOnItemChildClick(OnItemChildClick onItemChildClick) {
        this.onItemChildClick = onItemChildClick;
    }

    public HistoryStarsSearchAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_stars, parent, false);
        return new ViewHolder(v, mOnItemClick, onItemChildClick);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = items.get(position);
        //TODO Fill in your logic for binding the view.
        holder.tvName.setText(item);
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

        TextView tvName;
        ImageView ivDelete;

        public ViewHolder(View itemView, OnItemClick onItemClick, OnItemChildClick onItemChildClick) {
            this(itemView);
            this.mOnItemClick = onItemClick;
            this.mOnItemChildClick = onItemChildClick;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.stars_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            itemView.setOnClickListener(this);
            tvName.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_delete:
                    if (mOnItemChildClick != null) {
                        mOnItemChildClick.deleteClick(v, getAdapterPosition());
                    }
                    break;
                case R.id.stars_name:
                    if (mOnItemChildClick != null) {
                        mOnItemChildClick.historyClick(v, getAdapterPosition());
                    }
                    break;
            }

        }

    }

    public interface OnItemClick {

    }


    public interface OnItemChildClick {
        void deleteClick(View v, int position);
        void historyClick(View v, int position);
    }
}

