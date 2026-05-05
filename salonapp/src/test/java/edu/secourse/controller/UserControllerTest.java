package edu.secourse.controller;

import edu.secourse.controller.UserController;
import edu.secourse.model.Admin;
import edu.secourse.model.User;
import edu.secourse.service.UserService;
import edu.secourse.view.UserView;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void startUserManagement_adminCanViewAllUsers() {
        // Arrange
        UserService userService = mock(UserService.class);
        UserView userView = mock(UserView.class);

        UserController userController = new UserController(userService, userView);

        User admin = new Admin("admin", "admin123", "Admin admin", "admin@email.com");

        List<User> users = List.of(
                admin,
                new User("jdoe", "jdoe123", "John Doe", "jdoe@email.com", "customer")
        );

        when(userView.showMenuAndGetOption("admin"))
                .thenReturn(2)   // view all users
                .thenReturn(0);  // exit

        when(userService.getUsers()).thenReturn(users);

        // Act
        userController.startUserManagement(admin);

        // Assert
        verify(userService).getUsers();
        verify(userView).displayAllUsers(users);
        verify(userView).displayMessage("Exiting user management system...");
    }

    @Test
    void startUserManagement_customerCanChangePassword() {
        // Arrange
        UserService userService = mock(UserService.class);
        UserView userView = mock(UserView.class);

        UserController userController = new UserController(userService, userView);

        User customer = new User("jdoe", "jdoe123", "John Doe", "jdoe@email.com", "customer");

        when(userView.showMenuAndGetOption("customer"))
                .thenReturn(1)   // change password
                .thenReturn(0);  // exit

        when(userView.getUsernameInput()).thenReturn("jdoe");
        when(userView.getOldPasswordInput()).thenReturn("oldpass");
        when(userView.getNewPasswordInput()).thenReturn("newpass");

        when(userService.changePassword("jdoe", "oldpass", "newpass"))
                .thenReturn(true);

        // Act
        userController.startUserManagement(customer);

        // Assert
        verify(userService).changePassword("jdoe", "oldpass", "newpass");
        verify(userView).displayMessage("Password changed successfully");
    }
}