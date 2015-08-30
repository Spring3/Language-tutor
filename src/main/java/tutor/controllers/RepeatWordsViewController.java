package tutor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import tutor.models.Word;
import tutor.util.StageManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Spring on 8/30/2015.
 */
public class RepeatWordsViewController implements Initializable{

    public RepeatWordsViewController(){

    }

    @FXML
    private Label label_word;
    @FXML
    private Label label_translation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wordsToRepeat = new ArrayList<>();
    }

    private List<Word> wordsToRepeat;
    private int index;

    public void repeat(List<Word> wordList){
        wordsToRepeat = wordList;
        label_word.setText(wordsToRepeat.get(index).toString());
        label_translation.setText(wordsToRepeat.get(index).getTranslation().get());
    }


    public void nextClicked(ActionEvent actionEvent) {
        if (index < wordsToRepeat.size() - 1) {
            index++;
            label_word.setText(wordsToRepeat.get(index).toString());
            label_translation.setText(wordsToRepeat.get(index).getTranslation().get());
        }
        else {
            StageManager.getInstance().closeStage(StageManager.getInstance().getStage(2));
        }
    }
}
