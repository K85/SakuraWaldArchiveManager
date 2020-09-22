package com.sakurawald.data;

/**
 * 描述一个[返回值对象].
 */
public class ResultBox<E> {

    private E value = null;
    private int failCount = 0;
    private int successCount = 0;
    private boolean getWindowHandle = false;
    private boolean getWindowProcessID = false;
    private boolean getWindowProcessHandle = false;

    public boolean isGetWindowHandle() {
        return getWindowHandle;
    }

    public void setGetWindowHandle(boolean getWindowHandle) {
        this.getWindowHandle = getWindowHandle;
    }

    public boolean isGetWindowProcessID() {
        return getWindowProcessID;
    }

    public void setGetWindowProcessID(boolean getWindowProcessID) {
        this.getWindowProcessID = getWindowProcessID;
    }

    public boolean isGetWindowProcessHandle() {
        return getWindowProcessHandle;
    }

    public void setGetWindowProcessHandle(boolean getWindowProcessHandle) {
        this.getWindowProcessHandle = getWindowProcessHandle;
    }

    public ResultBox(E value) {
        this.value = value;
    }

    public void addFailCount() {
        this.failCount = this.failCount + 1;
    }

    public void addSuccessCount() {
        this.successCount = this.successCount + 1;
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

    /**
     * 快熟处理计次.
     */
    public void addCount(boolean isSuccess) {
        if (isSuccess) {
            addSuccessCount();
        } else {
            addFailCount();
        }

    }

    /**
     * @return 该ResultBox的报告信息.
     */
    public String getReport() {
        return "Report: getWindowHandle = " + getWindowHandle + ", getWindowProcessID = " + getWindowProcessID + ", getWindowProcessHandle = " + getWindowProcessHandle + ", successCount = " + successCount + ", failCount = " + failCount + ", value = " + getValue() + ".";
    }
}
