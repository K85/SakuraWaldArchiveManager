package com.sakurawald.data;


/**
 * 描述某个[界面]的某些[需要保存状态的UI控件].
 * 该界面可实现该接口.
 */
public interface UIStorage {

    public void saveUI();

    public void loadUI();

    public default void reloadUI() {
        loadUI();
    }

    public default void saveAndReloadUI() {
        saveUI();
        reloadUI();
    }

}
