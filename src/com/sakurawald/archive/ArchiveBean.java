package com.sakurawald.archive;

import com.sakurawald.Main;
import com.sakurawald.data.GameVersion;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.ArchiveBeanConfig_Data;
import com.sakurawald.file.ArchiveBeanConfig_File;
import com.sakurawald.file.ConfigFile;
import com.sakurawald.file.FileManager;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.DateUtil;
import com.sakurawald.util.FileUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

/**
 * 与ArchiveBean有关的类
 */
public class ArchiveBean {

    private ArchiveSeries owner_ArchiveSeries = null;
    /**
     * ArchiveBean的Name其实就是文件夹名称
     */
    private String archiveBean_Name = null;

    public ArchiveBean(ArchiveSeries owner_ArchiveSeries, String archiveBean_Name) {
        this.owner_ArchiveSeries = owner_ArchiveSeries;
        this.archiveBean_Name = archiveBean_Name;
    }

    public ArchiveSeries getOwner_ArchiveSeries() {
        return owner_ArchiveSeries;
    }

    public String getArchiveBean_Name() {
        return archiveBean_Name;
    }

    /**
     * 获取[一个ArchiveBean文件夹]的名称
     */
    public static String getArchiveBeanFolderName() {
        String result = "Backup_" + DateUtil.getDateDetail(Calendar.getInstance());

        result = result.replace(":", "-");

        return result;
    }

    public String getArchiveBeanPath() {
        return this.getOwner_ArchiveSeries().getArchiveSeriesPath() + this.getArchiveBean_Name() + "\\";
    }

    public long getArchiveBeanCreateTime() {
        return FileUtil.getFileCreateTime(getArchiveBeanPath());
    }

    /**
     * 获取[一个具体的ArchiveBean]的[文件夹路径]
     */
    public static String generateSpecificArchiveBeanPath(ArchiveSeries archiveSeries) {
        return archiveSeries.getArchiveSeriesPath() + ArchiveBean.getArchiveBeanFolderName() + "\\";
    }

    /**
     * 删除这个ArchiveBean
     */
    public void delete() {
        File f = new File(this.getArchiveBeanPath());
        FileUtil.deleteFolder(f.getAbsolutePath());
    }

    /**
     * 获取ArchiveBeans文件夹的路径
     */
    public static String getArchiveBeansPath() {

        // 判断是否启用"独立的ArchiveBeans文件夹"
        if (FileManager.applicationConfig_File.getSpecificDataInstance().Base.StorageSettings.useIndependentStorage == false) {
            return "D:\\ArchiveManager\\ArchiveBeans\\";
        }

        // 使用独立的ArchiveBeans文件夹
        return ConfigFile.getApplicationConfigPath() + "ArchiveBeans\\";
    }



    /**
     * 修改该ArchiveBean的备注信息, 并存储到本地
     */
    public void setRemark(String newRemark) {

        // Load
        ArchiveBeanConfig_File abcf = getArchiveBeanConfig();

        // Modify
        ArchiveBeanConfig_Data  abcd = abcf.getSpecificDataInstance();
        abcd.Information.remark = newRemark;

        // Save
        abcf.saveToLocal();
    }

    /**
     * 从本地直接获取该ArchiveBean的配置数据
     */
    public ArchiveBeanConfig_File getArchiveBeanConfig() {
        try {
            // 创建配置文件对象
           ArchiveBeanConfig_File  abcf =  new ArchiveBeanConfig_File(this.getArchiveBeanPath(),"ArchiveBeanConfig.json",ArchiveBeanConfig_Data.class);
            abcf.init();

            // Return
            return abcf;

        } catch (IllegalAccessException e) {
            LoggerManager.logException(e);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }

        return null;
    }

    /**
     * 获取该ArchiveBean的Info信息
     */
    public String getInfo() {

        StringBuffer sb = new StringBuffer();
        ArchiveBeanConfig_Data abcd = getArchiveBeanConfig().getSpecificDataInstance();

        MainController mc = Main.loader.getController();

        sb.append("★备份时间：" + abcd.Information.backup_time + "\n");
        sb.append("★游戏版本：" + abcd.Information.game_version + "\n");
        sb.append("★存档系列：" +  abcd.Information.archive_series+ "\n");

        sb.append("★操作系统名称：" + abcd.Information.os_name + "\n");
        sb.append("★操作系统版本：" + abcd.Information.os_version + "\n");
        sb.append("★操作系统架构：" + abcd.Information.os_arch  + "\n");
        sb.append("★用户名称：" + abcd.Information.user_name  + "\n");
        sb.append("★用户目录：" + abcd.Information.user_home + "\n");
        sb.append("★程序目录：" + abcd.Information.user_dir   );

        sb.append("\n\n");
        sb.append("★备注：" + abcd.Information.remark);

        return sb.toString();
    }

    /**
     * 获取所有的ArchiveBean, 包括不同GameVersion的.
     */
    public static ArrayList<ArchiveBean> getAllArchiveBean() {

        ArrayList<ArchiveBean> result = new ArrayList<ArchiveBean>();

        MainController mc = Main.loader.getController();

        // GameVersion
        for (GameVersion gv : FileManager.gameVersionConfig_File.getSpecificDataInstance().gameVersions) {

            // ArchiveSeries
            for (ArchiveSeries as : gv.getAllArchiveSeries()) {

                // ArchiveBean

                for (ArchiveBean ab : as.getAllArchiveBeans()) {
                    result.add(ab);
                }

            }

        }


        return result;
    }

    public String toString() {return this.archiveBean_Name;}

    public boolean renameArchiveBean(String newName) {

        boolean ret = false;

        // Rename
        File f = new File(this.getArchiveBeanPath());
        ret = f.renameTo(new File(f.getParentFile() + "\\" + newName));

        // Update
        ((MainController) Main.loader.getController()).update_combobox_backup_archive_series();

        return ret;
    }

    public void renameArchiveBean() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("重命名ArchiveBean");
        dialog.setHeaderText("每一个ArchiveBean都有一个名称，\n但请注意名称应符合Windows系统的文件命名规范！");


        dialog.setContentText("请输入该ArchiveBean的新名称：" + this.getArchiveBean_Name());

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();


        if (result.isPresent() == true){

            String input = result.get();

            if (input.trim().equalsIgnoreCase("") == true) {
                return;
            }

            if (renameArchiveBean(input) == false){

                new Alert(Alert.AlertType.WARNING, "重命名失败！", ButtonType.OK).show();

            }


        } else {
            //若点击了取消, 则直接返回
            return;
        }
    }

    /**
     * 调用该方法将用该ArchiveBean的数据覆盖本地游戏数据
     * @return 是否成功
     */
    public boolean rollBackAll() {

        // Copy GameArchive
        File file = new File(this.getOwner_ArchiveSeries().getOwner_GameVersion().smartlyGetGameArchive_Path());

        String src = this.getArchiveBeanPath();
        String tar = file.getAbsolutePath();

        LoggerManager.getLogger().info("复制文件夹: source = " + src + ", target = " + tar);
        try {
            FileUtil.copyFolder(src, tar);
            return true;
        } catch (IOException e) {
            LoggerManager.logException(e);
        } finally {
            deleteGameArchive_ArchiveBeanConfig();
        }

        return false;
    }

    /**
     * 调用本方式进行局部回档
     */
    public void rollbackPartly() {
// Load FXML
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("RollBackChooseWindow.fxml"));

        // Create
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            LoggerManager.logException(e);
        }

        Stage stage = new Stage();
        stage.setTitle("回档");
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        // Set Modality
        stage.initModality(Modality.APPLICATION_MODAL);

        // Set Icon
        stage.getIcons().add(new Image(
                Main.class.getResourceAsStream("icon.png")));

        // Show
        stage.show();

    }


    /**
     * 删除本地游戏存档路径中的ArchiveBeanConfig.json文件.
     * 该方法一般在[回档方法]之后调用.
     */
    public void deleteGameArchive_ArchiveBeanConfig() {
        File f = new File(this.getOwner_ArchiveSeries().getOwner_GameVersion().smartlyGetGameArchive_Path() + "ArchiveBeanConfig.json");
        if (f.delete() == false) {
            LoggerManager.getLogger().error("删除[本地游戏存档路径]中的[ArchiveBeanConfig.json]失败！");
        }

    }



}
