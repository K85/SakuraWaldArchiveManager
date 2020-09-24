package com.sakurawald.ui.controller;

import com.sakurawald.Main;
import com.sakurawald.api.*;
import com.sakurawald.archive.ArchiveBean;
import com.sakurawald.archive.ArchiveSeries;
import com.sakurawald.archive.GameVersion;
import com.sakurawald.data.ImageAndText;
import com.sakurawald.data.ResultBox;
import com.sakurawald.data.UIStorage;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.ConfigFile;
import com.sakurawald.file.FileManager;
import com.sakurawald.timer.AutoBackupTimer;
import com.sakurawald.timer.SmartAutoBackupTimer;
import com.sakurawald.util.FileUtil;
import com.sakurawald.util.HttpUtil;
import com.sakurawald.util.JavaFxUtil;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

public class MainController implements UIStorage {

    @FXML
    public ListView<ArchiveBean> listview_archive_beans;
    @FXML
    public ComboBox<GameVersion> combobox_backup_game_version;
    @FXML
    public ComboBox<ArchiveSeries> combobox_backup_archive_series;
    @FXML
    public Button button_backup;
    @FXML
    private Hyperlink hyperlink_about_2;
    @FXML
    private Hyperlink hyperlink_about_4;
    @FXML
    private Hyperlink hyperlink_about_7;
    @FXML
    private Hyperlink hyperlink_about_8;
    @FXML
    private Hyperlink hyperlink_about_1;
    @FXML
    private Hyperlink hyperlink_about_5;
    @FXML
    private Hyperlink hyperlink_about_3;
    @FXML
    private Hyperlink hyperlink_about_6;
    @FXML
    private Label label_version;
    @FXML
    private ImageView imageview_welcome;
    @FXML
    private Label label_welcome;
    @FXML
    private MenuItem menuitem_rollback_all_archivebean;
    @FXML
    private MenuItem menuitem_rollback_partly_archivebean;
    @FXML
    private MenuItem menuitem_delete_archivebean;
    @FXML
    private MenuItem menuitem_create_archiveseries;
    @FXML
    private MenuItem menuitem_rename_archiveseries;
    @FXML
    private MenuItem menuitem_delete_archiveseries;
    @FXML
    private TextArea textarea_archive_bean_info;

    @FXML
    private CheckBox checkbox_smartautobackup_enable;

    @FXML
    private CheckBox checkbox_autobackup_timing;

    @FXML
    private TextField textfield_autobackup_seconds;

    @FXML
    private CheckBox checkbox_storagesettings_use_indepent_storage;

    public static MainController getInstance() {
        return Main.loader.getController();
    }

    public ArchiveBean getSelectedArchiveBean() {
        return this.listview_archive_beans.getSelectionModel().getSelectedItem();
    }

    public GameVersion getSelectedGameVersion() {
        return this.combobox_backup_game_version.getSelectionModel().getSelectedItem();
    }

    public ArchiveSeries getSelectedArchiveSeries() {
        return this.combobox_backup_archive_series.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void initialize() {

        // Load UI (此处加载的UI组件是与本地配置文件有关的组件)
        loadUI();

        // Update GameVerison
        update_combobox_backup_game_version();

        // Update ArchiveSeries
        update_combobox_backup_archive_series();

        // Start Timer
        LoggerManager.logDebug("TimingSystem", "Init All Timers");
        LoggerManager.logDebug("TimingSystem", "Init >> AutoBackupTimer");
        AutoBackupTimer.getInstance().schedule();
        LoggerManager.logDebug("TimingSystem", "Init >> SmartAutoBackupTimer");
        SmartAutoBackupTimer.getInstance().schedule();

        // Load About
        loadAbout();

        // Load Version
        loadVersion();

        // Update Welcome
        updateWelcome();
    }

    public void saveUIAndLoadSettings() {
        saveUI();
        loadSettings();
    }

    /**
     * 加载与关于界面有关的UI.
     */
    public void loadAbout() {
        hyperlink_about_1.setText("关于作者： ");
        hyperlink_about_2.setText("Author: SakuraWald / K85");
        hyperlink_about_3.setText("Bilibili: SakuraWald （UID：5336084）");
        hyperlink_about_3.setOnAction(event -> {
            HttpUtil.openURL("https://space.bilibili.com/5336084");
        });
        hyperlink_about_4.setText("Email: 3172906506@qq.com");
        hyperlink_about_5.setText("Baidu ID: a526026058");
        hyperlink_about_6.setText("友情链接：");
        hyperlink_about_7.setText("百度贴吧 - 植物大战僵尸吧");
        hyperlink_about_7.setOnAction(event -> {
            HttpUtil.openURL("https://tieba.baidu.com/f?kw=%D6%B2%CE%EF%B4%F3%D5%BD%BD%A9%CA%AC");
        });
        hyperlink_about_8.setText("植物大战僵尸 - 资源下载站");
        hyperlink_about_8.setOnAction(event -> {
            HttpUtil.openURL(hyperlink_about_8.getAccessibleText());
        });

    }

    public void loadVersion() {
        String version = "Sakura";
        label_version.setText("Version：" + version + "\n" + "很高兴遇见你！");
    }

    @FXML
    void checkbox_smartautobackup_enable_OnAction(ActionEvent event) {
        // Modify
        CheckBox src = (CheckBox) event.getSource();
        FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.smartAutoBackup = src.isSelected();

        // Save and Reload
        saveUIAndLoadSettings();
    }

    /**
     * 加载存储的使用习惯, 若对应的数据不存在, 则自动选中默认项.
     */
    public void loadMemory_GameVersion() {

        // Load GameVersion
        for (GameVersion gv : combobox_backup_game_version.getItems()) {

            if (gv.getVersion_Name().equals(FileManager.tempConfig_File.getSpecificDataInstance().ArchiveMemory.selectedGameVersion)) {
                combobox_backup_game_version.getSelectionModel().select(gv);
                return;
            }
        }

        combobox_backup_game_version.getSelectionModel().select(0);
    }

    /**
     * 加载存储的使用习惯, 若对应的数据不存在, 则自动选中默认项.
     */
    public void loadMemory_ArchiveSeries() {

        // Load ArchiveSeries
        for (ArchiveSeries as : combobox_backup_archive_series.getItems()) {

            if (as.getArchiveSeries_Name().equals(FileManager.tempConfig_File.getSpecificDataInstance().ArchiveMemory.selectedArchiveSeries)) {
                combobox_backup_archive_series.getSelectionModel().select(as);
                return;
            }
        }

        combobox_backup_archive_series.getSelectionModel().select(0);
    }

    @FXML
    void checkbox_autobackup_timing_OnAction(ActionEvent event) {
        // Modify
        CheckBox src = (CheckBox) event.getSource();
        FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.enable = src.isSelected();

        // Save and Reload
        saveUIAndLoadSettings();
    }

    @FXML
    void checkbox_storagesettings_use_indepent_storage_OnAction(ActionEvent event) {
        // Modify
        CheckBox src = (CheckBox) event.getSource();
        FileManager.applicationConfig_File.getSpecificDataInstance().Base.StorageSettings.useIndependentStorage = src.isSelected();

        // Save And Reload
        saveUIAndLoadSettings();

        /**
         *   Update: 修改ArchiveBeans路径, 将导致所有的GameVersion和GameSeries数据都改变, 需要重新刷新UI.
         */

        update_combobox_backup_game_version();
        update_combobox_backup_archive_series();

    }

    @FXML
    void button_smartautobackup_test_OnAction(ActionEvent event) {

        // Get GameVersion
        GameVersion gv = getSelectedGameVersion();
        if (gv == null) {
            JavaFxUtil.DialogTools.mustChooseGameVersion_Dialog();
            return;
        }

        // Test
        if (gv.getCheatEngine() == null) {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.ERROR, "该GameVersion（" + gv.toString() + "）未配置引擎！").show();
            return;
        }

        ResultBox<Integer> r = gv.getCheatEngine().getValue();

        String report = r.getReport();

        if (r.getSuccessCount() > 0 && r.getFailCount() == 0) {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.INFORMATION, "该GameVersion（" + gv.toString() + "）的智能自动备份有效！" + "\n\n" + report).show();
        } else {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.ERROR, "该GameVersion（" + gv.toString() + "）的智能自动备份无效！" + "\n\n" + report).show();
        }

    }

    @FXML
    void textfield_autobackup_seconds_OnKeyReleased(KeyEvent event) {

        // 防止[左右方向键]移动光标时频繁刷新
        if (event.getCode().isArrowKey() == true) {
            return;
        }

        // Modify
        TextField src = (TextField) event.getSource();
        FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.time_second = Integer.valueOf(src.getText());

        // Save And Reload
        saveUIAndLoadSettings();
    }


    @FXML
    void button_backup_OnAction(ActionEvent event) {
        ArchiveSeries.backup_FromUI();
    }

    /**
     * 读取配置文件. 注意, 该方法加载的UI组件, 是与配置文件有关的组件.
     * 与配置文件无关的组件, 不应该在该方法中加载.
     */
    public void loadUI() {
        // 加载配置文件
        FileManager.applicationConfig_File.reloadFile();

        // 加载[存档]
        loadArchive();

        // 加载[设置]
        loadSettings();
    }


    /**
     * 从[本地存储]读取并添加GameVerison. 添加完毕后, 若存在可用Memory, 则会自动选择好上次记忆的GameVersion.
     */
    public void loadGameVersions() {

        combobox_backup_game_version.getItems().clear();
        for (GameVersion gv : FileManager.gameVersionConfig_File.getSpecificDataInstance().gameVersions) {
            combobox_backup_game_version.getItems().add(gv);
        }

        // Load Memory >> GameVerison
        loadMemory_GameVersion();

    }


    /**
     * 加载[UI界面]的[存档UI].
     */
    public void loadArchive() {
        loadGameVersions();
    }

    /**
     * 加载[UI界面]的[设置UI].
     */
    public void loadSettings() {
        // Load ApplicationConfig.json and Update
        checkbox_smartautobackup_enable.setSelected(FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.smartAutoBackup);
        checkbox_autobackup_timing.setSelected(FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.enable);
        checkbox_storagesettings_use_indepent_storage.setSelected(FileManager.applicationConfig_File.getSpecificDataInstance().Base.StorageSettings.useIndependentStorage);
        textfield_autobackup_seconds.setText(String.valueOf(FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.time_second));
    }

    public void update_combobox_backup_archive_series() {

        // 获取ArchiveSeries
        ArchiveSeries as = getSelectedArchiveSeries();

        // 获取该ArchiveSeries下的所有ArchiveBean
        listview_archive_beans.getItems().clear();

        if (as == null) {
            return;
        }

        for (ArchiveBean ab : as.getAllArchiveBeans()) {
            listview_archive_beans.getItems().add(ab);
        }

        // 设置ArchiveBean通过 时间倒序 来排序.
        listview_archive_beans.getItems().sort(new Comparator<ArchiveBean>() {

            @Override
            public int compare(ArchiveBean o1, ArchiveBean o2) {
                long a = Long.valueOf(o1.getArchiveBeanCreateTime());
                long b = Long.valueOf(o2.getArchiveBeanCreateTime());

                if (a > b) {
                    return -1;
                }

                return 1;
            }
        });


    }


    public void update_combobox_backup_game_version() {
        // 获取GameVersion
        GameVersion gv = getSelectedGameVersion();

        // Storage
        ArchiveSeries selectedArchiveSeries = getSelectedArchiveSeries();

        // 获取该GameVersion的所有ArchiveSeries
        combobox_backup_archive_series.getItems().clear();
        for (ArchiveSeries archiveSeries : gv.getAllArchiveSeries()) {
            combobox_backup_archive_series.getItems().add(archiveSeries);
        }

        if (combobox_backup_archive_series.getItems().contains(selectedArchiveSeries) == true) {
            combobox_backup_archive_series.getSelectionModel().select(selectedArchiveSeries);
        } else {
            // Load Memory >> Auto Choose GameVersion
            loadMemory_ArchiveSeries();
        }

        // 设置ArchiveSeries组合框 的 可用性.
        if (combobox_backup_archive_series.getItems().size() == 0) {
            combobox_backup_archive_series.setDisable(true);
        } else {
            combobox_backup_archive_series.setDisable(false);
        }

    }

    @FXML
    void combobox_backup_archive_series_OnAction(ActionEvent event) {
        // Update
        update_combobox_backup_archive_series();
    }

    @FXML
    void combobox_backup_game_version_OnAction(ActionEvent event) {
        // Update
        update_combobox_backup_game_version();
    }

    @FXML
    void listview_archive_beans_OnMouseReleased(MouseEvent event) {

        /** 左键单击: 显示选中ArchiveBean的Info **/
        if (event.getButton() == MouseButton.SECONDARY) {
            listview_archive_beans.getContextMenu().show(listview_archive_beans, Side.BOTTOM, 0, 0);
        }


    }

    @FXML
    void listview_archive_beans_OnMouseClicked(MouseEvent event) {

        /** 左键单击: 刷新ArchiveBean的Info **/
        if (event.getButton() == MouseButton.PRIMARY &&
                event.getClickCount() == 1) {
            update_textarea_archive_bean_info();
        }


        /** 左键双击: 回档 (所有) **/
        if (event.getButton() == MouseButton.PRIMARY &&
                event.getClickCount() == 2) {
            ArchiveBean.rollbackAll_FromUI();
        }

        /** 右键双击: 回档 (局部) **/
        if (event.getButton() == MouseButton.MIDDLE) {
            ArchiveBean.rollbackPartly_FromUI();
        }
    }

    public void update_textarea_archive_bean_info() {
        ArchiveBean ab = getSelectedArchiveBean();

        // Prevent NPE.
        if (ab != null) {
            textarea_archive_bean_info.setText(ab.getInfo());
        }

    }


    @FXML
    void menuitem_create_archiveseries_OnAction(ActionEvent event) {
        GameVersion gv = getSelectedGameVersion();
        gv.createArchiveSeries_FromUI();
    }

    @FXML
    void menuitem_import_archiveseries_OnAction(ActionEvent event) {

        ArchiveSeries as = getSelectedArchiveSeries();
        if (as == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveSeries_Dialog();
            return;
        }

        // Load FXML
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("ImportWindow.fxml"));

        // Create
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            LoggerManager.reportException(e);
        }

        Stage stage = new Stage();
        stage.setTitle("导入ArchiveSeries");
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

    @FXML
    void menuitem_set_remark_archivebean_OnAction(ActionEvent event) {
        // Set
        ArchiveBean.setRemarkArchiveBean_FromUI();

        // Update
        update_textarea_archive_bean_info();
    }


    @FXML
    void menuitem_delete_archivebean_OnAction(ActionEvent event) {

        // Delete
        ArchiveBean ab = getSelectedArchiveBean();
        if (ab == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveBean_Dialog();
            return;
        }

        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        JavaFxUtil.DialogTools.setIcon(askAlert);
        askAlert.setTitle("删除该ArchiveBean");
        askAlert.setHeaderText("ArchiveBean：" + ab.getArchiveBean_Name());
        askAlert.setContentText("确定要删除该ArchiveBean吗？");
        Optional<ButtonType> result = askAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ab.delete();
        } else {
            return;
        }

        // Update
        update_combobox_backup_archive_series();
    }

    @FXML
    void menuitem_delete_archiveseries_OnAction(ActionEvent event) {
        GameVersion gv = getSelectedGameVersion();
        gv.deleteArchiveSeries_FromUI();
    }

    @FXML
    void menuitem_rename_archiveseries_OnAction(ActionEvent event) {
        ArchiveSeries as = getSelectedArchiveSeries();
        if (as == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveSeries_Dialog();
            return;
        }

        as.renameArchiveSeries_FromUI();
    }

    @FXML
    void listview_archive_beans_OnKeyPressed(KeyEvent event) {

        /** 方向键上下键: 快速刷新ArchiveBean的Info **/
        if (event.getCode() == KeyCode.UP ||
                event.getCode() == KeyCode.DOWN) {
            update_textarea_archive_bean_info();
        }
    }


    @FXML
    void menuitem_rollback_all_archive_bean_OnAction(ActionEvent event) {
        ArchiveBean.rollbackAll_FromUI();
    }


    @FXML
    void menuitem_rollback_partly_archive_bean_OnAction(ActionEvent event) {
        ArchiveBean.rollbackPartly_FromUI();
    }

    /**
     * 保存配置文件.
     */
    public void saveUI() {
        FileManager.applicationConfig_File.saveFile();
    }

    @FXML
    public void menuitem_view_archivebean_path_OnAction(ActionEvent actionEvent) {

        ArchiveBean ab = getSelectedArchiveBean();

        if (ab == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveBean_Dialog();
            return;
        }

        FileUtil.viewFolder(ab.getArchiveBeanPath());
    }

    @FXML
    public void menuitem_delete_all_archivebean_OnAction(ActionEvent actionEvent) {

        // Delete
        ObservableList<ArchiveBean> abs = listview_archive_beans.getItems();
        if (abs.isEmpty() == true) {
            JavaFxUtil.DialogTools.notFountArchiveBeanInThisArchiveSeries();
            return;
        }

        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        JavaFxUtil.DialogTools.setIcon(askAlert);
        askAlert.setTitle("删除所有ArchiveBean");
        askAlert.setHeaderText("ArchiveSeries：" + getSelectedArchiveSeries().getArchiveSeries_Name());
        askAlert.setContentText("确定要删除所有ArchiveBean吗？");
        Optional<ButtonType> result = askAlert.showAndWait();

        if (result.get() == ButtonType.OK) {

            ArchiveSeries as = getSelectedArchiveSeries();
            for (ArchiveBean ab : as.getAllArchiveBeans()) {
                // ArchiveBean >> Delete
                ab.delete();
            }

        } else {
            return;
        }

        // Update
        update_combobox_backup_archive_series();
    }

    @FXML
    public void menuitem_view_archiveseries_path_OnAction(ActionEvent actionEvent) {
        ArchiveSeries as = getSelectedArchiveSeries();

        if (as == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveSeries_Dialog();
            return;
        }

        FileUtil.viewFolder(as.getArchiveSeries_Path());
    }

    @FXML
    public void menuitem_view_gameversion_path_OnAction(ActionEvent actionEvent) {
        GameVersion gv = getSelectedGameVersion();

        if (gv == null) {
            JavaFxUtil.DialogTools.mustChooseGameVersion_Dialog();
            return;
        }


        FileUtil.viewFolder(gv.getGameVersion_Path());
    }

    @FXML
    public void menuitem_rename_archivebean_OnAction(ActionEvent actionEvent) {

        ArchiveBean ab = getSelectedArchiveBean();
        if (ab == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveBean_Dialog();
            return;
        }

        // ArchiveBean >> Rename
        ab.renameArchiveBean();
    }

    public void menuitem_view_archivebeans_path_OnAction(ActionEvent actionEvent) {
        FileUtil.viewFolder(ArchiveBean.getArchiveBeansPath());
    }

    @FXML
    public void menuitem_view_application_config_path_OnAction(ActionEvent actionEvent) {
        FileUtil.viewFolder(ConfigFile.getApplicationConfigPath());
    }

    @FXML
    public void menuitem_view_application_run_path_OnAction(ActionEvent actionEvent) {
        FileUtil.viewFolder(FileUtil.getJavaRunPath());
    }

    /**
     * 更新[欢迎界面]
     */
    public void updateWelcome() {

        /** Dim the Result **/
        ImageAndText result = new ImageAndText();

        new Thread(new Runnable() {

            @Override
            public void run() {

                // Try-Catch
                try {
                    /** 获取随机句子 **/
                    if (FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomSentence.goToAncientTimes == true) {
                        result.setText(JinRiShiCiAPI.getPoetry().getFormatedString());
                    } else {
                        result.setText(HitoKotoAPI.getRandomSentence().getFormatedString());
                    }

                    /** 获取随机图片 **/
                    final String image_Path = ConfigFile.getApplicationConfigPath() + "RandomImage.png";
                    result.setImage("file:" + image_Path);
                    if (FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomImage.goToACGN == true) {
                        RandomImageAPI.saveImage(SinaRandomImageAPI.getInstance().getRandomImageURL(), image_Path);
                    } else {
                        RandomImageAPI.saveImage(QihooRandomImageAPI.getInstance().getRandomImageURL(), image_Path);
                    }

                    /** Start JavaFx Thread **/
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            /** Update UI **/
                            if (result.getText() != null) {
                                label_welcome.setText(result.getText());
                            }

                            if (result.getImage() != null) {
                                imageview_welcome.setImage(new Image(result.getImage()));
                                JavaFxUtil.centerImage(imageview_welcome);
                            }
                        }
                    });

                } catch (Exception e) {
                    /* 注意: updateWelcome()方法产生的Exception, 不会通过错误对话框提示, 而是静默输出到本地日志文件.
                     */
                    LoggerManager.logError(e);
                }
            }
        }).start();

    }

    @FXML
    void imageview_welcome_OnMouseClicked(MouseEvent event) {

        /** 左键双击: 刷新图文 **/
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            updateWelcome();
        }

        /** 右键双击: 打开图片 **/
        if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 2) {
            // Copy RandomImage.png to ClipBoard
            FileUtil.clipImage(ConfigFile.getApplicationConfigPath() + "RandomImage.png");
        }

    }

    @FXML
    public void menuitem_view_gameversion_archive_path_OnAction(ActionEvent actionEvent) {
        GameVersion gv = getSelectedGameVersion();
        FileUtil.viewFolder(gv.smartlyGetGameArchive_Path());
    }

    /**
     * 重新载入数据: GameVersion 和 ArchiveSeries.
     * 该方法在[手动修改本地文件数据]后调用.
     */
    @FXML
    public void menuitem_update_data_OnAction(ActionEvent actionEvent) {
        update_combobox_backup_game_version();
        update_combobox_backup_archive_series();
    }

    public void menuitem_delete_all_archivebean_except_OnAction(ActionEvent actionEvent) {

        // Delete
        ObservableList<ArchiveBean> abs = listview_archive_beans.getItems();
        if (abs.isEmpty() == true) {
            JavaFxUtil.DialogTools.notFountArchiveBeanInThisArchiveSeries();
            return;
        }

        // 选中ArchiveBean
        ArchiveBean selectedArchiveBean = getSelectedArchiveBean();
        if (selectedArchiveBean == null) {
            JavaFxUtil.DialogTools.mustChooseArchiveBean_Dialog();
            return;
        }

        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        JavaFxUtil.DialogTools.setIcon(askAlert);
        askAlert.setTitle("删除除此所有ArchiveBean");
        askAlert.setHeaderText("ArchiveSeries：" + getSelectedArchiveSeries().getArchiveSeries_Name() + "\n除此ArchiveBean：" + selectedArchiveBean.getArchiveBean_Name());
        askAlert.setContentText("确定要删除除此ArchiveBean外的所有ArchiveBean吗？");
        Optional<ButtonType> result = askAlert.showAndWait();

        if (result.get() == ButtonType.OK) {

            ArchiveSeries as = getSelectedArchiveSeries();

            for (ArchiveBean ab : as.getAllArchiveBeans()) {

                // 删除除了选中ArchiveBean的所有ArchiveBean
                if (selectedArchiveBean.getArchiveBean_Name().equals(ab.getArchiveBean_Name()) == false) {
                    // ArchiveBean >> Delete
                    ab.delete();
                }
            }

        } else {
            return;
        }

        // Update
        update_combobox_backup_archive_series();

    }
}
