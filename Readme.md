

  # SLogics Salon App
                                                                                                                                                                                                               
  A Java-based salon management system for managing users, stylists, customers, and appointments.
        
  # Authors                                                                                                                                                                                                      
                  
  - Yubraj
  - Yousuf
  - Frederick
  - Abdul
  - Alex
                      
  ## Features                                                                                                                                                                                                  
  - User management (Admin, Stylist, Customer)
  - Appointment scheduling and cancellation                                                                                               
  - Secure BCrypt password hashing
  - Role-based access                                                                                                                                                                                          
   
  ## Requirements                                                                                                                                                       
  - Java 21       
  - Maven 3.x

  ## High-level Requirements
  - Stylists would want to see a complete list of all appointments past and present
  - Customers must be able to manage their appointments.
  - Time slots can’t overlap for the same stylist
  - Unique login ID and pass code storage
  - Cancellation only allowed more than 24 hours from appointment
  - Calendar integration for scheduling
  - Allow admins to manage user accounts and appointments



| Requirement | User Story | Importance |
|---|---|---|
| Stylists can view all appointments (past and present) | As a stylist, he/she should be able to manage appointments to keep up on future appointments. | MODERATE |
| Customers can manage their appointments | As a customer, he/she should be able to make/cancel appointments. | LOW |
| No overlapping time slots for the same stylist | As an admin, two customers shouldn't be able to book overlapping times for one stylist to minimize confusion and maximize customer satisfaction. | HIGH |
| Unique login ID and passcode storage | As an admin, two users shouldn't be able to have the same login ID and passcode so they don't access one another's account. | HIGH |
| Cancellation only allowed more than 24 hours before appointment | As a customer, I want the app to tell me whether I am still within the 24 hour window for appointment cancellation so I may cancel if necessary. | LOW |
| Calendar integration for scheduling | As a stylist, I want my appointments to sync with a calendar so that I can keep track of my schedule more easily. | HIGH |
| Admins can manage user accounts and appointments | As the admin, he/she should be able to manage user accounts and appointments in case user loses their account info or appointment time. | MODERATE |
## CRC Cards & UML Diagram

<img src="./images/CRC Card-Page-1.drawio.svg" alt="CRC Cards" width="100%">

## Use Case Diagram

<img src="./images/Lab 5 Use Case Diagrams.drawio.svg" alt="Use Case Diagram" width="100%">
  
## Use-cases
  <details>

<summary>Our Use Cases</summary>

-------------
# Appointment Scheduling System - Use Cases

---

## 1. Application Start
**Description:** System displays available options to the user.  

**Actors:** Application  

**Preconditions:**
- Application has access to the database  

**Workflow:**
1. System initializes  
2. System displays options:
   - Login  
   - Schedule appointment  
   - View past appointments  
   - Modify appointments  

**Results:**
- Options are successfully displayed  

**Alternates:**
- If data fails to load:
  - System displays an error message explaining unavailable features  

---

## 2. Account Creation
**Description:** Customer creates a new account  

**Actors:** Customer  

**Preconditions:**
- Application is running  

**Workflow:**
1. Customer selects “Sign Up”  
2. Customer enters:
   - Name  
   - Email  
   - Username  
   - Password  
3. System validates:
   - Username uniqueness  
   - Email format  
   - Password strength  
4. Customer confirms creation  
5. Account is created  
6. Customer logs in  

**Results:**
- Account is stored in the database  

**Alternates:**
- Invalid input → show validation errors  
- Username already exists → prompt for a new one  
- User cancels → process stops  

---

## 3. Authentication (Login)
**Description:** User logs into the system  

**Actors:** Customer, Stylist, Admin  

**Preconditions:**
- Account exists  
- Application is running  

**Workflow:**
1. User enters username and password  
2. System validates credentials  
3. System grants access  

**Results:**
- User successfully logged in  

**Alternates:**
- Invalid credentials → show error message  
- Option to reset password  

---

## 4. Schedule Appointment
**Description:** Customer schedules an appointment  

**Actors:** Customer  

**Preconditions:**
- Customer is logged in  
- Available time slots exist  

**Workflow:**
1. Customer enters contact info  
2. System displays stylists  
3. Customer selects a stylist  
4. System displays available dates/times  
5. Customer selects a time slot  
6. System books the appointment  

**Results:**
- Appointment is saved  
- Time slot removed from availability  
- Notifications sent  

**Alternates:**
- No availability → suggest other stylists or times  
- User cancels  

---

## 5. Cancel Appointment
**Description:** Customer cancels an appointment  

**Actors:** Customer  

**Preconditions:**
- Appointment exists under account  

**Workflow:**
1. Customer views appointments  
2. Customer selects an appointment  
3. Clicks “Cancel”  
4. Confirms cancellation  

**Results:**
- Appointment removed  
- Stylist notified  

**Alternates:**
- User cancels confirmation → no changes  

---

## 6. Manage Stylist Profiles (Admin)
**Description:** Admin manages stylist accounts  

**Actors:** Admin  

**Preconditions:**
- Admin is logged in  

**Workflow:**
1. Admin opens dashboard  
2. Selects “Manage Stylists”  
3. Adds, updates, or deactivates a stylist  
4. Confirms action  

**Results:**
- Changes saved to database  

**Alternates:**
- Admin cancels → no changes  

---

## 7. Modify Appointment
**Description:** Authorized user reschedules or cancels an appointment  

**Actors:** Manager, Stylist  

**Preconditions:**
- Appointment exists  
- User is authorized  

**Workflow:**
1. User logs in  
2. Selects appointment  
3. Chooses:
   - Reschedule → pick new time  
   - Cancel → remove appointment  

**Results:**
- Reschedule:
  - Old slot freed  
  - New slot booked  
- Cancel:
  - Slot becomes available  

**Alternates:**
- User cancels modification  

---

## 8. Store User Profiles
**Description:** System saves user data  

**Actors:** Customer, Admin  

**Preconditions:**
- Required fields are provided  

**Workflow:**
1. User submits profile data  
2. System validates input  
3. System confirms action  
4. Data is saved  

**Results:**
- Profile stored in database  

**Alternates:**
- Invalid data → error message  
- User cancels  

---

## 9. View Reward Points
**Description:** Customer views reward balance  

**Actors:** Customer  

**Preconditions:**
- Customer is logged in  

**Workflow:**
1. Customer opens dashboard  
2. System calculates reward points  
3. System displays total  

**Results:**
- Reward points displayed  

**Alternates:**
- Data retrieval error  

---

## 10. View Daily Schedule (Stylist)
**Description:** Stylist views appointments for the day  

**Actors:** Stylist  

**Preconditions:**
- Stylist is logged in  

**Workflow:**
1. Open dashboard  
2. Select “Daily Schedule”  
3. System displays appointments  

**Results:**
- Stylist can view schedule  

**Alternates:**
- No appointments → display message  


----------

</details>



  ## Detailed Requirements
  <img width="975" height="931" alt="image" src="https://github.com/user-attachments/assets/6ce2a20e-4ff5-4de9-a5d5-87f878e20c33" />

  <img width="975" height="652" alt="image" src="https://github.com/user-attachments/assets/763b5d30-0fa4-4f02-bc74-891696e7f416" />




  ## How to Run
  ```bash
  mvn compile exec:java -Dexec.mainClass="edu.secourse.Main"
                                                                                                                                                                                                               
  How to Run Tests
                                                                                                                                                                                                               
  mvn test        
                                                                                                                                                                                                               
  Javadoc
                                                                                                                                                                                                               

---
```
  https://Yubraj977.github.io/salonapp/                                                                                                                                                                        
 


# 

































# Our App Demo

<img width="1362" height="369" alt="Screenshot 2026-04-30 at 3 45 47 PM" src="https://github.com/user-attachments/assets/282a813e-d6bd-4899-addc-a474bd1cb97d" />
<img width="1108" height="900" alt="Screenshot 2026-04-30 at 3 47 27 PM" src="https://github.com/user-attachments/assets/b2cbd402-a208-4fd3-80dc-d47a94320bdf" />

             
                                                      
