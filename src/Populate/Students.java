package Populate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

public class Students {

    public Students() {}
    public void generateStudents(Statement statement) throws SQLException {
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
}
