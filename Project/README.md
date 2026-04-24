# 🎓 Faculty Management System (FMS)
### ICT2132 – Object Oriented Programming Practicum | Mini Project
**University of Ruhuna | Faculty of Technology | Level II Semester I**

---

## 📌 Project Overview

The **Faculty Management System (FMS)** is a Java-based desktop application built for the Faculty of Technology, University of Ruhuna. It manages user profiles, courses, marks, attendance, medical records, notices, and timetables for four user roles: **Admin, Lecturer, Technical Officer, and Undergraduate**.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java (Swing) | GUI Desktop Application |
| MySQL 8.x | Relational Database |
| JDBC (mysql-connector-j 9.6.0) | Database Connectivity |
| FlatLaf 3.4.1 | Modern UI Theme |
| IntelliJ IDEA | IDE |

---

## 👥 User Roles & Capabilities

### 🔐 Admin
- Create and manage all user profiles (Admin, Lecturer, Technical Officer, Student)
- Create and manage courses
- Create and manage notices
- Create and manage timetables

### 👨‍🏫 Lecturer
- Update own profile (except username/password)
- Add/modify course materials
- Upload marks (CA1, CA2, Assignment, Final Exam)
- View student details, eligibility, marks, grades, GPA
- View student attendance and medical records
- View notices

### 🔧 Technical Officer
- Update own profile (except username/password)
- Add and maintain undergraduate attendance records
- Add and maintain undergraduate medical records
- View notices and department timetables

### 🎓 Undergraduate
- Update contact details and profile picture
- View own attendance, medical details, course details
- View grades, GPA (SGPA & CGPA)
- View timetable and notices

---

## 📁 Project Structure

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

## ⚙️ Setup & Installation

### Prerequisites
- Java JDK 17 or higher
- MySQL Server 8.x
- IntelliJ IDEA (recommended) or any Java IDE

### Step 1 — Clone the Repository
```bash
git clone https://github.com/<your-group>/FMSNEW.git
cd FMSNEW
```

### Step 2 — Set Up the Database
Open MySQL Workbench or MySQL CLI and run:
```sql
SOURCE schema.sql;
SOURCE data.sql;
```

### Step 3 — Configure Database Connection
Set environment variables **or** edit `src/dao/DBConnection.java`:

| Variable | Default |
|---|---|
| `FMS_DB_URL` | `jdbc:mysql://localhost:3306/faculty_system` |
| `FMS_DB_USER` | `root` |
| `FMS_DB_PASS` | *(empty)* |

### Step 4 — Build the Project

**Windows (PowerShell):**
```powershell
javac -cp "src;lib/*" -d out $(Get-ChildItem -Recurse -Filter *.java src | ForEach-Object {$_.FullName})
```

**Linux / macOS:**
```bash
find src -name "*.java" > sources.txt
javac -cp "src:lib/*" -d out @sources.txt
```

### Step 5 — Run the Application
```bash
# Windows
java -cp "out;lib/*" ui.Main

# Linux / macOS
java -cp "out:lib/*" ui.Main
```

---

## 🔑 Test Login Credentials

| Role | Username | Password |
|---|---|---|
| Admin | `admin01` | `admin123` |
| Lecturer | `lec01` | `lec123` |
| Technical Officer | `tech01` | `tech123` |
| Student | `stu001` | `stu123` |

---

## 🗄️ Database Schema Overview

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

## 📊 Sample Data Summary

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

## 📐 OOP Concepts Demonstrated

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

## 🎓 Grading System

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

## 👨‍💻 Group Members & Contributions

### Member 1 — [Name] | Student ID: [ID]
**Focus: Core Models, Database Layer, User Management**
- Designed and implemented `User.java` (abstract base class with OOP principles)
- Implemented `Admin.java`, `Lecturer.java`, `Student.java`, `TechOfficer.java` subclasses
- Implemented `UserDAO.java` (login, CRUD, polymorphic buildUser)
- Implemented `DBConnection.java`
- Designed `schema.sql` database structure
- Implemented `AdminDashboardPanel.java`, `UsersPanel.java`
- Admin role functionality: create/manage user profiles

---

### Member 2 — [Name] | Student ID: [ID]
**Focus: Attendance System & Medical Records**
- Implemented `Attendance.java`, `Medical.java` model classes
- Implemented `AttendanceDAO.java` (attendance queries, batch summary, individual summary, theory/practical filtering)
- Implemented `AttendancePanel.java` (UI for viewing and managing attendance)
- Implemented `MedicalsPanel.java` (UI for medical submissions and approval)
- Populated `data.sql` attendance scenarios (>80%, =80%, <80% with/without medicals)
- Technical Officer attendance and medical management features

---

### Member 3 — [Name] | Student ID: [ID]
**Focus: Marks, Grades & GPA System**
- Implemented `Mark.java` model class
- Implemented `MarksDAO.java` (save marks, CA average, eligibility check, grade calculation, SGPA/CGPA)
- Implemented `MarksPanel.java` (UI for mark entry, eligibility view, grade/GPA display)
- Implemented grading logic per UGC Commission Circular No. 12-2024
- Populated `data.sql` marks data for all students
- Batch and individual marks/grades/GPA summary views

---

### Member 4 — [Name] | Student ID: [ID]
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

## 📝 License

This project was developed as an academic assignment for ICT2132 – Object Oriented Programming Practicum, University of Ruhuna.
