package edu.secourse.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This represents an appointment between the stylist and the customer.
 *
 * <p>This class includes:</p>
 * <ul>
 *     <li>Unique appointment generation </li>
 *     <li>Creation of a customer/stylist association</li>
 *     <li>Date/Time based scheduling</li>
 *     <li>Appointment status monitoring</li>
 * </ul>
 */

public class Appointment {
    /**Counter for unique appointment ID's */
    private static final AtomicInteger counter = new AtomicInteger(0);

    /**Appointment identifier */
    private int appointmentId;

    /**Customer Account ID */
    private int stylistId;

    /**Appointment status */
    private Status status;

    /**Appointment status enum */
    public enum Status {
        ACTIVE,
        CANCELED
    }

    /**New Appointment constructor */
    public Appointment(int customerId, int stylistId, LocalDateTime startDateTime) {
        this.appointmentId = counter.incrementAndGet();
        this.customerId = customerId;
        this.stylistId = stylistId;
        this.startDateTime = startDateTime;
        this.status = Status.ACTIVE;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getStylistId() {
        return stylistId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        if (startDateTime == null) {
            throw new IllegalArgumentException("Date/Time can not be null");
        }
        this.getStartDateTime = startDateTime;
    }

    /**Appointment Cancel */
    public void cancel() {
        this.status = Status.CANCELED;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Appointment that)) return false;
        return appointmentId == that.appointmentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }

    @Override
    public String toString() {
        reutnr "Appointment{" +
            "appointmentId =" + appointmentId +
            ", customerId=" + customerId +
            ", stylistId=" + stylistId +
            ", startDateTime=" + startDateTime +
            ", status=" + status +
            '}';
    }
}