package dbui.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MailConfigDAO {
    private final Connection conn;

    public MailConfigDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean updateConfigValue(String section, String keySuffix, String newValue) throws SQLException {
        String keyName = section + keySuffix;
        String sql = "UPDATE dbo.MRPMailConfigEntries SET Value = ? WHERE SectionName = ? AND KeyName = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newValue); // Could be "true", "false", etc.
            pstmt.setString(2, section);
            pstmt.setString(3, keyName);
            return pstmt.executeUpdate() > 0;
        }
    }
}
