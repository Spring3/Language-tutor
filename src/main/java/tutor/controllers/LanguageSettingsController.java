package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tutor.dao.LanguageDAO;
import tutor.models.Language;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by user on 13.04.2015.
 */
public class LanguageSettingsController implements Initializable {

    @FXML
    private ListView<Language> listView_allLanguages;
    @FXML
    private ListView<Language> listView_languages;
    @FXML
    private Button btn_apply;
    @FXML
    private TextField tf_search;
    @FXML
    private TableColumn<Language, String> column_translationLang;
    @FXML
    private TableColumn<?, ?> column_download;
    @FXML
    private TableColumn<Language, String> column_wordsLang;
    @FXML
    private TableColumn<Integer, Integer> column_wordsAmount;
    @FXML
    private TableView tableView_library;
    @FXML
    private TextField textField_search;

    private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        ObservableList<Language> allLanguages = FXCollections.observableList(LanguageDAO.getInstance().readAllLanguages());
        listView_allLanguages.setItems(allLanguages);
        ObservableList<Language> userLanguages = FXCollections.observableArrayList(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));
        listView_languages.setItems(userLanguages);

        tf_search.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue.isEmpty()){
                ObservableList<Language> allLanguageslist = FXCollections.observableList(LanguageDAO.getInstance().readAllLanguages());
                listView_allLanguages.setItems(allLanguageslist);
            }
            else
            {
                ObservableList<Language> filteredLanguages = FXCollections.observableArrayList(LanguageDAO.getInstance().readAllLanguages().parallelStream()
                        .filter((lang) -> lang.getLang_name().toLowerCase().startsWith(newValue.toLowerCase()))
                        .collect(Collectors.toList()));
                listView_allLanguages.setItems(filteredLanguages);
            }
        });

        listView_allLanguages.setOnMouseClicked(value ->{
            if (value.getClickCount() == 2){
                saveLanguage();
            }
        });

        listView_languages.setOnMouseClicked(value ->{
            if (value.getClickCount() == 2){
                deleteLanguage();
            }
        });
    }

    private void saveLanguage(){
        Language selectedLanguage = listView_allLanguages.getSelectionModel().getSelectedItem();
        LanguageDAO.getInstance().assignLangToCurrentUser(selectedLanguage);
        Refresh();
    }

    /**
     * Loads all languages for current user and puts them into listboxes and choiceboxes
     */
    public void Refresh() {
        List<Language> currentUserLanguages = LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId());
        ObservableList<Language> userLanguages = FXCollections.observableArrayList();
        userLanguages.addAll(currentUserLanguages);
        listView_languages.setItems(userLanguages);
    }

    private void deleteLanguage(){
        Language selectedLang = listView_languages.getSelectionModel().getSelectedItem();
        if (selectedLang != null) {
            listView_languages.getItems().remove(selectedLang);
            LanguageDAO.getInstance().unAssignLangFromCurrentUser(selectedLang);
            System.out.println("Language: " + selectedLang + " was deleted");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_TITLE));
            alert.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_LANG_NOT_SELECTED));
            alert.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_CHOOSE_LANG));
            alert.show();
        }
    }

    public void btnApplyClicked(ActionEvent actionEvent) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.closeStage(stageManager.getStage(1));
    }
}
