package warsztaty.modul2;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class UserGroup {
    private int id;
    private String name;

    /**
     * getters and setters
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * operacje bazodanowe dla ActiveRecord
     */
    public void saveToDB() {
        if (this.id == 0) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String insertQuery = "insert into user_group(name) values (?);";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, this.name);
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

    public static UserGroup[] loadAllUserGroups() {
        UserGroup[] allUserGroups = new UserGroup[0];
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectAllUsersGroupQuery = "select * from user_group;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllUsersGroupQuery);
            while (resultSet.next()) {
                UserGroup userGroup = new UserGroup();
                userGroup.name = resultSet.getString("name");
                userGroup.id = resultSet.getInt("id");
                allUserGroups = Arrays.copyOf(allUserGroups, allUserGroups.length + 1);
                allUserGroups[allUserGroups.length - 1] = userGroup;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUserGroups;
    }

    public void updateUserGroup() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (this.id == 0) {
                saveToDB();
            } else {
                String sql = "UPDATE user_group SET name=? where id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, this.name);
                preparedStatement.setInt(2, this.id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static UserGroup loadUserGroupByID(int id) {
        UserGroup userGroup = new UserGroup();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String update = "select * from user_group where id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userGroup.id = rs.getInt("id");
                userGroup.name = rs.getString("name");
            }
            return userGroup;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userGroup;
    }


    public void deleteUserGroupByID(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (this.id != 0) {
                String sql = "DELETE FROM user_group WHERE id=?";
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
                        ", nazwa='" + name + '\'' +
                        "\n";
    }

    public void runUserGroups() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        while (true) {
            System.out.println(Arrays.toString(loadAllUserGroups()));
            System.out.println("Wybierz jedna z opcji: ");
            System.out.println("    add – dodanie użytkownika,");
            System.out.println("    edit – edycja użytkownika,");
            System.out.println("    delete – usunięcie użytkownika,");
            System.out.println("    quit – zakończenie programu.");
            answer = scanner.next();
            if (answer.equals("add")) {
                Scanner scanAdd = new Scanner(System.in);

                System.out.println("Podaj nazwe grupy: ");
                this.setName(scanAdd.nextLine());
                saveToDB();

            } else if (answer.equals("edit")) {
                Scanner scanAdd = new Scanner(System.in);
                Scanner scanAdd2 = new Scanner(System.in);
                System.out.println("Podaj ID: ");
                this.id = scanAdd.nextInt();
                System.out.println("Podaj nazwe: ");
                this.setName(scanAdd2.nextLine());
                updateUserGroup();
            } else if (answer.equals("delete")) {
                Scanner scanAdd = new Scanner(System.in);
                System.out.println("Podaj ID: ");
                this.id = scanAdd.nextInt();
                try {
                    deleteUserGroupByID(this.id);
                } catch (SQLException e) {
                    e.printStackTrace();
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

Zadanie 3
Program 3 – zarządzanie grupami

Program po uruchomieniu wyświetli na konsoli listę wszystkich grup.

Następnie wyświetli w konsoli napis

"Wybierz jedną z opcji:

    add – dodanie grupy,
    edit – edycja grupy,
    delete – edycja grupy,
    quit – zakończenie programu."

Po wpisaniu i zatwierdzeniu odpowiedniej opcji program odpyta o dane i wykona odpowiednią operacje:

    add – wszystkie dane występujące w klasie Group, bez id,
    edit – wszystkie dane występujące w klasie Group oraz id,
    delete – id grupy którą należy usunąć.

Po wykonaniu dowolnej z opcji, program ponownie wyświetli listę danych i zada pytanie o wybór opcji.

 */