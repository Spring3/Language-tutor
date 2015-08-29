package tutor.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sun.plugin.javascript.navig.Anchor;
import tutor.Main;
import tutor.dao.LanguageDAO;
import tutor.models.Language;
import tutor.util.DbManager;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
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
    private MenuItem mItem_theme;
    @FXML
    private MenuItem mItem_dictionary;
    @FXML
    private ImageView newsItemImage;
    @FXML
    private ChoiceBox<Language> choiceBox_lang_to_learn;
    @FXML
    private MenuItem mItem_newTask;
    @FXML
    private MenuItem mItem_about;
    @FXML
    private MenuItem mItem_statistics;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane taskPane;
    @FXML
    private Tab workbench;
    @FXML
    private ImageView task_dictation_img;
    @FXML
    private Label task_dictation_header;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        bundle = rb;
        ObservableList<Language> currentUserLanguages = FXCollections.observableArrayList(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));
        choiceBox_lang_to_learn.setItems(currentUserLanguages);
        try {
            choiceBox_lang_to_learn.setValue(choiceBox_lang_to_learn.getItems().get(0));
        }
        catch (IndexOutOfBoundsException ex){}
        scrollPane.setFitToWidth(true);
        task_dictation_img.setImage(new Image(Main.class.getClassLoader().getResource("tasks/dictation/dictation-image.jpg").toExternalForm(), true));

    }

    @FXML
    public void Shutdown(ActionEvent actionEvent) {
        stageManager.Shutdown();
        DbManager.getInstance().shutdown();
    }

    public void newsItemClicked(Event event) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.NEWS_ITEM_VIEW_PATH), newsItemLabel.getText(), 1, Optional.empty());
    }

    public void statisticsClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.USER_RATE_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_STATISTICS), 1, Optional.empty());
    }

    public void menuItem_about_clicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.ABOUT_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_ABOUT), 1, Optional.empty());
    }

    public void menuItem_dictionary_clicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.DICTIONARY_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_DICTIONARY), 1, Optional.empty());
    }

    public void editLangClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.LANGUAGE_SETTINGS_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_LANGUAGE_SETTINGS), 1, Optional.empty());
    }

    public void importFileClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.FILE_IMPORT_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_IMPORT), 1, Optional.empty());
    }

    public void localeClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.LOCALE_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_LOCALE), 1, Optional.empty());
    }

    public void themeClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.THEME_SETTINGS_VIEW_PATH), bundle.getString(ResourceBundleKeys.LABEL_THEME), 1, Optional.empty());
    }

    public void LogOut(ActionEvent actionEvent) {
        AuthController.setActiveUser(null);
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.AUTHENTICATION_VIEW_PATH), "Language Tutor", 0, Optional.empty());
    }

    public void dictationTaskClicked(Event event) {
        stageManager.navigateTo(Main.class.getClassLoader().getResource(Navigator.TASKVIEW_DICTATION_PATH), bundle.getString(ResourceBundleKeys.LABEL_DICTATION), 1, Optional.empty());
    }
}
