package tutor.tasks.dictation;

import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import java.util.*;

/**
 * Created by Spring on 4/9/2016.
 */
public class NormalDictation extends Dictation {

    public NormalDictation(Language langToLearn){
        languageToLearn = langToLearn;
        dictationType = DictationType.NORMAL;
        dictationMode = generateDictationMode();
        wordsForTask = new HashSet<>();
        passedWords = new HashMap();
        initialize();
    }

    public NormalDictation(AbstractDictation abstractDictation){
        languageToLearn = abstractDictation.getLanguage();
        dictationType = abstractDictation.getDictationType();
        dictationMode = abstractDictation.getDictationMode() == null ? generateDictationMode() : abstractDictation.getDictationMode();
        wordsForTask = new HashSet<>();
        passedWords = new HashMap();
        initialize();
    }


    @Override
    public void initialize() {
        List<Word> allWords = WordDAO.getInstance().readAllByLangForActiveUser(getLanguageToLearn());
        fillArrayWithWords(allWords);
    }


}
