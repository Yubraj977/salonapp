package edu.secourse;

import edu.secourse.controller.AuthController;
import edu.secourse.controller.UserController;
import edu.secourse.model.User;
import edu.secourse.service.UserService;
import edu.secourse.view.UserView;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    /**
     * Scanner used to read user input from the console.
     */
    final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        UserService userService = new UserService();
        UserView userView = new UserView();

        //Create default admin to control app
        User admin = new User("admin", "admin123", "admin", "admin@email", "admin");
        userService.createUser(admin);



        System.out.printf("========================================\n");
        System.out.printf("Hello and welcome to SLogics Salon App!\n");
        System.out.printf("========================================\n");

        boolean appRunning = true;

        while (appRunning) {
            int starterOption = openAppStartPage(userView);

            AuthController authController = new AuthController(userService, userView);
            UserController userController = new UserController(userService, userView);

            // User Login
            if (starterOption == 1) {
                boolean authenticated = false;
                int appmgtOpt = 0;

                while (!authenticated) {
                    authenticated = authController.login();
                    if (authenticated) {
                        appmgtOpt = chooseAppManagement(userView, userController);
                        authenticated = false;
                    }
                    if (appmgtOpt == 0){
                        break;
                    }
                }
            }else if (starterOption == 0) {
                System.exit(0);
            }
        }
    }

    private static int openAppStartPage(UserView userView) {
        try {
            int option = showStartMenu();
            switch (option) {
                case 1 -> {
                    return 1;
                }
                case 0 -> {
                    userView.displayMessage("Exiting Salon App...\n");
                    return 0;
                }
            }
        }
        catch (NumberFormatException e) {
            userView.displayMessage("Please enter a valid number");
        } catch (IllegalArgumentException e) {
            userView.displayMessage(e.getMessage());
        } catch (Exception e) {
            userView.displayMessage("Error: " + e.getMessage());
        }
        return 0;
    }

    private static int showStartMenu() {
        System.out.println("\n====== Salon App Start =======");
        System.out.println("Please select an option:");
        System.out.println("1. User Login");
        System.out.println("0. Exit");

        return Integer.parseInt(scanner.nextLine());
    }

    private static int chooseAppManagement(UserView userView, UserController userController) {
        boolean running = true;

        while (running) {
            try {
                int option = showAppManagementMenu();
                switch (option) {
                    case 1 -> {
                        startUserManagement(userController);
                        return 1;
                    }
                    case 2 -> {
                        startAppointmentManagement();
                        return 1;
                    }
                    case 0 -> {
                        userView.displayMessage("Exiting user management system...\n");
                        running = false;
                        return 0;
                    }
                }
            }
            catch (NumberFormatException e) {
                userView.displayMessage("Please enter a valid number");
            } catch (IllegalArgumentException e) {
                userView.displayMessage(e.getMessage());
            } catch (Exception e) {
                userView.displayMessage("Error: " + e.getMessage());
            }
        }
        return 0;
    }

    private static void startAppointmentManagement() {
    }


    private static void startUserManagement(UserController userController) {
        userController.startUserManagement();
    }

    private static int showAppManagementMenu() {
        System.out.println("\n====== Salon App Management =======");
        System.out.println("Please select an option:");
        System.out.println("1. User Management Menu");
        System.out.println("2. App Management Menu");
        System.out.println("0. Exit");

        return Integer.parseInt(scanner.nextLine());
    }
}