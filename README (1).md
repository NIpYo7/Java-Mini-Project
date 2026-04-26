# 🎓 Faculty Management System (FMS)

> A Java Swing desktop application for managing faculty operations including students, lecturers, attendance, marks, courses, and more — built as part of **ICT2132 Mini Project**.

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?style=flat-square&logo=mysql)
![FlatLaf](https://img.shields.io/badge/UI-FlatLaf%203.4.1-purple?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features-by-role)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Database Setup](#-database-setup)
- [Configuration](#-configuration)
- [Build & Run](#-build--run)
- [Test Logins](#-test-logins)
- [OOP Concepts](#-oop-concepts-demonstrated)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)

---

## 📌 Overview

The **Faculty Management System (FMS)** is a role-based desktop application designed for university faculty administration. It supports four user roles — **Admin**, **Lecturer**, **Technical Officer**, and **Student** — each with a tailored dashboard and set of features.

The system is built using **Java (Swing)** for the GUI, **MySQL** for the database backend, and the **FlatLaf** library for a modern UI theme.

---

## ✨ Features by Role

### 🔑 Admin
- Manage all users (create, update, delete)
- Manage courses and departments
- Post and manage notices
- View and manage timetables

### 👨‍🏫 Lecturer
- Update personal profile and photo
- Upload marks for students
- View student marks, eligibility, and attendance reports

### 🛠️ Technical Officer
- Update personal profile
- Manage student attendance records
- Manage medical records
- View notices and timetable

### 🎓 Student
- Update contact info and profile photo
- View personal attendance and medical records
- View enrolled courses and grades
- View GPA, timetable, and notices
- View and download lecture materials

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17+ | Core application language |
| Java Swing | Built-in | Desktop GUI framework |
| MySQL | 8.x | Relational database |
| JDBC (`mysql-connector-j`) | 9.6.0 | Database connectivity |
| FlatLaf | 3.4.1 | Modern flat UI theme |
| IntelliJ IDEA | Any | Recommended IDE |

---

## 📁 Project Structure

```
FMSNEW/
│
├── src/                            # All Java source code
│   ├── dao/                        # Data Access Objects (DB layer)
│   │   ├── DBConnection.java       # MySQL connection manager
│   │   ├── UserDAO.java            # User CRUD operations
│   │   ├── AttendanceDAO.java      # Attendance management
│   │   ├── CourseDAO.java          # Course management
│   │   ├── MarksDAO.java           # Marks and GPA operations
│   │   ├── NoticeDAO.java          # Notice board operations
│   │   └── LectureMaterialDAO.java # Lecture material upload/fetch
│   │
│   ├── model/                      # Java model/entity classes
│   │   ├── User.java               # Abstract base user class
│   │   ├── Admin.java              # Admin role model
│   │   ├── Lecturer.java           # Lecturer role model
│   │   ├── Student.java            # Student role model
│   │   ├── TechOfficer.java        # Tech Officer role model
│   │   ├── Attendance.java         # Attendance record model
│   │   ├── Course.java             # Course model
│   │   ├── Mark.java               # Marks model
│   │   ├── Medical.java            # Medical record model
│   │   ├── Notice.java             # Notice model
│   │   └── LectureMaterial.java    # Lecture material model
│   │
│   └── ui/                         # UI components
│       ├── Main.java               # Application entry point
│       ├── MainFrame.java          # Main application window
│       ├── theme/
│       │   └── AppTheme.java       # Custom FlatLaf theme config
│       └── panels/                 # Role-specific panels
│           ├── LoginPanel.java              # Login screen
│           ├── BasePanel.java               # Shared panel base class
│           ├── AdminDashboardPanel.java     # Admin home dashboard
│           ├── LecturerDashboardPanel.java  # Lecturer home dashboard
│           ├── StudentDashboardPanel.java   # Student home dashboard
│           ├── TechOfficerDashboardPanel.java # Tech Officer dashboard
│           ├── AttendancePanel.java         # Attendance management
│           ├── MarksPanel.java              # Marks management
│           ├── MedicalsPanel.java           # Medical records panel
│           ├── CoursesPanel.java            # Courses panel
│           ├── NoticesPanel.java            # Notices panel
│           ├── UsersPanel.java              # User management panel
│           ├── ProfilePanel.java            # Profile update panel
│           ├── TimetablePanel.java          # Timetable view panel
│           └── LectureMaterialsPanel.java   # Lecture materials panel
│
├── lib/                            # External JAR libraries
│   ├── flatlaf-3.4.1.jar           # FlatLaf UI theme
│   └── flatlaf-3.4.1-javadoc.jar   # FlatLaf documentation
│
├── out/                            # Compiled .class files (auto-generated)
│   └── production/FMSNEW/
│
├── schema.sql                      # Full DB schema (run first)
├── schema_lecture_materials.sql    # Additional schema for materials
├── data.sql                        # Sample seed data
├── FMSNEW.iml                      # IntelliJ module file
└── README.md                       # This file
```

---

## 🗄️ Database Setup

### Prerequisites
- MySQL 8.x installed and running
- A MySQL user with privileges to create databases

### Step 1 — Create the schema

```sql
mysql -u root -p < schema.sql
```

This creates the `faculty_system` database and all tables:
`users`, `courses`, `attendance`, `marks`, `medicals`, `notices`, `timetables`, `student_courses`, `lecture_materials`

### Step 2 — Add lecture materials table (if separate)

```sql
mysql -u root -p faculty_system < schema_lecture_materials.sql
```

### Step 3 — Load sample data

```sql
mysql -u root -p faculty_system < data.sql
```

Sample data includes:
- 1 Admin account
- 5 Lecturers
- 4 Technical Officers
- 20 Students (including repeat and batch-missed students)
- Attendance scenarios (>80%, =80%, <80% with/without medicals)
- CA/final marks, grades, and GPA-ready records

---

## ⚙️ Configuration

Database credentials are configured via **environment variables** (recommended for security):

| Variable | Default | Description |
|---|---|---|
| `FMS_DB_URL` | `jdbc:mysql://localhost:3306/faculty_system` | JDBC connection URL |
| `FMS_DB_USER` | `root` | MySQL username |
| `FMS_DB_PASS` | *(empty)* | MySQL password |

### Setting Environment Variables

**Windows (Command Prompt):**
```cmd
set FMS_DB_URL=jdbc:mysql://localhost:3306/faculty_system
set FMS_DB_USER=root
set FMS_DB_PASS=yourpassword
```

**Windows (PowerShell):**
```powershell
$env:FMS_DB_URL = "jdbc:mysql://localhost:3306/faculty_system"
$env:FMS_DB_USER = "root"
$env:FMS_DB_PASS = "yourpassword"
```

**Linux / macOS:**
```bash
export FMS_DB_URL=jdbc:mysql://localhost:3306/faculty_system
export FMS_DB_USER=root
export FMS_DB_PASS=yourpassword
```

> ⚠️ If no environment variables are set, the application falls back to defaults: `localhost:3306`, user `root`, empty password.

---

## 🚀 Build & Run

### Using IntelliJ IDEA (Recommended)

1. Open the `FMSNEW` folder as a project in IntelliJ IDEA.
2. Ensure the libraries in `/lib` are added to the project's classpath.
3. Add `mysql-connector-j-9.6.0.jar` to the project libraries (download from [MySQL official site](https://dev.mysql.com/downloads/connector/j/)).
4. Run `src/ui/Main.java`.

### Using Command Line (Windows PowerShell)

**Compile:**
```powershell
javac -cp "src;lib/*" -d out $(Get-ChildItem -Recurse -Filter *.java src | ForEach-Object {$_.FullName})
```

**Run:**
```powershell
java -cp "out;lib/*" ui.Main
```

### Using Command Line (Linux / macOS)

**Compile:**
```bash
find src -name "*.java" > sources.txt
javac -cp "src:lib/*" -d out @sources.txt
```

**Run:**
```bash
java -cp "out:lib/*" ui.Main
```

> 📝 Make sure `mysql-connector-j-9.6.0.jar` is present in the `lib/` directory before compiling.

---

## 🔐 Test Logins

| Role | Username | Password |
|---|---|---|
| Admin | `admin01` | `admin123` |
| Lecturer | `lec01` | `lec123` |
| Student | `stu001` | `stu123` |
| Technical Officer | `tech01` | `tech123` |

---

## 🧠 OOP Concepts Demonstrated

This project was built to showcase core Object-Oriented Programming principles:

| Concept | Implementation |
|---|---|
| **Classes & Objects** | All entities modelled as Java classes |
| **Inheritance** | `Admin`, `Lecturer`, `Student`, `TechOfficer` extend abstract `User` |
| **Abstraction** | `User` is an abstract base class; `BasePanel` is an abstract UI panel |
| **Polymorphism** | Role-specific dashboards and user object behavior |
| **Encapsulation** | Private fields with getters/setters in all model classes |
| **Exception Handling** | `try/catch` blocks throughout DAO and UI action layers |
| **Database Handling** | DAO pattern with JDBC for clean data access |
| **GUI** | Java Swing with FlatLaf theming |

---

## 📸 Screenshots

> *(Add screenshots of your application here)*

| Login Screen | Admin Dashboard | Student Dashboard |
|---|---|---|
| ![Login](#) | ![Admin](#) | ![Student](#) |

---

## 🤝 Contributing

This is an academic project. If you'd like to suggest improvements or report issues:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

Developed as part of the **ICT2132** coursework.

> Built with ❤️ using Java Swing + MySQL
