package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {
    public static void main(String[] args) {

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
