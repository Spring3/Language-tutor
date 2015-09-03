package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import tutor.dao.StatsDAO;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.Main;
import tutor.models.Stats;
import tutor.models.Word;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;
import tutor.util.TaskManager;

import java.net.URL;
import java.util.*;

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
    private TextField txt_task;
    @FXML
    private TextField txt_word;
    @FXML
    private TextField txt_translation;
    @FXML
    private AnchorPane pane_task;
    @FXML
    private FlowPane pane_answers;
    @FXML
    private AnchorPane pane_taskInfo;
    @FXML
    private Label label_answerCorrect;
    @FXML
    private Label label_answerWrong;
    @FXML
    private Label label_answer;
    @FXML
    private Label label_taskCount;
    @FXML
    private Button btn_startTask;
    @FXML
    private Button btn_repeatWords;
    @FXML
    private Button btn_confirm;
    @FXML
    private TextArea txt_description;
    private ResourceBundle bundle;
    private TaskManager manager;
    private int wordsAmount;
    private int wordIndex;
    private int mistakes;
    private int correctAnswers;
    private String answer;
    private List<Word> passedWords; //if boolean is true, then this word needs to be repeated again.


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        passedWords = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI(){

        table_articles.setCellValueFactory(param -> param.getValue().getArticle());
        table_word.setCellValueFactory(param -> param.getValue().getWord());
        table_translation.setCellValueFactory(param -> param.getValue().getTranslation());
        manager = new TaskManager(Controller.selectedLanguage);
        tblView_wordTranslation.setItems(FXCollections.observableArrayList(manager.createTask()));
        wordsAmount = manager.getWords().size();
        if (!manager.getMode().equals(TaskManager.TaskManagerMode.REVERSED)){
            pane_answers.getChildren().add(0, txt_translation);
            txt_translation.setVisible(true);
        }
        else{
            pane_answers.getChildren().add(0, txt_word);
            txt_word.setVisible(true);
        }

    }

    public void btnStartClicked(ActionEvent actionEvent) {
        tblView_wordTranslation.setVisible(false);
        pane_task.setVisible(true);
        btn_startTask.setVisible(false);
        btn_repeatWords.setVisible(false);
        btn_confirm.defaultButtonProperty().bind(btn_confirm.focusedProperty());
        txt_description.setVisible(false);
        StageManager.getInstance().getStage(1).getScene().setOnKeyReleased(event ->
        {
            KeyCombination combo = new KeyCodeCombination(KeyCode.ENTER);
            if (combo.match(event)) {
                btn_confirm.fire();
            }
        });
        showTask();
    }

    private void showTask(){
        if (manager.getWords().size() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString(ResourceBundleKeys.LABEL_TASK_COMPLETE));
            StringBuilder builder = new StringBuilder();
            builder.append(bundle.getString(ResourceBundleKeys.LABEL_TASK_RESULTS))
                    .append("\n")
                    .append(bundle.getString(ResourceBundleKeys.LABEL_TASK_WORDS_AMOUNT)).append(" : ").append(wordsAmount)
                    .append("\n")
                    .append(bundle.getString(ResourceBundleKeys.LABEL_TASK_MISTAKES)).append(" : ").append(mistakes)
                    .append("\n")
                    .append(bundle.getString(ResourceBundleKeys.LABEL_TASK_CORRECT_ANSWERS)).append(" : ").append(correctAnswers)
                    .append("\n")
                    .append(bundle.getString(ResourceBundleKeys.SUCCESS_RATE)).append(" : ").append(getSuccessRate()).append("%");
            alert.setContentText(builder.toString());
            alert.showAndWait();

            for(Word word : passedWords){
                WordDAO.getInstance().update(word);
            }

            Stats statistics = new Stats(AuthController.getActiveUser(), manager.getMode(), Controller.selectedLanguage, getSuccessRate());
            StatsDAO.getInstance().create(statistics);

            StageManager.getInstance().closeStage(StageManager.getInstance().getStage(1));

            return;
        }
        Random random = new Random();
        wordIndex = random.nextInt(manager.getWords().size());
        if (!manager.getMode().equals(TaskManager.TaskManagerMode.REVERSED)){
            txt_task.setText(manager.getWords().get(wordIndex).toString());
        }
        else{
            txt_task.setText(manager.getWords().get(wordIndex).getTranslation().get());
        }
        pane_answers.getChildren().get(0).requestFocus();
    }

    private float getSuccessRate(){
        if (correctAnswers != 0) {
            if (mistakes != 0) {
                if (mistakes > wordsAmount) {
                    return (float) correctAnswers / passedWords.size() * 70 + (float) correctAnswers / mistakes * 15 + (float) wordsAmount / mistakes * 15;
                }
                else{
                    return (float) correctAnswers / passedWords.size() * 70 + (float) correctAnswers/mistakes  * 15 + (float) mistakes/wordsAmount * 15;
                }
            }
            else{
                return (float) correctAnswers/passedWords.size() * 70 +  30;
            }
        }
        else{
            if (mistakes != 0){
                if (mistakes > wordsAmount){
                    return (float) correctAnswers / passedWords.size() * 70 +  15 + (float) wordsAmount/mistakes * 15;
                }
                else {
                    return (float) correctAnswers / passedWords.size() * 70 +  15 + (float) mistakes/wordsAmount * 15;
                }
            }
            else{
                return (float)correctAnswers/passedWords.size() * 70 + 30;
            }

        }
    }

    public void btnRepeatClicked(ActionEvent actionEvent) {
        StageManager.getInstance().navigateTo(Main.class.getClassLoader().getResource(Navigator.REPEAT_WORDS_VIEW_PATH), "", 2, Optional.empty(), true, true);
        ((RepeatWordsViewController) StageManager.getInstance().getControllerForViewOnLayer(2)).repeat(manager.getWords());
        StageManager.getInstance().showAll(true);
    }

    public void confirmAnswer(ActionEvent actionEvent) {
        boolean isCorrect;

        if (manager.getMode().equals(TaskManager.TaskManagerMode.REVERSED)) {
            if (!manager.getWords().get(wordIndex).getArticle().get().isEmpty()) {
                isCorrect = checkAnswer(true, true);
            } else {
                isCorrect = checkAnswer(false, true);
            }
        } else {
            isCorrect = checkAnswer(false, false);
        }
        pane_taskInfo.setVisible(true);


        if (isCorrect){
            if (!passedWords.contains(manager.getWords().get(wordIndex))){
                manager.getWords().get(wordIndex).incCorrectAnswerCount();
                WordDAO.getInstance().update(manager.getWords().get(wordIndex));
                passedWords.add(manager.getWords().get(wordIndex));
                correctAnswers ++;

            }
            label_answerWrong.setVisible(false);
            label_answerCorrect.setVisible(true);
            manager.getWords().remove(wordIndex);
        }
        else
        {
            if (!passedWords.contains(manager.getWords().get(wordIndex))){
                manager.getWords().get(wordIndex).incWrongAnswerCount();
                WordDAO.getInstance().update(manager.getWords().get(wordIndex));
                passedWords.add(manager.getWords().get(wordIndex));
            }
            mistakes ++;
            label_answerWrong.setVisible(true);
            label_answerCorrect.setVisible(false);
        }


        label_answer.setText(answer);
        label_taskCount.setText(String.valueOf(wordsAmount - manager.getWords().size()) + "/" + wordsAmount);


        for(Node txt : pane_answers.getChildren()){
            assert (txt instanceof TextField);
            ((TextField) txt).setText("");
        }
        showTask();
    }

    private boolean checkAnswer(boolean hasArticle, boolean reversed){
        Word taskWord = manager.getWords().get(wordIndex);
        if (reversed){
            if (hasArticle && !txt_word.getText().isEmpty()){
                answer = taskWord.toString();
                String article = txt_word.getText().substring(0, txt_word.getText().indexOf(" ")).trim();
                String word = txt_word.getText().substring(txt_word.getText().indexOf(" "), txt_word.getText().length());
                return article.equalsIgnoreCase(taskWord.getArticle().get()) && word.trim().equalsIgnoreCase(taskWord.getWord().get());
            }
            else {
                answer = taskWord.getWord().get();
                return (!hasArticle && txt_word.getText().trim().toUpperCase().equals(answer.toUpperCase()));
            }
        }
        else{
            answer = taskWord.getTranslation().get();
            return txt_translation.getText().trim().toUpperCase().equals(taskWord.getTranslation().get().toUpperCase());
        }
    }
}
