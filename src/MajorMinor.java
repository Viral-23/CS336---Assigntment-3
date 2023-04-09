import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MajorMinor {

    public MajorMinor() {}
    public void generateMajorsAndMinors(Statement statement) throws SQLException {
        Random rand = new Random();
        HashMap<String, Integer> majorCounts = new HashMap<>();

        ArrayList<String> majors = new ArrayList<>();
        majors.add("Bio");
        majors.add("Chem");
        majors.add("CS");
        majors.add("Eng");
        majors.add("Math");
        majors.add("Phys");

        ArrayList<String> minors = new ArrayList<>(majors);
        minors.add("NULL");

        for (String depName : majors) {
            majorCounts.put(depName, 0);
        }

        String query = "SELECT id FROM Students";
        ResultSet rs = statement.executeQuery(query);

        String sql1 = "INSERT INTO Majors (sid, dname) VALUES (?, ?)";
        String sql2 = "INSERT INTO Minors (sid, dname) VALUES (?, ?)";

        PreparedStatement preparedStatementMajor = statement.getConnection().prepareStatement(sql1);
        PreparedStatement preparedStatementMinor = statement.getConnection().prepareStatement(sql2);

        while (rs.next()) {

            String sid = rs.getString(1);
            String major = selectRandomMajor(majorCounts, majors);
            String doubleMajor = "";
            String doubleMajorOutputMessage = "";
            preparedStatementMajor.setString(1, sid);
            preparedStatementMajor.setString(2, major);
            preparedStatementMajor.executeUpdate();

            int doubleMajorTrue = rand.nextInt(11);
            if (doubleMajorTrue == 10) {
                ArrayList<String> temp = new ArrayList(majors);
                temp.remove(major);
                int index = rand.nextInt(temp.size());
                doubleMajor = temp.get(index);
                preparedStatementMajor.setString(1, sid);
                preparedStatementMajor.setString(2, doubleMajor);
                preparedStatementMajor.executeUpdate();

                doubleMajorOutputMessage = ", " + doubleMajor;
            }

            ArrayList<String> temp = new ArrayList<>(minors);
            temp.remove(major);
            temp.remove(doubleMajor);
            int minorIndex = rand.nextInt(temp.size());
            String minor = temp.get(minorIndex);
            String secondMinor = "";
            if (!minor.equals("NULL")) {
                preparedStatementMinor.setString(1, sid);
                preparedStatementMinor.setString(2, minor);
                preparedStatementMinor.executeUpdate();
            }

            String secondMinorOutputMessage = "";
            int secondMinorTrue = rand.nextInt(11);
            if (secondMinorTrue == 10) {
                temp.remove("NULL");
                temp.remove(minor);
                int index = rand.nextInt(temp.size());
                secondMinor = temp.get(index);
                preparedStatementMinor.setString(1, sid);
                preparedStatementMinor.setString(2, secondMinor);
                preparedStatementMinor.executeUpdate();

                secondMinorOutputMessage = ", " + secondMinor;

            }

            System.out.println("Inserted major " + major + doubleMajorOutputMessage + " for student ID: " + sid);
            System.out.println("Inserted minor " + minor + secondMinorOutputMessage + " for student ID: " + sid);

        }

    }

    private String selectRandomMajor(HashMap<String, Integer> majorCounts, ArrayList<String> dnames) {
        Random rand = new Random();
        int index;
        String major;
        do {
            index = rand.nextInt(dnames.size());
            major = dnames.get(index);
        } while (majorCounts.get(major) > 100 / dnames.size());

        majorCounts.put(major, majorCounts.get(major) + 1);

        return major;
    }
}
