package com.sakurawald.file;

import java.io.IOException;

public class ArchiveBeanConfig_File extends ConfigFile {

    public ArchiveBeanConfig_File(String path, String fineName,
                                  @SuppressWarnings("rawtypes") Class configDataClass)
            throws IllegalArgumentException, IllegalAccessException,
            IOException {
        super(path, fineName, configDataClass);
    }

    /**
     * 具体实例
     */
    public ArchiveBeanConfig_Data getSpecificDataInstance() {
        return (ArchiveBeanConfig_Data) super.getConfigDataClassInstance();
    }

}
