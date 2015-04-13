package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tutor.Main;
import tutor.dao.LanguageDAO;
import tutor.models.Language;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;
import tutor.util.UserConfigHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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

    private LanguageSettingsController settingsController;
    private StageManager stageManager;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        stageManager = StageManager.getInstance();
    }

    public void btn_addLanguage_clicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource(Navigator.LANGUAGE_SETTINGS_VIEW_PATH));
        loader.setResources(ResourceBundle.getBundle("locale/lang", Locale.getDefault()));
        try {
            Parent parent = loader.load();
            LanguageSettingsController controller = loader.getController();
            controller.setSettingsController(this);
            Scene scene = new Scene(parent);
            scene.getStylesheets().add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(bundle.getString(ResourceBundleKeys.TITLE_ADD_NEW_LANG));
            stage.setOnHiding(windowEvent -> System.out.println("A stage on layer " + 2 + " was resetted"));
            final Stage stageDuplicate = stage;
            stageDuplicate.setOnCloseRequest(windowEvent -> StageManager.getInstance().closeStage(stageDuplicate));
            stageManager.putStage(Main.class.getClassLoader().getResource(Navigator.LANGUAGE_SETTINGS_VIEW_PATH), stageDuplicate, 2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setSettingsController (LanguageSettingsController controller){
        settingsController = controller;
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

    public void btn_deleteLanguage_clicked(ActionEvent actionEvent) {
        Language selectedLang = listView_languages.getSelectionModel().getSelectedItem();
        if (selectedLang != null) {
            listView_languages.getItems().remove(selectedLang);
            LanguageDAO.getInstance().delete(selectedLang);
            System.out.println("Language: " + selectedLang + " was deleted");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_TITLE));
            alert.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_LANG_NOT_SELECTED));
            alert.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_CHOOSE_LANG));
            alert.show();
        }
    }
}
