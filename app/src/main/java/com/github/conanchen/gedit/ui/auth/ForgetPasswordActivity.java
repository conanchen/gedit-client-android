package com.github.conanchen.gedit.ui.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.back)
    AppCompatImageButton back;
    @BindView(R.id.mobile_desc)
    AppCompatTextView mobileDesc;
    @BindView(R.id.mobile)
    AppCompatEditText mobile;
    @BindView(R.id.verify)
    AppCompatEditText verify;
    @BindView(R.id.question)
    AppCompatTextView question;
    @BindView(R.id.refresh)
    AppCompatTextView refresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pass)
    AppCompatEditText pass;
    @BindView(R.id.pass_again)
    AppCompatEditText passAgain;
    @BindView(R.id.register)
    AppCompatButton register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.register:
                break;
        }
    }
}
