package tutor.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tutor.dao.LanguageDAO;
import tutor.models.Language;
import tutor.util.StageManager;
import tutor.Main;
import tutor.util.UserConfigHelper;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

/**
 * Created by user on 14.12.2014.
 */
public class SettingsController extends Navigator implements Initializable {

    //UI components, injected from the settings.fxml
    @FXML
    private Button btn_reportLocaleMistake;
    @FXML
    private RadioButton radioButton_wordAndTranslation;
    @FXML
    private StackPane stackpanel_file;
    @FXML
    private RadioButton radioButton_wordAndTranslationDropbox;
    @FXML
    private Button btn_openGoogleDocFile;
    @FXML
    private Button btn_openFile;
    @FXML
    private RadioButton radioButton_wordAndTranslationGoogleDocs;
    @FXML
    private StackPane pane_lang;
    @FXML
    private TextField textField_lingualeoProfileURL;
    @FXML
    private Button btn_openDropboxFile;
    @FXML
    private Button btn_apply;
    @FXML
    private RadioButton radioButton_wordsOnly;
    @FXML
    private RadioButton radioButton_localizeManually;
    @FXML
    private TextField textField_backupLocalDBPath;
    @FXML
    private StackPane pane_data_source;
    @FXML
    private StackPane stackpanel_localDB;
    @FXML
    private Button btn_createLocalDB;
    @FXML
    private RadioButton radioButton_translationOnlyGoogleDocs;
    @FXML
    private RadioButton radioButton_translationOnlyDropbox;
    @FXML
    private ListView<Language> listView_languages;
    @FXML
    private Button btn_chooseBackupLocalDBPath;
    @FXML
    private Button btn_connectoToGoogleDocs;
    @FXML
    private Button btn_openLocalDB;
    @FXML
    private StackPane stackpanel_googleDrive;
    @FXML
    private ChoiceBox<String> choiceBox_localizeTo;
    @FXML
    private RadioButton radioButton_wordsOnlyGoogleDocs;
    @FXML
    private TextField textField_dropboxFileURL;
    @FXML
    private Button btn_connectToLingualeo;
    @FXML
    private RadioButton radioButton_wordsOnlyDropbox;
    @FXML
    private Button btn_ok;
    @FXML
    private TextField textField_localDBPath;
    @FXML
    private Button btn_loadLocalDB;
    @FXML
    private StackPane stackpanel_dropbox;
    @FXML
    private ChoiceBox<String> choiceBox_activeLocale;
    @FXML
    private StackPane stackpanel_lingualeo;
    @FXML
    private ListView<String> listView;
    @FXML
    private ChoiceBox<String> choiceBox_uncorrectLocale;
    @FXML
    private ChoiceBox<String> choiceBox_theme;
    @FXML
    private ChoiceBox<Language> chB_localDB_data_lang;
    @FXML
    private ChoiceBox<Language> chB_file_data_lang;
    @FXML
    private ChoiceBox<Language> chB_googleDrive_data_lang;
    @FXML
    private ChoiceBox<Language> chB_dropbox_data_lang;
    @FXML
    private Button btn_downloadLocale;
    @FXML
    private Button btn_removeDataSource;
    @FXML
    private StackPane pane_other;
    @FXML
    private Button btn_localize;
    @FXML
    private CheckBox checkBox_autobackup;
    @FXML
    private RadioButton radioButton_translationOnly;
    @FXML
    private TextField textField_localFilePath;
    @FXML
    private Button btn_loadLocalFile;
    @FXML
    private RadioButton radioButton_localizeAutomatically;
    @FXML
    private TextField textField_googleDocsFIleURL;
    @FXML
    private Label label_errorMessage;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_connectToDropBox;
    @FXML
    private TableView<?> tableview_datasources;
    @FXML
    private Button btn_correctLocaleMistake;
    @FXML
    private ChoiceBox<String> choiceBox_data_source_type;

    private StageManager stageManager;
    private ResourceBundle bundle;
    private static final String DIALOGS_INFO_TITLE = "dialogs_info_title";
    private static final String DIALOGS_LANG_NOT_SELECTED = "dialogs_info_header_lang_not_selected";
    private static final String DIALOGS_CHOOSE_LANG = "dialogs_info_choose_lang";
    private static final String TITLE_ADD_NEW_LANG = "title_add_new_lang";

    //initialization method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stageManager = StageManager.getInstance(3);
        bundle = resourceBundle;
        //Initializing items for listView
        StackPane[] panes = {pane_data_source, pane_lang, pane_other};
        ObservableList<String> items = FXCollections.observableArrayList(resourceBundle.getString("listView_item_data_source"), resourceBundle.getString("listView_item_language"), resourceBundle.getString("listView_item_others"));
        listView.setItems(items);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                for (int i = 0; i < items.size(); i++) {
                    if (newValue.equals(items.get(i))) {
                        panes[i].setVisible(true);
                    } else {
                        panes[i].setVisible(false);
                    }
                }
            }
        });
        listView.getSelectionModel().select(0);
        ObservableList<String> allLocales = FXCollections.observableArrayList();
        allLocales.add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE));
        choiceBox_activeLocale.setItems(allLocales);
        choiceBox_activeLocale.getSelectionModel().select(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE));

        //Creating toggleGroup for radiobuttons
        ToggleGroup radioButtonToggleGroup = new ToggleGroup();
        radioButton_translationOnly.setToggleGroup(radioButtonToggleGroup);
        radioButton_wordAndTranslation.setToggleGroup(radioButtonToggleGroup);
        radioButton_wordsOnly.setToggleGroup(radioButtonToggleGroup);

        ToggleGroup radioButtonGoogleToggleGroup = new ToggleGroup();
        radioButton_translationOnlyGoogleDocs.setToggleGroup(radioButtonGoogleToggleGroup);
        radioButton_wordAndTranslationGoogleDocs.setToggleGroup(radioButtonGoogleToggleGroup);
        radioButton_wordsOnlyGoogleDocs.setToggleGroup(radioButtonGoogleToggleGroup);

        ToggleGroup radioButtonDropboxToggleGroup = new ToggleGroup();
        radioButton_translationOnlyDropbox.setToggleGroup(radioButtonDropboxToggleGroup);
        radioButton_wordAndTranslationDropbox.setToggleGroup(radioButtonDropboxToggleGroup);
        radioButton_wordsOnlyDropbox.setToggleGroup(radioButtonDropboxToggleGroup);

        ToggleGroup languagePanelRadioButtonToggleGroup = new ToggleGroup();
        radioButton_localizeAutomatically.setToggleGroup(languagePanelRadioButtonToggleGroup);
        radioButton_localizeManually.setToggleGroup(languagePanelRadioButtonToggleGroup);

        //initializing choiceboxes
        ObservableList<String> dataSource_types = FXCollections.observableArrayList(
                //resourceBundle.getString("source_type_local_db"),
                resourceBundle.getString("source_type_file"),
                resourceBundle.getString("source_type_google_disk_file"),
                resourceBundle.getString("source_type_dropbox_file"),
                resourceBundle.getString("source_type_lingualeo")
        );

        choiceBox_data_source_type.setItems(dataSource_types);

        choiceBox_data_source_type.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old_value, String new_value) {
                if (old_value != null && !old_value.isEmpty()) {
                     /*if (old_value.equals(resourceBundle.getString("source_type_local_db"))){
                        stackpanel_localDB.setVisible(false);
                     }else*/
                    if (old_value.equals(resourceBundle.getString("source_type_file"))) {
                        stackpanel_file.setVisible(false);
                        chB_file_data_lang.getSelectionModel().clearSelection();
                    } else if (old_value.equals(resourceBundle.getString("source_type_google_disk_file"))) {
                        stackpanel_googleDrive.setVisible(false);
                        chB_googleDrive_data_lang.getSelectionModel().clearSelection();
                    } else if (old_value.equals(resourceBundle.getString("source_type_dropbox_file"))) {
                        stackpanel_dropbox.setVisible(false);
                        chB_dropbox_data_lang.getSelectionModel().clearSelection();
                    } else if (old_value.equals(resourceBundle.getString("source_type_lingualeo"))) {
                        stackpanel_lingualeo.setVisible(false);
                    }
                }

                if (new_value != null && !new_value.isEmpty()){
                    /*if (new_value.equals(resourceBundle.getString("source_type_local_db"))){
                        stackpanel_localDB.setVisible(true);
                    }else*/ if (new_value.equals(resourceBundle.getString("source_type_file"))) {
                        stackpanel_file.setVisible(true);
                    }else if (new_value.equals(resourceBundle.getString("source_type_google_disk_file"))){
                        stackpanel_googleDrive.setVisible(true);
                    }else if (new_value.equals(resourceBundle.getString("source_type_dropbox_file"))){
                        stackpanel_dropbox.setVisible(true);
                    }else if (new_value.equals(resourceBundle.getString("source_type_lingualeo"))){
                        stackpanel_lingualeo.setVisible(true);
                    }
                }
            }
        });
        chB_dropbox_data_lang.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Language>() {
            @Override
            public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newValue) {
                if (newValue != null)
                {

                }
                else{

                }
            }
        });
        chB_file_data_lang.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Language>() {
            @Override
            public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newValue) {
                if (newValue != null){
                    btn_openFile.setDisable(false);
                    btn_openFile.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //TODO: FileChooser
                        }
                    });
                    radioButton_wordAndTranslation.setDisable(false);
                    radioButton_wordsOnly.setDisable(false);
                    radioButton_translationOnly.setDisable(false);
                    textField_localFilePath.setDisable(false);
                    textField_localFilePath.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (!newValue.isEmpty()){
                                btn_loadLocalFile.setDisable(false);
                                btn_loadLocalFile.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        //TODO:Parse opened file
                                    }
                                });
                            }
                            else{
                                btn_loadLocalFile.setDisable(true);
                            }
                        }
                    });
                }
                else{
                    btn_openFile.setDisable(true);
                    radioButton_wordAndTranslation.setDisable(true);
                    radioButton_wordAndTranslation.setSelected(false);
                    radioButton_wordsOnly.setDisable(true);
                    radioButton_wordsOnly.setSelected(false);
                    radioButton_translationOnly.setDisable(true);
                    radioButton_translationOnly.setSelected(false);
                    textField_localFilePath.setDisable(true);
                    textField_localFilePath.setText("");
                }
            }
        });
        //listView_languages
        Refresh();
    }

    public void btn_cancel_clicked(ActionEvent actionEvent) {
        Stage currentStage = (Stage)btn_cancel.getScene().getWindow();
        stageManager.closeStage(currentStage);
    }

    public void btn_addLanguage_clicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(Navigator.ADD_LANGUAGE_VIEW_PATH));
        loader.setResources(ResourceBundle.getBundle("locale/lang", Locale.getDefault()));
        try {
            Parent parent = loader.load();
            AddLanguageController controller = loader.getController();
            controller.setSettingsController(this);
            Scene scene = new Scene(parent);
            scene.getStylesheets().add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(bundle.getString(TITLE_ADD_NEW_LANG));
            stage.setOnHiding(windowEvent -> System.out.println("A stage on layer " + 2 + " was resetted"));
            final Stage stageDuplicate = stage;
            stageDuplicate.setOnCloseRequest(windowEvent -> StageManager.getInstance(3).closeStage(stageDuplicate));
            stageManager.putStage(Main.class.getResource(Navigator.ADD_LANGUAGE_VIEW_PATH), stageDuplicate, 2);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void Refresh(){
        List<Language> currentUserLanguages = new LanguageDAO().readAllLanguages(AuthController.getActiveUser().getId());
        ObservableList<Language> userLanguages = FXCollections.observableArrayList();
        userLanguages.addAll(currentUserLanguages);
        listView_languages.setItems(userLanguages);
        chB_dropbox_data_lang.setItems(userLanguages);
        chB_file_data_lang.setItems(userLanguages);
        chB_googleDrive_data_lang.setItems(userLanguages);
        chB_localDB_data_lang.setItems(userLanguages);
    }

    public void btn_deleteLanguage_clicked(ActionEvent actionEvent) {
        Language selectedLang = listView_languages.getSelectionModel().getSelectedItem();
        if (selectedLang != null) {
            listView_languages.getItems().remove(selectedLang);
            new LanguageDAO().delete(selectedLang);
            System.out.println("Language: " + selectedLang + " for user: " + selectedLang.getOwner().getUserName() + " was deleted");
        }
        else{
            Alert errorMessage = new Alert(Alert.AlertType.INFORMATION);
            errorMessage.setTitle(bundle.getString(DIALOGS_INFO_TITLE));
            errorMessage.setHeaderText(bundle.getString(DIALOGS_LANG_NOT_SELECTED));
            errorMessage.setContentText(bundle.getString(DIALOGS_CHOOSE_LANG));
            errorMessage.showAndWait();
        }
    }
}
