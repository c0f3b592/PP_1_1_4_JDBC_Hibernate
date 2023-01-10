package jm.task.core.jdbc.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import jm.task.core.jdbc.model.User;
import org.hibernate.*;
import org.hibernate.exception.SQLGrammarException;
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
        String createTableUsersSQL = "create table users (\n" +
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
        String dropTableUsersSQL = "drop table users";
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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String removeUserByIdHQL = "delete from User where id = :id";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery(removeUserByIdHQL).setParameter("id", id).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String getAllUsersHQL = "select user from User user";
        List<User> list = new ArrayList<>();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            list = session.createQuery(getAllUsersHQL, User.class).getResultList();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        String cleanUsersTableHQL = "delete from User a";
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery(cleanUsersTableHQL).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
