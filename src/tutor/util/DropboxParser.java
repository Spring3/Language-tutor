package tutor.util;

import tutor.models.DataSource;

import java.io.File;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class DropboxParser extends AbstractParser implements FileParser {

    public DropboxParser(ResourceBundle bundle){
        super.bundle = bundle;
    }

    @Override
    public void parse(File file, ContentType contentType, DataSource dataSource) {

    }

    @Override
    public void parse(InputStream stream, ContentType contentType, DataSource dataSource) {

    }

}
