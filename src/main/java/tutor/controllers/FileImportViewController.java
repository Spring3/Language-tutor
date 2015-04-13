package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import tutor.models.Language;
import tutor.util.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 13.04.2015.
 */
public class FileImportViewController implements Initializable{

    @FXML
    private ChoiceBox<String> choiceBox_data_source_type;

    @FXML
    private Button btn_openFile;

    @FXML
    private StackPane stackPanel;

    @FXML
    private TextField textField_filePath;

    @FXML
    private ChoiceBox<Language> chB_file_data_lang;

    @FXML
    private StackPane stackpanel_lingualeo;

    @FXML
    private TextField textField_lingualeoProfileURL;

    private ResourceBundle bundle;
    private StageManager stageManager;
    private Language selectedLanguage;
    private File selectedFile;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        stageManager = StageManager.getInstance();
        initializeChoiceBoxes();
    }

    /**
     * Loads choice boxes with the default data
     */
    private void initializeChoiceBoxes(){
        ObservableList<String> dataSource_types = FXCollections.observableArrayList(
                //resourceBundle.getString("source_type_local_db"),
                bundle.getString("source_type_file"),
                bundle.getString("source_type_google_disk_file"),
                bundle.getString("source_type_dropbox_file"),
                bundle.getString("source_type_lingualeo")
        );

        choiceBox_data_source_type.setItems(dataSource_types);

        initializeChoiceBoxEventHandlers();
    }

    /**
     * Binds choice box event handlers to the choiceboxes
     */
    private void initializeChoiceBoxEventHandlers(){
        choiceBox_data_source_type.getSelectionModel().selectedItemProperty().addListener((observableValue, old_value, new_value) -> {
            dataSourceChoiceBoxSelectedItemChangedEventHandler(old_value, new_value);
        });

        //if file data source language was changed
        chB_file_data_lang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fileLanguageSelectionChangedEventHandler(newValue);
        });
    }

    /**
     * Event Handler for a choice box, responsible for data source type selection
     * @param old_value stands for an old value, selected in choice box
     * @param new_value stands for a new value, selected in choice box
     */
    private void dataSourceChoiceBoxSelectedItemChangedEventHandler(String old_value, String new_value){
        //showing appropriate UI components depending on the selected item
        if (new_value != null && !new_value.isEmpty()) {
            if (!new_value.isEmpty() && !new_value.equals(bundle.getString("source_type_lingualeo"))) {
                stackpanel_lingualeo.setVisible(false);
                stackPanel.setVisible(true);
            }
            else if (new_value.equals(bundle.getString("source_type_lingualeo"))) {
                stackPanel.setVisible(false);
                stackpanel_lingualeo.setVisible(true);
            }else{
                stackPanel.setVisible(false);
            }
            chB_file_data_lang.getSelectionModel().clearSelection();
        }
    }

    /**
     * Event Handler for a choice box, responsoble for a local file data language selection
     * @param newValue stands for a new Language, selected in current choice box
     */
    private void fileLanguageSelectionChangedEventHandler(tutor.models.Language newValue){
        if (newValue != null) {
            selectedLanguage = newValue;
            btn_openFile.setDisable(false);
            textField_filePath.setDisable(false);
            btn_openFile.setOnAction(event -> {
                openFileClick();
            });
        }
        else{
            textField_filePath.setText("");
            textField_filePath.setDisable(true);
        }
    }

    private void openFileClick(){
        final String fileString = bundle.getString("source_type_file");
        final String googleDrive = bundle.getString("source_type_google_disk_file");
        final String dropbox = bundle.getString("source_type_dropbox_file");
        final String lingualeo = bundle.getString("source_type_lingualeo");
        String selectedItem = choiceBox_data_source_type.getSelectionModel().getSelectedItem();
        if (selectedItem.equals(fileString)) {
            processFile();
        }
        else if (selectedItem.equals(googleDrive)){
            GDriveManager gDriveManager = GDriveManager.getInstance(bundle);
            gDriveManager.analyzeFile(textField_filePath.getText());
            new GDriveParser(bundle).parse(gDriveManager, selectedLanguage);
        }

    }

    private void processFile(){
        //Choosing a file to open
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString(ResourceBundleKeys.FILE_CHOOSER_TITLE));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            //Writing a selected file's path to the textfield
            textField_filePath.setText(selectedFile.getAbsolutePath());
            new PlainFileParser(bundle).parse(selectedFile, selectedLanguage);

            textField_filePath.setDisable(false);
            textField_filePath.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                        if (newValue1.isEmpty())
                            btn_openFile.setDisable(true);
                    }
            );
        } else {
            textField_filePath.setText("");
            btn_openFile.setDisable(true);
        }
    }
}
