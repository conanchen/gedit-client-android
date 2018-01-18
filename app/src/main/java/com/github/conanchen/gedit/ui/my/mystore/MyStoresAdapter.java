package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.paging.PagedListAdapter;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.room.store.MyStores;
import com.github.conanchen.gedit.room.store.Store;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyStoresAdapter extends PagedListAdapter<MyStores, MyStoresAdapter.ViewHolder> {

    protected MyStoresAdapter() {
        super(MyStores.DIFF_CALLBACK);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_my_stores_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyStores store = getItem(position);
        if (store != null) {
            holder.bindTo(store);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        AppCompatImageView image;
        AppCompatTextView name;
        AppCompatTextView money;
        AppCompatTextView distance;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            distance = itemView.findViewById(R.id.distance);
            money = itemView.findViewById(R.id.money);
            layout = itemView.findViewById(R.id.layout);
        }

        public void bindTo(MyStores myStores) {
            name.setText(myStores.storeUuid);
            distance.setText(myStores.storeName);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(myStores);
                }
            });
        }
    }

    private OnItemClickListener listener;

    interface OnItemClickListener {
        void OnItemClick(MyStores store);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
