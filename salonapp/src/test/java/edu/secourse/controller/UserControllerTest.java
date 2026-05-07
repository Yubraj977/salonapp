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

import edu.secourse.model.User;
import edu.secourse.service.UserService;
import edu.secourse.view.UserView;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void nullUserReturnsImmediately() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        UserController controller = new UserController(userService, userView);

        controller.startUserManagement(null);

        assertTrue(userView.messages.isEmpty());
    }

    @Test
    void adminCanCreateUserSuccessfully() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        User newUser = new User("jdoe", "pass123", "John Doe", "jdoe@email.com", "customer");

        userView.menuOptions.addAll(List.of(1, 0));
        userView.userToCreate = newUser;
        userService.createResult = true;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertSame(newUser, userService.createdUser);
        assertTrue(userView.messages.contains("Successfully created user"));
    }

    @Test
    void adminCreateUserCanFail() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(1, 0));
        userView.userToCreate = new User("jdoe", "pass123", "John Doe", "jdoe@email.com", "customer");
        userService.createResult = false;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertTrue(userView.messages.contains("User could not be created"));
    }

    @Test
    void adminCanViewAllUsers() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        User admin = adminUser();
        User customer = customerUser();

        userService.users.addAll(List.of(admin, customer));
        userView.menuOptions.addAll(List.of(2, 0));

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(admin);

        assertTrue(userService.getUsersCalled);
        assertEquals(2, userView.displayedUsers.size());
        assertTrue(userView.messages.contains("Exiting user management system..."));
    }

    @Test
    void adminCanFindUserByIdWhenFound() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        User customer = customerUser();

        userView.menuOptions.addAll(List.of(3, 0));
        userView.accountIds.add(10);
        userService.userById = Optional.of(customer);

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertEquals(10, userService.lastUserId);
        assertSame(customer, userView.displayedUser);
    }

    @Test
    void adminFindUserByIdShowsNotFound() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(3, 0));
        userView.accountIds.add(99);
        userService.userById = Optional.empty();

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertEquals(99, userService.lastUserId);
        assertTrue(userView.messages.contains("User not found"));
    }

    @Test
    void adminCanUpdateUserSuccessfully() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(4, 0));
        userView.accountIds.add(5);
        userView.updatedNames.add("Updated Name");
        userView.updatedEmails.add("updated@email.com");
        userView.updatedRoles.add("stylist");
        userService.updateResult = true;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertEquals(5, userService.lastUpdatedId);
        assertEquals("Updated Name", userService.lastUpdatedName);
        assertEquals("updated@email.com", userService.lastUpdatedEmail);
        assertEquals("stylist", userService.lastUpdatedRole);
        assertTrue(userView.messages.contains("User updated successfully!"));
    }

    @Test
    void adminUpdateUserCanFail() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(4, 0));
        userView.accountIds.add(5);
        userView.updatedNames.add("Updated Name");
        userView.updatedEmails.add("updated@email.com");
        userView.updatedRoles.add("stylist");
        userService.updateResult = false;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertTrue(userView.messages.contains("User not found!"));
    }

    @Test
    void adminCanDeleteUserSuccessfully() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(5, 0));
        userView.accountIds.add(7);
        userService.deleteResult = true;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertEquals(7, userService.lastDeletedId);
        assertTrue(userView.messages.contains("Successfully deleted user"));
    }

    @Test
    void adminDeleteUserCanFail() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(5, 0));
        userView.accountIds.add(7);
        userService.deleteResult = false;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertTrue(userView.messages.contains("Error deleting user"));
    }

    @Test
    void adminCanChangePasswordSuccessfully() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(6, 0));
        userView.usernames.add("admin");
        userView.oldPasswords.add("oldpass");
        userView.newPasswords.add("newpass");
        userService.changePasswordResult = true;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertEquals("admin", userService.lastPasswordUsername);
        assertEquals("oldpass", userService.lastOldPassword);
        assertEquals("newpass", userService.lastNewPassword);
        assertTrue(userView.messages.contains("Password changed successfully"));
    }

    @Test
    void adminChangePasswordCanFail() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(6, 0));
        userView.usernames.add("admin");
        userView.oldPasswords.add("wrong");
        userView.newPasswords.add("newpass");
        userService.changePasswordResult = false;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertTrue(userView.messages.contains("Password change failed"));
    }

    @Test
    void adminInvalidOptionShowsMessage() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(99, 0));

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertTrue(userView.messages.contains("Invalid option selected."));
    }

    @Test
    void customerCanChangePasswordSuccessfully() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(1, 0));
        userView.usernames.add("customer");
        userView.oldPasswords.add("oldpass");
        userView.newPasswords.add("newpass");
        userService.changePasswordResult = true;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(customerUser());

        assertEquals("customer", userService.lastPasswordUsername);
        assertTrue(userView.messages.contains("Password changed successfully"));
    }

    @Test
    void stylistChangePasswordCanFail() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(1, 0));
        userView.usernames.add("stylist");
        userView.oldPasswords.add("wrong");
        userView.newPasswords.add("newpass");
        userService.changePasswordResult = false;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(stylistUser());

        assertEquals("stylist", userService.lastPasswordUsername);
        assertTrue(userView.messages.contains("Password change failed"));
    }

    @Test
    void customerInvalidOptionShowsMessage() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(99, 0));

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(customerUser());

        assertTrue(userView.messages.contains("Invalid option selected."));
    }

    @Test
    void numberFormatExceptionIsHandled() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.throwNumberFormatOnce = true;
        userView.menuOptions.add(0);

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertTrue(userView.messages.contains("Please enter a valid number"));
    }

    @Test
    void illegalArgumentExceptionIsHandled() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(1, 0));
        userView.throwIllegalArgumentOnCreate = true;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertTrue(userView.messages.contains("Username cannot be empty"));
    }

    @Test
    void generalExceptionIsHandled() {
        FakeUserService userService = new FakeUserService();
        FakeUserView userView = new FakeUserView();

        userView.menuOptions.addAll(List.of(1, 0));
        userView.throwRuntimeExceptionOnCreate = true;

        UserController controller = new UserController(userService, userView);
        controller.startUserManagement(adminUser());

        assertTrue(userView.messages.contains("Error: Unexpected error"));
    }

    private User adminUser() {
        return new User("admin", "admin123", "Admin User", "admin@email.com", "admin");
    }

    private User customerUser() {
        return new User("customer", "pass123", "Customer User", "customer@email.com", "customer");
    }

    private User stylistUser() {
        return new User("stylist", "pass123", "Stylist User", "stylist@email.com", "stylist");
    }

    static class FakeUserService extends UserService {
        List<User> users = new ArrayList<>();

        boolean getUsersCalled;
        boolean createResult;
        boolean updateResult;
        boolean deleteResult;
        boolean changePasswordResult;

        User createdUser;
        Optional<User> userById = Optional.empty();

        int lastUserId;
        int lastUpdatedId;
        int lastDeletedId;

        String lastUpdatedName;
        String lastUpdatedEmail;
        String lastUpdatedRole;

        String lastPasswordUsername;
        String lastOldPassword;
        String lastNewPassword;

        @Override
        public boolean createUser(User user) {
            createdUser = user;
            return createResult;
        }

        @Override
        public List<User> getUsers() {
            getUsersCalled = true;
            return users;
        }

        @Override
        public Optional<User> getUserById(int id) {
            lastUserId = id;
            return userById;
        }

        @Override
        public boolean updateUser(int id, String name, String email, String role) {
            lastUpdatedId = id;
            lastUpdatedName = name;
            lastUpdatedEmail = email;
            lastUpdatedRole = role;
            return updateResult;
        }

        @Override
        public boolean deleteUser(int id) {
            lastDeletedId = id;
            return deleteResult;
        }

        @Override
        public boolean changePassword(String username, String oldPassword, String newPassword) {
            lastPasswordUsername = username;
            lastOldPassword = oldPassword;
            lastNewPassword = newPassword;
            return changePasswordResult;
        }
    }

    static class FakeUserView extends UserView {
        List<Integer> menuOptions = new ArrayList<>();
        List<Integer> accountIds = new ArrayList<>();

        List<String> usernames = new ArrayList<>();
        List<String> oldPasswords = new ArrayList<>();
        List<String> newPasswords = new ArrayList<>();

        List<String> updatedNames = new ArrayList<>();
        List<String> updatedEmails = new ArrayList<>();
        List<String> updatedRoles = new ArrayList<>();

        List<User> displayedUsers = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        User userToCreate;
        User displayedUser;

        boolean throwNumberFormatOnce;
        boolean throwIllegalArgumentOnCreate;
        boolean throwRuntimeExceptionOnCreate;

        @Override
        public int showMenuAndGetOption(String role) {
            if (throwNumberFormatOnce) {
                throwNumberFormatOnce = false;
                throw new NumberFormatException();
            }

            if (menuOptions.isEmpty()) {
                return 0;
            }

            return menuOptions.remove(0);
        }

        @Override
        public User getUserCreationDetails() {
            if (throwIllegalArgumentOnCreate) {
                throw new IllegalArgumentException("Username cannot be empty");
            }

            if (throwRuntimeExceptionOnCreate) {
                throw new RuntimeException("Unexpected error");
            }

            return userToCreate;
        }

        @Override
        public int getAccountIdInput() {
            return accountIds.remove(0);
        }

        @Override
        public String getUsernameInput() {
            return usernames.remove(0);
        }

        @Override
        public String getOldPasswordInput() {
            return oldPasswords.remove(0);
        }

        @Override
        public String getNewPasswordInput() {
            return newPasswords.remove(0);
        }

        @Override
        public String getUpdatedNameInput() {
            return updatedNames.remove(0);
        }

        @Override
        public String getUpdatedEmailInput() {
            return updatedEmails.remove(0);
        }

        @Override
        public String getUpdatedRoleInput() {
            return updatedRoles.remove(0);
        }

        @Override
        public void displayAllUsers(List<User> users) {
            displayedUsers = users;
        }

        @Override
        public void displayUser(User user) {
            displayedUser = user;
        }

        @Override
        public void displayMessage(String message) {
            messages.add(message);
        }
    }
}

//class UserControllerTest {
//
//    @Test
//    void startUserManagement_adminCanViewAllUsers() {
//        FakeUserService userService = new FakeUserService();
//        FakeUserView userView = new FakeUserView();
//
//        UserController controller = new UserController(userService, userView);
//
//        User admin = new User("admin", "admin123", "Admin User", "admin@email.com", "admin");
//        User customer = new User("jdoe", "jdoe123", "John Doe", "jdoe@email.com", "customer");
//
//        userService.users.add(admin);
//        userService.users.add(customer);
//
//        // Option 2 = view all users, then 0 = exit
//        userView.menuOptions.add(2);
//        userView.menuOptions.add(0);
//
//        controller.startUserManagement(admin);
//
//        assertTrue(userService.getUsersCalled);
//        assertEquals(2, userView.displayedUsers.size());
//        assertTrue(userView.messages.contains("Exiting user management system..."));
//    }
//
//    static class FakeUserService extends UserService {
//        List<User> users = new ArrayList<>();
//        boolean getUsersCalled = false;
//
//        @Override
//        public List<User> getUsers() {
//            getUsersCalled = true;
//            return users;
//        }
//    }
//
//    static class FakeUserView extends UserView {
//        List<Integer> menuOptions = new ArrayList<>();
//        List<User> displayedUsers = new ArrayList<>();
//        List<String> messages = new ArrayList<>();
//
//        @Override
//        public int showMenuAndGetOption(String role) {
//            return menuOptions.remove(0);
//        }
//
//        @Override
//        public void displayAllUsers(List<User> users) {
//            displayedUsers = users;
//        }
//
//        @Override
//        public void displayMessage(String message) {
//            messages.add(message);
//        }
//    }
//}