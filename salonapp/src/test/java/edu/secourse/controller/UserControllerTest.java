package edu.secourse.controller;

import edu.secourse.model.User;
import edu.secourse.service.UserService;
import edu.secourse.view.UserView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserView userView;
    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userView = mock(UserView.class);
        userController = new UserController(userService, userView);

        user = new User(
                "jdoe",
                "password123",
                "John Doe",
                "jdoe@example.com",
                "CUSTOMER"
        );
    }

    @Test
    void testUserManagementOptionZero() {
        when(userView.showMenuAndGetOption()).thenReturn(0);

        userController.startUserManagement();
        verify(userView).displayMessage("Exiting user management system...");
    }

    @Test
    void testOptionOneSuccess() {
        when(userView.showMenuAndGetOption()).thenReturn(1, 0);
        when(userView.getUserCreationDetails()).thenReturn(user);
        when(userService.createUser(user)).thenReturn(true);

        userController.startUserManagement();

        verify(userService).createUser(user);
        verify(userView).displayMessage("Successfully created user");
    }

    @Test
    void testOptionOneNoSuccess() {
        when(userView.showMenuAndGetOption()).thenReturn(1, 0);
        when(userView.getUserCreationDetails()).thenReturn(user);
        when(userService.createUser(user)).thenReturn(false);

        userController.startUserManagement();

        verify(userView).displayMessage("User could not be created");
    }

    @Test
    void testOptionTwoView() {
        List<User> userList = new ArrayList<>();

        when(userView.showMenuAndGetOption()).thenReturn(2, 0);
        when(userService.getUsers()).thenReturn(userList);

        userController.startUserManagement();

        verify(userView).displayAllUsers(userList);
    }

    @Test
    void testOptionThreeFindUser() {
        when(userView.showMenuAndGetOption()).thenReturn(3, 0);
        when(userView.getAccountIdInput()).thenReturn(user.getAccountId());
        when(userService.getUserById(user.getAccountId())).thenReturn(Optional.of(user));

        userController.startUserManagement();

        verify(userView).displayUser(user);
    }

    @Test
    void testOptionThreeNoFindUser() {
        when(userView.showMenuAndGetOption()).thenReturn(3, 0);
        when(userView.getAccountIdInput()).thenReturn(900);
        when(userService.getUserById(900)).thenReturn(Optional.empty());

        userController.startUserManagement();
        verify(userView).displayMessage("User not found");
    }

    @Test
    void testOptionFourUpdateUser() {
        when(userView.showMenuAndGetOption()).thenReturn(4, 0);
        when(userView.getAccountIdInput()).thenReturn(user.getAccountId());
        when(userView.getUpdatedNameInput()).thenReturn("John Updated");
        when(userView.getUpdatedEmailInput()).thenReturn("updated@example.com");
        when(userView.getUpdatedRoleInput()).thenReturn("ADMIN");

        when(userService.updateUser(
                user.getAccountId(),
                "John Updated",
                "updated@example.com",
                "ADMIN"
        )).thenReturn(true);

        userController.startUserManagement();

        verify(userView).displayMessage("User updated successfully!");
    }

    @Test
    void testOptionFourNoUpdateUser() {
        when(userView.showMenuAndGetOption()).thenReturn(4, 0);
        when(userView.getAccountIdInput()).thenReturn(900);
        when(userView.getUpdatedNameInput()).thenReturn("Nobody");
        when(userView.getUpdatedEmailInput()).thenReturn("nobody@example.com");
        when(userView.getUpdatedRoleInput()).thenReturn("CUSTOMER");

        when(userService.updateUser(
                900,
                "Nobody",
                "nobody@example.com",
                "CUSTOMER"
        )).thenReturn(false);

        userController.startUserManagement();

        verify(userView).displayMessage("User not found!");
    }

    @Test
    void testOptionFiveDeleteUser() {
        when(userView.showMenuAndGetOption()).thenReturn(5, 0);
        when(userView.getAccountIdInput()).thenReturn(user.getAccountId());
        when(userService.deleteUser(user.getAccountId())).thenReturn(true);

        userController.startUserManagement();

        verify(userView).displayMessage("Successfully deleted user");
    }

    @Test
    void testOptionFiveNoDeleteUser() {
        when(userView.showMenuAndGetOption()).thenReturn(5, 0);
        when(userView.getAccountIdInput()).thenReturn(900);
        when(userService.deleteUser(900)).thenReturn(false);

        userController.startUserManagement();

        verify(userView).displayMessage("Error deleting user");
    }

    @Test
    void testOptionSixLoginSuccess() {
        when(userView.showMenuAndGetOption()).thenReturn(6, 0);
        when(userView.getUsernameInput()).thenReturn("jdoe");
        when(userView.getPasswordInput()).thenReturn("password123");
        when(userService.authenticate("jdoe", "password123")).thenReturn(true);

        userController.startUserManagement();

        verify(userView).displayMessage("Logged in successfully");
    }

    @Test
    void testOptionSixLoginFail() {
        when(userView.showMenuAndGetOption()).thenReturn(6, 0);
        when(userView.getUsernameInput()).thenReturn("jdoe");
        when(userView.getPasswordInput()).thenReturn("wrongpassword");
        when(userService.authenticate("jdoe", "wrongpassword")).thenReturn(false);

        userController.startUserManagement();

        verify(userView).displayMessage("Invalid username or password");
    }

    @Test
    void testOptionSevenChangePasswordSuccess() {
        when(userView.showMenuAndGetOption()).thenReturn(7, 0);
        when(userView.getUsernameInput()).thenReturn("jdoe");
        when(userView.getOldPasswordInput()).thenReturn("password123");
        when(userView.getNewPasswordInput()).thenReturn("newPassword123");

        when(userService.changePassword(
                "jdoe",
                "password123",
                "newPassword123"
        )).thenReturn(true);

        userController.startUserManagement();

        verify(userView).displayMessage("Password changed successfully");
    }

    @Test
    void testOptionSevenChangePasswordFail() {
        when(userView.showMenuAndGetOption()).thenReturn(7, 0);
        when(userView.getUsernameInput()).thenReturn("missinguser");
        when(userView.getOldPasswordInput()).thenReturn("password123");
        when(userView.getNewPasswordInput()).thenReturn("newPassword123");

        when(userService.changePassword(
                "missinguser",
                "password123",
                "newPassword123"
        )).thenReturn(false);

        userController.startUserManagement();

        verify(userView).displayMessage("Password change failed");
    }

    @Test
    void testInvalidOption() {
        when(userView.showMenuAndGetOption()).thenReturn(90, 0);

        userController.startUserManagement();

        verify(userView).displayMessage("Invalid option selected.");
    }

    @Test
    void testOptionFormat() {
        when(userView.showMenuAndGetOption())
                .thenThrow(new NumberFormatException())
                .thenReturn(0);

        userController.startUserManagement();

        verify(userView).displayMessage("Please enter a valid number");
    }
}