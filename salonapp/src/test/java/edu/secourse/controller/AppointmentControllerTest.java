package edu.secourse.controller;

import edu.secourse.model.Appointment;
import edu.secourse.service.AppointmentService;
import edu.secourse.view.AppointmentView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    private AppointmentService appointmentService;
    private AppointmentView appointmentView;
    private AppointmentController appointmentController;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentService = mock(AppointmentService.class);
        appointmentView = mock(AppointmentView.class);
        appointmentController = new AppointmentController(appointmentService, appointmentView);

        appointment = new Appointment(1, 2, LocalDateTime.of(2026, 5, 10, 14, 0));
    }

    @Test
    void testOptionZeroExits() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(0);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Exiting appointment management system...");
    }

    @Test
    void testOptionOneCreateAppointmentSuccess() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(1, 0);
        when(appointmentView.getAppointmentCreationDetails()).thenReturn(appointment);
        when(appointmentService.createAppointment(appointment)).thenReturn(true);

        appointmentController.startAppointmentManagement();

        verify(appointmentService).createAppointment(appointment);
        verify(appointmentView).displayMessage("Successfully created appointment");
    }

    @Test
    void testOptionOneCreateAppointmentFail() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(1, 0);
        when(appointmentView.getAppointmentCreationDetails()).thenReturn(appointment);
        when(appointmentService.createAppointment(appointment)).thenReturn(false);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Appointment could not be created");
    }

    @Test
    void testOptionTwoViewAllAppointments() {
        List<Appointment> appointmentList = new ArrayList<>();

        when(appointmentView.showMenuAndGetOption()).thenReturn(2, 0);
        when(appointmentService.getAppointments()).thenReturn(appointmentList);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayAllAppointments(appointmentList);
    }

    @Test
    void testOptionThreeFindAppointmentByIdFound() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(3, 0);
        when(appointmentView.getAppointmentIdInput()).thenReturn(appointment.getAppointmentId());
        when(appointmentService.getAppointmentById(appointment.getAppointmentId()))
                .thenReturn(Optional.of(appointment));

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayAppointment(appointment);
    }

    @Test
    void testOptionThreeFindAppointmentByIdNotFound() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(3, 0);
        when(appointmentView.getAppointmentIdInput()).thenReturn(999);
        when(appointmentService.getAppointmentById(999)).thenReturn(Optional.empty());

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Appointment not found");
    }

    @Test
    void testOptionFourCancelAppointmentSuccess() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(4, 0);
        when(appointmentView.getAppointmentIdInput()).thenReturn(appointment.getAppointmentId());
        when(appointmentService.cancelAppointment(appointment.getAppointmentId())).thenReturn(true);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Appointment successfully canceled");
    }

    @Test
    void testOptionFourCancelAppointmentFail() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(4, 0);
        when(appointmentView.getAppointmentIdInput()).thenReturn(999);
        when(appointmentService.cancelAppointment(999)).thenReturn(false);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Appointment could not be canceled");
    }

    @Test
    void testOptionFiveDeleteAppointmentSuccess() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(5, 0);
        when(appointmentView.getAppointmentIdInput()).thenReturn(appointment.getAppointmentId());
        when(appointmentService.deleteAppointment(appointment.getAppointmentId())).thenReturn(true);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Successfully deleted appointment");
    }

    @Test
    void testOptionFiveDeleteAppointmentFail() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(5, 0);
        when(appointmentView.getAppointmentIdInput()).thenReturn(999);
        when(appointmentService.deleteAppointment(999)).thenReturn(false);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Error deleting appointment");
    }

    @Test
    void testOptionSixRescheduleAppointmentSuccess() {
        LocalDateTime newDateTime = LocalDateTime.of(2026, 5, 20, 10, 0);

        when(appointmentView.showMenuAndGetOption()).thenReturn(6, 0);
        when(appointmentView.getAppointmentIdInput()).thenReturn(appointment.getAppointmentId());
        when(appointmentView.getNewDateTimeInput()).thenReturn(newDateTime);
        when(appointmentService.rescheduleAppointment(appointment.getAppointmentId(), newDateTime))
                .thenReturn(true);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Appointment successfully rescheduled");
    }

    @Test
    void testOptionSixRescheduleAppointmentFail() {
        LocalDateTime newDateTime = LocalDateTime.of(2026, 5, 20, 10, 0);

        when(appointmentView.showMenuAndGetOption()).thenReturn(6, 0);
        when(appointmentView.getAppointmentIdInput()).thenReturn(999);
        when(appointmentView.getNewDateTimeInput()).thenReturn(newDateTime);
        when(appointmentService.rescheduleAppointment(999, newDateTime)).thenReturn(false);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Appointment could not be rescheduled");
    }

    @Test
    void testOptionSevenViewByCustomer() {
        List<Appointment> customerAppointments = new ArrayList<>();

        when(appointmentView.showMenuAndGetOption()).thenReturn(7, 0);
        when(appointmentView.getCustomerIdInput()).thenReturn(1);
        when(appointmentService.getAppointmentsByCustomer(1)).thenReturn(customerAppointments);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayAllAppointments(customerAppointments);
    }

    @Test
    void testOptionEightViewByStylist() {
        List<Appointment> stylistAppointments = new ArrayList<>();

        when(appointmentView.showMenuAndGetOption()).thenReturn(8, 0);
        when(appointmentView.getStylistIdInput()).thenReturn(2);
        when(appointmentService.getAppointmentsByStylist(2)).thenReturn(stylistAppointments);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayAllAppointments(stylistAppointments);
    }

    @Test
    void testInvalidOption() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(99, 0);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Invalid option selected.");
    }

    @Test
    void testNumberFormatException() {
        when(appointmentView.showMenuAndGetOption())
                .thenThrow(new NumberFormatException())
                .thenReturn(0);

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Please enter a valid number");
    }

    @Test
    void testIllegalArgumentException() {
        when(appointmentView.showMenuAndGetOption()).thenReturn(1, 0);
        when(appointmentView.getAppointmentCreationDetails())
                .thenThrow(new IllegalArgumentException("Date/Time can not be null"));

        appointmentController.startAppointmentManagement();

        verify(appointmentView).displayMessage("Date/Time can not be null");
    }
}
