package tutor.controllers;

import com.sun.jmx.snmp.tasks.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import tutor.dao.LanguageDAO;
import tutor.dao.StatsDAO;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Stats;
import tutor.tasks.TaskType;
import tutor.util.StageManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by spring on 4/29/16.
 */
public class StatsViewController implements Initializable {


    @FXML
    Label lbl_username;
    @FXML
    Label lbl_regDate;
    @FXML
    ChoiceBox<Language> chb_language;
    @FXML
    Label lbl_totalWordsCount;
    @FXML
    Label lbl_passedTasksCount;
    @FXML
    Label lbl_totalSuccessRate;
    @FXML
    Label lbl_correctToMistakeRate;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_username.setText(AuthController.getActiveUser().toString() + " " + lbl_username.getText());
        lbl_regDate.setText(AuthController.getActiveUser().getDateOfRegistery().toLocalDateTime().toLocalDate().toString());
        chb_language.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            WordDAO wordDAO = WordDAO.getInstance();
            lbl_totalWordsCount.setText(wordDAO.getWordsCount(newValue, AuthController.getActiveUser()) + "");
            lbl_correctToMistakeRate.setText(wordDAO.getAnswersRatio(newValue, AuthController.getActiveUser()) + "");
            //TODO: let user select the type of the task.
            Stats statistics = StatsDAO.getInstance().read(AuthController.getActiveUser(), newValue, TaskType.DICTATION);
            if (statistics != null) {
                lbl_passedTasksCount.setText(statistics.getTries() + "");
                lbl_totalSuccessRate.setText(statistics.getSuccessRate() + "");
            }
            else{
                lbl_passedTasksCount.setText(0 + "");
                lbl_totalSuccessRate.setText(0 + "");

            }
        });

        chb_language.getItems().addAll(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));

        if (chb_language.getItems().size() > 0) {
            if (Controller.selectedLanguage != null) {
                chb_language.getSelectionModel().select(Controller.selectedLanguage);
            } else {
                chb_language.getSelectionModel().select(0);
            }
        }
    }

    public void Close(ActionEvent actionEvent) {
        StageManager.getInstance().closeStage(StageManager.getInstance().getStage(this));
    }
}
