package edu.secourse.model;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a system user with authentication and profile information.
 *
 * <p>This class provides functionality for:
 * <ul>
 *     <li>Automatic generation of a unique account identifier</li>
 *     <li>Secure password storage using BCrypt hashing</li>
 *     <li>Password validation and update mechanisms</li>
 *     <li>Tracking user metadata such as last login time</li>
 * </ul>
 *
 * <p>Passwords are never stored in plaintext. All passwords are hashed using
 * the BCrypt algorithm before storage, ensuring secure authentication practices.
 *
 * <p>A password change flag is included to enforce password updates (e.g., after
 * initial account creation or administrative reset).
 *
 * <p><b>Note:</b> This class is designed for non-Spring environments and uses
 * in-memory account number generation via {@link AtomicInteger}.
 *
 * @author
 */
public class User {

    /**
     * Thread-safe counter used to generate unique account identifiers.
     * Starts at 10000 for readability and separation from small test values.
     */
    private static final AtomicInteger counter = new AtomicInteger(10000);

    /** Unique identifier assigned to each user account. */
    private int accountId;

    /** Username used for authentication. */
    private String username;

    /**
     * Hashed password (BCrypt).
     * <p>Note: This field never stores plaintext passwords.
     */
    private String password;

    /** Full name of the user. */
    private String name;

    /** Email address associated with the user. */
    private String email;

    /** Role assigned to the user (e.g., ADMIN, DOCTOR, PATIENT). */
    private String role;

    /** Timestamp of the user's last login. */
    private Date lastLogin;

    /**
     * Flag indicating whether the user must change their password.
     *
     * <p>This is typically set to {@code true} upon account creation or
     * password reset, and is cleared after a successful password change.
     *
     * <p>Example usage:
     * <pre>
     * if (user.checkPassword(inputPassword)) {
     *     if (user.mustChangePassword()) {
     *         System.out.println("User must change password");
     *     }
     * }
     * </pre>
     */
    private boolean mustChangePassword;

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
    public User(String username, String password, String name, String email, String role) {
        this.accountId = counter.incrementAndGet();
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.lastLogin = new Date();

        setPassword(password);
        this.mustChangePassword = true;
    }

    /**
     * Returns the unique account ID.
     *
     * @return the account ID
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the hashed password.
     *
     * <p><b>Warning:</b> This method returns the hashed password, not plaintext.
     *
     * @return the hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Hashes and sets the user's password using BCrypt.
     *
     * @param password the plaintext password
     * @throws IllegalArgumentException if the password is null or blank
     */
    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Validates a plaintext password against the stored hashed password.
     *
     * @param password the plaintext password to validate
     * @return {@code true} if the password matches, {@code false} otherwise
     */
    public boolean checkPassword(String password) {
        if (password == null) return false;
        return BCrypt.checkpw(password, this.password);
    }

    /**
     * Changes the user's password after validating the current password.
     *
     * <p>This method enforces:
     * <ul>
     *     <li>Correct current password</li>
     *     <li>Non-empty new password</li>
     *     <li>New password must differ from the old password</li>
     * </ul>
     *
     * <p>Upon successful change, the {@code mustChangePassword} flag is cleared.
     *
     * @param oldPassword the current plaintext password
     * @param newPassword the new plaintext password
     * @throws IllegalArgumentException if validation fails
     */
    public void changePassword(String oldPassword, String newPassword) {
        if (!checkPassword(oldPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        if (BCrypt.checkpw(newPassword, this.password)) {
            throw new IllegalArgumentException("New password must be different from the old password");
        }

        setPassword(newPassword);
        this.mustChangePassword = false;
    }

    /**
     * Returns the user's full name.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's full name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param role the new role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the last login timestamp.
     *
     * @return the last login date
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * Updates the last login timestamp.
     *
     * @param lastLogin the new login date
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Checks whether the user must change their password.
     *
     * @return {@code true} if password change is required, {@code false} otherwise
     */
    public boolean mustChangePassword() {
        return mustChangePassword;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return accountId == user.accountId && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, username);
    }

    /**
     * Returns a string representation of the user.
     *
     * <p><b>Note:</b> The password is intentionally excluded for security reasons.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "User{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}