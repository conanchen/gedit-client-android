package com.github.conanchen.gedit.ui.my;

import android.arch.paging.PagedListAdapter;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.room.store.Store;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyWorkStoresAdapter extends PagedListAdapter<Store, MyWorkStoresAdapter.ViewHolder> {

    protected MyWorkStoresAdapter() {
        super(Store.DIFF_CALLBACK);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_my_work_stores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store store = getItem(position);
        if (store != null) {
            holder.bindTo(store);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        AppCompatImageView image;
        AppCompatTextView name;
        AppCompatTextView status;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            layout = itemView.findViewById(R.id.layout);
        }

        public void bindTo(Store store) {
            name.setText(store.address);
            status.setText(store.uuid);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(store);
                }
            });
        }
    }

    private OnItemClickListener listener;

    interface OnItemClickListener {
        void OnItemClick(Store store);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
