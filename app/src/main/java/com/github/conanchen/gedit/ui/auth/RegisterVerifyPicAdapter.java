package com.github.conanchen.gedit.ui.auth;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.user.auth.grpc.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/13.
 */

public class RegisterVerifyPicAdapter extends RecyclerView.Adapter<RegisterVerifyPicAdapter.ViewHolder> {

    private Context context;
    private List<Question> mData = new ArrayList<>();
    public Map<Integer, Boolean> isSelected = new HashMap<>();

    public RegisterVerifyPicAdapter(Context context, List<Question> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setNormal() {
        for (int i = 0; i < mData.size(); i++) {
            isSelected.put(i, false);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_register_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = mData.get(position);

        if (isSelected.get(position)) {
            holder.selecte.setVisibility(View.VISIBLE);
            holder.background.setVisibility(View.VISIBLE);
        } else {
            holder.selecte.setVisibility(View.GONE);
            holder.background.setVisibility(View.GONE);
        }

//        Glide.with(context)
//                .load(question.getImage())
//                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < isSelected.size(); i++) {
                    if (position == i) {
                        if (isSelected.get(i)) {
                            isSelected.put(i, false);
                        } else {
                            isSelected.put(i, true);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        AppCompatImageView selecte;
        View background;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            selecte = itemView.findViewById(R.id.selecte);
            background = itemView.findViewById(R.id.background);
        }
    }


}
