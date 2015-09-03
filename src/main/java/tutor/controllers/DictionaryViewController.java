package tutor.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;

/**
 * Created by Spring on 8/26/2015.
 */

public class DictionaryViewController implements Initializable{

    @FXML
    private ChoiceBox<Language> chb_language;
    @FXML
    private Pagination paginator;
    @FXML
    private TableView<Word> tblView_wordTranslation;
    @FXML
    private Button btn_apply;
    @FXML
    private TableColumn<Word, String> table_word;
    @FXML
    private TableColumn<Word, String> table_translation;
    @FXML
    private TableColumn<Word, Button> table_remove;
    @FXML
    private TableColumn<Word, String> table_articles;
    @FXML
    private CheckBox check_articles;

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
        table_articles.setCellValueFactory(param -> param.getValue().getArticle());
        table_word.setCellFactory(TextFieldTableCell.forTableColumn());
        table_translation.setCellFactory(TextFieldTableCell.forTableColumn());
        table_articles.setCellFactory(TextFieldTableCell.forTableColumn());;
        initializeTableViewColumns();

        check_articles.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
            table_articles.setVisible(newValue1);
        });

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

    private void initializeTableViewColumns(){
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
                    if (editedWord.toString().isEmpty() || editedWord.getTranslation().get().isEmpty()){
                        editedWord.setWord(event.getNewValue());
                    }
                    if (WordDAO.getInstance().contains(editedWord)) {
                        editedWord.setWord(event.getNewValue());
                        WordDAO.getInstance().update(editedWord);
                    } else {
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
                    if (editedWord.toString().isEmpty() || editedWord.getTranslation().get().isEmpty()){
                        editedWord.setTranslation(event.getNewValue());
                    }
                    if (WordDAO.getInstance().contains(editedWord)) {
                        editedWord.setTranslation(event.getNewValue());
                        WordDAO.getInstance().update(editedWord);
                    } else {
                        if (!editedWord.getTranslation().get().isEmpty() && !editedWord.getWord().get().isEmpty()) {
                            WordDAO.getInstance().create(editedWord);
                            loadWordsFor(chb_language.getSelectionModel().getSelectedItem());
                        }
                    }


                }
        );
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
            try {
                tblView_wordTranslation.setItems(FXCollections.observableArrayList(addedWords.subList(paginatorManager.getStartIndexForNextPageElements(), paginatorManager.getLastIndexForNextPageElements())));
            }
            catch (IndexOutOfBoundsException ex){}
            if (addedWords.size() > 0 && paginatorManager.getCurrentPage() == 1 && tblView_wordTranslation.getItems().size() > 0 &&tblView_wordTranslation.getItems().get(0).getId() != 0)
                tblView_wordTranslation.getItems().add(0, new Word("", "", wordLang, AuthController.getActiveUser().getNativeLanguage()));
            return new VBox();
        });
    }
}
