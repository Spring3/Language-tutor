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

    private final static String USERNAME_EMPTY = "error_username_empty";
    private final static String PASSWORD_DOES_NOT_MATCH = "error_passw_does_not_match";
    private final static String WRONG_DATA = "error_wrong_data";
    private final static String USER_ALREADY_EXISTS = "error_user_exists";

    public void enterProgramClicked(ActionEvent actionEvent) {
        if (!txtb_enter_username.getText().isEmpty()) {
            if (!txtb_enter_password.getText().isEmpty()) {
                User user = new UserDAO().readByUserName(txtb_enter_username.getText());
                if (user != null) {
                    if (user.getPassword() == txtb_enter_password.getText().hashCode()) {
                        System.out.println("User: " + txtb_enter_username.getText() + ", password: " + txtb_enter_password.getText());
                        stageManager.navigateTo(Main.class.getResource(Navigator.MAIN_VIEW_PATH), "Language tutor", 0, Optional.of(true));
                    }
                    else{
                        validation_label.setText(bundle.getString(WRONG_DATA));

                    }
                }
                else{
                    validation_label.setText(bundle.getString(WRONG_DATA));
                }

            }
            else{
                validation_label.setText(bundle.getString(WRONG_DATA));
            }
        }
        else{
            validation_label.setText(bundle.getString(USERNAME_EMPTY));
        }
    }

    public void showEnterPaneClicked(ActionEvent actionEvent) {
        pane_register.setVisible(false);
        pane_enter.setVisible(true);
    }

    public void showRegPaneClicked(ActionEvent actionEvent) {
        pane_enter.setVisible(false);
        pane_register.setVisible(true);
    }

    public void registerClicked(ActionEvent actionEvent) {
        if (!txtb_reg_username.getText().isEmpty()){
            if (!txtb_reg_pass1.getText().isEmpty() && !txtb_reg_pass2.getText().isEmpty()) {
                if (txtb_reg_pass1.getText().equals(txtb_reg_pass2.getText())) {
                    User user = new UserDAO().readByUserName(txtb_reg_username.getText());
                    if (user == null) {
                        user = new User(txtb_reg_username.getText(), txtb_reg_pass1.getText().hashCode());
                        if (new UserDAO().create(user)) {
                            System.out.println("Registering user: " + txtb_reg_username.getText() + ", password: " + txtb_reg_pass1.getText() + ", repeated: " + txtb_reg_pass2.getText());
                            stageManager.navigateTo(Main.class.getResource(Navigator.MAIN_VIEW_PATH), "Language Tutor", 0, Optional.of(true));
                        }
                        else{
                            validation_label.setText(bundle.getString(WRONG_DATA));
                        }
                    }
                    else{
                        validation_label.setText(bundle.getString(USER_ALREADY_EXISTS));
                    }
                }
                else
                {
                    validation_label.setText(bundle.getString(PASSWORD_DOES_NOT_MATCH));
                }
            }
            else{
                validation_label.setText(bundle.getString(WRONG_DATA));
            }
        }
        else{
            validation_label.setText(bundle.getString(USERNAME_EMPTY));
        }
    }

    public void navigateTo(URL fxmlViewURL, String title, int layerIndex, boolean isResizable){
        stageManager.navigateTo(fxmlViewURL,title,layerIndex, Optional.of(isResizable));
    }
}
