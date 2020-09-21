package com.sakurawald.data;

/**
 * 描述一种[图文数据对象]
 */
public class ImageAndText {

    private String image = null;
    private String text = null;

    /**
     * Fast Constructor.
     */
    public ImageAndText() {
        // Do nothing.
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
