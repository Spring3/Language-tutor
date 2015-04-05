package tutor.util;

import org.h2.jdbcx.JdbcConnectionPool;

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
    private JdbcConnectionPool connectionPool;

    private DbManager(){
        connectionPool = JdbcConnectionPool.create("jdbc:h2:file:./data/langTutor;MV_STORE=FALSE;MVCC=FALSE", "langtutor", "hearthstone");
        connectionPool.setMaxConnections(10);
    }

    /**
     * Singletone method for retrieving DbManager class instance
     * @return single instance of this class
     */
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

    /**
     * @return a connection instance
     */
    public Connection getConnection() {
        try {
            return connectionPool.getConnection();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Checks database connection
     * @return true if connection was successful
     */
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

    /**
     * Creates and initializes database with default tables and rows
     */
    public void initializeDb(){
        String[] initQueries = new String[]{
                "CREATE TABLE IF NOT EXISTS USERS(id integer IDENTITY PRIMARY KEY , username varchar(40), email varchar(40), password varchar(100), register_date timestamp, success_rate decimal(5,2), seed int);",
                "CREATE TABLE IF NOT EXISTS LANGUAGES(id integer IDENTITY PRIMARY KEY, lang_name varchar(40));",
                "CREATE TABLE IF NOT EXISTS WORD(id integer IDENTITY PRIMARY KEY, word varchar(100), word_translation varchar(100), lang_id integer);",
                "CREATE TABLE IF NOT EXISTS USER_WORD(user_id integer, word_id integer);",
                "CREATE TABLE IF NOT EXISTS USER_LANG(user_id integer, lang_id integer);"
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

    /**
     * Shuts down all the connection to the database
     */
    public void shutdown(){
        connectionPool.dispose();
    }
}
