package tutor.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by user on 17.02.2015.
 */
public class Word {
    public Word(){
        this.article = new SimpleStringProperty();
        this.word = new SimpleStringProperty();
        this.translation = new SimpleStringProperty();
        setAddedDate(new Date(Calendar.getInstance().getTime().getTime()));
    }

    public Word(String word, String translation, Language word_lang, Language translation_lang){
        this.word = new SimpleStringProperty(word);
        this.translation = new SimpleStringProperty(translation);
        setWordLang(word_lang);
        setTranslationLang(translation_lang);
        setAddedDate(new Date(Calendar.getInstance().getTime().getTime()));
    }

    public Word(String article, String word, String translation, Language word_lang, Language translation_lang){
        this.article = new SimpleStringProperty(article);
        this.word = new SimpleStringProperty(word);
        this.translation = new SimpleStringProperty(translation);
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
        return getArticle().get() + " " + getWord().get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (getArticle() != null ? !getArticle().equals(word1.getArticle()) : word1.getArticle() != null) return false;
        if (!getWord().equals(word1.getWord())) return false;
        if (!getTranslation().equals(word1.getTranslation())) return false;
        if (!getWordLang().equals(word1.getWordLang())) return false;
        return (!getTranslationLang().equals(word1.getTranslationLang()));

    }

    @Override
    public int hashCode() {
        int result = getArticle() != null ? getArticle().hashCode() : 0;
        result = 31 * result + getWord().hashCode();
        result = 31 * result + getTranslation().hashCode();
        result = 31 * result + getWordLang().hashCode();
        result = 31 * result + getTranslationLang().hashCode();
        return result;
    }
}
