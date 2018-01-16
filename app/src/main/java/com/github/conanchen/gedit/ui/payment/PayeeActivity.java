package com.github.conanchen.gedit.ui.payment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayeeActivity extends AppCompatActivity {

    @BindView(R.id.code)
    AppCompatImageView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payee);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
