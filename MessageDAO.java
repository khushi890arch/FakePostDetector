package dao;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public List<String> getFakePatterns() {
        List<String> patterns = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT pattern FROM fake_patterns")) {
            while (rs.next()) {
                patterns.add(rs.getString("pattern").toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patterns;
    }

    public boolean isFakeMessage(String message) {
        List<String> patterns = getFakePatterns();
        for (String pattern : patterns) {
            if (message.toLowerCase().contains(pattern)) {
                return true;
            }
        }
        return false;
    }
}
