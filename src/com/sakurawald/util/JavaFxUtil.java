package com.sakurawald.util;

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
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imgView.setX((imgView.getFitWidth() - w) / 2);
            imgView.setY((imgView.getFitHeight() - h) / 2);

        }
    }
}
