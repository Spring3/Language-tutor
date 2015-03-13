package tutor.util;

import tutor.dao.DataSourceDAO;
import tutor.dao.DataUnitDAO;
import tutor.models.DataSource;
import tutor.models.DataUnit;
import tutor.models.Language;

import java.io.*;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class PlainFileParser extends AbstractParser implements FileParser {

    public PlainFileParser(ResourceBundle bundle){
        super.bundle = bundle;
    }

    @Override
    public void parse(File file, ContentType contentType, DataSource dataSource) {
        try {
            doParsing(new FileInputStream(file), contentType, dataSource.getLanguage(), createDataSource(contentType, dataSource));
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void parse(InputStream stream, ContentType contentType, DataSource dataSource) {
        doParsing(stream, contentType, dataSource.getLanguage(), createDataSource(contentType, dataSource));
    }
}
