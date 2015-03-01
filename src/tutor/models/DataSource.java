package tutor.models;

import javafx.beans.property.SimpleObjectProperty;
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
    private String type;
    private String service;
    private SimpleObjectProperty<Language> language = new SimpleObjectProperty<>();

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
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Language getLanguage() {
        return language.get();
    }

    public void setLanguage(Language language) {
        this.language.set(language);
    }

    @Override
    public String toString() {
        if (getType() == DataSourceType.LOCAL_FILE) {
            return getLink().substring(getLink().lastIndexOf('/') + 1, getLink().lastIndexOf('.'));
        }
        else if (getType() == DataSourceType.GDRIVE_WORKSHEET || getType() == DataSourceType.GDRIVE_SPREADSHEET){
            return getLink();
        }
        return getLink();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSource that = (DataSource) o;

        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (service != null ? !service.equals(that.service) : that.service != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

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
}
