import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CollegeDB {
    public static void main(String[] args) throws SQLException {
        if (args.length != 3)
            System.out.print("Invalid Command line arguments.");

        String url = args[0];
        String username = args[1];
        String password = args[2];

        Connection myConn = DriverManager.getConnection(url, username, password);
        Statement statement = myConn.createStatement();

        System.out.println("Welcome to the university database. Queries available:");
        System.out.println("1. Search students by name.");
        System.out.println("2. Search students by year.");
        System.out.println("3. Search for students with a GPA >= threshold.");
        System.out.println("4. Search for students with a GPA <= threshold.");
        System.out.println("5. Get department statistics.");
        System.out.println("6. Get class statistics.");
        System.out.println("7. Execute an abitrary SQL query.");
        System.out.println("8. Exit the application.");

        Scanner scanner = new Scanner(System.in);
        int option = 0;

        while (option != 8) {
            System.out.println("Which query would you like to run (1-8)?");
            try {
                option = scanner.nextInt();
            }
            catch (InputMismatchException e) {
                scanner.nextLine();
                option = 0;
            }

            switch (option) {
                case 1: {
                    System.out.println("Please enter the name.");
                    scanner = new Scanner(System.in);
                    String name = "%"+ scanner.nextLine() + "%";
                    String query = "SELECT s.id, CONCAT(s.last_name, ', ', s.first_name) AS full_name,\n" +
                            "    GROUP_CONCAT(DISTINCT Majors.dname SEPARATOR ', ') AS majors, \n" +
                            "    GROUP_CONCAT(DISTINCT Minors.dname SEPARATOR ', ') AS minors,\n" +
                            "       SUM(CASE h.grade \n" +
                            "               WHEN 'A' THEN 4 \n" +
                            "               WHEN 'B' THEN 3 \n" +
                            "               WHEN 'C' THEN 2 \n" +
                            "               WHEN 'D' THEN 1 \n" +
                            "               ELSE 0 \n" +
                            "             END * CAST(c.credits AS SIGNED)) / SUM(CAST(c.credits AS SIGNED)) AS gpa, \n" +
                            "       total_credits.total_credits AS total_credits \n" +
                            "FROM Students s\n" +
                            "LEFT JOIN Majors ON s.id = Majors.sid\n" +
                            "LEFT JOIN Minors ON s.id = Minors.sid\n" +
                            "LEFT JOIN HasTaken h ON s.id = h.sid\n" +
                            "LEFT JOIN Classes c ON h.cname = c.className \n" +
                            "INNER JOIN (\n" +
                            "    SELECT sid, SUM(Classes.credits) AS total_credits\n" +
                            "    FROM HasTaken\n" +
                            "    INNER JOIN Classes ON HasTaken.cname = Classes.className\n" +
                            "    GROUP BY sid\n" +
                            ") AS total_credits ON s.id = total_credits.sid\n" +
                            "WHERE s.first_name LIKE ? OR s.last_name LIKE ?\n" +
                            "GROUP BY s.id, s.last_name, s.first_name;\n";

                    PreparedStatement preparedStatement = myConn.prepareStatement(query);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, name);

                    ResultSet rs = preparedStatement.executeQuery();

                    while (rs.next()) {
                        String fullName = rs.getString("full_name");
                        String id = rs.getString("id");
                        String major = rs.getString("majors");
                        String minor = rs.getString("minors");
                        int credits = rs.getInt("total_credits");
                        double gpa = rs.getDouble("gpa");

                        System.out.printf("%s\nID: %s\nMajor: %s\n", fullName, id, major);

                        if (minor != null) {
                            System.out.printf("Minor: %s\n", minor);
                        }

                        System.out.printf("GPA: %.3f\nCredits: %d\n\n", gpa, credits);
                    }
                    break;
                }
                case 2: {
                    System.out.println("Please enter the year.");
                    scanner = new Scanner(System.in);
                    String year = scanner.nextLine();
                    int minCredits = 0;
                    int maxCredits = 0;
                    switch(year.toUpperCase()) {
                        case "FR": {
                            minCredits = 0;
                            maxCredits = 29;
                            break;
                        }
                        case "SO": {
                            minCredits = 30;
                            maxCredits = 59;
                            break;
                        }
                        case "JU": {
                            minCredits = 60;
                            maxCredits = 89;
                            break;
                        }
                        case "SR": {
                            minCredits = 90;
                            maxCredits = 120;
                            break;
                        }
                        default: {
                            System.out.println("Invalid year");
                            break;
                        }
                    }

                    String query = "SELECT s.id, CONCAT(s.last_name, ', ', s.first_name) AS full_name,\n" +
                            "    GROUP_CONCAT(DISTINCT Majors.dname SEPARATOR ', ') AS majors, \n" +
                            "    GROUP_CONCAT(DISTINCT Minors.dname SEPARATOR ', ') AS minors,\n" +
                            "       SUM(CASE h.grade \n" +
                            "               WHEN 'A' THEN 4 \n" +
                            "               WHEN 'B' THEN 3 \n" +
                            "               WHEN 'C' THEN 2 \n" +
                            "               WHEN 'D' THEN 1 \n" +
                            "               ELSE 0 \n" +
                            "             END * CAST(c.credits AS SIGNED)) / SUM(CAST(c.credits AS SIGNED)) AS gpa, \n" +
                            "       total_credits.total_credits AS total_credits \n" +
                            "FROM Students s\n" +
                            "LEFT JOIN Majors ON s.id = Majors.sid\n" +
                            "LEFT JOIN Minors ON s.id = Minors.sid\n" +
                            "LEFT JOIN HasTaken h ON s.id = h.sid\n" +
                            "LEFT JOIN Classes c ON h.cname = c.className \n" +
                            "INNER JOIN (\n" +
                            "    SELECT sid, SUM(Classes.credits) AS total_credits\n" +
                            "    FROM HasTaken\n" +
                            "    INNER JOIN Classes ON HasTaken.cname = Classes.className\n" +
                            "    GROUP BY sid\n" +
                            ") AS total_credits ON s.id = total_credits.sid\n" +
                            "GROUP BY s.id, s.last_name, s.first_name\n" +
                            "HAVING total_credits BETWEEN ? AND ?\n";

                    PreparedStatement preparedStatement = myConn.prepareStatement(query);
                    preparedStatement.setInt(1, minCredits);
                    preparedStatement.setInt(2, maxCredits);

                    ResultSet rs = preparedStatement.executeQuery();

                    while (rs.next()) {
                        String fullName = rs.getString("full_name");
                        String id = rs.getString("id");
                        String major = rs.getString("majors");
                        String minor = rs.getString("minors");
                        int credits = rs.getInt("total_credits");
                        double gpa = rs.getDouble("gpa");

                        System.out.printf("%s\nID: %s\nMajor: %s\n", fullName, id, major);

                        if (minor != null) {
                            System.out.printf("Minor: %s\n", minor);
                        }

                        System.out.printf("GPA: %.3f\nCredits: %d\n\n", gpa, credits);
                    }
                    break;
                }
                case 3: {
                    System.out.println("Please enter the threshold.");
                    scanner = new Scanner(System.in);
                    double gpa = scanner.nextDouble();
                    String query = "SELECT s.id, CONCAT(s.last_name, ', ', s.first_name) AS full_name,\n" +
                            "    GROUP_CONCAT(DISTINCT Majors.dname SEPARATOR ', ') AS majors, \n" +
                            "    GROUP_CONCAT(DISTINCT Minors.dname SEPARATOR ', ') AS minors,\n" +
                            "       SUM(CASE h.grade \n" +
                            "               WHEN 'A' THEN 4 \n" +
                            "               WHEN 'B' THEN 3 \n" +
                            "               WHEN 'C' THEN 2 \n" +
                            "               WHEN 'D' THEN 1 \n" +
                            "               ELSE 0 \n" +
                            "             END * CAST(c.credits AS SIGNED)) / SUM(CAST(c.credits AS SIGNED)) AS gpa, \n" +
                            "       total_credits.total_credits AS total_credits \n" +
                            "FROM Students s\n" +
                            "LEFT JOIN Majors ON s.id = Majors.sid\n" +
                            "LEFT JOIN Minors ON s.id = Minors.sid\n" +
                            "LEFT JOIN HasTaken h ON s.id = h.sid\n" +
                            "LEFT JOIN Classes c ON h.cname = c.className \n" +
                            "INNER JOIN (\n" +
                            "    SELECT sid, SUM(Classes.credits) AS total_credits\n" +
                            "    FROM HasTaken\n" +
                            "    INNER JOIN Classes ON HasTaken.cname = Classes.className\n" +
                            "    GROUP BY sid\n" +
                            ") AS total_credits ON s.id = total_credits.sid\n" +
                            "WHERE s.first_name LIKE '%John%' OR s.last_name LIKE '%John%'\n" +
                            "GROUP BY s.id, s.last_name, s.first_name\n" +
                            "HAVING gpa >= ?";

                    PreparedStatement preparedStatement = myConn.prepareStatement(query);
                    preparedStatement.setDouble(1, gpa);
                    ResultSet rs = preparedStatement.executeQuery();

                    while (rs.next()) {
                        String fullName = rs.getString("full_name");
                        String id = rs.getString("id");
                        String major = rs.getString("majors");
                        String minor = rs.getString("minors");
                        int credits = rs.getInt("total_credits");
                        gpa = rs.getDouble("gpa");

                        System.out.printf("%s\nID: %s\nMajor: %s\n", fullName, id, major);

                        if (minor != null) {
                            System.out.printf("Minor: %s\n", minor);
                        }

                        System.out.printf("GPA: %.3f\nCredits: %d\n\n", gpa, credits);
                    }
                    break;
                }
                case 4: {
                    System.out.println("Please enter the threshold.");
                    scanner = new Scanner(System.in);
                    double gpa = scanner.nextDouble();
                    String query = "SELECT s.id, CONCAT(s.last_name, ', ', s.first_name) AS full_name,\n" +
                            "    GROUP_CONCAT(DISTINCT Majors.dname SEPARATOR ', ') AS majors, \n" +
                            "    GROUP_CONCAT(DISTINCT Minors.dname SEPARATOR ', ') AS minors,\n" +
                            "       SUM(CASE h.grade \n" +
                            "               WHEN 'A' THEN 4 \n" +
                            "               WHEN 'B' THEN 3 \n" +
                            "               WHEN 'C' THEN 2 \n" +
                            "               WHEN 'D' THEN 1 \n" +
                            "               ELSE 0 \n" +
                            "             END * CAST(c.credits AS SIGNED)) / SUM(CAST(c.credits AS SIGNED)) AS gpa, \n" +
                            "       total_credits.total_credits AS total_credits \n" +
                            "FROM Students s\n" +
                            "LEFT JOIN Majors ON s.id = Majors.sid\n" +
                            "LEFT JOIN Minors ON s.id = Minors.sid\n" +
                            "LEFT JOIN HasTaken h ON s.id = h.sid\n" +
                            "LEFT JOIN Classes c ON h.cname = c.className \n" +
                            "INNER JOIN (\n" +
                            "    SELECT sid, SUM(Classes.credits) AS total_credits\n" +
                            "    FROM HasTaken\n" +
                            "    INNER JOIN Classes ON HasTaken.cname = Classes.className\n" +
                            "    GROUP BY sid\n" +
                            ") AS total_credits ON s.id = total_credits.sid\n" +
                            "WHERE s.first_name LIKE '%John%' OR s.last_name LIKE '%John%'\n" +
                            "GROUP BY s.id, s.last_name, s.first_name\n" +
                            "HAVING gpa <= ?";

                    PreparedStatement preparedStatement = myConn.prepareStatement(query);
                    preparedStatement.setDouble(1, gpa);
                    ResultSet rs = preparedStatement.executeQuery();

                    while (rs.next()) {
                        String fullName = rs.getString("full_name");
                        String id = rs.getString("id");
                        String major = rs.getString("majors");
                        String minor = rs.getString("minors");
                        int credits = rs.getInt("total_credits");
                        gpa = rs.getDouble("gpa");

                        System.out.printf("%s\nID: %s\nMajor: %s\n", fullName, id, major);

                        if (minor != null) {
                            System.out.printf("Minor: %s\n", minor);
                        }

                        System.out.printf("GPA: %.3f\nCredits: %d\n\n", gpa, credits);
                    }
                    break;
                }
                case 5: {
                    System.out.println("Please enter the department.");
                    scanner = new Scanner(System.in);
                    String department = scanner.nextLine();

                    String query = "SELECT Majors.dname AS major_name,\n" +
                            "       COUNT(DISTINCT s.id) AS num_students,\n" +
                            "       AVG(subquery.student_gpa) AS avg_gpa\n" +
                            "FROM Students s\n" +
                            "LEFT JOIN Majors ON s.id = Majors.sid\n" +
                            "LEFT JOIN (\n" +
                            "    SELECT h.sid, SUM(CASE h.grade \n" +
                            "               WHEN 'A' THEN 4 \n" +
                            "               WHEN 'B' THEN 3 \n" +
                            "               WHEN 'C' THEN 2 \n" +
                            "               WHEN 'D' THEN 1 \n" +
                            "               ELSE 0 \n" +
                            "             END * CAST(c.credits AS SIGNED)) / SUM(CAST(c.credits AS SIGNED)) AS student_gpa\n" +
                            "    FROM HasTaken h\n" +
                            "    LEFT JOIN Classes c ON h.cname = c.className\n" +
                            "    GROUP BY h.sid\n" +
                            ") AS subquery ON s.id = subquery.sid\n" +
                            "WHERE Majors.dname = ?\n" +
                            "GROUP BY Majors.dname";

                    PreparedStatement preparedStatement = myConn.prepareStatement(query);
                    preparedStatement.setString(1, department);
                    ResultSet rs = preparedStatement.executeQuery();

                    while (rs.next()) {
                        int numStudents = rs.getInt("num_students");
                        double avgGPA = rs.getDouble("avg_gpa");
                        System.out.printf("Num students: %d\nAverage GPA: %.3f\n", numStudents, avgGPA);
                    }
                }
                case 6: {
                    System.out.println("Please enter the class name.");
                    scanner = new Scanner(System.in);
                    String className = scanner.nextLine();
                    String query = "SELECT COUNT(DISTINCT it.sid) as enrollment\n" +
                            "FROM IsTaking it\n" +
                            "WHERE it.cname = ?;";
                    PreparedStatement preparedStatement = myConn.prepareStatement(query);
                    preparedStatement.setString(1, className);
                    ResultSet rs = preparedStatement.executeQuery();

                    if (rs.next()) {
                        int enrollment = rs.getInt("enrollment");
                        System.out.printf("%s\n%d students currently enrolled\nGrades of previous enrollees:\n", className, enrollment);
                    }

                    query = "SELECT h.grade, COUNT(*) as count\n" +
                            "FROM HasTaken h\n" +
                            "WHERE h.cname = ?\n" +
                            "GROUP BY h.grade;";
                    preparedStatement = myConn.prepareStatement(query);
                    preparedStatement.setString(1, className);
                    rs = preparedStatement.executeQuery();

                    while (rs.next()) {
                        String grade = rs.getString("grade");
                        int count = rs.getInt("count");
                        System.out.printf("%s %d\n", grade, count);
                    }

                    break;
                }
                case 7: {
                    System.out.println("Please enter the query.");
                    scanner = new Scanner(System.in);
                    String query = scanner.nextLine();

                    try {
                        statement = myConn.createStatement();
                        ResultSet rs = statement.executeQuery(query);

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        StringBuilder columnNames = new StringBuilder();
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames.append(rsmd.getColumnName(i)).append("\t");
                        }
                        System.out.println(columnNames.toString().trim());

                        while (rs.next()) {
                            StringBuilder rowData = new StringBuilder();
                            for (int i = 1; i <= columnCount; i++) {
                                rowData.append(rs.getString(i)).append("\t");
                            }
                            System.out.println(rowData.toString().trim());
                        }
                    } catch (SQLException e) {
                        System.out.println("Invalid query.");
                    }
                    break;
                }
                case 8: {
                    System.out.println("Goodbye.");
                    break;
                }
                default: {
                    System.out.println("Invalid option, please enter a number between 1 and 8.");
                    break;
                }
            }
        }
        scanner.close();
    }
}

