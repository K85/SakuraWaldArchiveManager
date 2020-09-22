package com.sakurawald.timer;

import com.sakurawald.archive.ArchiveSeries;
import com.sakurawald.archive.GameVersion;
import com.sakurawald.data.ResultBox;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import com.sakurawald.ui.controller.MainController;

import java.util.Timer;
import java.util.TimerTask;

/**
 * [智能自动备份]的Timer类
 */
public class SmartAutoBackupTimer extends TimerTask {

    /**
     * Single Pattern.
     */
    private static SmartAutoBackupTimer instance = null;

    /**
     * 记录上次的僵尸数量.
     */
    private ResultBox<Integer> lastZombiesCount = new ResultBox<Integer>() {

        {
            /**
             * 此处设置1次, 防止SmartAutoBackupTimer在首次初始化时, 误判条件.
             * 合理的条件判断应该是: 初始化时[失败], 然后游戏中为[成功], 脱离游戏状态后再变为[失败]
             */
            this.setFailCount(1);

            /**
             * 此处设置0次, 防止SmartAutoBackupTimer在首次初始化时, 误判条件.
             * 合理的条件判断应该是: 初始化时[失败], 然后游戏中为[成功], 脱离游戏状态后再变为[失败]
             */
            this.setValue(0);
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
        GameVersion gv = MainController.getInstance().getSelectedGameVersion();
        if (gv.getCheatEngine() == null) {
            return;
        }

        /** Smart Auto Backup **/
        ResultBox<Integer> nowZombiesCount_ResultBox = gv.getCheatEngine().getValue();

        // Flag: 表明是否已符合某个"智能自动备份"条件
        boolean flag = false;

        // 判断 >> 是否符合CheatEngine的直接读取
        if (flag == false && nowZombiesCount_ResultBox.getFailCount() > 0) {

            if (lastZombiesCount.getFailCount() == 0) {
                // Start Backup
                LoggerManager.logDebug("SmartAutoBackup", "Match Conditions >> State >> Backup >> Start");
                ArchiveSeries.backup_FromUI();

                // Update Data
                flag = true;
            }
        }

        // 判断 >> 是否符合CheatEngine的TriggerWhenValueChangeTo触发器
        com.sakurawald.data.CheatEngine.TriggerWhenValueChangeTo triggerValueTo = gv.getCheatEngine().getTriggerWhenValueChangeTo();
        if (flag == false && triggerValueTo != null) {

            // 防止持续触发
            if (nowZombiesCount_ResultBox.getValue().equals(lastZombiesCount.getValue()) == false) {
                if (nowZombiesCount_ResultBox.getValue() == triggerValueTo.value) {

                    // 判断触发条件
                    if (nowZombiesCount_ResultBox.getSuccessCount() >= triggerValueTo.successCountCondition) {
                        LoggerManager.logDebug("SmartAutoBackup", "Match Conditions >> Trigger >> Backup >> Start");
                        ArchiveSeries.backup_FromUI();

                        // Update Data
                        flag = true;
                    }

                }

            }

        }

        // Update Data
        lastZombiesCount = nowZombiesCount_ResultBox;

    }


    @Override
    public void run() {

        // Try-Catch >> Protect
        try {
            doTask();
        } catch (Exception e) {
            LoggerManager.reportException(e);
        }

    }

    public void schedule() {
        new Timer().schedule(this, 0, 1000);
    }
}
