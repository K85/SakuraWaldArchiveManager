package com.sakurawald.util;

import java.io.IOException;

import com.sakurawald.data.Sentence;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/** 针对一言的API **/
public class HitoKotoAPI {

	/** 获取一言Get请求的地址 **/
	private static String getGetURL() {
		// [!] 无论如何，这里都要请求1个原创音乐。不然，ugc_total_count永远为0
		return "https://v1.hitokoto.cn"
				+ FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomSentence.RandomHitoKoto.get_URL_Params;
	}

	/** 返回随机的一句一言 **/
	public static Sentence getRandomSentence() {

		/** 获取JSON数据 **/

		String JSON = getRandomSentence_JSON();

		// 若未找到结果，则返回0
		if (JSON == null) {
			return null;
		}

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

		LoggerManager.logDebug("一言 - API", "获取到的句子: " + result);

		return result;
	}

	private static String getRandomSentence_JSON() {

		LoggerManager.logDebug("一言 - API", "随机获取句子 - Run");

		String result = null;

		OkHttpClient client = new OkHttpClient();

		Request request = null;
		String URL = getGetURL();
		LoggerManager.logDebug("一言 - API", "一言 >> 请求URL: " + URL);
		request = new Request.Builder().url(URL).get().build();

		Response response = null;

		String JSON = null;
		try {
			response = client.newCall(request).execute();
			LoggerManager.logDebug("一言 - API", "一言 >> 请求结果: " + response);

			JSON = response.body().string();
			result = JSON;


		} catch (IOException e) {
			LoggerManager.logException(e);
		} finally {

			LoggerManager.logDebug("一言 - API",
					"随机获取句子 - 结果: Code = " + response.message()
							+ ", Response = " + JSON);
		}

		/** 关闭Response的body **/
		response.body().close();

		return result;

	}
}
