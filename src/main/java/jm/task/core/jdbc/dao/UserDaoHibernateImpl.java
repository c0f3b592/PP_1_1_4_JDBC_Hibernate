package jm.task.core.jdbc.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import jm.task.core.jdbc.model.User;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.jdbc.Work;

import javax.persistence.PersistenceException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory;
    public UserDaoHibernateImpl() {}

    public UserDaoHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void createUsersTable() {
        String createTableUsersSQL = "create table schema1_1_5.users (\n" +
                "    id bigint auto_increment,\n" +
                "    name varchar(255),\n" +
                "    lastName varchar(255),\n" +
                "    age tinyint,\n" +
                "    primary key (id))";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(createTableUsersSQL).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (e.getCause().getClass() == SQLGrammarException.class) {
                if (((SQLGrammarException) e.getCause()).getErrorCode() != MysqlErrorNumbers.ER_TABLE_EXISTS_ERROR) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void dropUsersTable() {
        String dropTableUsersSQL = "drop table schema1_1_5.users";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(dropTableUsersSQL).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (e.getCause().getClass() == SQLGrammarException.class) {
                if (((SQLGrammarException) e.getCause()).getErrorCode() != MysqlErrorNumbers.ER_BAD_TABLE_ERROR) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String saveUsersSQL = "insert into schema1_1_5.users (name, lastName, age)\n" +
                "values ('" + name + "', '" + lastName + "', " + age + ")";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(saveUsersSQL).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String removeUserByIdSQL = "delete from schema1_1_5.users where id=" + id;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(removeUserByIdSQL).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String getAllUsersSQL = "select * from schema1_1_5.users";
        List<User> list = new ArrayList<>();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            List<Object[]> rows = session.createSQLQuery(getAllUsersSQL).list();

            for (Object[] row : rows) {
                User user = new User(row[1].toString(), row[2].toString(), Byte.decode(row[3].toString()));
                list.add(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        String cleanUsersTableSQL = "delete from schema1_1_5.users";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(cleanUsersTableSQL).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
