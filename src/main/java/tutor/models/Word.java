package tutor.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by user on 17.02.2015.
 */
public class Word {
    public Word(){
        this.article = new SimpleStringProperty();
        this.word = new SimpleStringProperty();
        this.translation = new SimpleStringProperty();
    }

    public Word(String word, String translation, Language word_lang, Language translation_lang){
        this.word = new SimpleStringProperty(word);
        this.translation = new SimpleStringProperty(translation);
        setWordLang(word_lang);
    }

    public Word(String article, String word, String translation, Language word_lang, Language translation_lang){
        this.article = new SimpleStringProperty(article);
        this.word = new SimpleStringProperty(word);
        this.translation = new SimpleStringProperty(translation);
    }

    private int id;
    private StringProperty article;
    private StringProperty word;
    private StringProperty translation;
    private Language word_lang;
    private Language translation_lang;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (getArticle() != null ? !getArticle().equals(word1.getArticle()) : word1.getArticle() != null) return false;
        if (!getWord().equals(word1.getWord())) return false;
        if (!getTranslation().equals(word1.getTranslation())) return false;
        if (!word_lang.equals(word1.word_lang)) return false;
        return translation_lang.equals(word1.translation_lang);

    }

    @Override
    public int hashCode() {
        int result = getArticle() != null ? getArticle().hashCode() : 0;
        result = 31 * result + getWord().hashCode();
        result = 31 * result + getTranslation().hashCode();
        result = 31 * result + word_lang.hashCode();
        result = 31 * result + translation_lang.hashCode();
        return result;
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
        return word_lang;
    }

    public void setWordLang(Language lang) {
        this.word_lang = lang;
    }

    public Language getTranslationLang(){
        return translation_lang;
    }

    public void setTranslation_lang(Language lang) {
        this.translation_lang = lang;
    }

}
