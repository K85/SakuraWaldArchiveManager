package com.sakurawald.ui.controller;


import com.sakurawald.Main;
import com.sakurawald.api.*;
import com.sakurawald.archive.ArchiveBean;
import com.sakurawald.archive.ArchiveSeries;
import com.sakurawald.data.GameVersion;
import com.sakurawald.data.ImageAndText;
import com.sakurawald.data.ResultBox;
import com.sakurawald.data.UIStorage;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.ConfigFile;
import com.sakurawald.file.FileManager;
import com.sakurawald.timer.AutoBackupTimer;
import com.sakurawald.timer.SmartAutoBackupTimer;
import com.sakurawald.util.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import sun.security.krb5.Config;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class MainController implements UIStorage {


    @FXML
    private ImageView imageview_welcome;

    @FXML
    private Label label_welcome;

    @FXML
    public ListView<ArchiveBean> listview_archive_beans;

    @FXML
    public ComboBox<GameVersion> combobox_backup_game_version;

    @FXML
    public ComboBox<ArchiveSeries> combobox_backup_archive_series;

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
    public Button button_backup;

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

    @FXML
    public void initialize() {
        // Load UI
        loadUI();

        // Update GameVerison
        update_combobox_backup_game_version();

        // Update ArchiveSeries
        update_combobox_backup_archive_series();

        // Welcome
        updateWelcome();

        // Start Timer
        LoggerManager.logDebug("计时系统", "初始化Timer");
        AutoBackupTimer.getInstance().schedule();
        SmartAutoBackupTimer.getInstance().schedule();

    }

    public void saveUIAndLoadSettings() {
        saveUI();
        loadSettings();
    }

    @FXML
    void checkbox_smartautobackup_enable_OnAction(ActionEvent event) {
      CheckBox src = (CheckBox) event.getSource();
        FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.smartAutoBackup = src.isSelected();

        saveUIAndLoadSettings();
    }

    /**
     * 加载存储的使用习惯, 若对应的数据不存在, 则自动选中默认项
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
        CheckBox src = (CheckBox) event.getSource();
        FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.enable = src.isSelected();

        saveUIAndLoadSettings();
    }

    @FXML
    void checkbox_storagesettings_use_indepent_storage_OnAction(ActionEvent event) {
        CheckBox src = (CheckBox) event.getSource();
        FileManager.applicationConfig_File.getSpecificDataInstance().Base.StorageSettings.useIndependentStorage = src.isSelected();

        // Update
        saveUIAndLoadSettings();
        update_combobox_backup_game_version();
        update_combobox_backup_archive_series();

    }

    @FXML
    void button_smartautobackup_test_OnAction(ActionEvent event) {

        // Get GameVersion
        GameVersion gv = combobox_backup_game_version.getSelectionModel().getSelectedItem();
        if (gv == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个GameVersion！", ButtonType.OK).show();;
            return;
        }

        // Test
        if (gv.getCheatEngine() == null) {
            new Alert(Alert.AlertType.ERROR, "该GameVersion（" + gv.toString()+"）未配置引擎！").show();
            return;
        }

        ResultBox<Integer> r = gv.getCheatEngine().getValue();

        if (r.getSuccessCount() > 0) {
            new Alert(Alert.AlertType.INFORMATION, "该GameVersion（" + gv.toString()+"）的智能自动备份有效！").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "该GameVersion（" + gv.toString()+"）的智能自动备份无效！").show();
        }


    }

    @FXML
    void textfield_autobackup_seconds_OnKeyReleased(KeyEvent event) {

        // 防止左右方向键频繁刷新
        if (event.getCode().isArrowKey() == true) {
            return;
        }

        TextField src = (TextField) event.getSource();

        FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.time_second = Integer.valueOf(src.getText());
        saveUIAndLoadSettings();
    }

    public void backup() {
        // Create Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                String temp = button_backup.getText();
                button_backup.setDisable(true);
                button_backup.setText("...");

                // 获取GameVersion
                GameVersion gv = combobox_backup_game_version.getSelectionModel().selectedItemProperty().get();

                // 备份该GameVersion
                ArchiveSeries archiveSeries = combobox_backup_archive_series.getSelectionModel().selectedItemProperty().get();

                // 判断选中的[ArchiveSeries]是否空. 若空则创建默认的ArchiveSeries
                System.out.println("当前的系列：" + archiveSeries);
                if (archiveSeries == null) {
                    LoggerManager.logDebug("该GameVersion没有任何存在的ArchiveSeries, 即将创建默认的ArchiveSeries: GameVersion = " + gv.getVersion_Name());

                    // Create Default ArchiveSeries
                    gv.createDefaultArchiveSeries();

                    // Update
                    archiveSeries = combobox_backup_archive_series.getSelectionModel().selectedItemProperty().get();
                }

                archiveSeries.backup();

                // Update
                update_combobox_backup_archive_series();

                button_backup.setDisable(false);
                button_backup.setText(temp);

            }
        });

    }

    @FXML
    void button_backup_OnAction(ActionEvent event) {
        backup();
    }


    /**
     * 读取配置文件
     */
    public void loadUI() {
        // 加载配置文件
        FileManager.applicationConfig_File.reloadFile();

        // 加载[存档]
        loadArchive();

        // 加载[设置]
        loadSettings();
    }


public void loadGameVersions() {combobox_backup_game_version.getItems().clear();
    for (GameVersion gv : FileManager.gameVersionConfig_File.getSpecificDataInstance().gameVersions) {
        combobox_backup_game_version.getItems().add(gv);
    }

    // Load Memory >> GameVerison
    loadMemory_GameVersion();

    }

    public void loadArchive() {
    loadGameVersions();

    }


    public void loadSettings() {
            // 生效配置文件
            checkbox_smartautobackup_enable.setSelected(FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.smartAutoBackup);
            checkbox_autobackup_timing.setSelected(FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.enable);
            checkbox_storagesettings_use_indepent_storage.setSelected(FileManager.applicationConfig_File.getSpecificDataInstance().Base.StorageSettings.useIndependentStorage);

            textfield_autobackup_seconds.setText(String.valueOf(FileManager.applicationConfig_File.getSpecificDataInstance().Base.AutoBackup.AutoBackupOnTime.time_second));
    }

    public void update_combobox_backup_archive_series() {

        // 获取ArchiveSeries
        ArchiveSeries as = combobox_backup_archive_series.getSelectionModel().getSelectedItem();

        // 获取该ArchiveSeries下的所有ArchiveBean
        listview_archive_beans.getItems().clear();

        if (as == null) {
            return;
        }

        for (ArchiveBean ab : as.getAllArchiveBeans()) {
            listview_archive_beans.getItems().add(ab);
        }

       listview_archive_beans.getItems().sort(new Comparator<ArchiveBean>() {

            @Override
            public int compare(ArchiveBean o1, ArchiveBean o2) {
                long a=Long.valueOf(o1.getArchiveBeanCreateTime());
                long b=Long.valueOf(o2.getArchiveBeanCreateTime());

                if (a > b) {
                    return -1;
                }

                return 1;
            }
        });


    }


    public void update_combobox_backup_game_version() {
        // 获取GameVersion
        GameVersion gv = combobox_backup_game_version.getSelectionModel().getSelectedItem();

        // Storage
        ArchiveSeries selected = combobox_backup_archive_series.getSelectionModel().getSelectedItem();

        // 获取该GameVersion的所有ArchiveSeries
        combobox_backup_archive_series.getItems().clear();
        for (ArchiveSeries archiveSeries :gv.getAllArchiveSeries()) {
            combobox_backup_archive_series.getItems().add(archiveSeries);
        }

        if (combobox_backup_archive_series.getItems().contains(selected) == true) {
            combobox_backup_archive_series.getSelectionModel().select(selected);
        } else {
            // Load Memory >> Auto Choose GameVersion
            loadMemory_ArchiveSeries();
        }


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


        /** 左键双击: 回档All **/
        if (event.getButton() == MouseButton.PRIMARY &&
        event.getClickCount() == 2) {
            rollbackAll();
        }

        /** 右键双击: 回档局部 **/
        if (event.getButton() == MouseButton.MIDDLE) {
            rollbackPartly();
        }
    }

    public void update_textarea_archive_bean_info() {
        ArchiveBean ab = listview_archive_beans.getSelectionModel().getSelectedItem();
        if (ab != null) {
            textarea_archive_bean_info.setText(ab.getInfo());
        }

    }


    @FXML
    void menuitem_create_archiveseries_OnAction(ActionEvent event) {
        GameVersion gv = combobox_backup_game_version.getSelectionModel().getSelectedItem();
        gv.createArchiveSeries_UI();

    }

    @FXML
    void menuitem_import_archiveseries_OnAction(ActionEvent event)  {

        ArchiveSeries as = combobox_backup_archive_series.getSelectionModel().getSelectedItem();
        if (as == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveSeries", ButtonType.OK).show();
            return;
        }

        // Load FXML
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("ImportWindow.fxml"));

        // Create
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            LoggerManager.logException(e);
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
        setRemarkArchiveBean_UI();

        // Update
        update_textarea_archive_bean_info();
    }

    public void setRemarkArchiveBean_UI() {

        ArchiveBean ab = listview_archive_beans.getSelectionModel().getSelectedItem();
        if (ab == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveBean！", ButtonType.OK).show();
            return;
        }

        String originalRemark = ab.getArchiveBeanConfig().getSpecificDataInstance().Information.remark;

        TextInputDialog dialog = new TextInputDialog(originalRemark);
        dialog.setTitle("设置ArchiveBean的备注");
        dialog.setHeaderText("为该ArchiveBean（"+ ab.getArchiveBean_Name()+ "）设置备注");
        dialog.setContentText("备注：");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();


        if (result.isPresent() == true){

            String input = result.get();

            // 文件名过滤器
            input = FileUtil.fileNameFilter(input);


            if (input.trim().equalsIgnoreCase("") == true) {
                return;
            }

            ab.setRemark(input);

        } else {
            //若点击了取消, 则直接返回
            return;
        }

    }

    @FXML
    void menuitem_delete_archivebean_OnAction(ActionEvent event) {

        // Delete
        ArchiveBean ab = listview_archive_beans.getSelectionModel().getSelectedItem();
        if (ab == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveBean！", ButtonType.OK).show();
            return;
        }

        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        askAlert.setTitle("删除该ArchiveBean");
        askAlert.setHeaderText("你确定要删除该ArchiveBean（" +ab.getArchiveBean_Name()+"）吗？");
        Optional<ButtonType> result = askAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            ab.delete();
        } else {
            return;
        }

        // Update
        update_combobox_backup_archive_series();
    }

    @FXML
    void menuitem_delete_archiveseries_OnAction(ActionEvent event) {
        GameVersion gv = combobox_backup_game_version.getSelectionModel().getSelectedItem();
        gv.deleteArchiveSeries_UI();
    }

    @FXML
    void menuitem_rename_archiveseries_OnAction(ActionEvent event) {
        ArchiveSeries as = combobox_backup_archive_series.getSelectionModel().getSelectedItem();
        if (as == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个GameSeries！", ButtonType.OK).show();
            return;
        }

        as.renameArchiveSeries();
    }

    @FXML
    void listview_archive_beans_OnKeyPressed(KeyEvent event) {

        if (event.getCode() == KeyCode.UP ||
        event.getCode() == KeyCode.DOWN) {
            update_textarea_archive_bean_info();
        }
    }


    public void rollbackAll() {
        // 选中ArchiveBean
        ArchiveBean ab = listview_archive_beans.getSelectionModel().getSelectedItem();
        if (ab == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveBean！", ButtonType.OK).show();;
            return;
        }

        // RollBack
        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        askAlert.setTitle("回档该ArchiveBean");
        askAlert.setHeaderText("你确定要回档该ArchiveBean（" +ab.getArchiveBean_Name()+"）吗？");
        Optional<ButtonType> result = askAlert.showAndWait();
        boolean ret = false;
        if (result.get() == ButtonType.OK){
            ret = ab.rollBackAll();
        } else {
            return;
        }

        if (ret == true) {
            new Alert(Alert.AlertType.INFORMATION, "回档成功！", ButtonType.OK).show();
        } else {
            new Alert(Alert.AlertType.ERROR, "回档失败！（可能的原因：权限不足 或 文件被占用）", ButtonType.OK).show();
        }
    }

    @FXML
    void menuitem_rollback_all_archive_bean_OnAction(ActionEvent event) {
        rollbackAll();
    }

    public void rollbackPartly() {
        // 选中ArchiveBean
        ArchiveBean ab = listview_archive_beans.getSelectionModel().getSelectedItem();
        if (ab == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveBean！", ButtonType.OK).show();;
            return;
        }

        ab.rollbackPartly();
    }

    @FXML
    void menuitem_rollback_partly_archive_bean_OnAction(ActionEvent event) {
       rollbackPartly();
    }

    /**
     * 保存配置文件
     */
    public void saveUI() {
        FileManager.applicationConfig_File.saveFile();
    }


    @FXML
    public void menuitem_view_archivebean_path_OnAction(ActionEvent actionEvent) {

        ArchiveBean ab = listview_archive_beans.getSelectionModel().getSelectedItem();

        if (ab == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveBean！", ButtonType.OK).show();;
            return;
        }

        try {
            Desktop.getDesktop().open(new File( ab.getArchiveBeanPath()));
        } catch (IOException e) {
            LoggerManager.logException(e);
        }
    }

    @FXML
    public void menuitem_delete_all_archivebean_OnAction(ActionEvent actionEvent) {

        // Delete
        ObservableList<ArchiveBean> abs = listview_archive_beans.getItems();
        if (abs.isEmpty() == true) {
            new Alert(Alert.AlertType.WARNING, "当前ArchiveSeries没有任何的ArchiveBean！", ButtonType.OK).show();
            return;
        }

        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        askAlert.setTitle("删除所有ArchiveBean");
        askAlert.setHeaderText("你确定要删除所有ArchiveBean吗？");
        Optional<ButtonType> result = askAlert.showAndWait();

        if (result.get() == ButtonType.OK){

            ArchiveSeries as  = combobox_backup_archive_series.getSelectionModel().getSelectedItem();

            for (ArchiveBean ab : as.getAllArchiveBeans()) {
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
        ArchiveSeries as = combobox_backup_archive_series.getSelectionModel().getSelectedItem();

        if (as == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveSeries！", ButtonType.OK).show();;
            return;
        }

        try {
            Desktop.getDesktop().open(new File( as.getArchiveSeriesPath()));
        } catch (IOException e) {
           LoggerManager.logException(e);
        }

    }

    @FXML
    public void menuitem_view_gameversion_path_OnAction(ActionEvent actionEvent) {
        GameVersion gv = combobox_backup_game_version.getSelectionModel().getSelectedItem();

        if (gv == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个GameVersion！", ButtonType.OK).show();;
            return;
        }


        try {
            File target = new File( gv.getGameVersionPath());

            // 创建文件夹防止报错
            target.mkdirs();

            Desktop.getDesktop().open(target);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }
    }

    @FXML
    public void menuitem_rename_archivebean_OnAction(ActionEvent actionEvent) {

        ArchiveBean ab = listview_archive_beans.getSelectionModel().getSelectedItem();
        if (ab == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个GameBean！", ButtonType.OK).show();
            return;
        }

        ab.renameArchiveBean();

    }

    public void menuitem_view_archivebeans_path_OnAction(ActionEvent actionEvent) {

        try {
            File target = new File( ArchiveBean.getArchiveBeansPath());

            // 创建文件夹防止报错
            target.mkdirs();

            Desktop.getDesktop().open(target);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }

    }

    @FXML
    public void menuitem_view_application_config_path_OnAction(ActionEvent actionEvent) {
        try {
            File target = new File(ConfigFile.getApplicationConfigPath());

            // 创建文件夹防止报错
            target.mkdirs();

            Desktop.getDesktop().open(target);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }
    }

    @FXML
    public void menuitem_view_application_run_path_OnAction(ActionEvent actionEvent) {
        try {
            File target = new File(FileUtil.getJavaRunPath());

            // 创建文件夹防止报错
            target.mkdirs();

            Desktop.getDesktop().open(target);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }
    }

    /**
     * 更新[欢迎界面]
     */
    public void updateWelcome() {

        /** 结果定义 **/
        ImageAndText result = new ImageAndText();

        new Thread(new Runnable(){

            @Override
            public void run() {
                /** 获取随机句子 **/
                if (FileManager.applicationConfig_File.getSpecificDataInstance().Welcome.RandomSentence.goToAncientTimes == true) {
                    result.setText(JinRiShiCiAPI.getPoetry().getKeySentence());
                } else {
                    result.setText(HitoKotoAPI.getRandomSentence().getFormatedString());
                }

                /** 获取随机图片 **/
                final String image_Path = ConfigFile.getApplicationConfigPath() + "RandomImage.png";
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
                        label_welcome.setText(result.getText());
                        imageview_welcome.setImage(new Image("file:" + image_Path));
                        JavaFxUtil.centerImage(imageview_welcome);
                    }
                });

            }
        }).start();





    }

    @FXML
    void imageview_welcome_OnMouseClicked(MouseEvent event) {

        // 左键双击: 刷新图文
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            updateWelcome();
        }

        // 右键双击: 打开图片
        if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 2) {
            // Copy RandomImage.png to ClipBoard
            FileUtil.clipImage(ConfigFile.getApplicationConfigPath() + "RandomImage.png");
        }

    }

    @FXML
    public void menuitem_view_gameversion_archive_path_OnAction(ActionEvent actionEvent) {
        try {

            GameVersion gv = combobox_backup_game_version.getSelectionModel().getSelectedItem();

            File target = new File(gv.smartlyGetGameArchive_Path());

            // 创建文件夹防止报错
            target.mkdirs();

            Desktop.getDesktop().open(target);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }
    }

    @FXML
    public void menuitem_update_data_OnAction(ActionEvent actionEvent) {
        update_combobox_backup_game_version();
        update_combobox_backup_archive_series();
    }

    public void menuitem_delete_all_archivebean_except_OnAction(ActionEvent actionEvent) {

        // Delete
        ObservableList<ArchiveBean> abs = listview_archive_beans.getItems();
        if (abs.isEmpty() == true) {
            new Alert(Alert.AlertType.WARNING, "当前ArchiveSeries没有任何的ArchiveBean！", ButtonType.OK).show();
            return;
        }

        // 选中ArchiveBean
        ArchiveBean selectedArchiveBean = listview_archive_beans.getSelectionModel().getSelectedItem();
        if (selectedArchiveBean == null) {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveBean！", ButtonType.OK).show();
            return;
        }

        Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
        askAlert.setTitle("删除除此所有ArchiveBean");
        askAlert.setHeaderText("你确定要删除除此（" +selectedArchiveBean.getArchiveBean_Name() + "）所有ArchiveBean吗？");
        Optional<ButtonType> result = askAlert.showAndWait();

        if (result.get() == ButtonType.OK){

            ArchiveSeries as  = combobox_backup_archive_series.getSelectionModel().getSelectedItem();

            for (ArchiveBean ab : as.getAllArchiveBeans()) {

                // 删除除了选中ArchiveBean的所有ArchiveBean
                if (selectedArchiveBean.getArchiveBean_Name().equals(ab.getArchiveBean_Name()) == false) {
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
