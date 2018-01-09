package com.github.conanchen.gedit.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */
public class PictureUtil {

    /**
     * 1.质量压缩
     *
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 20;
        while (baos.toByteArray().length / 1024 > 20) {  //循环判断如果压缩后图片是否大于20kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中


        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath 图片路径
     * @return
     */
    private static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例

        newOpts.inSampleSize = 1;//设置缩放比例

        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        //进行 质量压缩
        return centerSquareScaleBitmap(bitmap);//压缩好比例大小后   再截取正方形 再进行质量压缩
    }

    /**
     * 得到临时图片路径
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String bitmapToPath(String filePath) throws IOException {

        Bitmap bm = getimage(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);

        //得到文件名
        String imgName = getfilepath(filePath);
        //得到存放路径
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImgTmp";
        //获取 sdcard的跟目录

        File parent = new File(sdPath);
        if (!parent.exists()) {
            //创建路径
            parent.mkdirs();
        }
        //写入 临时文件
        File file = new File(parent, imgName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
        baos.close();
        //返回图片路径
        return sdPath + "/" + imgName;

    }

    public static String bitmapToPathVideoFrame(String filePath) throws IOException {

        Bitmap bm = getimageVideoFrame(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);

        //得到文件名
        String imgName = getfilepath(filePath);
        //得到存放路径
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImgTmp";
        //获取 sdcard的跟目录

        File parent = new File(sdPath);
        if (!parent.exists()) {
            //创建路径
            parent.mkdirs();
        }
        //写入 临时文件
        File file = new File(parent, imgName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
        baos.close();
        //返回图片路径
        return sdPath + "/" + imgName;

    }

    private static Bitmap getimageVideoFrame(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例

        newOpts.inSampleSize = 1;//设置缩放比例

        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        //进行 质量压缩
        return bitmap;//压缩好比例大小后   再截取正方形 再进行质量压缩
    }


    /**
     * @param path
     * @return
     */
    private static String getfilepath(String path) {
        return System.currentTimeMillis() + getExtensionName(path);
    }


    /*
     * 获取文件扩展名
     */
    private static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot, filename.length());
            }
        }
        return filename;
    }


    /**
     * 删除临时文件
     *
     * @param imgs
     */
    public static void deleteImgTmp(List<String> imgs) {

        for (String string : imgs) {
            if (!TextUtils.isEmpty(string)){
                File file = new File(string);
                if (file.exists()) {
                    file.delete();
                }
            }
        }

    }

    public Bitmap getBitmap(String url, int displaypixels) throws MalformedURLException, IOException {
        Bitmap bmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        InputStream stream = new URL(url).openStream();
        byte[] bytes = getBytes(stream);
//这3句是处理图片溢出的begin( 如果不需要处理溢出直接 opts.inSampleSize=1;)
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, displaypixels);
//end
        opts.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        return bmp;
    }

    /**
     * 数据流转成btyle[]数组
     */
    private byte[] getBytes(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[2048];
        int len = 0;
        try {
            while ((len = is.read(b, 0, 2048)) != -1) {
                baos.write(b, 0, len);
                baos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /****
     * 处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
     */
    private int computeSampleSize(BitmapFactory.Options options,
                                  int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    /**
     * @param bitmap 原图
     * @return 缩放截取正中部分后的位图。
     */

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap) {

        //edgeLength  希望得到的正方形部分的边长
        int edgeLength = 300;

        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        } else {
            //判断图片高宽谁大
            if (widthOrg > heightOrg) {

                //从图中截取正中间的正方形部分。
                int xTopLeft = (widthOrg - heightOrg) / 2;
                int yTopLeft = 0;

                try {
                    result = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, widthOrg, widthOrg);
                } catch (Exception e) {
                    return bitmap;
                }

            } else if (heightOrg > widthOrg) {

                //从图中截取正中间的正方形部分。
                int xTopLeft = 0;
                int yTopLeft = (heightOrg - widthOrg) / 2;

                try {
                    result = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, heightOrg, heightOrg);
                } catch (Exception e) {
                    return bitmap;
                }
            }
        }

        return compressImage(result);
    }

    /**
     * 保存文件
     *
     * @param bm
     * @throws IOException
     */
    public static String saveFile(Bitmap bm) {

        BufferedOutputStream bos = null;
        String path = null;//文件输出路径
        try {
            Date date = new Date();
            long currentTime = date.getTime();
            //保存路径
           path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.sxd.moment/image/" + currentTime + ".jpg";

            File myCaptureFile = new File(path);
            bos= new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        } catch (Exception e) {

        } finally {
            try {
                bos.flush();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String imagePath = null;
        try{
           imagePath = bitmapToPathVideoFrame(path);
        }catch (Exception e){

        }

        return imagePath;
    }

}
