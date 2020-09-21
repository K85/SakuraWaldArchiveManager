package com.sakurawald.data;

/**
 * 用于描述有一个[句子]对象
 **/
public class Sentence {

    private int id = 0;
    private String content = null;

    private String type = null;
    private String from = null;
    private String creator = null;
    private String created_at = null;

    public Sentence(int id, String content, String type, String from,
                    String creator, String created_at) {
        super();
        this.id = id;
        this.content = content;
        this.type = type;
        this.from = from;
        this.creator = creator;
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getCreator() {
        return creator;
    }

    /**
     * @return 格式化后的文本, 可用于快速展示.
     */
    public String getFormatedString() {
        return "『" + this.getContent() + "』" + "-「" + this.getFrom() + "」";
    }

    public String getFrom() {
        return from;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Sentence [id=" + id + ", content=" + content + ", type=" + type
                + ", from=" + from + ", creator=" + creator + ", created_at="
                + created_at + "]";
    }

}
