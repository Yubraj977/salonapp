package edu.secourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    @DisplayName("Should create admin successfully.")
    public void testAdminCreation() {
        Admin admin = new Admin(
                "admin",
                "admin123",
                "Admin Administrator",
                "admin@email.com"
        );

        assertAll(
                ()->assertTrue(admin.getAccountId() > 10000),
                () -> assertEquals("admin", admin.getUsername()),
                () -> assertEquals("Admin Administrator", admin.getName()),
                () -> assertEquals("admin@email.com", admin.getEmail()),
                () -> assertEquals("ADMIN", admin.getRole().toUpperCase()),
                () -> assertTrue(admin.checkPassword("admin123"))
        );
    }

}