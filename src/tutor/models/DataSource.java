package tutor.models;

import javafx.beans.property.SimpleStringProperty;
import tutor.util.DataSourceType;
import tutor.util.Service;

/**
 * Created by user on 17.02.2015.
 */
public class DataSource implements DataSourceType, Service {
    public DataSource(){

    }

    public DataSource(String link, String type, String service, Language language){
        setLink(link);
        setType(type);
        setService(service);
        setLanguage(language);
    }

    private int id;
    private SimpleStringProperty link = new SimpleStringProperty();
    private SimpleStringProperty type = new SimpleStringProperty();
    private String service;
    private Language language;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link.get();
    }

    public void setLink(String link) {
        this.link.set(link);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSource that = (DataSource) o;
        if (id != that.id) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (service != that.service) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (service != null ? service.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
//"CREATE TABLE IF NOT EXISTS DATA_SRC(id integer IDENTITY PRIMARY KEY, link varchar(100), type varchar(50), service varchar(50), credentials_id integer, language_id integer);",
}