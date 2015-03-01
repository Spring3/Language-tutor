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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tutor.dao.DataSourceDAO;
import tutor.dao.LanguageDAO;
import tutor.models.DataSource;
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
    private TableView<DataSource> tableview_datasources;
    @FXML
    private Button btn_correctLocaleMistake;
    @FXML
    private ChoiceBox<String> choiceBox_data_source_type;
    @FXML
    private TableColumn<DataSource, Language> tableView_column_Language;
    @FXML
    private TableColumn<DataSource, String> tableView_column_source;

    private StageManager stageManager;
    private ResourceBundle bundle;
    private volatile File selectedFile;
    private List<File> themeDirectories;
    private File selectedThemeFile;
    private final ObservableList<DataSource> allDataSources = FXCollections.observableArrayList();
    private Language selectedLanguage;
    private ToggleGroup radioButtonToggleGroup;
    private ToggleGroup radioButtonGoogleToggleGroup;
    private ToggleGroup radioButtonDropboxToggleGroup;
    private ToggleGroup languagePanelRadioButtonToggleGroup;
    private DataSourceManager dataSourceManager;

    //initialization method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //StageManager for operating with stages
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
        //listView_languages
        Refresh();

        //Showing all the themes, avaliable.
        //TODO: Add server connection handling
        findAllThemes();

        //Initializing activeLocale choicebox
        ObservableList<String> allLocales = FXCollections.observableArrayList();
        allLocales.add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE));
        choiceBox_activeLocale.setItems(allLocales);
        choiceBox_activeLocale.getSelectionModel().select(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE));

        //Creating toggleGroup for radiobuttons
        radioButtonToggleGroup = new ToggleGroup();
        radioButton_translationOnly.setToggleGroup(radioButtonToggleGroup);
        radioButton_wordAndTranslation.setToggleGroup(radioButtonToggleGroup);
        radioButton_wordsOnly.setToggleGroup(radioButtonToggleGroup);

        radioButtonToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue != null) {
                    if (textField_localFilePath.getText() != null) {
                        if (!textField_localFilePath.getText().isEmpty()) {
                            btn_loadLocalFile.setDisable(false);
                            btn_loadLocalFile.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    Thread thread = new Thread(){
                                        @Override
                                        public void run(){
                                            parseSelectedFile();
                                        }
                                    };
                                    thread.setDaemon(true);
                                    thread.run();
                                }
                            });
                        }
                    }
                }
            }
        });
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

        //initializing choiceboxes
        ObservableList<String> dataSource_types = FXCollections.observableArrayList(
                //resourceBundle.getString("source_type_local_db"),
                resourceBundle.getString("source_type_file"),
                resourceBundle.getString("source_type_google_disk_file"),
                resourceBundle.getString("source_type_dropbox_file"),
                resourceBundle.getString("source_type_lingualeo")
        );

        choiceBox_data_source_type.setItems(dataSource_types);
        //On data source choicebox item changed
        choiceBox_data_source_type.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old_value, String new_value) {
                //showing appropriate UI components
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

                if (new_value != null && !new_value.isEmpty()) {
                    /*if (new_value.equals(resourceBundle.getString("source_type_local_db"))){
                        stackpanel_localDB.setVisible(true);
                    }else*/
                    if (new_value.equals(resourceBundle.getString("source_type_file"))) {
                        stackpanel_file.setVisible(true);
                    } else if (new_value.equals(resourceBundle.getString("source_type_google_disk_file"))) {
                        stackpanel_googleDrive.setVisible(true);
                    } else if (new_value.equals(resourceBundle.getString("source_type_dropbox_file"))) {
                        stackpanel_dropbox.setVisible(true);
                    } else if (new_value.equals(resourceBundle.getString("source_type_lingualeo"))) {
                        stackpanel_lingualeo.setVisible(true);
                    }
                }
            }
        });

        //if dropbox file language was changed
        chB_dropbox_data_lang.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Language>() {
            @Override
            public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newValue) {
                if (newValue != null) {
                    //TODO: complete
                } else {

                }
            }
        });

        //if file data source language was changed
        chB_file_data_lang.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Language>() {
            @Override
            public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newValue) {
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
                                    Thread thread = new Thread(){
                                        @Override
                                        public void run(){
                                            parseSelectedFile();
                                        }
                                    };
                                    thread.setDaemon(true);
                                    thread.run();

                                });
                            }

                            textField_localFilePath.setDisable(false);
                            textField_localFilePath.textProperty().addListener(new ChangeListener<String>() {
                                                                                   @Override
                                                                                   public void changed(ObservableValue<? extends String> observable, String oldValue, String
                                                                                           newValue) {
                                                                                       if (newValue.isEmpty()) {
                                                                                           btn_loadLocalFile.setDisable(true);
                                                                                       }
                                                                                   }
                                                                               }
                            );
                        }
                        else {
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
        });


        chB_googleDrive_data_lang.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Language>(){

            @Override
            public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newLangValue) {
                if (newLangValue != null){
                    selectedLanguage = newLangValue;
                    textField_googleDocsFIleURL.setDisable(false);
                    textField_googleDocsFIleURL.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (newValue != null){
                                if (!newValue.isEmpty()){
                                    GDriveManager gDriveManager = GDriveManager.getInstance();
                                    if (gDriveManager.gotCode()) {
                                        btn_openGoogleDocFile.setDisable(false);
                                        btn_openGoogleDocFile.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent event) {
                                                if (!textField_googleDocsFIleURL.getText().isEmpty() && gDriveManager.gotCode()){
                                                    InputStream fileInputStream = gDriveManager.getFileInputStream(textField_googleDocsFIleURL.getText(), newLangValue);
                                                    DataSource fileDataSource = new DataSourceDAO().readAllByOwner(AuthController.getActiveUser()).stream().filter((src) -> src.getLink().equals(textField_googleDocsFIleURL.getText()) && src.getLanguage().equals(newLangValue)).findFirst().get();
                                                    DataSourceManager.ContentType selectedContentType = null;
                                                    if (radioButton_wordAndTranslationGoogleDocs.isSelected()) {
                                                        selectedContentType = DataSourceManager.ContentType.WORDS_TRANSLATION;
                                                    }
                                                    else if (radioButton_wordsOnlyGoogleDocs.isSelected()){
                                                        selectedContentType = DataSourceManager.ContentType.WORDS_ONLY;
                                                    }
                                                    else{
                                                        selectedContentType = DataSourceManager.ContentType.TRANSLATION_ONLY;
                                                    }
                                                    if(dataSourceManager == null){
                                                        dataSourceManager = DataSourceManager.getInstance(bundle);
                                                    }
                                                    dataSourceManager.parse(fileInputStream, selectedContentType, fileDataSource);
                                                }
                                            }
                                        });

                                    }
                                    else{
                                        btn_openGoogleDocFile.setDisable(true);
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_TITLE));
                                        alert.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_NO_RIGHTS_HEADER));
                                        alert.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_NO_RIGHTS_CONTENT));
                                        alert.showAndWait();

                                    }
                                }
                                else{
                                    btn_openGoogleDocFile.setDisable(true);
                                }
                            }
                        }
                    });


                } else{
                    textField_googleDocsFIleURL.setDisable(true);

                }
                radioButton_translationOnlyGoogleDocs.setSelected(false);
                radioButton_wordsOnlyGoogleDocs.setSelected(false);
                radioButton_wordAndTranslationGoogleDocs.setSelected(false);
            }
        });
        refreshTableView();
    }

    private void refreshTableView() {
        //initializing tableView
        tableView_column_Language.setCellValueFactory(new PropertyValueFactory<>("language"));
        tableView_column_source.setCellValueFactory(new PropertyValueFactory<>("link"));
        allDataSources.addAll(new DataSourceDAO().readAllByOwner(AuthController.getActiveUser()));
        tableview_datasources.setItems(allDataSources);
    }

    private void parseSelectedFile(){
        DataSource currentDataSource = null;
        if (radioButtonToggleGroup.getSelectedToggle() != null){
            dataSourceManager = DataSourceManager.getInstance(bundle);
            currentDataSource = new DataSource(textField_localFilePath.getText(), DataSource.LOCAL_FILE, DataSource.SERVICE_OS, selectedLanguage);
            DataSourceManager.ContentType dataSourceContentType = null;
            if (radioButton_wordAndTranslation.isSelected()){
                dataSourceContentType = DataSourceManager.ContentType.WORDS_TRANSLATION;
            }
            else if (radioButton_wordsOnly.isSelected()){
                dataSourceContentType = DataSourceManager.ContentType.WORDS_ONLY;
            }
            else if (radioButton_translationOnly.isSelected()){
                dataSourceContentType = DataSourceManager.ContentType.TRANSLATION_ONLY;
            }
            dataSourceManager.parse(selectedFile, dataSourceContentType, currentDataSource);
        }
        else {
            //if no radiobuttons were clicked
            btn_loadLocalFile.setDisable(true);
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_TITLE));
            errorAlert.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_RADIOBUTTON_HEADER));
            errorAlert.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_RADIOBUTTON_TEXT));
            errorAlert.showAndWait();
        }
        //Displaying current datasource in tableView
        allDataSources.add(currentDataSource);
    }

    private void findAllThemes() {
        try {
            File themesDirectory = null;
            //try{
            themesDirectory = new File("themes" + File.separator + "themes"); //dev
                if(!themesDirectory.getAbsoluteFile().exists()){
                    themesDirectory = new File("themes" + File.separator); //production
                }
            /*}
            catch (Exception ex){

            } */
            themeDirectories = new ArrayList<>();
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
            choiceBox_theme.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    for (File directory : themeDirectories) {
                        if (directory.getName().equals(newValue)) {
                            selectedThemeFile = new File("themes" + File.separator + "themes" + File.separator + newValue + File.separator + newValue + ".css"); //dev mode
                            if (!selectedThemeFile.getAbsoluteFile().exists()){
                                selectedThemeFile = new File("themes" + File.separator + newValue + File.separator + newValue + ".css");//production

                            }
                            System.out.println(selectedThemeFile.getPath());
                            //TODO: change theme in config files after Ok or Apply btn is clicked.
                        }
                    }
                }
            });
        }
        catch (Exception ex){
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("log.txt"));
                PrintWriter writer = new PrintWriter(bufferedWriter);
                ex.printStackTrace(writer);
                writer.flush();
                writer.close();
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            catch (IOException exc){

            }
        }
    }

    public void btn_cancel_clicked(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btn_cancel.getScene().getWindow();
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
            stage.setTitle(bundle.getString(ResourceBundleKeys.TITLE_ADD_NEW_LANG));
            stage.setOnHiding(windowEvent -> System.out.println("A stage on layer " + 2 + " was resetted"));
            final Stage stageDuplicate = stage;
            stageDuplicate.setOnCloseRequest(windowEvent -> StageManager.getInstance(3).closeStage(stageDuplicate));
            stageManager.putStage(Main.class.getResource(Navigator.ADD_LANGUAGE_VIEW_PATH), stageDuplicate, 2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Refresh() {
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
        } else {
            Alert errorMessage = new Alert(Alert.AlertType.INFORMATION);
            errorMessage.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_INFO_TITLE));
            errorMessage.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_LANG_NOT_SELECTED));
            errorMessage.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_CHOOSE_LANG));
            errorMessage.showAndWait();
        }
    }

    public void connectToGDrive(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getResource(Navigator.WEBVIEW_VIEW_PATH), "Browser", 2, Optional.of(true));

    }
}
