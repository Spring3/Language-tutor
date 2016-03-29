package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
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
    private ChoiceBox<Language> chb_word_lang;

    @FXML
    private Button btn_openFile;

    @FXML
    private ChoiceBox<Language> chb_translation_lang;

    private ResourceBundle bundle;
    private Language selectedLanguage;
    private Language selectedTranslationLanguage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
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
            StageManager.getInstance().closeStage(StageManager.getInstance().getStage(1));
            return;
        }
        chb_word_lang.getItems().addAll(selectedLanguages);
        chb_translation_lang.getItems().addAll(LanguageDAO.getInstance().readAllLanguages());
        chb_translation_lang.getSelectionModel().selectedItemProperty().addListener((observable1, oldValue1, newValue1) -> {
            selectedTranslationLanguage = newValue1;
            if (selectedLanguage != null){
                btn_openFile.setDisable(false);
                btn_openFile.setOnAction(event -> {
                    processFile();
                });
            }
            else{
                btn_openFile.setDisable(true);
            }
        });
        chb_word_lang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedLanguage = newValue;
            fileLanguageSelectionChangedEventHandler();
        });

        chb_word_lang.getSelectionModel().clearSelection();

        chb_word_lang.getSelectionModel().select(Controller.selectedLanguage);
        chb_translation_lang.getSelectionModel().select(AuthController.getActiveUser().getNativeLanguage());
    }
    /**
     * Event Handler for a choice box, responsible for a local file data language selection
     */
    private void fileLanguageSelectionChangedEventHandler(){
        if (selectedTranslationLanguage != null) {
            btn_openFile.setDisable(false);
            btn_openFile.setOnAction(event -> {
                processFile();
            });
        }
        else{
            btn_openFile.setDisable(true);
        }
    }

    private void processFile(){
        //Choosing a file to open
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString(ResourceBundleKeys.FILE_CHOOSER_TITLE));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CVS files (*.csv)", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            BasicParser parser = new BasicParser();
            parser.parse(selectedFile, selectedLanguage, selectedTranslationLanguage);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Files imported successfully");
            alert.setContentText("Total words imported: " + parser.getTotalWordsAmount() + "\n"
            + "Total words added: " + parser.getAddedWordsAmount() + "\n"
            + "Total words ignored: " + parser.getIgnoredWordsAmount() + "\n");
            alert.show();
            StageManager.getInstance().closeStage(StageManager.getInstance().getStage(1));
        }
    }
}
