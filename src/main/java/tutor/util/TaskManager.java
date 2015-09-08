package tutor.util;

import tutor.controllers.AuthController;
import tutor.controllers.Controller;
import tutor.dao.StatsDAO;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Stats;
import tutor.models.Word;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by Spring on 8/30/2015.
 */
public class TaskManager {
    public TaskManager(Language langToLearn){
        languageToLearn = langToLearn;
        wordsForTask = new HashSet<>();
        passedWords = new HashMap<>();
    }

    private static final int MAX_WORDS_PER_TASK = 10;
    private Language languageToLearn;
    private Set<Word> wordsForTask;
    private TaskManagerMode mode;
    private DictationMode dictationMode;

    private int wordIndex;
    private int wordsAmount;
    private int mistakes;
    private int correctAnswers;
    private String correctAnswer;
    private Map<Word, Boolean> passedWords;
    private Language answerWordLanguage;
    private Word correctAnswerWord;

    public TaskManagerMode getMode(){
        return mode;
    }

    public DictationMode getDictationMode() {return dictationMode;}


    public enum TaskManagerMode{
        NORMAL,
        REPETITION,
        LEARNING,
        TRADITIONAL
    }

    public enum DictationMode{
        NORMAL,
        REVERSED

    }

    public Set<Word> getWords(){
        return wordsForTask;
    }

    public int getWordsAmount(){
        return wordsAmount;
    }

    public int getMistakesCount(){
        return mistakes;
    }

    public String getCorrectAnswer(){
        return correctAnswer;
    }

    public int getCorrectAnswersCount(){
        return correctAnswers;
    }

    public Word getCorrectAnswerWord(){
        return correctAnswerWord;
    }

    public void incrementMistakesCounter(){
        mistakes ++;
    }

    public void incrementCorrectAnswersCount(){
        correctAnswers ++;
    }

    public int getMaxWordsPerTask(){
        return MAX_WORDS_PER_TASK;
    }

    public Language getAnswerWordLanguage(){
        return answerWordLanguage;
    }

    public float getSuccessRate() {
        if (correctAnswers != 0) {
            if (mistakes != 0) {
                return (float) correctAnswers / getWordsAmount() * 70 + (float) correctAnswers / mistakes + (float)(21 - mistakes / getWordsAmount() * 21);
            } else {
                return (float) correctAnswers / getWordsAmount() * 70 + 30;
            }
        } else {
            return (float) correctAnswers / getWordsAmount() * 70 + 15 + (float) (15 - mistakes / getWordsAmount() * 15);
        }
    }

    public void endTask(){
        for(Map.Entry<Word,Boolean> entry : passedWords.entrySet()){
            WordDAO.getInstance().update(entry.getKey());
        }

        Stats statistics = new Stats(AuthController.getActiveUser(), getMode(), Controller.selectedLanguage, getSuccessRate());
        StatsDAO.getInstance().create(statistics);
    }

    public String getNextTaskWord(){
        Random random = new Random();
        wordIndex = random.nextInt(getWords().size());
        int count = 0;
        if (!getDictationMode().equals(TaskManager.DictationMode.REVERSED)){
            while (getWords().size() > 1 && get(wordIndex).toString().equals(correctAnswer) && count < 6) {
                wordIndex = random.nextInt(getWords().size());
                count ++;
            }

            correctAnswerWord = get(wordIndex);
            correctAnswer = correctAnswerWord.toString();
            answerWordLanguage = correctAnswerWord.getWordLang();


        }
        else{
            while (getWords().size() > 1 && get(wordIndex).getTranslation().get().equals(correctAnswer) && count < 6){
                wordIndex = random.nextInt(getWords().size());
                count ++;
            }
            correctAnswerWord = get(wordIndex);
            correctAnswer = correctAnswerWord.getTranslation().get();
            answerWordLanguage = correctAnswerWord.getTranslationLang();


        }
        return correctAnswer;
    }

    public boolean confirmAnswer(String confirmedAnswer) {

        if (getDictationMode().equals(TaskManager.DictationMode.REVERSED)) {
            if (!get(wordIndex).getArticle().get().isEmpty()) {
                return checkAnswer(confirmedAnswer, true, true);
            } else {
                return checkAnswer(confirmedAnswer, false, true);
            }
        } else {
            return checkAnswer(confirmedAnswer, false, false);
        }
    }

    private boolean checkAnswer( String enteredText, boolean hasArticle, boolean reversed){
        Word taskWord = get(wordIndex);
        if (reversed){
            return checkAnswerForReversedTest(enteredText, hasArticle, taskWord);
        }
        else{
            return checkAnswerForNormalTest(enteredText, taskWord);

        }
    }

    private boolean checkAnswerForReversedTest(String enteredText, boolean hasArticle, Word taskWord){
        if (hasArticle && !enteredText.isEmpty()){
            String article = enteredText.substring(0, enteredText.indexOf(" ")).trim();
            String word = enteredText.substring(enteredText.indexOf(" "), enteredText.length());
            if(article.equalsIgnoreCase(taskWord.getArticle().get()) && word.trim().equalsIgnoreCase(taskWord.getWord().get()))
                return rememberAnswerResult( true, taskWord);
            else{
                for(Word synonym : taskWord.getWordsWithSimilarTranslation()){
                    if (article.equalsIgnoreCase(synonym.getArticle().get()) && word.trim().equalsIgnoreCase(synonym.getWord().get())) {
                        return rememberAnswerResult( true, taskWord);
                    }
                }
                return rememberAnswerResult(false, taskWord);
            }
        }
        else {
            correctAnswer = taskWord.getWord().get();
            if (!hasArticle && enteredText.trim().equalsIgnoreCase(correctAnswer))
                return rememberAnswerResult( true, taskWord);
            else{
                for(Word word : taskWord.getWordsWithSimilarTranslation()){
                    if (enteredText.trim().equalsIgnoreCase(word.toString())){
                        return rememberAnswerResult( true, taskWord);
                    }
                }
                return rememberAnswerResult(false, taskWord);
            }
        }
    }

    private boolean checkAnswerForNormalTest(String enteredText, Word taskWord){
        correctAnswer = taskWord.getTranslation().get();
        if (enteredText.trim().equalsIgnoreCase(correctAnswer)){
            return rememberAnswerResult(true, taskWord);
        }
        else{
            for(Word word : taskWord.getOtherTranslationVariants()){
                if (enteredText.trim().equalsIgnoreCase(word.getTranslation().get())){
                    return rememberAnswerResult(true, taskWord);
                }
            }
            return rememberAnswerResult(false, taskWord);
        }
    }

    private boolean rememberAnswerResult( boolean isCorrect, Word answer){
        if (isCorrect){
            if (passedWords.get(answer) == null){
                incrementCorrectAnswersCount();
                answer.incCorrectAnswerCount();
                WordDAO.getInstance().update(answer);
                passedWords.put(answer, true);
            }
            else{
                if (passedWords.get(answer)){
                    incrementCorrectAnswersCount();
                }
            }
            getWords().remove(get(wordIndex));
            return true;
        }
        else
        {
            if (passedWords.get(answer) == null){
                answer.incWrongAnswerCount();
                WordDAO.getInstance().update(answer);
                passedWords.put(answer, false);
                incrementMistakesCounter();
            }
            else{
                if (passedWords.get(answer))
                    incrementMistakesCounter();
            }
            return false;
        }
    }

    public Word get(int index){
        if (index == 0){
            return getWords().iterator().next();
        }
        int counter = 0;
        if (getWords().size() > 0){
            Iterator<Word> iterator;
            for ( iterator = getWords().iterator(); counter < index && iterator.hasNext(); counter ++){
                iterator.next();
            }
            return iterator.next();
        }
        throw new IndexOutOfBoundsException("Index out of bounds.");
    }

    public Set<Word> createTask(){
        Random random = new Random();
        if (checkInternetConnection())
            mode = TaskManagerMode.values()[random.nextInt(4)];
        else
            mode = TaskManagerMode.values()[random.nextInt(3)];
        dictationMode = DictationMode.values()[random.nextInt(2)];
        switch (mode){
            case NORMAL:{
                createNormalTask();
                break;
            }
            case REPETITION:{
                createRepetitionTask();
                break;
            }
            case LEARNING:{
                createLearningTask();
                break;
            }
            default:{
                createNormalTask();
                break;
            }
        }
        return wordsForTask;
    }

    private boolean checkInternetConnection(){
        try{
            URL url = new URL("http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200)
                return true;
            return false;
        }
        catch (Exception ex){
            return false;
        }
    }

    private void createNormalTask(){
        List<Word> allWords = WordDAO.getInstance().readAllByLangForActiveUser(languageToLearn);
        fillArrayWithWords(allWords);
    }

    private void createRepetitionTask(){
        List<Word> wordsToRepeat = WordDAO.getInstance().readAllWordsToRepeatFor(languageToLearn);
        fillArrayWithWords(wordsToRepeat);
    }

    private void createLearningTask(){
        List<Word> newWords = WordDAO.getInstance().readAllNewlyAddedWordsFor(languageToLearn);
        fillArrayWithWords(newWords);
    }

    private void fillArrayWithWords(List<Word> allWords) {
        wordsForTask.clear();
        Random rand = new Random();
        try {
            if (allWords.size() < MAX_WORDS_PER_TASK) {
                wordsForTask.addAll(allWords);
                allWords = WordDAO.getInstance().readAllByLangForActiveUser(languageToLearn);

                for (int i = wordsForTask.size(); i < MAX_WORDS_PER_TASK; i++) {
                    if (allWords.size() == 0) {
                        break;
                    }
                    int randomIndex = rand.nextInt(allWords.size());
                    wordsForTask.add(allWords.get(randomIndex));
                    allWords.remove(randomIndex);
                }
            } else {
                for (int i = 0; i < MAX_WORDS_PER_TASK; i++) {
                    int randomIndex = rand.nextInt(allWords.size());
                    wordsForTask.add(allWords.get(randomIndex));
                    allWords.remove(randomIndex);
                }
            }
        } catch (NullPointerException ex) {
            createNormalTask();
        }
        finally {
            wordsAmount = wordsForTask.size();
        }

    }
}
