package com.sakurawald.debug;
import com.sakurawald.file.ApplicaitonConfig_Data;

import com.sakurawald.file.FileManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LoggerManager {
    /**
     * [本地日志]文件
     */
    private static final Logger local_Logger = Logger.getLogger(LoggerManager.class);

    public static Logger getLogger() {
        return local_Logger;
    }

    public static void logDebug(String type, String msg, boolean forceLog) {

        if (forceLog == true) {
            local_Logger.debug("[" + type + "] " + msg);
            return;
        }

        // 如果不存在ApplicationConfig.json文件, 则默认开启Debug模式
        if (FileManager.applicationConfig_File == null ||
                FileManager.applicationConfig_File.isHasInit() == false ||
                FileManager.applicationConfig_File.getSpecificDataInstance().Debug.debug == true) {
            local_Logger.debug("[" + type + "] " + msg);
        }

    }



    public static void logDebug(String type, String msg) {

        logDebug(type,msg,false);
    }

    public static void logException(Exception e) {

            String data = getExceptionInfo(e);
            // 输出到<本地存储>
            LoggerManager.getLogger().error(data);

            // Show Dialog
        showErrorDialog(e);

    }


    public static String getExceptionInfo(Exception e) {
        String result = "错误类型: " + e.getClass();
        result = result + "\n原因: " + e.getCause();
        result = result + "\n消息: " + e.getMessage();
        result = result + "\n栈追踪: ";
        for (StackTraceElement s : e.getStackTrace()) {
            result = result + "\tat " + s + "\r\n";
        }

        return result;
    }

    public static void showErrorDialog(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误对话框");
        alert.setHeaderText("哦不！一个错误发生了！");
        alert.setContentText("错误原因：" + ex.getCause() + "\n错误消息：" + ex.getMessage());

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
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


}
