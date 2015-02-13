package tutor.controllers;

import com.sun.istack.internal.NotNull;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import tutor.Main;
import tutor.util.StageManager;

import javax.swing.text.html.Option;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main controller for the main.fxml view.
 */
public class Controller extends Navigator implements Initializable
{
    public Controller(){
        stageManager = StageManager.getInstance(3);
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
    @FXML
    private Button btn_send_feedback;
    @FXML
    private MenuItem mItem_Settings;
    @FXML
    private ImageView social_fb;
    @FXML
    private ImageView social_gplus;
    @FXML
    private MenuItem mItem_dictionary;
    @FXML
    private Label history_header;
    @FXML
    private ImageView social_twitter;
    @FXML
    private Button btn_donate;
    @FXML
    private ImageView social_vk;
    @FXML
    private AnchorPane newsItem;
    @FXML
    private ImageView history_img;
    @FXML
    private ImageView newsItemImage;
    @FXML
    private ChoiceBox<String> choiceBox_lang_to_learn;
    @FXML
    private ImageView offlineTask_img;
    @FXML
    private Label offlineTask_header;
    @FXML
    private MenuItem mItem_newTask;
    @FXML
    private MenuItem mItem_about;
    @FXML
    private MenuItem mItem_statistics;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    /**
     * Event handler for "Invite friend" menu item.
     * Opens a new stage.
     * @param actionEvent
     */
    @FXML
    public void InvitationMenuItemClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getResource(INVITATION_VIEW_PATH), "Invite a friend voa email", 1, Optional.empty());
    }
    @FXML
    public void EditSettingsClick(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getResource(EDIT_SETTINGS_PATH), "Settings", 1, Optional.empty());
    }
    @FXML
    public void Shutdown(ActionEvent actionEvent) {
        stageManager.Shutdown();
    }

    public void newsItemClicked(Event event) {
        stageManager.navigateTo(Main.class.getResource(NEWS_ITEM_VIEW_PATH), newsItemLabel.getText(), 1, Optional.empty());
    }

    public void offlineTaskClicked(Event event) {
        stageManager.navigateTo(Main.class.getResource(OFFLINE_TASK_VIEW_PATH), offlineTask_header.getText(), 1, Optional.empty());
    }

    public void onlineTaskClicked(Event event) {
        stageManager.navigateTo(Main.class.getResource(ONLINE_TASK_VIEW_PATH), onlineTask_header.getText(), 1, Optional.empty());
    }

    public void historyItemClicked(Event event) {
        stageManager.navigateTo(Main.class.getResource(HISTORY_ITEM_VIEW_PATH), history_header.getText(), 1, Optional.empty());
    }

    public void sendFeedbackClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getResource(SEND_FEEDBACK_VIEW_PATH), "Feedback", 1, Optional.empty());
    }

    public void statisticsClicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getResource(USER_RATE_VIEW_PATH), "Statistics", 1, Optional.empty());
    }

    public void menuItem_about_clicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getResource(ABOUT_VIEW_PATH), "About", 1, Optional.empty());
    }

    public void menuItem_dictionary_clicked(ActionEvent actionEvent) {
        stageManager.navigateTo(Main.class.getResource(DICTIONARY_VIEW_PATH), "Dictionary", 1, Optional.empty());
    }
}
