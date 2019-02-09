package warsztaty.modul2;

import java.sql.*;
import java.util.Arrays;

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
}
