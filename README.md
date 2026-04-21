# ICT2132 – Object Oriented Programming Practicum
## Mini Project – Faculty Management System

**University of Ruhuna | Faculty of Technology | Department of ICT**
**Level II Semester I – Group B08**

---


## Project Description

A desktop application built with **Java (Swing GUI)** and **MySQL** for the Faculty of Technology to manage:

- User profiles — Admin, Lecturer, Student, Technical Officer
- Course details and materials
- Undergraduate marks and GPA (graded by UGC Circular No. 12-2024)
- Undergraduate attendance (theory, practical, combined) with medical support
- Notices and timetables

---

## OOP Concepts Demonstrated

| Concept | Where Used |
|---------|-----------|
| **Abstraction** | `User.java` — abstract class with abstract `getRole()` method |
| **Encapsulation** | All model classes — `private` fields with public getters/setters |
| **Inheritance** | `Admin`, `Lecturer`, `Student`, `TechOfficer` all extend `User` |
| **Polymorphism** | `UserDAO.buildUser()` returns different subclass based on role |
| **Error Handling** | Every DAO method wraps SQL in `try-catch(SQLException)` |
| **Database Handling** | `DBConnection` + 5 DAO classes for all CRUD operations |
| **GUI** | All dashboards use Java Swing — `JFrame`, `JTabbedPane`, `JTable` |

---

## Project Structure

```
ICT2132-FacultySystem-B08/
│
├── database/
│   ├── schema.sql          ← Run this FIRST — creates all tables
│   └── data.sql            ← Run this SECOND — inserts sample data
│
├── src/
│   ├── Main.java           ← Entry point — run this to start the app
│   │
│   ├── model/              ← OOP model classes
│   │   ├── User.java           (abstract parent)
│   │   ├── Admin.java          (extends User)
│   │   ├── Lecturer.java       (extends User)
│   │   ├── Student.java        (extends User)
│   │   ├── TechOfficer.java    (extends User)
│   │   ├── Course.java
│   │   ├── Attendance.java
│   │   ├── Mark.java
│   │   ├── Notice.java
│   │   └── Medical.java
│   │
│   ├── dao/                ← Database access layer
│   │   ├── DBConnection.java   (MySQL connection)
│   │   ├── UserDAO.java
│   │   ├── CourseDAO.java
│   │   ├── AttendanceDAO.java
│   │   ├── MarksDAO.java
│   │   └── NoticeDAO.java
│   │
│   └── ui/                 ← GUI screens (Java Swing)
│       ├── LoginFrame.java
│       ├── AdminDashboard.java
│       ├── LecturerDashboard.java
│       ├── StudentDashboard.java
│       ├── TechOfficerDashboard.java
│       └── SharedPanels.java
│
├── .gitignore
└── README.md
```

---

## How to Run

### Requirements
- Java JDK 11 or higher
- MySQL 8.0 or higher
- IntelliJ IDEA (recommended)
- MySQL Connector/J JAR — [download here](https://dev.mysql.com/downloads/connector/j/)

### Step 1 — Set up the database

Open MySQL Workbench (or any MySQL client) and run the two files in order:

```sql
-- Run first
source database/schema.sql

-- Run second
source database/data.sql
```

### Step 2 — Add MySQL driver to IntelliJ

1. Download `mysql-connector-j-x.x.x.jar`
2. In IntelliJ: **File → Project Structure → Libraries → + → Java**
3. Select the downloaded `.jar` file → OK

### Step 3 — Set your MySQL password

Open `src/dao/DBConnection.java` and update line:

```java
private static final String DB_PASS = "";  // ← put your MySQL password here
```

### Step 4 — Run the project

Right-click `src/Main.java` → **Run 'Main'**

The login window will appear.

---

## Test Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin01` | `admin123` |
| Lecturer | `lec01` | `lec123` |
| Student | `stu001` | `stu123` |
| Technical Officer | `tech01` | `tech123` |

---

## Features by Role

### Admin
- Create and manage all user profiles (add, view, delete)
- Create and manage courses
- Post and delete notices
- Manage timetable entries

### Lecturer
- View assigned courses
- Upload marks (CA1, CA2, Assignment, Final) for students
- View marks summary and grades for whole batch
- Check student eligibility (attendance + CA marks)
- View attendance records and medical details
- View notices

### Student
- View enrolled courses and timetable
- View attendance (theory only / practical only / combined)
- Submit medical certificates for absences
- View marks, grades, and SGPA
- Update contact details and profile picture
- View notices

### Technical Officer
- Record attendance for each session (theory/practical)
- Add and manage medical records
- Approve or reject medical certificates
- View notices and department timetable

---

## Grading System

Based on **UGC Commission Circular No. 12-2024**:

| Grade | Marks Range | Grade Point |
|-------|-------------|-------------|
| A+    | 85 – 100    | 4.0 |
| A     | 75 – 84     | 4.0 |
| A-    | 70 – 74     | 3.7 |
| B+    | 65 – 69     | 3.3 |
| B     | 60 – 64     | 3.0 |
| B-    | 55 – 59     | 2.7 |
| C+    | 50 – 54     | 2.3 |
| C     | 45 – 49     | 2.0 |
| C-    | 40 – 44     | 1.7 |
| D+    | 35 – 39     | 1.3 |
| D     | 30 – 34     | 1.0 |
| E     | 0 – 29      | 0.0 (Fail) |

**Eligibility rule:** CA average must be ≥ 40% to sit the final exam.
**Attendance rule:** Must have ≥ 80% attendance (approved medicals count as present).

---

## Database Tables

| Table | Purpose |
|-------|---------|
| `users` | All 4 user types with role column |
| `courses` | Course details and lecturer assignment |
| `student_courses` | Many-to-many: student enrolments |
| `attendance` | Per-session attendance records |
| `medicals` | Medical certificates with approval status |
| `marks` | CA and final exam marks |
| `notices` | System-wide notices |
| `timetables` | Weekly schedule per department |
| `course_materials` | Study materials uploaded by lecturers |

---

## Individual Contributions

### Member 1 – [Full Name]
- `database/schema.sql`, `database/data.sql`
- `model/User.java` (abstract parent class)
- `model/Admin.java`, `Lecturer.java`, `Student.java`, `TechOfficer.java`
- `dao/DBConnection.java`, `dao/UserDAO.java`
- `ui/LoginFrame.java`, `ui/AdminDashboard.java`
- `src/Main.java`

### Member 2 – [Full Name]
- `model/Course.java`, `model/Mark.java`
- `dao/CourseDAO.java`, `dao/MarksDAO.java`
- `ui/LecturerDashboard.java`
- Implemented UGC grading and SGPA calculation

### Member 3 – [Full Name]
- `model/Attendance.java`, `model/Medical.java`, `model/Notice.java`
- `dao/AttendanceDAO.java`
- `ui/StudentDashboard.java`
- Implemented attendance percentage and medical logic

### Member 4 – [Full Name]
- `dao/NoticeDAO.java`
- `ui/TechOfficerDashboard.java`, `ui/SharedPanels.java`
- `README.md`, `.gitignore`
- Implemented timetable and medical approval system

---

## Submission Details

- **Course:** ICT2132 – Object Oriented Programming Practicum
- **Deadline:** Sunday 26th April 2026, 11:59 PM
- **Submitted to:** LMS under Mini Project section

---

*University of Ruhuna | Faculty of Technology | 2026*
