package com.sakurawald.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import com.sakurawald.util.NumberUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;


/**
 * 随机获取一张图片的API
 **/
public class QihooRandomImageAPI extends RandomImageAPI {

    /**
     * 单例设计
     **/
    public static QihooRandomImageAPI instance = new QihooRandomImageAPI();

    private QihooRandomImageAPI() {
        // Do nothing.
    }

    /**
     * @return 访问的随机图片URL
     */
    private static String getAssessRandomImageURL() {
        int cid = FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.RandomQihooImage.cid;
        int start = NumberUtil.getRandomNumber(
                FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.RandomQihooImage.start_min,
                FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.RandomQihooImage.start_max);

        String result = "http://wallpaper.apc.360.cn/index.php?%20c=WallPaper&a=getAppsByCategory&cid="
                + cid + "&start=" + start + "&count=1";

        LoggerManager
                .logDebug("随机图片(360壁纸) - API", "Request URL >> " + result, true);

        return result;
    }

    public static QihooRandomImageAPI getInstance() {
        return instance;
    }

    private static String getRandomImageURL_JSON() {

        LoggerManager.logDebug("随机图片(360壁纸) - API", "Get Random Image -> Run");

        String result = null;

        OkHttpClient client = new OkHttpClient();

        Request request = null;
        request = new Request.Builder().url(getAssessRandomImageURL()).get()
                .build();

        Response response = null;

        String JSON = null;
        try {
            response = client.newCall(request).execute();
            JSON = response.body().string();
            result = JSON;

        } catch (SocketTimeoutException e) {
            // 如果是连接超时, 则静默处理.
            LoggerManager.logError(e);
        } catch (IOException e) {
            LoggerManager.logError(e);
        }

        LoggerManager.logDebug("随机图片(360壁纸) - API", "Get Random Image -> Response: JSON = " + JSON);

        if (response != null) {
            /** 关闭Response的body **/
            response.body().close();
        }

        return result;
    }

    /**
     * 获取随机图片的URL在线地址
     *
     * @return 随机图片的URL地址, 失败返回null.
     **/
    @Override
    public String getRandomImageURL() {

        LoggerManager.logDebug("随机图片(360壁纸) - API",
                "Get Random Image URL -> run");

        String result = null;

        /** 获取JSON数据 **/
        String JSON = getRandomImageURL_JSON();

        // 若未找到结果，则返回0
        if (JSON == null) {
            return null;
        }

        JsonParser jParser = new JsonParser();
        JsonObject response_json = (JsonObject) jParser.parse(JSON);// 构造JsonObject对象

        JsonArray data = response_json.get("data").getAsJsonArray();

        for (JsonElement je : data) {
            result = je.getAsJsonObject().get("url").getAsString();
            break;
        }

        LoggerManager.logDebug("随机图片(360壁纸) - API",
                "Get Random Image URL -> Response: Image_URL = " + result);

        return result;
    }

}
