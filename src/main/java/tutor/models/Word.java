package tutor.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by user on 17.02.2015.
 */
public class Word {
    public Word(){
        this.word = new SimpleStringProperty();
        this.translation = new SimpleStringProperty();
    }

    public Word(String word, String translation, Language lang){
        this.word = new SimpleStringProperty(word);
        this.translation = new SimpleStringProperty(translation);
        setLang(lang);
    }

    private int id;
    private StringProperty word;
    private StringProperty translation;
    private Language lang;

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

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (!word.equals(word1.word)) return false;
        if (translation != null ? !translation.equals(word1.translation) : word1.translation != null) return false;
        return lang.equals(word1.lang);

    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + (translation != null ? translation.hashCode() : 0);
        result = 31 * result + lang.hashCode();
        return result;
    }
}
