🎓 Faculty Management System (FMS)
ICT2132 – Object Oriented Programming Practicum | Mini Project
University of Ruhuna | Faculty of Technology | Level II Semester I

---

📌 Project Overview

The Faculty Management System (FMS)is a Java-based desktop application built for the Faculty of Technology, University of Ruhuna. It manages user profiles, courses, marks, attendance, medical records, notices, and timetables for four user roles: **Admin, Lecturer, Technical Officer, and Undergraduate**.

---
🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java (Swing) | GUI Desktop Application |
| MySQL 8.x | Relational Database |
| JDBC (mysql-connector-j 9.6.0) | Database Connectivity |
| FlatLaf 3.4.1 | Modern UI Theme |
| IntelliJ IDEA | IDE |

---

👥 User Roles & Capabilities

🔐 Admin
- Create and manage all user profiles (Admin, Lecturer, Technical Officer, Student)
- Create and manage courses
- Create and manage notices
- Create and manage timetables

👨‍🏫 Lecturer
- Update own profile (except username/password)
- Add/modify course materials
- Upload marks (CA1, CA2, Assignment, Final Exam)
- View student details, eligibility, marks, grades, GPA
- View student attendance and medical records
- View notices

🔧 Technical Officer
- Update own profile (except username/password)
- Add and maintain undergraduate attendance records
- Add and maintain undergraduate medical records
- View notices and department timetables

🎓 Undergraduate
- Update contact details and profile picture
- View own attendance, medical details, course details
- View grades, GPA (SGPA & CGPA)
- View timetable and notices

---

📁 Project Structure

```
FMSNEW/
├── src/
│   ├── Main.java                          # Application entry point
│   ├── dao/                               # Data Access Layer (DB operations)
│   │   ├── DBConnection.java              # MySQL JDBC connection
│   │   ├── UserDAO.java                   # User CRUD operations
│   │   ├── AttendanceDAO.java             # Attendance & medical queries
│   │   ├── MarksDAO.java                  # Marks, grades, GPA calculations
│   │   ├── CourseDAO.java                 # Course management
│   │   └── NoticeDAO.java                 # Notice management
│   ├── model/                             # OOP Model Layer
│   │   ├── User.java                      # Abstract parent class
│   │   ├── Admin.java                     # Admin subclass
│   │   ├── Lecturer.java                  # Lecturer subclass
│   │   ├── Student.java                   # Student subclass
│   │   ├── TechOfficer.java               # Technical Officer subclass
│   │   ├── Course.java                    # Course model
│   │   ├── Attendance.java                # Attendance model
│   │   ├── Mark.java                      # Mark model
│   │   ├── Medical.java                   # Medical record model
│   │   └── Notice.java                    # Notice model
│   └── ui/
│       ├── MainFrame.java                 # Main application window & navigation
│       ├── theme/
│       │   └── AppTheme.java              # Custom color theme
│       └── panels/
│           ├── LoginPanel.java            # Login screen
│           ├── BasePanel.java             # Abstract base panel
│           ├── AdminDashboardPanel.java   # Admin home dashboard
│           ├── UsersPanel.java            # User management (Admin)
│           ├── CoursesPanel.java          # Course management
│           ├── NoticesPanel.java          # Notices view/manage
│           ├── TimetablePanel.java        # Timetable view
│           ├── AttendancePanel.java       # Attendance management & summary
│           ├── MarksPanel.java            # Marks upload, grades, GPA
│           ├── MedicalsPanel.java         # Medical records management
│           ├── ProfilePanel.java          # Profile update panel
│           ├── LecturerDashboardPanel.java
│           ├── StudentDashboardPanel.java
│           └── TechOfficerDashboardPanel.java
├── lib/
│   ├── flatlaf-3.4.1.jar                  # UI theme library
│   └── mysql-connector-j-9.6.0.jar        # MySQL JDBC driver
├── schema.sql                             # Database schema (DDL)
├── data.sql                               # Sample data (DML)
└── README.md
```

---

⚙️ Setup & Installation

Prerequisites
- Java JDK 17 or higher
- MySQL Server 8.x
- IntelliJ IDEA (recommended) or any Java IDE

Step 1 — Clone the Repository
```bash
git clone https://github.com/<your-group>/FMSNEW.git
cd FMSNEW
```

Step 2 — Set Up the Database
Open MySQL Workbench or MySQL CLI and run:
```sql
SOURCE schema.sql;
SOURCE data.sql;
```
Step 3 — Configure Database Connection
Set environment variables **or** edit `src/dao/DBConnection.java`:

| Variable | Default |
|---|---|
| `FMS_DB_URL` | `jdbc:mysql://localhost:3306/faculty_system` |
| `FMS_DB_USER` | `root` |
| `FMS_DB_PASS` | *(empty)* |

Step 4 — Build the Project

Windows (PowerShell):
```powershell
javac -cp "src;lib/*" -d out $(Get-ChildItem -Recurse -Filter *.java src | ForEach-Object {$_.FullName})
```

Linux / macOS:
```bash
find src -name "*.java" > sources.txt
javac -cp "src:lib/*" -d out @sources.txt
```

Step 5 — Run the Application
```bash
# Windows
java -cp "out;lib/*" ui.Main

# Linux / macOS
java -cp "out:lib/*" ui.Main
```

---

🔑 Test Login Credentials

| Role | Username | Password |
|---|---|---|
| Admin | `admin01` | `admin123` |
| Lecturer | `lec01` | `lec123` |
| Technical Officer | `tech01` | `tech123` |
| Student | `stu001` | `stu123` |

---

🗄️ Database Schema Overview

| Table | Description |
|---|---|
| `users` | All user accounts (admin, lecturer, student, tech_officer) |
| `courses` | Course details with theory/practical credits |
| `student_courses` | Many-to-many enrollment mapping |
| `attendance` | Per-session attendance (theory & practical separately) |
| `medicals` | Medical certificate submissions with approval status |
| `marks` | CA1, CA2, Assignment, and Final Exam marks |
| `notices` | Announcements created by admin |
| `timetables` | Course schedule (day, time, room) |

---

📊 Sample Data Summary

- ✅ 1 Admin
- ✅ 5 Lecturers
- ✅ 4 Technical Officers
- ✅ 20 Students (including 2 repeat students, 2 batch-missed students)
- ✅ 5 Courses with theory and/or practical sessions (15 sessions each)
- ✅ Attendance scenarios covered:
  - Students with >80% attendance
  - Students with exactly 80% attendance
  - Students with <80% attendance (no medicals)
  - Students with <80% attendance (with approved medicals)
  - Students with >80% attendance (with medicals)
- ✅ CA marks and final exam marks for all students
- ✅ Grades calculated per UGC Commission Circular No. 12-2024

---

📐 OOP Concepts Demonstrated

| Concept | Where Used |
|---|---|
| **Classes & Objects** | All model classes (`User`, `Course`, `Mark`, etc.) |
| **Inheritance** | `Admin`, `Lecturer`, `Student`, `TechOfficer` extend `User` |
| **Abstraction** | `User` is abstract with `getRole()` abstract method; `BasePanel` is abstract |
| **Polymorphism** | `buildUser()` in `UserDAO` creates correct subclass; panels behave by role |
| **Encapsulation** | All model fields are `private` with getters/setters |
| **Exception Handling** | `try/catch` blocks in all DAO classes and UI event handlers |
| **Database Handling** | Full CRUD via JDBC in DAO layer (`UserDAO`, `MarksDAO`, etc.) |
| **GUI** | Java Swing with FlatLaf theme across all panels |

---

🎓 Grading System

Grades are assigned per **UGC Commission Circular No. 12-2024**:

| Grade | Marks Range | Grade Points |
|---|---|---|
| A+ | 85 – 100 | 4.0 |
| A  | 75 – 84  | 4.0 |
| A- | 70 – 74  | 3.7 |
| B+ | 65 – 69  | 3.3 |
| B  | 60 – 64  | 3.0 |
| B- | 55 – 59  | 2.7 |
| C+ | 50 – 54  | 2.3 |
| C  | 45 – 49  | 2.0 |
| C- | 40 – 44  | 1.7 |
| D+ | 35 – 39  | 1.3 |
| D  | 30 – 34  | 1.0 |
| E  | 0 – 29   | 0.0 |

**Eligibility Rule:** CA average (CA1 + CA2 + Assignment) must be **≥ 40%** to sit for the Final Exam.

---

👨‍💻 Group Members & Contributions

Member 1 — [Name] | Student ID: [ID]
**Focus: Core Models, Database Layer, User Management**
- Designed and implemented `User.java` (abstract base class with OOP principles)
- Implemented `Admin.java`, `Lecturer.java`, `Student.java`, `TechOfficer.java` subclasses
- Implemented `UserDAO.java` (login, CRUD, polymorphic buildUser)
- Implemented `DBConnection.java`
- Designed `schema.sql` database structure
- Implemented `AdminDashboardPanel.java`, `UsersPanel.java`
- Admin role functionality: create/manage user profiles

---

Member 2 — [Name] | Student ID: [ID]
**Focus: Attendance System & Medical Records**
- Implemented `Attendance.java`, `Medical.java` model classes
- Implemented `AttendanceDAO.java` (attendance queries, batch summary, individual summary, theory/practical filtering)
- Implemented `AttendancePanel.java` (UI for viewing and managing attendance)
- Implemented `MedicalsPanel.java` (UI for medical submissions and approval)
- Populated `data.sql` attendance scenarios (>80%, =80%, <80% with/without medicals)
- Technical Officer attendance and medical management features

---

Member 3 — [Name] | Student ID: [ID]
**Focus: Marks, Grades & GPA System**
- Implemented `Mark.java` model class
- Implemented `MarksDAO.java` (save marks, CA average, eligibility check, grade calculation, SGPA/CGPA)
- Implemented `MarksPanel.java` (UI for mark entry, eligibility view, grade/GPA display)
- Implemented grading logic per UGC Commission Circular No. 12-2024
- Populated `data.sql` marks data for all students
- Batch and individual marks/grades/GPA summary views

---

Member 4 — [Name] | Student ID: [ID]
**Focus: UI Theme, Courses, Notices, Timetable & Student Dashboard**
- Implemented `AppTheme.java` (custom FlatLaf color theme)
- Implemented `Course.java`, `Notice.java` model classes
- Implemented `CourseDAO.java`, `NoticeDAO.java`
- Implemented `LoginPanel.java` (multi-role login screen)
- Implemented `MainFrame.java` (main window, role-based navigation)
- Implemented `CoursesPanel.java`, `NoticesPanel.java`, `TimetablePanel.java`
- Implemented `ProfilePanel.java`
- Implemented `StudentDashboardPanel.java`, `LecturerDashboardPanel.java`, `TechOfficerDashboardPanel.java`
- `BasePanel.java` abstract panel design

---

📝 License

This project was developed as an academic assignment for ICT2132 – Object Oriented Programming Practicum, University of Ruhuna.
ICT2132 – Object Oriented Programming Practicum
Mini Project – Faculty Management System

University of Ruhuna | Faculty of Technology | Department of ICT
Level II Semester I – Group B08

---


Project Description

A desktop application built with **Java (Swing GUI)** and **MySQL** for the Faculty of Technology to manage:

- User profiles — Admin, Lecturer, Student, Technical Officer
- Course details and materials
- Undergraduate marks and GPA (graded by UGC Circular No. 12-2024)
- Undergraduate attendance (theory, practical, combined) with medical support
- Notices and timetables

---

OOP Concepts Demonstrated

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

Project Structure

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

How to Run

Requirements
- Java JDK 11 or higher
- MySQL 8.0 or higher
- IntelliJ IDEA (recommended)
- MySQL Connector/J JAR — [download here](https://dev.mysql.com/downloads/connector/j/)

Step 1 — Set up the database

Open MySQL Workbench (or any MySQL client) and run the two files in order:

```sql
-- Run first
source database/schema.sql

-- Run second
source database/data.sql
```

Step 2 — Add MySQL driver to IntelliJ

1. Download `mysql-connector-j-x.x.x.jar`
2. In IntelliJ: **File → Project Structure → Libraries → + → Java**
3. Select the downloaded `.jar` file → OK

Step 3 — Set your MySQL password

Open `src/dao/DBConnection.java` and update line:

```java
private static final String DB_PASS = "";  // ← put your MySQL password here
```

Step 4 — Run the project

Right-click `src/Main.java` → **Run 'Main'**

The login window will appear.

---

Test Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin01` | `admin123` |
| Lecturer | `lec01` | `lec123` |
| Student | `stu001` | `stu123` |
| Technical Officer | `tech01` | `tech123` |

---

Features by Role

Admin
- Create and manage all user profiles (add, view, delete)
- Create and manage courses
- Post and delete notices
- Manage timetable entries

Lecturer
- View assigned courses
- Upload marks (CA1, CA2, Assignment, Final) for students
- View marks summary and grades for whole batch
- Check student eligibility (attendance + CA marks)
- View attendance records and medical details
- View notices

Student
- View enrolled courses and timetable
- View attendance (theory only / practical only / combined)
- Submit medical certificates for absences
- View marks, grades, and SGPA
- Update contact details and profile picture
- View notices

Technical Officer
- Record attendance for each session (theory/practical)
- Add and manage medical records
- Approve or reject medical certificates
- View notices and department timetable

---

Grading System

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

Eligibility rule:** CA average must be ≥ 40% to sit the final exam.
Attendance rule:** Must have ≥ 80% attendance (approved medicals count as present).

---

Database Tables

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
Individual Contributions

Member 1 – [Full Name]
- `database/schema.sql`, `database/data.sql`
- `model/User.java` (abstract parent class)
- `model/Admin.java`, `Lecturer.java`, `Student.java`, `TechOfficer.java`
- `dao/DBConnection.java`, `dao/UserDAO.java`
- `ui/LoginFrame.java`, `ui/AdminDashboard.java`
- `src/Main.java`

Member 2 – [Full Name]
- `model/Course.java`, `model/Mark.java`
- `dao/CourseDAO.java`, `dao/MarksDAO.java`
- `ui/LecturerDashboard.java`
- Implemented UGC grading and SGPA calculation

Member 3 – [Full Name]
- `model/Attendance.java`, `model/Medical.java`, `model/Notice.java`
- `dao/AttendanceDAO.java`
- `ui/StudentDashboard.java`
- Implemented attendance percentage and medical logic
Member 4 – [Full Name]
- `dao/NoticeDAO.java`
- `ui/TechOfficerDashboard.java`, `ui/SharedPanels.java`
- `README.md`, `.gitignore`
- Implemented timetable and medical approval system

---

Submission Details

- Course: ICT2132 – Object Oriented Programming Practicum
- Deadline: Sunday 26th April 2026, 11:59 PM
- Submitted to: LMS under Mini Project section

---

University of Ruhuna | Faculty of Technology | 2026
