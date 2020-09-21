package com.sakurawald.api;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 随机获取一张图片的API
 **/
public class SinaRandomImageAPI extends RandomImageAPI {

    /**
     * 单例设计
     **/
    public static SinaRandomImageAPI instance = new SinaRandomImageAPI();

    private SinaRandomImageAPI() {
        // Do nothing.
    }

    /**
     * 存储随机图片API站点
     **/
    private static final ArrayList<String> random_Image_Website_URLs = new ArrayList<String>();

    static {

        init();

    }

    public static SinaRandomImageAPI getInstance() {
        return instance;
    }

    private static String getRandomImageWebsiteURL() {
        Random random = new Random();
        int n = random.nextInt(random_Image_Website_URLs.size());

        LoggerManager.logDebug("随机图片(新浪图库) - API", "Use Random Website URL >> "
                + random_Image_Website_URLs.get(n), true);

        return random_Image_Website_URLs.get(n);
    }

    public static void init() {

        random_Image_Website_URLs.clear();

        String[] random_Image_Websites = FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.RandomSinaImage.random_Image_URLs
                .split("\\|");

        Collections.addAll(random_Image_Website_URLs, random_Image_Websites);
    }

    /**
     * 获取随机图片的URL在线地址
     **/
    @Override
    public String getRandomImageURL() {

        LoggerManager.logDebug("随机图片(新浪图库) - API", "Get Random Image URL -> Run");

        String result = null;

        OkHttpClient client = new OkHttpClient();

        Request request = null;
        request = new Request.Builder().url(getRandomImageWebsiteURL()).get()
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();

            /** 此处获取该Random Page返回的随机图片URL **/
            result = response.request().url().toString();
        } catch (SocketTimeoutException e) {
            // 如果是连接超时, 则静默处理.
            LoggerManager.getLogger().error(e);
        } catch (IOException e) {
            LoggerManager.logException(e);
        } finally {
            LoggerManager.logDebug("随机图片(新浪图库) - API",
                    "Get Random Image URL -> Response: Image_URL = " + result);
        }

        /** 关闭Response的body **/
        response.body().close();

        return result;

    }

}
