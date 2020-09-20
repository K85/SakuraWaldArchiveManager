package com.sakurawald.ui.controller;

import com.sakurawald.Main;
import com.sakurawald.archive.ArchiveBean;
import com.sakurawald.archive.ArchiveSeries;
import com.sakurawald.data.GameVersion;
import com.sakurawald.debug.LoggerManager;
import com.sakurawald.file.FileManager;
import com.sakurawald.util.FileUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImportController {

    @FXML
    private TreeView<String> treeview_all_archive_bean;

    @FXML
    private Button button_import;

    @FXML
    private Button button_select_invert;

    @FXML
    private Button button_select_all;

    @FXML
    void button_import_OnAction(ActionEvent event) {

        // Create Thread
        Platform.runLater(new Runnable() {


            @Override
            public void run() {

                // 获取所有选中的ArchiveBean
                ArrayList<ArchiveBean> abs = getSelectedArchiveBeans();

                // Get Data
                MainController mc = Main.loader.getController();

                // Import
                for (ArchiveBean ab : abs) {

                    File source_File = new File(ab.getArchiveBeanPath());
                    File source_Parent_File = source_File.getParentFile();
                    source_Parent_File.mkdirs();

                    String source = source_File.getAbsolutePath();
                    String target = mc.combobox_backup_archive_series.getSelectionModel().getSelectedItem().getArchiveSeriesPath() + source_File.getName();
                    try {
                        FileUtil.copyFolder(source, target);
                    } catch (IOException e) {
                        LoggerManager.logException(e);
                    }

                }

                // Update
                mc.update_combobox_backup_archive_series();

                // Close Window
                ((Stage)treeview_all_archive_bean.getScene().getWindow()).close();
            }
        });


    }

    @FXML
    void button_select_all_OnAction(ActionEvent event) {

        ((CheckBoxTreeItem) treeview_all_archive_bean.getRoot()).setSelected(true);

    }

    @FXML
    void button_select_invert_OnAction(ActionEvent event) {

        CheckBoxTreeItem root = (CheckBoxTreeItem) treeview_all_archive_bean.getRoot();

        for (CheckBoxTreeItem cbti : getAllArchiveBean()) {
            cbti.setSelected(! cbti.isSelected());
        }
    }

    /**
     * 从UI上获取所有的ArchiveBean的CheckBoxTreeItem
     */
    public ArrayList<CheckBoxTreeItem> getAllArchiveBean() {

            ArrayList<CheckBoxTreeItem> result = new ArrayList<CheckBoxTreeItem>();

        getChildren(result, (CheckBoxTreeItem) treeview_all_archive_bean.getRoot());

        return result;
    }

    /**
     * 从UI上获取选中的所有ArchiveBean
     */
    public ArrayList<ArchiveBean> getSelectedArchiveBeans() {

        ArrayList<ArchiveBean> result = new ArrayList<ArchiveBean>();

        for (CheckBoxTreeItem cbti : getAllArchiveBean()) {

            if (cbti.getValue() instanceof ArchiveBean && cbti.isSelected() == true) {
                result.add((ArchiveBean) cbti.getValue());
            }

        }

        return result;
    }


    private void getChildren(ArrayList<CheckBoxTreeItem> result,  CheckBoxTreeItem node) {

        if (node.getValue() instanceof ArchiveBean) {
            result.add(node);
        }

        if (node.getChildren().isEmpty() == false) {

            for (Object children : node.getChildren()) {
                getChildren(result, (CheckBoxTreeItem) children);
            }

        }


    }

    @FXML
    public void initialize() {

        TreeItem<String> classes = new TreeItem<>("所有的ArchiveBean");

        treeview_all_archive_bean.setRoot(this.getTreeItem_AllArchiveBean());
        // 设置TreeView为 复选框模式
        treeview_all_archive_bean.setCellFactory(CheckBoxTreeCell.<String>forTreeView());

    }

    /**
     * 获取所有的ArchiveBean, 包括不同GameVersion的.
     */
    public TreeItem getTreeItem_AllArchiveBean() {

        /** 获取控制器 **/
        MainController mc = Main.loader.getController();

        /** 构造Root节点 **/
        CheckBoxTreeItem result = new CheckBoxTreeItem("所有的GameVersion");
        result.setExpanded(true);

        // GameVersion
        for (GameVersion gv : FileManager.gameVersionConfig_File.getSpecificDataInstance().gameVersions) {

            CheckBoxTreeItem cbti_gameVersion = new CheckBoxTreeItem(gv);
            cbti_gameVersion.setExpanded(true);

            result.getChildren().add(cbti_gameVersion);

            // ArchiveSeries
            for (ArchiveSeries as : gv.getAllArchiveSeries()) {

                CheckBoxTreeItem cbti_archiveSeries = new CheckBoxTreeItem(as);

                cbti_gameVersion.getChildren().add(cbti_archiveSeries);

                // ArchiveBean
                for (ArchiveBean ab : as.getAllArchiveBeans()) {
                    CheckBoxTreeItem cbti_archiveBean = new CheckBoxTreeItem(ab);

                    cbti_archiveSeries.getChildren().add(cbti_archiveBean);

                }

            }

        }

        return result;
    }
}
