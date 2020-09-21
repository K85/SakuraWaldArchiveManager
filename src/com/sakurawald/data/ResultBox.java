package com.sakurawald.data;

/**
 * 描述一个返回对象
 */
public class  ResultBox<E> {

    private E value = null;
    private int failCount = 0;
    private int successCount = 0;

    public ResultBox(E value) {
        this.value = value;
    }

    public void addFailCount() {
        this.failCount = this.failCount + 1;
    }

    public void addSuccessCount() {
        this.successCount  = this.successCount  + 1;
    }

    public ResultBox() {
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
}
