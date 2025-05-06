package dbui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {

    @FXML
    private Label statusLabel;

    @FXML
    private void connectToDatabase() {
        String url = "jdbc:sqlserver://MTLSQLDEV013:1433;databaseName=Datacorp;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
        try (Connection conn = DriverManager.getConnection(url)) {
            statusLabel.setText("✅ Connection successful!");
        } catch (SQLException e) {
            statusLabel.setText("❌ Connection failed.");
            e.printStackTrace();
        }
    }

    private String getSection(ComboBox section) {
        return section.getValue().toString();
    }
}
