package com.github.conanchen.gedit.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.github.conanchen.gedit.R;

import java.util.ArrayList;
import java.util.List;

public class CustomPopWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private Activity activity;
    private View popView;

    private ListView mListView;

    private LinearLayout layout;

    private OnItemClickListener onItemClickListener;


    private List<String> mData = new ArrayList<>();

    public CustomPopWindow(Activity activity, List<String> mData) {
        super(activity);
        this.activity = activity;
        this.mData = mData;


        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.popup_menu, null);// 加载菜单布局文件
        this.setContentView(popView);// 把布局文件添加到popupwindow中
        this.setWidth(dip2px(activity, 100));// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
//        this.setHeight(LayoutParams.MATCH_PARENT);
//        this.setHeight(dip2px(activity, 260));
        this.setHeight(dip2px(activity, 200));
        this.setFocusable(true);// 获取焦点
        this.setTouchable(true); // 设置PopupWindow可触摸
        this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);

        layout = (LinearLayout) popView.findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopWindow.this.dismiss();
            }
        });

        // 获取选项卡
        mListView = (ListView) popView.findViewById(R.id.pop_menu_list_view);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(this);
    }


    /**
     * 设置显示的位置
     *
     * @param resourId 这里的x,y值自己调整可以
     */
    public void showLocation(int resourId) {
        showAsDropDown(activity.findViewById(resourId), dip2px(activity, -70),
                dip2px(activity, 10));
    }


    // dip转换为px
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onItemClickListener.onClick(parent, view, position, id);
        this.dismiss();
    }

    // 点击监听接口
    public interface OnItemClickListener {
        void onClick(AdapterView<?> parent, View view, int position, long id);
    }

    // 设置监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setShowRedDot(List<Boolean> showRedDot){
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_popmenu, null);
            AppCompatTextView tv = convertView.findViewById(R.id.item_pop_menu_name);
            tv.setText(mData.get(position));
            return convertView;
        }
    };
}

