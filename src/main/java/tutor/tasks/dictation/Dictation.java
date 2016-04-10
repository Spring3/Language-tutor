package tutor.tasks.dictation;

import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;
import tutor.tasks.ITask;
import tutor.tasks.TaskType;

import java.util.*;

/**
 * Created by Spring on 4/9/2016.
 */
public abstract class Dictation implements ITask {

    public enum Mode{
        NORMAL,  // given the word -> write translation
        REVERSED // given the translation -> write word
    }

    public enum DictationType{
        NORMAL,
        REPEATING,
        LEARNING
    }

    private static int MAX_WORDS_PER_TASK = 10;
    private int wordsAmount;
    private boolean firstTry;
    Language languageToLearn;
    Set<Word> wordsForTask;
    Map<Word, Boolean> passedWords;
    Word correctWord;
    DictationType dictationType;
    Mode dictationMode;

    protected DictationType getDictationType(){
        return dictationType;
    }

    @Override
    public Set<Word> getWords() {
        return wordsForTask;
    }

    @Override
    public Map<Word, Boolean> getPassedWords() {
        return passedWords;
    }

    @Override
    public int getWordsAmount() {
        return wordsAmount;
    }

    @Override
    public boolean firstTry(){
        return firstTry;
    }

    @Override
    public void setMaxWordsForTask(int value) {
        MAX_WORDS_PER_TASK = value;
    }

    @Override
    public int getMaxWordsForTask() {
        return MAX_WORDS_PER_TASK;
    }

    @Override
    public Language getAnswerLanguage() {
        return getMode() == Mode.NORMAL ? getCorrectWord().getWordLang() : getCorrectWord().getTranslationLang();
    }

    @Override
    public Language getLanguageToLearn() {
        return languageToLearn;
    }

    @Override
    public Word getCorrectWord() {
        return correctWord;
    }

    @Override
    public String getCorrectAnswer(){
        return getMode() == Mode.NORMAL ?  getCorrectWord().getTranslation().get() : getCorrectWord().getArticle().get() + " " + getCorrectWord().getWord().get();
    }

    @Override
    public String getTaskWord(){
        return getMode() == Mode.NORMAL ?  getCorrectWord().getArticle().get() + " " + getCorrectWord().getWord().get() : getCorrectWord().getTranslation().get();
    }

    @Override
    public Mode getMode() {
        return dictationMode;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.DICTATION;
    }

    Dictation.Mode generateDictationMode(){
        Random rand = new Random();
        return Dictation.Mode.values()[rand.nextInt(Dictation.Mode.values().length)];
    }

    protected void fillArrayWithWords(List<Word> allWords) {
        getWords().clear();
        Random rand = new Random();
        try {
            if (allWords.size() < getMaxWordsForTask()) {
                getWords().addAll(allWords);
            } else {
                for (int i = 0; i < getMaxWordsForTask(); i++) {
                    int randomIndex = rand.nextInt(allWords.size());
                    getWords().add(allWords.get(randomIndex));
                    allWords.remove(randomIndex);
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        wordsAmount = getWords().size();
    }

    private void saveProgress(Word answer, boolean isCorrect){
        if (isCorrect) {
            if (getPassedWords().get(answer) == null) {
                answer.incCorrectAnswerCount();
                WordDAO.getInstance().update(answer);
                getPassedWords().put(answer, true);
                firstTry = true;
            }else{
                firstTry = false;
            }

            getWords().remove(getCorrectWord());
        }
        else{
            if (getPassedWords().get(answer) == null){
                answer.incWrongAnswerCount();
                WordDAO.getInstance().update(answer);
                getPassedWords().put(answer, false);
            }
        }
    }

    @Override
    public Word getNextWord() {
        Random random = new Random();
        Word nextWord;
        int nextWordIndex = random.nextInt(getWords().size());

        if (getWords().size() > 0) {
            if (nextWordIndex == 0) {
                nextWord = getWords().iterator().next();
                correctWord = nextWord;
                return nextWord;
            }

            int counter = 0;
            Iterator<Word> iterator;
            for (iterator = getWords().iterator(); counter < nextWordIndex && iterator.hasNext(); counter ++){
                iterator.next();
            }
            nextWord = iterator.next();
            correctWord = nextWord;
            return nextWord;
        }

        return null;
    }

    @Override
    public boolean check(String text) {
        return check(text, false);
    }

    @Override
    public boolean check(String text, boolean hasArticle){
        if (!hasArticle || getMode() == Mode.NORMAL) {
            if (text.trim().equalsIgnoreCase(getCorrectAnswer().trim())) {
                saveProgress(getCorrectWord(), true);
                return true;
            } else {
                List<Word> analogs = getMode() == Mode.NORMAL ? getCorrectWord().getOtherTranslationVariants() : getCorrectWord().getWordsWithSimilarTranslation();
                for (Word word : analogs) {
                    if (getMode() == Mode.NORMAL) {
                        if (text.trim().equalsIgnoreCase(word.getTranslation().get())) {
                            saveProgress(word, true);
                            return true;
                        }
                    } else {
                        if (text.trim().equalsIgnoreCase(word.getWord().get())) {
                            saveProgress(word, true);
                            return true;
                        }
                    }
                }
            }
        }
        else{
            String article = text.substring(0, text.indexOf(" ")).trim();
            String word = text.substring(text.indexOf(" "), text.length());
            //Reversed mode
            if (article.equalsIgnoreCase(getCorrectWord().getArticle().get()) && word.trim().equalsIgnoreCase(getCorrectAnswer())){
                saveProgress(getCorrectWord(), true);
                return true;
            }
            else{
                for (Word w : getCorrectWord().getWordsWithSimilarTranslation()){
                    if (article.equalsIgnoreCase(w.getArticle().get()) && word.trim().equalsIgnoreCase(w.getWord().get())){
                        saveProgress(w, true);
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
