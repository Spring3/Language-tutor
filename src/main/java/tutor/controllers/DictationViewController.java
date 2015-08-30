package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;
import tutor.util.TaskManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Spring on 8/30/2015.
 */
public class DictationViewController implements Initializable {

    public DictationViewController(){
    }

    @FXML
    private TableView<Word> tblView_wordTranslation;
    @FXML
    private TableColumn<Word, String> table_articles;
    @FXML
    private TableColumn<Word, String> table_word;
    @FXML
    private TableColumn<Word, String> table_translation;
    @FXML
    private Button btn_startTask;
    private ResourceBundle bundle;
    private TaskManager manager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        initializeUI();
    }

    private void initializeUI(){

        table_articles.setCellValueFactory(param -> param.getValue().getArticle());
        table_word.setCellValueFactory(param -> param.getValue().getWord());
        table_translation.setCellValueFactory(param -> param.getValue().getTranslation());
        manager = new TaskManager(Controller.selectedLanguage);
        tblView_wordTranslation.setItems(FXCollections.observableArrayList(manager.createTask()));

    }

    public void btnStartClicked(ActionEvent actionEvent) {
        tblView_wordTranslation.setVisible(false);
    }
}
