package com.github.conanchen.gedit.util.pay;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/17.
 */
public class AliPayUtil {

    private PayResultCallBack callBack;//支付回调

    private Activity activity;//上下文关联

    private final static int NET_WORK_ERROR_CODE = 1;//网络错误

    private final static int ORDER_INFO_ERROR = 2;//订单信息异常

    private final static int ALI_PAY_ERROR = 3;//支付宝支付异常

    public final static int CANCEL_PAY_ERROR = 4;//取消支付

    private final static int DUPLICATE_SUBMIT_ORDER_ERROR = 5;//订单重复提交

    private final static int PAY_FAIL_ERROR = 6;//支付失败

    private final static int PAY_FAIL_OTHER_ERROR = 7;//支付失败 未知错误


    public AliPayUtil(Activity activity, PayResultCallBack callBack) {
        this.callBack = callBack;
        this.activity = activity;
    }

    /**
     * 支付宝支付
     *
     * @param orderInfo
     */
    public void doAliPay(final String orderInfo) {

        if (!isNetworkConnected()) {
            callBack.OnFail(NET_WORK_ERROR_CODE, "网络连接失败");
            return;
        }

        if (TextUtils.isEmpty(orderInfo)) {
            callBack.OnFail(ORDER_INFO_ERROR, "订单信息为空");
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask payTask = new PayTask(activity);
                Map<String, String> result = payTask.payV2(orderInfo, true);
                String resultStatus = result.get("resultStatus");

                Message message = new Message();
                Bundle bundle = new Bundle();
                message.what = 1;
                bundle.putString("resultStatus", resultStatus);
                bundle.putString("orderInfo", orderInfo);
                message.setData(bundle);
                handler.handleMessage(message);
            }
        };

        new Thread(runnable).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Looper.prepare();
                    if (msg.getData() == null || TextUtils.isEmpty(msg.getData().getString("resultStatus"))) {
                        callBack.OnFail(ALI_PAY_ERROR, "支付异常");
                        Looper.loop();
                        return;
                    }

                    String resultStatus = msg.getData().getString("resultStatus");
                    String orderInfo = msg.getData().getString("orderInfo");

                    if ("9000".equals(resultStatus)) {
                        //支付成功
                        callBack.OnSuccess(orderInfo);
                    } else if ("8000".equals(resultStatus)) {
                        //支付正在处理  正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                        callBack.OnDealing(orderInfo);
                    } else if ("6004".equals(resultStatus)) {
                        //支付正在处理 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                        callBack.OnDealing(orderInfo);
                    } else if ("6002".equals(resultStatus)) {
                        //网络连接失败
                        callBack.OnFail(NET_WORK_ERROR_CODE, "网络连接失败");
                    } else if ("6001".equals(resultStatus)) {
                        //取消支付
                        callBack.OnFail(CANCEL_PAY_ERROR, "取消支付");
                    } else if ("5000".equals(resultStatus)) {
                        //重复提交订单
                        callBack.OnFail(DUPLICATE_SUBMIT_ORDER_ERROR, "重复提交订单");
                    } else if ("4000".equals(resultStatus)) {
                        //支付失败
                        callBack.OnFail(PAY_FAIL_ERROR, "支付失败");
                    } else {
                        //支付失败 其它支付错误
                        callBack.OnFail(PAY_FAIL_OTHER_ERROR, "支付失败");
                    }
                    Looper.loop();
                    break;
            }
        }
    };

    /**
     * 判断网络是否连接
     *
     * @return
     */
    private boolean isNetworkConnected() {
        if (activity != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
