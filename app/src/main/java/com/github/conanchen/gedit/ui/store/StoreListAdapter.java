package com.github.conanchen.gedit.ui.store;

import android.arch.paging.PagedListAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.room.store.Store;

/**
 * Created by Administrator on 2018/1/15.
 */

public class StoreListAdapter extends PagedListAdapter<Store, StoreListAdapter.ViewHolder> {

    protected StoreListAdapter() {
        super(Store.DIFF_CALLBACK);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_home_store_list, parent, false);
        return new StoreListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store store = getItem(position);
        if (store != null) {
            holder.bindTo(store);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView image;
        AppCompatTextView name;
        AppCompatTextView points;
        AppCompatTextView distance;
        AppCompatTextView address;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.store_name);
            points = itemView.findViewById(R.id.points);
            distance = itemView.findViewById(R.id.store_distance);
            address = itemView.findViewById(R.id.address_info);
        }

        public void bindTo(Store store) {
            name.setText(store.address);
            distance.setText(store.uuid);
        }
    }
}
