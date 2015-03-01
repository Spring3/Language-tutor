package tutor.util;

import com.sun.istack.internal.NotNull;
import javafx.scene.control.Alert;
import tutor.dao.DataSourceDAO;
import tutor.dao.DataUnitDAO;
import tutor.models.DataSource;
import tutor.models.DataUnit;
import tutor.models.Language;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

/**
 * Created by user on 17.02.2015.
 */
public class DataSourceManager {

    private DataSourceManager(ResourceBundle bundle){
        this.bundle = bundle;
    }

    private static DataSourceManager instance;
    private ResourceBundle bundle;

    public static enum ContentType {
        UNKNOWN,
        WORDS_TRANSLATION,
        WORDS_ONLY,
        TRANSLATION_ONLY
    }

    public static DataSourceManager getInstance(ResourceBundle bundle){
        if (instance == null){
            synchronized (DataSourceManager.class){
                if (instance == null){
                    instance = new DataSourceManager(bundle);
                }
            }
        }
        return instance;
    }

    public void parse(File dataFile, ContentType contentType, @NotNull DataSource dataSource){
        Language dataLanguage =  dataSource.getLanguage();
        try {
            final DataSource finalDataSource;
            //Opening selected file
            BufferedReader fileReader = new BufferedReader(new FileReader(dataFile));
            //Creating a datasource of type FILE for service OS

            List<DataSource> allDataSourcesForSelectedLanguage = new DataSourceDAO().readAllByLanguage(dataLanguage);
            //Checking whether there is already such data source
            boolean isDuplicate = allDataSourcesForSelectedLanguage.stream().anyMatch((a) -> a.equals(dataSource));

            //if not
            if (!isDuplicate) {
                //saving our new datasource to the database
                DataSource tempDataSource = null;
                new DataSourceDAO().create(dataSource);
                //replacing existing currentDataSource with the one from database to get id
                allDataSourcesForSelectedLanguage = new DataSourceDAO().readAllByLanguage(dataLanguage);
                for (DataSource source : allDataSourcesForSelectedLanguage){
                    if (source.getLink().equals(dataSource.getLink())){
                        tempDataSource = source;
                        break;
                    }
                }
                finalDataSource = tempDataSource;
                parseWordFile(fileReader, contentType, dataLanguage, finalDataSource);
            } else {
                System.err.println("Datasource: "  + dataSource.getLink() + " had already been added.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_TITLE));
                alert.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_DATASOURCE_ALREADY_ADDED));
                alert.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_DATASOURCE_MESSAGE));
                alert.showAndWait();

            }
            //TODO: Check the whole file first. In case the data format is wrong, show an appropriate message
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void parse(InputStream stream, ContentType contentType, @NotNull DataSource dataSource){
        Language language = dataSource.getLanguage();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        if (!dataSource.getType().equals(DataSourceType.GDRIVE_SPREADSHEET) && !dataSource.getType().equals(DataSourceType.GDRIVE_SPREADSHEET) && !dataSource.getType().equals(DataSourceType.LOCAL_DB)){
            parseWordFile(reader, contentType, language, dataSource);
        }

    }

    private void parseWordFile(BufferedReader fileReader, ContentType contentType, Language dataLanguage, DataSource finalDataSource){
        if (contentType == ContentType.WORDS_ONLY) {
            fileReader.lines().forEach(line -> new DataUnitDAO()
                    .create(
                            new DataUnit(
                                    line.trim(),
                                    "",
                                    dataLanguage,
                                    finalDataSource)
                    ));

        } else if (contentType == ContentType.TRANSLATION_ONLY) {
            fileReader.lines().forEach(line -> new DataUnitDAO()
                    .create(
                            new DataUnit(
                                    "",
                                    line.trim(),
                                    dataLanguage,
                                    finalDataSource)
                    ));

        } else if (contentType == ContentType.WORDS_TRANSLATION) {
            fileReader.lines().forEach((s) -> new DataUnitDAO()
                    .create(
                            new DataUnit(
                                    s.substring(0, s.indexOf('=')).trim(),
                                    s.substring(s.indexOf('=') + 1, s.length()).trim(),
                                    dataLanguage,
                                    finalDataSource)
                    ));

        }
    }


}
