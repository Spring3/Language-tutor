package tutor.models;

/**
 * Created by user on 17.02.2015.
 */
public class Word {
    public Word(){

    }

    public Word(String word, String translation, Language lang){
        setWord(word);
        setTranslation(translation);
        setLang(lang);
    }

    private int id;
    private String word;
    private String translation;
    private Language lang;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
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

        Word word = (Word) o;
        if (word.getId() != 0)
            if (id != word.id) return false;
        if (lang != null ? !lang.equals(word.lang) : word.lang != null) return false;
        if (translation != null ? !translation.equals(word.translation) : word.translation != null)
            return false;
        if (this.word != null ? !this.word.equals(word.word) : word.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (translation != null ? translation.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        return result;
    }
}
