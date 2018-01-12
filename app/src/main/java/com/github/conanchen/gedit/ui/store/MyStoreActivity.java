package com.github.conanchen.gedit.ui.store;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/MyStoreActivity")
public class MyStoreActivity extends AppCompatActivity {

    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.add)
    AppCompatTextView add;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        title.setText("我的店铺");

    }

    @OnClick({R.id.left_back, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.add:
                //添加店铺
                break;
        }
    }
}
