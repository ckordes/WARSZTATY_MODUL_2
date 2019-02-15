package warsztaty.modul2;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class Users {
    private int id;
    private String firstName;
    private String password;
    private String email;
    private int user_group_id;

    /**
     * getters and setters
     */

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public int getUser_group_id() {
        return user_group_id;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser_group_id(int user_group_id) {
        this.user_group_id = user_group_id;
    }

    /**
     * operacje bazodanowe dla ActiveRecord
     */

    public void saveToDB() {
        if (this.id == 0) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String insertQuery = "insert into users(email,password,firstname,user_group_id) values (?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, this.email);
                preparedStatement.setString(2, this.password);
                preparedStatement.setString(3, this.firstName);
                preparedStatement.setInt(4, this.user_group_id);
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

    public static Users[] loadAllUsers() {
        Users[] allUsers = new Users[0];
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectAllUsersQuery = "select * from users;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllUsersQuery);
            while (resultSet.next()) {
                Users user = new Users();
                user.email = resultSet.getString("email");
                user.firstName = resultSet.getString("firstName");
                user.id = resultSet.getInt("id");
                user.user_group_id = resultSet.getInt("user_group_id");
                allUsers = Arrays.copyOf(allUsers, allUsers.length + 1);
                allUsers[allUsers.length - 1] = user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    public void updateUser() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (this.id == 0) {
                saveToDB();
            } else {
                String sql = "UPDATE users SET email =? ,password=? ,firstname=?, user_group_id=? where id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, this.email);
                preparedStatement.setString(2, this.password);
                preparedStatement.setInt(4, this.user_group_id);
                preparedStatement.setString(3, this.firstName);
                preparedStatement.setInt(5, this.id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Users loadUserByID(int id) {
        Users user = new Users();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String update = "select * from users where id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user.id = rs.getInt("id");
                user.firstName = rs.getString("firstName");
                user.email = rs.getString("email");
                user.password = rs.getString("password");
                user.user_group_id = rs.getInt("user_group_id");
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUserByID(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (this.id != 0) {
                String sql = "DELETE FROM users WHERE id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                this.id = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
pobranie wszystkich członków danej grupy (dopisz metodę loadAllByGroupId do klasy User).
 */
    public static Users[] loadAllByGroupId(int id) {
        Users user = new Users();
        Users[] allUsers = new Users[0];
        try (Connection connection = DatabaseConnection.getConnection()) {
            String update = "select * from users" +
                    "join user_group on users.user_group_id = user_group.id" +
                    "WHERE user_group_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user.id = rs.getInt("id");
                user.email = rs.getString("email");
                user.firstName = rs.getString("firstName");
                user.password = rs.getString("password");
                user.user_group_id = rs.getInt("user_group_id");

                Arrays.copyOf(allUsers, allUsers.length + 1);
                allUsers[allUsers.length - 1] = user;
            }
            return allUsers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                        ", firstName='" + firstName + '\'' +
                        ", password='" + password + '\'' +
                        ", email='" + email + '\'' +
                        ", user_group_id='" + user_group_id + '\'' +
                        "\n";
    }

    public void runUsers() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        while (true) {
            System.out.println(Arrays.toString(loadAllUsers()));
            System.out.println("Wybierz jedna z opcji: ");
            System.out.println("    add – dodanie użytkownika,");
            System.out.println("    edit – edycja użytkownika,");
            System.out.println("    delete – usunięcie użytkownika,");
            System.out.println("    quit – zakończenie programu.");
            answer = scanner.next();
            if (answer.equals("add")) {
                Scanner scanAdd = new Scanner(System.in);

                System.out.println("Podaj imie: ");
                this.setFirstName(scanAdd.next());
                System.out.println("Podaj email: ");
                this.setEmail(scanAdd.next());
                System.out.println("Podaj haslo: ");
                this.setPassword(scanAdd.next());
                System.out.println("Podaj ID user group: ");
                this.setUser_group_id(scanAdd.nextInt());
                saveToDB();

//                jeśli wybrano add – program zapyta o wszystkie dane, występujące w klasie User, oprócz id,
            } else if (answer.equals("edit")) {
                Scanner scanAdd = new Scanner(System.in);
                System.out.println("Podaj ID: ");
                this.id = scanAdd.nextInt();
                System.out.println("Podaj imie: ");
                this.setFirstName(scanAdd.next());
                System.out.println("Podaj email: ");
                this.setEmail(scanAdd.next());
                System.out.println("Podaj haslo: ");
                this.setPassword(scanAdd.next());
                System.out.println("Podaj ID user group: ");
                this.setUser_group_id(scanAdd.nextInt());
                updateUser();
                //w przypadku edit – o wszystkie dane występujące w klasie User oraz id,
            } else if (answer.equals("delete")) {
                Scanner scanAdd = new Scanner(System.in);
                System.out.println("Podaj ID: ");
                this.id = scanAdd.nextInt();
                try {
                    deleteUserByID(this.id);
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

Zadanie 1
Program 1 – zarządzanie użytkownikami

Program po uruchomieniu wyświetli na konsoli listę wszystkich użytkowników.

Następnie wyświetli

"Wybierz jedną z opcji:

    add – dodanie użytkownika,
    edit – edycja użytkownika,
    delete – usunięcie użytkownika,
    quit – zakończenie programu."

Po wpisaniu i zatwierdzeniu jednej z opcji program odpyta o odpowiednie dane:

    jeśli wybrano add – program zapyta o wszystkie dane, występujące w klasie User, oprócz id,
    w przypadku edit – o wszystkie dane występujące w klasie User oraz id,
    po wybraniu delete – zapyta o id użytkownika, którego należy usunąć.

Po wykonaniu dowolnej z opcji, program ponownie wyświetli listę danych i zada pytanie o wybór opcji.

 */