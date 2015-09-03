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
import java.util.stream.Collectors;

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
    private String correctAnswer;
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
        if (!manager.getDictationMode().equals(TaskManager.DictationMode.REVERSED)){
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
        if (!manager.getDictationMode().equals(TaskManager.DictationMode.REVERSED)){
            txt_task.setText(manager.get(wordIndex).toString());
        }
        else{
            txt_task.setText(manager.get(wordIndex).getTranslation().get());
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

        if (manager.getDictationMode().equals(TaskManager.DictationMode.REVERSED)) {
            if (!manager.get(wordIndex).getArticle().get().isEmpty()) {
                checkAnswer(true, true);
            } else {
                checkAnswer(false, true);
            }
        } else {
            checkAnswer(false, false);
        }



        for(Node txt : pane_answers.getChildren()){
            assert (txt instanceof TextField);
            ((TextField) txt).setText("");
        }
        showTask();
    }

    private void checkAnswer(boolean hasArticle, boolean reversed){
        Word taskWord = manager.get(wordIndex);
        if (reversed){
            checkAnswerForReversedTest(true, hasArticle, taskWord);
        }
        else{
            checkAnswerForNormalTest(false, taskWord);

        }
    }

    private void checkAnswerForReversedTest(boolean reversed, boolean hasArticle, Word taskWord){
        if (hasArticle && !txt_word.getText().isEmpty()){
            String article = txt_word.getText().substring(0, txt_word.getText().indexOf(" ")).trim();
            String word = txt_word.getText().substring(txt_word.getText().indexOf(" "), txt_word.getText().length());
            if(article.equalsIgnoreCase(taskWord.getArticle().get()) && word.trim().equalsIgnoreCase(taskWord.getWord().get()))
                rememberAnswerResult(reversed, true, taskWord);
            else{
                for(Word synonym : taskWord.getWordsWithSimilarTranslation()){
                    if (article.equalsIgnoreCase(synonym.getArticle().get()) && word.trim().equalsIgnoreCase(synonym.getWord().get())) {
                        rememberAnswerResult(reversed, true, synonym);
                        return;
                    }
                }
                rememberAnswerResult(reversed, false, taskWord);
            }
        }
        else {
            correctAnswer = taskWord.getWord().get();
            if (!hasArticle && txt_word.getText().trim().equalsIgnoreCase(correctAnswer))
                rememberAnswerResult(reversed, true, taskWord);
            else{
                for(Word word : taskWord.getWordsWithSimilarTranslation()){
                    if (txt_word.getText().trim().equalsIgnoreCase(word.toString())){
                        rememberAnswerResult(reversed, true, word);
                        return;
                    }
                }
                rememberAnswerResult(reversed, false, taskWord);
            }
        }
    }

    private void checkAnswerForNormalTest(boolean reversed, Word taskWord){
        correctAnswer = taskWord.getTranslation().get();
        if (txt_translation.getText().trim().equalsIgnoreCase(correctAnswer)){
            rememberAnswerResult(reversed, true, taskWord);
        }
        else{
            for(Word word : taskWord.getOtherTranslationVariants()){
                if (txt_translation.getText().trim().equalsIgnoreCase(word.getTranslation().get())){
                    rememberAnswerResult(reversed, true, word);
                    return;
                }
            }
            rememberAnswerResult(reversed, false, taskWord);
        }
    }

    private void rememberAnswerResult(boolean reversed, boolean isCorrect, Word answer){
        if (isCorrect){
            if (!passedWords.contains(answer)){
                answer.incCorrectAnswerCount();
                WordDAO.getInstance().update(answer);
                passedWords.add(answer);
                correctAnswers ++;

            }
            label_answerWrong.setVisible(false);
            label_answerCorrect.setVisible(true);
            manager.getWords().remove(manager.get(wordIndex));
        }
        else
        {
            if (!passedWords.contains(answer)){
                answer.incWrongAnswerCount();
                WordDAO.getInstance().update(answer);
                passedWords.add(answer);
            }
            mistakes ++;
            label_answerWrong.setVisible(true);
            label_answerCorrect.setVisible(false);
        }

        pane_taskInfo.setVisible(true);

        StringBuilder answerVariants = new StringBuilder();
        if (reversed){
            answerVariants.append(answer.toString()).append("\n");
            for(String str : answer.getWordsWithSimilarTranslation().stream().map(Word::toString).collect(Collectors.toList())){
                if (!str.equalsIgnoreCase(answer.toString())){
                    answerVariants.append(str).append("\n");
                }
            }
        }
        else{
            answerVariants.append(answer.getTranslation().get()).append("\n");
            for(String str : answer.getOtherTranslationVariants().stream().map(word -> word.getTranslation().get()).collect(Collectors.toList())){
                if (!str.equalsIgnoreCase(answer.getTranslation().get())){
                    answerVariants.append(str).append("\n");
                }
            }
        }
        label_answer.setText(answerVariants.toString());
        label_taskCount.setText(String.valueOf(wordsAmount - manager.getWords().size()) + "/" + wordsAmount);
    }
}
