package edu.secourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StylistTest {

    @Test
    @DisplayName("Should create stylist successfully.")
    public void testStylistCreation() {
        Stylist stylist = new Stylist(
                "stylist",
                "stylist123",
                "Stylist Test",
                "stylist@email.com"
        );

        assertAll(
                () -> assertTrue(stylist.getAccountId() > 10000),
                () -> assertEquals("stylist", stylist.getUsername()),
                () -> assertEquals("Stylist Test", stylist.getName()),
                () -> assertEquals("stylist@email.com", stylist.getEmail()),
                () -> assertEquals("STYLIST", stylist.getRole().toUpperCase()),
                () -> assertTrue(stylist.checkPassword("stylist123"))
        );
    }
}