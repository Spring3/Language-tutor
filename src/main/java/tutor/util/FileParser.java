package tutor.util;

import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import javax.sql.rowset.serial.SerialRef;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 02.03.2015.
 */
public interface FileParser {
    void parse(File file, Language lang, Language translationLang);
}
