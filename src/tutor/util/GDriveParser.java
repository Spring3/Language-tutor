package tutor.util;

import tutor.models.DataSource;
import tutor.models.Language;
import java.io.File;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class GDriveParser extends AbstractParser implements FileParser {

    public GDriveParser(ResourceBundle bundle){
        super.bundle = bundle;
    }

    @Override
    public void parse(File file, ContentType contentType, DataSource dataSource) {

    }

    @Override
    public void parse(InputStream stream, ContentType contentType, DataSource dataSource) {
        Language language = dataSource.getLanguage();

        //No need to create datasource as is will be created in GDriveManager.getFileInputStream() method.
        if (dataSource.getType().equals(DataSourceType.GDRIVE_WORKSHEET)){
            doParsing(stream, contentType, language, dataSource);
        }
        else if (dataSource.getType().equals(DataSourceType.GDRIVE_SPREADSHEET)){
            parseSpreadsheet(stream, contentType, language, dataSource);
        }
    }

    private void parseSpreadsheet(InputStream inputStream, ContentType contentType, Language dataLanguage, DataSource finalDataSource){

    }
}
