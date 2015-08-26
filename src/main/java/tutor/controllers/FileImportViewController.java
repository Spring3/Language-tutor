package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import tutor.dao.LanguageDAO;
import tutor.dao.UserDAO;
import tutor.models.Language;
import tutor.util.*;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 13.04.2015.
 */
public class FileImportViewController implements Initializable{

    @FXML
    private Button btn_openFile;

    @FXML
    private StackPane stackPanel;

    @FXML
    private TextField textField_filePath;

    @FXML
    private ChoiceBox<Language> chB_file_data_lang;

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
        ObservableList<Language> selectedLanguages = FXCollections.observableArrayList(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));
        if (selectedLanguages.size() == 0){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_TITLE));
            alert.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_LANG_NOT_SELECTED));
            alert.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_LANG_NOT_SELECTED));
            alert.showAndWait();
        }
        chB_file_data_lang.setItems(selectedLanguages);
        chB_file_data_lang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fileLanguageSelectionChangedEventHandler(newValue);
        });

        chB_file_data_lang.getSelectionModel().clearSelection();
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
                processFile();
            });
        }
        else{
            textField_filePath.setText("");
            textField_filePath.setDisable(true);
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
            new BasicParser().parse(selectedFile, selectedLanguage);

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
