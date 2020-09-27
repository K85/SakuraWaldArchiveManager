package com.sakurawald.api;

import com.google.gson.*;
import com.sakurawald.data.Poetry;
import com.sakurawald.debug.LoggerManager;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

public class JinRiShiCiAPI {

    /**
     * 随机获取一首古诗
     *
     * @return 返回Poerty对象, 失败返回[空数据的Poetry对象].
     */
    public static Poetry getPoetry() {

        /** Connect **/
        String user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
        String URL = "https://v2.jinrishici.com/one.json";
        Connection conn = Jsoup.connect(URL);

        // 修改http包中的header,伪装成浏览器进行抓取
        conn.header("User-Agent", user_agent);

        /*
            请求附带Token, 该部分暂时删除
            String token = null;
            conn.header("X-User-Token", token);*/

        Response response_JSON = null;
        try {
            response_JSON = conn.ignoreContentType(true).execute();
        } catch (IOException e) {
            LoggerManager.logError(e);
            return Poetry.getNullPoetry();
        }

        /** 进行JSON解析 **/
        String keySentence = null;
        String poetry_Title = null;
        String poetry_Dynasty = null;
        String poetry_Author = null;
        String poetry_Content = null;
        String warning = null;
        try {

            String result_JSON = response_JSON.body();
            LoggerManager.logDebug("JinRiShiCi", "Get JSON >> " + result_JSON);
            JsonParser jParser = new JsonParser();
            JsonObject json = (JsonObject) jParser.parse(result_JSON);// 构造JsonObject对象
            JsonObject data = json.get("data").getAsJsonObject();
            keySentence = data.get("content")
                    .getAsString();
            JsonObject origin = data.get("origin").getAsJsonObject();
            poetry_Title = origin.get("title").getAsString();
            poetry_Dynasty = origin.get("dynasty").getAsString();
            poetry_Author = origin.get("author").getAsString();

            // Analyse Content.
            ArrayList<String> arr = getContent(origin.get("content").getAsJsonArray());
            poetry_Content = getContent(arr);

            /** 检测warning **/
            warning = null;
            JsonElement je = json.get("warning");
            if (je instanceof JsonNull) {
                warning = "没有受到warning警告!";
            } else {
                warning = je.getAsString();
            }
        } catch (Exception e) {
            LoggerManager.reportException(e);
        }

        LoggerManager.logDebug("JinRiShiCi", "Response >> keySentence = " + keySentence);
        LoggerManager.logDebug("JinRiShiCi", "Response >> warning = "
                + warning);

        return new Poetry(keySentence, poetry_Title, poetry_Dynasty, poetry_Author, poetry_Content, null);
    }

    private static ArrayList<String> getContent(JsonArray contentArray) {

        ArrayList<String> result = new ArrayList<String>();

        for (JsonElement jsonElement : contentArray) {
            String sentencePart = jsonElement.getAsString();
            result.add(sentencePart);
        }

        return result;
    }

    private static String getContent(ArrayList<String> contentArray) {

        StringBuilder sb = new StringBuilder();

        for (String sentencePart : contentArray) {
            sb.append(sentencePart);
            sb.append("\n");
        }

        return sb.toString().trim();
    }


}
