package com.sakurawald.util;

import com.sakurawald.debug.LoggerManager;
import sun.rmi.runtime.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;


/**
 * Java原生的API可用于发送HTTP请求，即java.net.URL、java.net.URLConnection，这些API很好用、很常用，
 * 但不够简便；
 * 
 * 1.通过统一资源定位器（java.net.URL）获取连接器（java.net.URLConnection） 2.设置请求的参数 3.发送请求
 * 4.以输入流的形式获取返回内容 5.关闭输入流
 * 
 * @author LZH
 *
 */
public class HttpConnectionUtil {

	/**
	 * 
	 * @param urlPath
	 *            下载路径
	 * @param downloadDir
	 *            下载存放目录
	 * @return 返回下载文件路径
	 */
	@SuppressWarnings({ "unused" })
	public static String downloadFile(String urlPath, String downloadDir) {
		File file = null;
		String path = null;
		try {
			// 统一资源
			URL url = new URL(urlPath);
			// 连接类的父类，抽象类
			URLConnection urlConnection = url.openConnection();
			// http的连接类
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
			// 设定请求的方法，默认是GET
			httpURLConnection.setRequestMethod("GET");
			// 设置字符编码
			httpURLConnection.setRequestProperty("Charset", "UTF-8");

			// 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
			httpURLConnection.connect();

			// 文件大小
			int fileLength = httpURLConnection.getContentLength();

			// 文件名
			System.out.println("File Length---->" + fileLength);
			LoggerManager.logDebug("[下载系统]", "所要下载的文件: URL_path = " + urlPath
					+ ", fileLength = " + fileLength);

			/**
			 * [!] 判断是否为付费歌曲。 若一首歌曲是付费歌曲，则网易云的音乐下载链接会404
			 * **/
			if (fileLength == 0) {
				LoggerManager.getLogger().error("下载的文件异常, 大小为0!");
				return null;
			}


			BufferedInputStream bin = new BufferedInputStream(
					httpURLConnection.getInputStream());

			path = downloadDir;

			System.out.println(path);

			file = new File(path);

			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			OutputStream out = new FileOutputStream(file);
			int size = 0;
			int len = 0;
			byte[] buf = new byte[1024];
			while ((size = bin.read(buf)) != -1) {
				len += size;
				out.write(buf, 0, size);

				// [!] 仅用于本地测试
				// System.out.println("下载进度-------> " + len * 100 / fileLength +
				// "%\n");
			}
			bin.close();
			out.close();
		} catch (MalformedURLException e) {
			LoggerManager.logException(e);
		} catch (IOException e) {
			LoggerManager.logException(e);
		}

		return null;
	}

	public static void downloadImageFile(String image_URL, String path) {
		URL url = null;
		try {
			url = new URL(image_URL);
			DataInputStream dataInputStream = new DataInputStream(
					url.openStream());

			FileOutputStream fileOutputStream = new FileOutputStream(new File(
					path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
