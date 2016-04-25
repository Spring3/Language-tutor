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
        NORMAL,      // random words
        REPEATING,   // repeat words, where made mistakes
        LEARNING;     // learn most recently added words

        public static DictationType random(){
            Random rand = new Random();
            return values()[rand.nextInt(values().length)];
        }
    }

    private static int MAX_WORDS_PER_TASK = 10;
    private int wordsAmount;
    private boolean firstTry;
    private Word correctWord;
    Language languageToLearn;
    Set<Word> wordsForTask;
    Map<Word, Boolean> passedWords;
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
        return getMode() == Mode.NORMAL ? getCorrectWord().getTranslationLang() : getCorrectWord().getWordLang();
    }

    @Override
    public Language getTaskWordLanguage(){
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

    void fillArrayWithWords(List<Word> allWords) {
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

        int maxtries = 6;

        do {
            int nextWordIndex = random.nextInt(getWords().size());
            nextWord = get(nextWordIndex);
            maxtries --;
            if (getCorrectWord() == null)
                break;
        } while((nextWord.equals(getCorrectWord()) && getWords().size() > 1) || maxtries > 0);

        correctWord = nextWord;
        return nextWord;
    }

    private Word get(int index){

        Word nextWord = null;
        if (getWords().size() > 0) {
            if (index == 0) {
                nextWord = getWords().iterator().next();
                return nextWord;
            }

            int counter = 0;
            Iterator<Word> iterator;
            for (iterator = getWords().iterator(); counter < index && iterator.hasNext(); counter ++){
                iterator.next();
            }
            nextWord = iterator.next();
        }
        return nextWord;
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
        saveProgress(getCorrectWord(), false);
        return false;
    }
}
