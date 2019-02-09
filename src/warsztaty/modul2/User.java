package warsztaty.modul2;

import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.Arrays;

public class User {
    private int id;
    private String firstName;
    private String password;
    private String email;

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

    /**
     * operacje bazodanowe dla ActiveRecord
     */

    public void saveToDB() {
        if (this.id == 0) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String insertQuery = "insert into users(email,password,firstname) values (?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, this.email);
                preparedStatement.setString(2, this.password);
                preparedStatement.setString(3, this.firstName);
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

    public static User[] loadAllUsers() {
        User[] allUsers = new User[0];
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectAllUsersQuery = "select * from users;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllUsersQuery);
            while (resultSet.next()) {
                User user = new User();
                user.email = resultSet.getString("email");
                user.firstName = resultSet.getString("firstName");
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
                String sql = "UPDATE users SET email =? ,password=? ,firstname=? where id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, this.email);
                preparedStatement.setString(2, this.password);
                preparedStatement.setString(3, this.password);
                preparedStatement.setInt(4, this.id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User loadUserByID(int id) {
        User user = new User();
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
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public  void deleteUserByID(int id)throws SQLException{
        try(Connection connection = DatabaseConnection.getConnection()){
                if (this.id != 0) {
                    String sql = "DELETE FROM users WHERE id=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    this.id = 0;
                }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
