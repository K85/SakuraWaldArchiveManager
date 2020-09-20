package com.sakurawald.timer;

import com.sakurawald.Main;
import com.sakurawald.data.GameVersion;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import com.sakurawald.ui.controller.MainController;

import java.util.Timer;
import java.util.TimerTask;

/**
 * [智能自动备份]的Timer类
 */
public class SmartAutoBackupTimer extends TimerTask {

    private static SmartAutoBackupTimer instance = null;

    /**
     * 记录上次的僵尸数量
     */
    private int lastZombiesCount = 0;

    public static SmartAutoBackupTimer getInstance() {

        if (instance == null) {
            instance = new SmartAutoBackupTimer();
        }

        return instance;
    }


    @Override
    public void run() {

        // Is Enable
        if (FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.smartAutoBackup == false) {
            return;
        }

        // Get GameVersion
        MainController mc = Main.loader.getController();
        GameVersion gv = mc.combobox_backup_game_version.getSelectionModel().getSelectedItem();
        if (gv.getCheatEngine() == null) {
            return;
        }

        /** Smart Auto Backup **/

        int nowZombiesCount = 0;
        nowZombiesCount = gv.getCheatEngine().getValue();

        if (nowZombiesCount == -1) {

            if (lastZombiesCount != 1) {
                // Start Backup
                LoggerManager.logDebug("智能自动备份", "符合条件 >> 开始执行智能自动备份");
                mc.backup();
            }

        }

        lastZombiesCount = nowZombiesCount;
    }

    public void schedule() {
        new Timer().schedule(this, 0, 1000);
    }
}
