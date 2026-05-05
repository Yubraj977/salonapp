package edu.secourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    @DisplayName("Customer should be created successfully")
    void testCustomerCreation() {
        Customer customer = new Customer(
                "customer",
                "customer123",
                "Customer Test",
                "customer@email.com"
        );

        assertAll(
                () -> assertTrue(customer.getAccountId() > 10000),
                () -> assertEquals("customer", customer.getUsername()),
                () -> assertEquals("Customer Test", customer.getName()),
                () -> assertEquals("customer@email.com", customer.getEmail()),
                () -> assertEquals("CUSTOMER", customer.getRole().toUpperCase()),
                () -> assertTrue(customer.checkPassword("customer123"))
        );
    }

}