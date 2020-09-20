package com.sakurawald.file;

import java.io.IOException;

/**
 * 用于描述[不同的PVZ游戏]的[存档存储忒单]
 */
public class GameVersionConfig_File extends ConfigFile {

	public GameVersionConfig_File(String path, String fineName,
                                  @SuppressWarnings("rawtypes") Class configDataClass)
			throws IllegalArgumentException, IllegalAccessException,
			IOException {
		super(path, fineName, configDataClass);
	}

	/** 具体实例 */
	public GameVersionConfig_Data getSpecificDataInstance() {
		return (GameVersionConfig_Data) super.getConfigDataClassInstance();
	}

}
