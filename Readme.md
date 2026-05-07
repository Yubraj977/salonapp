
# 💈 SLogics Salon App

> A Java-based salon management system for managing users, stylists, customers, and appointments.

![Java](https://img.shields.io/badge/Java-21-orange) ![Maven](https://img.shields.io/badge/Maven-3.x-blue) ![BCrypt](https://img.shields.io/badge/Security-BCrypt-green)

## 👥 Authors
| Yubraj | Yousuf | Frederick | Abdul | Alex |
|--------|--------|-----------|-------|------|

---

## ✨ Features
- 🔐 Secure BCrypt password hashing
- 👤 Role-based access — Admin, Stylist, Customer
- 📅 Appointment scheduling with no overlapping time slots
- 🗓️ Calendar integration for stylist scheduling
- ⏰ 24-hour cancellation window enforcement
- 🛠️ Admin tools for managing accounts and appointments

---

## 📋 Requirements
- Java 21
- Maven 3.x

---

## 📌 User Stories & Priorities

| Requirement | User Story | Priority |
|---|---|---|
| No overlapping time slots per stylist | As an admin, two customers shouldn't book overlapping times for one stylist | 🔴 High |
| Unique login ID and passcode storage | As an admin, no two users should share login credentials | 🔴 High |
| Calendar integration for scheduling | As a stylist, I want appointments synced to a calendar | 🔴 High |
| Stylists can view all appointments | As a stylist, I should be able to view past and future appointments | 🟡 Moderate |
| Admins can manage user accounts and appointments | As an admin, I should handle accounts and appointments when needed | 🟡 Moderate |
| Customers can manage their appointments | As a customer, I should be able to make and cancel appointments | 🟢 Low |
| 24-hour cancellation window enforced | As a customer, I want to know if I'm within the cancellation window | 🟢 Low |

---

## 🗂️ CRC Cards & UML Diagram

<img src="./images/CRC Card-Page-1.drawio.svg" alt="CRC Cards" width="100%">

---

## 📊 Use Case Diagram

<img src="./images/Lab 5 Use Case Diagrams.drawio.svg" alt="Use Case Diagram" width="100%">

---

## 🗂️ Use Cases

<details>
<summary>View all 10 use cases</summary>

### 1. Application Start
**Actor:** System  
System initializes and displays options: Login, Schedule appointment, View past appointments, Modify appointments.

### 2. Account Creation
**Actor:** Customer  
Customer signs up with name, email, username, and password. System validates uniqueness and stores the account.

### 3. Authentication (Login)
**Actors:** Customer, Stylist, Admin  
User enters credentials. System validates and grants role-based access.

### 4. Schedule Appointment
**Actor:** Customer  
Customer selects a stylist and available time slot. System books and removes the slot from availability.

### 5. Cancel Appointment
**Actor:** Customer  
Customer selects an existing appointment and cancels it. Stylist is notified.

### 6. Manage Stylist Profiles
**Actor:** Admin  
Admin can add, update, or deactivate stylist accounts from the dashboard.

### 7. Modify Appointment
**Actors:** Manager, Stylist  
Authorized user can reschedule (freeing the old slot and booking a new one) or cancel an appointment.

### 8. Store User Profiles
**Actors:** Customer, Admin  
User submits profile data. System validates and saves it to the database.

### 9. View Reward Points
**Actor:** Customer  
Customer opens dashboard. System calculates and displays their total reward points.

### 10. View Daily Schedule
**Actor:** Stylist  
Stylist opens dashboard and selects "Daily Schedule" to view the day's appointments.

</details>

---

## 📸 App Demo

<img width="1362" alt="App Demo 1" src="https://github.com/user-attachments/assets/282a813e-d6bd-4899-addc-a474bd1cb97d" />
<img width="1108" alt="App Demo 2" src="https://github.com/user-attachments/assets/b2cbd402-a208-4fd3-80dc-d47a94320bdf" />

---

## 🚀 How to Run

```bash
# Compile and run
mvn compile exec:java -Dexec.mainClass="edu.secourse.Main"

# Run tests
mvn test
```

---

## 📄 Documentation
       
- [📘 Javadoc](https://Yubraj977.github.io/salonapp/slogix_javadoc/index.html)
- [🌐 Project Site](https://Yubraj977.github.io/salonapp/)
