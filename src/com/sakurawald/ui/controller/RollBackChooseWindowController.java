package com.sakurawald.ui.controller;

import com.sakurawald.Main;
import com.sakurawald.archive.ArchiveBean;
import com.sakurawald.archive.ArchiveExplanation;
import com.sakurawald.data.ArchiveFile;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class RollBackChooseWindowController {

    @FXML
    private Button button_rollback;

    @FXML
    private Button button_select_all;

    @FXML
    private Button button_select_invert;

    @FXML
    private TableView<ArchiveFile> tableview_choose;

    @FXML
    private TableColumn<ArchiveFile, CheckBox> tableview_choose_checkbox;

    @FXML
    private TableColumn<ArchiveFile, String> tableview_choose_filename;

    @FXML
    private TableColumn<ArchiveFile, String> tableview_choose_archive_explanation;

    @FXML
    public void initialize() {

        /** Init TableView **/

        //Bind Data
        tableview_choose_checkbox.setCellValueFactory(cellData -> cellData.getValue().getMyCheckBox().getCheckBox());
        tableview_choose_filename.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFile().getName()));
        tableview_choose_archive_explanation.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getArchiveExplanation().getExplanation()));

        /** Update **/
        update_treetableview_choose();
    }

    /**
     * 更新[ArchiveFile选择界面].
     */
    public void update_treetableview_choose() {

        // Get Selected Archive Bean
        MainController mc = MainController.getInstance();
        ArchiveBean ab = mc.getSelectedArchiveBean();

        // 获取ArchiveBean的存档文件
        ArrayList<ArchiveFile> afs = new ArrayList<ArchiveFile>();
        for (File f : Objects.requireNonNull(new File(ab.getArchiveBeanPath()).listFiles())) {

            /**
             * File Filter: 过滤[目录]和[ArchiveBeanConfig.json文件]
             */
            if (f.isFile() == true && f.getName().equalsIgnoreCase("ArchiveBeanConfig.json") == false) {
                afs.add(new ArchiveFile(ab, f));
            }
        }

        // Analyse All ArchiveExplanation
        ArchiveExplanation.analyseArchiveExplanation(afs);

        // Add To UI
        ObservableList<ArchiveFile> data = FXCollections.observableArrayList(afs);
        tableview_choose.setItems(data);
    }


    public ArrayList<ArchiveFile> getAllSelectedArchiveFile() {
        ArrayList<ArchiveFile> result = new ArrayList<ArchiveFile>();

        for (ArchiveFile af : tableview_choose.getItems()) {
            if (af.getMyCheckBox().getCheckBox().getValue().isSelected() == true) {
                result.add(af);
            }
        }

        return result;

    }

    @FXML
    void button_rollback_OnAction(ActionEvent event) {

        // Get All Selected ArchiveFile
        ArrayList<ArchiveFile> afs = getAllSelectedArchiveFile();

        // RollBack
        for (ArchiveFile af : afs) {
            af.rollback();
        }

        // Close
        ((Stage) tableview_choose.getScene().getWindow()).close();
    }

    @FXML
    void button_select_all_OnAction(ActionEvent event) {

        for (ArchiveFile af : tableview_choose.getItems()) {
            af.getMyCheckBox().getCheckBox().getValue().setSelected(true);
        }

    }

    @FXML
    void button_select_invert_OnAction(ActionEvent event) {
        for (ArchiveFile af : tableview_choose.getItems()) {
            boolean flag = af.getMyCheckBox().getCheckBox().getValue().isSelected();
            af.getMyCheckBox().getCheckBox().getValue().setSelected(!flag);
        }
    }

    @FXML
    void tableview_choose_OnMouseClicked(MouseEvent event) {

        // 左键双击: 快速勾选
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            ArchiveFile af = tableview_choose.getSelectionModel().getSelectedItem();
            af.getMyCheckBox().getCheckBox().getValue().setSelected(!af.getMyCheckBox().getCheckBox().getValue().isSelected());
        }


    }
}
