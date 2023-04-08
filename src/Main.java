import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
public class Main {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream("path/to/db.properties")) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("Database information not provided.");
        }
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        Connection myConn = DriverManager.getConnection(url, username, password);
        Statement mySt = myConn.createStatement();

//        generateDepartments(mySt);
//
//        generateStudents(mySt);
//
        generateClasses(mySt);

//        generateMajorsAndMinors(mySt);


    }

    private static void generateDepartments(Statement statement) throws SQLException {
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

    private static void generateStudents(Statement statement) throws SQLException {
        String[] firstNames =
                {"James", "John", "Robert", "Michael", "David", "Mary", "Linda", "Susan", "Lisa", "Carol"};
        String[] lastNames =
                {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Moore", "Lee"};

        Random rand = new Random();
        HashMap<Integer, Integer> takenIDs = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            String firstName = firstNames[i % 10];
            String lastName = lastNames[i % 10];

            int id;
            do {
                id = rand.nextInt(900000000) + 100000000;
            } while (takenIDs.containsKey(id));
            takenIDs.put(id, 1);

            String query = "INSERT INTO Students (first_name, last_name, id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, String.valueOf(id));
            preparedStatement.executeUpdate();
            System.out.println("Inserted student " + firstName + " " + lastName + " with ID " + id);
        }

    }

    private static void generateClasses(Statement statement) throws SQLException {
        String[] threeCreditClasses = {
                "Bird Watching",
                "Psychology",
                "Intro to Computers",
                "Music in Film",
                "Sound Design",
                "Algorithms",
                "Tree Planting",
                "Physics I",
                "General Chemistry",
                "Expository Writing",
                "Statics",
                "Probability",
                "Databases",
                "Wood Working",
                "Sustainable Energy"
        };

        String[] fourCreditClasses = {
                "Multivariable Calculus",
                "Data Structures",
                "Physics II",
                "Electronic Devices",
                "Organic Chemistry",
                "Linear Algebra",
                "Discrete Mathematics",
                "Intro to AI",
                "Computer Systems",
                "Capstone and Design",
                "Android Development",
                "Criminology",
                "Economics",
                "Internet Technology",
                "Data Science"
        };

        String sql = "INSERT INTO Classes (className, credits) VALUES (?, ?)";
        PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);

        for (String className : threeCreditClasses) {
            preparedStatement.setString(1, className);
            preparedStatement.setString(2, "3");
            preparedStatement.executeUpdate();
            System.out.println("Inserted class " + className + " with credits " + 3);
        }

        for (String className : fourCreditClasses) {
            preparedStatement.setString(1, className);
            preparedStatement.setString(2, "4");
            preparedStatement.executeUpdate();
            System.out.println("Inserted class " + className + " with credits " + 4);
        }

    }

    private static void generateMajorsAndMinors(Statement statement) throws SQLException {
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

    private static String selectRandomMajor(HashMap<String, Integer> majorCounts, ArrayList<String> dnames) {
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

    private static void generateIsTaking(Statement statement) throws SQLException {
        Random rand = new Random();

        String query = "SELECT id FROM Students";
        ResultSet rs = statement.executeQuery(query);

        String sql = "INSERT INTO isTaking (sid, dname) VALUES (?, ?)";
        PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);

        int numClasses = rand.nextInt(3) + 3;
        for (int i = 0; i < numClasses; i++) {

        }
    }

}