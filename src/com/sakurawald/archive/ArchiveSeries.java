package com.sakurawald.archive;

import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.ArchiveBeanConfig_Data;
import com.sakurawald.file.ArchiveBeanConfig_File;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.FileUtil;
import com.sakurawald.util.JavaFxUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * 描述与ArchiveSeries有关的类.
 */
public class ArchiveSeries {

    private GameVersion owner_GameVersion = null;
    private String archiveSeries_Name = null;

    public ArchiveSeries(GameVersion owner_GameVersion, String archiveSeries_Name) {
        this.owner_GameVersion = owner_GameVersion;
        this.archiveSeries_Name = archiveSeries_Name;
    }

    public static ArchiveSeries generateArchiveSeries(GameVersion gameVersion, String archiveSeriesName) {
        return new ArchiveSeries(gameVersion, archiveSeriesName);
    }

    /**
     * 备份.
     * 为了方便代码编写, 该方法被抽取为static方法, 不打算封装到ArchiveSeries对象中.
     */
    public static void backup_FromUI() {
        // Create Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                MainController mc = MainController.getInstance();

                String temp = mc.button_backup.getText();
                mc.button_backup.setDisable(true);
                mc.button_backup.setText("...");

                // 获取GameVersion
                GameVersion gv = mc.getSelectedGameVersion();

                // 备份该GameVersion
                ArchiveSeries archiveSeries = mc.getSelectedArchiveSeries();

                // 判断选中的[ArchiveSeries]是否空. 若空则创建默认的ArchiveSeries
                if (archiveSeries == null) {
                    LoggerManager.logDebug("该GameVersion没有任何存在的ArchiveSeries, 即将创建默认的ArchiveSeries: GameVersion = " + gv.getVersion_Name());

                    // Create Default ArchiveSeries
                    gv.createDefaultArchiveSeries();

                    // Update
                    archiveSeries = mc.getSelectedArchiveSeries();
                }

                // Backup
                // 此处不新建线程, 因为Backup任务量极小, 没有调度线程的必要.
                archiveSeries.backup();

                // Update
                mc.update_combobox_backup_archive_series();

                // Reset UI
                mc.button_backup.setDisable(false);
                mc.button_backup.setText(temp);

            }
        });

    }

    public GameVersion getOwner_GameVersion() {
        return owner_GameVersion;
    }

    public String getArchiveSeries_Name() {
        return archiveSeries_Name;
    }

    /**
     * 重写该方法以方便快速描述ArchiveSeries.
     */
    public String toString() {
        return this.getArchiveSeries_Name();
    }

    public String getArchiveSeries_Path() {
        return ArchiveBean.getArchiveBeansPath() + this.getOwner_GameVersion().getVersion_Name() + "\\" + this.getArchiveSeries_Name() + "\\";
    }

    /**
     * @return 该ArchiveSeries下的所有ArchiveBean.
     */
    public ArrayList<ArchiveBean> getAllArchiveBeans() {

        ArrayList<ArchiveBean> result = new ArrayList<ArchiveBean>();

        File archiveBeans_Folders = new File(getArchiveSeries_Path());
        for (File archiveBean_File : Objects.requireNonNull(archiveBeans_Folders.listFiles())) {

            if (archiveBean_File.isDirectory() == true) {
                result.add(new ArchiveBean(this, archiveBean_File.getName()));
            }

        }

        LoggerManager.logDebug("Get All ArchiveBean In This ArchiveSeries >> ArchiveSeries = " + this);
        return result;
    }

    /**
     * 修改[本地存储]的ArchiveSeries对应的文件夹名称.
     *
     * @return 是否成功.
     */
    public boolean renameArchiveSeries(String newName) {

        boolean ret = false;

        // Rename
        File f = new File(this.getArchiveSeries_Path());
        ret = f.renameTo(new File(f.getParentFile() + "\\" + newName));

        // Update
        MainController.getInstance().update_combobox_backup_game_version();

        return ret;
    }

    public void renameArchiveSeries_FromUI() {

        TextInputDialog dialog = new TextInputDialog(this.getArchiveSeries_Name());

        JavaFxUtil.DialogTools.setIcon(dialog);
        dialog.setTitle("重命名ArchiveSeries");

        dialog.setHeaderText("ArchiveSeries：" + this.getArchiveSeries_Name());
        dialog.setContentText("该ArchiveSeries的新名称：");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() == true) {

            String newName_ArchiveSeries = result.get();

            if (newName_ArchiveSeries.trim().equalsIgnoreCase("") == true) {
                return;
            }

            if (renameArchiveSeries(newName_ArchiveSeries) == true) {
                /**
                 *  自动选中重命名后的ArchiveSeries.
                 *  注意: JavaFx的Combobox中, 判断自定义对象是否equals是根据toString()来判断的.
                 */
                ArchiveSeries newNameArchiveSeries = ArchiveSeries.generateArchiveSeries(this.getOwner_GameVersion(), newName_ArchiveSeries);
                MainController.getInstance().combobox_backup_archive_series.getSelectionModel().select(newNameArchiveSeries);

            } else {
                JavaFxUtil.DialogTools.alert(Alert.AlertType.ERROR, "重命名失败！", ButtonType.OK).show();
            }

        }
    }

    /**
     * 对该ArchiveSeries进行[备份]
     */
    public void backup() {

        // 获取[该ArchiveSeries所属的GameVersion]的[存档路径]
        File file = new File(this.getOwner_GameVersion().smartlyGetGameArchive_Path());

        /** Check File Attributes.
         * 注意: 实际上这行代码的意义不大, 因为Java无法检测出文件夹的Writable属性,
         * 即使文件夹为只读属性, Writable返回总是true.
         */
        FileUtil.checkFileAttributes(file);

        // Copy GameArchive
        String from = file.getAbsolutePath();
        String to = ArchiveBean.generateSpecificArchiveBean_Path(this);
        LoggerManager.logDebug("Copy Folder >> from = " + from + ", to = " + to);
        try {
            FileUtil.copyFolder(from, to);
        } catch (IOException e) {
            LoggerManager.reportException(e);
        }

        // Create ArchiveBeanConfig.json
        try {
            ArchiveBeanConfig_File abcf = new ArchiveBeanConfig_File(to, "ArchiveBeanConfig.json",
                    ArchiveBeanConfig_Data.class);

            abcf.init();
        } catch (IllegalAccessException | IOException e) {
            LoggerManager.reportException(e);
        }

    }


}
