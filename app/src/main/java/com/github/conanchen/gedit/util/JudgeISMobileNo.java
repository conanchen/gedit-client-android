package com.github.conanchen.gedit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/6.
 */
public class JudgeISMobileNo {
    /**
     * 判断是不是手机号码
     * @param mobiles
     * @return
     */
    public static boolean  isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
