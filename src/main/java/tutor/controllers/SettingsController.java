package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tutor.dao.LanguageDAO;
import tutor.models.Language;
import tutor.util.*;
import tutor.Main;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.scene.control.Alert;

/**
 * Created by user on 14.12.2014.
 */
public class SettingsController implements Initializable {

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
    private Button btn_correctLocaleMistake;
    @FXML
    private ChoiceBox<String> choiceBox_data_source_type;

    private StageManager stageManager;
    private ResourceBundle bundle;
    private volatile File selectedFile;
    private List<File> themeDirectories;
    private File selectedThemeFile;
    private Language selectedLanguage;
    private ToggleGroup radioButtonToggleGroup;
    private ToggleGroup radioButtonGoogleToggleGroup;
    private ToggleGroup radioButtonDropboxToggleGroup;
    private ToggleGroup languagePanelRadioButtonToggleGroup;


    //initialization method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //StageManager for operating with stages
        stageManager = StageManager.getInstance();
        bundle = resourceBundle;

        initializeListView();
        //listView_languages
        Refresh();
        //TODO: repair this method
        //findAllThemes();  this method throws NullPointerException
        loadActiveLocale();
        initToggleGroups();
        initializeChoiceBoxes();

    }

    /**
     * Loads tab names for settings.fxml view
     */
    private void initializeListView(){
        StackPane[] panes = {pane_data_source, pane_lang, pane_other};
        ObservableList<String> items = FXCollections.observableArrayList(bundle.getString("listView_item_data_source"), bundle.getString("listView_item_language"), bundle.getString("listView_item_others"));
        listView.setItems(items);
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            for (int i = 0; i < items.size(); i++) {
                if (newValue.equals(items.get(i))) {
                    panes[i].setVisible(true);
                } else {
                    panes[i].setVisible(false);
                }
            }
        });
        listView.getSelectionModel().select(0);
    }

    /**
     * Finds current active locale and shows it in the choice box
     */
    private void loadActiveLocale(){
        ObservableList<String> allLocales = FXCollections.observableArrayList();
        allLocales.add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE));
        choiceBox_activeLocale.setItems(allLocales);
        choiceBox_activeLocale.getSelectionModel().select(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE));
    }

    /**
     * Initializes toggle groups for radiobuttons
     */
    private void initToggleGroups(){
        radioButtonToggleGroup = new ToggleGroup();
        radioButton_translationOnly.setToggleGroup(radioButtonToggleGroup);
        radioButton_wordAndTranslation.setToggleGroup(radioButtonToggleGroup);
        radioButton_wordsOnly.setToggleGroup(radioButtonToggleGroup);

        radioButtonGoogleToggleGroup = new ToggleGroup();
        radioButton_translationOnlyGoogleDocs.setToggleGroup(radioButtonGoogleToggleGroup);
        radioButton_wordAndTranslationGoogleDocs.setToggleGroup(radioButtonGoogleToggleGroup);
        radioButton_wordsOnlyGoogleDocs.setToggleGroup(radioButtonGoogleToggleGroup);

        radioButtonDropboxToggleGroup = new ToggleGroup();
        radioButton_translationOnlyDropbox.setToggleGroup(radioButtonDropboxToggleGroup);
        radioButton_wordAndTranslationDropbox.setToggleGroup(radioButtonDropboxToggleGroup);
        radioButton_wordsOnlyDropbox.setToggleGroup(radioButtonDropboxToggleGroup);

        languagePanelRadioButtonToggleGroup = new ToggleGroup();
        radioButton_localizeAutomatically.setToggleGroup(languagePanelRadioButtonToggleGroup);
        radioButton_localizeManually.setToggleGroup(languagePanelRadioButtonToggleGroup);

        initTooglesToggleChangedHandlers();
    }

    /**
     * Initializes toogle changed event handlers for toggle groups of radiobuttons
     */
    private void initTooglesToggleChangedHandlers(){
        radioButtonToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (textField_localFilePath.getText() != null) {
                    if (!textField_localFilePath.getText().isEmpty()) {
                        btn_loadLocalFile.setDisable(false);
                        btn_loadLocalFile.setOnAction(event -> {
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    new PlainFileParser(bundle).parse(selectedFile, getPlainFileContentType(), selectedLanguage);
                                }
                            };
                            thread.run();
                        });
                    }
                }
            }
        });

        radioButtonGoogleToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !textField_googleDocsFIleURL.getText().isEmpty()) {
                btn_openGoogleDocFile.setDisable(false);
            }
        });
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
     * Event Handler for a choice box, responsible for data source type selection
     * @param old_value stands for an old value, selected in choice box
     * @param new_value stands for a new value, selected in choice box
     */
    private void dataSourceChoiceBoxSelectedItemChangedEventHandler(String old_value, String new_value){
        //showing appropriate UI components depending on the selected item
        if (old_value != null && !old_value.isEmpty()) {
                 /*if (old_value.equals(resourceBundle.getString("source_type_local_db"))){
                    stackpanel_localDB.setVisible(false);
                 }else*/
            if (old_value.equals(bundle.getString("source_type_file"))) {
                stackpanel_file.setVisible(false);
                chB_file_data_lang.getSelectionModel().clearSelection();
            } else if (old_value.equals(bundle.getString("source_type_google_disk_file"))) {
                stackpanel_googleDrive.setVisible(false);
                chB_googleDrive_data_lang.getSelectionModel().clearSelection();
            } else if (old_value.equals(bundle.getString("source_type_dropbox_file"))) {
                stackpanel_dropbox.setVisible(false);
                chB_dropbox_data_lang.getSelectionModel().clearSelection();
            } else if (old_value.equals(bundle.getString("source_type_lingualeo"))) {
                stackpanel_lingualeo.setVisible(false);
            }
        }

        if (new_value != null && !new_value.isEmpty()) {
                /*if (new_value.equals(resourceBundle.getString("source_type_local_db"))){
                    stackpanel_localDB.setVisible(true);
                }else*/
            if (new_value.equals(bundle.getString("source_type_file"))) {
                stackpanel_file.setVisible(true);
            } else if (new_value.equals(bundle.getString("source_type_google_disk_file"))) {
                stackpanel_googleDrive.setVisible(true);
            } else if (new_value.equals(bundle.getString("source_type_dropbox_file"))) {
                stackpanel_dropbox.setVisible(true);
            } else if (new_value.equals(bundle.getString("source_type_lingualeo"))) {
                stackpanel_lingualeo.setVisible(true);
            }
        }
    }

    /**
     * Event Handler for a choice box, responsoble for a local file data language selection
     * @param newValue stands for a new Language, selected in current choice box
     */
    private void fileLanguageSelectionChangedEventHandler(Language newValue){
        if (newValue != null) {
            selectedLanguage = newValue;
            btn_openFile.setDisable(false);
            radioButton_wordAndTranslation.setDisable(false);
            radioButton_wordsOnly.setDisable(false);
            textField_localFilePath.setDisable(false);
            radioButton_translationOnly.setDisable(false);
            btn_openFile.setOnAction(event -> {
                //Choosing a file to open
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(bundle.getString(ResourceBundleKeys.FILE_CHOOSER_TITLE));
                selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    //Writing a selected file's path to the textfield
                    textField_localFilePath.setText(selectedFile.getAbsolutePath());
                    //if radiobutton was toggled
                    if (radioButtonToggleGroup.getSelectedToggle() != null) {
                        btn_loadLocalFile.setDisable(false); //activate load btn to parse the selected file
                        btn_loadLocalFile.setOnAction(event1 -> {
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    new PlainFileParser(bundle).parse(selectedFile, getPlainFileContentType(), selectedLanguage);
                                }
                            };
                            thread.run();

                        });
                    }

                    textField_localFilePath.setDisable(false);
                    textField_localFilePath.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                                if (newValue1.isEmpty()) {
                                    btn_loadLocalFile.setDisable(true);
                                }
                            }
                    );
                } else {
                    radioButton_wordAndTranslation.setSelected(false);
                    radioButton_wordsOnly.setSelected(false);
                    radioButton_translationOnly.setSelected(false);
                    textField_localFilePath.setText("");
                }
            });
        }
        else{
            textField_localFilePath.setText("");
            textField_localFilePath.setDisable(true);
            radioButtonToggleGroup.selectToggle(null);
            btn_loadLocalFile.setDisable(true);
        }
    }

    /**
     * Event Handler for a button, that loads a file from google drive
     * @param gDriveManager a manager, responsible for all the transactions between google drive and this program
     * @param newLangValue a language of a selected file's content
     */
    private void btnOpenGoogleFileClickEventHandler(GDriveManager gDriveManager, Language newLangValue) {
        if (!textField_googleDocsFIleURL.getText().isEmpty()) {
            InputStream fileInputStream = gDriveManager.getFileInputStream(textField_googleDocsFIleURL.getText());
            ContentType selectedContentType = null;
            if (radioButton_wordAndTranslationGoogleDocs.isSelected()) {
                selectedContentType = ContentType.WORDS_TRANSLATION;
            } else if (radioButton_wordsOnlyGoogleDocs.isSelected()) {
                selectedContentType = ContentType.WORDS_ONLY;
            } else if (radioButton_translationOnlyGoogleDocs.isSelected()) {
                selectedContentType = ContentType.TRANSLATION_ONLY;
            } else {
                AlertThrower.throwAlert(Alert.AlertType.ERROR, bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_TITLE), bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_HEADER_NO_CONTENT_TYPE), bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_CONTENT_NO_CONTENT_TYPE));
            }
            new GDriveParser(bundle, gDriveManager.getDataSourceType()).parse(fileInputStream, selectedContentType, newLangValue);

        } else {
            AlertThrower.throwAlert(Alert.AlertType.ERROR, bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_TITLE), bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_HEADER_NO_CONTENT_TYPE), bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_CONTENT_NO_CONTENT_TYPE));
        }

    }

    /**
     * Event handler for a text box with a link to a file on goggle drive
     * @param newValue stands for a new value, written into the text box
     * @param newLangValue stands for a language of a file content
     */
    private void googleFileURLTextFieldTextChangedEventHandler(String newValue, Language newLangValue){
        if (newValue != null){
            if (!newValue.isEmpty()) {
                GDriveManager gDriveManager = GDriveManager.getInstance(bundle);
                btn_openGoogleDocFile.setDisable(false);
                btn_openGoogleDocFile.setOnAction(event -> {
                    btnOpenGoogleFileClickEventHandler(gDriveManager, newLangValue);
                });
            }
            else{
                btn_openGoogleDocFile.setDisable(true);
            }
        }
    }

    /**
     * Event Handler for a choice box, responsible for a selection of a file data's language
     * @param newLangValue
     */
    private void googleDriveDataLanguageChangedEventHandler(Language newLangValue){
        if (newLangValue != null){
            selectedLanguage = newLangValue;
            textField_googleDocsFIleURL.setDisable(false);
            textField_googleDocsFIleURL.textProperty().addListener((observable1, oldValue1, newValue) -> {
                googleFileURLTextFieldTextChangedEventHandler(newValue, newLangValue);
            });


        } else{
            textField_googleDocsFIleURL.setDisable(true);

        }
        radioButton_translationOnlyGoogleDocs.setSelected(false);
        radioButton_wordsOnlyGoogleDocs.setSelected(false);
        radioButton_wordAndTranslationGoogleDocs.setSelected(false);
    }

    /**
     * Binds choice box event handlers to the choiceboxes
     */
    private void initializeChoiceBoxEventHandlers(){
        choiceBox_data_source_type.getSelectionModel().selectedItemProperty().addListener((observableValue, old_value, new_value) -> {
            dataSourceChoiceBoxSelectedItemChangedEventHandler(old_value, new_value);
        });

        //if dropbox file language was changed
        chB_dropbox_data_lang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //TODO: complete
            } else {

            }
        });

        //if file data source language was changed
        chB_file_data_lang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fileLanguageSelectionChangedEventHandler(newValue);
        });

        chB_googleDrive_data_lang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newLangValue) -> {
            googleDriveDataLanguageChangedEventHandler(newLangValue);
        });
    }

    /**
     * Gets a selected file content type
     * @return a content type based on the type of a selected radiobutton
     */
    private ContentType getPlainFileContentType(){
        ContentType dataSourceContentType = null;
        if (radioButtonToggleGroup.getSelectedToggle() != null){

            if (radioButton_wordAndTranslation.isSelected()){
                dataSourceContentType = ContentType.WORDS_TRANSLATION;
            }
            else if (radioButton_wordsOnly.isSelected()){
                dataSourceContentType = ContentType.WORDS_ONLY;
            }
            else if (radioButton_translationOnly.isSelected()){
                dataSourceContentType = ContentType.TRANSLATION_ONLY;
            }
            else{
                dataSourceContentType = ContentType.UNKNOWN;
            }
        }
        else{
            btn_loadLocalFile.setDisable(true);
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_TITLE));
            errorAlert.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_RADIOBUTTON_HEADER));
            errorAlert.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_RADIOBUTTON_TEXT));
            errorAlert.showAndWait();
        }
        return dataSourceContentType;
    }

    /**
     * Searches for all the themes available locally and displays them in listboxes and choiceboxes
     */
    private void findAllThemes() {
        try {
            File themesDirectory = new File("themes" + File.separator + "themes"); //for development mode
            themeDirectories = new ArrayList<>();

            if (!themesDirectory.getAbsoluteFile().exists()) {
                themesDirectory = new File("themes" + File.separator); //production
            }

            ObservableList<String> themesNames = FXCollections.observableArrayList();
            for (File file : themesDirectory.listFiles()) {
                if (file.isDirectory()) {
                    themeDirectories.add(file);
                    themesNames.add(file.getName());
                }
            }

            choiceBox_theme.setItems(themesNames);
            String selectedThemeName = UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME);
            selectedThemeName = selectedThemeName.substring(selectedThemeName.lastIndexOf("/") + 1, selectedThemeName.lastIndexOf(".css"));
            choiceBox_theme.getSelectionModel().select(selectedThemeName);
            choiceBox_theme.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                themeChangedEventHandler(newValue);
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Event Handler for a choicebox, responsible for theme selection
     * @param newValue stands for a newValue, selected from a choicebox.
     */
    private void themeChangedEventHandler(String newValue){
        themeDirectories.stream().filter(directory -> directory.getName().equals(newValue)).forEach(directory -> {

            StringBuffer themesFilePathBuffer = new StringBuffer();
            themesFilePathBuffer.append("themes").append(File.separator).append("themes").append(File.separator).append(newValue).append(File.separator).append(newValue).append(".css");

            selectedThemeFile = new File(themesFilePathBuffer.toString()); //dev mode
            if (!selectedThemeFile.getAbsoluteFile().exists()) {

                themesFilePathBuffer.append("themes").append(File.separator).append(newValue).append(File.separator).append(newValue).append(".css");
                selectedThemeFile = new File(themesFilePathBuffer.toString());//production

            }
            System.out.println(selectedThemeFile.getPath());
            //TODO: change theme in config files after Ok or Apply btn is clicked.

        });
    }


    public void btn_cancel_clicked(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btn_cancel.getScene().getWindow();
        stageManager.closeStage(currentStage);
    }

    public void btn_addLanguage_clicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource(Navigator.ADD_LANGUAGE_VIEW_PATH));
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
            stage.setTitle(bundle.getString(ResourceBundleKeys.TITLE_ADD_NEW_LANG));
            stage.setOnHiding(windowEvent -> System.out.println("A stage on layer " + 2 + " was resetted"));
            final Stage stageDuplicate = stage;
            stageDuplicate.setOnCloseRequest(windowEvent -> StageManager.getInstance().closeStage(stageDuplicate));
            stageManager.putStage(Main.class.getClassLoader().getResource(Navigator.ADD_LANGUAGE_VIEW_PATH), stageDuplicate, 2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads all languages for current user and puts them into listboxes and choiceboxes
     */
    public void Refresh() {
        List<Language> currentUserLanguages = LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId());
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
            LanguageDAO.getInstance().delete(selectedLang);
            System.out.println("Language: " + selectedLang + " was deleted");
        } else {
            AlertThrower.throwAlert(Alert.AlertType.INFORMATION, bundle.getString(ResourceBundleKeys.DIALOGS_INFO_TITLE), bundle.getString(ResourceBundleKeys.DIALOGS_LANG_NOT_SELECTED), bundle.getString(ResourceBundleKeys.DIALOGS_CHOOSE_LANG));
        }
    }

}
