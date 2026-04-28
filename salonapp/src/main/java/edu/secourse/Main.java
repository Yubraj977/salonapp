package edu.secourse;

import edu.secourse.controller.UserController;
import edu.secourse.model.Admin;
import edu.secourse.model.Customer;
import edu.secourse.model.Stylist;
import edu.secourse.service.UserService;
import edu.secourse.view.UserView;

public class Main {
    public static void main(String[] args) {
        System.out.printf("========================================\n");
        System.out.printf("Hello and welcome to SLogics Salon App!\n");
        System.out.printf("========================================\n");

        UserService userService = new UserService();
        seedData(userService);

        UserView userView = new UserView();
        UserController userController = new UserController(userService, userView);
        userController.startUserManagement();
    }

    private static void seedData(UserService userService) {
        userService.createUser(new Admin("admin",        "Admin@123",   "Alice Admin",   "alice@salon.com",   "ADMIN"));
        userService.createUser(new Stylist("stylist1",   "Style@123",   "Bob Carter",    "bob@salon.com",     "STYLIST"));
        userService.createUser(new Stylist("stylist2",   "Style@456",   "Clara Diaz",    "clara@salon.com",   "STYLIST"));
        userService.createUser(new Customer("customer1", "Pass@123",    "David Evans",   "david@email.com",   "CUSTOMER"));
        userService.createUser(new Customer("customer2", "Pass@456",    "Emma Foster",   "emma@email.com",    "CUSTOMER"));

        System.out.println("Seed data loaded: 1 admin, 2 stylists, 2 customers.\n");
    }
}