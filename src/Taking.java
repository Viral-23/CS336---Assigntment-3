import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Taking {

    public Taking() {}
    public void generateIsTaking(Statement statement) throws SQLException {
        Random rand = new Random();

        String query = "SELECT id FROM Students";
        ResultSet rs = statement.executeQuery(query);

        String sql = "INSERT INTO isTaking (sid, cname) VALUES (?, ?)";
        PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);

        while (rs.next()) {
            Classes classes = new Classes();
            String sid = rs.getString(1);
            int numClasses = rand.nextInt(3) + 3;
            for (int i = 0; i < numClasses; i++) {
                int credits = rand.nextInt(2) + 3;
                preparedStatement.setString(1, sid);
                String className = classes.getRandomClass(credits);
                preparedStatement.setString(2, className);
                preparedStatement.executeUpdate();
                System.out.println("Inserted class " + className + " for student " + sid);
            }
        }
    }
}
