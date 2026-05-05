package edu.secourse.session;

import edu.secourse.model.User;

public class UserSession {
    private static User loggedInUser;

    public static void login(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }
}
