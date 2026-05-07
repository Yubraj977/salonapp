package edu.secourse.controller;

import edu.secourse.model.User;
import edu.secourse.service.UserService;
import edu.secourse.view.UserView;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

//Because UserController depend on console input through UserView ,
// a unit test needs some way to control what the view returns. so we use fake/stub classes
class UserControllerTest {

    @Test
    void startUserManagement_adminCanViewAllUsers() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        UserController controller = new UserController(userService, userView);

        User admin = new User("admin", "admin123", "Admin User", "admin@email.com", "admin");
        User customer = new User("jdoe", "jdoe123", "John Doe", "jdoe@email.com", "customer");

        userService.users.add(admin);
        userService.users.add(customer);

        // Option 2 = view all users, then 0 = exit
        userView.menuOptions.add(2);
        userView.menuOptions.add(0);

        controller.startUserManagement(admin);

        assertTrue(userService.getUsersCalled);
        assertEquals(2, userView.displayedUsers.size());
        assertTrue(userView.messages.contains("Exiting user management system..."));
    }

    static class FakeUserService extends UserService {
        List<User> users = new ArrayList<>();
        boolean getUsersCalled = false;

        @Override
        public List<User> getUsers() {
            getUsersCalled = true;
            return users;
        }
    }

    static class FakeUserView extends UserView {
        List<Integer> menuOptions = new ArrayList<>();
        List<User> displayedUsers = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        @Override
        public int showMenuAndGetOption(String role) {
            return menuOptions.remove(0);
        }

        @Override
        public void displayAllUsers(List<User> users) {
            displayedUsers = users;
        }

        @Override
        public void displayMessage(String message) {
            messages.add(message);
        }
    }
}