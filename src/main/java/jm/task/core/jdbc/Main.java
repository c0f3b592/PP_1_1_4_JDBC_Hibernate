package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Properties;
import java.util.logging.Level;


public class Main {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        UserServiceImpl service = new UserServiceImpl();
        service.createUsersTable();
        service.saveUser("A", "Aa", (byte) 12);
        service.saveUser("B", "Bb", (byte) 24);
        service.saveUser("C", "Cc", (byte) 34);
        service.saveUser("D", "Dd", (byte) 56);
        for (User user : service.getAllUsers()) {
            System.out.println(user.toString());
        }
        service.cleanUsersTable();
        service.dropUsersTable();

    }
}
