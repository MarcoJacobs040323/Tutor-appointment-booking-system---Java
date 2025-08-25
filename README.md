## Milestone 2: Desktop Application – Wellness Management

### Features
- CRUD operations for:
    - Appointments
    - Counselors
    - Feedback
- Multi-tabbed interface for easy navigation
- Input validation and confirmation dialogs
- MVC Architecture
- Java OOP principles (encapsulation, inheritance, polymorphism)

Exception handling and user-friendly error messages

### Modules
- AppointmentManager.java
- CounselorManager.java
- FeedbackManager.java
- DBConnection.java
- MainDashboard.java – GUI launcher

### Java Tables
```sql
CREATE TABLE appointments (
  id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  student VARCHAR,
  counselor VARCHAR,
  date DATE,
  time TIME,
  status VARCHAR
);

CREATE TABLE counselors (
  id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  name VARCHAR,
  specialization VARCHAR,
  availability VARCHAR
);

CREATE TABLE feedback (
  id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  student VARCHAR,
  rating INT,
  comments VARCHAR
);
```

## Technologies Used
- Java SE 17+
- Java Swing
- Apache Derby (JavaDB)
- JSP & Servlets
- JDBC
- Git & GitHub

## How to Run

### Desktop App (Milestone 2)
- Ensure JavaDB is configured correctly.
- Run MainDashboard.java from your IDE.
- Use the tabs to manage appointments, counselors, and feedback.

