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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataUnit dataUnit = (DataUnit) o;
        if (dataUnit.getId() != 0)
            if (id != dataUnit.id) return false;
        if (dataSrc != null ? !dataSrc.equals(dataUnit.dataSrc) : dataUnit.dataSrc != null) return false;
        if (lang != null ? !lang.equals(dataUnit.lang) : dataUnit.lang != null) return false;
        if (translation != null ? !translation.equals(dataUnit.translation) : dataUnit.translation != null)
            return false;
        if (word != null ? !word.equals(dataUnit.word) : dataUnit.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (translation != null ? translation.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (dataSrc != null ? dataSrc.hashCode() : 0);
        return result;
    }
}
