package jm.task.core.jdbc;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.logging.Level;


public class Main {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        UserService service = new UserServiceImpl();

        service.createUsersTable();
        service.saveUser("A", "Aa", (byte) 12);
        service.saveUser("B", "Bb", (byte) 24);
        service.saveUser("C", "Cc", (byte) 34);
        service.saveUser("qwe", "Dd", (byte) 56);
        for (User user : service.getAllUsers()) {
            System.out.println(user);
        }
        service.cleanUsersTable();
        service.dropUsersTable();

    }
}
