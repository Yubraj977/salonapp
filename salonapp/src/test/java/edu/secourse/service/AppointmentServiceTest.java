package edu.secourse.service;

import edu.secourse.model.Appointment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import edu.secourse.model.Appointment;
import edu.secourse.model.User;
import edu.secourse.session.UserSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    private AppointmentService apptServ;
    private LocalDateTime dateTime1;
    private LocalDateTime dateTime2;

    @BeforeEach
    void setUp() {
        apptServ = new AppointmentService();
        dateTime1 = LocalDateTime.of(2026, 5, 10, 10, 30);
        dateTime2 = LocalDateTime.of(2026, 6, 15, 14, 0);
    }

    @AfterEach
    void tearDown() {
        UserSession.logout();
    }

    @Test
    void createAppointment_successfullyAddsAppointment() {
        Appointment appointment = new Appointment(67, 69, dateTime1);

        boolean result = apptServ.createAppointment(appointment);

        assertTrue(result);
        assertEquals(1, apptServ.getAppointments().size());
    }

    @Test
    void createAppointment_nullAppointmentThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> apptServ.createAppointment(null)
        );

        assertEquals("Appointment cannot be null", exception.getMessage());
    }

    @Test
    void createAppointment_duplicateDateTimeReturnsFalse() {
        Appointment appointment1 = new Appointment(67, 69, dateTime1);
        Appointment appointment2 = new Appointment(68, 70, dateTime1);

        assertTrue(apptServ.createAppointment(appointment1));

        boolean result = apptServ.createAppointment(appointment2);

        assertFalse(result);
        assertEquals(1, apptServ.getAppointments().size());
    }

    @Test
    void getAppointments_returnsCopyOfAppointmentsList() {
        Appointment appointment1 = new Appointment(67, 69, dateTime1);
        Appointment appointment2 = new Appointment(68, 70, dateTime2);

        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);

        List<Appointment> appointments = apptServ.getAppointments();
        appointments.clear();

        assertEquals(2, apptServ.getAppointments().size());
    }

    @Test
    void isApptBooked_returnsTrueWhenDateTimeExists() {
        Appointment appointment = new Appointment(67, 69, dateTime1);
        apptServ.createAppointment(appointment);

        assertTrue(apptServ.isApptBooked(dateTime1));
    }

    @Test
    void isApptBooked_returnsFalseWhenDateTimeDoesNotExist() {
        Appointment appointment = new Appointment(67, 69, dateTime1);
        apptServ.createAppointment(appointment);

        assertFalse(apptServ.isApptBooked(dateTime2));
    }

    @Test
    void isApptBooked_returnsFalseWhenDateTimeIsNull() {
        assertFalse(apptServ.isApptBooked(null));
    }

    @Test
    void getAppointmentById_returnsAppointmentWhenFound() {
        Appointment appointment = new Appointment(67, 69, dateTime1);
        apptServ.createAppointment(appointment);

        Optional<Appointment> result =
                apptServ.getAppointmentById(appointment.getAppointmentId());

        assertTrue(result.isPresent());
        assertEquals(appointment.getAppointmentId(), result.get().getAppointmentId());
    }

    @Test
    void getAppointmentById_returnsEmptyWhenNotFound() {
        Optional<Appointment> result = apptServ.getAppointmentById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAppointmentByStartDateTime_returnsAppointmentWhenFound() {
        Appointment appointment = new Appointment(67, 69, dateTime1);
        apptServ.createAppointment(appointment);

        Optional<Appointment> result =
                apptServ.getAppointmentByStartDateTime(dateTime1);

        assertTrue(result.isPresent());
        assertEquals(dateTime1, result.get().getStartDateTime());
    }

    @Test
    void getAppointmentByStartDateTime_returnsEmptyWhenNotFound() {
        Appointment appointment = new Appointment(67, 69, dateTime1);
        apptServ.createAppointment(appointment);

        Optional<Appointment> result =
                apptServ.getAppointmentByStartDateTime(dateTime2);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAppointmentsByCustomer_returnsOnlyMatchingCustomerAppointments() {
        Appointment appointment1 = new Appointment(10, 20, dateTime1);
        Appointment appointment2 = new Appointment(10, 30, dateTime2);
        Appointment appointment3 = new Appointment(99, 20,
                LocalDateTime.of(2026, 7, 1, 9, 0));

        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);
        apptServ.createAppointment(appointment3);

        List<Appointment> result = apptServ.getAppointmentsByCustomer(10);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getCustomerId() == 10));
    }

    @Test
    void getAppointmentsByStylist_returnsOnlyMatchingStylistAppointments() {
        Appointment appointment1 = new Appointment(10, 20, dateTime1);
        Appointment appointment2 = new Appointment(11, 20, dateTime2);
        Appointment appointment3 = new Appointment(12, 99,
                LocalDateTime.of(2026, 7, 1, 9, 0));

        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);
        apptServ.createAppointment(appointment3);

        List<Appointment> result = apptServ.getAppointmentsByStylist(20);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getStylistId() == 20));
    }

    @Test
    void getAppointmentsForLoggedInUser_returnsEmptyWhenNoUserLoggedIn() {
        List<Appointment> result = apptServ.getAppointmentsForLoggedInUser();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAppointmentsForLoggedInUser_returnsAllAppointmentsForAdmin() {
        Appointment appointment1 = new Appointment(10, 20, dateTime1);
        Appointment appointment2 = new Appointment(11, 21, dateTime2);

        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);

        User admin = new User("admin", "pass", "Admin User", "admin@email.com", "ADMIN");
        UserSession.login(admin);

        List<Appointment> result = apptServ.getAppointmentsForLoggedInUser();

        assertEquals(2, result.size());
    }

    @Test
    void getAppointmentsForLoggedInUser_returnsCustomerAppointmentsOnly() {
        User customer = new User("customer", "pass", "Customer User", "customer@email.com", "CUSTOMER");

        Appointment appointment1 = new Appointment(customer.getAccountId(), 20, dateTime1);
        Appointment appointment2 = new Appointment(999, 20, dateTime2);

        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);

        UserSession.login(customer);

        List<Appointment> result = apptServ.getAppointmentsForLoggedInUser();

        assertEquals(1, result.size());
        assertEquals(customer.getAccountId(), result.get(0).getCustomerId());
    }

    @Test
    void getAppointmentsForLoggedInUser_returnsStylistAppointmentsOnly() {
        User stylist = new User("stylist", "pass", "Stylist User", "stylist@email.com", "STYLIST");

        Appointment appointment1 = new Appointment(10, stylist.getAccountId(), dateTime1);
        Appointment appointment2 = new Appointment(10, 999, dateTime2);

        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);

        UserSession.login(stylist);

        List<Appointment> result = apptServ.getAppointmentsForLoggedInUser();

        assertEquals(1, result.size());
        assertEquals(stylist.getAccountId(), result.get(0).getStylistId());
    }

    @Test
    void getAppointmentsForLoggedInUser_returnsEmptyForUnknownRole() {
        User unknownUser = new User("guest", "pass", "Guest User", "guest@email.com", "GUEST");

        Appointment appointment = new Appointment(10, 20, dateTime1);
        apptServ.createAppointment(appointment);

        UserSession.login(unknownUser);

        List<Appointment> result = apptServ.getAppointmentsForLoggedInUser();

        assertTrue(result.isEmpty());
    }

    @Test
    void updateAppointment_returnsTrueAndUpdatesDateTimeWhenFound() {
        Appointment appointment = new Appointment(67, 69, dateTime1);
        apptServ.createAppointment(appointment);

        boolean result = apptServ.updateAppointment(
                appointment.getAppointmentId(),
                dateTime2
        );

        assertTrue(result);
        assertEquals(dateTime2, appointment.getStartDateTime());
    }

    @Test
    void updateAppointment_returnsFalseWhenAppointmentNotFound() {
        boolean result = apptServ.updateAppointment(999, dateTime2);

        assertFalse(result);
    }

    @Test
    void cancelAppointment_returnsTrueAndRemovesAppointmentWhenFound() {
        Appointment appointment = new Appointment(67, 69, dateTime1);
        apptServ.createAppointment(appointment);

        boolean result = apptServ.cancelAppointment(appointment.getAppointmentId());

        assertTrue(result);
        assertEquals(0, apptServ.getAppointments().size());
    }

    @Test
    void cancelAppointment_returnsFalseWhenAppointmentNotFound() {
        boolean result = apptServ.cancelAppointment(999);

        assertFalse(result);
    }
}
//class AppointmentServiceTest {
//    private AppointmentService apptServ;
//    @Test
//    void createAppointment() {
//        apptServ = new AppointmentService();
//        LocalDateTime localDateTime = LocalDateTime.now();
//        Appointment appointment1 = new Appointment(67, 69,localDateTime);
//        apptServ.createAppointment(appointment1);
//        assertEquals(1, apptServ.getAppointments().size());
//    }
//
//    @Test
//    void getAppointments() {
//        apptServ = new AppointmentService();
//        LocalDateTime ldt = LocalDateTime.now();
//        LocalDateTime ldt2 = LocalDateTime.of(2026, 12, 25, 4, 20);
//        Appointment appointment1 = new Appointment(67, 69,ldt);
//        Appointment appointment2 = new Appointment(68, 66,ldt2);
//        apptServ.createAppointment(appointment1);
//        apptServ.createAppointment(appointment2);
//        assertEquals(2, apptServ.getAppointments().size());
//    }
//
//    @Test
//    void isApptBookedTrue() {
//        apptServ = new AppointmentService();
//        LocalDateTime ldt = LocalDateTime.now();
//        LocalDateTime ldt2 = LocalDateTime.of(2026, 12, 25, 4, 20);
//        LocalDateTime ldt3 = LocalDateTime.of(2026, 12, 25, 4, 20);
//        Appointment appointment1 = new Appointment(67, 69,ldt);
//        Appointment appointment2 = new Appointment(68, 66,ldt2);
//        Appointment appointment3 = new Appointment(70, 71,ldt3);
//        apptServ.createAppointment(appointment1);
//        apptServ.createAppointment(appointment2);
//        assertEquals(2, apptServ.getAppointments().size());
//    }
//
//    @Test
//    void isApptBookedFalse() {
//        apptServ = new AppointmentService();
//        LocalDateTime ldt = LocalDateTime.now();
//        LocalDateTime ldt2 = LocalDateTime.now();
//        Appointment appointment1 = new Appointment(67, 69,ldt);
//        Appointment appointment2 = new Appointment(68, 66,ldt2);
//        apptServ.createAppointment(appointment1);
//        apptServ.createAppointment(appointment2);
//        assertEquals(2, apptServ.getAppointments().size());
//    }
//
//    @Test
//    void getAppointmentById() {
//        apptServ = new AppointmentService();
//        LocalDateTime ldt = LocalDateTime.now();
//        Appointment appointment1 = new Appointment(67, 69,ldt);
//        apptServ.createAppointment(appointment1);
//        assertTrue(apptServ.getAppointmentById(appointment1.getAppointmentId()).isPresent());
//    }
//
//    @Test
//    void updateAppointment() {
//        apptServ = new AppointmentService();
//        LocalDateTime ldt = LocalDateTime.now();
//        LocalDateTime ldt2 = LocalDateTime.of(2026, 12, 25, 4, 20);
//        Appointment appointment1 = new Appointment(67, 69,ldt);
//        apptServ.createAppointment(appointment1);
//        apptServ.updateAppointment(appointment1.getAppointmentId(), ldt2);
//        assertTrue(appointment1.getStartDateTime().isEqual(ldt2));
//    }
//
//    @Test
//    void cancelAppointment() {
//        apptServ = new AppointmentService();
//        LocalDateTime ldt = LocalDateTime.now();
//        Appointment appointment1 = new Appointment(67, 69,ldt);
//        apptServ.createAppointment(appointment1);
//        apptServ.cancelAppointment(appointment1.getAppointmentId());
//        assertEquals(0, apptServ.getAppointments().size());
//    }
//}