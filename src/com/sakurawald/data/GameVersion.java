package com.sakurawald.data;


import com.sakurawald.Main;
import com.sakurawald.archive.ArchiveBean;
import com.sakurawald.archive.ArchiveExplanation;
import com.sakurawald.archive.ArchiveSeries;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.FileUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 描述[一个PVZ的游戏版本]
 */
public class GameVersion {

    private String version_Name = null;
    private String version_Number = "NONE";
    private String version_Remark = "NONE";

    private ArrayList<ArchiveExplanation> archieveExplanations = ArchiveExplanation.getDefaultArchiveExplanations();



    /**
     * 描述[智能自动备份]功能
     */
    private SmartAutoBackup SmartAutoBackup = new SmartAutoBackup();
    private class SmartAutoBackup{
        /**
         * 若cheatEngine为null, 则[智能自动备份]无法生效
         */
        public CheatEngine cheatEngine = null;

    }

    /**
     * 尝试获取该GameVersion的CheatEngine, 若没有则返回null
     */
    public CheatEngine getCheatEngine() {

        return this.SmartAutoBackup.cheatEngine;
    }

    /**
     * [存档]文件的路径. 若为固定路径, 则应该填写绝对路径.
     * 填写$AUTO则表示根据操作系统自动判断路径
     * 填写$CD则表示程序运行的当前目录
      */
    private String archive_Path = "$AUTO";

    /**
     * 快速构造[游戏版本对象]
     */
    public GameVersion(String version_Name, String version_Number, String version_Remark, String archive_Path) {
        this.version_Name = version_Name;
        this.version_Number = version_Number;
        this.version_Remark = version_Remark;
        this.archive_Path = archive_Path;
    }



    /**
     * 根据[本程序运行的操作系统]自动判断[游戏存档路径]
     */
    public String getAutoArchivePath() {

        return null;
    }

    public GameVersion(String version_Name, String version_Remark) {
        this.version_Name = version_Name;
        this.version_Remark = version_Remark;
    }

    public GameVersion(String version_Name, String version_Remark, CheatEngine cheatEngine) {
        this.version_Name = version_Name;
        this.version_Remark = version_Remark;
        this.SmartAutoBackup.cheatEngine = cheatEngine;
    }

    public GameVersion(String version_Name) {
        this.version_Name = version_Name;
    }

    public String getVersion_Name() {
        return version_Name;
    }

    public String getVersion_Number() {
        return version_Number;
    }

    public String getVersion_Remark() {
        return version_Remark;
    }

    public ArrayList<ArchiveExplanation> getArchieveExplanations() {
        return archieveExplanations;
    }

    public String getArchive_Path() {
        return archive_Path;
    }

    @Override
    public String toString() {
        return version_Remark + "（" + version_Name + "）";
    }


    public String translateArchivePath(String archivePath) {
        return archivePath.replace("$CD", FileUtil.getJavaRunPath());
    }

    public String smartlyGetGameArchive_Path() {

        // 判断是否为"自动模式"
        if (this.getArchive_Path().equalsIgnoreCase("$AUTO") == false) {
            return translateArchivePath(this.archive_Path);
        }

        // "自动模式"
        OperationSystemVersion osv = OperationSystemVersion.getOperationSystemVersion();
        LoggerManager.logDebug("获取到的操作系统版本信息: " + osv.getOperationVersion());

        if (osv == OperationSystemVersion.WINDOWS_XP) {
            return FileUtil.getJavaRunPath() + "userdata\\";
        }

        if (osv == OperationSystemVersion.WINDOWS_VISTA
        || osv == OperationSystemVersion.WINDOWS_7
        || osv == OperationSystemVersion.WINDOWS_8
        || osv == OperationSystemVersion.WINDOWS_10) {
            return "C:\\ProgramData\\PopCap Games\\PlantsVsZombies\\userdata\\";
        }

        return null;
    }

    public String getGameVersionPath() {
        return ArchiveBean.getArchiveBeansPath() + this.getVersion_Name() + "\\";
    }

    /**
     * 获取该[游戏版本]的[所有存档系列]
     */
    public ArrayList<ArchiveSeries> getAllArchiveSeries() {

        ArrayList<ArchiveSeries> result = new ArrayList<ArchiveSeries>();

        File files = new File(this.getGameVersionPath());
        File[] allSeries = files.listFiles();

        if (allSeries == null) {
            LoggerManager.getLogger().error("获取当前游戏版本的所有存档系列失败: GameVersion = " + this.getVersion_Name());
            return new ArrayList<ArchiveSeries>();
        }

        for (File f : allSeries) {

            if (f.isFile() == false) {
                result.add(new ArchiveSeries(this, f.getName()));
            }

        }

        LoggerManager.logDebug("获取到所有存档系列: GameVersion = " + this.getVersion_Name() + ", AllSeries = " + result);

        return result;
    }

    /**
     * 获取UI界面选中的ArchiveSeries
     */
    public String getArchiveSeriesFolderName() {

        MainController controller = Main.loader.getController();
        ArchiveSeries archiveSeries =  controller.combobox_backup_archive_series.getSelectionModel().getSelectedItem();

        LoggerManager.logDebug("当前选中的存档系列: " + archiveSeries);

        return archiveSeries.getArchiveSeries_Name();
    }

    /**
     * 为该GameVersion创建默认的ArchiveSeries
     */
    public void createDefaultArchiveSeries() {
        createArchiveSeries("DefaultArchiveSeries");
    }

    public void createArchiveSeries(String archiveSeriesName) {
        // Create Default Archive Series
        String archiveSeriesPath = this.getGameVersionPath() +  archiveSeriesName;
        File f = new File(archiveSeriesPath);
        f.mkdirs();

        // Update
        ((MainController) Main.loader.getController()).update_combobox_backup_game_version();
    }

    public void deleteArchiveSeries(ArchiveSeries archiveSeries) {

        // Delete
        File f = new File(archiveSeries.getArchiveSeriesPath());
        FileUtil.deleteFolder(f.getAbsolutePath());

        // Update
        ((MainController) Main.loader.getController()).update_combobox_backup_game_version();
    }

    public void deleteArchiveSeries_UI() {

        ArchiveSeries as = ((MainController) Main.loader.getController()).combobox_backup_archive_series.getSelectionModel().getSelectedItem();

        if (as == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveSeries", ButtonType.OK).show();
            return;
        }

        //对话框 Alert Alert.AlertType.CONFIRMATION：反问对话框
        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        //设置对话框标题
        askAlert.setTitle("删除该ArchiveSeries");
        //设置内容

        askAlert.setHeaderText("你确定要删除该ArchiveSeries（" + as.getArchiveSeries_Name() + "）吗？");
        //显示对话框
        Optional<ButtonType> result = askAlert.showAndWait();
        //如果点击OK
        if (result.get() == ButtonType.OK){
            deleteArchiveSeries(as);
        }

    }

    public GameVersion(String version_Name, String version_Remark, String archive_Path) {
        this.version_Name = version_Name;
        this.version_Remark = version_Remark;
        this.archive_Path = archive_Path;
    }

    public void setVersion_Name(String version_Name) {
        this.version_Name = version_Name;
    }

    public void setVersion_Number(String version_Number) {
        this.version_Number = version_Number;
    }

    public void setVersion_Remark(String version_Remark) {
        this.version_Remark = version_Remark;
    }

    public void setArchieveExplanations(ArrayList<ArchiveExplanation> archieveExplanations) {
        this.archieveExplanations = archieveExplanations;
    }

    public GameVersion.SmartAutoBackup getSmartAutoBackup() {
        return SmartAutoBackup;
    }

    public void setSmartAutoBackup(GameVersion.SmartAutoBackup smartAutoBackup) {
        SmartAutoBackup = smartAutoBackup;
    }

    public void setArchive_Path(String archive_Path) {
        this.archive_Path = archive_Path;
    }

    public void createArchiveSeries_UI() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("新建ArchiveSeries");
        dialog.setHeaderText("每一个ArchiveSeries都有一个名称，\n但请注意名称应符合Windows系统的文件命名规范！");
        dialog.setContentText("请输入新建ArchiveSeries的名称：");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();


        if (result.isPresent() == true){

            String archiveSeriesName = result.get();

            // 文件名过滤器
            archiveSeriesName = FileUtil.fileNameFilter(archiveSeriesName);


            if (archiveSeriesName.trim().equalsIgnoreCase("") == true) {
                return;
            }

            // Ceeate
            createArchiveSeries(archiveSeriesName);

            // 自动选中刚创建的系列
            ((MainController) Main.loader.getController()).combobox_backup_archive_series.getSelectionModel().select(new ArchiveSeries(this, archiveSeriesName));

        } else {
            //若点击了取消, 则直接返回
            return;
        }

    }

}
