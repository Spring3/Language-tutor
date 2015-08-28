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
                "CREATE TABLE IF NOT EXISTS LANGUAGES(id integer IDENTITY PRIMARY KEY, lang_name varchar(40));",
                "CREATE TABLE IF NOT EXISTS USERS(id integer IDENTITY PRIMARY KEY , username varchar(40), email varchar(40), password varchar(100), register_date timestamp, success_rate decimal(5,2), seed int, lang_id integer, FOREIGN KEY (lang_id) REFERENCES LANGUAGES(id));",
                "CREATE TABLE IF NOT EXISTS WORD(id integer IDENTITY PRIMARY KEY, article varchar(10), word varchar(100), word_translation varchar(100), lang_id integer, translation_id integer, FOREIGN KEY(lang_id) REFERENCES LANGUAGES(id), FOREIGN KEY(translation_id) REFERENCES LANGUAGES(id));",
                "CREATE TABLE IF NOT EXISTS USER_WORD(user_id integer, word_id integer, FOREIGN KEY (user_id) REFERENCES USERS(id), FOREIGN KEY (word_id) REFERENCES WORD(id));",
                "CREATE TABLE IF NOT EXISTS USER_LANG(user_id integer, lang_id integer, FOREIGN KEY (user_id) REFERENCES USERS(id), FOREIGN KEY (lang_id) REFERENCES LANGUAGES(id));",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Afrikaans');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Albanian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Arabic');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Armenian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Azerbaijani');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Basque');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Belarusian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Bengali');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Bosnian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Bulgarian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Catalan');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Cebuano');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Chichewa');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Chineese');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Croatian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Czech');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Danish');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Dutch');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('English');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Esperanto');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Estonian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Filipino');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Finnish');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('French');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Galician');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Georgian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('German');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Greek');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Gujarati');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Haitian Creole');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Hausa');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Hebrew');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Hindi');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Hmong');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Hungarian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Icelandic');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Igbo');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Indonesian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Irish');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Italian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Japanese');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Javanese');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Kannada');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Kazakh');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Khmer');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Korean');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Lao');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Latin');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Latvian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Lithuanian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Macedonian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Malagasy');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Malay');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Malayalam');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Maltese');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Maori');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Marathi');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Mongolian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Myanmar');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Nepali');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Norwegian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Persian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Polish');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Portuguese');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Punjabi');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Romanian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Russian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Serbian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Sesotho');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Sinhala');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Slovak');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Slovenian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Somali');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Spanish');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Sundanese');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Swahili');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Swedish');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Tajik');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Tamil');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Telugu');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Thai');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Turkish');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Ukrainian');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Urdu');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Uzbek');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Vietnamese');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Welsh');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Yiddish');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Yoruba');",
                "INSERT INTO LANGUAGES (lang_name) VALUES ('Zulu');"
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
