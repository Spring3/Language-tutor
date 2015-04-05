package tutor.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
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
import tutor.Main;
import tutor.controllers.Navigator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by user on 26.02.2015.
 */
public class GDriveManager {

    private static final String CLIENT_ID="250336174195-rfp97d44k25g3mda6gjutis6fm67klfn.apps.googleusercontent.com";       //TODO: put it on server as a GoogleClientSecrets object and make the client get it when he needs it
    private static final String CLIENT_SECRET="rFlo6WHJWB17loAxxbwx9oIh";                                                   //and this one too
    private static final String REDIRECT_URI = "https://www.example.com/oauth2callback";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static GDriveManager instance;
    private static String code;
    private static GoogleAuthorizationCodeFlow flow;
    private static GoogleTokenResponse response;
    private ResourceBundle bundle;
    private DataSourceType dataSourceType;
    private Drive gDrive;

    private GDriveManager(ResourceBundle bundle){
        this.bundle = bundle;
    }

    public static GDriveManager getInstance(ResourceBundle bundle){
        if (instance == null){
            synchronized (GDriveManager.class){
                if (instance == null){
                    instance = new GDriveManager(bundle);
                }
            }
        }
        return instance;
    }

    public DataSourceType getDataSourceType(){
        return dataSourceType;
    }

    private String getRefreshToken(){
        return UserConfigHelper.getInstance().getParameter(UserConfigHelper.REFRESH_TOKEN);
    }

    public boolean gotCode(){
        return code != null && !("".equals(code));
    }

    private boolean authorise(){
        String refreshToken = getRefreshToken();
        if (refreshToken != null){
           GoogleCredential credential = new GoogleCredential.Builder()
                   .setTransport(HTTP_TRANSPORT)
                   .setJsonFactory(JSON_FACTORY)
                   .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
                   .setFromTokenResponse(new TokenResponse().setRefreshToken(refreshToken));
           try {
               credential.refreshToken();
               gDrive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
               return true;
           }
           catch (IOException ex){
               ex.printStackTrace();
           }
        }
        else{
            try {
                StageManager.getInstance().navigateTo(Main.class.getResource(Navigator.WEBVIEW_VIEW_PATH), "Browser", 2, Optional.of(true));
                String authorizeUrl = new GoogleAuthorizationCodeRequestUrl(
                        CLIENT_ID,
                        REDIRECT_URI,
                        Collections.singleton(DriveScopes.DRIVE)).setState("").build();
                Credential credential = exchangeCode(code);
                UserConfigHelper.getInstance().setParameter(UserConfigHelper.REFRESH_TOKEN, credential.getRefreshToken());
                gDrive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
                return true;
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return false;
    }

    private Credential exchangeCode(String authorizationCode) throws IOException{
        GoogleAuthorizationCodeFlow flow = getFlow();
        GoogleTokenResponse response = flow.newTokenRequest(authorizationCode).setRedirectUri(REDIRECT_URI).execute();
        return flow.createAndStoreCredential(response, null);
    }

    public InputStream getFileInputStream(String fileURL) {
        InputStream inputStream = null;
        boolean isDoc = false;
        try {
            if (authorise()) {
                String fileId;
                try {
                    fileId = fileURL.substring(fileURL.indexOf("=") + 1, fileURL.indexOf("&"));
                } catch (StringIndexOutOfBoundsException ex) {
                    fileId = fileURL.substring(fileURL.indexOf("d/") + 2, fileURL.indexOf("/edit"));
                }
                File file = gDrive.files().get(fileId).execute();
                if ("application/vnd.google-apps.spreadsheet".equals(file.getMimeType())) {
                    dataSourceType = DataSourceType.GDRIVE_SPREADSHEET;
                    isDoc = false;
                } else if ("application/vnd.google-apps.document".equals(file.getMimeType())) {
                    dataSourceType = DataSourceType.GDRIVE_WORKSHEET;
                    isDoc = true;
                }
                String downloadURL;
                if (isDoc) {
                    downloadURL = file.getExportLinks().get("text/plain");
                } else {
                    downloadURL = file.getExportLinks().get("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                }
                if (downloadURL != null) {
                    if (downloadURL.length() > 0) {
                        HttpResponse httpResponse = gDrive.getRequestFactory().buildGetRequest(new GenericUrl(downloadURL)).execute();
                        inputStream = httpResponse.getContent();
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return inputStream;
    }

    public void setCode(String code){
        this.code = code;
    }

    private GoogleAuthorizationCodeFlow getFlow(){
        if (flow == null) {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                    .setAccessType("offline")
                    .setApprovalPrompt("force").build();
        }
        return flow;
    }

    public String getFlowURL(){
        if (flow == null){
            flow = getFlow();
        }
        return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }
}
