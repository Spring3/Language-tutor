package tutor.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tutor.dao.LanguageDAO;
import tutor.models.Language;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by user on 13.04.2015.
 */
public class LanguageSettingsController implements Initializable {

    @FXML
    private ListView<Language> listView_allLanguages;
    @FXML
    private Button btn_apply;
    @FXML
    private TextField tf_search;

    private ResourceBundle bundle;
    private Set<Language> selectedLanguages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        selectedLanguages = new LinkedHashSet<>();
        listView_allLanguages.getItems().addAll(FXCollections.observableList(LanguageDAO.getInstance().readAllLanguages()));
        listView_allLanguages.getItems().removeAll(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));
        tf_search.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue.isEmpty()){
                ObservableList<Language> allLanguageslist = FXCollections.observableList(listView_allLanguages.getItems());
                listView_allLanguages.setItems(allLanguageslist);
            }
            else
            {
                ObservableList<Language> filteredLanguages = FXCollections.observableArrayList(listView_allLanguages.getItems().parallelStream()
                        .filter((lang) -> lang.getLangName().toLowerCase().startsWith(newValue.toLowerCase()))
                        .collect(Collectors.toList()));
                listView_allLanguages.setItems(filteredLanguages);
            }
        });

        Platform.runLater(() -> {
            tf_search.requestFocus();
        });

        listView_allLanguages.setOnMouseClicked(value ->{
            selectedLanguages.add(listView_allLanguages.getSelectionModel().getSelectedItem());
        });
    }

    private void saveLanguages(){
        for(Language lang : selectedLanguages)
            LanguageDAO.getInstance().assignLangToCurrentUser(lang, AuthController.getActiveUser());
    }


    public void btnApplyClicked(ActionEvent actionEvent) {
        saveLanguages();
        StageManager stageManager = StageManager.getInstance();
        Controller controller = ((Controller)stageManager.getControllerForViewOnLayer(0));
        controller.updateLanguageList();
        stageManager.closeStage(stageManager.getStage(1));
    }
}
