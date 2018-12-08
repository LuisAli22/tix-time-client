package com.github.tix_measurements.time.client.ui;

import com.github.tix_measurements.time.model.Main;
import com.github.tix_measurements.time.model.Setup;
import com.github.tix_measurements.time.model.reporting.utils.ConfigurationReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class Setup2Controller {
    private static ConfigurationReader configurationReader = ConfigurationReader.getInstance();
    private static final Logger logger = LogManager.getLogger();
    @FXML
    private Button createInstallationButton;
    @FXML
    private TextField installationName;
    @FXML
    private Text status;
    @FXML
    private Hyperlink cancelLink;

    @FXML
    private void createInstallation() {
        final String installationInput = installationName.getText().trim().replace("\"", "\\\"");
        if (installationInput != null) {
            final int responseStatusCode = Setup.install(installationInput);
            if (responseStatusCode == 401) {
                // new installation details are incorrect
                status.setText("Verifique los datos ingresados");
            } else if (responseStatusCode == 200) {
                try {
                    Main.startReporting();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/setup3.fxml"));
                    Parent root = loader.load();
                    createInstallationButton.getScene().setRoot(root);
                } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                    logger.error("Cannot load setup 3 screen");
                }
            } else {
                status.setText("Falló la conexión con el servidor");
                logger.error("Cannot connect to server when trying to create new installation");
            }
        }
    }

    @FXML
    private void help() {
        try {
            Desktop.getDesktop().browse(new URI(configurationReader.getUrl()));
        } catch (Exception e) {
            logger.error("Error when opening help URL");
        }
    }

    @FXML
    private void cancel() {
        closeWindow(cancelLink);
    }

    private void closeWindow(Hyperlink link) {
        link.getScene().getWindow().hide();
    }

}
