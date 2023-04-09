import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
public class Main {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream("src/db.properties")) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("Database information not provided.");
        }
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        Connection myConn = DriverManager.getConnection(url, username, password);
        Statement mySt = myConn.createStatement();

        Departments departments = new Departments();
        Students students = new Students();
        Classes classes = new Classes();
        MajorMinor majorMinor = new MajorMinor();
        TakingTaken takingTaken = new TakingTaken();

//        departments.generateDepartments(mySt);
//
//        students.generateStudents(mySt);
//
//        classes.generateClasses(mySt);
//
//        majorMinor.generateMajorsAndMinors(mySt);

        takingTaken.generateIsTaking(mySt);

    }
}