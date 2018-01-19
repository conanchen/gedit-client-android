package com.github.conanchen.gedit.ui.my.myfans;

import android.arch.paging.PagedListAdapter;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.room.my.fan.Fanship;

/**
 * Created by Conan Chen on 2018/1/17.
 */

public class MyFansAdapter extends PagedListAdapter<Fanship, MyFansAdapter.ViewHolder> {
    protected MyFansAdapter() {
        super(Fanship.DIFF_CALLBACK);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_my_introduced_stores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fanship store = getItem(position);
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

        public void bindTo(Fanship fanship) {
            name.setText(fanship.fanName);
            status.setText(fanship.fanUuid);
            layout.setOnClickListener(view -> listener.OnItemClick(fanship)
            );
        }
    }

    private OnItemClickListener listener;

    interface OnItemClickListener {
        void OnItemClick(Fanship store);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
