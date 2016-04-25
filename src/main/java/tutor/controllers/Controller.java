package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import tutor.Main;
import tutor.dao.LanguageDAO;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.tasks.TaskManager;
import tutor.tasks.dictation.Dictation;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main controller for the main.fxml view.
 */
public class Controller implements Initializable
{
    public Controller(){
        stageManager = StageManager.getInstance();
    }

    private StageManager stageManager = null;
    @FXML
    private Label newsItemLabel;
    @FXML
    private MenuItem mItem_InviteFriend;
    @FXML
    private Label onlineTask_header;
    @FXML
    private MenuItem mItem_exit;
    @FXML
    private MenuItem mItem_check4Updates;
    @FXML
    private ImageView onlineTask_img;
    /*
      <MenuItem fx:id="mItem_lang" mnemonicParsing="false" text="Language" />
                  <MenuItem fx:id="mItem_importFile" mnemonicParsing="false" text="Import File" />
                  <MenuItem fx:id="mItem_locale" mnemonicParsing="false" text="Locale" />
                  <MenuItem fx:id="mItem_theme" mnemonicParsing="false" text="Theme" />
     */
    @FXML
    private MenuItem mItem_lang;
    @FXML
    private MenuItem mItem_importFile;
    @FXML
    private MenuItem mItem_locale;
    @FXML
    private MenuItem mItem_dictionary;
    @FXML
    private ImageView newsItemImage;
    @FXML
    private ListView<Language> listView_languages;
    @FXML
    private MenuItem mItem_newTask;
    @FXML
    private MenuItem mItem_about;
    @FXML
    private MenuItem mItem_statistics;
    @FXML
    private AnchorPane taskPane;
    @FXML
    private Tab workbench;
    @FXML
    private ImageView task_dictation_img;
    @FXML
    private Label task_dictation_header;

    private ResourceBundle bundle;
    public static Language selectedLanguage;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        bundle = rb;
        listView_languages.getItems().addAll(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));
        listView_languages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedLanguage = newValue;
        });

        try {
            if (listView_languages.getItems().get(0) != null) {
                listView_languages.getSelectionModel().select(0);
            }
        }
        catch (IndexOutOfBoundsException ex){        }

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem add = new MenuItem("Add language");
        MenuItem remove = new MenuItem("Remove language");
        contextMenu.getItems().addAll(add, remove);

        add.setOnAction((event) -> {
            stageManager.navigateTo(Navigator.getPathFor(Navigator.LANGUAGE_SETTINGS_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_LANGUAGE_SETTINGS), 1, Optional.empty(), false);
            updateLanguageList();
        });

        remove.setOnAction((event) -> {
            Language selectedItem = listView_languages.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                LanguageDAO.getInstance().unAssignLangFromCurrentUser(selectedItem);
                updateLanguageList();
            }
        });

        listView_languages.setContextMenu(contextMenu);

        task_dictation_img.setImage(new Image(Main.class.getClassLoader().getResource("tasks/dictation/dictation-image.jpg").toExternalForm(), true));
        stageManager.getStage(0).setOnCloseRequest(windowEvent -> Shutdown(null));
    }

    @FXML
    public void Shutdown(ActionEvent actionEvent) {
        stageManager.Shutdown();

    }

    public void statisticsClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Navigator.getPathFor(Navigator.USER_RATE_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_STATISTICS), 1, Optional.empty(), false);
    }

    public void menuItem_about_clicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Navigator.getPathFor(Navigator.ABOUT_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_ABOUT), 1, Optional.empty(), false);
    }

    public void menuItem_dictionary_clicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Navigator.getPathFor(Navigator.DICTIONARY_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_DICTIONARY), 1, Optional.empty(), false);
    }

    public void editLangClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Navigator.getPathFor(Navigator.LANGUAGE_SETTINGS_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_LANGUAGE_SETTINGS), 1, Optional.empty(), false);
    }

    public void importFileClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Navigator.getPathFor(Navigator.FILE_IMPORT_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_IMPORT), 1, Optional.empty(), false);
    }

    public void localeClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Navigator.getPathFor(Navigator.LOCALE_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_LOCALE), 1, Optional.empty(), false);
    }

    public void LogOut(ActionEvent actionEvent) {
        AuthController.setActiveUser(null);
        stageManager.navigateTo(Navigator.getPathFor(Navigator.AUTHENTICATION_VIEW_PATH), "Language Tutor", 0, Optional.empty(), false);
    }

    public void dictationTaskClicked(Event event) {
        if (selectedLanguage == null)
            return;                       //TODO: add alert notification
        if (WordDAO.getInstance().countFor(selectedLanguage) > 0) {
            stageManager.navigateTo(Navigator.getPathFor(Navigator.TASKVIEW_DICTATION_PATH), bundle.getString(ResourceBundleKeys.LABEL_DICTATION), 1, Optional.empty(), false);
            ((DictationViewController) stageManager.getControllerForViewOnLayer(1)).init();
        }
        else{
            showAlert();
        }
    }

    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString(ResourceBundleKeys.LABEL_ERROR));
        alert.setHeaderText(bundle.getString(ResourceBundleKeys.LABEL_NO_WORDS));
        alert.setContentText(bundle.getString(ResourceBundleKeys.LABEL_NO_WORDS_CONTEXT));
        alert.show();
    }

    public void updateLanguageList(){
        listView_languages.getItems().clear();
        listView_languages.getItems().addAll(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));
    }

    public void learningDictationTaskClicked(Event event) {
        if (selectedLanguage == null)
            return;
        if (WordDAO.getInstance().countFor(selectedLanguage) > 0){
            stageManager.navigateTo(Navigator.getPathFor(Navigator.TASKVIEW_DICTATION_PATH), bundle.getString(ResourceBundleKeys.LABEL_DICTATION), 1, Optional.empty(), false);
            ((DictationViewController) stageManager.getControllerForViewOnLayer(1)).init(Dictation.DictationType.LEARNING, TaskManager.Output.random());
        }
        else{
            showAlert();
        }
    }

    public void repeatingDictationTaskClicked(Event event) {
        if (selectedLanguage == null)
            return;
        if (WordDAO.getInstance().countFor(selectedLanguage) > 0){
            stageManager.navigateTo(Navigator.getPathFor(Navigator.TASKVIEW_DICTATION_PATH), bundle.getString(ResourceBundleKeys.LABEL_DICTATION), 1, Optional.empty(), false);
            ((DictationViewController) stageManager.getControllerForViewOnLayer(1)).init(Dictation.DictationType.REPEATING, TaskManager.Output.random());
        }
        else{
            showAlert();
        }
    }

    public void voiceDictationTaskClicked(Event event) {
        if (selectedLanguage == null)
            return;
        if (WordDAO.getInstance().countFor(selectedLanguage) > 0){
            stageManager.navigateTo(Navigator.getPathFor(Navigator.TASKVIEW_DICTATION_PATH), bundle.getString(ResourceBundleKeys.LABEL_DICTATION), 1, Optional.empty(), false);
            ((DictationViewController) stageManager.getControllerForViewOnLayer(1)).init(Dictation.DictationType.random(), TaskManager.Output.VOICE);
        }
        else{
            showAlert();
        }
    }
}
