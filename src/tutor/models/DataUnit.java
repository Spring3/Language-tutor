package tutor.models;

/**
 * Created by user on 17.02.2015.
 */
public class DataUnit {
    public DataUnit(){

    }

    public DataUnit(String word, String translation, Language lang, DataSource dataSrc){
        setWord(word);
        setTranslation(translation);
        setLang(lang);
        setDataSrc(dataSrc);
    }

    private int id;
    private String word;
    private String translation;
    private Language lang;
    private DataSource dataSrc;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public DataSource getDataSrc(){
        return dataSrc;
    }

    public void setDataSrc(DataSource src){
        dataSrc = src;
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

}
