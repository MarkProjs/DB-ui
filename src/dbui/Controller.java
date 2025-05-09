package dbui;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import dbui.model.MailConfigDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private Label connectLabel;
    @FXML private Button connectBtn;
    @FXML private ComboBox<String> sectionComboBox;
    @FXML private Button pauseOutboxBtn;
    @FXML private Button pauseInboxBtn;
    @FXML private Button suspendBtn;
    @FXML private Button closeAppBtn;
    @FXML private Label statusLabel;

    private Connection conn;
    private MailConfigDAO dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectToDatabase();
    }

    public void connectToDatabase() {
        String url = "jdbc:sqlserver://MTLSQLDEV013:1433;databaseName=Datacorp;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
        try {
            conn = DriverManager.getConnection(url);
            dao = new MailConfigDAO(conn);

            //show action UI
            sectionComboBox.setVisible(true);
            pauseOutboxBtn.setVisible(true);
            pauseInboxBtn.setVisible(true);
            suspendBtn.setVisible(true);
            closeAppBtn.setVisible(true);

            sectionComboBox.getItems().addAll("B2B", "FSK", "EDI", "MRP");
            sectionComboBox.setOnAction(e -> {
                String selectedSection = sectionComboBox.getValue();
                if (selectedSection != null) {
                    updateButtonLabels(selectedSection);
                }
            });

        }catch (SQLException e) {
            statusLabel.setText("❌ Connection to the database failed.");
        }
    }

    private void handleAction(String keySuffix, String actionName) {
        String section = sectionComboBox.getValue();
        if (section == null) {
            statusLabel.setText("⚠️ Select a section first.");
            statusLabel.setVisible(true);
            return;
        }

        try {
           String currentValue = dao.checkConfigValue(section, keySuffix);
           String newValue = "true".equalsIgnoreCase(currentValue) ? "false" : "true";

           boolean success = dao.updateConfigValue(section, keySuffix, newValue);
           if (success) {
               statusLabel.setText(actionName + "for "+section+ "is now "+newValue.toUpperCase());
               updateButtonLabels(section);
           } else {
               statusLabel.setText("No matching config found.");
           }
           statusLabel.setVisible(true);
        }catch (SQLException e) {
            statusLabel.setVisible(true);
            statusLabel.setText("❌ Database update failed.");
            e.printStackTrace();
        }
    }

    private void updateButtonLabels(String section) {
        try {
            // Outbox
            String outboxValue = dao.checkConfigValue(section, "PauseOutbox");
            boolean isOutboxPaused = "true".equalsIgnoreCase(outboxValue);
            pauseOutboxBtn.setText(isOutboxPaused ? "Resume Outbox" : "Pause Outbox");

            // Inbox
            String inboxValue = dao.checkConfigValue(section, "PauseInbox");
            boolean isInboxPaused = "true".equalsIgnoreCase(inboxValue);
            pauseInboxBtn.setText(isInboxPaused ? "Resume Inbox" : "Pause Inbox");

            // Suspend
            String suspendValue = dao.checkConfigValue(section, "Suspend");
            boolean isSuspended = "true".equalsIgnoreCase(suspendValue);
            suspendBtn.setText(isSuspended ? "Activate Section" : "Suspend Section");

        } catch (SQLException e) {
            statusLabel.setText("⚠️ Could not load config states.");
            statusLabel.setVisible(true);
            e.printStackTrace();
        }
    }


    public void handlePauseOutbox(){
        handleAction("PauseOutbox", "Outbox");
    }
    public void handlePauseInbox(){
        handleAction("PauseInbox", "Inbox");
    }
    public void handleSuspend(){
        handleAction("Suspend", "Suspend");
    }
    public void handleCloseApp(){
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("✅ Database connection closed.");
            }
        }catch (SQLException e) {
            System.err.println("⚠️ Error closing the connection:");
            e.printStackTrace();
        }

        javafx.application.Platform.exit();
    }
}
