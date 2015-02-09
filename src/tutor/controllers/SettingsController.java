package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 14.12.2014.
 */
public class SettingsController extends Navigator implements Initializable {

    //UI components, injected from the settings.fxml
    @FXML
    private ChoiceBox<String> choiceBox_localizeTo;
    @FXML
    private Button btn_reportLocaleMistake;
    @FXML
    private RadioButton radioButton_wordAndTranslation;
    @FXML
    private TextField textField_dropboxFileURL;
    @FXML
    private Button btn_connectToLingualeo;
    @FXML
    private RadioButton radioButton_wordsOnlyGoogleDocs;
    @FXML
    private RadioButton radioButton_wordsOnlyDropbox;
    @FXML
    private Button btn_ok;
    @FXML
    private RadioButton radioButton_wordAndTranslationDropbox;
    @FXML
    private TextField textField_localDBPath;
    @FXML
    private Button btn_loadLocalDB;
    @FXML
    private RadioButton radioButton_wordAndTranslationGoogleDocs;
    @FXML
    private StackPane pane_lang;
    @FXML
    private ChoiceBox<String> choiceBox_activeLocale;
    @FXML
    private TextField textField_lingualeoProfileURL;
    @FXML
    private ListView<String> listView;
    @FXML
    private ChoiceBox<String> choiceBox_uncorrectLocale;
    @FXML
    private Button btn_apply;
    @FXML
    private RadioButton radioButton_wordsOnly;
    @FXML
    private RadioButton radioButton_localizeManually;
    @FXML
    private Button btn_downloadLocale;
    @FXML
    private TextField textField_backupLocalDBPath;
    @FXML
    private StackPane pane_data_source;
    @FXML
    private StackPane pane_other;
    @FXML
    private Button btn_createLocalDB;
    @FXML
    private Button btn_localize;
    @FXML
    private CheckBox checkBox_autobackup;
    @FXML
    private Button btn_loadLocalFile;
    @FXML
    private TextField textField_localFilePath;
    @FXML
    private RadioButton radioButton_translationOnly;
    @FXML
    private RadioButton radioButton_translationOnlyGoogleDocs;
    @FXML
    private RadioButton radioButton_translationOnlyDropbox;
    @FXML
    private RadioButton radioButton_localizeAutomatically;
    @FXML
    private TextField textField_googleDocsFIleURL;
    @FXML
    private Label label_errorMessage;
    @FXML
    private Button btn_chooseBackupLocalDBPath;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_connectoToGoogleDocs;
    @FXML
    private Button btn_connectToDropBox;
    @FXML
    private Button btn_correctLocaleMistake;

    //initialization method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initializing items for listView
        StackPane[] panes = {pane_data_source, pane_lang, pane_other};
        ObservableList<String> items = FXCollections.observableArrayList("Data Source", "Language", "Others");
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


    }
}
