package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 14.12.2014.
 */
public class SettingsController extends Navigator implements Initializable {
    @FXML
    ListView listView;

    @FXML
    Pane pane_lang;

    @FXML
    Pane pane_data_source;

    @FXML
    Pane pane_theme;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pane[] panes = {pane_data_source, pane_lang, pane_theme};
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
    }
}
