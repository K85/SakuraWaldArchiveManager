package com.sakurawald.file;


import com.sakurawald.debug.LoggerManager;

import java.io.File;
import java.io.IOException;

//用于管理所有的本地配置文件，包括每一个配置文件的命名
public class FileManager {

    /**
     * 单例模式
     */
    private static FileManager instance = null;

    public static FileManager getInstance() {

        if (instance == null) {
            instance = new FileManager();
        }

        return instance;
    }

    private FileManager() {
        // Do nothing.
    }

    /**
     * 配置文件列表
     **/
    public static ApplicationConfig_File applicationConfig_File = null;
    public static GameVersionConfig_File gameVersionConfig_File = null;
    public static TempConfig_File tempConfig_File = null;

    /**
     * 调用本方法来<初始化>配置文件系统.
     */
    public void init() throws IllegalArgumentException,
            IllegalAccessException, IOException {

        LoggerManager.logDebug("FileSystem", "Init All Configs...", true);

        // Create Folder
        LoggerManager.logDebug("FileSystem", "Create ArchiveBeans Folder", true);
        new File(ConfigFile.getApplicationConfigPath() + "\\ArchiveBeans").mkdirs();

        // ApplicationConfig.json
        LoggerManager.logDebug("FileSystem", "Init >> Application.json", true);
        applicationConfig_File = new ApplicationConfig_File(ConfigFile.getApplicationConfigPath(),
                "ApplicationConfig.json", ApplicaitonConfig_Data.class);
        applicationConfig_File.init();

        // GameVersionConfig.json
        LoggerManager.logDebug("FileSystem", "Init >> GameVersionConfig.json", true);
        gameVersionConfig_File = new GameVersionConfig_File(ConfigFile.getApplicationConfigPath(),
                "GameVersionConfig.json", GameVersionConfig_Data.class);
        gameVersionConfig_File.init();

        // TempConfig.json
        LoggerManager.logDebug("FileSystem", "Init >> TempConfig.json", true);
        tempConfig_File = new TempConfig_File(ConfigFile.getApplicationConfigPath(),
                "TempConfig.json", TempConfig_Data.class);
        tempConfig_File.init();

    }

}
