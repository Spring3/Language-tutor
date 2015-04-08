package tutor.util;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import tutor.models.Language;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class GDriveParser extends GDriveFileParser{

    public GDriveParser(ResourceBundle bundle){
        this.bundle = bundle;
    }
    private ResourceBundle bundle;

    public void parse(GDriveManager manager, ContentType contentType, Language lang) {
        if (manager.getDataSourceType().equals(DataSourceType.GDRIVE_WORKSHEET)) {
            new PlainFileParser(bundle).doParsing(manager.getFileInputStream(), contentType, lang);
        } else if (manager.getDataSourceType().equals(DataSourceType.GDRIVE_SPREADSHEET)) {
            parseSpreadsheet(manager, contentType, lang);
        }
    }

    private void parseSpreadsheet(GDriveManager manager, ContentType contentType, Language dataLanguage){
        String downloadURL = manager.getFile().getExportLinks().get("text/csv");
        File downloadedCSVFile = new File("temp/" + manager.getFileId() + ".csv");
        File tempDir = new File("temp");
        if (!tempDir.exists() && ! tempDir.isDirectory()){
            tempDir.mkdir();
        }
        if (!downloadedCSVFile.exists()){
            try {
                downloadedCSVFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream csvFileInputStream = null;
        try {
            HttpResponse response = manager.getDrive().getRequestFactory().buildGetRequest(new GenericUrl(downloadURL+"&charset=utf-8")).execute();
            csvFileInputStream = response.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(csvFileInputStream, StandardCharsets.UTF_8));
            writer = new BufferedWriter(new FileWriter(downloadedCSVFile, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = null;
        try {
            while ((s = reader.readLine()) != null) {
                writer.write(s);
                writer.newLine();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        if (writer != null)
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
