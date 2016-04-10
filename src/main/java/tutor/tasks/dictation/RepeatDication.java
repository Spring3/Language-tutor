package tutor.tasks.dictation;

import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Spring on 4/9/2016.
 */
public class RepeatDication extends Dictation {

    public RepeatDication(Language langToLearn){
        languageToLearn = langToLearn;
        wordsForTask = new HashSet<>();
        passedWords = new HashMap<>();
        dictationType = DictationType.REPEATING;
        dictationMode = generateDictationMode();
        initialize();
    }

    public RepeatDication(AbstractDictation dictation){
        languageToLearn = dictation.getLanguage();
        dictationType = dictation.getDictationType();
        dictationMode = dictation.getDictationMode() == null ? generateDictationMode() : dictation.getDictationMode();
        wordsForTask = new HashSet<>();
        passedWords = new HashMap<>();
        initialize();
    }


    @Override
    public void initialize() {
        List<Word> wordsToRepeat = WordDAO.getInstance().readAllWordsToRepeatFor(getLanguageToLearn());
        fillArrayWithWords(wordsToRepeat);
    }
}
