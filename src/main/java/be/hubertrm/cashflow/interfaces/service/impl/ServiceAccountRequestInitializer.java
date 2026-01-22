package be.hubertrm.cashflow.interfaces.service.impl;

import be.hubertrm.cashflow.interfaces.service.GoogleRequestInitializer;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@ConditionalOnProperty(value = "google.auth.request.initializer", havingValue = "service-account")
public class ServiceAccountRequestInitializer implements GoogleRequestInitializer {

    @Value("${google.service-account.credentials.path}")
    private String credentialsFilePath;

    @Override
    public HttpRequestInitializer authorize() throws IOException {

        try (InputStream in = ServiceAccountRequestInitializer.class.getResourceAsStream(this.credentialsFilePath)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + this.credentialsFilePath);
            }
            List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

            final GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                    .createScoped(scopes);

            final HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(credentials);
            final HttpRequestFactory requestFactory = new NetHttpTransport()
                    .createRequestFactory(credentialsAdapter);
            return requestFactory.getInitializer();
        }
    }


}
