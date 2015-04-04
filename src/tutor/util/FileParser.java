package tutor.util;

import tutor.models.Language;

import java.io.File;
import java.io.InputStream;

/**
 * Created by user on 02.03.2015.
 */
public interface FileParser {
    void parse(File file, ContentType contentType, Language lang);
    void parse(InputStream stream, ContentType contentType, Language lang);
}
