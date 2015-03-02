package tutor.util;

import javafx.scene.control.Alert;
import tutor.dao.DataSourceDAO;
import tutor.dao.DataUnitDAO;
import tutor.models.DataSource;
import tutor.models.DataUnit;
import tutor.models.Language;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public abstract class AbstractParser implements FileParser{
    public ResourceBundle bundle;

    @Override
    public abstract void parse(File file, ContentType contentType, DataSource dataSource);

    @Override
    public abstract void parse(InputStream stream, ContentType contentType, DataSource dataSource);

    @Override
    public DataSource createDataSource(ContentType contentType, DataSource dataSource) {
        Language dataLanguage = dataSource.getLanguage();

        List<DataSource> allDataSourcesForSelectedLanguage;
        //Checking whether there is already such data source
        boolean isDuplicate = isDataSourceDuplicated(dataSource);
        DataSource result = null;
        //if not
        if (!isDuplicate) {
            new DataSourceDAO().create(dataSource);
            //replacing existing currentDataSource with the one from database to get id
            allDataSourcesForSelectedLanguage = new DataSourceDAO().readAllByLanguage(dataLanguage);
            for (DataSource source : allDataSourcesForSelectedLanguage) {
                if (source.getLink().equals(dataSource.getLink())) {
                    result = source;
                    break;
                }
            }

        } else {
            System.err.println("Datasource: " + dataSource.getLink() + " had already been added.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_TITLE));
            alert.setHeaderText(bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_DATASOURCE_ALREADY_ADDED));
            alert.setContentText(bundle.getString(ResourceBundleKeys.DIALOGS_ERROR_DATASOURCE_MESSAGE));
            alert.showAndWait();
        }
        //TODO: Check the whole file first. In case the data format is wrong, show an appropriate message
        return result;
    }

    private boolean isDataSourceDuplicated(DataSource src ){
        return new DataSourceDAO().readAllByLanguage(src.getLanguage()).stream().filter((a) -> a.getLink().equals(src.getLink()) && a.getLanguage().equals(src.getLanguage())).findFirst().isPresent();
    }

    void doParsing(InputStream inputStream, ContentType contentType, Language dataLanguage, DataSource finalDataSource){
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));

            List<DataUnit> dataUnits = new ArrayList<>();
            DataUnitDAO dao = new DataUnitDAO();
            if (contentType == ContentType.WORDS_ONLY) {
                fileReader.lines().forEach(line -> dataUnits.add(new DataUnit(line.trim(), "", dataLanguage, finalDataSource)));
                dataUnits.parallelStream().filter(dataUnit -> !dao.contains(finalDataSource, dataUnit)).forEach(dao::create);

            } else if (contentType == ContentType.TRANSLATION_ONLY) {
                fileReader.lines().forEach(line -> dataUnits.add(new DataUnit("", line.trim(), dataLanguage, finalDataSource)));
                dataUnits.parallelStream().filter(dataUnit -> !dao.contains(finalDataSource, dataUnit)).forEach(dao::create);

            } else if (contentType == ContentType.WORDS_TRANSLATION) {
                fileReader.lines().forEach((s) -> dataUnits.add(new DataUnit(s.substring(0, s.indexOf('=')).trim(), s.substring(s.indexOf('=') + 1, s.length()).trim(), dataLanguage, finalDataSource)));
                dataUnits.parallelStream().filter(dataUnit -> !dao.contains(finalDataSource, dataUnit)).forEach(dao::create);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
