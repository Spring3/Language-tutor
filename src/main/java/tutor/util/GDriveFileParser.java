package tutor.util;

import tutor.models.Language;

/**
 * Created by user on 06.04.2015.
 */
public abstract class GDriveFileParser{

    abstract void parse(GDriveManager manager, ContentType contentType, Language lang);
}
