package tutor.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Spring on 8/26/2015.
 */

public class DictionaryViewController implements Initializable{

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
        table_articles.setCellFactory(TextFieldTableCell.forTableColumn());
        initializeTableViewColumns();
        loadWordsFor(Controller.selectedLanguage);
        check_articles.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
            table_articles.setVisible(newValue1);
        });
    }

    private void initializeTableViewColumns(){

        MenuItem mnuDel = new MenuItem("Delete word");
        mnuDel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Word item = tblView_wordTranslation.getSelectionModel().getSelectedItem();
                if (item != null){
                    if (WordDAO.getInstance().contains(item)) {
                        WordDAO.getInstance().delete(WordDAO.getInstance().readByContentForActiveUser(item.getWord().get()));
                        loadWordsFor(Controller.selectedLanguage);
                    }
                }
            }
        });

        tblView_wordTranslation.setContextMenu(new ContextMenu(mnuDel));

        table_articles.setOnEditCommit( event -> {
            Word editedWord = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (editedWord.toString().isEmpty() || editedWord.getTranslation().get().isEmpty()){
                editedWord.setArticle(event.getNewValue());
            }
            if (WordDAO.getInstance().contains(editedWord)) {
                editedWord.setArticle(event.getNewValue());
                WordDAO.getInstance().update(editedWord);
            } else {
                if (!editedWord.getTranslation().get().isEmpty() && !editedWord.getWord().get().isEmpty()) {
                    WordDAO.getInstance().create(editedWord);
                    loadWordsFor(Controller.selectedLanguage);
                }
            }
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
                            loadWordsFor(Controller.selectedLanguage);
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
                            loadWordsFor(Controller.selectedLanguage);
                        }
                    }
                }
        );
    }

    private void loadWordsFor(Language wordLang){
        int totalWordsCount = WordDAO.getInstance().countFor(wordLang);
        paginatorManager = new PaginatorManager(totalWordsCount);
        paginator.setPageCount(paginatorManager.getTotalPages());
        paginator.setCurrentPageIndex(paginatorManager.getCurrentPage() - 1);
        paginator.setPageFactory(param -> {
            tblView_wordTranslation.getItems().clear();
            paginatorManager.goToPage(param);
            ObservableList<Word> addedWords = FXCollections.observableArrayList(WordDAO.getInstance().readAllByLangForActiveUser(wordLang, param * paginatorManager.getMaxItemsPerPage(), paginatorManager.getMaxItemsPerPage()));
            try {
                tblView_wordTranslation.setItems(FXCollections.observableArrayList(addedWords));
            }
            catch (IndexOutOfBoundsException ex){}
            if ( paginatorManager.getCurrentPage() == 1 ) {
                tblView_wordTranslation.getItems().add(0, new Word("", "", wordLang, AuthController.getActiveUser().getNativeLanguage()));
            }
            return new VBox();
        });
    }

    public void importFileClicked(ActionEvent actionEvent) {
        StageManager.getInstance().navigateTo(Navigator.getPathFor(Navigator.FILE_IMPORT_VIEW_PATH), ResourceBundleKeys.FILE_CHOOSER_TITLE, 2, Optional.empty(), true, false);
        initializeUI();
    }
}
