package com.sakurawald.archive;

import com.sakurawald.Main;
import com.sakurawald.data.GameVersion;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.ArchiveBeanConfig_Data;
import com.sakurawald.file.ArchiveBeanConfig_File;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.FileUtil;
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

    public GameVersion getOwner_GameVersion() {
        return owner_GameVersion;
    }

    public String getArchiveSeries_Name() {
        return archiveSeries_Name;
    }

    public ArchiveSeries(GameVersion owner_GameVersion, String archiveSeries_Name) {
        this.owner_GameVersion = owner_GameVersion;
        this.archiveSeries_Name = archiveSeries_Name;
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
        dialog.setTitle("重命名ArchiveSeries");

//        dialog.setHeaderText("每一个ArchiveSeries都有一个名称，\n但请注意名称应符合Windows系统的文件命名规范！");
        dialog.setContentText("该ArchiveSeries的新名称：" + this.getArchiveSeries_Name());

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() == true) {

            String input = result.get();

            if (input.trim().equalsIgnoreCase("") == true) {
                return;
            }

            if (renameArchiveSeries(input) == false) {
                new Alert(Alert.AlertType.WARNING, "重命名失败！", ButtonType.OK).show();
            }

            // 自动选中重命名后的ArchiveSeries
            MainController.getInstance().combobox_backup_archive_series.getSelectionModel().select(new ArchiveSeries(this.getOwner_GameVersion(), input));

        }
    }

    /**
     * 备份.
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
                archiveSeries.backup();

                // Update
                mc.update_combobox_backup_archive_series();

                // Reset UI
                mc.button_backup.setDisable(false);
                mc.button_backup.setText(temp);

            }
        });

    }

    /**
     * 对该ArchiveSeries进行[备份]
     */
    public void backup() {

        // 获取[该ArchiveSeries所属的GameVersion]的[存档路径]
        File file = new File(this.getOwner_GameVersion().smartlyGetGameArchive_Path());

        // Copy GameArchive
        String src = file.getAbsolutePath();
        String tar = ArchiveBean.generateSpecificArchiveBean_Path(this);
        LoggerManager.getLogger().info("复制文件夹: source = " + src + ", target = " + tar);
        try {
            FileUtil.copyFolder(src, tar);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }

        // Create ArchiveBeanConfig.json
        try {
            ArchiveBeanConfig_File abcf = new ArchiveBeanConfig_File(tar, "ArchiveBeanConfig.json",
                    ArchiveBeanConfig_Data.class);

            abcf.init();
        } catch (IllegalAccessException | IOException e) {
            LoggerManager.logException(e);
        }

    }


}
