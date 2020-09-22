package com.sakurawald.timer;

import com.sakurawald.archive.ArchiveSeries;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import com.sakurawald.ui.controller.MainController;

import java.util.Timer;
import java.util.TimerTask;

public class AutoBackupTimer extends TimerTask {

    /**
     * 存储已经运行的时间.
     */
    private int passedTimeMs = 0;

    private final int period = 1000;

    private static AutoBackupTimer instance = null;

    private AutoBackupTimer() {
        // Do nothing.
    }

    public static AutoBackupTimer getInstance() {

        if (instance == null) {
            instance = new AutoBackupTimer();
        }

        return instance;
    }

    @Override
    public void run() {

        // Try-Catch >> Protect
        try {
            // Add Time
            passedTimeMs = passedTimeMs + period;

            // If
            if (passedTimeMs >= FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.time_second * 1000) {

                if (FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.enable == true) {
                    doTask();
                }

                passedTimeMs = 0;
            }
        } catch (Exception e) {
            LoggerManager.reportException(e);
        }

    }

    /**
     * 满足时间条件后执行的方法.
     */
    public void doTask() {


        LoggerManager.logDebug("AutoBackup", "Start");
        MainController mc = MainController.getInstance();

        ArchiveSeries.backup_FromUI();
    }


    public void schedule() {
        new Timer().schedule(this, 0, period);
    }

}
