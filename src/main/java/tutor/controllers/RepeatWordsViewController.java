package tutor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import tutor.util.Voice;
import tutor.models.Word;
import tutor.util.StageManager;

import java.net.URL;
import java.util.*;

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
    private Voice voice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wordsToRepeat = new HashSet<>();
        voice = Voice.getInstance();
    }

    private Set<Word> wordsToRepeat;
    private int index;

    public void repeat(Set<Word> wordList){
        wordsToRepeat = wordList;
        label_word.setText(get(index).toString());
        voice.say(label_word.getText(), get(index).getWordLang());
        label_translation.setText(get(index).getTranslation().get());
        voice.say(label_translation.getText(), get(index).getTranslationLang());
    }


    public void nextClicked(ActionEvent actionEvent) {
        if (index < wordsToRepeat.size() - 1) {
            index++;
            label_word.setText(get(index).toString());
            voice.say(label_word.getText(), get(index).getWordLang());
            label_translation.setText(get(index).getTranslation().get());
            voice.say(label_translation.getText(), get(index).getTranslationLang());
        }
        else {
            voice.dispose();
            StageManager.getInstance().closeStage(StageManager.getInstance().getStage(2));
        }
    }

    private Word get(int index){
        int counter = 0;
        if (wordsToRepeat.size() > 0){
            if (index == 0){
                return wordsToRepeat.iterator().next();
            }
            Iterator<Word> iterator;
            for ( iterator = wordsToRepeat.iterator(); counter < index && iterator.hasNext(); counter ++){
                iterator.next();
            }
            return iterator.next();
        }
        return new Word();
    }
}
