package com.sakurawald.util;

import java.io.IOException;
import java.util.Iterator;

import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/** 随机获取一张图片的API **/
public class QihooRandomImageAPI extends RandomImageAPI {

	/** 单例设计 **/
	public static QihooRandomImageAPI instance = new QihooRandomImageAPI();

	private static String getAssessRandomImageURL() {
		int cid = FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.RandomQihooImage.cid;
		int start = NumberUtil.getRandomNumber(
				FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.RandomQihooImage.start_min,
				FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.RandomQihooImage.start_max);

		String result = "http://wallpaper.apc.360.cn/index.php?%20c=WallPaper&a=getAppsByCategory&cid="
				+ cid + "&start=" + start + "&count=1";

		LoggerManager
				.logDebug("随机图片(360壁纸) - API", "本次访问的API地址: " + result, true);

		return result;
	}

	public static QihooRandomImageAPI getInstance() {
		return instance;
	}

	private static String getRandomImageURL_JSON() {

		LoggerManager.logDebug("随机图片(360壁纸) - API", "访问360壁纸资源站 - Run");

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

		} catch (IOException e) {
			LoggerManager.logException(e);
		} finally {

			LoggerManager.logDebug("随机图片(360壁纸) - API", "随机获取图片 - 结果: Code = "
					+ response.message() + ", Response = " + JSON);
		}

		/** 关闭Response的body **/
		response.body().close();

		return result;

	}

	/** 获取随机图片的URL在线地址 **/
	@Override
	public String getRandomImageURL() {

		LoggerManager.logDebug("随机图片(360壁纸) - API", "访问360壁纸资源站 - 开始获取");

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

		Iterator<JsonElement> it = data.iterator();

		while (it.hasNext()) {
			JsonElement je = it.next();
			result = je.getAsJsonObject().get("url").getAsString();
			break;
		}

		LoggerManager.logDebug("随机图片(360壁纸) - API",
				"随机获取图片的URL - 结果: Image_URL = " + result);

		return result;
	}

}
