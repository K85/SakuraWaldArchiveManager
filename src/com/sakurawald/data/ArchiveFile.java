package com.sakurawald.data;

import com.sakurawald.archive.ArchiveBean;
import com.sakurawald.archive.ArchiveExplanation;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.ui.data.MyCheckBox;
import com.sakurawald.util.FileUtil;
import javafx.scene.control.CheckBox;

import java.io.File;
import java.io.IOException;

/**
 * 描述[ArchiveBean]中的[单个存档文件]
 */
public class ArchiveFile {

    private ArchiveBean owner_ArchiveBean = null;
    private File file = null;
    /**
     * 用于UI选择的CheckBox
     */
    private final MyCheckBox checkBox = new MyCheckBox();
    /**
     * 描述该ArchiveFile的ArchiveExplanation
     */
    private ArchiveExplanation archiveExplanation = null;

    public ArchiveFile(ArchiveBean owner_ArchiveBean, File file) {
        this.owner_ArchiveBean = owner_ArchiveBean;
        this.file = file;
    }

    public ArchiveBean getOwner_ArchiveBean() {
        return owner_ArchiveBean;
    }

    public ArchiveExplanation getArchiveExplanation() {
        return archiveExplanation;
    }

    public void setArchiveExplanation(ArchiveExplanation archiveExplanation) {
        this.archiveExplanation = archiveExplanation;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public MyCheckBox getMyCheckBox() {
        return checkBox;
    }

    /**
     * 对该ArchiveFile进行[回档]操作
     */
    public void rollback() {

        // Copy Single File
        String from = this.getFile().getAbsolutePath();
        String to = this.getOwner_ArchiveBean().getOwner_ArchiveSeries().getOwner_GameVersion().smartlyGetGameArchive_Path() + this.getFile().getName();
        try {
            FileUtil.copyFile(from, to);
        } catch (IOException e) {
            LoggerManager.logException(e);
        }

    }

}
