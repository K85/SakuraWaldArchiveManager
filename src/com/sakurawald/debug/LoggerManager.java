package com.sakurawald.debug;

import com.sakurawald.file.ApplicaitonConfig_Data;

import com.sakurawald.file.FileManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LoggerManager {

    public static final String SAD_FACIAL_EXPRESSION = "(ó﹏ò｡)";

    /**
     * [本地存储的记录器对象]
     */
    private static final Logger local_Logger = Logger.getLogger(LoggerManager.class);

    public static Logger getLogger() {
        return local_Logger;
    }

    public static void logDebug(String type, String msg, boolean forceLog) {
        logDebug("[" + type + "] " + msg, forceLog);
    }

    public static void logDebug(String content, boolean forceLog) {
        if (forceLog == true) {
            local_Logger.debug(content);
            return;
        }

        // 如果不存在ApplicationConfig.json文件, 则默认开启Debug模式
        if (FileManager.applicationConfig_File == null ||
                FileManager.applicationConfig_File.isHasInit() == false ||
                FileManager.applicationConfig_File.getSpecificDataInstance().Debug.debug == true) {
            local_Logger.debug(content);
        }
    }

    public static void logDebug(String content) {
        logDebug(content, false);
    }


    public static void logDebug(String type, String msg) {

        logDebug(type, msg, false);
    }

    /**
     * 输出Exception到本地存储, 并且展示错误对话框.
     */
    public static void logException(Exception e) {

        // 输出到<本地存储>
        String data = getExceptionInfo(e);
        LoggerManager.getLogger().error(data);

        // Show Dialog
        showErrorDialog(e);

    }


    public static String getExceptionInfo(Exception e) {

        StringBuilder result = new StringBuilder("错误类型: " + e.getClass());
        result.append("\n原因: ").append(e.getCause());
        result.append("\n消息: ").append(e.getMessage());
        result.append("\n栈追踪: ");

        for (StackTraceElement s : e.getStackTrace()) {
            result.append("\tat ").append(s).append("\r\n");
        }

        return result.toString();
    }

    public static void showErrorDialog(Exception e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误对话框");
                alert.setHeaderText("哦不！一个错误发生了 " + SAD_FACIAL_EXPRESSION + "？！！");
                alert.setContentText("错误原因：" + e.getCause() + "\n错误消息：" + e.getMessage());

                // Create expandable Exception.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                String exceptionText = sw.toString();

                Label label = new Label("错误栈追踪：");
                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                alert.getDialogPane().setExpandableContent(expContent);
                alert.show();
            }
        });


    }


}
