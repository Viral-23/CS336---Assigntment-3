import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

public class TakingTaken {

    public TakingTaken() {}
    public void generateIsTaking(Statement statement) throws SQLException {
        HashMap<String, Integer> rankingCounts = new HashMap<>();
        rankingCounts.put("Fr", 0);
        rankingCounts.put("So", 0);
        rankingCounts.put("Ju", 0);
        rankingCounts.put("Sr", 0);

        Random rand = new Random();

        String query = "SELECT id FROM Students";
        ResultSet rs = statement.executeQuery(query);

        String sql1 = "INSERT INTO IsTaking (sid, cname) VALUES (?, ?)";
        PreparedStatement preparedStatement1 = statement.getConnection().prepareStatement(sql1);

        String sql2 = "INSERT INTO HasTaken (sid, cname, grade) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement2 = statement.getConnection().prepareStatement(sql2);

        while (rs.next()) {
            Classes classes = new Classes();
            String sid = rs.getString(1);
            int numClasses = rand.nextInt(3) + 3;
            for (int i = 0; i < numClasses; i++) {
                int credits = rand.nextInt(2) + 3;
                preparedStatement1.setString(1, sid);
                String className = classes.getRandomClass(credits);
                preparedStatement1.setString(2, className);
                preparedStatement1.executeUpdate();
                System.out.println("Inserted class " + className + " for student " + sid);
            }
            int rank;
            String year = "";
            do {
                rank = rand.nextInt(4);
                switch (rank) {
                    case 0:
                        year = "Fr";
                        break;
                    case 1:
                        year = "So";
                        break;
                    case 2:
                        year = "Ju";
                        break;
                    case 3:
                        year = "Sr";
                        break;
                }
            } while (rankingCounts.get(year) >= 25);

            numClasses = 0;

            if (year.equals("Fr")) {
                numClasses = rand.nextInt(8) + 1;
                rankingCounts.put(year, rankingCounts.get(year) + 1);
            }

            if (year.equals("So")) {
                numClasses = rand.nextInt(5) + 10;
                rankingCounts.put(year, rankingCounts.get(year) + 1);
            }

            if (year.equals("Ju")) {
                numClasses = rand.nextInt(3) + 20;
                rankingCounts.put(year, rankingCounts.get(year) + 1);
            }

            if (year.equals("Sr")) {
                numClasses = 30;
                rankingCounts.put(year, rankingCounts.get(year) + 1);
            }

            for (int i = 0; i < numClasses; i++) {
                String grade = getRandomGrade();
                int credits = rand.nextInt(2) + 3;
                preparedStatement2.setString(1, sid);
                String className = classes.getRandomClass(credits);
                preparedStatement2.setString(2, className);
                preparedStatement2.setString(3, grade);
                preparedStatement2.executeUpdate();
                System.out.println("Taken class " + className + " for student " + sid);
            }
        }
        System.out.println(rankingCounts.get("Fr"));
        System.out.println(rankingCounts.get("So"));
        System.out.println(rankingCounts.get("Ju"));
        System.out.println(rankingCounts.get("Sr"));
    }
    private String getRandomGrade() {
        String grade = "";
        Random rand = new Random();
        int num = rand.nextInt(100) + 1;
        if (num <= 20) { // 20% chance of getting an A
            grade = "A";
        } else if (num <= 50) { // 30% chance of getting a B
            grade = "B";
        } else if (num <= 80) { // 30% chance of getting a C
            grade = "C";
        } else if (num <= 95) { // 15% chance of getting a D
            grade = "D";
        } else { // 5% chance of getting an F
            grade = "F";
        }
        return grade;
    }
}
