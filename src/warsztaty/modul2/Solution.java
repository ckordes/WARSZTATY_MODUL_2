package warsztaty.modul2;

import sun.awt.image.MultiResolutionToolkitImage;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

import static warsztaty.modul2.Users.loadAllUsers;
import static warsztaty.modul2.Exercise.loadAllExercises;

public class Solution {
    private int id;
    private Date created;
    private Date updated;
    private String description;
    private int exercise_id;
    private int users_id;

    /**
     * getters and setters
     */

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getDescription() {
        return description;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    /**
     * operacje bazodanowe dla ActiveRecord
     */


    public void saveToDB() {
        if (this.id == 0) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String insertQuery = "insert into solution(created, description,exercise_id, users_id) values (now(),?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, this.description);
                preparedStatement.setInt(2, this.exercise_id);
                preparedStatement.setInt(3, this.users_id);
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

    public static Solution[] loadAllSolutions() {
        Solution[] allSolutions = new Solution[0];
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectAllExercises = "select * from solution;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllExercises);
            while (resultSet.next()) {
                Solution solution = new Solution();
                solution.created = resultSet.getDate("created");
                solution.updated = resultSet.getDate("updated");
                solution.description = resultSet.getString("description");
                solution.exercise_id = resultSet.getInt("exercise_id");
                solution.users_id = resultSet.getInt("users_id");
                solution.id = resultSet.getInt("id");

                allSolutions = Arrays.copyOf(allSolutions, allSolutions.length + 1);
                allSolutions[allSolutions.length - 1] = solution;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allSolutions;
    }

    public void updateSolution() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (this.id == 0) {
                saveToDB();
            } else {
                String sql = "UPDATE solution SET updated=now(), description=?,exercise_id=?, users_id=? where id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, this.description);
                preparedStatement.setInt(2, this.exercise_id);
                preparedStatement.setInt(3, this.users_id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Solution[] loadAllByUserId(int id) {
        Solution[] allSolutions = new Solution[0];

        try (Connection connection = DatabaseConnection.getConnection()) {
            String update = "select * from solution " +
                    "JOIN users on solution.users_id = users.id " +
                    "where users_id =?;";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Solution solution = new Solution();
                solution.id = rs.getInt("id");
                solution.created = rs.getDate("created");
                solution.updated = rs.getDate("updated");
                solution.description = rs.getString("description");
                solution.users_id = rs.getInt("users_id");
                solution.exercise_id = rs.getInt("exercise_id");

                allSolutions = Arrays.copyOf(allSolutions, allSolutions.length + 1);
                allSolutions[allSolutions.length - 1] = solution;
            }
            return allSolutions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allSolutions;
    }

    /*
    pobranie wszystkich rozwiązań danego zadania, posortowanych od najnowszego do najstarszego
    (dopisz metodę loadAllByExerciseId do klasy Solution),
     */

    public static Solution[] loadAllByExerciseId(int id) {
        Solution[] allSolutions = new Solution[0];

        try (Connection connection = DatabaseConnection.getConnection()) {
            String update = "select * from solution" +
                    "JOIN exercise on solution.exercise_id = exercise.id" +
                    "where exercise.id = ?" +
                    "order by updated desc;";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Solution solution = new Solution();
                solution.id = rs.getInt("id");
                solution.updated = rs.getDate("updated");
                solution.created = rs.getDate("created");
                solution.description = rs.getString("description");
                solution.users_id = rs.getInt("users_id");
                solution.exercise_id = rs.getInt("exercise_id");

                allSolutions = Arrays.copyOf(allSolutions, allSolutions.length + 1);
                allSolutions[allSolutions.length - 1] = solution;
            }
            return allSolutions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allSolutions;
    }

    public static Solution loadSolutionByID(int id) {
        Solution solution = new Solution();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String update = "select * from solution where id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                solution.id = rs.getInt("id");
                solution.description = rs.getString("description");
                solution.users_id = rs.getInt("users_id");
                solution.exercise_id = rs.getInt("exercise_id");
                solution.created = rs.getDate("created");
                solution.updated = rs.getDate("updated");
            }
            return solution;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solution;
    }

    public void deleteSolutionByID(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (this.id != 0) {
                String sql = "DELETE FROM solution WHERE id=?";
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
                        ", utworzono='" + created + '\'' +
                        ", zaktualizowano='" + updated + '\'' +
                        ", opis='" + description + '\'' +
                        ", numer zadania='" + exercise_id + '\'' +
                        ", numer osoby='" + users_id + '\'' +
                        "\n";
    }
/*
    private int id;
    private Date created;
    private Date updated;
    private String description;
    private int exercise_id;
    private int users_id;
 */


    public void runSolution() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        while (true) {
            System.out.println("Wybierz jedna z opcji: ");
            System.out.println("    add – przypisywanie zadań do użytkowników,");
            System.out.println("    view – przeglądanie rozwiązań danego użytkownika,");
            System.out.println("    quit – zakończenie programu.");
            answer = scanner.next();
            if (answer.equals("add")) {
                Scanner scanAdd = new Scanner(System.in);
                Scanner scanAdd2 = new Scanner(System.in);
//                jeśli wybrano add – wyświetli listę wszystkich użytkowników, odpyta o id,
//                następnie wyświetli listę wszystkich zadań i zapyta o id zadania, utworzy i zapisze obiekt typu Solution.
                System.out.println(Arrays.toString(loadAllUsers()));
                System.out.println("Podaj id uzytkownika: ");
                this.users_id = (scanAdd.nextInt());
                System.out.println(Arrays.toString(loadAllExercises()));
                System.out.println("Podaj ID zadania: ");
                this.exercise_id = scanAdd.nextInt();
//                System.out.println("Podaj opis: ");
//                this.setDescription(scanAdd2.nextLine());
                saveToDB();

            } else if (answer.equals("view")) {
                Scanner scanAdd = new Scanner(System.in);
                Solution[] allSolutions;
                int userID;
                System.out.println(Arrays.toString(loadAllUsers()));
                System.out.println("Podaj ID uzytkownika: ");
                userID = scanAdd.nextInt();
                allSolutions = loadAllByUserId(userID);
                for (int i = 0; i < allSolutions.length; i++) {
                    System.out.println("ID zadania: " + allSolutions[i].exercise_id +
                            ", opis: " + allSolutions[i].description +
                            ", utworzono: " + allSolutions[i].created +
                            ", zaaktualizowano: " + allSolutions[i].updated);
                }
            } else if (answer.equals("quit")) {
                break;
            } else {
                System.out.println("Bledny wybor! Wybierz podobnie!");
            }

        }
    }
}

/*


Po wybraniu odpowiedniej opcji, program odpyta o dane i wykona odpowiednią operację:

    po wybraniu add – wyświetli listę zadań, do których Użytkownik nie dodał jeszcze rozwiązania,
    a następnie odpyta o id zadania, do którego ma zostać dodane rozwiązanie.
    Pole created zostanie wypełnione automatycznie, więc Użytkownik zostanie odpytany jeszcze tylko o rozwiązanie zadania,

    w przypadku wybrania quit – program zakończy działanie.

Dla uproszczenia przyjmujemy, że dodanego rozwiązania nie możemy usuwać, ani edytować.

W przypadku próby dodania rozwiązania, które już istnieje czyli Użytkownik poda id z zakresu innego niż zaprezentowany w programie, program ma wyświetlić odpowiedni komunikat.

 */

/*

Zadanie 4
Program 4 – przypisywanie zadań

Program po uruchomieniu wyświetli w konsoli napis

"Wybierz jedną z opcji:

    add – przypisywanie zadań do użytkowników,
    view – przeglądanie rozwiązań danego użytkownika,
    quit – zakończenie programu."

Po wpisaniu i zatwierdzeniu odpowiedniej opcji program odpyta o dane:

    jeśli wybrano add – wyświetli listę wszystkich użytkowników, odpyta o id,
    następnie wyświetli listę wszystkich zadań i zapyta o id zadania, utworzy i zapisze obiekt typu Solution.

    Pole created wypełni się automatycznie, a pola updated i description mają zostać puste.


    view – zapyta o id użytkownika, którego rozwiązania chcemy zobaczyć.

Po wykonaniu dowolnej z opcji, program ponownie zada pytanie o wybór opcji.

 */