package com.sakurawald.archive;

import com.sakurawald.Main;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.ArchiveBeanConfig_Data;
import com.sakurawald.file.ArchiveBeanConfig_File;
import com.sakurawald.file.ConfigFile;
import com.sakurawald.file.FileManager;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.DateUtil;
import com.sakurawald.util.FileUtil;
import com.sakurawald.util.JavaFxUtil;
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
import java.util.Objects;
import java.util.Optional;

/**
 * 描述一个ArchiveBean, 代表一个[游戏存档]对象
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
    public static String generateArchiveBean_FolderName() {
        return "Backup " + DateUtil.getCurrentDateDetailString();
    }

    public String getArchiveBeanPath() {
        return this.getOwner_ArchiveSeries().getArchiveSeries_Path() + this.getArchiveBean_Name() + "\\";
    }

    /**
     * @return 该ArchiveBean所属文件夹的[创建时间]
     */
    public long getArchiveBeanCreateTime() {
        return FileUtil.getFileCreateTime(getArchiveBeanPath());
    }

    /**
     * 获取[一个具体的ArchiveBean]的[文件夹路径]
     */
    public static String generateSpecificArchiveBean_Path(ArchiveSeries archiveSeries) {
        return archiveSeries.getArchiveSeries_Path() + ArchiveBean.generateArchiveBean_FolderName() + "\\";
    }

    /**
     * 从本次存储删除这个ArchiveBean
     */
    public void delete() {
        File f = new File(this.getArchiveBeanPath());
        FileUtil.deleteFolder(f.getAbsolutePath());
    }

    /**
     * @return 获取ArchiveBeans文件夹的路径, ArchiveBeans的路径受"是否使用独立存储路径"的影响.
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
        ArchiveBeanConfig_Data abcd = abcf.getSpecificDataInstance();
        abcd.Information.remark = newRemark;

        // Save
        abcf.saveFile();
    }

    /**
     * 从本地直接获取该ArchiveBean的配置数据
     */
    public ArchiveBeanConfig_File getArchiveBeanConfig() {
        try {
            // 创建配置文件对象
            ArchiveBeanConfig_File abcf = new ArchiveBeanConfig_File(this.getArchiveBeanPath(), "ArchiveBeanConfig.json", ArchiveBeanConfig_Data.class);
            abcf.init();

            // Return
            return abcf;

        } catch (IllegalAccessException | IOException e) {
            LoggerManager.reportException(e);
        }

        return null;
    }

    /**
     * 获取该ArchiveBean的Info信息
     */
    public String getInfo() {

        StringBuilder sb = new StringBuilder();
        ArchiveBeanConfig_Data abcd = getArchiveBeanConfig().getSpecificDataInstance();
        MainController mc = MainController.getInstance();

        /** Information **/
        sb.append("★备份时间：" + abcd.Information.backup_time + "\n");
        sb.append("★游戏版本：" + abcd.Information.game_version + "\n");
        sb.append("★存档系列：" + abcd.Information.archive_series + "\n");
        sb.append("★存档目录：" + abcd.Information.archive_path + "\n");
        sb.append("★操作系统名称：" + abcd.Information.os_name + "\n");
        sb.append("★操作系统版本：" + abcd.Information.os_version + "\n");
        sb.append("★用户名称：" + abcd.Information.user_name + "\n");
        sb.append("★用户目录：" + abcd.Information.user_home + "\n");

        sb.append("\n");
        sb.append("★备注：" + abcd.Information.remark);

        return sb.toString();
    }

    /**
     * 获取所有的ArchiveBean, 包括不同GameVersion的.
     */
    public static ArrayList<ArchiveBean> getAllArchiveBean() {

        ArrayList<ArchiveBean> result = new ArrayList<ArchiveBean>();

        MainController mc = MainController.getInstance();

        // GameVersion
        for (GameVersion gv : FileManager.gameVersionConfig_File.getSpecificDataInstance().gameVersions) {

            // ArchiveSeries
            for (ArchiveSeries as : gv.getAllArchiveSeries()) {

                // ArchiveBean
                result.addAll(as.getAllArchiveBeans());

            }

        }


        return result;
    }

    public static void rollbackAll_FromUI() {
        MainController mc = MainController.getInstance();

        // 选中ArchiveBean
        ArchiveBean ab = mc.getSelectedArchiveBean();
        if (ab == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveBean_Dialog();
            return;
        }

        // RollBack
        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        JavaFxUtil.DialogTools.setIcon(askAlert);
        askAlert.setTitle("回档该ArchiveBean");
        askAlert.setHeaderText("ArchiveBean：" + ab.getArchiveBean_Name());
        askAlert.setContentText("确定要回档该ArchiveBean吗？");
        Optional<ButtonType> result = askAlert.showAndWait();
        boolean ret = false;
        if (result.get() == ButtonType.OK) {
            // ArchiveBean >> Rollback All
            ret = ab.rollBackAll();
        } else {
            return;
        }

        if (ret == true) {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.INFORMATION, "回档成功！", ButtonType.OK).show();
        } else {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.ERROR, "回档失败！（可能的原因：权限不足 或 文件被占用）", ButtonType.OK).show();
        }
    }

    public static void rollbackPartly_FromUI() {
        MainController mc = MainController.getInstance();

        // 选中ArchiveBean
        ArchiveBean ab = mc.getSelectedArchiveBean();
        if (ab == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveBean_Dialog();
            return;
        }

        // ArchiveBean >> Rollback Partyly
        ab.rollbackPartly();
    }

    public static void setRemarkArchiveBean_FromUI() {

        MainController mc = MainController.getInstance();

        ArchiveBean ab = mc.getSelectedArchiveBean();
        if (ab == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveBean_Dialog();
            return;
        }

        String originalRemark = ab.getArchiveBeanConfig().getSpecificDataInstance().Information.remark;

        TextInputDialog dialog = new TextInputDialog(originalRemark);
        JavaFxUtil.DialogTools.setIcon(dialog);
        dialog.setTitle("设置ArchiveBean的备注");


        String text = "ArchiveBean：" + ab.getArchiveBean_Name();
        if (originalRemark != null) {
            text = text + "\n\n原备注：" + originalRemark + "\n\n";
        }
        dialog.setHeaderText(text);

        dialog.setContentText("该ArchiveBean的新备注：");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() == true) {

            String input = result.get();

            // 防空字符的字符串
            if (input.trim().equalsIgnoreCase("") == true) {
                return;
            }

            // Set ArchiveBean Remark.
            ab.setRemark(input);

        }

    }

    public String toString() {
        return this.archiveBean_Name;
    }

    public boolean renameArchiveBean(String newName) {

        boolean ret = false;

        // Rename
        File f = new File(this.getArchiveBeanPath());
        ret = f.renameTo(new File(f.getParentFile() + "\\" + newName));

        // Update
        MainController.getInstance().update_combobox_backup_archive_series();

        return ret;
    }

    public void renameArchiveBean() {

        TextInputDialog dialog = new TextInputDialog(this.getArchiveBean_Name());
        JavaFxUtil.DialogTools.setIcon(dialog);
        dialog.setTitle("重命名ArchiveBean");

        dialog.setHeaderText("ArchiveBean：" + this.getArchiveBean_Name());

        dialog.setContentText("该ArchiveBean的新名称：");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();


        if (result.isPresent() == true) {

            String input = result.get();

            if (input.trim().equalsIgnoreCase("") == true) {
                return;
            }

            if (renameArchiveBean(input) == false) {
                JavaFxUtil.DialogTools.alert(Alert.AlertType.ERROR, "重命名失败！", ButtonType.OK).show();
            }


        } else {
            //若点击了取消, 则直接返回
            return;
        }
    }

    /**
     * 调用该方法将用该ArchiveBean的数据覆盖本地游戏数据
     *
     * @return 是否成功
     */
    public boolean rollBackAll() {

        // Copy GameArchive
        File file = new File(this.getOwner_ArchiveSeries().getOwner_GameVersion().smartlyGetGameArchive_Path());

        ArrayList<ArchiveFile> afs = getAllArchiveFile();

        /**
         *   只要回档过程中有一个ArchiveFile回档失败, 则认为本次回档操作是失败的.
         *   但失败的ArchiveFile会被及时捕捉, 不会影响其他正常的ArchiveFile.
         */
        boolean allSuccess = true;

        for (ArchiveFile af : afs) {
            // ArchiveFile >> Rollback
            if (af.rollback() == false) {
                allSuccess = false;
            }
        }

        return allSuccess;
    }

    /**
     * @return 将该ArchiveBean所拥有的的所有文件都封装为ArchievFile, 并自动过滤一些无效的ArchiveFile
     */
    public ArrayList<ArchiveFile> getAllArchiveFile() {

        // 获取ArchiveBean的存档文件
        ArrayList<ArchiveFile> result = new ArrayList<ArchiveFile>();
        for (File f : Objects.requireNonNull(new File(this.getArchiveBeanPath()).listFiles())) {

            /**
             * File Filter: 过滤[目录]和[ArchiveBeanConfig.json文件]
             */
            if (f.isFile() == true && f.getName().equalsIgnoreCase("ArchiveBeanConfig.json") == false) {
                result.add(new ArchiveFile(this, f));
            }
        }

        return result;
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
            LoggerManager.reportException(e);
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


}
