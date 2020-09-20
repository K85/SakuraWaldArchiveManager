package com.sakurawald.api;

import com.google.gson.*;
import com.sakurawald.data.Poetry;
import com.sakurawald.debug.LoggerManager;
import com.sun.deploy.trace.LoggerTraceListener;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class JinRiShiCiAPI {

    // 通过token访问今日诗词的官网，直接在今日诗词官方API获取关键诗句
    public static Poetry getPoetry() {

        /** 连接网络 **/
        String user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
        String URL = "https://v2.jinrishici.com/one.json";
        Connection conn = Jsoup.connect(URL);

    // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", user_agent);
    //        String token = null;
    //        conn.header("X-User-Token", token);

        Response response_JSON = null;
        try {
            response_JSON = conn.ignoreContentType(true).execute();
        } catch (IOException e) {
            LoggerManager.logException(e);
            return new Poetry("诗词获取失败");
        }



        String keySentence = null;
        String poetry_Title = null;
        String poetry_Dynasty = null;
        String poetry_Author = null;
        String poetry_Content = null;
        String warning = null;
        try {
            /** 进行JSON解析 **/
            String result_JSON = response_JSON.body();
            LoggerManager.logDebug("网络系统", "今日诗词 >> 获取到的JSON文本: " + result_JSON);
            JsonParser jParser = new JsonParser();
            JsonObject json = (JsonObject) jParser.parse(result_JSON);// 构造JsonObject对象

            JsonObject data = json.get("data").getAsJsonObject();

             keySentence = data.get("content")
                    .getAsString();

             JsonObject origin = data.get("origin").getAsJsonObject();

             poetry_Title = origin.get("title").getAsString();
             poetry_Dynasty = origin.get("dynasty").getAsString();
             poetry_Author = origin.get("author").getAsString();

             ArrayList<String> arr = getContent(origin.get("content").getAsJsonArray());
             poetry_Content = getContent(arr);

            /** 判断warning是否为null **/
             warning = null;
            JsonElement je = json.get("warning");
            if (je instanceof JsonNull) {
                warning = "没有受到warning警告!";
            } else {
                warning = je.getAsString();
            }
        } catch(Exception e) {
            LoggerManager.logException(e);
        }

        LoggerManager.logDebug("网络系统", "今日诗词 >> 发送请求：keySentence = " + keySentence);
        LoggerManager.logDebug("网络系统", "今日诗词 >> 发送请求：warning = "
                + warning);

        return new Poetry(keySentence, poetry_Title, poetry_Dynasty, poetry_Author, poetry_Content, null);
    }

    private static ArrayList<String> getContent(JsonArray contentArray) {

        ArrayList<String> result = new ArrayList<String>();

        Iterator<JsonElement> it = contentArray.iterator();

        while (it.hasNext()) {
            String sentencePart =  it.next().getAsString();
            result.add(sentencePart);
        }

        return result;
    }

    private static String getContent(ArrayList<String> contentArray) {

        StringBuffer sb = new StringBuffer();

        for (String sentencePart : contentArray) {
            sb.append(sentencePart);
            sb.append("\n");
        }

        return sb.toString().trim();
    }



}
