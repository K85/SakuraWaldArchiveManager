package com.sakurawald.timer;

import com.sakurawald.Main;
import com.sakurawald.data.CheatEngine;
import com.sakurawald.data.GameVersion;
import com.sakurawald.data.ResultBox;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import com.sakurawald.ui.controller.MainController;

import javax.xml.transform.Result;
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
    private ResultBox<Integer> lastZombiesCount = new ResultBox<Integer>(new Integer(-1)) {

        {
            /**
             * 此处设置1次, 防止SmartAutoBackupTimer在首次初始化时, 误判条件.
             * 合理的条件判断应该是: 初始化时[失败], 然后游戏中为[成功], 脱离游戏状态后再变为[失败]
              */
            this.setFailCount(1);
        }

    };

    private SmartAutoBackupTimer() {
        // Do nothing.
    }


    public static SmartAutoBackupTimer getInstance() {

        if (instance == null) {
            instance = new SmartAutoBackupTimer();
        }

        return instance;
    }

    public void doTask() {
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
        ResultBox<Integer> nowZombiesCount_ResultBox = null;
        nowZombiesCount_ResultBox = gv.getCheatEngine().getValue();

        if (nowZombiesCount_ResultBox.getFailCount() > 0) {

            if (lastZombiesCount.getFailCount() == 0) {
                // Start Backup
                LoggerManager.logDebug("智能自动备份", "符合条件 >> 开始执行智能自动备份");
                mc.backup();
            }

        }

        lastZombiesCount = nowZombiesCount_ResultBox;
    }


    @Override
    public void run() {

        // Try-Catch >> Protect
        try {
            doTask();
        } catch( Exception e) {
            LoggerManager.logException(e);
        }

    }

    public void schedule() {
        new Timer().schedule(this, 0, 1000);
    }
}
