package be.hubertrm.cashflow.interfaces.service.impl;

import be.hubertrm.cashflow.interfaces.service.GoogleRequestInitializer;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@ConditionalOnProperty(value = "google.auth.request.initializer", havingValue = "oauth")
public class OAuthRequestInitializer implements GoogleRequestInitializer {

    @Value("${google.oauth.credentials.path}")
    private String credentialsFilePath;

    @Override
    public Credential authorize() throws GeneralSecurityException, IOException {

        try (InputStream in = OAuthRequestInitializer.class.getResourceAsStream(this.credentialsFilePath)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + this.credentialsFilePath);
            }

            List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(new JacksonFactory(), new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), new JacksonFactory(), clientSecrets, scopes
            ).setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                    .setPort(8888)
                    .build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        }
    }


}
