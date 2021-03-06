package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.grpc.auth.RegisterInfo;
import com.github.conanchen.gedit.user.auth.grpc.Question;
import com.github.conanchen.gedit.user.auth.grpc.RegisterResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep2AnswerResponse;
import com.github.conanchen.gedit.util.JudgeISMobileNo;
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

/**
 * 注册界面   进入界面先获取问题及
 */
@Route(path = "/app/RegisterActivity")
public class RegisterActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    RegisterViewModel registerViewModel;

    @BindView(R.id.mobile)
    AppCompatEditText mEtMobile;
    @BindView(R.id.verify)
    AppCompatEditText mEtVerifyCode;
    @BindView(R.id.question)
    AppCompatTextView mTvQuestionDesc;
    @BindView(R.id.title)
    AppCompatTextView mTvTitle;
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

    @Autowired
    public String TYPE;//判断是注册还是忘记密码

    private static final Gson gson = new Gson();
    private RegisterVerifyPicAdapter mAdapter;
    private List<Question> mData = new ArrayList<>();
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

        setTitle();
        setupViewModel();
        setupRecyclerView();

        setupInputChecker();

    }

    private void setTitle() {
        if ("Register".equals(TYPE)) {
            mTvTitle.setText("注册");
        } else if ("ForgetPass".equals(TYPE)) {
            mTvTitle.setText("修改密码");
        }
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
                    Toast.makeText(RegisterActivity.this, "注册中....", Toast.LENGTH_SHORT).show();

                    String mobile = mEtMobile.getText().toString().trim();
                    String verifyCode = mEtVerifyCode.getText().toString().trim();
                    String password = mEtPass.getText().toString().trim();

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
                if (Status.Code.OK == registerInfo.status.getCode()) {
                    mTvQuestionDesc.setText("registerInfo" + gson.toJson(registerInfo));
                    token = registerInfo.token;
                    mTvQuestionDesc.setText(Strings.isNullOrEmpty(registerInfo.questionTip) ? "找图片" : registerInfo.questionTip);
                    //获取验证数据成功
                    showVerifyPicOrQuestion(registerInfo);
                } else {
                    Toast.makeText(RegisterActivity.this, registerInfo.status.getDetails(), Toast.LENGTH_SHORT).show();
                }


            }
        });
        registerViewModel.getVerify(RegisterInfo.builder().build());


        registerViewModel.getRegisterSmsLiveData().observe(this, new Observer<SmsStep2AnswerResponse>() {
            @Override
            public void onChanged(@Nullable SmsStep2AnswerResponse smsStep2AnswerResponse) {
                // TODO: 2018/1/19 接收到短信  填写短信验证码即可
                mEtVerifyCode.setText(smsStep2AnswerResponse.getStatus().getDetails() + "");
            }
        });

        registerViewModel.getRegisterLiveData().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(@Nullable RegisterResponse registerResponse) {
                if (Status.Code.OK.getNumber() == registerResponse.getStatus().getCode().getNumber()) {
                    if ("Register".equals(TYPE)) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    } else if ("ForgetPass".equals(TYPE)) {
                        Toast.makeText(RegisterActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
                    }
                    registerViewModel.saveRegisterOKAccessToken(registerResponse);
                    finish();
                } else {
                    if ("Register".equals(TYPE)) {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                    } else if ("ForgetPass".equals(TYPE)) {
                        Toast.makeText(RegisterActivity.this, "修改密码失败", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    /**
     * 初始化recyclerView的设置
     */
    private void setupRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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

                List<Question> questions = new ArrayList<>();//装选中的图片对象
                if (mAdapter.isSelected != null && (mAdapter.isSelected.size() > 0)) {
                    int num = 0;
                    for (int i = 0; i < mAdapter.isSelected.size(); i++) {
                        if (mAdapter.isSelected.get(i)) {
                            Question question = mData.get(i);
                            questions.add(question);
                            num = num + 1;
                        }
                    }
                    //判断选中的个数是否大于0
                    if (num <= 0) {
                        Toast.makeText(RegisterActivity.this, "请选择需要验证的图片", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                String tel = mEtMobile.getText().toString().trim();

                if (Strings.isNullOrEmpty(tel)) {
                    Toast.makeText(RegisterActivity.this, "请输入电话号码", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!JudgeISMobileNo.isMobileNO(tel)) {
                    Toast.makeText(RegisterActivity.this, "电话号码格式有误", Toast.LENGTH_LONG).show();
                    return;
                }


                //获取验证码
                RegisterInfo registerInfo = RegisterInfo.builder()
                        .setMobile(tel)
                        .setToken(token)
                        .setQuestion(questions)
                        .build();
                registerViewModel.getSms(registerInfo);
                break;
            case R.id.refresh:
                registerViewModel.getVerify(RegisterInfo.builder().build());
                break;
        }
    }
}
