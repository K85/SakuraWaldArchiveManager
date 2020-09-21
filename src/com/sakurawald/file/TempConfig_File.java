package com.sakurawald.file;

import java.io.IOException;

public class TempConfig_File extends ConfigFile {

    public TempConfig_File(String path, String fineName,
                           @SuppressWarnings("rawtypes") Class configDataClass)
            throws IllegalArgumentException, IllegalAccessException,
            IOException {
        super(path, fineName, configDataClass);
    }

    /**
     * 具体实例
     */
    public TempConfig_Data getSpecificDataInstance() {
        return (TempConfig_Data) super.getConfigDataClassInstance();
    }

}
