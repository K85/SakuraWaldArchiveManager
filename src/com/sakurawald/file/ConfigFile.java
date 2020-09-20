package com.sakurawald.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.util.FileUtil;


import java.io.*;

/**
 * 描述一个<配置文件对象>
 * 
 * @author K85
 */
public class ConfigFile {

	/**
	 * ApplicationPath永远返回的是 程序运行路径的同级路径相关目录
	 * @return
	 */
	public static String getApplicationConfigPath() {

		String result = null;

		result = FileUtil.getJavaRunPath();
		result = result  + "\\ArchiveManager\\";

		return result;
	}

	@SuppressWarnings("rawtypes")
	/**
	 * 用于<反射>的<配置文件Data对象>
	 */
	private Class configDataClass = null;

	private String filePath = null;
	private String fileName = null;

	/**
	 * 标注该对象是否已完成初始化
	 */
	private boolean hasInit = false;


	public boolean isHasInit() {
		return hasInit;
	}

	/**
	 * 存储<Data类的实例对象>
	 */
	private Object configDataClassInstance = null;

	public ConfigFile(String filePath, String fileName, Class<?> configDataClass)
			throws IllegalArgumentException, IllegalAccessException,
			IOException {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
		this.configDataClass = configDataClass;
	}

	/**
	 * 创建<该Data类的实例对象>
	 */
	@SuppressWarnings("unchecked")
	public void createConfigDataClassInstance() {

		LoggerManager.logDebug("配置文件系统",
				"利用反射创建Data类的实例对象: " + configDataClass.getSimpleName());
		try {
			this.configDataClassInstance = this.configDataClass.newInstance();
		} catch (Exception e) {
			LoggerManager.logException(e);
        }

	}

	/**
	 * 在<本地存储>中<创建空文件>
	 */
	public void createFile() {
		File file = new File(filePath + fileName);

		file.getParentFile().mkdirs();

		try {
			file.createNewFile();
		} catch (IOException e) {
			LoggerManager.logException(e);
		}
	}

	public Class<?> getConfigDataClass() {
		return configDataClass;
	}

	/**
	 * 获取<该Data类的实例对象>
	 * 
	 * @return
	 */
	public Object getConfigDataClassInstance() {

		if (this.configDataClassInstance == null) {
			this.createConfigDataClassInstance();
		}

		return this.configDataClassInstance;
	}

	/**
	 * 获取<该配置文件>的<File对象>
	 */
	public File getFile() {
		return new File(filePath + fileName);
	}

	/**
	 * 获取<该配置文件>的<文件名>
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 获取<该配置文件>的<路径名>
	 * 
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	public void init() throws IllegalArgumentException, IllegalAccessException,
			IOException {

		// 调用方法，给该File的Data的静态变量进行赋值
		if (isExist() == false) {
			createFile();
			writeNormalFile();
		}

		LoggerManager.logDebug("配置文件系统",
				"开始加载<本地配置文件>到<内存>: " + this.getFileName());
		// 加载配置文件
		loadFile();

		this.hasInit = true;
	}

	/**
	 * 判断该<配置文件>是否已经存在
	 */
	public boolean isExist() {
		File file = new File(filePath + fileName);
		return file.exists();
	}

	/**
	 * 从<本地存储>加载<数据>到<内存>
	 */
	@SuppressWarnings("unchecked")
	public void loadFile() throws IllegalArgumentException,
			IllegalAccessException {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(this.getFile()));
			// 从<本地存储>中<读取JSON配置文件数据>
			this.configDataClassInstance = new Gson().fromJson(reader,
					this.configDataClass);
		} catch (FileNotFoundException e) {
			LoggerManager.logException(e);
		} finally {

			// Load完毕立即close掉本地文件流, 以免资源占用.
			try {
				reader.close();
			} catch (IOException e) {
				LoggerManager.logException(e);
			}
		}
	}

	/**
	 * 重新从<本地存储>加载<数据>到<内存>. 该方法会<覆盖><内存>中<已有的数据>.
	 */
	public void reloadFile() {
		LoggerManager.logDebug("配置文件系统", "开始<重载>配置文件: " + this.getFileName());
		try {
			loadFile();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LoggerManager.logException(e);
		}
	}

	/**
	 * 保存<配置文件>到本地存储.
	 */
	public void saveFile() {

		LoggerManager.logDebug("配置文件系统",
				"保存<内存>中的数据到<本地存储>: " + this.getFileName());

		// 定义要写出的本地配置文件
		File file = new File(filePath + fileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			Gson gson = new GsonBuilder().serializeNulls().create();
			String nowJson = gson.toJson(this.getConfigDataClassInstance());

			fos.write(nowJson.getBytes());
			fos.flush();
			fos.close();

		} catch (Exception e) {
			LoggerManager.logException(e);
		}

	}

	/**
	 * 写出<该配置文件>的<默认配置文件数据>.
	 */
	@SuppressWarnings("rawtypes")
	public void writeNormalFile() throws IllegalArgumentException,
			IllegalAccessException, IOException {

		LoggerManager.logDebug("配置文件系统", "开始写出<默认配置文件>: " + this.getFileName());

		// 定义要写出的本地配置文件
		File file = new File(filePath + fileName);
		FileOutputStream fos = new FileOutputStream(file);

		Gson gson = new GsonBuilder().serializeNulls().create();
		String defaultJson = gson.toJson(this.getConfigDataClassInstance());

		fos.write(defaultJson.getBytes());
		fos.flush();
		fos.close();

	}

}
