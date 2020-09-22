package com.sakurawald.archive;


import com.sakurawald.data.CheatEngine;
import com.sakurawald.data.OperationSystemVersion;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.FileUtil;
import com.sakurawald.util.JavaFxUtil;
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

    /**
     * 描述该GameVersion所配置的ArchiveExplanation
     */
    private ArrayList<ArchiveExplanation> archieveExplanations = ArchiveExplanation.getDefaultArchiveExplanations();

    /**
     * 描述[智能自动备份]功能
     */
    private SmartAutoBackup SmartAutoBackup = new SmartAutoBackup();
    /**
     * [存档]文件的路径. 若为固定路径, 则应该填写绝对路径.
     * 填写$AUTO则表示根据操作系统自动判断路径.
     * 填写$CD则表示程序运行的当前目录.
     */
    private String archive_Path = "$AUTO";

    /**
     * Fast Constructor
     */
    public GameVersion(String version_Name, String version_Number, String version_Remark, String archive_Path) {
        this.version_Name = version_Name;
        this.version_Number = version_Number;
        this.version_Remark = version_Remark;
        this.archive_Path = archive_Path;
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


    /**
     * Fast Constructor
     */
    public GameVersion(String version_Name) {
        this.version_Name = version_Name;
    }

    public GameVersion(String version_Name, String version_Remark, String archive_Path) {
        this.version_Name = version_Name;
        this.version_Remark = version_Remark;
        this.archive_Path = archive_Path;
    }

    /**
     * 尝试获取该GameVersion的CheatEngine, 若没有配置CheatEngine则返回null.
     */
    public CheatEngine getCheatEngine() {
        return this.SmartAutoBackup.cheatEngine;
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

    /**
     * 重写该方法以便快速描述GameVersion.
     */
    @Override
    public String toString() {
        return version_Remark + " [" + version_Name + "]";
    }

    /**
     * @return 经过转换后的[原始ArchivePath].
     */
    public String translateArchivePath(String archivePath) {
        return archivePath.replace("$CD", FileUtil.getJavaRunPath());
    }

    /**
     * @return 根据操作系统和配置的ArchivePath, 一键获得[游戏的存档目录]. 获取失败则返回null.
     */
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

    /**
     * @return 该GameVersion在本地存储的文件夹路径.
     */
    public String getGameVersion_Path() {
        return ArchiveBean.getArchiveBeansPath() + this.getVersion_Name() + "\\";
    }

    /**
     * @return 获取该[游戏版本]的[所有ArchiveSeries]. 失败返回[空元素的集合].
     */
    public ArrayList<ArchiveSeries> getAllArchiveSeries() {

        ArrayList<ArchiveSeries> result = new ArrayList<ArchiveSeries>();

        File files = new File(this.getGameVersion_Path());
        File[] allSeries = files.listFiles();

        if (allSeries == null) {
            LoggerManager.logDebug("Get All ArchiveSeries In This GameVersion >> Not Found Any ArchiveSeries >> GameVersion = " + this.getVersion_Name());
            return new ArrayList<ArchiveSeries>();
        }

        for (File f : allSeries) {

            if (f.isFile() == false) {
                result.add(new ArchiveSeries(this, f.getName()));
            }

        }

        LoggerManager.logDebug("Get All ArchiveSeries In This GameVersion >> GameVersion = " + this.getVersion_Name() + ", AllSeries = " + result);

        return result;
    }

    /**
     * 获取UI界面选中的ArchiveSeries.
     */
    public String getSelectedArchiveSeries_FolderName() {

        MainController controller = MainController.getInstance();
        ArchiveSeries archiveSeries = controller.getSelectedArchiveSeries();

        LoggerManager.logDebug("Current Selected ArchiveSeries >> " + archiveSeries);

        return archiveSeries.getArchiveSeries_Name();
    }

    /**
     * 为该GameVersion创建默认的ArchiveSeries.
     */
    public void createDefaultArchiveSeries() {
        createArchiveSeries("DefaultArchiveSeries");
    }

    /**
     * 在[本地存储]创建ArchiveSeries的文件夹.
     */
    public boolean createArchiveSeries(String archiveSeriesName) {
        // Create Default Archive Series
        String archiveSeriesPath = this.getGameVersion_Path() + archiveSeriesName;

        // Mkdirs
        boolean ret = new File(archiveSeriesPath).mkdirs();

        // Update
        MainController.getInstance().update_combobox_backup_game_version();

        return ret;
    }

    /**
     * 在[本地存储]删除ArchiveSeries的文件夹.
     */
    public void deleteArchiveSeries(ArchiveSeries archiveSeries) {

        // Delete
        File f = new File(archiveSeries.getArchiveSeries_Path());
        FileUtil.deleteFolder(f.getAbsolutePath());

        // Update
        MainController.getInstance().update_combobox_backup_game_version();
    }

    public void deleteArchiveSeries_FromUI() {

        ArchiveSeries as = MainController.getInstance().getSelectedArchiveSeries();

        if (as == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveSeries_Dialog();
            return;
        }


        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        JavaFxUtil.DialogTools.setIcon(askAlert);
        askAlert.setTitle("删除该ArchiveSeries");


        askAlert.setHeaderText("ArchiveSeries：" + as.getArchiveSeries_Name());
        askAlert.setContentText("确定要删除该ArchiveSeries吗？");
        Optional<ButtonType> result = askAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deleteArchiveSeries(as);
        }

    }

    public void createArchiveSeries_FromUI() {

        TextInputDialog dialog = new TextInputDialog();
        JavaFxUtil.DialogTools.setIcon(dialog);
        dialog.setTitle("新建ArchiveSeries");
        dialog.setHeaderText("命名规则：\n" + "1. 最多使用256个字符\n" + "2. 不允许使用 ? \\ * | “ < > : /");
        dialog.setContentText("新建ArchiveSeries的名称：");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() == true) {

            String archiveSeriesName = result.get();

            // FileName Filter
            archiveSeriesName = FileUtil.fileNameFilter(archiveSeriesName);

            if (archiveSeriesName.trim().equalsIgnoreCase("") == true) {
                return;
            }

            // Ceeate ArchiveSeries
            if (createArchiveSeries(archiveSeriesName) == true) {
                /**
                 *   自动选中刚创建的系列
                 *   注意: JavaFx的Combobox是根据 对象.toString() 文本是否相同 来判断 两个对象是否 equals.
                 */
                MainController.getInstance().combobox_backup_archive_series.getSelectionModel().select(new ArchiveSeries(this, archiveSeriesName));
            } else {
                JavaFxUtil.DialogTools.alert(Alert.AlertType.ERROR, "新建失败！", ButtonType.OK).show();
            }

        }

    }

    private class SmartAutoBackup {
        /**
         * 若cheatEngine为null, 则[智能自动备份]无法生效.
         */
        public CheatEngine cheatEngine = null;

    }

}
