package com.github.conanchen.gedit.ui.hello;

import android.arch.paging.PagedListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.room.hello.Hello;

public class HelloAdapter extends PagedListAdapter<Hello, HelloAdapter.HelloItemViewHolder> {

    protected HelloAdapter() {
        super(Hello.DIFF_CALLBACK);
    }

    @Override
    public HelloItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_hello_list, parent, false);
        return new HelloItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HelloItemViewHolder holder, int position) {
        Hello hello = getItem(position);
        if (hello != null) {
            holder.bindTo(hello);
        }
    }

    static class HelloItemViewHolder extends RecyclerView.ViewHolder {
        TextView hellotext;

        public HelloItemViewHolder(View itemView) {
            super(itemView);
            hellotext = itemView.findViewById(R.id.hellotext);
        }

        public void bindTo(Hello user) {
            hellotext.setText(String.format("created:%d lastUpdated:%d uuid:%s message:%s",
                    user.created, user.lastUpdated, user.uuid, user.message));
        }
    }
}
