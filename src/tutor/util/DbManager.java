package tutor.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Created by user on 10.02.2015.
 */
public class DbManager {

    private static DbManager instance;

    private DbManager(){
        try {
            Class.forName("org.h2.Driver");
        }
        catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public static DbManager getInstance(){
        if (instance == null){
            synchronized (DbManager.class){
                if (instance == null){
                    instance = new DbManager();
                }
            }
        }
        return instance;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:file:./data/langTutor", "langtutor", "hearthstone");
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public boolean checkConnection(){
        if (!new File("/data/langTutor.mv.db").exists())
            initializeDb();
        Connection connection = getConnection();
        if (connection != null) {
            try {
                Statement testStatement = connection.createStatement();
                testStatement.executeQuery("select * from USERS ");
                return true;
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return false;
    }

    public void initializeDb(){
        String[] initQueries = new String[]{
                "CREATE TABLE IF NOT EXISTS USERS(id bigint auto_increment, nickname varchar(40), email varchar(40), password varchar(100), register_date timestamp, success_rate decimal(5,2));",
                "CREATE TABLE IF NOT EXISTS LANGUAGES(id bigint auto_increment, lang_name varchar(40), isNative boolean, user_id integer);",
                "CREATE TABLE IF NOT EXISTS DICTIONARY(id bigint auto_increment, word varchar(100), word_translation varchar(100), lang_id integer);",
                "CREATE TABLE IF NOT EXISTS DATA_SOURCE(id bigint auto_increment, link varchar(100), type varchar(50), service varchar(50), credentials_id integer, language_id integer);",
                "CREATE TABLE IF NOT EXISTS USER_INFO(id bigint auto_increment, user_id integer, username varchar(40), email varchar(40), password varchar(40));"
        };
        Connection connection = getConnection();
        for (int i = 0; i < initQueries.length; i++) {
            try {
                connection.createStatement().execute(initQueries[i]);
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }

    }
}
