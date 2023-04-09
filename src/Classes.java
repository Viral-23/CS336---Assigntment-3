import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Classes {

    public Classes() {}
    private String[] threeCreditClasses = {
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
    private String[] fourCreditClasses = {
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
    public void generateClasses(Statement statement) throws SQLException {

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

    public String getRandomClass(int credits) {
        String className = "";
        Random rand = new Random();
        int index;
        if (credits == 3) {
            index = rand.nextInt(threeCreditClasses.length);
            className = threeCreditClasses[index];
            removeClass(className, threeCreditClasses, true);
        }

        if (credits == 4) {
            index = rand.nextInt(fourCreditClasses.length);
            className = fourCreditClasses[index];
            removeClass(className, fourCreditClasses, false);
        }

        return className;
    }

    private void removeClass(String className, String[] classes, boolean three) {
        String[] temp = new String[classes.length - 1];
        for (int i = 0; i < classes.length - 1; i++) {
            if (classes[i].equals(className))
                continue;
            temp[i] = classes[i];
        }
        if (three)
            threeCreditClasses = temp;
        else
            fourCreditClasses = temp;
    }

}
