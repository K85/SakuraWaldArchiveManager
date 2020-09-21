package com.sakurawald;

import com.sakurawald.archive.ArchiveSeries;
import com.sakurawald.data.GameVersion;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import com.sakurawald.timer.AutoBackupTimer;
import com.sakurawald.timer.SmartAutoBackupTimer;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.FileUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {

    /**
     * Decompilation Easter
     */
    public static final String Decompilation_Easter = "Hi！当你看到这些文字时，可能你已经尝试反编译该软件了。\n" +
            "奈何本人技术较差，代码写的粗糙，请Dalao们轻喷！\n" +
            "哦，对了。该项目没有进行混淆处理，若有疑问可以自行查看源码。" + LoggerManager.SAD_FACIAL_EXPRESSION;

    public static FXMLLoader loader = null;
    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;
        loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));

        // Create
        Parent root = loader.load();

        primaryStage.setTitle("Plants vs. Zombies Archive Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        // Set Icon
        primaryStage.getIcons().add(new Image(
                Main.class.getResourceAsStream("icon.png")));


        // Show
        primaryStage.show();

        // Add Events
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
                askAlert.setTitle("退出");
                askAlert.setHeaderText("确定要退出本程序吗？");
                Optional<ButtonType> result = askAlert.showAndWait();
                if (result.get() == ButtonType.OK) {

                    // Call
                    beforeExit();

                    // 直接退出虚拟机
                    System.exit(0);

                } else {
                    event.consume();
                }
            }
        });

        // Register Shortcut Key
        KeyCombination kc = new KeyCodeCombination(KeyCode.B, KeyCombination.ALT_DOWN);
        Mnemonic mnemonic = new Mnemonic(MainController.getInstance().button_backup, kc);
        Main.stage.getScene().addMnemonic(mnemonic);

    }

    public static void main(String[] args) {

        // 初始化日志路径
        String rootPath = FileUtil.getJavaRunPath();
        System.setProperty("local_logger.base_path", rootPath);

        // 初始化配置文件系统
        try {
            FileManager.getInstance().init();
        } catch (IllegalAccessException e) {
            LoggerManager.logException(e);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }

        // 启动JavaFX程序
        launch(args);
    }

    /**
     * 在JVM退出之前执行
     */
    public void beforeExit() {

        MainController mc = MainController.getInstance();

        // Save Memory
        GameVersion selectedGameVersion = mc.getSelectedGameVersion();
        if (selectedGameVersion != null) {
            FileManager.tempConfig_File.getSpecificDataInstance().ArchiveMemory.selectedGameVersion = selectedGameVersion.getVersion_Name();
            FileManager.tempConfig_File.saveFile();
        }

        // Save Memory
        ArchiveSeries selectedArchiveSeries = mc.getSelectedArchiveSeries();
        if (selectedArchiveSeries != null) {
            FileManager.tempConfig_File.getSpecificDataInstance().ArchiveMemory.selectedArchiveSeries = selectedArchiveSeries.getArchiveSeries_Name();
            FileManager.tempConfig_File.saveFile();
        }

    }

}
