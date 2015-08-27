package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import tutor.dao.LanguageDAO;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;
import tutor.util.PaginatorManager;

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
    public TextField txtb_word;
    @FXML
    public TextField txtb_translation;
    @FXML
    public Button btn_add;
    @FXML
    public TableColumn<Word, String> table_word;
    @FXML
    public TableColumn<Word, String> table_translation;

    private PaginatorManager paginatorManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
        initializeUI();
    }

    private ResourceBundle bundle;

    private void initializeUI(){
        table_word.setCellValueFactory(param -> param.getValue().getWord());
        table_translation.setCellValueFactory(param -> param.getValue().getTranslation());
        table_word.setCellFactory(TextFieldTableCell.forTableColumn());
        table_translation.setCellFactory(TextFieldTableCell.forTableColumn());
        table_word.setOnEditCommit(event ->
                {
                    Word editedWord = event.getTableView().getItems().get(event.getTablePosition().getRow());
                    editedWord.setWord(event.getNewValue());
                    WordDAO.getInstance().update(editedWord);
                }
        );

        table_translation.setOnEditCommit(event ->
                {
                    Word editedWord = event.getTableView().getItems().get(event.getTablePosition().getRow());
                    editedWord.setTranslation(event.getNewValue());
                    WordDAO.getInstance().update(editedWord);
                }
        );

        ObservableList<Language> languages = FXCollections.observableList(LanguageDAO.getInstance().readAllLanguagesByUser(AuthController.getActiveUser().getId()));
        chb_language.setItems(languages);
        chb_language.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                loadWordsFor(newValue);
            }
        });
        try {
            chb_language.setValue(chb_language.getItems().get(0));
        }
        catch (IndexOutOfBoundsException ex){}

    }

    private void loadWordsFor(Language lang){
        ObservableList<Word> words = FXCollections.observableArrayList(WordDAO.getInstance().readAllByLangForActiveUser(lang));
        paginatorManager = new PaginatorManager(words.size());
        paginator.setPageCount(paginatorManager.getTotalPages());
        paginator.setCurrentPageIndex(paginatorManager.getCurrentPage() - 1);
        paginator.setPageFactory(param -> {
            ObservableList<Word> addedWords = FXCollections.observableArrayList(WordDAO.getInstance().readAllByLangForActiveUser(lang));
            tblView_wordTranslation.getItems().clear();
            paginatorManager.goToPage(param);
            tblView_wordTranslation.setItems(FXCollections.observableArrayList(addedWords.subList(paginatorManager.getStartIndexForNextPageElements(), paginatorManager.getLastIndexForNextPageElements())));
            return new VBox();
        });


    }

    public void addWord(ActionEvent actionEvent) {
        if (!txtb_word.getText().isEmpty() && !txtb_translation.getText().isEmpty() && chb_language.getSelectionModel().getSelectedItem() != null){
            WordDAO.getInstance().create(new Word(txtb_word.getText(), txtb_translation.getText(), chb_language.getSelectionModel().getSelectedItem()));
            tblView_wordTranslation.getItems().clear();
            loadWordsFor(chb_language.getSelectionModel().getSelectedItem());
        }
    }

    public void apply(ActionEvent actionEvent) {

    }
}
