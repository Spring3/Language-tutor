package tutor.util;

import tutor.models.Language;

import javax.activation.DataSource;
import java.io.File;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class GDriveParser extends AbstractParser implements FileParser {

    public GDriveParser(ResourceBundle bundle, DataSourceType dataSourceType){
        super.bundle = bundle;
        this.dataSourceType = dataSourceType;
    }

    private DataSourceType dataSourceType;

    @Override
    public void parse(File file, ContentType contentType, Language lang) {

    }

    @Override
    public void parse(InputStream stream, ContentType contentType, Language lang) {

        //No need to create datasource as is will be created in GDriveManager.getFileInputStream() method.
        if (dataSourceType.equals(DataSourceType.GDRIVE_WORKSHEET)) {
            doParsing(stream, contentType, lang);
        } else if (dataSourceType.equals(DataSourceType.GDRIVE_SPREADSHEET)) {
            parseSpreadsheet(stream, contentType, lang);
        }
    }

    private void parseSpreadsheet(InputStream inputStream, ContentType contentType, Language dataLanguage){

    }
}
