package tutor.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import tutor.google.Voice;
import tutor.Main;
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
    private TextField txt_answer;
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
    private Label lbl_dictation_traditional_normal;
    @FXML
    private Label lbl_dictation_traditional_reversed;
    @FXML
    private Button btn_startTask;
    @FXML
    private Button btn_repeatWords;
    @FXML
    private Button btn_confirm;
    @FXML
    private Button btn_update;
    @FXML
    private TextArea txt_description;
    @FXML
    private ImageView imageView;
    private ResourceBundle bundle;
    private TaskManager manager;
    private Voice voice;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        voice = Voice.getInstance();
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
                    .append(bundle.getString(ResourceBundleKeys.LABEL_TASK_WORDS_AMOUNT)).append(" : ").append(manager.getWordsAmount())
                    .append("\n")
                    .append(bundle.getString(ResourceBundleKeys.LABEL_TASK_MISTAKES)).append(" : ").append(manager.getMistakesCount())
                    .append("\n")
                    .append(bundle.getString(ResourceBundleKeys.LABEL_TASK_CORRECT_ANSWERS)).append(" : ").append(manager.getCorrectAnswersCount())
                    .append("\n")
                    .append(bundle.getString(ResourceBundleKeys.SUCCESS_RATE)).append(" : ").append(manager.getSuccessRate()).append("%");
            alert.setContentText(builder.toString());
            alert.showAndWait();

            manager.endTask();

            StageManager.getInstance().closeStage(StageManager.getInstance().getStage(1));
            return;
        }
        if (!manager.getMode().equals(TaskManager.TaskManagerMode.TRADITIONAL)){

            txt_task.setText(manager.getNextTaskWord());
            voice.say(txt_task.getText(), manager.getAnswerWordLanguage());
        }
        else{
            btn_update.setVisible(true);
            if (manager.getDictationMode().equals(TaskManager.DictationMode.NORMAL)){
                lbl_dictation_traditional_normal.setVisible(true);
            }
            else{
                lbl_dictation_traditional_reversed.setVisible(true);
            }
            txt_task.setVisible(false);
            voice.say(manager.getNextTaskWord(), manager.getAnswerWordLanguage());
        }
        pane_answers.getChildren().get(0).requestFocus();
    }

    public void repeatVoice(ActionEvent actionEvent) {
        voice.say(manager.getCorrectAnswer(), manager.getAnswerWordLanguage());
    }

    public void btnRepeatClicked(ActionEvent actionEvent) {
        StageManager.getInstance().navigateTo(Main.class.getClassLoader().getResource(Navigator.REPEAT_WORDS_VIEW_PATH), "", 2, Optional.empty(), true, true);
        ((RepeatWordsViewController) StageManager.getInstance().getControllerForViewOnLayer(2)).repeat(manager.getWords());
        StageManager.getInstance().showAll(true);
    }

    public void confirmAnswer(ActionEvent actionEvent) {

        boolean result = manager.confirmAnswer(txt_answer.getText());
        if (result){
            voice.play(voice.MEDIA_ANSWER_CORRECT);
            label_answerWrong.setVisible(false);
            label_answerCorrect.setVisible(true);
        }
        else{
            voice.play(voice.MEDIA_ANSWER_WRONG);
            label_answerWrong.setVisible(true);
            label_answerCorrect.setVisible(false);
        }

        pane_taskInfo.setVisible(true);

        StringBuilder answerVariants = new StringBuilder();
        Word answerWord = manager.getCorrectAnswerWord();
        if (manager.getDictationMode().equals(TaskManager.DictationMode.REVERSED)){
            answerVariants.append(answerWord.toString()).append("\n");
            for(String str : answerWord.getWordsWithSimilarTranslation().stream().map(Word::toString).collect(Collectors.toList())){
                if (!str.equalsIgnoreCase(answerWord.toString())){
                    answerVariants.append(str).append("\n");
                }
            }
        }
        else{
            answerVariants.append(answerWord.getTranslation().get()).append("\n");
            for(String str : answerWord.getOtherTranslationVariants().stream().map(word -> word.getTranslation().get()).collect(Collectors.toList())){
                if (!str.equalsIgnoreCase(answerWord.getTranslation().get())){
                    answerVariants.append(str).append("\n");
                }
            }
        }
        label_answer.setText(answerVariants.toString());
        label_taskCount.setText(String.valueOf(manager.getWordsAmount() - manager.getWords().size()) + "/" + manager.getWordsAmount());

        txt_answer.setText("");
        showTask();
    }
}
