package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {


    public UserDaoJDBCImpl() {}

    public void createUsersTable() {
        try (Connection con = Util.getConnection(); Statement state = con.createStatement()) {
            state.execute("CREATE TABLE IF NOT EXISTS user" +
                    "(id BIGINT(19) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(25) NOT NULL, lastname VARCHAR(45) NOT NULL, " +
                    "age TINYINT UNSIGNED NOT NULL) DEFAULT CHARSET=utf8");
        } catch (SQLException e) {
            System.out.println("Unable to create user table: " + e);
        }
    }

    public void dropUsersTable() {
        try (Connection con = Util.getConnection(); Statement state = con.createStatement()){
            state.execute("DROP TABLE IF EXISTS user");
        } catch (SQLException e) {
            System.out.println("Unable to drop user table: " + e);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection con = Util.getConnection(); PreparedStatement state = con.prepareStatement("INSERT INTO user (name, lastName, age) VALUES (?,?,?)")) {
            state.setString(1, name);
            state.setString(2, lastName);
            state.setByte(3, age);
            state.executeUpdate();
            System.out.println("User " + name + " is added to DB");
        } catch (SQLException e) {
            System.out.println("Unable to add " + name + " to user table: " + e);
        }
    }

    public void removeUserById(long id) {
        try (Connection con = Util.getConnection(); PreparedStatement state = con.prepareStatement("DELETE FROM user WHERE id = ?")) {
            state.setLong(1, id);
            state.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Unable to delete user with id " + id + ": " + e);
        }

    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection con = Util.getConnection(); PreparedStatement state = con.prepareStatement("SELECT * FROM user")){
            ResultSet resSet = state.executeQuery();
            while (resSet.next()) {
                User user = new User(resSet.getString(2), resSet.getString(3),
                        resSet.getByte(4));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Unable to get users from DB user: " + e);
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection con = Util.getConnection(); Statement state = con.createStatement()){
            state.execute("TRUNCATE TABLE user");
        } catch (SQLException e) {
            System.out.println("Unable to drop user table: " + e);
        }
    }
}
