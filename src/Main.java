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

        Departments departments = new Departments();
        Students students = new Students();
        Classes classes = new Classes();
        MajorMinor majorMinor = new MajorMinor();
        Taking taking = new Taking();

        departments.generateDepartments(mySt);

        students.generateStudents(mySt);

        classes.generateClasses(mySt);

        majorMinor.generateMajorsAndMinors(mySt);

        taking.generateIsTaking(mySt);


    }
}