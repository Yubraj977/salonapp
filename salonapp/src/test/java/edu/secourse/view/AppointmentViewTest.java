package edu.secourse.view;

import static org.junit.jupiter.api.Assertions.*;

import edu.secourse.model.Appointment;
import edu.secourse.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

class AppointmentViewTest {

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

        AppointmentView view = new AppointmentView();

        int option = view.showMenuAndGetOption("admin");

        assertEquals(2, option);
    }

    @Test
    void showMenuAndGetOption_customerReturnsSelectedOption() {
        setInput("1\n");

        AppointmentView view = new AppointmentView();

        int option = view.showMenuAndGetOption("customer");

        assertEquals(1, option);
    }

    @Test
    void showMenuAndGetOption_stylistReturnsSelectedOption() {
        setInput("3\n");

        AppointmentView view = new AppointmentView();

        int option = view.showMenuAndGetOption("stylist");

        assertEquals(3, option);
    }

    @Test
    void showMenuAndGetOption_invalidNumberThrowsException() {
        setInput("abc\n");

        AppointmentView view = new AppointmentView();

        assertThrows(NumberFormatException.class,
                () -> view.showMenuAndGetOption("admin"));
    }

    @Test
    void getAppointmentCreationDetails_createsAppointmentWhenIdsExist() {
        setInput("""
                10
                20
                2026-05-20 14:30
                """);

        FakeUserService userService = new FakeUserService(true);
        AppointmentView view = new AppointmentView(userService);

        Appointment appointment = view.getAppointmentCreationDetails();

        assertNotNull(appointment);
    }

    @Test
    void getAppointmentCreationDetails_retriesUntilCustomerAndStylistExist() {
        setInput("""
                99
                10
                88
                20
                2026-05-20 14:30
                """);

        FakeUserService userService = new FakeUserService(false, true, false, true);
        AppointmentView view = new AppointmentView(userService);

        Appointment appointment = view.getAppointmentCreationDetails();

        assertNotNull(appointment);
        assertEquals(4, userService.callCount);
    }

    @Test
    void getAppointmentCreationDetails_invalidDateThrowsException() {
        setInput("""
                10
                20
                wrong-date
                """);

        FakeUserService userService = new FakeUserService(true);
        AppointmentView view = new AppointmentView(userService);

        assertThrows(Exception.class, view::getAppointmentCreationDetails);
    }

    @Test
    void getAppointmentIdInput_returnsId() {
        setInput("15\n");

        AppointmentView view = new AppointmentView();

        assertEquals(15, view.getAppointmentIdInput());
    }

    @Test
    void getCustomerIdInput_returnsId() {
        setInput("25\n");

        AppointmentView view = new AppointmentView();

        assertEquals(25, view.getCustomerIdInput());
    }

    @Test
    void getStylistIdInput_returnsId() {
        setInput("35\n");

        AppointmentView view = new AppointmentView();

        assertEquals(35, view.getStylistIdInput());
    }

    @Test
    void getNewDateTimeInput_returnsDateTime() {
        setInput("2026-06-01 09:45\n");

        AppointmentView view = new AppointmentView();

        LocalDateTime result = view.getNewDateTimeInput();

        assertEquals(LocalDateTime.of(2026, 6, 1, 9, 45), result);
    }

    @Test
    void getNewDateTimeInput_invalidDateThrowsException() {
        setInput("bad-date\n");

        AppointmentView view = new AppointmentView();

        assertThrows(Exception.class, view::getNewDateTimeInput);
    }

    @Test
    void displayAppointment_printsAppointmentDetails() {
        ByteArrayOutputStream output = captureOutput();

        AppointmentView view = new AppointmentView();
        Appointment appointment = new Appointment();

        view.displayAppointment(appointment);

        String printed = output.toString();

        assertTrue(printed.contains("Appointment Details"));
    }

    @Test
    void displayAppointments_printsAppointmentsHeader() {
        ByteArrayOutputStream output = captureOutput();

        AppointmentView view = new AppointmentView();

        view.displayAppointments(List.of(new Appointment(), new Appointment()));

        String printed = output.toString();

        assertTrue(printed.contains("Appointments"));
        assertTrue(printed.contains("Appointment Details"));
    }

    @Test
    void displayAllAppointments_printsNoAppointmentsWhenEmpty() {
        ByteArrayOutputStream output = captureOutput();

        AppointmentView view = new AppointmentView();

        view.displayAllAppointments(List.of());

        String printed = output.toString();

        assertTrue(printed.contains("All Appointments"));
        assertTrue(printed.contains("No appointments found"));
    }

    @Test
    void displayAllAppointments_printsAppointmentsWhenNotEmpty() {
        ByteArrayOutputStream output = captureOutput();

        AppointmentView view = new AppointmentView();

        view.displayAllAppointments(List.of(new Appointment()));

        String printed = output.toString();

        assertTrue(printed.contains("All Appointments"));
        assertTrue(printed.contains("Appointment Details"));
    }

    @Test
    void displayMessage_printsMessage() {
        ByteArrayOutputStream output = captureOutput();

        AppointmentView view = new AppointmentView();

        view.displayMessage("Test message");

        assertTrue(output.toString().contains("Test message"));
    }

    @Test
    void displayError_printsErrorMessage() {
        ByteArrayOutputStream errorOutput = captureError();

        AppointmentView view = new AppointmentView();

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

    static class FakeUserService extends UserService {
        private final boolean[] results;
        private int callCount = 0;

        FakeUserService(boolean... results) {
            this.results = results;
        }

        @Override
        public boolean existsByAccountNumber(int accountNumber) {
            if (results.length == 0) {
                return true;
            }

            boolean result = results[Math.min(callCount, results.length - 1)];
            callCount++;
            return result;
        }
    }
}