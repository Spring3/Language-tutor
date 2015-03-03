package tutor.models;

/**
 * Created by user on 03.03.2015.
 */
public class UserInfo {

    public UserInfo(){

    }

    public UserInfo(String google_code, User owner){
        setGoogle_code(google_code);
        setOwner(owner);
    }

    private int id;
    private String google_code;
    private User owner;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getGoogle_code() {
        return google_code;
    }

    public void setGoogle_code(String google_code) {
        this.google_code = google_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
