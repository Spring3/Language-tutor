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
        setTranslationLang(translation_lang);
    }

    public Word(String article, String word, String translation, Language word_lang, Language translation_lang){
        this.article = new SimpleStringProperty(article);
        this.word = new SimpleStringProperty(word);
        this.translation = new SimpleStringProperty(translation);
        setWordLang(word_lang);
        setTranslationLang(translation_lang);
    }

    private int id;
    private StringProperty article;
    private StringProperty word;
    private StringProperty translation;
    private Language wordLang;
    private Language translationLang;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (getArticle() != null ? !getArticle().equals(word1.getArticle()) : word1.getArticle() != null) return false;
        if (!getWord().equals(word1.getWord())) return false;
        if (!getTranslation().equals(word1.getTranslation())) return false;
        if (!wordLang.equals(word1.wordLang)) return false;
        return translationLang.equals(word1.translationLang);

    }

    @Override
    public int hashCode() {
        int result = getArticle() != null ? getArticle().hashCode() : 0;
        result = 31 * result + getWord().hashCode();
        result = 31 * result + getTranslation().hashCode();
        result = 31 * result + wordLang.hashCode();
        result = 31 * result + translationLang.hashCode();
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
}
