package com.sakurawald.data;

public class ImageAndText {

    private String image = null;
    private String text = null;

    public ImageAndText() {
    }

    @Override
    public String toString() {
        return "ImageAndText{" +
                "image='" + image + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public ImageAndText(String image, String text) {
        this.image = image;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
