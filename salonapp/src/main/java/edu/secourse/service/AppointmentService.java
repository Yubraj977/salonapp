package edu.secourse.service;

import edu.secourse.model.*;
import edu.secourse.session.UserSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentService {
    private final List<Appointment> appointments = new ArrayList<>();

    public AppointmentService(){}

    public boolean createAppointment(Appointment appointment) {
        if  (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        if (isApptBooked(appointment.getStartDateTime())) {
            System.out.println("Appointment time slot is already booked");
            return false;
        }

        appointments.add(appointment);
        return true;
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public boolean isApptBooked(LocalDateTime bookedOn) {
        if (bookedOn == null) {
            return false;
        }
        return appointments.stream().anyMatch(Appointment -> Appointment.getStartDateTime().equals(bookedOn));
    }

    public Optional<Appointment> getAppointmentById(int id) {
        return appointments.stream().filter(Appointment -> Appointment.getAppointmentId() == id).findFirst();
    }

    public Optional<Appointment> getAppointmentByStartDateTime(LocalDateTime sDT) {
        return appointments.stream().filter(Appointment -> Appointment.getStartDateTime().equals(sDT)).findFirst();
    }

    public List<Appointment> getAppointmentsByCustomer(int customerId) {
        return appointments.stream()
                .filter(appointment -> appointment.getCustomerId() == customerId)
                .toList();
    }

    public List<Appointment> getAppointmentsByStylist(int stylistId) {
        return appointments.stream()
                .filter(appointment -> appointment.getStylistId() == stylistId)
                .toList();
    }

    public List<Appointment> getAppointmentsForLoggedInUser() {
        User user = UserSession.getLoggedInUser();

        if (user == null) {
            return new ArrayList<>();
        }

        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            return appointments;
        }

        if (user.getRole().equalsIgnoreCase("CUSTOMER")) {
            return appointments.stream()
                    .filter(appointment -> appointment.getCustomerId() == user.getAccountId())
                    .toList();
        }

        if (user.getRole().equalsIgnoreCase("STYLIST")) {
            return appointments.stream()
                    .filter(appointment -> appointment.getStylistId() == user.getAccountId())
                    .toList();
        }

        return new ArrayList<>();
    }

    public boolean updateAppointment(int aID, LocalDateTime sDT) {
        Optional<Appointment> optionalAppointment = getAppointmentById(aID);
        if (optionalAppointment.isEmpty()) {
            return false;
        }
        Appointment appointment = optionalAppointment.get();
        appointment.setStartDateTime(sDT);
        return true;
    }

    public boolean cancelAppointment(int aID) {
        Optional<Appointment> optionalAppointment = getAppointmentById(aID);
        if (optionalAppointment.isEmpty()) {
            return false;
        }
        return appointments.removeIf(Appointment -> Appointment.getAppointmentId() == aID);
    }
}
