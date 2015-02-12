package tutor.models;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by user on 11.02.2015.
 */
public class User {

    public User(){

    }

    public User(String userName, int password){
       setUserName(userName);
        setPassword(password);
        setSuccess_rate(0f);
        setDateOfRegistery(Timestamp.valueOf(LocalDateTime.now()));
        setSeed(new Random().nextInt(Integer.MAX_VALUE));
    }

    public User(int id, String username, String email, int password, Timestamp dateOfRegistery, float success_rate, int seed) {
        setId(id);
        setUserName(username);
        setEmail(email);
        setPassword(password);
        setDateOfRegistery(dateOfRegistery);
        setSuccess_rate(success_rate);
        setSeed(seed);
    }

    private int id;
    private String userName;
    private String email;
    private int password;
    private Timestamp dateOfRegistery;
    private float success_rate;
    private int seed;

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public float getSuccess_rate() {
        return success_rate;
    }

    public void setSuccess_rate(float success_rate) {
        this.success_rate = success_rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateOfRegistery() {
        return dateOfRegistery;
    }

    public void setDateOfRegistery(Timestamp dateOfRegistery) {
        this.dateOfRegistery = dateOfRegistery;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
