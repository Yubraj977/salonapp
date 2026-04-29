package edu.secourse.service;

import edu.secourse.model.Appointment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentService {
    private final List<Appointment> appointments = new ArrayList<>();
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

    public boolean updateAppointment(int cID, LocalDateTime sDT) {
        Optional<Appointment> optionalAppointment = getAppointmentById(cID);
        if (optionalAppointment.isEmpty()) {
            return false;
        }
        Appointment appointment = optionalAppointment.get();
        appointment.setStartDateTime(sDT);
        return true;
    }

    public boolean deleteAppointment(int cID) {
        Optional<Appointment> optionalAppointment = getAppointmentById(cID);
        if (optionalAppointment.isEmpty()) {
            return false;
        }
        return appointments.removeIf(Appointment -> Appointment.getAppointmentId() == cID);
    }
}
