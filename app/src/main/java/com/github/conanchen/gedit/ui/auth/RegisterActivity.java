package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.auth.RegisterInfo;
import com.github.conanchen.gedit.user.auth.grpc.Question;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册界面   进入界面先获取问题及
 */
@Route(path = "/app/RegisterActivity")
public class RegisterActivity extends BaseActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    RegisterViewModel registerViewModel;
    @BindView(R.id.mobile)
    AppCompatEditText mobile;
    @BindView(R.id.verify)
    AppCompatEditText verify;
    @BindView(R.id.question)
    AppCompatTextView textViewQuestion;
    @BindView(R.id.register)
    AppCompatButton register;
    private static final Gson gson = new Gson();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private RegisterVerifyPicAdapter mAdapter;
    private List<Question> mData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupViewModel();
        setupRecyclerView();
        RegisterInfo registerInfo = RegisterInfo.builder().build();
        registerViewModel.getVerify(registerInfo);
    }


    private void setupViewModel() {
        registerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel.class);
        registerViewModel.getRegisterResponseLiveData().observe(this, new Observer<RegisterInfo>() {
            @Override
            public void onChanged(@Nullable RegisterInfo registerInfo) {
                textViewQuestion.setText("registerInfo" + gson.toJson(registerInfo));
                //获取验证数据成功
                showVerifyPicOrQuestion(registerInfo);

            }
        });
    }

    /**
     * 初始化recyclerView的设置
     */
    private void setupRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        mAdapter = new RegisterVerifyPicAdapter(this, mData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNormal();
    }

    /**
     * 显示问题及图片
     *
     * @param registerInfo
     */
    private void showVerifyPicOrQuestion(RegisterInfo registerInfo) {
        List<Question> question = registerInfo.question;
        if (question != null && !question.isEmpty()) {
            textViewQuestion.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            List<Question> q = new ArrayList<>();
            for (int i = 0; i < question.size(); i++) {
                q.add(question.get(i));
            }
            if (!q.isEmpty()) {
                mData.clear();
                mData.addAll(q);
                mAdapter.setNormal();
            }
        }
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
