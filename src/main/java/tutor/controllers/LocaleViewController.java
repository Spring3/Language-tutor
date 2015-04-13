package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import tutor.util.UserConfigHelper;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 13.04.2015.
 */
public class LocaleViewController implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBox_activeLocale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> allLocales = FXCollections.observableArrayList();
        allLocales.add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE));
        choiceBox_activeLocale.setItems(allLocales);
        choiceBox_activeLocale.getSelectionModel().select(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE));
    }
}
