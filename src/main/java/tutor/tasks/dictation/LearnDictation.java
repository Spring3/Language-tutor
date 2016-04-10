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
public class LearnDictation extends Dictation {

    public LearnDictation(Language language){
        languageToLearn = language;
        wordsForTask = new HashSet<>();
        passedWords = new HashMap<>();
        dictationType = DictationType.LEARNING;
        dictationMode = generateDictationMode();
        initialize();
    }

    public LearnDictation(AbstractDictation dictation){
        languageToLearn = dictation.getLanguage();
        dictationType = dictation.getDictationType();
        dictationMode = dictation.getDictationMode() == null ? generateDictationMode() : dictation.getDictationMode();
        wordsForTask = new HashSet<>();
        passedWords = new HashMap<>();
        initialize();
    }

    @Override
    public void initialize() {
        List<Word> newWords = WordDAO.getInstance().readAllNewlyAddedWordsFor(getLanguageToLearn());
        fillArrayWithWords(newWords);
    }
}
