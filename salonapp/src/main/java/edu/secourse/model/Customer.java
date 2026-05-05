package edu.secourse.model;

public class Customer extends User {
    /**
     * Constructs a new {@code User} with the specified attributes.
     *
     * <p>The account ID is automatically generated, and the provided password
     * is hashed using BCrypt. The user is required to change their password
     * upon first login.
     *
     * @param username the username for authentication
     * @param password the plaintext password (will be hashed)
     * @param name     the full name of the user
     * @param email    the user's email address
     * @param role     the role assigned to the user
     * @throws IllegalArgumentException if the password is null or blank
     */
    public Customer(String username, String password, String name, String email) {
        super(username, password, name, email, "customer");
    }
}
