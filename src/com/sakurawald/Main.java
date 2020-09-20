package com.sakurawald;

import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import com.sakurawald.timer.AutoBackupTimer;
import com.sakurawald.timer.SmartAutoBackupTimer;
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
                    primaryStage.close();
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

        // 初始化计时器
        LoggerManager.logDebug("计时系统", "初始化Timer");
        AutoBackupTimer.getInstance().schedule();

        SmartAutoBackupTimer.getInstance().schedule();

        // 启动JavaFX程序
        launch(args);

    }


}
