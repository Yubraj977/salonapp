package edu.secourse.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructor(){
        User user = new User("jdoe", "Password123", "John Doe", "jdoe@example.com", "CUSTOMER");

        assertTrue(user.getAccountId() > 10000);
        assertEquals("jdoe", user.getUsername());
//        assertEquals("Password123", user.getPassword());
        assertEquals("jdoe@example.com", user.getEmail());
        assertEquals("CUSTOMER", user.getRole());
        assertNotNull(user.getLastLogin());
        assertTrue(user.mustChangePassword());

    }

    @Test
    void testUserUniqueAccountId() {
        User user1 = new User("user1", "Password123", "User One", "user1@example.com", "CUSTOMER");
        User user2 = new User("user2", "Password123", "User Two", "user2@example.com", "CUSTOMER");

        assertNotEquals(user1.getAccountId(), user2.getAccountId());
    }

    @Test
    void testSettersToUpdateFields() {
        User user = new User("jdoe", "Password123", "John Doe", "jdoe@example.com", "CUSTOMER");
        Date date = new Date();

        user.setUsername("jsmith");
        user.setName("John Smith");
        user.setEmail("jsmith@email.com");
        user.setRole("ADMIN");
        user.setLastLogin(date);

        //assertions
        assertEquals("jsmith", user.getUsername());
        assertEquals("John Smith", user.getName());
        assertEquals("jsmith@email.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
        assertEquals(date, user.getLastLogin());
    }


    @Test
    void wrongPasswordThrowsException() {
        User user = new User("jdoe", "OldPassword123", "John Doe", "jdoe@example.com", "CUSTOMER");

        assertThrows(IllegalArgumentException.class, () -> user.changePassword("WrongPassword", "NewPassword123"));
    }

    @Test
    void sameOldPasswordThrowsException() {
        User user = new User("jdoe", "Password123", "John Doe", "jdoe@example.com", "CUSTOMER");

        assertThrows(IllegalArgumentException.class, () -> user.changePassword("Password123", "Password123"));
    }

    @Test
    void blankPasswordThrowsException() {
        User user = new User("jdoe", "OldPassword123", "John Doe", "jdoe@example.com", "CUSTOMER");

        assertThrows(IllegalArgumentException.class, () -> user.changePassword("Password123", ""));
    }

    @Test
    void testCorrectPasswordChange() {
        User user = new User("jdoe", "OldPassword123", "John Doe", "jdoe@example.com", "CUSTOMER");

        user.changePassword("OldPassword123", "NewPassword123");

        assertTrue(user.checkPassword("NewPassword123"));
        assertFalse(user.checkPassword("OldPassword123"));
        assertFalse(user.mustChangePassword());
    }

    @Test
    void testCorrectWrongNullPassword() {
        User user = new User("jdoe", "Password123", "John Doe", "jdoe@example.com", "CUSTOMER");

        assertTrue(user.checkPassword("Password123"));
        assertFalse(user.checkPassword("WrongPassword123"));
        assertFalse(user.checkPassword(null));
    }

    @Test
    void testHashedPassword(){
        User user = new User("jdoe", "Password123", "John Doe", "jdoe@example.com", "CUSTOMER");

        assertNotEquals("Password123", user.getPassword());
    }
}