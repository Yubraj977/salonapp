package edu.secourse.controller;

import edu.secourse.model.User;
import edu.secourse.service.UserService;
import edu.secourse.view.UserView;

import java.util.Optional;

/**
 * Controller class responsible for coordinating interactions between the
 * {@link UserView} (presentation layer) and {@link UserService} (business logic layer).
 *
 * <p>This class serves as the central control unit in the Model-View-Controller (MVC)
 * architecture, handling user input, invoking appropriate business logic, and updating
 * the view with results.
 *
 * <p>Responsibilities include:
 * <ul>
 *     <li>Processing user commands from the view</li>
 *     <li>Delegating operations to the service layer</li>
 *     <li>Managing application flow and user interaction loop</li>
 *     <li>Handling input validation and runtime exceptions</li>
 * </ul>
 *
 * <p>This implementation uses a console-based interface and runs continuously
 * until the user chooses to exit.
 */
public class UserController {

    /** Service layer responsible for user-related business logic. */
    private final UserService userService;

    /** View layer responsible for user interaction. */
    private final UserView userView;

    /**
     * Constructs a {@code UserController} with the specified service and view.
     *
     * @param userService the service used to manage user data
     * @param userView    the view used to interact with the user
     */
    public UserController(UserService userService, UserView userView) {
        this.userService = userService;
        this.userView = userView;
    }

    /**
     * Starts the user management system.
     *
     * <p>This method runs an interactive loop that continuously displays a menu,
     * processes user input, and invokes the appropriate operations until the
     * user chooses to exit.
     *
     * <p>Exceptions are handled gracefully to prevent application crashes due
     * to invalid input or unexpected runtime errors.
     */
    public void startUserManagement(User loggedUser) {
        boolean running = true;

        while (running) {
            try {
                if (loggedUser == null) {
                    return;
                }
                if(loggedUser.getRole().equalsIgnoreCase("admin")) {
                    int option = userView.showMenuAndGetOption("admin");

                    switch (option) {
                        case 1 -> createUser();
                        case 2 -> viewAllUsers();
                        case 3 -> findUserById();
                        case 4 -> updateUser();
                        case 5 -> deleteUser();
                        case 6 -> changePassword();
                        case 0 -> {
                            userView.displayMessage("Exiting user management system...");
                            running = false;
                        }
                        default -> userView.displayMessage("Invalid option selected.");
                    }
                } else if (
                        loggedUser.getRole().equalsIgnoreCase("customer")
                                || loggedUser.getRole().equalsIgnoreCase("stylist")) {
                    int option = userView.showMenuAndGetOption(loggedUser.getRole());

                    switch (option) {
                        case 1 -> changePassword();
                        case 0 -> {
                            userView.displayMessage("Exiting user management system...");
                            running = false;
                        }
                        default -> userView.displayMessage("Invalid option selected.");
                    }
                }

            } catch (NumberFormatException e) {
                userView.displayMessage("Please enter a valid number");
            } catch (IllegalArgumentException e) {
                userView.displayMessage(e.getMessage());
            } catch (Exception e) {
                userView.displayMessage("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the password change operation.
     *
     * <p>Prompts the user for username, current password, and new password,
     * then delegates the update to the service layer.
     */
    private void changePassword() {
        String username = userView.getUsernameInput();
        String oldPassword = userView.getOldPasswordInput();
        String newPassword = userView.getNewPasswordInput();

        boolean passwordChange = userService.changePassword(username, oldPassword, newPassword);

        if (passwordChange) {
            userView.displayMessage("Password changed successfully");
        } else {
            userView.displayMessage("Password change failed");
        }
    }

    /**
     * Handles user authentication (login).
     *
     * <p>Prompts for username and password, then verifies credentials using
     * the service layer.
     */
//    private void login() {
//        String username = userView.getUsernameInput();
//        String password = userView.getPasswordInput();
//
//        boolean authenticated = userService.authenticate(username, password);
//
//        if (authenticated) {
//            userView.displayMessage("Logged in successfully");
//        } else {
//            userView.displayMessage("Invalid username or password");
//        }
//    }

    /**
     * Handles deletion of a user by account ID.
     */
    private void deleteUser() {
        int id = userView.getAccountIdInput();

        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            userView.displayMessage("Successfully deleted user");
        } else {
            userView.displayMessage("Error deleting user");
        }
    }

    /**
     * Handles updating user information.
     *
     * <p>Prompts for user ID and updated fields, then delegates the update
     * operation to the service layer.
     */
    private void updateUser() {
        int id = userView.getAccountIdInput();

        String name = userView.getUpdatedNameInput();
        String email = userView.getUpdatedEmailInput();
        String role = userView.getUpdatedRoleInput();

        boolean updated = userService.updateUser(id, name, email, role);

        if (updated) {
            userView.displayMessage("User updated successfully!");
        } else {
            userView.displayMessage("User not found!");
        }
    }

    /**
     * Retrieves and displays a user by their account ID.
     */
    private void findUserById() {
        int id = userView.getAccountIdInput();

        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            userView.displayUser(user.get());
        } else {
            userView.displayMessage("User not found");
        }
    }

    /**
     * Retrieves and displays all users.
     */
    private void viewAllUsers() {
        userView.displayAllUsers(userService.getUsers());
    }

    /**
     * Handles creation of a new user.
     *
     * <p>Prompts the user for required details and delegates the creation
     * process to the service layer.
     */
    private void createUser() {
        User user = userView.getUserCreationDetails();
        boolean success = userService.createUser(user);

        if (success) {
            userView.displayMessage("Successfully created user");
        } else {
            userView.displayMessage("User could not be created");
        }
    }
}