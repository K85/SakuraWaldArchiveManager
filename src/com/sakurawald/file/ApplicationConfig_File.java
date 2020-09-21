package com.sakurawald.file;

import java.io.IOException;

public class ApplicationConfig_File extends ConfigFile {

	public ApplicationConfig_File(String path, String fineName,
								  @SuppressWarnings("rawtypes") Class configDataClass)
			throws IllegalArgumentException, IllegalAccessException,
			IOException {
		super(path, fineName, configDataClass);
	}

	/** 具体实例 */
	public ApplicaitonConfig_Data getSpecificDataInstance() {
		return (ApplicaitonConfig_Data) super.getConfigDataClassInstance();
	}

}
