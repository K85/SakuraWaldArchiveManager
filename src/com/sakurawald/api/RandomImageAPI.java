package com.sakurawald.api;

import com.sakurawald.debug.LoggerManager;
import com.sakurawald.util.HttpConnectionUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 随机获取一张图片的API
 **/
public abstract class RandomImageAPI {

    /**
     * 通过图片的URL来获取图片的保存名称
     **/
    public static String getSaveImageFileName(String image_URL) {
        URL url = null;

        try {
            url = new URL(image_URL);
        } catch (MalformedURLException e) {
            LoggerManager.reportException(e);
        }

        File file = new File(url.getFile());

        return url.getHost() + "#" + file.getName();
    }

    public static void saveImage(String image_URL, String save_path) {
        LoggerManager.logDebug("随机图片", "Download Image >> image_URL = " + image_URL
                + ", save_path = " + save_path, true);

        // Prevent NPE.
        if (image_URL == null) {
            LoggerManager.logDebug("随机图片", "Download Image >> image_URL is null. >> Cancel");
            return;
        }

        HttpConnectionUtil.downloadImageFile(image_URL, save_path);
    }

    /**
     * 获取随机图片的URL在线地址
     **/
    public abstract String getRandomImageURL();

}
