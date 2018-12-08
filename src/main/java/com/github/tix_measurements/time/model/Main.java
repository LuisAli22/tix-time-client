package com.github.tix_measurements.time.model;

import com.github.tix_measurements.time.model.reporting.Reporter;
import org.apache.commons.lang3.SerializationUtils;

import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.prefs.Preferences;

public class Main {

    public static Reporter reporter;
    public static Preferences preferences = Preferences.userRoot().node("/com/tix/model");
    private static String username = null;
    private static String password = null;
    private static String installation = null;
    private static int port = -1;
    private  static String logsPath;
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                username = args[0].replace("\"", "\\\"");
            } catch (RuntimeException e) {
                System.err.println("Username missing.");
                System.exit(1);
            }
            try {
                password = args[1].replace("\"", "\\\"");
            } catch (RuntimeException e) {
                System.err.println("Password missing.");
                System.exit(1);
            }
            try {
                installation = args[2].replace("\"", "\\\"");
            } catch (RuntimeException e) {
                System.err.println("Installation name missing.");
                System.exit(1);
            }
            try {
                port = Integer.parseInt(args[3]);
            } catch (RuntimeException e) {
                System.err.println("Port number missing or cannot be parsed.");
                System.exit(1);
            }
            try {
                logsPath = args[4].replace("\"", "\\\"");
            } catch (RuntimeException e) {
                System.err.println("Logs dir missing or cannot be parsed.");
                System.exit(1);
            }
            preferences = Preferences.userRoot().node("/com/tix/model" + installation);
            preferences.put("logsPath", logsPath);
            Setup.cliLogin(username, password);
            if (!installationExists()){
                Setup.cliInstall(installation, port);
            }
            try {
                startReporting();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new Reporter instance and activates it.
     */
    public static void startReporting() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final long USER_ID = Main.preferences.getLong("userID", 0L);
        final long INSTALLATION_ID = Main.preferences.getLong("installationID", 0L);
        final byte[] keyPairBytes = Main.preferences.getByteArray("keyPair", null);
        final KeyPair KEY_PAIR = SerializationUtils.deserialize(keyPairBytes);
        final int CLIENT_PORT = Main.preferences.getInt("clientPort", -1);
        final String LOGS_PATH = Main.preferences.get("logsPath","");


        //final boolean SAVE_LOGS_LOCALLY = Main.preferences.getBoolean("saveLogsLocally", true);

        reporter = new Reporter(USER_ID, INSTALLATION_ID, KEY_PAIR, CLIENT_PORT, LOGS_PATH);
        reporter.run();
    }
    /**
     * Checks whether user has already set up the application before.
     */
    private static boolean installationExists() {

        int userID = 0;
        byte[] keyPair = null;
        int installationID = 0;
        try {
            userID = preferences.getInt("userID", 0);
            keyPair = preferences.getByteArray("keyPair", null);
            installationID = preferences.getInt("installationID", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (userID > 0) && (keyPair != null) && (installationID > 0);
    }
}