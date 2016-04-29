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
                "CREATE TABLE IF NOT EXISTS LANGUAGES(id integer IDENTITY PRIMARY KEY, lang_name varchar(40), short_name varchar(8));",
                "CREATE TABLE IF NOT EXISTS USERS(id integer IDENTITY PRIMARY KEY , username varchar(40), email varchar(40), password varchar(100), register_date timestamp, success_rate decimal(5,2), seed int, lang_id integer, FOREIGN KEY (lang_id) REFERENCES LANGUAGES(id));",
                "CREATE TABLE IF NOT EXISTS WORD(id integer IDENTITY PRIMARY KEY, article varchar(10), word varchar(100), word_translation varchar(100), lang_id integer, translation_id integer, whenAdded Date, wrongAnswers integer, correctAnswers integer, FOREIGN KEY(lang_id) REFERENCES LANGUAGES(id), FOREIGN KEY(translation_id) REFERENCES LANGUAGES(id));",
                "CREATE TABLE IF NOT EXISTS USER_WORD(user_id integer, word_id integer, FOREIGN KEY (user_id) REFERENCES USERS(id), FOREIGN KEY (word_id) REFERENCES WORD(id));",
                "CREATE TABLE IF NOT EXISTS USER_LANG(user_id integer, lang_id integer, FOREIGN KEY (user_id) REFERENCES USERS(id), FOREIGN KEY (lang_id) REFERENCES LANGUAGES(id));",
                "CREATE TABLE IF NOT EXISTS USER_STATS(id integer IDENTITY PRIMARY KEY , user_id integer, task_type varchar(20), lang_id integer, rate double, tries integer, FOREIGN KEY(user_id) REFERENCES USERS(id), FOREIGN KEY(lang_id) REFERENCES LANGUAGES(id));",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Afrikaans', 'af');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Albanian', 'sq');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Arabic', 'ar');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Armenian', 'hy');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Azerbaijani', 'az');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Basque', 'eu');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Belarusian', 'be');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Bengali', 'bn');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Bosnian', 'bs');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Bulgarian', 'bg');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Catalan', 'ca');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Cebuano', 'ceb');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Chichewa', 'ny');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Chineese', 'zh-CN');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Croatian', 'hr');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Czech', 'cs');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Danish', 'da');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Dutch', 'nl');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('English', 'en');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Esperanto', 'eo');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Estonian', 'et');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Filipino', 'tl');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Finnish', 'fi');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('French', 'fr');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Galician', 'gl');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Georgian', 'ka');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('German', 'de');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Greek', 'el');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Gujarati', 'gu');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Haitian Creole', 'ht');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Hausa', 'ha');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Hebrew', 'iw');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Hindi', 'hi');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Hmong', 'hmn');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Hungarian', 'hu');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Icelandic', 'is');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Igbo', 'ig');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Indonesian', 'id');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Irish', 'ga');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Italian', 'it');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Japanese', 'ja');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Javanese', 'jw');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Kannada', 'kn');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Kazakh', 'kk');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Khmer', 'km');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Korean', 'ko');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Lao', 'lo');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Latin', 'la');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Latvian', 'lv');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Lithuanian', 'lt');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Macedonian', 'mk');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Malagasy', 'mg');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Malay', 'ms');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Malayalam', 'ml');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Maltese', 'mt');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Maori', 'mi');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Marathi', 'mr');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Mongolian', 'mn');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Myanmar', 'my');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Nepali', 'ne');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Norwegian', 'no');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Persian', 'fa');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Polish', 'pl');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Portuguese', 'pt');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Punjabi', 'ma');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Romanian', 'ro');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Russian', 'ru');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Serbian', 'sr');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Sesotho', 'st');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Sinhala', 'si');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Slovak', 'sk');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Slovenian', 'sl');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Somali', 'so');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Spanish', 'es');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Sundanese', 'su');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Swahili', 'sw');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Swedish', 'sv');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Tajik', 'tg');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Tamil', 'ta');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Telugu', 'te');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Thai', 'th');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Turkish', 'tr');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Ukrainian', 'uk');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Urdu', 'ur');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Uzbek', 'uz');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Vietnamese', 'vi');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Welsh', 'cy');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Yiddish', 'yi');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Yoruba', 'yo');",
                "INSERT INTO LANGUAGES (lang_name, short_name) VALUES ('Zulu', 'zu');"
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
