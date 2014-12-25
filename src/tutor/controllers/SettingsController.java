package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 14.12.2014.
 */
public class SettingsController extends Navigator implements Initializable {

    //UI components, injected from the settings.fxml
    @FXML
    private TextField textField_backupLocalDBPath;
    @FXML
    private RadioButton radioButton_wordAndTranslation;
    @FXML
    private StackPane pane_data_source;
    @FXML
    private StackPane pane_theme;
    @FXML
    private TextField textField_dropboxFileURL;
    @FXML
    private Button btn_connectToLingualeo;
    @FXML
    private Button btn_createLocalDB;
    @FXML
    private CheckBox checkBox_autobackup;
    @FXML
    private Button btn_loadLocalFile;
    @FXML
    private TextField textField_localFilePath;
    @FXML
    private RadioButton radioButton_translationOnly;
    @FXML
    private TextField textField_localDBPath;
    @FXML
    private TextField textField_googleDocsFIleURL;
    @FXML
    private Button btn_loadLocalDB;
    @FXML
    private Button btn_chooseBackupLocalDBPath;
    @FXML
    private StackPane pane_lang;
    @FXML
    private TextField textField_lingualeoProfileURL;
    @FXML
    private Button btn_connectoToGoogleDocs;
    @FXML
    private Button btn_connectToDropBox;
    @FXML
    private ListView<String> listView;
    @FXML
    private RadioButton radioButton_wordsOnly;


    //initialization method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initializing items for listView
        StackPane[] panes = {pane_data_source, pane_lang, pane_theme};
        ObservableList<String> items = FXCollections.observableArrayList("Data Source", "Language", "Theme");
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
    }
}
