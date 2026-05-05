package edu.secourse.service;

import edu.secourse.model.User;
import edu.secourse.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService();

        user = new User(
                "jdoe",
                "password123",
                "John Doe",
                "jdoe@example.com",
                "CUSTOMER"
        );

        userService.createUser(user);
    }

    @Test
    void testCreateNewUser() {
        User newUser = new User(
                "jsmith",
                "pasword123",
                "John Smith",
                "jsmith@email.com",
                "ADMIN"
        );

        User duplicateUser = new User(
                "jdoe",
                "Password123",
                "John Doe",
                "jdoe@example.com",
                "CUSTOMER"
        );

        boolean newUserresult = userService.createUser(newUser);
        assertTrue(newUserresult);
        assertEquals(2, userService.getUsers().size());

    }

    @Test
    void testCreateDuplicateUser() {
        User duplicateUser = new User(
                "jdoe",
                "Password123",
                "John Doe",
                "jdoe@example.com",
                "CUSTOMER"
        );

        boolean duplicateUserresult = userService.createUser(duplicateUser);

        assertFalse(duplicateUserresult);
        assertEquals(1, userService.getUsers().size());
    }

    @Test
    void testUserCreationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
    }

    @Test
    void testGetUserAndUserExists() {

        assertAll(
                () -> assertEquals(1, userService.getUsers().size()),
                () -> assertTrue(userService.existsByUsername("jdoe")),
                () -> assertFalse(userService.existsByUsername("missingUser")),
                () -> assertFalse(userService.existsByUsername(null))
        );
    }

    @Test
    void testGetUserCorrectResponse() {
        Optional<User> dummyUser1 = userService.getUserByUsername("jdoe");
        Optional<User> dummyUser2 = userService.getUserByUsername("missing");
        Optional<User> dummyUser3 = userService.getUserByUsername(null);

        assertAll(
                () -> assertTrue(dummyUser1.isPresent()),
                () -> assertEquals("John Doe", dummyUser1.get().getName()),
                () -> assertTrue(dummyUser2.isEmpty()),
                () -> assertTrue(dummyUser3.isEmpty())

        );
    }

    @Test
    void testUpdateUserExists() {
        boolean result = userService.updateUser(
                user.getAccountId(),
                "John Updated",
                "updatedjohn@email.com",
                "ADMIN"
        );

        assertTrue(result);
        assertEquals("John Updated", user.getName());
        assertEquals("updatedjohn@email.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testUpdateUserDoesNotExist() {
        boolean result = userService.updateUser(
                90000,
                "NonExistent",
                "nonexistent@email.com",
                "CUSTOMER"
        );

        assertFalse(result);
    }

    @Test
    void testDeleteExistingUser() {
        boolean result = userService.deleteUser(user.getAccountId());

        assertTrue(result);
        assertEquals(0, userService.getUsers().size());
    }

    @Test
    void testDeleteUserDoesNotExist() {
        boolean result = userService.deleteUser(90000);
        assertFalse(result);
    }

    @Test
    void testAuthenticateCorrectCredentials() {
        User user = userService.authenticate("jdoe", "password123");

        assertNotNull(user);
        assertEquals("jdoe", user.getUsername());
        assertTrue(PasswordUtil.checkPassword("password123", user.getPassword()));
    }

    @Test
    void testAuthenticateIncorrectCredentials() {
        User result = userService.authenticate("noUser", "noPass");

        assertNull(result);
    }

    @Test
    void testChangePasswordAllCases() {
        boolean result1 = userService.changePassword("jdoe", "password123", "newpassword");
        boolean result2 = userService.changePassword("missingUser", "password123", "newpassword");

        // change password for existing user
        assertTrue(result1);
        assertNotNull(userService.authenticate("jdoe", "newpassword"));
        assertNull(userService.authenticate("jdoe", "wrongpassword123"));

        // user does not exist
        assertFalse(result2);

        // exception for old wrong password
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword("jdoe", "wrongpassword123", "newpassword")
        );
    }

}