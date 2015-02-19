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
import tutor.dao.DataUnitDAO;
import tutor.dao.LanguageDAO;
import tutor.models.DataSource;
import tutor.models.DataUnit;
import tutor.models.Language;
import tutor.util.StageManager;
import tutor.Main;
import tutor.util.UserConfigHelper;
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
    private static final String DIALOGS_INFO_TITLE = "dialogs_info_title";
    private static final String DIALOGS_LANG_NOT_SELECTED = "dialogs_info_header_lang_not_selected";
    private static final String DIALOGS_CHOOSE_LANG = "dialogs_info_choose_lang";
    private static final String TITLE_ADD_NEW_LANG = "title_add_new_lang";
    private static final String FILE_CHOOSER_TITLE = "title_f_chooser";
    private static final String DIALOGS_RADIOBUTTON_HEADER = "dialogs_info_header_radiobutton";
    private static final String DIALOGS_RADIOBUTTON_TEXT = "dialogs_info_choose_file_structure";
    private static final String DIALOGS_ERROR_TITLE = "dialogs_error_title";
    private static final String DIALOGS_ERROR_DATASOURCE_ALREADY_ADDED = "dialogs_error_header_datasource_already_added";
    private static final String DIALOGS_ERROR_DATASOURCE_MESSAGE = "dialogs_error_datasource_message";
    private volatile File selectedFile;
    private List<File> themeDirectories;
    private File selectedThemeFile;
    private final ObservableList<DataSource> allDataSources = FXCollections.observableArrayList();
    private Language selectedLanguage;

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
        ToggleGroup radioButtonToggleGroup = new ToggleGroup();
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
                        fileChooser.setTitle(bundle.getString(FILE_CHOOSER_TITLE));
                        selectedFile = fileChooser.showOpenDialog(null);
                        if (selectedFile != null) {
                            //Writing a selected file's path to the textfield
                            textField_localFilePath.setText(selectedFile.getAbsolutePath());
                            //if radiobutton was toggled
                            if (radioButtonToggleGroup.getSelectedToggle() != null) {
                                btn_loadLocalFile.setDisable(false); //activate load btn to parse the selected file
                                btn_loadLocalFile.setOnAction(event1 -> {
                                    //TODO:Handle
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
        try {
            final DataSource finalDataSource;
            //Opening selected file
            BufferedReader fileReader = new BufferedReader(new FileReader(selectedFile));
            //Creating a datasource of type FILE for service OS
            DataSource currentDataSource = new DataSource(textField_localFilePath.getText(), DataSource.LOCAL_FILE, DataSource.SERVICE_OS, selectedLanguage);
            List<DataSource> allDataSourcesForSelectedLanguage = new DataSourceDAO().readAllByLanguage(selectedLanguage);
            //Checking whether there is already such data source
            boolean isDuplicate = allDataSourcesForSelectedLanguage.stream().anyMatch((a) -> a.getLink().equals(currentDataSource.getLink()) && a.getLanguage().equals(currentDataSource.getLanguage()));

            //if not
            if (!isDuplicate) {
                //saving our new datasource to the database
                DataSource tempDataSource = null;
                new DataSourceDAO().create(currentDataSource);
                //replacing existing currentDataSource with the one from database to get id
                allDataSourcesForSelectedLanguage = new DataSourceDAO().readAllByLanguage(selectedLanguage);
                for (DataSource source : allDataSourcesForSelectedLanguage){
                    if (source.getLink().equals(currentDataSource.getLink())){
                        tempDataSource = source;
                        break;
                    }
                }
                finalDataSource = tempDataSource;
            } else {
                //finding our original dataSource from the database
                try {
                    finalDataSource = allDataSourcesForSelectedLanguage.stream().filter((src) -> src.equals(currentDataSource)).findFirst().get();
                }
                catch (NoSuchElementException ex){
                    System.err.println("Datasource: "  + currentDataSource.getLink() + " had already been added.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(bundle.getString(DIALOGS_ERROR_TITLE));
                    alert.setHeaderText(bundle.getString(DIALOGS_ERROR_DATASOURCE_ALREADY_ADDED));
                    alert.setContentText(bundle.getString(DIALOGS_ERROR_DATASOURCE_MESSAGE));
                    alert.showAndWait();
                    return;
                }
            }

            if (radioButton_wordsOnly.isSelected()) {
                fileReader.lines().forEach(line -> new DataUnitDAO()
                    .create(
                            new DataUnit(
                                    line.trim(),
                                    "",
                                    selectedLanguage,
                                    finalDataSource)
                    ));

            } else if (radioButton_translationOnly.isSelected()) {
                fileReader.lines().forEach(line -> new DataUnitDAO()
                    .create(
                            new DataUnit(
                                    "",
                                    line.trim(),
                                    selectedLanguage,
                                    finalDataSource)
                    ));

            } else if (radioButton_wordAndTranslation.isSelected()) {
                //If user clicked "Words - translation" radiobutton, we are separating our word from the translation
                fileReader.lines().forEach((s) -> new DataUnitDAO()
                        .create(
                                new DataUnit(
                                        s.substring(0, s.indexOf('=')).trim(),
                                        s.substring(s.indexOf('=') + 1, s.length()).trim(),
                                        selectedLanguage,
                                        finalDataSource)
                        ));
                //TODO: Check the whole file first. In case the data format is wrong, show an appropriate message
            } else {
                //if no radiobuttons were clicked
                btn_loadLocalFile.setDisable(true);
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setTitle(bundle.getString(DIALOGS_INFO_TITLE));
                errorAlert.setHeaderText(bundle.getString(DIALOGS_RADIOBUTTON_HEADER));
                errorAlert.setContentText(bundle.getString(DIALOGS_RADIOBUTTON_TEXT));
                errorAlert.showAndWait();
            }
            //Displaying current datasource in tableView
            allDataSources.add(finalDataSource);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
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
                            //TODO: change theme in config files after Ok or Apply btn clicked.
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
            stage.setTitle(bundle.getString(TITLE_ADD_NEW_LANG));
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
            errorMessage.setTitle(bundle.getString(DIALOGS_INFO_TITLE));
            errorMessage.setHeaderText(bundle.getString(DIALOGS_LANG_NOT_SELECTED));
            errorMessage.setContentText(bundle.getString(DIALOGS_CHOOSE_LANG));
            errorMessage.showAndWait();
        }
    }
}
