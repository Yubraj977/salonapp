package edu.secourse.service;

import edu.secourse.model.Appointment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {
    private AppointmentService apptServ;
    @Test
    void createAppointment() {
        apptServ = new AppointmentService();
        LocalDateTime localDateTime = LocalDateTime.now();
        Appointment appointment1 = new Appointment(67, 69,localDateTime);
        apptServ.createAppointment(appointment1);
        assertEquals(1, apptServ.getAppointments().size());
    }

    @Test
    void getAppointments() {
        apptServ = new AppointmentService();
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = LocalDateTime.of(2026, 12, 25, 4, 20);
        Appointment appointment1 = new Appointment(67, 69,ldt);
        Appointment appointment2 = new Appointment(68, 66,ldt2);
        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);
        assertEquals(2, apptServ.getAppointments().size());
    }

    @Test
    void isApptBookedTrue() {
        apptServ = new AppointmentService();
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = LocalDateTime.of(2026, 12, 25, 4, 20);
        LocalDateTime ldt3 = LocalDateTime.of(2026, 12, 25, 4, 20);
        Appointment appointment1 = new Appointment(67, 69,ldt);
        Appointment appointment2 = new Appointment(68, 66,ldt2);
        Appointment appointment3 = new Appointment(70, 71,ldt3);
        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);
        assertEquals(2, apptServ.getAppointments().size());
    }

    @Test
    void isApptBookedFalse() {
        apptServ = new AppointmentService();
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = LocalDateTime.now();
        Appointment appointment1 = new Appointment(67, 69,ldt);
        Appointment appointment2 = new Appointment(68, 66,ldt2);
        apptServ.createAppointment(appointment1);
        apptServ.createAppointment(appointment2);
        assertEquals(2, apptServ.getAppointments().size());
    }

    @Test
    void getAppointmentById() {
        apptServ = new AppointmentService();
        LocalDateTime ldt = LocalDateTime.now();
        Appointment appointment1 = new Appointment(67, 69,ldt);
        apptServ.createAppointment(appointment1);
        assertTrue(apptServ.getAppointmentById(appointment1.getAppointmentId()).isPresent());
    }

    @Test
    void updateAppointment() {
        apptServ = new AppointmentService();
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = LocalDateTime.of(2026, 12, 25, 4, 20);
        Appointment appointment1 = new Appointment(67, 69,ldt);
        apptServ.createAppointment(appointment1);
        apptServ.updateAppointment(appointment1.getAppointmentId(), ldt2);
        assertTrue(appointment1.getStartDateTime().isEqual(ldt2));
    }

    @Test
    void cancelAppointment() {
        apptServ = new AppointmentService();
        LocalDateTime ldt = LocalDateTime.now();
        Appointment appointment1 = new Appointment(67, 69,ldt);
        apptServ.createAppointment(appointment1);
        apptServ.cancelAppointment(appointment1.getAppointmentId());
        assertEquals(0, apptServ.getAppointments().size());
    }
}