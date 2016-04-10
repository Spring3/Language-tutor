package tutor.tasks;

import tutor.models.Language;
import tutor.models.Word;
import tutor.tasks.dictation.Dictation;

import java.util.Map;
import java.util.Set;

/**
 * Created by Spring on 4/9/2016.
 */
public interface ITask {

    Set<Word> getWords();
    Map<Word, Boolean> getPassedWords();
    int getWordsAmount();
    void setMaxWordsForTask(int value);
    int getMaxWordsForTask();
    Language getAnswerLanguage();
    Language getLanguageToLearn();
    String getCorrectAnswer();
    String getTaskWord();
    Word getCorrectWord();
    Dictation.Mode getMode();
    TaskType getTaskType();
    void initialize();
    Word getNextWord();
    boolean firstTry();
    boolean check(String text);
    boolean check(String text, boolean hasArticle);

}
