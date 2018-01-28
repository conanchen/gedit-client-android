package com.github.conanchen.gedit.util.pay;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/21.
 */
public class WxPay {

    private static WxPay mWXPay;
    private IWXAPI mWXApi;
    private String mPayParam;
    private WXPayResultCallBack mCallback;

    public static final int NO_OR_LOW_WX = 1;   //未安装微信或微信版本过低
    public static final int ERROR_PAY_PARAM = 2;  //支付参数错误
    public static final int ERROR_PAY = 3;  //支付失败

    public interface WXPayResultCallBack {
        void onSuccess(); //支付成功

        void onError(int error_code);   //支付失败

        void onCancel();    //支付取消
    }

    public WxPay(Context context) {
        mWXApi = WXAPIFactory.createWXAPI(context, null);
        mWXApi.registerApp(getWeixinKey(context));//appId
    }

    public static void init(Context context) {
        if (mWXPay == null) {
            mWXPay = new WxPay(context);
        }
    }

    public static WxPay getInstance() {
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    public void doPay(String pay_param, WXPayResultCallBack callback) {
        mPayParam = pay_param;
        mCallback = callback;

        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            Log.i("-=-=-=", "不支持微信支付");
            return;
        }

        JSONObject param = null;
        try {
            param = new JSONObject(mPayParam);
        } catch (JSONException e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            Log.i("weixinpay", "订单号有问题");
            return;
        }
        if (TextUtils.isEmpty(param.optString("appid")) || TextUtils.isEmpty(param.optString("partnerid"))
                || TextUtils.isEmpty(param.optString("prepayid")) ||
                TextUtils.isEmpty(param.optString("noncestr")) || TextUtils.isEmpty(param.optString("timestamp")) ||
                TextUtils.isEmpty(param.optString("sign"))) {
            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = param.optString("appid");
        req.partnerId = param.optString("partnerid");
        req.prepayId = param.optString("prepayid");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = param.optString("noncestr");
        req.timeStamp = param.optString("timestamp");
        req.sign = param.optString("sign");

        mWXApi.sendReq(req);
        Log.i("-=调起微信", "asdasdasdsa");
    }

    //支付回调响应
    public void onResp(int error_code) {
        Log.i("-=-=-=", "" + error_code);
        if (mCallback == null) {
            return;
        }

        if (error_code == 0) {   //成功
            mCallback.onSuccess();
        } else if (error_code == -1) {   //错误
            mCallback.onError(ERROR_PAY);
        } else if (error_code == -2) {   //取消
            mCallback.onCancel();
        }

        mCallback = null;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    /**
     * 读取微信的AppKey
     *
     * @param context context
     * @return String
     */
    public String getWeixinKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);

            if (null != appInfo) {
                Bundle bundle = appInfo.metaData;
                String key = null;
                if (null != bundle) {
                    key = String.valueOf(bundle.get("weixin_key"));
                    Log.i("-=-=-=", "key :" + key);
                }
                if (!TextUtils.isEmpty(key)) {
                    return key;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
