package tutor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by spring on 5/3/16.
 */
public class AboutViewController implements Initializable {


    @FXML
    public Hyperlink profileLink;

    public void linkClicked(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI(profileLink.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
