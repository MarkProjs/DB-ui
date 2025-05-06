package dbui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://10.210.19.34:1433;databaseName=Datacorp;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connection successful!");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TOP 100 * FROM dbo.MRPMailConfigEntries");
            while (rs.next()) {
                System.out.printf("%d\t%s\t%s\t%s%n",
                        rs.getInt("Id"),
                        rs.getString("SectionName"),
                        rs.getString("KeyName"),
                        rs.getString("Value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
