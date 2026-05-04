package edu.secourse.controller;

import edu.secourse.model.Appointment;
import edu.secourse.service.AppointmentService;
import edu.secourse.view.AppointmentView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for coordinating interactions between the
 * {@link AppointmentView} (presentation layer) and {@link AppointmentService} (business logic layer).
 *
 * <p>This class serves as the central control unit in the Model-View-Controller (MVC)
 * architecture, handling user input, invoking appropriate business logic, and updating
 * the view with results.
 *
 * <p>Responsibilities include:
 * <ul>
 *     <li>Processing appointment commands from the view</li>
 *     <li>Delegating operations to the service layer</li>
 *     <li>Managing application flow and user interaction loop</li>
 *     <li>Handling input validation and runtime exceptions</li>
 * </ul>
 *
 * <p>This implementation uses a console-based interface and runs continuously
 * until the user chooses to exit.
 */
public class AppointmentController {

    /** Service layer responsible for appointment-related business logic. */
    private final AppointmentService appointmentService;

    /** View layer responsible for user interaction. */
    private final AppointmentView appointmentView;

    /**
     * Constructs an {@code AppointmentController} with the specified service and view.
     *
     * @param appointmentService the service used to manage appointment data
     * @param appointmentView    the view used to interact with the user
     */
    public AppointmentController(AppointmentService appointmentService, AppointmentView appointmentView) {
        this.appointmentService = appointmentService;
        this.appointmentView = appointmentView;
    }

    /**
     * Starts the appointment management system.
     *
     * <p>This method runs an interactive loop that continuously displays a menu,
     * processes user input, and invokes the appropriate operations until the
     * user chooses to exit.
     *
     * <p>Exceptions are handled gracefully to prevent application crashes due
     * to invalid input or unexpected runtime errors.
     */
    public void startAppointmentManagement() {
        boolean running = true;

        while (running) {
            try {
                int option = appointmentView.showMenuAndGetOption();

                switch (option) {
                    case 1 -> createAppointment();
                    case 2 -> viewAllAppointments();
                    case 3 -> findAppointmentById();
                    case 4 -> cancelAppointment();
                    case 5 -> deleteAppointment();
                    case 6 -> rescheduleAppointment();
                    case 7 -> viewAppointmentsByCustomer();
                    case 8 -> viewAppointmentsByStylist();
                    case 0 -> {
                        appointmentView.displayMessage("Exiting appointment management system...");
                        running = false;
                    }
                    default -> appointmentView.displayMessage("Invalid option selected.");
                }

            } catch (NumberFormatException e) {
                appointmentView.displayMessage("Please enter a valid number");
            } catch (IllegalArgumentException e) {
                appointmentView.displayMessage(e.getMessage());
            } catch (Exception e) {
                appointmentView.displayMessage("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the creation of a new appointment.
     *
     * <p>Prompts the user for required details and delegates the creation
     * process to the service layer.
     */
    private void createAppointment() {
        Appointment appointment = appointmentView.getAppointmentCreationDetails();
        boolean success = appointmentService.createAppointment(appointment);

        if (success) {
            appointmentView.displayMessage("Successfully created appointment");
        } else {
            appointmentView.displayMessage("Appointment could not be created");
        }
    }

    /**
     * Retrieves and displays all appointments.
     */
    private void viewAllAppointments() {
        appointmentView.displayAllAppointments(appointmentService.getAppointments());
    }

    /**
     * Retrieves and displays an appointment by its ID.
     */
    private void findAppointmentById() {
        int id = appointmentView.getAppointmentIdInput();

        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);

        if (appointment.isPresent()) {
            appointmentView.displayAppointment(appointment.get());
        } else {
            appointmentView.displayMessage("Appointment not found");
        }
    }

    /**
     * Handles cancellation of an appointment by its ID.
     *
     * <p>Sets the appointment status to CANCELED without removing it from the system.
     */
    private void cancelAppointment() {
        int id = appointmentView.getAppointmentIdInput();

        boolean canceled = appointmentService.cancelAppointment(id);

        if (canceled) {
            appointmentView.displayMessage("Appointment successfully canceled");
        } else {
            appointmentView.displayMessage("Appointment could not be canceled");
        }
    }

    /**
     * Handles deletion of an appointment by its ID.
     */
    private void deleteAppointment() {
        int id = appointmentView.getAppointmentIdInput();

        boolean deleted = appointmentService.cancelAppointment(id);

        if (deleted) {
            appointmentView.displayMessage("Successfully deleted appointment");
        } else {
            appointmentView.displayMessage("Error deleting appointment");
        }
    }

    /**
     * Handles rescheduling of an existing appointment.
     *
     * <p>Prompts for the appointment ID and a new date/time, then delegates
     * the update to the service layer.
     */
    private void rescheduleAppointment() {
        int id = appointmentView.getAppointmentIdInput();
        LocalDateTime newDateTime = appointmentView.getNewDateTimeInput();

        boolean rescheduled = appointmentService.updateAppointment(id, newDateTime);

        if (rescheduled) {
            appointmentView.displayMessage("Appointment successfully rescheduled");
        } else {
            appointmentView.displayMessage("Appointment could not be rescheduled");
        }
    }

    /**
     * Retrieves and displays all appointments for a specific customer.
     */
    private void viewAppointmentsByCustomer() {
        int customerId = appointmentView.getCustomerIdInput();

        List<Appointment> appointments = appointmentService.getAppointmentsByCustomer(customerId);
        appointmentView.displayAllAppointments(appointments);
    }

    /**
     * Retrieves and displays all appointments for a specific stylist.
     */
    private void viewAppointmentsByStylist() {
        int stylistId = appointmentView.getStylistIdInput();

        List<Appointment> appointments = appointmentService.getAppointmentsByStylist(stylistId);
        appointmentView.displayAllAppointments(appointments);
    }
}
