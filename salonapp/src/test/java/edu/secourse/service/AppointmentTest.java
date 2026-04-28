package edu.secourse.service;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {
    @Test
    void testConstructorAndGetters() {
        LocalDateTime time = LocalDateTime.now();

        Appointment appt = new Appointment (10, 20, time);

        assertAll(
                () -> assertNotNull(appt),
                () -> assertEquals(10, appt.getCustomerId()),
                () -> assertEquals(20, appt.getStylistId()),
                () -> assertEquals(time, appt.getStartDateTime()),
                () -> assertEquals(Appointment.Status.ACTIVE, appt.getStatus())
        );
    }

    @Test
    void testUniqueIds() {
        Appointment a1 = new Appointment(1, 2, LocalDateTime.now());
        Appointment a2 = new Appointment(1, 2, LocalDateTime.now());

        assertNotEquals(a1.getAppointmentId(), a2.getAppointmentId());
    }

    @Test
    void testCancel() {
        Appointment appt = new Appointment(1, 2, LocalDateTime.now());

        appt.cancel();

        assertEquals(Appointment.Status.CANCELED, appt.getStatus());
    }

    @Test
    void testSetStartDateTimeValid() {
        Appointment appt = new Appointment(1, 2, LocalDateTime.now());
        LocalDateTime newTime = LocalDateTime.now().plusDays(2);

        appt.setStartDateTime(newTime);

        assertEquals(newTime, appt.getStartDateTime());
    }

    @Test
    void testSetStartDateTimeNullThrows() {
        Appointment appt = new Appointment(1, 2, LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> appt.setStartDateTime(null));
    }

    @Test
    void testEqualsSameObject() {
        Appointment appt = new Appointment(1, 2, LocalDateTime.now());

        assertEquals(appt, appt);
    }

    @Test
    void testEqualsDifferentObjects() {
        Appointment a1 = new Appointment(1, 2, LocalDateTime.now());
        Appointment a2 = new Appointment(1, 2, LocalDateTime.now());

        assertNotEquals(a1, a2);
    }

    @Test
    void testEqualsNull() {
        Appointment appt = new Appointment(1, 2 LocalDateTime.now());

        assertNotEquals(appt, null);
    }

    @Test
    void testEqualsDifferentType() {
        Appointment appt = new Appointment(1, 2 LocalDateTime.now());

        assertNotEquals(appt, "not an appointment");
    }

    @Test
    void testHashCodeConsistency() {
        Appointment appt = new Appointment(1,2 LocalDateTime.now());

        int hash1 = appt.hashCode();
        int hash2 = appt.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    void testToStringNotNullAndContainsFields() {
        Appointment appt = new Appointment(1, 2, LocalDateTime.now());

        String str = appt.toString();

        assertNotNull(str);
        assertTrue(str.contains("appointmentId"));
        assertTrue(str.contains("customerId"));
        assertTrue(str.contains("stylistId"));
        assertTrue(str.contains("status"));
    }
}