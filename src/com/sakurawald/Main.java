package com.sakurawald;

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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {

    public static FXMLLoader loader = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));

        // Create
        Parent root = loader.load();

        primaryStage.setTitle("Plants vs. Zombies Archive Manager (Sakura Wald)");
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
                //对话框 Alert Alert.AlertType.CONFIRMATION：反问对话框
                Alert askAlert = new Alert(Alert.AlertType.CONFIRMATION);
                //设置对话框标题
                askAlert.setTitle("退出");
                //设置内容
                askAlert.setHeaderText("你确定要退出本程序吗？");
                //显示对话框
                Optional<ButtonType> result = askAlert.showAndWait();
                //如果点击OK
                if (result.get() == ButtonType.OK){

                    beforeExit();

                    // 直接退出虚拟机
                    System.exit(0);

                } else {
                    event.consume();
                }
            }
        });

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

        MainController mc = Main.loader.getController();

        // Save Memory
        FileManager.tempConfig_File.getSpecificDataInstance().ArchiveMemory.selectedArchiveSeries = mc.combobox_backup_archive_series.getSelectionModel().getSelectedItem().getArchiveSeries_Name();
        FileManager.tempConfig_File.saveFile();

        // Save Memory
        FileManager.tempConfig_File.getSpecificDataInstance().ArchiveMemory.selectedGameVersion = mc.combobox_backup_game_version.getSelectionModel().getSelectedItem().getVersion_Name();
        FileManager.tempConfig_File.saveFile();
    }

}
