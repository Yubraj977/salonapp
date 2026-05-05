package edu.secourse.controller;

import edu.secourse.model.User;
import edu.secourse.service.UserService;
import edu.secourse.session.UserSession;
import edu.secourse.view.UserView;

public class AuthController {
    private final UserView userView;
    private final UserService userService;


    /**
     * Constructs a {@code AuthController} with the specified service and view.
     *
     * @param userService the service used to manage user data
     * @param userView    the view used to interact with the user
     */
    public AuthController(UserService userService, UserView userView) {
        this.userService = userService;
        this.userView = userView;
    }

    /**
     * Handles user authentication (login).
     *
     * <p>Prompts for username and password, then verifies credentials using
     * the service layer.
     */
    public boolean login() {
        String username = userView.getUsernameInput();
        String password = userView.getPasswordInput();

//        boolean authenticated = userService.authenticate(username, password);
        User user = userService.authenticate(username, password);

        if (user != null) {
            UserSession.login(user);
//            userView.displayMessage("Logged in successfully as " + user.getRole());
            userView.displayMessage("Welcome " + user.getName() + " (" + user.getRole() + ")");
            return true;
        }else {
            userView.displayMessage("Invalid username or password");
            return false;
        }

//        if (authenticated) {
//            userView.displayMessage("Logged in successfully");
//        } else {
//            userView.displayMessage("Invalid username or password");
//        }
//        return authenticated;
    }
}
