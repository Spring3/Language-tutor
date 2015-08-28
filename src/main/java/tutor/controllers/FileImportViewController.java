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
    private Label label_addedWords;

    @FXML
    private Label label_totalWords;

    @FXML
    private TextField textField_search;

    @FXML
    private Label label_importFailure;

    @FXML
    private ChoiceBox<Language> chb_word_lang;

    @FXML
    private Button btn_openFile;

    @FXML
    private TableColumn<Language, String> column_wordsLang;

    @FXML
    private TableColumn<Integer, Integer> column_wordsAmount;

    @FXML
    private TableView tableView_library;

    @FXML
    private Label label_success;

    @FXML
    private Label label_ignoredWords;

    @FXML
    private TableColumn<Language, String> column_translationLang;

    @FXML
    private TableColumn<?, ?> column_download;

    @FXML
    private AnchorPane pane_importInfo;

    @FXML
    private ChoiceBox<Language> chb_translation_lang;

    private ResourceBundle bundle;
    private StageManager stageManager;
    private Language selectedLanguage;
    private Language selectedTranslationLanguage;
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
        chb_word_lang.setItems(selectedLanguages);
        chb_translation_lang.setItems(FXCollections.observableArrayList(LanguageDAO.getInstance().readAllLanguages()));
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
            fileLanguageSelectionChangedEventHandler(newValue);
        });

        chb_word_lang.getSelectionModel().clearSelection();
    }
    /**
     * Event Handler for a choice box, responsoble for a local file data language selection
     * @param newValue stands for a new Language, selected in current choice box
     */
    private void fileLanguageSelectionChangedEventHandler(tutor.models.Language newValue){
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
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            BasicParser parser = new BasicParser();
            parser.parse(selectedFile, selectedLanguage, selectedTranslationLanguage);
            pane_importInfo.setVisible(true);
            if (parser.isSuccessfull()){
                label_success.setVisible(true);
                label_importFailure.setVisible(false);
            }
            else{
                label_success.setVisible(false);
                label_importFailure.setVisible(true);
            }
            label_totalWords.setText(parser.getTotalWordsAmount() + "");
            label_addedWords.setText(parser.getAddedWordsAmount() + "");
            label_ignoredWords.setText(parser.getIgnoredWordsAmount() + "");
        }
    }
}
