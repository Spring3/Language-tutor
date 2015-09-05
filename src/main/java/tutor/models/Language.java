package tutor.models;

/**
 * Created by user on 13.02.2015.
 */
public class Language {

    public Language(){

    }

    public Language (String name){
        setLangName(name);
    }

    public Language(int id, String name){
        setId(id);
        setLangName(name);
    }

    private int id;
    private String langName;
    private String shortName;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String lang_name) {
        this.langName = lang_name;
    }

    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getShortName(){
        return shortName;
    }

    @Override
    public String toString() {
        return getLangName();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (!getLangName().equals(language.getLangName())) return false;
        return getShortName().equals(language.getShortName());

    }

    @Override
    public int hashCode() {
        int result = getLangName().hashCode();
        result = 31 * result + getShortName().hashCode();
        return result;
    }
}
