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

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:file:./data/langTutor;MV_STORE=FALSE;MVCC=FALSE", "langtutor", "hearthstone");
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public boolean checkConnection(){
        if (!new File("./data/langTutor.h2.db").exists())
            initializeDb();
        Connection connection = getConnection();
        if (connection != null) {
            try {
                Statement testStatement = connection.createStatement();
                testStatement.executeQuery("select * from USERS WHERE id=1");
                connection.close();
                return true;
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        try {
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public void initializeDb(){
        String[] initQueries = new String[]{
                "CREATE TABLE IF NOT EXISTS USERS(id integer IDENTITY PRIMARY KEY , username varchar(40), email varchar(40), password varchar(100), register_date timestamp, success_rate decimal(5,2), seed int);",
                "CREATE TABLE IF NOT EXISTS LANGUAGES(id integer IDENTITY PRIMARY KEY, lang_name varchar(40), user_id integer);",
                "CREATE TABLE IF NOT EXISTS DATA_SRC(id integer IDENTITY PRIMARY KEY, link varchar(100), type varchar(20), service varchar(20), language_id integer);",
                "CREATE TABLE IF NOT EXISTS DATA_UNIT(id integer IDENTITY PRIMARY KEY, word varchar(100), word_translation varchar(100), lang_id integer, data_src_id integer);",
                "CREATE TABLE IF NOT EXISTS USER_INFO(id integer IDENTITY PRIMARY KEY , google_code varchar(80), user_id integer);"
        };
        Connection connection = getConnection();
        try {
            for (int i = 0; i < initQueries.length; i++) {

                connection.createStatement().execute(initQueries[i]);

            }
            connection.close();
        }

        catch(SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Db initialized");

    }
}
