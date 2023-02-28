package com.spring.auth.repository.impl;

import com.spring.auth.model.Role;
import com.spring.auth.model.User;
import com.spring.auth.repository.ConnectionBuilder;
import com.spring.auth.repository.UserRepository;
import com.spring.auth.repository.UserRepositoryException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private ConnectionBuilder connectionPool;

    private final String salt = BCrypt.gensalt();
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());
    public static final String COLUMN_LABEL_NAME = "name";
    public static final String COLUMN_LABEL_SURNAME = "surname";
    public static final String COLUMN_LABEL_EMAIL = "email";
    public static final String COLUMN_LABEL_LOGIN = "login";
    public static final String COLUMN_LABEL_PASSWORD = "password";
    public static final String COLUMN_LABEL_ROLE = "role_name";
    public static final String COLUMN_LABEL_REGISTER_DATE = "register_date";
    public static final String COLUMN_LABEL_IS_ACTIVE = "is_active";
    public static final String COLUMN_LABEL_ID = "id";

    private UserRepositoryImpl() {

    }

    private static final String GET_USER_ID_QUERY = "SELECT id, password FROM users WHERE login = ?;";

    @Override
    public int takeUsersIdByLogin(String login, String password) throws UserRepositoryException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_ID_QUERY)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                int id = 0;
                if (resultSet.next()) {
                    String storedHash = resultSet.getString("password");
                    if (verifyPassword(password, storedHash)) {
                        id = resultSet.getInt("id");
                    }
                }
                return id;
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.INFO, "Problems with taking info from data or another exception occurred", e);
            throw new UserRepositoryException("Database getting info problems" ,e);
        }
    }

    private static final String CHECK_IS_LOGIN_EXISTS_QUERY = "SELECT id FROM users WHERE login = ?;";

    public boolean checkIsLoginExists(String login) throws UserRepositoryException {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_IS_LOGIN_EXISTS_QUERY)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.INFO, "Problems with taking info from data or another exception occurred", e);
            throw new UserRepositoryException("Database getting info problems" ,e);
        }
    }

    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users JOIN user_details ON " +
            "user_details.Users_id = users.id\n JOIN role ON  users.role_id = role.id\n WHERE users.id = ?;";

    @Override
    public User takeUserById(int id) throws UserRepositoryException {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString(COLUMN_LABEL_NAME);
                    String surname = resultSet.getString(COLUMN_LABEL_SURNAME);
                    String email = resultSet.getString(COLUMN_LABEL_EMAIL);
                    String login = resultSet.getString(COLUMN_LABEL_LOGIN);
                    String password = resultSet.getString(COLUMN_LABEL_PASSWORD);
                    String userRole = resultSet.getString(COLUMN_LABEL_ROLE).toUpperCase();
                    Date registerDate = resultSet.getDate(COLUMN_LABEL_REGISTER_DATE);
                    int isActive = resultSet.getInt(COLUMN_LABEL_IS_ACTIVE);
                    user = new User(
                            id,
                            name,
                            surname,
                            email,
                            login,
                            password,
                            Role.valueOf(userRole),
                            registerDate,
                            isActive != 0);
                }
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.INFO, "Problems with taking info from data or another exception occurred", e);
            throw new UserRepositoryException("Database getting info problems" ,e);
        }
        return user;
    }

    private static final String INSERT_USER_MAIN_DATA_QUERY = "INSERT INTO users " +
            "(login, password, email, role_id, is_active)  VALUES (?, ?, ?, ?, ?);";
    private static final String INSERT_USER_DETAILS_QUERY = "INSERT INTO user_details " +
            "(users_id, name, surname, register_date)  VALUES (?, ?, ?, ?);";

    @Override
    public void addNewUser(User user) throws UserRepositoryException {
        int roleId = takeUsersRole(user);
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statementOne = connection.prepareStatement(INSERT_USER_MAIN_DATA_QUERY,
                    Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement statementTwo = connection.prepareStatement(INSERT_USER_DETAILS_QUERY)) {
                statementOne.setString(1, user.getLogin());
                statementOne.setString(2, BCrypt.hashpw(user.getPassword(), salt));
                statementOne.setString(3, user.getEmail());
                statementOne.setInt(4, roleId);
                statementOne.setInt(5, user.isActive() ? 1 : 0);
                statementOne.executeUpdate();
                try (ResultSet generatedKeys = statementOne.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        statementTwo.setInt(1, userId);
                        statementTwo.setString(2, user.getName());
                        statementTwo.setString(3, user.getSurname());
                        statementTwo.setDate(4, new java.sql.Date(user.getRegisterDate().getTime()));
                        statementTwo.executeUpdate();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.log(Level.INFO, "Problems with ending transaction or another exception occurred", e);
                throw new UserRepositoryException("Database adding info problems" ,e);
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.INFO, "Problems with adding info to data or another exception occurred", e);
            throw new UserRepositoryException("Database adding info problems" ,e);
        }
    }

    public static final String GET_USER_ROLE_QUERY = "SELECT id FROM news_service_db.role where role_name = ?;";

    private int takeUsersRole(User user) throws UserRepositoryException {
        int roleId = 0;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_ROLE_QUERY)) {
            statement.setString(1, user.getRole().toString().toLowerCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    roleId = resultSet.getInt(COLUMN_LABEL_ID);
                }
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.INFO, "Problems with taking info from data or another exception occurred", e);
            throw new UserRepositoryException("Database getting info problems" ,e);
        }
        return roleId;
    }

    private boolean verifyPassword(String enteredPassword, String storedHash) {
        return BCrypt.checkpw(enteredPassword, storedHash);
    }

    public Connection getConnection() throws SQLException, InterruptedException {
        return connectionPool.takeConnection();
    }

}
