package com.sakurawald.util;

import com.sakurawald.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class JavaFxUtil {

    public static void centerImage(ImageView imgView) {
        Image img = imgView.getImage();

        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imgView.getFitWidth() / img.getWidth();
            double ratioY = imgView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            reducCoeff = Math.min(ratioX, ratioY);

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imgView.setX((imgView.getFitWidth() - w) / 2);
            imgView.setY((imgView.getFitHeight() - h) / 2);

        }
    }

    public static class DialogTools {

        public static void mustChooseArchiveSeries_Dialog() {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.WARNING, "请先选中一个ArchiveSeries！", ButtonType.OK).show();
        }

        public static void mustChooseArchiveBean_Dialog() {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.WARNING, "请先选中一个ArchiveBean！", ButtonType.OK).show();
        }

        public static void mustChooseGameVersion_Dialog() {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.WARNING, "请先选中一个GameVersion！", ButtonType.OK).show();
        }

        public static void notFountArchiveBeanInThisArchiveSeries() {
            JavaFxUtil.DialogTools.alert(Alert.AlertType.WARNING, "当前ArchiveSeries没有任何的ArchiveBean！", ButtonType.OK).show();
        }

        public static void setIcon(Dialog dialog) {
            // Set Icon
            ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image(
                    Main.class.getResourceAsStream("icon.png")));
        }

        public static Alert alert(Alert.AlertType alertType, String contentText, ButtonType... buttonTypes) {
            Alert a = new Alert(alertType, contentText, buttonTypes);
            setIcon(a);
            return a;
        }
    }

}
