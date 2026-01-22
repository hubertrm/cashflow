package be.hubertrm.cashflow.interfaces.service;

import com.google.api.client.http.HttpRequestInitializer;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleRequestInitializer {
    HttpRequestInitializer authorize() throws IOException, GeneralSecurityException;
}
