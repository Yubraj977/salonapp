package edu.secourse;

import edu.secourse.controller.UserController;
import edu.secourse.service.UserService;
import edu.secourse.view.UserView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("========================================\n");
        System.out.printf("Hello and welcome to SLogics Salon App!\n");
        System.out.printf("========================================\n");

        UserService userService = new UserService();
        UserView userView = new UserView();

        UserController userController = new UserController(userService, userView);

        userController.startUserManagement();
    }
}