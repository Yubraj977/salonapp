package edu.secourse.service;

import edu.secourse.model.User;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.secourse.util.PasswordUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing {@link User} objects in-memory.
 *
 * <p>This class implements core business logic for user management, including:
 * <ul>
 *     <li>Creating new users</li>
 *     <li>Retrieving users (by ID or username)</li>
 *     <li>Updating user information</li>
 *     <li>Deleting users</li>
 *     <li>Authentication and password management</li>
 * </ul>
 *
 * <p><b>Note:</b> This implementation does not use a database. All user data is
 * stored in an in-memory {@link List}, which means data will not persist after
 * the application terminates.
 *
 * <p>This class is suitable for small applications, testing environments,
 * or demonstration purposes.
 */
public class UserService {

    /**
     * Internal list storing all users.
     * <p>This list is maintained in-memory and is updated as users are created,
     * modified, or deleted.
     */
    private final List<User> users = new ArrayList<>();

    /**
     * Creates and stores a new user.
     *
     * <p>The user will only be added if no existing user shares the same username.
     *
     * @param user the user object to be added
     * @return {@code true} if the user was successfully created,
     *         {@code false} if a user with the same username already exists
     * @throws IllegalArgumentException if the provided user is {@code null}
     */
    public boolean createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (existsByUsername(user.getUsername())) {
            System.out.println("User already exists");
            return false;
        }

        users.add(user);

        // add user to json
        saveUserToJson(user);
        return true;
    }

    /**
     * Returns a list of all users.
     *
     * <p>A defensive copy of the internal list is returned to prevent
     * external modification of the service's internal state.
     *
     * @return a list containing all users
     */
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Checks whether a user with the specified username exists.
     *
     * @param username the username to check
     * @return {@code true} if a user with the given username exists,
     *         {@code false} otherwise
     */
    public boolean existsByUsername(String username) {
        if (username == null) {
            return false;
        }

        return users.stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    public boolean existsByAccountNumber(int accountNumber) {
        return users.stream()
                .anyMatch(user -> user.getAccountId() == accountNumber);
    }

    /**
     * Retrieves a user by their account ID.
     *
     * @param accountId the account ID to search for
     * @return an {@link Optional} containing the user if found,
     *         or an empty {@code Optional} if no user exists with the given ID
     */
    public Optional<User> getUserById(int accountId) {
        return users.stream()
                .filter(user -> user.getAccountId() == accountId)
                .findFirst();
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user if found,
     *         or an empty {@code Optional} if no user exists with the given username
     */
    public Optional<User> getUserByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }

        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    /**
     * Updates an existing user's profile information.
     *
     * <p>This method updates the user's name, email, and role based on the
     * provided account ID.
     *
     * @param id    the account ID of the user to update
     * @param name  the new name
     * @param email the new email address
     * @param role  the new role
     * @return {@code true} if the user was successfully updated,
     *         {@code false} if no user with the given ID exists
     */
    public boolean updateUser(int id, String name, String email, String role) {
        Optional<User> optionalUser = getUserById(id);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);

        return true;
    }

    /**
     * Deletes a user by their account ID.
     *
     * @param id the account ID of the user to delete
     * @return {@code true} if the user was successfully removed,
     *         {@code false} if no user with the given ID exists
     */
    public boolean deleteUser(int id) {
        Optional<User> optionalUser = getUserById(id);

        if (optionalUser.isEmpty()) {
            return false;
        }

        return users.removeIf(user -> user.getAccountId() == id);
    }

    /**
     * Authenticates a user using their username and password.
     *
     * <p>The password is validated using the {@link User#checkPassword(String)}
     * method, which compares the provided plaintext password with the stored
     * hashed password.
     *
     * @param username the username
     * @param password the plaintext password
     * @return {@code true} if authentication is successful,
     *         {@code false} otherwise
     */
    public User authenticate(String username, String password) {
//        Optional<User> optionalUser = getUserByUsername(username);

//        return optionalUser
//                .map(u -> u.checkPassword(password))
//                .orElse(false);
        return users.stream()
                .filter(user -> user.getUsername().equals(username)
                        && PasswordUtil.checkPassword(password, user.getPassword())
                )
                .findFirst()
                .orElse(null);
    }

    /**
     * Changes the password of a user.
     *
     * <p>This method verifies the user's current password before applying the
     * change. The password update is delegated to the {@link User#changePassword(String, String)} method.
     *
     * @param username    the username of the user
     * @param oldPassword the current password
     * @param newPassword the new password
     * @return {@code true} if the password was successfully changed,
     *         {@code false} if the user does not exist
     * @throws IllegalArgumentException if password validation fails
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> optionalUser = getUserByUsername(username);

        if (optionalUser.isEmpty()) {
            return false;
        }

        optionalUser.get().changePassword(oldPassword, newPassword);
        return true;
    }

    public void saveUserToJson(User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("data/users.json");

        try {
            // Ensure directory exists
            file.getParentFile().mkdirs();

            List<User> users;

            //Read existing users if file exists
            if (file.exists() && file.length() > 0) {
                users = objectMapper.readValue(file, new TypeReference<List<User>>() {});
            } else {
                users = new ArrayList<>();
            }

            // Check if user exists already
            boolean exists = users.stream()
                    .anyMatch(u -> u.getAccountId() == user.getAccountId());

            if (!exists) {
                // Add new user
                users.add(user);
            }

            // Write updated list back
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, users);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}