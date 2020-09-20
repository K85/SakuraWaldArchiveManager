package com.sakurawald.archive;

import com.sakurawald.Main;
import com.sakurawald.data.GameVersion;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.ArchiveBeanConfig_Data;
import com.sakurawald.file.ArchiveBeanConfig_File;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.FileUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 描述与ArchiveSeries有关的类
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

    public String toString() {
        return this.getArchiveSeries_Name();
    }

    public String getArchiveSeriesPath() {
        return ArchiveBean.getArchiveBeansPath() + this.getOwner_GameVersion().getVersion_Name() + "\\" + this.getArchiveSeries_Name() + "\\";
    }

    public ArrayList<ArchiveBean> getAllArchiveBeans() {

        ArrayList<ArchiveBean> result = new ArrayList<ArchiveBean>();

        File archiveBeans_Folders = new File(getArchiveSeriesPath());
        for (File archiveBean_File: archiveBeans_Folders.listFiles()) {

            if (archiveBean_File.isDirectory() == true) {
                result.add(new ArchiveBean(this, archiveBean_File.getName()));
            }

        }

        LoggerManager.logDebug("获取所有的ArchiveBean: ArchiveSeries = " + this);
        return result;
    }


    public boolean renameArchiveSeries(String newName) {

        boolean ret = false;

        // Rename
        File f = new File(this.getArchiveSeriesPath());
        ret = f.renameTo(new File(f.getParentFile() + "\\" + newName));

        // Update
        ((MainController) Main.loader.getController()).update_combobox_backup_game_version();

        return ret;
    }

    public void renameArchiveSeries() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("重命名ArchiveSeries");
        dialog.setHeaderText("每一个ArchiveSeries都有一个名称，\n但请注意名称应符合Windows系统的文件命名规范！");


        dialog.setContentText("请输入该ArchiveSeries的新名称：" + this.getArchiveSeries_Name());

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();


        if (result.isPresent() == true){

            String input = result.get();

            if (input.trim().equalsIgnoreCase("") == true) {
                return;
            }

            if (renameArchiveSeries(input) == false){

                new Alert(Alert.AlertType.WARNING, "重命名失败！", ButtonType.OK).show();

            }


        } else {
            //若点击了取消, 则直接返回
            return;
        }
    }

    /**
     * 对该[游戏版本对象]进行存档备份
     */
    public void backup() {

        // 获取[该游戏版本对象]的[存档路径]
        File file = new File(this.getOwner_GameVersion().smartlyGetGameArchive_Path());

        // Copy GameArchive
        String src = file.getAbsolutePath();
        String tar = ArchiveBean.generateSpecificArchiveBeanPath(this);
        LoggerManager.getLogger().info("复制文件夹: source = " + src + ", target = " + tar);
        try {
            FileUtil.copyFolder(src, tar);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }

        // Create ArchiveBeanConfig.json
        try {
            ArchiveBeanConfig_File abcf =  new ArchiveBeanConfig_File(tar , "ArchiveBeanConfig.json" ,
                    ArchiveBeanConfig_Data.class);


            abcf.init();
        } catch (IllegalAccessException e) {
            LoggerManager.logException(e);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }


    }


}
