CREATE DATABASE IF NOT EXISTS faculty_system;
USE faculty_system;

-- USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    full_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(100),
    phone       VARCHAR(20),
    address     VARCHAR(200),
    role        ENUM('admin','lecturer','student','tech_officer') NOT NULL,
    department  VARCHAR(100),
    profile_pic VARCHAR(200),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


-- COURSES TABLE
CREATE TABLE IF NOT EXISTS courses (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    course_code      VARCHAR(20)  NOT NULL UNIQUE,
    course_name      VARCHAR(100) NOT NULL,
    department       VARCHAR(100),
    credit_theory    INT DEFAULT 0,
    credit_practical INT DEFAULT 0,
    lecturer_id      INT,
    semester         INT,
    FOREIGN KEY (lecturer_id) REFERENCES users(id)
    );


-- STUDENT_COURSES TABLE
CREATE TABLE IF NOT EXISTS student_courses (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id  INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (course_id)  REFERENCES courses(id)
    );


-- ATTENDANCE TABLE
CREATE TABLE IF NOT EXISTS attendance (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    student_id     INT NOT NULL,
    course_id      INT NOT NULL,
    session_number INT NOT NULL,
    session_type   ENUM('theory','practical') NOT NULL,
    status         ENUM('present','absent') DEFAULT 'absent',
    session_date   DATE,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (course_id)  REFERENCES courses(id)
    );



-- MEDICALS TABLE
CREATE TABLE IF NOT EXISTS medicals (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    student_id  INT NOT NULL,
    course_id   INT NOT NULL,
    session_num INT NOT NULL,
    reason      VARCHAR(200),
    doc_path    VARCHAR(200),
    approved    TINYINT DEFAULT 0,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (course_id)  REFERENCES courses(id)
    );

-- MARKS TABLE
CREATE TABLE IF NOT EXISTS marks (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id  INT NOT NULL,
    exam_type  VARCHAR(30) NOT NULL,
    mark       DECIMAL(5,2),
    entered_by INT,
    entered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (course_id)  REFERENCES courses(id),
    FOREIGN KEY (entered_by) REFERENCES users(id)
    );


-- NOTICES TABLE
CREATE TABLE IF NOT EXISTS notices (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    content     TEXT,
    created_by  INT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
    );


-- TIMETABLES TABLE
CREATE TABLE IF NOT EXISTS timetables (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    course_id   INT NOT NULL,
    day_of_week VARCHAR(10),
    start_time  TIME,
    end_time    TIME,
    room        VARCHAR(50),
    department  VARCHAR(100),
    FOREIGN KEY (course_id) REFERENCES courses(id)
    );

-- COURSE MATERIALS TABLE
CREATE TABLE IF NOT EXISTS course_materials (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    course_id    INT NOT NULL,
    title        VARCHAR(200),
    file_path    VARCHAR(300),
    uploaded_by  INT,
    uploaded_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id)   REFERENCES courses(id),
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
    );











