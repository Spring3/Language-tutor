package tutor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import tutor.Main;
import tutor.dao.UserDAO;
import tutor.models.User;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by user on 11.02.2015.
 */
public class AuthController extends Navigator implements Initializable {

    public AuthController(){
        stageManager = StageManager.getInstance(3);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
    }

    private StageManager stageManager;

    @FXML
    private TextField txtb_reg_username;

    @FXML
    private Button btn_enter;

    @FXML
    private PasswordField txtb_enter_password;

    @FXML
    private PasswordField txtb_reg_pass2;

    @FXML
    private Button btn_register;

    @FXML
    private AnchorPane pane_enter;

    @FXML
    private Button btn_showEnterPane;

    @FXML
    private PasswordField txtb_reg_pass1;

    @FXML
    private TextField txtb_enter_username;

    @FXML
    private Button btn_showRegPane;

    @FXML
    private AnchorPane pane_register;

    @FXML
    private Label validation_label;

    private ResourceBundle bundle;

    private static User activeUser;

    public static User getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(User activeUser) {
        AuthController.activeUser = activeUser;
    }

    public void enterProgramClicked(ActionEvent actionEvent) {
        if (!txtb_enter_username.getText().isEmpty()) {
            if (!txtb_enter_password.getText().isEmpty()) {
                User user = new UserDAO().readByUserName(txtb_enter_username.getText());
                if (user != null) {
                    if (user.getPassword() == txtb_enter_password.getText().hashCode()) {
                        System.out.println("User: " + txtb_enter_username.getText() + ", password: " + txtb_enter_password.getText());
                        setActiveUser(user);
                        stageManager.navigateTo(Main.class.getResource(Navigator.MAIN_VIEW_PATH), "Language tutor", 0, Optional.of(true));
                    }
                    else{
                        validation_label.setText(bundle.getString(ResourceBundleKeys.WRONG_DATA));

                    }
                }
                else{
                    validation_label.setText(bundle.getString(ResourceBundleKeys.WRONG_DATA));
                }

            }
            else{
                validation_label.setText(bundle.getString(ResourceBundleKeys.WRONG_DATA));
            }
        }
        else{
            validation_label.setText(bundle.getString(ResourceBundleKeys.USERNAME_EMPTY));
        }
    }

    public void showEnterPaneClicked(ActionEvent actionEvent) {
        txtb_reg_username.setText("");
        txtb_reg_pass1.setText("");
        txtb_reg_pass2.setText("");
        pane_register.setVisible(false);
        pane_enter.setVisible(true);
    }

    public void showRegPaneClicked(ActionEvent actionEvent) {
        txtb_enter_username.setText("");
        txtb_enter_password.setText("");
        pane_enter.setVisible(false);
        pane_register.setVisible(true);
    }

    public void registerClicked(ActionEvent actionEvent) {
        if (!txtb_reg_username.getText().isEmpty()){
            if (txtb_reg_username.getText().length() >= 5) {
                if (!txtb_reg_pass1.getText().isEmpty() && !txtb_reg_pass2.getText().isEmpty()) {
                    if (txtb_reg_pass1.getText().length() >= 5 || txtb_reg_pass2.getText().length() >= 5) {
                        if (txtb_reg_pass1.getText().equals(txtb_reg_pass2.getText())) {
                            User user = new UserDAO().readByUserName(txtb_reg_username.getText());
                            if (user == null) {
                                user = new User(txtb_reg_username.getText(), txtb_reg_pass1.getText().hashCode());
                                if (new UserDAO().create(user)) {
                                    System.out.println("Registering user: " + txtb_reg_username.getText() + ", password: " + txtb_reg_pass1.getText() + ", repeated: " + txtb_reg_pass2.getText());
                                    user = new UserDAO().readByUserName(user.getUserName());
                                    setActiveUser(user);
                                    stageManager.navigateTo(Main.class.getResource(Navigator.MAIN_VIEW_PATH), "Language Tutor", 0, Optional.of(true));
                                } else {
                                    validation_label.setText(bundle.getString(ResourceBundleKeys.WRONG_DATA));
                                }
                            } else {
                                validation_label.setText(bundle.getString(ResourceBundleKeys.USER_ALREADY_EXISTS));
                            }
                        } else {
                            validation_label.setText(bundle.getString(ResourceBundleKeys.PASSWORD_DOES_NOT_MATCH));
                        }
                    }
                    else{
                        validation_label.setText(bundle.getString(ResourceBundleKeys.SHORT_PASS));
                    }
                } else {
                    validation_label.setText(bundle.getString(ResourceBundleKeys.WRONG_DATA));
                }
            }
            else{
                validation_label.setText(bundle.getString(ResourceBundleKeys.SHORT_NAME));
            }
        }
        else{
            validation_label.setText(bundle.getString(ResourceBundleKeys.USERNAME_EMPTY));
        }
    }

    public void navigateTo(URL fxmlViewURL, String title, int layerIndex, boolean isResizable){
        stageManager.navigateTo(fxmlViewURL,title,layerIndex, Optional.of(isResizable));
    }
}
