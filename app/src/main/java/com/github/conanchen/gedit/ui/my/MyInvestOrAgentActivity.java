package com.github.conanchen.gedit.ui.my;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.util.ListViewAdaptToScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
@Route(path = "/app/MyInvestOrAgentActivity")
public class MyInvestOrAgentActivity extends AppCompatActivity {

    @BindView(R.id.had_num)
    AppCompatTextView mTextViewAdNum;
    @BindView(R.id.left_num)
    AppCompatTextView mTextViewLeftNum;
    @BindView(R.id.add)
    AppCompatEditText mEditTextAdd;
    @BindView(R.id.need_money)
    AppCompatTextView needMoney;
    @BindView(R.id.submit)
    AppCompatButton submit;
    @BindView(R.id.listView)
    ListViewAdaptToScrollView listView;
    @BindView(R.id.parent_scrollView)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invest_or_agent);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.back, R.id.right, R.id.add_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                //代理
                Toast.makeText(this, "去代理", Toast.LENGTH_LONG).show();
                break;
            case R.id.add_record:
                //增持记录
                Toast.makeText(this, "增持记录", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
