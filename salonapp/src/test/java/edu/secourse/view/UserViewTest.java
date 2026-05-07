package edu.secourse.view;

import edu.secourse.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserViewTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void showMenuAndGetOption_adminReturnsSelectedOption() {
        setInput("2\n");

        UserView view = new UserView();

        int option = view.showMenuAndGetOption("admin");

        assertEquals(2, option);
    }

    @Test
    void showMenuAndGetOption_customerReturnsSelectedOption() {
        setInput("1\n");

        UserView view = new UserView();

        int option = view.showMenuAndGetOption("customer");

        assertEquals(1, option);
    }

    @Test
    void showMenuAndGetOption_stylistReturnsSelectedOption() {
        setInput("1\n");

        UserView view = new UserView();

        int option = view.showMenuAndGetOption("stylist");

        assertEquals(1, option);
    }

    @Test
    void showMenuAndGetOption_invalidNumberThrowsException() {
        setInput("abc\n");

        UserView view = new UserView();

        assertThrows(NumberFormatException.class,
                () -> view.showMenuAndGetOption("admin"));
    }

    @Test
    void getUserCreationDetails_createsUserWithValidInputs() {
        setInput("""
                jdoe
                pass123
                John Doe
                jdoe@email.com
                CUSTOMER
                """);

        UserView view = new UserView();

        User user = view.getUserCreationDetails();

        assertNotNull(user);
    }

    @Test
    void getUserCreationDetails_retriesInvalidEmailThenCreatesUser() {
        ByteArrayOutputStream errorOutput = captureError();

        setInput("""
                jdoe
                pass123
                John Doe
                wrong-email
                jdoe@email.com
                CUSTOMER
                """);

        UserView view = new UserView();

        User user = view.getUserCreationDetails();

        assertNotNull(user);
        assertTrue(errorOutput.toString().contains("Invalid email!"));
    }

    @Test
    void getUserCreationDetails_retriesInvalidRoleThenCreatesUser() {
        ByteArrayOutputStream output = captureOutput();

        setInput("""
                jdoe
                pass123
                John Doe
                jdoe@email.com
                WRONG_ROLE
                CUSTOMER
                """);

        UserView view = new UserView();

        User user = view.getUserCreationDetails();

        assertNotNull(user);
        assertTrue(output.toString().contains("Invalid role. Please try again!"));
    }

    @Test
    void getAccountIdInput_returnsAccountId() {
        setInput("15\n");

        UserView view = new UserView();

        assertEquals(15, view.getAccountIdInput());
    }

    @Test
    void getAccountIdInput_invalidNumberThrowsException() {
        setInput("bad-id\n");

        UserView view = new UserView();

        assertThrows(NumberFormatException.class, view::getAccountIdInput);
    }

    @Test
    void getUsernameInput_returnsUsername() {
        setInput("jdoe\n");

        UserView view = new UserView();

        assertEquals("jdoe", view.getUsernameInput());
    }

    @Test
    void getPasswordInput_returnsPassword() {
        setInput("pass123\n");

        UserView view = new UserView();

        assertEquals("pass123", view.getPasswordInput());
    }

    @Test
    void getNewPasswordInput_returnsNewPassword() {
        setInput("newpass123\n");

        UserView view = new UserView();

        assertEquals("newpass123", view.getNewPasswordInput());
    }

    @Test
    void getOldPasswordInput_returnsOldPassword() {
        setInput("oldpass123\n");

        UserView view = new UserView();

        assertEquals("oldpass123", view.getOldPasswordInput());
    }

    @Test
    void getUpdatedNameInput_returnsUpdatedName() {
        setInput("Updated Name\n");

        UserView view = new UserView();

        assertEquals("Updated Name", view.getUpdatedNameInput());
    }

    @Test
    void getUpdatedEmailInput_returnsUpdatedEmail() {
        setInput("updated@email.com\n");

        UserView view = new UserView();

        assertEquals("updated@email.com", view.getUpdatedEmailInput());
    }

    @Test
    void getUpdatedRoleInput_returnsUpdatedRole() {
        setInput("ADMIN\n");

        UserView view = new UserView();

        assertEquals("ADMIN", view.getUpdatedRoleInput());
    }

    @Test
    void displayUser_printsUserDetails() {
        ByteArrayOutputStream output = captureOutput();

        UserView view = new UserView();
        User user = new User("jdoe", "pass123", "John Doe", "jdoe@email.com", "CUSTOMER");

        view.displayUser(user);

        assertTrue(output.toString().contains("User Details"));
    }

    @Test
    void displayAllUsers_printsNoUsersWhenEmpty() {
        ByteArrayOutputStream output = captureOutput();

        UserView view = new UserView();

        view.displayAllUsers(List.of());

        assertTrue(output.toString().contains("No users found"));
    }

    @Test
    void displayAllUsers_printsUsersWhenNotEmpty() {
        ByteArrayOutputStream output = captureOutput();

        UserView view = new UserView();
        User user = new User("jdoe", "pass123", "John Doe", "jdoe@email.com", "CUSTOMER");

        view.displayAllUsers(List.of(user));

        String printed = output.toString();

        assertTrue(printed.contains("All Users"));
        assertTrue(printed.contains("User Details"));
    }

    @Test
    void displayMessage_printsMessage() {
        ByteArrayOutputStream output = captureOutput();

        UserView view = new UserView();

        view.displayMessage("Test message");

        assertTrue(output.toString().contains("Test message"));
    }

    @Test
    void displayError_printsErrorMessage() {
        ByteArrayOutputStream errorOutput = captureError();

        UserView view = new UserView();

        view.displayError("Something went wrong");

        assertTrue(errorOutput.toString().contains("Error: Something went wrong"));
    }

    private void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    private ByteArrayOutputStream captureOutput() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        return output;
    }

    private ByteArrayOutputStream captureError() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setErr(new PrintStream(output));
        return output;
    }
}