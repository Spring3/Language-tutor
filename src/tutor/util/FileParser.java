package tutor.util;

import tutor.models.Language;

import java.io.File;
import java.io.InputStream;

/**
 * Created by user on 02.03.2015.
 */
public interface FileParser {
    public void parse(File file, ContentType contentType, Language lang);
    public void parse(InputStream stream, ContentType contentType, Language lang);
}
