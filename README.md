# CS336-Assigntment-3
Create and populate a database. Database is populated with JDBC. The database can be queried for specific information.

## CollegeDB
This file contains the user query software, which allows the user to query different pieces of information based on the option they select:
1. Search students by name.
2. Search students by year.
3. Search for students with a GPA >= threshold.
4. Search for students with a GPA <= threshold.
5. Get department statistics.
6. Get class statistics.
7. Execute an abitrary SQL query.
8. Exit the application.

Most (if not all) errors are handled so that invalid inputs will not cause the application to break. The way to run this is by inputting the database url, username, and password as command line arguments in that order. A while loop and a switch statement work together to keep asking the user the option and necessary information required to run the queries.

## Data Population:
All methods to populate the database are found in the populate package. The database url, username, and password are stored in the properties file, which are read in the Populate "main" method. This is to ensure the username and password are hidden.

### Students:
Populates the Students table in the College database. There are two arrays, including 10 different first name options and 10 different last name options. The names are not selected randomly, but selected through each iteration at the corresponding index. The ID is randomly selected, and a hashmap is used to keep track of previously generated IDs since they must be unique. A prepared statement is used to insert the student information into the database, generating unique students.
### Classes:
Populates the Classes table in the College database. The classes are defined as instance variable arrays, so that they can be accessed easily later on. The generateClasses() method uses prepared statements to insert data into the table. The actual class is selected randomly, and once it is selected it is removed from that instance of the class so that it cannot be reused.

### Departments:
Populates the Departments table in the College database. The department names and campus names are stored in a String array, which is then inserted into the database using a prepared statement.

### MajorMinor:
Populates both the Major and Minor tables of the College database. An arraylist keeps track of the different majors and minors available. The amount of majors to be inserted is randomly selected, which is either 1 or 2. Then, a prepared statement is used to insert the majors into the database. When a major is inserted, it is removed from the arraylist so that repeats cannot form. The same logic goes for the minors, except the students majors are deleted from the minors list before selection to ensure uniqueness. The number of minors to be inserted ranges from 0 to 2.

### TakingTaken:
Populates both the IsTaking and HasTaken tables of the College database. Prepared statements are used to insert the classes to the table. For each student ID, a number of 3 to 5 unique classes is inserted into the table. Then, a random number of taken classes is inserted based on the rank which is randomly assigned to a student. To ensure even distribution of the ranks, a hashmap keeps track of the number of ranks currently inserted. If the number is >= 25, the rank is rerolled until one that does not exceed 25 is chosen. The grade is randomly assigned as well, but the odds are not even for each. There is the largest chance of getting a B, with smaller chances on D's and F's.

### Populate: 

The actual "main" method which runs all of these generate methods. Also sets up the connection to the database and sets up the universal statement used in every method.