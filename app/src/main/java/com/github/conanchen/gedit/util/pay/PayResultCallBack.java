package com.github.conanchen.gedit.util.pay;

/**
 * Created by Administrator on 2017/5/17.
 */
public interface PayResultCallBack {
    void OnSuccess(String orderInfo);

    void OnDealing(String orderInfo);

    void OnFail(int code, String error_message);
}
