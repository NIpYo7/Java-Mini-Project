

CREATE DATABASE IF NOT EXISTS faculty_system;
USE faculty_system;

-- Drop tables in reverse FK order for clean re-run
DROP TABLE IF EXISTS course_materials;
DROP TABLE IF EXISTS timetables;
DROP TABLE IF EXISTS notices;
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS medicals;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS student_courses;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS users;

-- ------------------------------------------------------------
-- USERS
-- role: admin | lecturer | student | tech_officer
-- profile_pic: relative path to uploaded image file
-- ------------------------------------------------------------
CREATE TABLE users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL DEFAULT 'pass123',
    full_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(100),
    phone       VARCHAR(20),
    address     VARCHAR(200),
    role        ENUM('admin','lecturer','student','tech_officer') NOT NULL,
    department  VARCHAR(100) DEFAULT 'ICT',
    profile_pic VARCHAR(300) DEFAULT NULL,
    reg_number  VARCHAR(30)  DEFAULT NULL,
    batch       INT          DEFAULT NULL,
    is_repeat   TINYINT      DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- COURSES  (updated to match real timetable)
-- ------------------------------------------------------------
CREATE TABLE courses (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    course_code      VARCHAR(20)  NOT NULL UNIQUE,
    course_name      VARCHAR(150) NOT NULL,
    department       VARCHAR(100) DEFAULT 'ICT',
    credit_theory    INT DEFAULT 0,
    credit_practical INT DEFAULT 0,
    lecturer_id      INT,
    semester         INT DEFAULT 2,
    has_practical    TINYINT DEFAULT 0,
    FOREIGN KEY (lecturer_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ------------------------------------------------------------
-- STUDENT_COURSES  (enrolment)
-- ------------------------------------------------------------
CREATE TABLE student_courses (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id  INT NOT NULL,
    UNIQUE KEY uq_enrol (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES courses(id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- ATTENDANCE
-- session_date is REQUIRED — used to block duplicate medicals
-- session_number: 1-15
-- session_type: theory | practical
-- status: present | absent
-- ------------------------------------------------------------
CREATE TABLE attendance (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    student_id     INT  NOT NULL,
    course_id      INT  NOT NULL,
    session_number INT  NOT NULL,
    session_type   ENUM('theory','practical') NOT NULL,
    status         ENUM('present','absent') DEFAULT 'absent',
    session_date   DATE NOT NULL,
    UNIQUE KEY uq_att (student_id, course_id, session_number, session_type),
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES courses(id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- MEDICALS
-- submitted_at: auto timestamp shown in UI
-- approved: 0=pending, 1=approved, 2=rejected
-- Cannot submit medical if attendance record exists for that date
-- This is enforced in Java (AttendanceDAO) and also via app logic
-- ------------------------------------------------------------
CREATE TABLE medicals (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    student_id   INT NOT NULL,
    course_id    INT NOT NULL,
    session_num  INT NOT NULL,
    session_date DATE NOT NULL,
    reason       VARCHAR(300),
    doc_path     VARCHAR(300),
    approved     TINYINT DEFAULT 0,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES courses(id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- MARKS
-- exam_type: CA1 | CA2 | assignment | final
-- All marks out of 100
-- ------------------------------------------------------------
CREATE TABLE marks (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id  INT NOT NULL,
    exam_type  VARCHAR(30) NOT NULL,
    mark       DECIMAL(5,2),
    entered_by INT,
    entered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_mark (student_id, course_id, exam_type),
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (entered_by) REFERENCES users(id) ON DELETE SET NULL
);

-- ------------------------------------------------------------
-- NOTICES
-- ------------------------------------------------------------
CREATE TABLE notices (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(200) NOT NULL,
    content    TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- ------------------------------------------------------------
-- TIMETABLES
-- ------------------------------------------------------------
CREATE TABLE timetables (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    course_id   INT NOT NULL,
    day_of_week VARCHAR(10),
    start_time  TIME,
    end_time    TIME,
    room        VARCHAR(50),
    department  VARCHAR(100) DEFAULT 'ICT',
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- COURSE MATERIALS
-- ------------------------------------------------------------
CREATE TABLE course_materials (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    course_id   INT NOT NULL,
    title       VARCHAR(200),
    file_path   VARCHAR(300),
    uploaded_by INT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id)   REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE SET NULL
);
