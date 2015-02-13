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
import javafx.stage.WindowEvent;
import tutor.dao.LanguageDAO;
import tutor.models.Language;
import tutor.util.StageManager;
import tutor.Main;
import tutor.util.UserConfigHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

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

    //initialization method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stageManager = StageManager.getInstance(3);
        //Initializing items for listView
        StackPane[] panes = {pane_data_source, pane_lang, pane_other};
        ObservableList<String> items = FXCollections.observableArrayList(resourceBundle.getString("listView_item_data_source"), resourceBundle.getString("listView_item_language"), resourceBundle.getString("listView_item_others"));
        listView.setItems(items);

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String selectedItem = listView.getSelectionModel().getSelectedItem().toString();
                if (selectedItem != null){
                    for (int i = 0 ; i < items.size(); i++){
                        if (selectedItem.equals(items.get(i))){
                            panes[i].setVisible(true);
                        }
                        else{
                            panes[i].setVisible(false);
                        }
                    }
                }
            }
        });




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
                resourceBundle.getString("source_type_local_db"),
                resourceBundle.getString("source_type_file"),
                resourceBundle.getString("source_type_google_disk_file"),
                resourceBundle.getString("source_type_dropbox_file"),
                resourceBundle.getString("source_type_lingualeo")
        );

        choiceBox_data_source_type.setItems(dataSource_types);

        choiceBox_data_source_type.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old_value, String new_value) {
                if (old_value != null && !old_value.isEmpty()){
                     if (old_value.equals(resourceBundle.getString("source_type_local_db"))){
                        stackpanel_localDB.setVisible(false);
                     }else if (old_value.equals(resourceBundle.getString("source_type_file"))) {
                         stackpanel_file.setVisible(false);
                     }else if (old_value.equals(resourceBundle.getString("source_type_google_disk_file"))){
                         stackpanel_googleDrive.setVisible(false);
                     }else if (old_value.equals(resourceBundle.getString("source_type_dropbox_file"))){
                         stackpanel_dropbox.setVisible(false);
                     }else if (old_value.equals(resourceBundle.getString("source_type_lingualeo"))){
                         stackpanel_lingualeo.setVisible(false);
                     }
                }

                if (new_value != null && !new_value.isEmpty()){
                    if (new_value.equals(resourceBundle.getString("source_type_local_db"))){
                        stackpanel_localDB.setVisible(true);
                    }else if (new_value.equals(resourceBundle.getString("source_type_file"))) {
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
            stage.setTitle("Add new language");
            stage.setOnHiding(windowEvent -> System.out.println("A stage on layer " + 2 + " was resetted"));
            final Stage stageDuplicate = stage;
            stageDuplicate.setOnCloseRequest(windowEvent -> StageManager.getInstance(3).closeStage(stageDuplicate));
            stageManager.putStage(Main.class.getResource(Navigator.ADD_LANGUAGE_VIEW_PATH), stageDuplicate, 2);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        //stageManager.navigateTo(Main.class.getResource(Navigator.ADD_LANGUAGE_VIEW_PATH), "Add new language", 2, Optional.empty());
    }

    public void Refresh(){
        List<Language> currentUserLanguages = new LanguageDAO().readAllLanguages(AuthController.getActiveUser().getId());
        ObservableList<Language> userLanguages = FXCollections.observableArrayList();
        for(Language lang : currentUserLanguages){
            userLanguages.add(lang);
        }
        listView_languages.setItems(userLanguages);
    }
}
