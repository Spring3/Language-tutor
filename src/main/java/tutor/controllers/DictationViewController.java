package tutor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tutor.tasks.AbstractTask;
import tutor.tasks.TaskType;
import tutor.tasks.dictation.Dictation;
import tutor.util.Voice;
import tutor.Main;
import tutor.models.Word;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;
import tutor.tasks.TaskManager;

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
    private TextField txt_task;
    @FXML
    private TextField txt_answer;
    @FXML
    private AnchorPane pane_task;
    @FXML
    private VBox vbox_answers;
    @FXML
    private AnchorPane pane_taskInfo;
    @FXML
    private Label label_answerCorrect;
    @FXML
    private Label label_answerWrong;
    @FXML
    private Label label_taskCount;
    @FXML
    private Label lbl_dictation_traditional_normal;
    @FXML
    private Label lbl_dictation_traditional_reversed;
    @FXML
    private Label lbl_answerCaption;
    @FXML
    private Button btn_update;
    @FXML
    private ImageView imageView;
    private ResourceBundle bundle;
    private TaskManager manager;
    private Voice voice;
    private Dictation task;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        voice = Voice.getInstance();
        initializeUI();
    }

    private void initializeUI(){
        Random rand = new Random();
        manager = new TaskManager(TaskManager.Output.values()[rand.nextInt(TaskManager.Output.values().length)]);
        AbstractTask abstractTask = new AbstractTask(TaskType.DICTATION, Controller.selectedLanguage);
        task = (Dictation) manager.createTask(abstractTask);

    }

    public void init(){
        pane_task.setVisible(true);
        StageManager.getInstance().getStage(1).getScene().setOnKeyReleased(event ->
        {
            KeyCombination combo = new KeyCodeCombination(KeyCode.ENTER);
            if (combo.match(event)) {
                confirmAnswer();
            }
        });

        showTask();
    }

    private void showTask(){
        if (task.getWords().size() == 0){
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
        if (manager.getOutputMode().equals(TaskManager.Output.TEXT)){
            lbl_answerCaption.setVisible(true);
            txt_task.setText(manager.getNextTaskWord());
            voice.say(txt_task.getText(), task.getAnswerLanguage());
        }
        else{
            lbl_answerCaption.setVisible(false);
            btn_update.setVisible(true);
            ImageView imageView = new ImageView(Main.class.getClassLoader().getResource("common/updatepng.png").toExternalForm());
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            btn_update.setGraphic(imageView);
            if (task.getMode().equals(Dictation.Mode.NORMAL)){
                lbl_dictation_traditional_normal.setVisible(true);
            }
            else{
                lbl_dictation_traditional_reversed.setVisible(true);
            }
            txt_task.setVisible(false);
            manager.getNextTaskWord();
            voice.say(task.getCorrectAnswer(), task.getAnswerLanguage());
        }
        txt_answer.requestFocus();
    }

    public void repeatVoice(ActionEvent actionEvent) {
        voice.say(manager.getCorrectAnswer(), task.getAnswerLanguage());
    }

    public void confirmAnswer() {

        vbox_answers.getChildren().clear();
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

        Word answerWord = manager.getCorrectWord();
        if (task.getMode().equals(Dictation.Mode.REVERSED)){
            vbox_answers.getChildren().add(makeLabel(answerWord.toString()));
            for(String str : answerWord.getWordsWithSimilarTranslation().stream().map(Word::toString).collect(Collectors.toList())){
                if (!str.equalsIgnoreCase(answerWord.toString())){
                    vbox_answers.getChildren().add(makeLabel(str));
                }
            }
        }
        else{
            vbox_answers.getChildren().add(makeLabel(answerWord.getTranslation().get()));
            for(String str : answerWord.getOtherTranslationVariants().stream().map(word -> word.getTranslation().get()).collect(Collectors.toList())){
                if (!str.equalsIgnoreCase(answerWord.getTranslation().get())){
                    vbox_answers.getChildren().add(makeLabel(str));
                }
            }
        }
        label_taskCount.setText(String.valueOf(manager.getWordsAmount() - task.getWords().size()) + "/" + manager.getWordsAmount());
        txt_answer.setText("");
        showTask();
    }

    private Label makeLabel(String text){
        Label result = new Label(text);
        result.getStyleClass().add("settings_label");
        result.setStyle("-fx-font-size: 20");
        return result;
    }
}
