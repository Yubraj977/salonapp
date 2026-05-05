

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

!! Make into table: 
Requirement	User Story	Importance
•	Stylists would want to see a complete list of all appointments past and present	As a stylist, he/she should be able to manage appointments to keep up on future appointments.	MODERATE
•	Customers must be able to manage their appointments.	As a customer, he/she should be able to make/cancel appointments.	 LOW
•	Time slots can’t overlap for the same stylist	As an admin, two customers shouldn’t be able to book overlapping times for one stylist to minimize confusion and maximize customer satisfaction.	HIGH
•	Unique login ID and pass code storage	As an admin, two users shouldn’t be able to have the same login ID and passcode so they don’t access one another’s account.	HIGH
•	Cancellation only allowed more than 24 hours from appointment	As a customer, I want the app to tell me whether I am still within the 24 hour window for appointment cancellation so I may cancel if necessary.	LOW
•	Calendar integration for scheduling	As a stylist, I want my appointments to sync with a calendar so that I can keep track of my schedule more easily.	HIGH
•	Allow admins to manage user accounts and appointments	As the admin, he/she should be able to manage user accounts and appointments in case user loses their account info or appointment time.	MODERATE

  ## Use-cases !!Create dropd down toggle
-------------
Use Cases*
1.	Application Start:
1.	Description: System displays options for users:
2.	Work-flow:
1.	Application provides login option.
2.	Application provides appointment scheduling option.
3.	Application provides option for customers to see their past appointments.
4.	Application provides option for customers to make appointments.
5.	Application provides option for customers to update appointments.
3.	Pre-conditions:
1.	Application gains access to the database.
4.	Results (Application successfully displays options):
5.	Alternates (Application fails to display):
1.	Application encounters an issue (In loading data from database or other sources).
2.	Application displays the appropriate error or warning message with the reason for not display specific option(s).
6.	Actors: Application
2.	Account Creation:
1.	Description: Customer creates an account.
2.	Work-flow:
1.	Customer opens application
2.	Customer clicks on “sign up”
3.	Customer enters details like “name”, “email”, etc
4.	Customer creates a username and password
1.	If username/password is taken already, Application displays “Username already taken” for usernames and “Password already taken” for passwords
5.	Customer confirms account creation
6.	Customer logs into the application
3.	Pre-Conditions:
1.	application starts successfully.
4.	Results: (Successful creation of account)
1.	Customer account is successfully created with unique username/password
2.	Customer creates password according to application’s recommendations
5.	Alternatives:
1.	Customer cancels account creation.
2.	Customer enters unavailable/invalid username, email, or password
6.	Entities: customer
3.	Authentication:
1.	Description: User authenticates via login
2.	Work-flow:
1.	Application displays login
2.	user enters username and password
3.	User clicks login button
4.	Admin authenticates login
3.	Pre-Conditions:
1.	Application has successfully started up
2.	Customer has already created an account
4.	Results: (Successful login):
1.	User enters correct username
2.	User enters correct password and clicks login button
3.	Application displays: “User logs in successfully”
5.	Alternates (Unsuccessful Login):
1.	User enters wrong username and/or password
2.	Application displays: “ wrong username/password”
3.	Application displays option to reset account username and/or password
6.	Entities: admin, stylist, customer
4.	Schedule Appointment:
1.	Description: Customer schedules a time and date for their hair appointment
2.	Pre-conditions:
1.	Date and Time slots are available to be scheduled
2.	Customer inputs contact info to pair to their appointment slot
3.	Work-flow:
1.	Customer inputs name and email/phone number for contact
2.	App brings up list of stylists to select from
3.	Upon stylist selection, open calendar and corresponding time slots
4.	Display available days/times in blue, unavailable days/times in grey
1.	Available:
1.	Schedule customer for that date and time
2.	Remove said date and time from stylist availability
2.	Unavailable:
1.	Display no appointments available for this stylist
2.	Give option to choose another stylist or check back in a few days
4.	Results (If scheduling was completed):
1.	A customer was assigned to a date/time slot and that slot was removed from the stylists availability
2.	Confirmation messages sent to both customer and stylist
5.	Alternates:
1.	Customer doesn’t schedule
6.	Entities involved:
1.	Customer, Scheduling system
5.	Use Case - Cancel Appointment (Customer)
a. Description: Customer wants to cancel their appointment
b. Work-flow:
-Customer logs into their account
-Customer sees their booked appointment
-Customer clicks the cancel button
-Application displays “Confirm Cancel” and “No, I want to keep my appointment” buttons
-If they click “Confirm Cancel”, remove the booked block from the database and from the ui from both consumer and stylist
c. Pre-Conditions:
-The appointment is booked under this account
d. Results:
-User cancel the appointment
-Appointment is removed from system
-Stylist gets notified of cancellation
e. Alternatives:
-They select “No, I want to keep my appointment”, in which the appointment remains and the user is returned to the dashboard
f. Entities involved:
-Customer, Application
6.	Use Case - Manage Stylist Profiles (Admin)
a. Description: The Admin adds, edits, or deactivates stylist profiles
b. Pre-conditions:
i. The user is an administrator
ii. The administrator is logged in
c. Workflow:
i. The Admin opens the Admin dashboard and selects “Manage Stylists”.
ii. The system displays the lists of stylists
iii.Admin selects a stylist or clicks “Add Stylist”
iv. The Admin adds or updates stylist info or deactivates the stylist
v. System displays a confirmation prompt
vi: admin confirms
Results:
1. A stylist profile has been, added, updated, or deactivated in the database
Alternates: Admin cancels at the confirmation prompt and no changes are saved.
7.	Use Case - Update/Modify Appointment
1.	Description: Manager/Corresponding Stylist modifying an appointment to another time/date
2.	Pre-Conditions:
1.	There is a customer/stylist request to change any given appointment
2.	Employee/Manager is logged into their designated Authorized account
3.	If worker: Worker has customer and/or manager approval for schedule changes. If manager: Manager has customer and/or worker approval for schedule changes
3.	Workflow:
1.	User authenticates into their specific authorized account
2.	User then selects specific, previously scheduled appointment
3.	Choose the ‘Modify Appointment’ option
4.	Then choose between canceling or rescheduling
1.	If Reschdule:
1.	Bring up current availability based on the present schedule
2.	Select an available date
3.	Update schedule
4.	Results:
1.	If Reschedule:
1.	Previous time slot is added back to the stylists availability
2.	New time slot is reserved and removed from the list of available time slots
3.	Confirmation emails sent to both the stylist and the customer
5.	Alternates:
1.	If Cancel:
1.	Appointment is removed from schedule
2.	That time slot is then added back to the stylists availability for another customer to schedule
3.	Confirmation emails sent to both the stylist and the customer, informing about the cancellation
6.	Entities involved:
1.	Manager, Stylist, Customer
8.	Use case - Store User Profiles in Database
1.	Description: Application stores user profiles in an appropriate database
2.	Preconditions:
1.	Admin creates user accounts for stylists.
2.	Customer Creates their own account
3.	required profile fields exist in the system
1. A new account is submitted either through admin creation or customer sign up.
2.The application validates required profile fields
3. The application displays a confirmation prompt
4. User confirms
5. The application saves the new user profile to the database
6. The application displays a success message
Results:
1. A user profile is added or updated in the database
2. The profile becomes available for role-based features such as scheduling or customer reward points
Alternates:
1. Invalid or missing fields, duplicate username, or database error
2. User cancels at confirmation prompt: no changes are saved
Entities involved: Admin, Stylist, Customer
9.	Use Case - View Reward Points (Customer)
1.	Description: Customer can check their current amount of Reward Points
2.	Preconditions:
1.	Customer opens application and logs in
3.	Workflow
1.	Customer opens application
2.	Customer logs into their account
3.	Application checks accounts number of appointments associated with user account
4.	Application displays value as user’s “Reward Points”
4.	Results:
1.	Application displays Customer’s current Reward Points
5.	Alternatives:
1.	Application is unable to find Customer’s current “Reward Points” value, returns error
6.	Entities Involved:
1.	Application, Customer
10.	Use Case - View Daily Schedule (Stylist)
1.	Description: Stylist views appointments for the day
2.	Entities Involved: Stylist, Application
3.	Pre-conditions: Logged in as a stylist
4.	Workflow:
1.	open dashboard
2.	select “Daily Schedule”
3.	System displays current date and stylists appointments for that day
4.	stylist can click on an appointment to see full details (contact info/notes)
if no appointments system displays “No appointments scheduled for today”
5.	Results:
1.	Stylist is able to check their current and future appointments

----------


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

             
                                                      
