package com.sakurawald.data;

/**
 * 描述诗歌
 */
public class Poetry {

    private String keySentence = null;
    private String title = null;
    private String dynasty = null;
    private String author = null;
    private String content = null;

    public Poetry(String keySentence) {
        this.keySentence = keySentence;
    }

    public Poetry(String keySentence, String title, String dynasty, String author, String content, String translate) {
        this.keySentence = keySentence;
        this.title = title;
        this.dynasty = dynasty;
        this.author = author;
        this.content = content;
        this.translate = translate;
    }

    @Override
    public String toString() {
        return "Poetry{" +
                "keySentence='" + keySentence + '\'' +
                ", title='" + title + '\'' +
                ", dynasty='" + dynasty + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", translate='" + translate + '\'' +
                '}';
    }

    private String translate = null;

    public String getKeySentence() {
        return keySentence;
    }

    public String getTitle() {
        return title;
    }

    public String getDynasty() {
        return dynasty;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTranslate() {
        return translate;
    }
}
