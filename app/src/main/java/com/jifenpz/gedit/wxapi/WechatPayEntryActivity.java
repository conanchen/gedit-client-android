package com.jifenpz.gedit.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.github.conanchen.gedit.util.pay.WechatPay;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


/**
 * Created by Administrator on 2017/6/21.
 */
public class WechatPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (WechatPay.getInstance() != null) {
            WechatPay.getInstance().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (WechatPay.getInstance() != null) {
            WechatPay.getInstance().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (WechatPay.getInstance() != null) {

                WechatPay.getInstance().onResp(resp.errCode);
                finish();
            }
        }

    }
}
