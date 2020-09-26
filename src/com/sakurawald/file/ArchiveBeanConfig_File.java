package com.sakurawald.file;

import com.sakurawald.debug.LoggerManager;

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

    public void init() throws IllegalArgumentException, IllegalAccessException,
            IOException {

        // 调用方法, 给该File的Data的静态变量进行赋值
        if (isExist() == false) {
            createFile();
            writeNormalFile();
        }

        LoggerManager.logDebug("FileSystem",
                "Load Local File to Memory >> " + this.getFileName(), false);

        // 从本地存储加载相应的配置文件
        loadFile();

        // Set Flag.
        super.setHasInit(true);

    }
}
