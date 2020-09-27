package com.sakurawald.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sakurawald.data.Sentence;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


/**
 * 一言的API
 **/
public class HitoKotoAPI {

    /**
     * 获取一言Get请求的地址
     **/
    private static String getRequestURL() {
        return "https://v1.hitokoto.cn"
                + FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomSentence.RandomHitoKoto.get_URL_Params;
    }

    /**
     * 返回随机的一句一言
     *
     * @return 获取到的句子, 失败返回[空数据的Sentence].
     **/
    public static Sentence getRandomSentence() {

        /** 获取JSON数据 **/
        String JSON = getRandomSentence_JSON();

        // 若未找到结果，则返回null
        if (JSON == null) {
            return Sentence.getNullSentence();
        }

        /** 解析JSON数据 **/
        JsonParser jParser = new JsonParser();
        JsonObject jo = (JsonObject) jParser.parse(JSON);// 构造JsonObject对象
        JsonObject response = jo.getAsJsonObject();
        int id = response.get("id").getAsInt();
        String content = response.get("hitokoto").getAsString();
        String type = response.get("type").getAsString();
        String from = response.get("from").getAsString();
        String creator = response.get("creator").getAsString();
        String created_at = response.get("created_at").getAsString();

        /** 封装JSON数据 **/
        Sentence result = new Sentence(id, content, type, from, creator,
                created_at);

        LoggerManager.logDebug("HitoKoto", "Get Sentence >> " + result);

        return result;
    }

    private static String getRandomSentence_JSON() {

        LoggerManager.logDebug("HitoKoto", "Get Random Sentence -> Run");

        String result = null;

        OkHttpClient client = new OkHttpClient();

        Request request = null;
        String URL = getRequestURL();
        LoggerManager.logDebug("HitoKoto", "Request URL >> " + URL);
        request = new Request.Builder().url(URL).get().build();

        Response response = null;

        String JSON = null;
        try {
            response = client.newCall(request).execute();
            LoggerManager.logDebug("HitoKoto", "Request Response >> " + response);

            JSON = response.body().string();
            result = JSON;

        } catch (IOException e) {
            LoggerManager.logError(e);
        }

        LoggerManager.logDebug("HitoKoto",
                "Get Random Sentence >> Response: JSON = " + JSON);

        /** 关闭Response的body **/
        if (response != null) {
            response.body().close();
        }

        return result;

    }
}
