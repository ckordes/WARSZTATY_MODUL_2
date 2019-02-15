package warsztaty.modul2;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class Exercise {
    private int id;
    private String title;
    private String description;

    /***
     * getters and setters
     */
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * operacje bazodanowe dla ActiveRecord
     */

    public void saveToDB() {
        if (this.id == 0) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String insertQuery = "insert into exercise(title, description) values (?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, this.title);
                preparedStatement.setString(2, this.description);
                preparedStatement.executeUpdate();

                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Exercise[] loadAllExercises() {
        Exercise[] allExercises = new Exercise[0];
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectAllExercises = "select * from exercise;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllExercises);
            while (resultSet.next()) {
                Exercise exercise = new Exercise();
                exercise.title = resultSet.getString("title");
                exercise.description = resultSet.getString("description");
                exercise.id = resultSet.getInt("id");
                allExercises = Arrays.copyOf(allExercises, allExercises.length + 1);
                allExercises[allExercises.length - 1] = exercise;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allExercises;
    }

    public void updateExercise() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (this.id == 0) {
                saveToDB();
            } else {
                String sql = "UPDATE exercise SET title=?, description=? where id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, this.title);
                preparedStatement.setString(2, this.description);
                preparedStatement.setInt(3, this.id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Exercise loadExerciseByID(int id) {
        Exercise exercise = new Exercise();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String update = "select * from exercise where id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                exercise.id = rs.getInt("id");
                exercise.title = rs.getString("title");
                exercise.description = rs.getString("description");
            }
            return exercise;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercise;
    }

    public void deleteExerciseByID(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (this.id != 0) {
                String sql = "DELETE FROM exercise WHERE id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                this.id = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                        ", tytul='" + title + '\'' +
                        ", opis='" + description + '\'' +
                        "\n";
    }

    public void runExercise() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        while (true) {
            System.out.println(Arrays.toString(loadAllExercises()));
            System.out.println("Wybierz jedna z opcji: ");
            System.out.println("    add – dodanie użytkownika,");
            System.out.println("    edit – edycja użytkownika,");
            System.out.println("    delete – usunięcie użytkownika,");
            System.out.println("    quit – zakończenie programu.");
            answer = scanner.next();
            if (answer.equals("add")) {
                Scanner scanAdd = new Scanner(System.in);
                Scanner scanAdd2 = new Scanner(System.in);

                System.out.println("Podaj tytul: ");
                this.setTitle(scanAdd.nextLine());
                System.out.println("Podaj opis: ");
                this.setDescription(scanAdd2.nextLine());
                saveToDB();

            } else if (answer.equals("edit")) {
                Scanner scanAdd = new Scanner(System.in);
                Scanner scanAdd2 = new Scanner(System.in);
                Scanner scanAdd3 = new Scanner(System.in);
                System.out.println("Podaj ID: ");
                this.id = scanAdd.nextInt();
                System.out.println("Podaj tytul: ");
                this.setTitle(scanAdd2.nextLine());
                System.out.println("Podaj opis: ");
                this.setDescription(scanAdd3.nextLine());
                updateExercise();
            } else if (answer.equals("delete")) {
                Scanner scanAdd = new Scanner(System.in);
                System.out.println("Podaj ID: ");
                this.id = scanAdd.nextInt();
                try {
                    deleteExerciseByID(this.id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //    po wybraniu delete – zapyta o id użytkownika, którego należy usunąć.
            } else if (answer.equals("quit")) {
                break;
            } else {
                System.out.println("Bledny wybor! Wybierz podobnie!");
            }

        }
    }

}
/*

Zadanie 2
Program 2 – zarządzanie zadaniami

Program po uruchomieniu wyświetli na konsoli listę wszystkich zadań.

Następnie wyświetli w konsoli napis

"Wybierz jedną z opcji:

    add – dodanie zadania,
    edit – edycja zadania,
    delete – edycja zadania,
    quit – zakończenie programu."

Po wpisaniu i zatwierdzeniu odpowiedniej opcji program odpyta o następujące dane:

    w przypadku add – o wszystkie dane występujące w klasie Exercise bez id,
    po wybraniu edit – wszystkie dane występujące w klasie Exercise oraz id,
    jeśli wybrano delete – zapyta o id zadania które należy usunąć.

Po wykonaniu dowolnej z opcji, program ponownie wyświetli listę danych i zada pytanie o wybór opcji.

 */