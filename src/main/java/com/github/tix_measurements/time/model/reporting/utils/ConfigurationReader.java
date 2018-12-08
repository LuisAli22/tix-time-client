package com.github.tix_measurements.time.model.reporting.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.tix_measurements.time.model.Setup;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class ConfigurationReader {
    private static ConfigurationReader instance = null;
    private ConfigurationData configurationData;
    public String getIp() {
        return configurationData.getIp();
    }

    public int getServerPort() {
        return configurationData.getServerPort();
    }

    public int getClientPort() {
        return configurationData.getClientPort();
    }

    public String getUrl() {
        return configurationData.getUrl();
    }

    public  String getLogsPath() {return configurationData.getLogsPath();}

    public  boolean isSaveLogsLocally(){return configurationData.isSaveLogsLocally();}

    private CloseableHttpClient client;

    public ConfigurationReader(){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream configFileIs = Setup.class.getClassLoader()
                .getResourceAsStream("Application.yml");
        try {
            configurationData = mapper.readValue(configFileIs, ConfigurationData.class);
            setCloseableHttpClient();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ConfigurationReader getInstance() {
        if (instance == null)
            instance = new ConfigurationReader();

        return instance;
    }
    private void setCloseableHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        if (configurationData.isHttps()) {
            final SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();

            client = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .build();
        }else{
            client = HttpClients.custom()
                    .build();
        }
    }
    public CloseableHttpClient getCloseableHttpClient(){
        return client;
    }
}
class ConfigurationData{
    private boolean https;
    private String ip;
    private int serverPort;
    private int clientPort;
    private String url;
    private String logsPath;
    private boolean saveLogsLocally;

    public boolean isSaveLogsLocally() {
        return saveLogsLocally;
    }

    public boolean isHttps() {
        return https;
    }

    public String getLogsPath() {
        return logsPath;
    }

    public String getIp() {
        return ip;
    }

    public int getServerPort() {
        return serverPort;
    }


    public int getClientPort() {
        return clientPort;
    }

    public String getUrl() {
        return url;
    }
}