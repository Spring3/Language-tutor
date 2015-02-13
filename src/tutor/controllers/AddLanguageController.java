package tutor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import tutor.util.StageManager;

import java.net.URL;
import java.util.ResourceBundle;

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


    public void addLanguageClicked(ActionEvent actionEvent) {

    }
}
