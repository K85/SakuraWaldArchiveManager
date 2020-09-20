package com.sakurawald.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/** 随机获取一张图片的API **/
public class SinaRandomImageAPI extends RandomImageAPI {

	/** 单例设计 **/
	public static SinaRandomImageAPI instance = new SinaRandomImageAPI();

	/** 存储随机图片API站点 **/
	private static ArrayList<String> random_Image_Website_URLs = new ArrayList<String>();

	static {

		init();

	}

	public static SinaRandomImageAPI getInstance() {
		return instance;
	}

	private static String getRandomImageWebsiteURL() {
		Random random = new Random();
		int n = random.nextInt(random_Image_Website_URLs.size());

		LoggerManager.logDebug("随机图片(新浪图库) - API", "本次使用的随机图片站点: "
				+ random_Image_Website_URLs.get(n), true);

		return random_Image_Website_URLs.get(n);
	}

	public static void init() {

		random_Image_Website_URLs.clear();

		String[] random_Image_Websites = FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.RandomSinaImage.random_Image_URLs
				.split("\\|");

		for (String website_URL : random_Image_Websites) {
			random_Image_Website_URLs.add(website_URL);
		}
	}

	/** 获取随机图片的URL在线地址 **/
	@Override
	public String getRandomImageURL() {

		LoggerManager.logDebug("随机图片(新浪图库) - API", "随机获取图片的URL - 开始获取");

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
		} catch (IOException e) {
			LoggerManager.logException(e);
		} finally {
			LoggerManager.logDebug("随机图片(新浪图库) - API",
					"随机获取图片的URL - 结果: Image_URL = " + result);
		}

		/** 关闭Response的body **/
		response.body().close();

		return result;

	}

}
