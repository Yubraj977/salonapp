package edu.secourse.view;

import edu.secourse.model.User;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based view class responsible for handling all user interactions.
 *
 * <p>This class provides methods for:
 * <ul>
 *     <li>Displaying menus and prompts to the user</li>
 *     <li>Collecting user input from the console</li>
 *     <li>Displaying user information and system messages</li>
 * </ul>
 *
 * <p>The {@code UserView} is part of the presentation layer in the
 * Model-View-Controller (MVC) architecture. It does not contain business logic,
 * but instead delegates processing to the controller.
 *
 * <p><b>Note:</b> This implementation uses standard input/output (console)
 * via {@link Scanner} and {@code System.out}.
 */
public class UserView {

    /**
     * Scanner used to read user input from the console.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the main menu and retrieves the user's selected option.
     *
     * @return the selected menu option as an integer
     * @throws NumberFormatException if the input cannot be parsed as an integer
     */
    public int showMenuAndGetOption(String role) {
        System.out.println("\n====== User Management =======");
        System.out.println("Please select an option:");

        if (role.equalsIgnoreCase("admin")) {
            System.out.println("1. Create User");
            System.out.println("2. View All Users");
            System.out.println("3. View User By Id");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Change Password");
            System.out.println("0. Exit");
        } else if(role.equalsIgnoreCase("customer") || role.equalsIgnoreCase("stylist")) {
            System.out.println("1. Change Password");
            System.out.println("0. Exit");
        }

        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Prompts the user to enter details required to create a new {@link User}.
     *
     * @return a newly constructed {@link User} object with user-provided details
     */
    public User getUserCreationDetails() {
        System.out.println("\n----- Create User ------");

        System.out.println("Please enter your username:");
        String username = scanner.nextLine();

        System.out.println("Please enter your password:");
        String password = scanner.nextLine();

        System.out.println("Please enter your full name:");
        String fullName = scanner.nextLine();

        // Email validation
        String email = "";
        boolean isEmailValid = false;
        while (!isEmailValid) {
            System.out.println("Please enter your email:");
            email = scanner.nextLine();


            EmailValidator validator = EmailValidator.getInstance();
            isEmailValid = validator.isValid(email);
            if (isEmailValid) {
                System.out.println("Email is verified. Proceed!");
                break;
            }else{
                System.err.println("Invalid email!");
            }
        }

        String role = "";
        boolean isRoleValid = false;
        List<String> roles = List.of("ADMIN", "CUSTOMER", "STYLIST");
        while (!isRoleValid) {
            System.out.println("Please select role:");
            role = scanner.nextLine();

            // Validate role
            if (roles.contains(role)) {
                isRoleValid = true;
            }
            else  {
                isRoleValid = false;
                System.out.println("Invalid role. Please try again!");
            }
        }

        return new User(username, password, fullName, email, role);
    }

    /**
     * Prompts the user to enter an account ID.
     *
     * @return the account ID entered by the user
     * @throws NumberFormatException if the input is not a valid integer
     */
    public int getAccountIdInput() {
        System.out.println("Please enter your account id:");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Prompts the user to enter a username.
     *
     * @return the entered username
     */
    public String getUsernameInput() {
        System.out.println("Please enter your username:");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a password.
     *
     * @return the entered password
     */
    public String getPasswordInput() {
        System.out.println("Please enter password:");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a new password.
     *
     * @return the new password
     */
    public String getNewPasswordInput() {
        System.out.println("Please enter new password:");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter their current password.
     *
     * @return the current password
     */
    public String getOldPasswordInput() {
        System.out.println("Please enter old password:");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter an updated name.
     *
     * @return the updated name
     */
    public String getUpdatedNameInput() {
        System.out.println("Please enter updated name:");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter an updated email.
     *
     * @return the updated email
     */
    public String getUpdatedEmailInput() {
        System.out.println("Please enter updated email:");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter an updated role.
     *
     * @return the updated role
     */
    public String getUpdatedRoleInput() {
        System.out.println("Please enter updated role:");
        return scanner.nextLine();
    }

    /**
     * Displays the details of a single user.
     *
     * @param user the user to display
     */
    public void displayUser(User user) {
        System.out.println("\n----- User Details ------");
        System.out.println(user);
    }

    /**
     * Displays a list of all users.
     *
     * <p>If the list is empty, an appropriate message is displayed.
     *
     * @param users the list of users to display
     */
    public void displayAllUsers(List<User> users) {
        System.out.println("\n----- All Users ------");

        if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }

        for (User user : users) {
            displayUser(user);
        }
    }

    /**
     * Displays a general informational message to the user.
     *
     * @param message the message to display
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void displayError(String message) {
        System.err.println("Error: " + message);
    }
}