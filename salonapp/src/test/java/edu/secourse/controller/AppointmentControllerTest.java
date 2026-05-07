package edu.secourse.controller;

import edu.secourse.model.Appointment;
import edu.secourse.model.User;
import edu.secourse.service.AppointmentService;
import edu.secourse.service.UserService;
import edu.secourse.view.AppointmentView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


import edu.secourse.model.Appointment;
import edu.secourse.model.User;
import edu.secourse.service.AppointmentService;
import edu.secourse.service.UserService;
import edu.secourse.view.AppointmentView;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentControllerTest {

    @Test
    void nullUserReturnsImmediately() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        AppointmentController controller = new AppointmentController(service, view);

        controller.startAppointmentManagement(null);

        assertTrue(view.messages.isEmpty());
    }

    @Test
    void adminCanCreateAppointmentSuccessfully() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();
        Appointment appointment = new Appointment();

        view.menuOptions.addAll(List.of(1, 0));
        view.appointmentToCreate = appointment;
        service.createResult = true;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertSame(appointment, service.createdAppointment);
        assertTrue(view.messages.contains("Successfully created appointment"));
    }

    @Test
    void adminCreateAppointmentCanFail() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(1, 0));
        view.appointmentToCreate = new Appointment();
        service.createResult = false;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Appointment could not be created"));
    }

    @Test
    void adminCanViewAllAppointments() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();
        service.appointments.addAll(List.of(appointment1, appointment2));

        view.menuOptions.addAll(List.of(2, 0));

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(service.getAppointmentsCalled);
        assertEquals(2, view.displayedAllAppointments.size());
    }

    @Test
    void adminCanFindAppointmentByIdWhenFound() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        Appointment appointment = new Appointment();
        view.menuOptions.addAll(List.of(3, 0));
        view.ids.add(10);
        service.appointmentById = Optional.of(appointment);

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertEquals(10, service.lastAppointmentId);
        assertSame(appointment, view.displayedAppointment);
    }

    @Test
    void adminFindAppointmentByIdShowsNotFound() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(3, 0));
        view.ids.add(99);
        service.appointmentById = Optional.empty();

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Appointment not found"));
    }

    @Test
    void adminCanCancelAppointmentSuccessfully() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(4, 0));
        view.ids.add(5);
        service.cancelResult = true;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertEquals(5, service.lastCanceledId);
        assertTrue(view.messages.contains("Appointment successfully canceled"));
    }

    @Test
    void adminCancelAppointmentCanFail() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(4, 0));
        view.ids.add(5);
        service.cancelResult = false;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Appointment could not be canceled"));
    }

    @Test
    void adminCanDeleteAppointmentSuccessfully() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(5, 0));
        view.ids.add(7);
        service.cancelResult = true;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertEquals(7, service.lastCanceledId);
        assertTrue(view.messages.contains("Successfully deleted appointment"));
    }

    @Test
    void adminDeleteAppointmentCanFail() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(5, 0));
        view.ids.add(7);
        service.cancelResult = false;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Error deleting appointment"));
    }

    @Test
    void adminCanRescheduleAppointmentSuccessfully() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        LocalDateTime newTime = LocalDateTime.of(2026, 5, 20, 10, 0);

        view.menuOptions.addAll(List.of(6, 0));
        view.ids.add(3);
        view.newDateTimes.add(newTime);
        service.updateResult = true;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertEquals(3, service.lastUpdatedId);
        assertEquals(newTime, service.lastUpdatedDateTime);
        assertTrue(view.messages.contains("Appointment successfully rescheduled"));
    }

    @Test
    void adminRescheduleAppointmentCanFail() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        LocalDateTime newTime = LocalDateTime.of(2026, 5, 20, 10, 0);

        view.menuOptions.addAll(List.of(6, 0));
        view.ids.add(3);
        view.newDateTimes.add(newTime);
        service.updateResult = false;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Appointment could not be rescheduled"));
    }

    @Test
    void adminCanViewAppointmentsByCustomer() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        service.customerAppointments.add(new Appointment());
        view.menuOptions.addAll(List.of(7, 0));
        view.customerIds.add(12);

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertEquals(12, service.lastCustomerId);
        assertEquals(1, view.displayedAllAppointments.size());
    }

    @Test
    void adminCanViewAppointmentsByStylist() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        service.stylistAppointments.add(new Appointment());
        view.menuOptions.addAll(List.of(8, 0));
        view.stylistIds.add(22);

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertEquals(22, service.lastStylistId);
        assertEquals(1, view.displayedAllAppointments.size());
    }

    @Test
    void adminInvalidOptionShowsMessage() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(99, 0));

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Invalid option selected."));
    }

    @Test
    void customerCanViewOwnAppointments() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        service.loggedInUserAppointments.add(new Appointment());
        view.menuOptions.addAll(List.of(1, 0));

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(customerUser());

        assertTrue(service.getLoggedInAppointmentsCalled);
        assertEquals(1, view.displayedUserAppointments.size());
    }

    @Test
    void customerCanCancelAppointment() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(2, 0));
        view.ids.add(15);
        service.cancelResult = true;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(customerUser());

        assertEquals(15, service.lastCanceledId);
        assertTrue(view.messages.contains("Appointment successfully canceled"));
    }

    @Test
    void stylistCanRescheduleAppointment() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        LocalDateTime newTime = LocalDateTime.of(2026, 6, 1, 9, 30);

        view.menuOptions.addAll(List.of(3, 0));
        view.ids.add(30);
        view.newDateTimes.add(newTime);
        service.updateResult = true;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(stylistUser());

        assertEquals(30, service.lastUpdatedId);
        assertEquals(newTime, service.lastUpdatedDateTime);
        assertTrue(view.messages.contains("Appointment successfully rescheduled"));
    }

    @Test
    void customerInvalidOptionShowsMessage() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(99, 0));

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(customerUser());

        assertTrue(view.messages.contains("Invalid option selected."));
    }

    @Test
    void numberFormatExceptionIsHandled() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.throwNumberFormatOnce = true;
        view.menuOptions.add(0);

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Please enter a valid number"));
    }

    @Test
    void illegalArgumentExceptionIsHandled() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(1, 0));
        view.throwIllegalArgumentOnCreate = true;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Date/Time can not be null"));
    }

    @Test
    void generalExceptionIsHandled() {
        FakeAppointmentService service = new FakeAppointmentService();
        FakeAppointmentView view = new FakeAppointmentView();

        view.menuOptions.addAll(List.of(1, 0));
        view.throwRuntimeExceptionOnCreate = true;

        AppointmentController controller = new AppointmentController(service, view);
        controller.startAppointmentManagement(adminUser());

        assertTrue(view.messages.contains("Error: Unexpected error"));
    }

    private User adminUser() {
        return new User("admin", "admin123", "Admin User", "admin@email.com", "admin");
    }

    private User customerUser() {
        return new User("customer", "pass123", "Customer User", "customer@email.com", "customer");
    }

    private User stylistUser() {
        return new User("stylist", "pass123", "Stylist User", "stylist@email.com", "stylist");
    }

    static class FakeAppointmentService extends AppointmentService {
        List<Appointment> appointments = new ArrayList<>();
        List<Appointment> loggedInUserAppointments = new ArrayList<>();
        List<Appointment> customerAppointments = new ArrayList<>();
        List<Appointment> stylistAppointments = new ArrayList<>();

        boolean createResult;
        boolean cancelResult;
        boolean updateResult;

        boolean getAppointmentsCalled;
        boolean getLoggedInAppointmentsCalled;

        Appointment createdAppointment;
        Optional<Appointment> appointmentById = Optional.empty();

        int lastAppointmentId;
        int lastCanceledId;
        int lastUpdatedId;
        int lastCustomerId;
        int lastStylistId;

        LocalDateTime lastUpdatedDateTime;

        @Override
        public boolean createAppointment(Appointment appointment) {
            this.createdAppointment = appointment;
            return createResult;
        }

        @Override
        public List<Appointment> getAppointments() {
            getAppointmentsCalled = true;
            return appointments;
        }

        @Override
        public List<Appointment> getAppointmentsForLoggedInUser() {
            getLoggedInAppointmentsCalled = true;
            return loggedInUserAppointments;
        }

        @Override
        public Optional<Appointment> getAppointmentById(int id) {
            lastAppointmentId = id;
            return appointmentById;
        }

        @Override
        public boolean cancelAppointment(int id) {
            lastCanceledId = id;
            return cancelResult;
        }

        @Override
        public boolean updateAppointment(int id, LocalDateTime newDateTime) {
            lastUpdatedId = id;
            lastUpdatedDateTime = newDateTime;
            return updateResult;
        }

        @Override
        public List<Appointment> getAppointmentsByCustomer(int customerId) {
            lastCustomerId = customerId;
            return customerAppointments;
        }

        @Override
        public List<Appointment> getAppointmentsByStylist(int stylistId) {
            lastStylistId = stylistId;
            return stylistAppointments;
        }
    }

    static class FakeAppointmentView extends AppointmentView {
        List<Integer> menuOptions = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<Integer> customerIds = new ArrayList<>();
        List<Integer> stylistIds = new ArrayList<>();
        List<LocalDateTime> newDateTimes = new ArrayList<>();

        List<String> messages = new ArrayList<>();
        List<Appointment> displayedAllAppointments = new ArrayList<>();
        List<Appointment> displayedUserAppointments = new ArrayList<>();

        Appointment appointmentToCreate;
        Appointment displayedAppointment;

        boolean throwNumberFormatOnce;
        boolean throwIllegalArgumentOnCreate;
        boolean throwRuntimeExceptionOnCreate;

        public FakeAppointmentView() {
            super();
        }

        public FakeAppointmentView(UserService userService) {
            super(userService);
        }

        @Override
        public int showMenuAndGetOption(String role) {
            if (throwNumberFormatOnce) {
                throwNumberFormatOnce = false;
                throw new NumberFormatException();
            }

            if (menuOptions.isEmpty()) {
                return 0;
            }

            return menuOptions.remove(0);
        }

        @Override
        public Appointment getAppointmentCreationDetails() {
            if (throwIllegalArgumentOnCreate) {
                throw new IllegalArgumentException("Date/Time can not be null");
            }

            if (throwRuntimeExceptionOnCreate) {
                throw new RuntimeException("Unexpected error");
            }

            return appointmentToCreate;
        }

        @Override
        public int getAppointmentIdInput() {
            return ids.remove(0);
        }

        @Override
        public LocalDateTime getNewDateTimeInput() {
            return newDateTimes.remove(0);
        }

        @Override
        public int getCustomerIdInput() {
            return customerIds.remove(0);
        }

        @Override
        public int getStylistIdInput() {
            return stylistIds.remove(0);
        }

        @Override
        public void displayAllAppointments(List<Appointment> appointments) {
            displayedAllAppointments = appointments;
        }

        @Override
        public void displayAppointments(List<Appointment> appointments) {
            displayedUserAppointments = appointments;
        }

        @Override
        public void displayAppointment(Appointment appointment) {
            displayedAppointment = appointment;
        }

        @Override
        public void displayMessage(String message) {
            messages.add(message);
        }
    }
}
//Because AppointmentController depend on console input through AppointmentView,
// a unit test needs some way to control what the view returns. so we use fake/stub classes
//class AppointmentControllerTest {
//
//    @Test
//    void startAppointmentManagement_adminCanViewAllAppointments() {
//        FakeAppointmentService appointmentService = new FakeAppointmentService();
//        FakeAppointmentView appointmentView = new FakeAppointmentView();
//
//        AppointmentController controller =
//                new AppointmentController(appointmentService, appointmentView);
//
//        User admin = new User("admin", "admin123", "Admin User", "admin@email.com", "admin");
//
//        Appointment appointment1 = new Appointment();
//        Appointment appointment2 = new Appointment();
//
//        appointmentService.appointments.add(appointment1);
//        appointmentService.appointments.add(appointment2);
//
//        // Option 2 = view all appointments, then 0 = exit
//        appointmentView.menuOptions.add(2);
//        appointmentView.menuOptions.add(0);
//
//        controller.startAppointmentManagement(admin);
//
//        assertTrue(appointmentService.getAppointmentsCalled);
//        assertEquals(2, appointmentView.displayedAppointments.size());
//        assertTrue(appointmentView.messages.contains("Exiting appointment management system..."));
//    }
//
//    static class FakeAppointmentService extends AppointmentService {
//        List<Appointment> appointments = new ArrayList<>();
//        boolean getAppointmentsCalled = false;
//
//        @Override
//        public List<Appointment> getAppointments() {
//            getAppointmentsCalled = true;
//            return appointments;
//        }
//    }
//
//    static class FakeAppointmentView extends AppointmentView {
//        List<Integer> menuOptions = new ArrayList<>();
//        List<Appointment> displayedAppointments = new ArrayList<>();
//        List<String> messages = new ArrayList<>();
//
//        public FakeAppointmentView() {
//            super();
//        }
//        public FakeAppointmentView(UserService userService) {
//            super(userService);
//        }
//
//        @Override
//        public int showMenuAndGetOption(String role) {
//            return menuOptions.remove(0);
//        }
//
//        @Override
//        public void displayAllAppointments(List<Appointment> appointments) {
//            displayedAppointments = appointments;
//        }
//
//        @Override
//        public void displayMessage(String message) {
//            messages.add(message);
//        }
//    }
//}


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
