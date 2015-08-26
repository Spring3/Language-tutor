package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tutor.dao.LanguageDAO;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Spring on 8/26/2015.
 */

public class DictionaryViewController implements Initializable{

    @FXML
    public ChoiceBox<Language> chb_language;
    @FXML
    public Pagination paginator;
    @FXML
    public TableView<Word> tblView_wordTranslation;
    @FXML
    public Button btn_apply;
    @FXML
    public ChoiceBox<String> chb_sort;
    @FXML
    public TextField txtb_word;
    @FXML
    public TextField txtb_translation;
    @FXML
    public Button btn_add;
    @FXML
    public TableColumn<Word, String> table_word;
    @FXML
    public TableColumn<Word, String> table_translation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
        initializeUI();
    }

    private ResourceBundle bundle;

    private void initializeUI(){
        table_word.setCellValueFactory(param -> param.getValue().getWord());
        table_translation.setCellValueFactory(param -> param.getValue().getTranslation());

        ObservableList<Language> languages = FXCollections.observableList(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));
        chb_language.setItems(languages);
        chb_language.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)){
                loadWordsFor(newValue);
            }
        });
        try {
            chb_language.setValue(chb_language.getItems().get(0));
        }
        catch (IndexOutOfBoundsException ex){}

        ObservableList<String> sortStrategies = FXCollections.observableList(new ArrayList<String>(){{
            add("A-Z");
            add("Z-A");
            add("New");
            add("Old");
        }});
        chb_sort.setItems(sortStrategies);
        chb_sort.setValue(chb_sort.getItems().get(2));

    }

    private void loadWordsFor(Language lang){
        ObservableList<Word> words = FXCollections.observableArrayList(WordDAO.getInstance().readAllByLangForActiveUser(lang));
        tblView_wordTranslation.setItems(words);
    }

    public void addWord(ActionEvent actionEvent) {
        if (!txtb_word.getText().isEmpty() && !txtb_translation.getText().isEmpty() && chb_language.getSelectionModel().getSelectedItem() != null)
            WordDAO.getInstance().create(new Word(txtb_word.getText(), txtb_translation.getText(), chb_language.getSelectionModel().getSelectedItem()));
    }

    public void apply(ActionEvent actionEvent) {

    }
}
