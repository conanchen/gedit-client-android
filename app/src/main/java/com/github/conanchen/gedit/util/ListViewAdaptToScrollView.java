package com.github.conanchen.gedit.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/1/12.
 */
public class ListViewAdaptToScrollView extends ListView {
    public ListViewAdaptToScrollView(Context context) {
        super(context);
    }

    public ListViewAdaptToScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewAdaptToScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } catch (Exception e) {

        }
    }
}
