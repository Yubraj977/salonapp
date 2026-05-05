package edu.secourse.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hash password (used when creating/saving a user)
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verify password (used during login)
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}