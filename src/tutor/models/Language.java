package tutor.models;

/**
 * Created by user on 13.02.2015.
 */
public class Language {

    public Language(){

    }

    public Language (String name, User owner){
        setLang_name(name);
        setOwner(owner);
    }

    public Language(int id, String name, User user){
        setId(id);
        setLang_name(name);
        setOwner(user);
    }

    private int id;

    private String lang_name;

    private User owner;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (lang_name != null ? !lang_name.equals(language.lang_name) : language.lang_name != null) return false;
        if (owner != null ? !owner.equals(language.owner) : language.owner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (lang_name != null ? lang_name.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getLang_name();
    }
}
