import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Departments {

    public Departments() {}
    public static void generateDepartments(Statement statement) throws SQLException {
        String[][] departments = {
                {"Bio", "Busch"},
                {"Chem", "CAC"},
                {"CS", "Livi"},
                {"Eng", "CD"},
                {"Math", "Busch"},
                {"Phys", "CAC"}
        };
        String sql = "INSERT INTO Departments (depName, campus) VALUES (?, ?)";
        PreparedStatement stmt = statement.getConnection().prepareStatement(sql);

        for (String[] dept : departments) {
            stmt.setString(1, dept[0]);
            stmt.setString(2, dept[1]);
            stmt.executeUpdate();
        }

        stmt.close();
    }
}
