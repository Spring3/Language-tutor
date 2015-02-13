package tutor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tutor.dao.LanguageDAO;
import tutor.models.Language;
import tutor.util.StageManager;
import java.net.URL;
import java.util.ResourceBundle;
import tutor.Main;

/**
 * Created by user on 13.02.2015.
 */
public class AddLanguageController extends Navigator implements Initializable {
    public AddLanguageController(){
        stageManager = StageManager.getInstance(3);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    StageManager stageManager;

    @FXML
    TextField textField_language;


    public void addLanguageClicked(ActionEvent actionEvent) {
        Language lang = new Language(textField_language.getText(), AuthController.getActiveUser());
        Language tempLang = new LanguageDAO().readBy(lang.getLang_name(), lang.getOwner().getId());
        if (tempLang != null){
            if (tempLang.equals(lang)){
                //TODO: Validate error
                return;
            }
        }
        else {
            new LanguageDAO().create(lang);
            System.out.println("Language: "+ lang.getLang_name() + " for user: " + lang.getOwner().getUserName() + " was created.");
            Stage currentStage = (Stage)textField_language.getScene().getWindow();
            stageManager.closeStage(currentStage);
        }



    }
}
