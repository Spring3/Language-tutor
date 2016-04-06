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
import tutor.util.Voice;
import tutor.Main;
import tutor.models.Word;
import tutor.util.ResourceBundleKeys;
import tutor.util.StageManager;
import tutor.util.TaskManager;

import java.awt.*;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        voice = Voice.getInstance();
        initializeUI();
    }

    private void initializeUI(){

        manager = new TaskManager(Controller.selectedLanguage);
        manager.createTask();

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
            lbl_answerCaption.setVisible(true);
            txt_task.setText(manager.getNextTaskWord());
            voice.say(txt_task.getText(), manager.getAnswerWordLanguage());
        }
        else{
            lbl_answerCaption.setVisible(false);
            btn_update.setVisible(true);
            ImageView imageView = new ImageView(Main.class.getClassLoader().getResource("common/updatepng.png").toExternalForm());
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            btn_update.setGraphic(imageView);
            if (manager.getDictationMode().equals(TaskManager.DictationMode.NORMAL)){
                lbl_dictation_traditional_normal.setVisible(true);
            }
            else{
                lbl_dictation_traditional_reversed.setVisible(true);
            }
            txt_task.setVisible(false);
            voice.say(manager.getNextTaskWord(), manager.getAnswerWordLanguage());
        }
        txt_answer.requestFocus();
    }

    public void repeatVoice(ActionEvent actionEvent) {
        voice.say(manager.getCorrectAnswer(), manager.getAnswerWordLanguage());
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

        Word answerWord = manager.getCorrectAnswerWord();
        if (manager.getDictationMode().equals(TaskManager.DictationMode.REVERSED)){
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
        label_taskCount.setText(String.valueOf(manager.getWordsAmount() - manager.getWords().size()) + "/" + manager.getWordsAmount());
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
