package tutor.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
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
    public TableColumn<Word, String> table_word;
    @FXML
    public TableColumn<Word, String> table_translation;
    @FXML
    public TableColumn<Word, Button> table_remove;

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
        table_remove.setCellValueFactory(param -> {
            Button button = new Button("x");
            button.getStyleClass().add("custombutton");
            button.setOnAction(event -> {
                if (WordDAO.getInstance().contains(param.getValue())) {
                    WordDAO.getInstance().delete(WordDAO.getInstance().readByContentForActiveUser(param.getValue().getWord().get()));
                    loadWordsFor(chb_language.getSelectionModel().getSelectedItem());
                }
            });
            return new ReadOnlyObjectWrapper<Button>(button);
        });

        table_word.setOnEditCommit(event ->
                {
                    Word editedWord = event.getTableView().getItems().get(event.getTablePosition().getRow());
                    editedWord.setWord(event.getNewValue());

                    if (WordDAO.getInstance().contains(editedWord)){
                        WordDAO.getInstance().update(editedWord);
                    }
                    else{
                        if (!editedWord.getTranslation().get().isEmpty() && !editedWord.getWord().get().isEmpty()) {
                            WordDAO.getInstance().create(editedWord);
                            loadWordsFor(chb_language.getSelectionModel().getSelectedItem());
                        }
                    }
                }
        );

        table_translation.setOnEditCommit(event ->
                {
                    Word editedWord = event.getTableView().getItems().get(event.getTablePosition().getRow());
                    editedWord.setTranslation(event.getNewValue());
                    if (WordDAO.getInstance().contains(editedWord)) {
                        WordDAO.getInstance().update(editedWord);
                    } else {
                        if (!editedWord.getTranslation().get().isEmpty() && !editedWord.getWord().get().isEmpty()) {
                            WordDAO.getInstance().create(editedWord);
                            loadWordsFor(chb_language.getSelectionModel().getSelectedItem());
                        }
                    }

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

    private void loadWordsFor(Language wordLang){
        ObservableList<Word> words = FXCollections.observableArrayList(WordDAO.getInstance().readAllByLangForActiveUser(wordLang));
        paginatorManager = new PaginatorManager(words.size());
        paginator.setPageCount(paginatorManager.getTotalPages());
        paginator.setCurrentPageIndex(paginatorManager.getCurrentPage() - 1);
        paginator.setPageFactory(param -> {
            ObservableList<Word> addedWords = FXCollections.observableArrayList(WordDAO.getInstance().readAllByLangForActiveUser(wordLang));
            tblView_wordTranslation.getItems().clear();
            paginatorManager.goToPage(param);
            tblView_wordTranslation.setItems(FXCollections.observableArrayList(addedWords.subList(paginatorManager.getStartIndexForNextPageElements(), paginatorManager.getLastIndexForNextPageElements())));
            if (addedWords.size() > 0 && paginatorManager.getCurrentPage() == 1 && tblView_wordTranslation.getItems().get(0).getId() != 0)
                tblView_wordTranslation.getItems().add(0, new Word("", "", wordLang, AuthController.getActiveUser().getNativeLanguage()));
            return new VBox();
        });


    }
}
