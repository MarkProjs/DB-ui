package dbui;

import dbui.model.MailConfigDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {

    @FXML private Label statusLabel;

    @FXML
    private void connectToDatabase() {
        String url = "jdbc:sqlserver://MTLSQLDEV013:1433;databaseName=Datacorp;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
        try  {
            conn = DriverManager.getConnection(url);
            statusLabel.setText("✅ Connection successful!");
        } catch (SQLException e) {
            statusLabel.setText("❌ Connection failed.");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if(conn != null && !conn.isClosed()) {
                conn.close();
                statusLabel.setText("✅ Disconnected from database.");
            }

        } catch (SQLException e) {
            statusLabel.setText("❌ Failed to disconnect.");
            e.printStackTrace();
        }
    }
}
