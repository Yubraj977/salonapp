package edu.secourse;

import edu.secourse.controller.AppointmentController;
import edu.secourse.controller.AuthController;
import edu.secourse.controller.UserController;
import edu.secourse.model.Admin;
import edu.secourse.model.Customer;
import edu.secourse.model.Stylist;
import edu.secourse.model.User;
import edu.secourse.service.AppointmentService;
import edu.secourse.service.UserService;
import edu.secourse.session.UserSession;
import edu.secourse.view.AppointmentView;
import edu.secourse.view.UserView;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    /**
     * Scanner used to read user input from the console.
     */
    final static Scanner scanner = new Scanner(System.in);

    static boolean islogged = false;
    static UserController userController;

    public static void main(String[] args) {


        // Initializing service with shared repository
        UserService userService = new UserService();
        AppointmentService appointmentService = new AppointmentService();

        // Initializing views
        UserView userView = new UserView();
        AppointmentView appointmentView = new AppointmentView(userService);

        // Initialize app data
        initializeAppData(userService);

        // Start Application
        startSalonApp(userView, userService, appointmentService, appointmentView);

    }

    private static void startSalonApp(
            UserView userView, UserService userService,
            AppointmentService appointmentService, AppointmentView appointmentView
    ) {
        // Display App information
        System.out.printf("========================================\n");
        System.out.printf("Hello and welcome to SLogics Salon App!\n");
        System.out.printf("========================================\n");

        boolean appRunning = true;

        while (appRunning) {
            int starterOption = openAppStartPage(userView);

            AuthController authController = new AuthController(userService, userView);
            UserController userController = new UserController(userService, userView);
            AppointmentController appointmentController = new AppointmentController(appointmentService, appointmentView);

            //exit Application
            if (starterOption == 0) System.exit(0);
            else if(starterOption == 1){
                if (islogged == false){
                    appLogin(authController, userController, appointmentController, userView);
                }
            }
        }
    }

    private static void initializeAppData(UserService userService) {
        //Create default admin to control app
        User admin = new Admin("admin", "admin123", "admin", "admin@email");
        userService.createUser(admin);

        //Creating sample users
        userService.createUser(
                new Customer("alice", "alice123", "Alice Wonder", "alice@email.com")
        );

        userService.createUser(
                new Customer("john", "john123", "John Wonder", "john@email.com")
        );

        userService.createUser(
                new Stylist("Esther", "esther123", "Esther Wonder", "esther@email.com")
        );
    }

    private static void appLogin(
            AuthController authController,
            UserController userController,
            AppointmentController appointmentController,
            UserView userView)
    {

//        boolean authenticated = false;
        int appmgtOpt = 0;

        boolean authenticated = authController.login();
        while (authenticated) {
            User loggedUser = UserSession.getLoggedInUser();
            if (authenticated) {
//                islogged = true;
                appmgtOpt = chooseAppManagement(userView, userController, appointmentController, loggedUser);
                authenticated = false;

            }
            if (appmgtOpt == 0){
                break;
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
        System.out.println("Please select an option:");
        System.out.println("1. User Login");
        System.out.println("0. Exit");

        return Integer.parseInt(scanner.nextLine());
    }

    private static int chooseAppManagement(
            UserView userView, UserController userController,
            AppointmentController appointmentController, User loggedUser) {

        boolean running = true;
        while (running) {
            try {
                int option = showAppManagementMenu();
                switch (option) {
                    case 1 -> {
                        startUserManagement(userController, loggedUser);
                        running = true;
                    }
                    case 2 -> {
                        startAppointmentManagement(appointmentController, loggedUser);
                        running = true;
                    }
                    case 0 -> {
                        userView.displayMessage("Logging out ...\n");
                        UserSession.logout();
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

    private static void startAppointmentManagement(AppointmentController appointmentController, User loggedUser) {
        appointmentController.startAppointmentManagement(loggedUser);
    }


    private static void startUserManagement(UserController userController, User loggedUser) {
        userController.startUserManagement(loggedUser);
    }

    private static int showAppManagementMenu() {
        System.out.println("\n====== Salon App Management =======");
        System.out.println("Please select an option:");
        System.out.println("1. User Management Menu");
        System.out.println("2. App Management Menu");
        System.out.println("0. Logout");

        return Integer.parseInt(scanner.nextLine());
    }
}