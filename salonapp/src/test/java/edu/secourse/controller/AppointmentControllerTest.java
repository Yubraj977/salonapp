package edu.secourse.controller;

import edu.secourse.controller.AppointmentController;
import edu.secourse.model.Admin;
import edu.secourse.model.Appointment;
import edu.secourse.model.User;
import edu.secourse.service.AppointmentService;
import edu.secourse.view.AppointmentView;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    @Test
    void startAppointmentManagement_adminCanViewAllAppointments() {
        // Arrange
        AppointmentService appointmentService = mock(AppointmentService.class);
        AppointmentView appointmentView = mock(AppointmentView.class);

        AppointmentController controller =
                new AppointmentController(appointmentService, appointmentView);

        User admin = new Admin("admin", "Admin User", "admin@email.com", "adminpass");

        List<Appointment> appointments = List.of(
                mock(Appointment.class),
                mock(Appointment.class)
        );

        when(appointmentView.showMenuAndGetOption("admin"))
                .thenReturn(2)   // view all appointments
                .thenReturn(0);  // exit

        when(appointmentService.getAppointments()).thenReturn(appointments);

        // Act
        controller.startAppointmentManagement(admin);

        // Assert
        verify(appointmentService).getAppointments();
        verify(appointmentView).displayAllAppointments(appointments);
        verify(appointmentView).displayMessage("Exiting appointment management system...");
    }

    @Test
    void startAppointmentManagement_customerCanViewOwnAppointments() {
        // Arrange
        AppointmentService appointmentService = mock(AppointmentService.class);
        AppointmentView appointmentView = mock(AppointmentView.class);

        AppointmentController controller =
                new AppointmentController(appointmentService, appointmentView);

        User customer = mock(User.class);
        when(customer.getRole()).thenReturn("customer");

        List<Appointment> appointments = List.of(
                mock(Appointment.class),
                mock(Appointment.class)
        );

        when(appointmentView.showMenuAndGetOption("customer"))
                .thenReturn(1)   // view user appointments
                .thenReturn(0);  // exit

        when(appointmentService.getAppointmentsForLoggedInUser())
                .thenReturn(appointments);

        // Act
        controller.startAppointmentManagement(customer);

        // Assert
        verify(appointmentService).getAppointmentsForLoggedInUser();
        verify(appointmentView).displayAppointments(appointments);
        verify(appointmentView).displayMessage("Exiting appointment management system...");
    }

    @Test
    void startAppointmentManagement_customerCanRescheduleAppointment() {
        // Arrange
        AppointmentService appointmentService = mock(AppointmentService.class);
        AppointmentView appointmentView = mock(AppointmentView.class);

        AppointmentController controller =
                new AppointmentController(appointmentService, appointmentView);

        User customer = mock(User.class);
        when(customer.getRole()).thenReturn("customer");

        LocalDateTime newDateTime = LocalDateTime.of(2026, 5, 10, 14, 30);

        when(appointmentView.showMenuAndGetOption("customer"))
                .thenReturn(3)   // reschedule
                .thenReturn(0);  // exit

        when(appointmentView.getAppointmentIdInput()).thenReturn(1);
        when(appointmentView.getNewDateTimeInput()).thenReturn(newDateTime);

        when(appointmentService.updateAppointment(1, newDateTime))
                .thenReturn(true);

        // Act
        controller.startAppointmentManagement(customer);

        // Assert
        verify(appointmentService).updateAppointment(1, newDateTime);
        verify(appointmentView).displayMessage("Appointment successfully rescheduled");
    }
}


//package edu.secourse.controller;
//
//import edu.secourse.model.Appointment;
//import edu.secourse.model.User;
//import edu.secourse.service.AppointmentService;
//import edu.secourse.session.UserSession;
//import edu.secourse.view.AppointmentView;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//class AppointmentControllerTest {
//
//    private AppointmentService appointmentService;
//    private AppointmentView appointmentView;
//    private AppointmentController appointmentController;
//    private Appointment appointment;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        appointmentService = mock(AppointmentService.class);
//        appointmentView = mock(AppointmentView.class);
//        appointmentController = new AppointmentController(appointmentService, appointmentView);
//
//        user = new User(
//                "admin",
//                "admin123",
//                "Admin admin",
//                "admin@email.com",
//                "ADMIN"
//        );
//        UserSession.login(user);
//        appointment = new Appointment(1, 2, LocalDateTime.of(2026, 5, 10, 14, 0));
//    }
//
//    @Test
//    void testOptionZeroExits() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(0);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Exiting appointment management system...");
//    }
//
//    @Test
//    void testOptionOneCreateAppointmentSuccess() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(1, 0);
//        when(appointmentView.getAppointmentCreationDetails()).thenReturn(appointment);
//        when(appointmentService.createAppointment(appointment)).thenReturn(true);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentService).createAppointment(appointment);
//        verify(appointmentView).displayMessage("Successfully created appointment");
//    }
//
//    @Test
//    void testOptionOneCreateAppointmentFail() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(1, 0);
//        when(appointmentView.getAppointmentCreationDetails()).thenReturn(appointment);
//        when(appointmentService.createAppointment(appointment)).thenReturn(false);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Appointment could not be created");
//    }
//
//    @Test
//    void testOptionTwoViewAllAppointments() {
//        User loggedUser = UserSession.getLoggedInUser();
//        List<Appointment> appointmentList = new ArrayList<>();
//
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(2, 0);
//        when(appointmentService.getAppointments()).thenReturn(appointmentList);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayAllAppointments(appointmentList);
//    }
//
//    @Test
//    void testOptionThreeFindAppointmentByIdFound() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(3, 0);
//        when(appointmentView.getAppointmentIdInput()).thenReturn(appointment.getAppointmentId());
//        when(appointmentService.getAppointmentById(appointment.getAppointmentId()))
//                .thenReturn(Optional.of(appointment));
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayAppointment(appointment);
//    }
//
//    @Test
//    void testOptionThreeFindAppointmentByIdNotFound() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(3, 0);
//        when(appointmentView.getAppointmentIdInput()).thenReturn(999);
//        when(appointmentService.getAppointmentById(999)).thenReturn(Optional.empty());
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Appointment not found");
//    }
//
//    @Test
//    void testOptionFourCancelAppointmentSuccess() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(4, 0);
//        when(appointmentView.getAppointmentIdInput()).thenReturn(appointment.getAppointmentId());
//        when(appointmentService.cancelAppointment(appointment.getAppointmentId())).thenReturn(true);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Appointment successfully canceled");
//    }
//
//    @Test
//    void testOptionFourCancelAppointmentFail() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(4, 0);
//        when(appointmentView.getAppointmentIdInput()).thenReturn(999);
//        when(appointmentService.cancelAppointment(999)).thenReturn(false);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Appointment could not be canceled");
//    }
//
////    @Test
////    void testOptionFiveDeleteAppointmentSuccess() {
////        User loggedUser = UserSession.getLoggedInUser();
////        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(5, 0);
////        when(appointmentView.getAppointmentIdInput()).thenReturn(appointment.getAppointmentId());
////        when(appointmentService.deleteAppointment(appointment.getAppointmentId())).thenReturn(true);
////
////        appointmentController.startAppointmentManagement(loggedUser);
////
////        verify(appointmentView).displayMessage("Successfully deleted appointment");
////    }
//
////    @Test
////    void testOptionFiveDeleteAppointmentFail() {
////        User loggedUser = UserSession.getLoggedInUser();
////        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(5, 0);
////        when(appointmentView.getAppointmentIdInput()).thenReturn(999);
////        when(appointmentService.deleteAppointment(999)).thenReturn(false);
////
////        appointmentController.startAppointmentManagement(loggedUser);
////
////        verify(appointmentView).displayMessage("Error deleting appointment");
////    }
//
//    @Test
//    void testOptionSixRescheduleAppointmentSuccess() {
//        User loggedUser = UserSession.getLoggedInUser();
//        LocalDateTime newDateTime = LocalDateTime.of(2026, 5, 20, 10, 0);
//
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(6, 0);
//        when(appointmentView.getAppointmentIdInput()).thenReturn(appointment.getAppointmentId());
//        when(appointmentView.getNewDateTimeInput()).thenReturn(newDateTime);
//        when(appointmentService.updateAppointment(appointment.getAppointmentId(), newDateTime))
//                .thenReturn(true);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Appointment successfully rescheduled");
//    }
//
//    @Test
//    void testOptionSixRescheduleAppointmentFail() {
//        User loggedUser = UserSession.getLoggedInUser();
//        LocalDateTime newDateTime = LocalDateTime.of(2026, 5, 20, 10, 0);
//
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(6, 0);
//        when(appointmentView.getAppointmentIdInput()).thenReturn(999);
//        when(appointmentView.getNewDateTimeInput()).thenReturn(newDateTime);
//        when(appointmentService.updateAppointment(999, newDateTime)).thenReturn(false);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Appointment could not be rescheduled");
//    }
//
//    @Test
//    void testOptionSevenViewByCustomer() {
//        List<Appointment> customerAppointments = new ArrayList<>();
//        User loggedUser = UserSession.getLoggedInUser();
//
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(7, 0);
//        when(appointmentView.getCustomerIdInput()).thenReturn(1);
//        when(appointmentService.getAppointmentsByCustomer(1)).thenReturn(customerAppointments);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayAllAppointments(customerAppointments);
//    }
//
//    @Test
//    void testOptionEightViewByStylist() {
//        List<Appointment> stylistAppointments = new ArrayList<>();
//        User loggedUser = UserSession.getLoggedInUser();
//
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(8, 0);
//        when(appointmentView.getStylistIdInput()).thenReturn(2);
//        when(appointmentService.getAppointmentsByStylist(2)).thenReturn(stylistAppointments);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayAllAppointments(stylistAppointments);
//    }
//
//    @Test
//    void testInvalidOption() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(99, 0);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Invalid option selected.");
//    }
//
//    @Test
//    void testNumberFormatException() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole()))
//                .thenThrow(new NumberFormatException())
//                .thenReturn(0);
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Please enter a valid number");
//    }
//
//    @Test
//    void testIllegalArgumentException() {
//        User loggedUser = UserSession.getLoggedInUser();
//        when(appointmentView.showMenuAndGetOption(loggedUser.getRole())).thenReturn(1, 0);
//        when(appointmentView.getAppointmentCreationDetails())
//                .thenThrow(new IllegalArgumentException("Date/Time can not be null"));
//
//        appointmentController.startAppointmentManagement(loggedUser);
//
//        verify(appointmentView).displayMessage("Date/Time can not be null");
//    }
//}
