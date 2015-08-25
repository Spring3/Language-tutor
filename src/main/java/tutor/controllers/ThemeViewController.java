package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import tutor.Main;
import tutor.util.UserConfigHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by user on 13.04.2015.
 */
public class ThemeViewController implements Initializable{

    @FXML
    private ChoiceBox<String> choiceBox_theme;

    private URL selectedThemePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> themesNames = FXCollections.observableArrayList(new ArrayList<String>(){{
            add("frost");
            add("flat");
        }});

        choiceBox_theme.setItems(themesNames);
        String selectedThemeName = UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME);
        selectedThemeName = selectedThemeName.substring(selectedThemeName.lastIndexOf("/") + 1, selectedThemeName.lastIndexOf(".css"));
        choiceBox_theme.getSelectionModel().select(selectedThemeName);
        choiceBox_theme.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            themeChangedEventHandler(newValue);
        });
    }

    private void themeChangedEventHandler(String newValue){
        selectedThemePath = Main.class.getClassLoader().getResource(newValue.toLowerCase() + ".css");
    }
}
