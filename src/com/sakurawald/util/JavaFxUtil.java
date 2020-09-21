package com.sakurawald.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveSeries！", ButtonType.OK).show();
        }

        public static void mustChooseArchiveBean_Dialog() {
            new Alert(Alert.AlertType.WARNING, "请先选中一个ArchiveBean！", ButtonType.OK).show();
        }

        public static void mustChooseGameVersion_Dialog() {
            new Alert(Alert.AlertType.WARNING, "请先选中一个GameVersion！", ButtonType.OK).show();
        }

        public static void notFountArchiveBeanInThisArchiveSeries() {
            new Alert(Alert.AlertType.WARNING, "当前ArchiveSeries没有任何的ArchiveBean！", ButtonType.OK).show();
        }
    }

}
