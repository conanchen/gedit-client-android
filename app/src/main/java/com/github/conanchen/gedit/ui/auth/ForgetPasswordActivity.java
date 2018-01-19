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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.grpc.auth.RegisterInfo;
import com.github.conanchen.gedit.user.auth.grpc.Question;
import com.github.conanchen.gedit.user.auth.grpc.RegisterResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep2AnswerResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ForgetPasswordActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    RegisterViewModel registerViewModel;


    @BindView(R.id.mobile)
    AppCompatEditText mEtMobile;
    @BindView(R.id.verify)
    AppCompatEditText mEtVerifyCode;
    @BindView(R.id.question)
    AppCompatTextView mTvQuestionDesc;
    @BindView(R.id.get_sms_code)
    AppCompatTextView mTvGetSmsCode;
    @BindView(R.id.register)
    AppCompatButton mBtRegister;
    @BindView(R.id.pass)
    AppCompatEditText mEtPass;
    @BindView(R.id.pass_again)
    AppCompatEditText mEtPassAgain;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private static final Gson gson = new Gson();
    private RegisterVerifyPicAdapter mAdapter;
    private List<Question> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        setupViewModel();

        setupInputChecker();
    }

    private void setupInputChecker() {
        Observable<CharSequence> observableMobile = RxTextView.textChanges(mEtMobile);
        Observable<CharSequence> observableVerifyCode = RxTextView.textChanges(mEtVerifyCode);
        Observable<CharSequence> observablePass = RxTextView.textChanges(mEtPass);
        Observable<CharSequence> observablePassAgain = RxTextView.textChanges(mEtPassAgain);
        Observable.combineLatest(observableMobile, observableVerifyCode, observablePass, observablePassAgain,
                (mobile, verifyCode, pass, passAgain) -> isTelValid(mobile.toString()) && isVerifyValid(verifyCode.toString(), pass.toString(), passAgain.toString()))
                .subscribe(aBoolean -> RxView.enabled(mBtRegister).accept(aBoolean));

        RxView.clicks(mBtRegister)
                .throttleFirst(3, TimeUnit.SECONDS) //防止3秒内连续点击,或者只使用doOnNext部分
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Toast.makeText(ForgetPasswordActivity.this, "处理中....", Toast.LENGTH_SHORT).show();

                    String mobile = mEtMobile.toString().trim();
                    String verifyCode = mEtVerifyCode.toString().trim();
                    String password = mEtPass.toString().trim();

                    RegisterInfo registerInfo = RegisterInfo.builder()
                            .setMobile(mobile)
                            .setSmscode(verifyCode)
                            .setPassword(password)
                            .build();
                    registerViewModel.getSRegister(registerInfo);
                });
    }

    private boolean isTelValid(String tel) {
        return !Strings.isNullOrEmpty(tel) && tel.length() == 11;
    }

    private boolean isVerifyValid(String verifyCode, String pass, String passAgain) {
        return !Strings.isNullOrEmpty(verifyCode) && verifyCode.length() == 6 && isPassValid(pass, passAgain);
    }

    private boolean isPassValid(String pass, String passAgain) {
        return !Strings.isNullOrEmpty(pass) && !Strings.isNullOrEmpty(passAgain)
                && pass.length() >= 6 && pass.equals(passAgain);
    }

    private void setupViewModel() {
        registerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel.class);

        registerViewModel.getRegisterResponseLiveData().observe(this, new Observer<RegisterInfo>() {
            @Override
            public void onChanged(@Nullable RegisterInfo registerInfo) {
                Log.i("-=-=-=", "获取验证的图片的观察者" + gson.toJson(registerInfo));
                mTvQuestionDesc.setText("registerInfo" + gson.toJson(registerInfo));
                //获取验证数据成功
                showVerifyPicOrQuestion(registerInfo);

            }
        });
        registerViewModel.getVerify(RegisterInfo.builder().build());


        registerViewModel.getRegisterSmsLiveData().observe(this, new Observer<SmsStep2AnswerResponse>() {
            @Override
            public void onChanged(@Nullable SmsStep2AnswerResponse smsStep2AnswerResponse) {
                Log.i("-=-=-=", "获取短信的观察者" + gson.toJson(smsStep2AnswerResponse));
                mEtVerifyCode.setText(smsStep2AnswerResponse.getStatus().getCode() + "");
            }
        });


        registerViewModel.getRegisterLiveData().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(@Nullable RegisterResponse signinResponse) {
                // TODO: 2018/1/18 注册成功就就去登录
                Log.i("-=-=-=", "注册是否成功的观察者" + gson.toJson(signinResponse));
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
            mTvQuestionDesc.setVisibility(View.VISIBLE);
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


    @OnClick({R.id.back, R.id.get_sms_code, R.id.refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
            case R.id.get_sms_code:
                String tel = mEtMobile.getText().toString().trim();
                //获取验证码
                RegisterInfo registerInfo = RegisterInfo.builder()
                        .setMobile(tel)
                        .setToken("进入界面服务器返回")
                        .setQuestionUuid("问题的uuid")
                        .build();
                registerViewModel.getSms(registerInfo);
                break;
            case R.id.refresh:
                registerViewModel.getVerify(RegisterInfo.builder().build());
                break;
        }
    }
}
