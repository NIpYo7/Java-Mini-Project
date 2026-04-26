-- ═════════════════════════════════════════════════════════════════
--  Faculty System — Complete Schema + Seed Data
--  Database : faculty_system
--  Generated: 2026-04-24
-- ═════════════════════════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS faculty_system;
USE faculty_system;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS medicals;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS timetables;
DROP TABLE IF EXISTS notices;
DROP TABLE IF EXISTS student_courses;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- ─────────────────────────────────────────────
-- SCHEMA
-- ─────────────────────────────────────────────

CREATE TABLE users (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    full_name   VARCHAR(120) NOT NULL,
    email       VARCHAR(120),
    phone       VARCHAR(30),
    address     VARCHAR(255),
    role        ENUM('admin','lecturer','student','tech_officer') NOT NULL,
    department  VARCHAR(80),
    profile_pic VARCHAR(255),
    reg_number  VARCHAR(40),
    batch       INT DEFAULT 0,
    is_repeat   TINYINT(1) DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE courses (
    id               INT PRIMARY KEY AUTO_INCREMENT,
    course_code      VARCHAR(20)  NOT NULL UNIQUE,
    course_name      VARCHAR(150) NOT NULL,
    department       VARCHAR(80)  NOT NULL,
    credit_theory    INT NOT NULL DEFAULT 0,
    credit_practical INT NOT NULL DEFAULT 0,
    lecturer_id      INT,
    semester         INT NOT NULL,
    has_practical    TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_course_lecturer FOREIGN KEY (lecturer_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE student_courses (
    student_id INT NOT NULL,
    course_id  INT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    CONSTRAINT fk_sc_student FOREIGN KEY (student_id) REFERENCES users(id)    ON DELETE CASCADE,
    CONSTRAINT fk_sc_course  FOREIGN KEY (course_id)  REFERENCES courses(id)  ON DELETE CASCADE
);

CREATE TABLE notices (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    title      VARCHAR(150) NOT NULL,
    content    TEXT         NOT NULL,
    created_by INT          NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notice_user FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE timetables (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    course_id   INT NOT NULL,
    day_of_week ENUM('Monday','Tuesday','Wednesday','Thursday','Friday') NOT NULL,
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL,
    room        VARCHAR(40) NOT NULL,
    department  VARCHAR(80) NOT NULL,
    CONSTRAINT fk_tt_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE attendance (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    student_id     INT  NOT NULL,
    course_id      INT  NOT NULL,
    session_number INT  NOT NULL,
    session_type   ENUM('theory','practical') NOT NULL,
    status         ENUM('present','absent')   NOT NULL,
    session_date   DATE NOT NULL,
    UNIQUE KEY uq_attendance (student_id, course_id, session_number, session_type),
    CONSTRAINT fk_att_student FOREIGN KEY (student_id) REFERENCES users(id)   ON DELETE CASCADE,
    CONSTRAINT fk_att_course  FOREIGN KEY (course_id)  REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE medicals (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    student_id   INT          NOT NULL,
    course_id    INT          NOT NULL,
    session_num  INT          NOT NULL,
    session_date DATE         NOT NULL,
    reason       VARCHAR(255) NOT NULL,
    doc_path     VARCHAR(255),
    approved     TINYINT NOT NULL DEFAULT 0,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_med_student FOREIGN KEY (student_id) REFERENCES users(id)   ON DELETE CASCADE,
    CONSTRAINT fk_med_course  FOREIGN KEY (course_id)  REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE marks (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT          NOT NULL,
    course_id  INT          NOT NULL,
    exam_type  VARCHAR(20)  NOT NULL,
    mark       DECIMAL(5,2) NOT NULL,
    entered_by INT          NOT NULL,
    entered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_mark (student_id, course_id, exam_type),
    CONSTRAINT fk_mark_student    FOREIGN KEY (student_id) REFERENCES users(id)   ON DELETE CASCADE,
    CONSTRAINT fk_mark_course     FOREIGN KEY (course_id)  REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT fk_mark_entered_by FOREIGN KEY (entered_by) REFERENCES users(id)   ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
-- USERS
--   id=1        admin
--   id=2..6     ICT lecturers  (lec01–lec05)
--   id=7..8     TCS lecturers  (lec06–lec07)
--   id=9        ENG lecturer   (lec08)
--   id=10..13   tech officers
--   id=14..33   students       (stu001–stu020)
-- ─────────────────────────────────────────────

INSERT INTO users
    (username, password, full_name, email, phone, address, role, department, reg_number, batch, is_repeat)
VALUES
-- admin
('admin01','admin123','System Administrator',    'admin@fot.ruh.ac.lk', '0771000001','Faculty of Technology','admin',        'ICT',NULL,0,0),
-- ICT lecturers
('lec01',  'lec123',  'Dr. N. Perera',           'lec01@fot.ruh.ac.lk', '0772000001','Galle',      'lecturer','ICT',NULL,0,0),
('lec02',  'lec123',  'Ms. K. Fernando',         'lec02@fot.ruh.ac.lk', '0772000002','Matara',     'lecturer','ICT',NULL,0,0),
('lec03',  'lec123',  'Mr. S. Silva',            'lec03@fot.ruh.ac.lk', '0772000003','Hambantota', 'lecturer','ICT',NULL,0,0),
('lec04',  'lec123',  'Dr. P. Jayasuriya',       'lec04@fot.ruh.ac.lk', '0772000004','Galle',      'lecturer','ICT',NULL,0,0),
('lec05',  'lec123',  'Ms. D. Senanayake',       'lec05@fot.ruh.ac.lk', '0772000005','Matara',     'lecturer','ICT',NULL,0,0),
-- TCS lecturers
('lec06',  'lec123',  'Ms. R. Wickramasinghe',   'lec06@fot.ruh.ac.lk', '0772000006','Galle',      'lecturer','TCS',NULL,0,0),
('lec07',  'lec123',  'Mr. A. Bandara',          'lec07@fot.ruh.ac.lk', '0772000007','Matara',     'lecturer','TCS',NULL,0,0),
-- ENG lecturer
('lec08',  'lec123',  'Ms. F. Rajapaksa',        'lec08@fot.ruh.ac.lk', '0772000008','Colombo',    'lecturer','ENG',NULL,0,0),
-- tech officers
('tech01', 'tech123', 'T. Officer One',          'tech01@fot.ruh.ac.lk','0773000001','Lab Complex','tech_officer','ICT',NULL,0,0),
('tech02', 'tech123', 'T. Officer Two',          'tech02@fot.ruh.ac.lk','0773000002','Lab Complex','tech_officer','ICT',NULL,0,0),
('tech03', 'tech123', 'T. Officer Three',        'tech03@fot.ruh.ac.lk','0773000003','Lab Complex','tech_officer','ICT',NULL,0,0),
('tech04', 'tech123', 'T. Officer Four',         'tech04@fot.ruh.ac.lk','0773000004','Lab Complex','tech_officer','ICT',NULL,0,0),
-- students
('stu001', 'stu123',  'Student 01',              'stu001@fot.ruh.ac.lk','0710000001','Matara',    'student','ICT','TG/2023/001',2023,0),
('stu002', 'stu123',  'Student 02',              'stu002@fot.ruh.ac.lk','0710000002','Galle',     'student','ICT','TG/2023/002',2023,0),
('stu003', 'stu123',  'Student 03',              'stu003@fot.ruh.ac.lk','0710000003','Hambantota','student','ICT','TG/2023/003',2023,0),
('stu004', 'stu123',  'Student 04',              'stu004@fot.ruh.ac.lk','0710000004','Matara',    'student','ICT','TG/2023/004',2023,0),
('stu005', 'stu123',  'Student 05',              'stu005@fot.ruh.ac.lk','0710000005','Galle',     'student','ICT','TG/2023/005',2023,0),
('stu006', 'stu123',  'Student 06',              'stu006@fot.ruh.ac.lk','0710000006','Matara',    'student','ICT','TG/2023/006',2023,0),
('stu007', 'stu123',  'Student 07',              'stu007@fot.ruh.ac.lk','0710000007','Matara',    'student','ICT','TG/2023/007',2023,0),
('stu008', 'stu123',  'Student 08',              'stu008@fot.ruh.ac.lk','0710000008','Matara',    'student','ICT','TG/2023/008',2023,0),
('stu009', 'stu123',  'Student 09',              'stu009@fot.ruh.ac.lk','0710000009','Matara',    'student','ICT','TG/2023/009',2023,0),
('stu010', 'stu123',  'Student 10',              'stu010@fot.ruh.ac.lk','0710000010','Matara',    'student','ICT','TG/2023/010',2023,0),
('stu011', 'stu123',  'Student 11',              'stu011@fot.ruh.ac.lk','0710000011','Matara',    'student','ICT','TG/2023/011',2023,0),
('stu012', 'stu123',  'Student 12',              'stu012@fot.ruh.ac.lk','0710000012','Matara',    'student','ICT','TG/2023/012',2023,0),
('stu013', 'stu123',  'Student 13',              'stu013@fot.ruh.ac.lk','0710000013','Matara',    'student','ICT','TG/2023/013',2023,0),
('stu014', 'stu123',  'Student 14',              'stu014@fot.ruh.ac.lk','0710000014','Matara',    'student','ICT','TG/2023/014',2023,0),
('stu015', 'stu123',  'Student 15',              'stu015@fot.ruh.ac.lk','0710000015','Matara',    'student','ICT','TG/2023/015',2023,0),
('stu016', 'stu123',  'Student 16',              'stu016@fot.ruh.ac.lk','0710000016','Matara',    'student','ICT','TG/2023/016',2023,0),
('stu017', 'stu123',  'Student 17 (Repeat)',      'stu017@fot.ruh.ac.lk','0710000017','Matara',    'student','ICT','TG/2022/017',2022,1),
('stu018', 'stu123',  'Student 18 (Repeat)',      'stu018@fot.ruh.ac.lk','0710000018','Matara',    'student','ICT','TG/2022/018',2022,1),
('stu019', 'stu123',  'Student 19 (Batch Missed)','stu019@fot.ruh.ac.lk','0710000019','Matara',    'student','ICT','TG/2021/019',2021,0),
('stu020', 'stu123',  'Student 20 (Batch Missed)','stu020@fot.ruh.ac.lk','0710000020','Matara',    'student','ICT','TG/2021/020',2021,0);

-- ─────────────────────────────────────────────
-- COURSES
--   lecturer_id references: lec01=2  lec02=3  lec03=4
--                            lec04=5  lec05=6  lec06=7
--                            lec07=8  lec08=9
-- ─────────────────────────────────────────────

INSERT INTO courses
    (course_code, course_name, department, credit_theory, credit_practical, lecturer_id, semester, has_practical)
VALUES
('ICT2132', 'OOP Practicum',                       'ICT', 0, 2, 2, 1, 1),
('ICT2113', 'Data Structures & Algorithms',         'ICT', 2, 1, 3, 1, 1),
('ICT2122', 'Object Oriented Programming',          'ICT', 2, 0, 4, 1, 0),
('ICT2142', 'Object Oriented Analysis & Design',    'ICT', 2, 0, 5, 1, 0),
('ICT2152', 'E-commerce Implementation & Security', 'ICT', 2, 0, 6, 1, 0),
('TCS2121', 'Soft Skills',                          'TCS', 2, 0, 7, 1, 0),
('TCS2112', 'Business Economics',                   'TCS', 1, 0, 8, 1, 0),
('ENG2112', 'English III',                          'ENG', 1, 0, 9, 1, 0);

-- ─────────────────────────────────────────────
-- TIMETABLES
--   ICT2132 has two back-to-back practical slots on Thursday.
--   All students attend both slots — no group split.
-- ─────────────────────────────────────────────

INSERT INTO timetables (course_id, day_of_week, start_time, end_time, room, department)
VALUES
-- Monday
((SELECT id FROM courses WHERE course_code='ICT2113'), 'Monday',    '08:00:00','10:00:00','LH-01', 'ICT'),
((SELECT id FROM courses WHERE course_code='ICT2113'), 'Monday',    '10:00:00','12:00:00','Lab-01','ICT'),
-- Tuesday
((SELECT id FROM courses WHERE course_code='ICT2142'), 'Tuesday',   '09:00:00','11:00:00','LH-02', 'ICT'),
-- Wednesday
((SELECT id FROM courses WHERE course_code='TCS2121'), 'Wednesday', '08:00:00','10:00:00','LH-04', 'TCS'),
((SELECT id FROM courses WHERE course_code='ICT2152'), 'Wednesday', '11:00:00','13:00:00','LH-03', 'ICT'),
-- Thursday
((SELECT id FROM courses WHERE course_code='ICT2122'), 'Thursday',  '09:00:00','11:00:00','LH-01', 'ICT'),
((SELECT id FROM courses WHERE course_code='ICT2132'), 'Thursday',  '11:00:00','13:00:00','Lab-01','ICT'),
((SELECT id FROM courses WHERE course_code='ICT2132'), 'Thursday',  '14:00:00','16:00:00','Lab-01','ICT'),
-- Friday
((SELECT id FROM courses WHERE course_code='TCS2112'), 'Friday',    '08:00:00','09:00:00','LH-05', 'TCS'),
((SELECT id FROM courses WHERE course_code='ENG2112'), 'Friday',    '09:00:00','10:00:00','LH-05', 'ENG');

-- ─────────────────────────────────────────────
-- STUDENT COURSES — all students enrolled in all courses
-- ─────────────────────────────────────────────

INSERT INTO student_courses (student_id, course_id)
SELECT u.id, c.id
FROM   users u
CROSS JOIN courses c
WHERE  u.role = 'student';

-- ─────────────────────────────────────────────
-- NOTICES
-- ─────────────────────────────────────────────

INSERT INTO notices (title, content, created_by) VALUES
('Semester Started',   'Semester I academic activities started from this week.',         1),
('Medical Submission', 'Upload medical details within 7 days from absence date.',        1),
('CA Evaluation',      'CA1, CA2 and assignment marks will be published in the system.', 1),
('Final Eligibility',  'Eligibility is based on attendance and CA average >= 40.',       1);

-- ─────────────────────────────────────────────
-- ATTENDANCE — ICT2132 (15 theory + 15 practical sessions)
--   stu001 = id 14 | stu002 = id 15 | stu003 = id 16
--   stu004 = id 17 | stu005 = id 18
-- ─────────────────────────────────────────────

WITH RECURSIVE sessions AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM sessions WHERE n < 15
)
INSERT INTO attendance (student_id, course_id, session_number, session_type, status, session_date)
-- stu001: theory 14/15 present, practical 13/15 present
SELECT 14,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'theory',
       IF(n<=14,'present','absent'),DATE_ADD('2026-01-05',INTERVAL n DAY) FROM sessions
UNION ALL
SELECT 14,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'practical',
       IF(n<=13,'present','absent'),DATE_ADD('2026-02-05',INTERVAL n DAY) FROM sessions
-- stu002: theory 12/15, practical 12/15
UNION ALL
SELECT 15,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'theory',
       IF(n<=12,'present','absent'),DATE_ADD('2026-01-05',INTERVAL n DAY) FROM sessions
UNION ALL
SELECT 15,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'practical',
       IF(n<=12,'present','absent'),DATE_ADD('2026-02-05',INTERVAL n DAY) FROM sessions
-- stu003: theory 9/15, practical 9/15
UNION ALL
SELECT 16,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'theory',
       IF(n<=9,'present','absent'), DATE_ADD('2026-01-05',INTERVAL n DAY) FROM sessions
UNION ALL
SELECT 16,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'practical',
       IF(n<=9,'present','absent'), DATE_ADD('2026-02-05',INTERVAL n DAY) FROM sessions
-- stu004: theory 11/15, practical 11/15
UNION ALL
SELECT 17,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'theory',
       IF(n<=11,'present','absent'),DATE_ADD('2026-01-05',INTERVAL n DAY) FROM sessions
UNION ALL
SELECT 17,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'practical',
       IF(n<=11,'present','absent'),DATE_ADD('2026-02-05',INTERVAL n DAY) FROM sessions
-- stu005: theory 10/15, practical 10/15
UNION ALL
SELECT 18,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'theory',
       IF(n<=10,'present','absent'),DATE_ADD('2026-01-05',INTERVAL n DAY) FROM sessions
UNION ALL
SELECT 18,(SELECT id FROM courses WHERE course_code='ICT2132'),n,'practical',
       IF(n<=10,'present','absent'),DATE_ADD('2026-02-05',INTERVAL n DAY) FROM sessions;

-- ─────────────────────────────────────────────
-- MEDICALS
--   approved: 0 = pending | 1 = approved | 2 = rejected
-- ─────────────────────────────────────────────

INSERT INTO medicals (student_id, course_id, session_num, session_date, reason, approved)
VALUES
(17,(SELECT id FROM courses WHERE course_code='ICT2132'),12,'2026-01-17','Fever and doctor recommendation',       1),
(17,(SELECT id FROM courses WHERE course_code='ICT2132'),13,'2026-02-18','Hospital admission',                    1),
(18,(SELECT id FROM courses WHERE course_code='ICT2132'),11,'2026-01-16','Clinic treatment pending confirmation', 0),
(18,(SELECT id FROM courses WHERE course_code='ICT2132'),12,'2026-02-17','Late medical submission rejected',      2),
(16,(SELECT id FROM courses WHERE course_code='ICT2132'),10,'2026-01-15','Not approved evidence',                 2);

-- ─────────────────────────────────────────────
-- MARKS — ICT2132 detailed (CA1, CA2, assignment, final)
-- ─────────────────────────────────────────────

INSERT INTO marks (student_id, course_id, exam_type, mark, entered_by)
VALUES
-- stu001 (id 14) — good standing
(14,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca1',        78, 3),
(14,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca2',        74, 3),
(14,(SELECT id FROM courses WHERE course_code='ICT2132'),'assignment', 80, 3),
(14,(SELECT id FROM courses WHERE course_code='ICT2132'),'final',      82, 3),
-- stu002 (id 15) — borderline pass
(15,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca1',        45, 3),
(15,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca2',        42, 3),
(15,(SELECT id FROM courses WHERE course_code='ICT2132'),'assignment', 40, 3),
(15,(SELECT id FROM courses WHERE course_code='ICT2132'),'final',      48, 3),
-- stu003 (id 16) — failing, no final mark yet
(16,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca1',        34, 3),
(16,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca2',        32, 3),
(16,(SELECT id FROM courses WHERE course_code='ICT2132'),'assignment', 36, 3),
-- stu004 (id 17) — good standing
(17,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca1',        68, 3),
(17,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca2',        72, 3),
(17,(SELECT id FROM courses WHERE course_code='ICT2132'),'assignment', 70, 3),
(17,(SELECT id FROM courses WHERE course_code='ICT2132'),'final',      76, 3),
-- stu005 (id 18) — borderline fail, no final mark yet
(18,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca1',        38, 3),
(18,(SELECT id FROM courses WHERE course_code='ICT2132'),'ca2',        35, 3),
(18,(SELECT id FROM courses WHERE course_code='ICT2132'),'assignment', 37, 3);

-- ─────────────────────────────────────────────
-- MARKS — final exam bulk data for other ICT courses
--         students id 14–23 (stu001–stu010)
-- ─────────────────────────────────────────────

INSERT INTO marks (student_id, course_id, exam_type, mark, entered_by)
SELECT sc.student_id,
       sc.course_id,
       'final',
       CASE
           WHEN sc.student_id % 5 = 0 THEN 58
           WHEN sc.student_id % 4 = 0 THEN 64
           WHEN sc.student_id % 3 = 0 THEN 72
           WHEN sc.student_id % 2 = 0 THEN 48
           ELSE 81
       END,
       3
FROM   student_courses sc
JOIN   courses c ON sc.course_id = c.id
WHERE  c.course_code IN ('ICT2113','ICT2122','ICT2142','ICT2152')
  AND  sc.student_id BETWEEN 14 AND 23;
