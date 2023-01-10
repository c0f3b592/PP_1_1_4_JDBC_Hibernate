package jm.task.core.jdbc.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import jm.task.core.jdbc.model.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection;

    public UserDaoJDBCImpl() {}

    public UserDaoJDBCImpl(Connection connection) {

        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createUsersTable() {
        String createTableUsersSQL = "create table users (\n" +
                "    id bigint auto_increment,\n" +
                "    name varchar(255),\n" +
                "    lastName varchar(255),\n" +
                "    age tinyint,\n" +
                "    primary key (id))";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableUsersSQL);
        } catch (SQLException e) {
            if (e.getErrorCode() != MysqlErrorNumbers.ER_TABLE_EXISTS_ERROR) {
                e.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
        String dropTableUsersSQL = "drop table users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableUsersSQL);
        } catch (SQLException e) {
            if (e.getErrorCode() != MysqlErrorNumbers.ER_BAD_TABLE_ERROR) {
                e.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String saveUsersSQL = "insert into users (name, lastName, age)\n" +
                    "values ('" + name + "', '" + lastName + "', " + age + ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(saveUsersSQL);
            connection.commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        String removeUserByIdSQL = "delete from users where id=" + id;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(removeUserByIdSQL);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        String getAllUsersSQL = "select * from users";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getAllUsersSQL);
            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                byte age = resultSet.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cleanUsersTable() {
        String cleanUsersTableSQL = "delete from users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(cleanUsersTableSQL);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }
}
