package edu.secourse.view;

import edu.secourse.model.Appointment;
import edu.secourse.service.AppointmentService;
import edu.secourse.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based view class responsible for handling all appointment interactions.
 *
 * <p>This class provides methods for:
 * <ul>
 *     <li>Displaying menus and prompts to the user</li>
 *     <li>Collecting appointment input from the console</li>
 *     <li>Displaying appointment information and system messages</li>
 * </ul>
 *
 * <p>The {@code AppointmentView} is part of the presentation layer in the
 * Model-View-Controller (MVC) architecture. It does not contain business logic,
 * but instead delegates processing to the controller.
 */
public class AppointmentView {

    // UserService to enable this view to see user details for validation
    private UserService userService;

    /** Formatter used for parsing and displaying date/time input. */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /** Scanner used to read user input from the console. */
    private final Scanner scanner = new Scanner(System.in);

    public AppointmentView(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the appointment management menu and retrieves the user's selected option.
     *
     * @return the selected menu option as an integer
     * @throws NumberFormatException if the input cannot be parsed as an integer
     */
    public int showMenuAndGetOption(String role) {

        if (role.equalsIgnoreCase("admin")) {
            System.out.println("\n====== Appointment Management =======");
            System.out.println("Please select an option:");
            System.out.println("1. Create Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("3. View Appointment By Id");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Delete Appointment");
            System.out.println("6. Reschedule Appointment");
            System.out.println("7. View Appointments By Customer");
            System.out.println("8. View Appointments By Stylist");
            System.out.println("0. Exit");
        } else if (role.equalsIgnoreCase("customer") || role.equalsIgnoreCase("stylist")) {
            System.out.println("\n====== Appointment Management =======");
            System.out.println("Please select an option:");
            System.out.println("1. View my appointments");
            System.out.println("2. Cancel Appointment");
            System.out.println("3. Reschedule Appointment");
            System.out.println("0. Exit");
        }

        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Prompts the user to enter details required to create a new {@link Appointment}.
     *
     * @return a newly constructed {@link Appointment} object with user-provided details
     * @throws DateTimeParseException if the date/time input is not in the expected format
     */
    public Appointment getAppointmentCreationDetails() {
        System.out.println("\n----- Create Appointment ------");

        // Check to see if customer with id exists
        System.out.println("Please enter customer ID:");
        boolean customerExists = true;

        int customerId = 0;
        while (customerExists) {
            customerId = Integer.parseInt(scanner.nextLine());

            if (userService.existsByAccountNumber(customerId)) {
                customerExists = false;
                System.out.println("Customer ID: " + customerId + " found.");
//                break;
            }else  {
                System.out.println("Customer ID: " + customerId + " not found.\n Please try again.");
                customerExists = true;
            }
        }

        // Check to see if stylist with id exists
        System.out.println("Please enter stylist ID:");
        boolean stylistExists = true;

        int stylistId = 0;
        while (stylistExists) {
            stylistId = Integer.parseInt(scanner.nextLine());

            if (userService.existsByAccountNumber(stylistId)) {
                stylistExists = false;
                System.out.println("Stylist ID: " + stylistId + " found.");
//                break;
            }else  {
                System.out.println("Stylist ID: " + customerId + " not found.\n Please try again.");
                stylistExists = true;
            }
        }

        System.out.println("Please enter appointment date and time (yyyy-MM-dd HH:mm):");
        LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(), FORMATTER);

        return new Appointment(customerId, stylistId, dateTime);
    }

    /**
     * Prompts the user to enter an appointment ID.
     *
     * @return the appointment ID entered by the user
     * @throws NumberFormatException if the input is not a valid integer
     */
    public int getAppointmentIdInput() {
        System.out.println("Please enter appointment ID:");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Prompts the user to enter a customer ID.
     *
     * @return the customer ID entered by the user
     * @throws NumberFormatException if the input is not a valid integer
     */
    public int getCustomerIdInput() {
        System.out.println("Please enter customer ID:");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Prompts the user to enter a stylist ID.
     *
     * @return the stylist ID entered by the user
     * @throws NumberFormatException if the input is not a valid integer
     */
    public int getStylistIdInput() {
        System.out.println("Please enter stylist ID:");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Prompts the user to enter a new date and time for rescheduling.
     *
     * @return the new {@link LocalDateTime} entered by the user
     * @throws DateTimeParseException if the input is not in the expected format
     */
    public LocalDateTime getNewDateTimeInput() {
        System.out.println("Please enter new date and time (yyyy-MM-dd HH:mm):");
        return LocalDateTime.parse(scanner.nextLine(), FORMATTER);
    }

    /**
     * Displays the details of a single appointment.
     *
     * @param appointment the appointment to display
     */
    public void displayAppointment(Appointment appointment) {
        System.out.println("\n----- Appointment Details ------");
        System.out.println(appointment);
    }

    public void displayAppointments(List<Appointment> appointments) {
        System.out.println("\n----- Appointments ------");
        for (Appointment appointment : appointments) {
            displayAppointment(appointment);
        }
    }

    /**
     * Displays a list of all appointments.
     *
     * <p>If the list is empty, an appropriate message is displayed.
     *
     * @param appointments the list of appointments to display
     */
    public void displayAllAppointments(List<Appointment> appointments) {
        System.out.println("\n----- All Appointments ------");

        if (appointments.isEmpty()) {
            System.out.println("No appointments found");
            return;
        }

        for (Appointment appointment : appointments) {
            displayAppointment(appointment);
        }
    }

    /**
     * Displays a general informational message to the user.
     *
     * @param message the message to display
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void displayError(String message) {
        System.err.println("Error: " + message);
    }
}
