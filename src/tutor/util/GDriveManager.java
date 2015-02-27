package tutor.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import tutor.models.DataSource;
import tutor.models.Language;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by user on 26.02.2015.
 */
public class GDriveManager {

    private static String CLIENT_ID="250336174195-rfp97d44k25g3mda6gjutis6fm67klfn.apps.googleusercontent.com";
    private static String CLIENT_SECRET="rFlo6WHJWB17loAxxbwx9oIh";

    private static String REDIRECT_URI = "https://www.example.com/oauth2callback";

    private static GDriveManager instance;
    private String code;
    private static GoogleAuthorizationCodeFlow flow;

    private GDriveManager(){

    }

    public static GDriveManager getInstance(){
        if (instance == null){
            synchronized (GDriveManager.class){
                if (instance == null){
                    instance = new GDriveManager();
                }
            }
        }
        return instance;
    }

    public void parseFile(String fileURL, Language dataLanguage){
        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
                    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
                    Drive gDrive = new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), credential).build();
                    String fileId = fileURL.substring(fileURL.indexOf("="), fileURL.indexOf("&"));
                    File file = gDrive.files().get(fileId).execute();
                    DataSource dataSource = null;
                    if (".xlsx".equals(file.getFileExtension()))
                        dataSource = new DataSource(fileURL, DataSourceType.GDRIVE_SPREADSHEET, Service.SERVICE_GDRIVE, dataLanguage);
                    else if (".docx".equals(file.getFileExtension()))
                        dataSource = new DataSource(fileURL, DataSourceType.GDRIVE_WORKSHEET, Service.SERVICE_GDRIVE, dataLanguage);
                    //TODO: check if such dataSource already exists
                    if (null != file.getDownloadUrl() && file.getDownloadUrl().length() > 0){
                        HttpResponse httpResponse = gDrive.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
                        InputStream inputStream = httpResponse.getContent();
                    }
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        };
        thread.run();
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getFlowURL(){
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();

        return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }
}
