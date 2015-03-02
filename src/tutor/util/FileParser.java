package tutor.util;

import tutor.models.DataSource;

import java.io.File;
import java.io.InputStream;

/**
 * Created by user on 02.03.2015.
 */
public interface FileParser {
    public void parse(File file, ContentType contentType, DataSource dataSource);
    public void parse(InputStream stream, ContentType contentType, DataSource dataSource);
    DataSource createDataSource(ContentType contentType, DataSource dataSource);
}
