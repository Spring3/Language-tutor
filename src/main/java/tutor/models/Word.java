package tutor.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import tutor.dao.WordDAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by user on 17.02.2015.
 */
public class Word {
    public Word(){
        this.article = new SimpleStringProperty("");
        this.word = new SimpleStringProperty("");
        this.translation = new SimpleStringProperty("");
        setAddedDate(new Date(Calendar.getInstance().getTime().getTime()));
    }

    public Word(String word, String translation, Language word_lang, Language translation_lang){
        this.article = new SimpleStringProperty("");
        this.word = new SimpleStringProperty(removeTrashFromString(word));
        this.translation = new SimpleStringProperty(removeTrashFromString(translation));
        setWordLang(word_lang);
        setTranslationLang(translation_lang);
        setAddedDate(new Date(Calendar.getInstance().getTime().getTime()));
    }

    public Word(String article, String word, String translation, Language word_lang, Language translation_lang){
        this.article = new SimpleStringProperty(removeTrashFromString(article));
        this.word = new SimpleStringProperty(removeTrashFromString(word));
        this.translation = new SimpleStringProperty(removeTrashFromString(translation));
        setWordLang(word_lang);
        setTranslationLang(translation_lang);
        setAddedDate(new Date(Calendar.getInstance().getTime().getTime()));
    }

    private int id;
    private StringProperty article;
    private StringProperty word;
    private StringProperty translation;
    private Language wordLang;
    private Language translationLang;
    private Date addedDate;
    private int wrongAnswerCount;
    private int correctAnswerCount;
    private List<Word> otherTranslationVariants;
    private List<Word> otherWordsWithSameTranslation;

    public void setAddedDate(Date date){
        addedDate = date;
    }

    public Date getAddedDate(){
        return addedDate;
    }

    public int getWrongAnswerCount(){
        return wrongAnswerCount;
    }

    public void setWrongAnswerCount(int count)
    {
        wrongAnswerCount = count;
    }

    public int getCorrectAnswerCount(){
        return correctAnswerCount;
    }

    public void setCorrectAnswerCount(int count){
        correctAnswerCount = count;
    }

    public void incWrongAnswerCount(){
        wrongAnswerCount ++;
    }

    public void incCorrectAnswerCount(){
        correctAnswerCount ++;
    }

    public List<Word> getOtherTranslationVariants(){
        if (otherTranslationVariants == null){
            otherTranslationVariants = WordDAO.getInstance().readOtherTranslationVariantsFor(this);
        }
        return otherTranslationVariants;
    }

    public List<Word> getWordsWithSimilarTranslation(){
        if (otherWordsWithSameTranslation == null){
            otherWordsWithSameTranslation = WordDAO.getInstance().readWordsWithSimilarTranslationFor(this);
        }
        return otherWordsWithSameTranslation;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public StringProperty getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public StringProperty getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation.set(translation);
    }

    public StringProperty getArticle(){
        return article;
    }

    public void setArticle(String article){
        this.article.set(article);
    }

    public Language getWordLang() {
        return wordLang;
    }

    public void setWordLang(Language lang) {
        this.wordLang = lang;
    }

    public Language getTranslationLang(){
        return translationLang;
    }

    public void setTranslationLang(Language lang) {
        this.translationLang = lang;
    }

    @Override
    public String toString() {
        return (getArticle().get() + " " + getWord().get()).trim();
    }


    private String removeTrashFromString(String string){
        String[] buffer = string.split("");
        try {
            if (buffer[0].charAt(0) == ('\ufeff')) {
                String[] tempBuffer = new String[buffer.length - 1];
                System.arraycopy(buffer, 1, tempBuffer, 0, tempBuffer.length);
                StringBuilder builder = new StringBuilder();
                for (String s : tempBuffer) {
                    builder.append(s);
                }
                return builder.toString();
            } else {
                return string;
            }
        }
        catch (StringIndexOutOfBoundsException ex){

        }
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (getId() != word1.getId()) return false;
        if (!getArticle().get().equals(word1.getArticle().get())) return false;
        if (!getWord().get().equals(word1.getWord().get())) return false;
        if (!getTranslation().get().equals(word1.getTranslation().get())) return false;
        if (!getWordLang().equals(word1.getWordLang())) return false;
        return getTranslationLang().equals(word1.getTranslationLang());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getArticle().get().hashCode();
        result = 31 * result + getWord().get().hashCode();
        result = 31 * result + getTranslation().get().hashCode();
        result = 31 * result + getWordLang().hashCode();
        result = 31 * result + getTranslationLang().hashCode();
        return result;
    }
}
