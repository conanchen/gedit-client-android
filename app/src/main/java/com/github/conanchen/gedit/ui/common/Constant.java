package com.github.conanchen.gedit.ui.common;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class Constant {

    public static int selectMode = PictureConfig.MULTIPLE;//单选 or 多选
    public static int MAX_SELECT_NUM = 9;// 图片最大可选数量
    public static int maxSelectNumLicence = 1;// 图片最大可选数量
    public static int selectType = PictureMimeType.ofImage();//图片or视频

    public static int selectModeVideo = PictureConfig.SINGLE;//单选 or 多选
    public static int maxSelectNumVideo = 1;//选择视频的数量
    public static int selectTypeVideo = PictureMimeType.ofVideo();//图片or视频


    public static boolean isShow = true;//是否显示拍照选项
    public static int copyMode = 1;
    public static boolean enablePreview = true;// 是否打开预览选项
    public static int cropW = 0;//cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
    public static int cropH = 0;// cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
    public static int maxB = 101200;// 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb
    public static int compressW = 0;// 压缩宽 如果值大于图片原始宽高无效
    public static int compressH = 0;// 压缩高 如果值大于图片原始宽高无效
    public static boolean isCompress = true;//是否压缩
    public static boolean isCheckNumMode = false;// 是否显示QQ选择风格(带数字效果)
    public static int compressFlag = 1;// 1 系统自带压缩 2 luban压缩
    public static boolean mode = false;// 启动相册模式
    public static boolean clickVideo = false;// 是否开启点击声音
}
