package dbui.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MailConfigDAO {
    private final Connection conn;

    public MailConfigDAO(Connection conn) {
        this.conn = conn;
    }

    public String checkConfigValue(String section, String keySuffix) throws SQLException {
        String keyName = section + keySuffix;
        String sql = "SELECT Value FROM dbo.MRPMailConfigEntries WHERE SectionName = ? AND KeyName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, section);
            stmt.setString(2, keyName);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Value");
            }
        }
        return null;
    }

    public boolean updateConfigValue(String section, String keySuffix, String newValue) throws SQLException {
        String keyName = section + keySuffix;
        String sql = "UPDATE dbo.MRPMailConfigEntries SET Value = ? WHERE SectionName = ? AND KeyName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newValue); // Could be "true", "false", etc.
            stmt.setString(2, section);
            stmt.setString(3, keyName);
            return stmt.executeUpdate() > 0;
        }
    }
}
