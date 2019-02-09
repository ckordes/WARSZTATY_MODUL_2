package warsztaty.modul2;

import java.sql.*;
import java.util.Arrays;

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

}
