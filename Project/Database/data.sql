
USE faculty_system;

-- ============================================================
-- USERS
-- ============================================================
INSERT INTO users (username,password,full_name,email,phone,role,department,reg_number,batch,is_repeat) VALUES
-- Admin (1)
('admin01','admin123','Dr. Nimal Perera','nimal@ruh.ac.lk','0771111111','admin','Administration',NULL,NULL,0),

-- Lecturers (5) — IDs will be 2-6
('lec01','lec123','Dr. Saman Fernando','saman@ruh.ac.lk','0772222201','lecturer','ICT',NULL,NULL,0),
('lec02','lec123','Dr. Amali Jayawardena','amali@ruh.ac.lk','0772222202','lecturer','ICT',NULL,NULL,0),
('lec03','lec123','Mr. Ruwan Silva','ruwan@ruh.ac.lk','0772222203','lecturer','ICT',NULL,NULL,0),
('lec04','lec123','Ms. Dilini Rathnayake','dilini@ruh.ac.lk','0772222204','lecturer','ICT',NULL,NULL,0),
('lec05','lec123','Dr. Kasun Wickrama','kasun@ruh.ac.lk','0772222205','lecturer','ICT',NULL,NULL,0),

-- Tech Officers (4) — IDs 7-10
('tech01','tech123','Mr. Pradeep Bandara','pradeep@ruh.ac.lk','0773333301','tech_officer','ICT',NULL,NULL,0),
('tech02','tech123','Ms. Sanduni Herath','sanduni@ruh.ac.lk','0773333302','tech_officer','ICT',NULL,NULL,0),
('tech03','tech123','Mr. Chamara Kumara','chamara@ruh.ac.lk','0773333303','tech_officer','ICT',NULL,NULL,0),
('tech04','tech123','Ms. Ishara Mendis','ishara@ruh.ac.lk','0773333304','tech_officer','ICT',NULL,NULL,0),

-- Students (20) — IDs 11-30
-- Normal batch students (IDs 11-25)
('stu001','stu123','Ashan Perera','ashan@stu.ruh.ac.lk','0774444401','student','ICT','ICT/2023/001',2023,0),
('stu002','stu123','Bimsara Karunarathna','bims@stu.ruh.ac.lk','0774444402','student','ICT','ICT/2023/002',2023,0),
('stu003','stu123','Chathuri Dissanayake','chat@stu.ruh.ac.lk','0774444403','student','ICT','ICT/2023/003',2023,0),
('stu004','stu123','Dinusha Weerasekara','dinu@stu.ruh.ac.lk','0774444404','student','ICT','ICT/2023/004',2023,0),
('stu005','stu123','Erandi Gunasekara','eran@stu.ruh.ac.lk','0774444405','student','ICT','ICT/2023/005',2023,0),
('stu006','stu123','Fathima Rameez','fath@stu.ruh.ac.lk','0774444406','student','ICT','ICT/2023/006',2023,0),
('stu007','stu123','Gayan Samaraweera','gaya@stu.ruh.ac.lk','0774444407','student','ICT','ICT/2023/007',2023,0),
('stu008','stu123','Hasini Liyanage','hasi@stu.ruh.ac.lk','0774444408','student','ICT','ICT/2023/008',2023,0),
('stu009','stu123','Ishan Rajapaksha','isha@stu.ruh.ac.lk','0774444409','student','ICT','ICT/2023/009',2023,0),
('stu010','stu123','Janitha Seneviratne','jani@stu.ruh.ac.lk','0774444410','student','ICT','ICT/2023/010',2023,0),
('stu011','stu123','Kasun Madusanka','kasu@stu.ruh.ac.lk','0774444411','student','ICT','ICT/2023/011',2023,0),
('stu012','stu123','Lasantha Jayasuriya','lasa@stu.ruh.ac.lk','0774444412','student','ICT','ICT/2023/012',2023,0),
('stu013','stu123','Malsha Wickramasinghe','mals@stu.ruh.ac.lk','0774444413','student','ICT','ICT/2023/013',2023,0),
('stu014','stu123','Nadeesha Kumari','nade@stu.ruh.ac.lk','0774444414','student','ICT','ICT/2023/014',2023,0),
('stu015','stu123','Oshadha Peiris','osha@stu.ruh.ac.lk','0774444415','student','ICT','ICT/2023/015',2023,0),
-- Repeat/batch-missed students (IDs 26-30)
('stu016','stu123','Piumal Gamage (Repeat)','pium@stu.ruh.ac.lk','0774444416','student','ICT','ICT/2022/016',2022,1),
('stu017','stu123','Ravindu Rathnasiri (Repeat)','ravi@stu.ruh.ac.lk','0774444417','student','ICT','ICT/2022/017',2022,1),
('stu018','stu123','Sachini Vithanage','sach@stu.ruh.ac.lk','0774444418','student','ICT','ICT/2023/018',2023,0),
('stu019','stu123','Tharaka Senanayake','thar@stu.ruh.ac.lk','0774444419','student','ICT','ICT/2023/019',2023,0),
('stu020','stu123','Uthpala Nanayakkara','uth@stu.ruh.ac.lk','0774444420','student','ICT','ICT/2023/020',2023,0);

-- ============================================================
-- COURSES  (from real timetable)
-- Monday    08:00-10:00 ICT2113 (T) Data Structures & Algorithms
-- Monday    10:00-12:00 ICT2113 (P) Data Structures & Algorithms
-- Tuesday   09:00-11:00 ICT2142 (T) Object Oriented Analysis & Design
-- Wednesday 08:00-10:00 TCS2121 (T) Soft Skills
-- Wednesday 11:00-13:00 ICT2152 (T) E-commerce Implementation & Security
-- Thursday  09:00-11:00 ICT2122 (T) Object Oriented Programming
-- Thursday  11:00-13:00 ICT2132 (P) OOP Practicum
-- Thursday  14:00-16:00 ICT2132 (P) OOP Practicum (2nd slot)
-- Friday    08:00-09:00 TCS2112 (T) Business Economics
-- Friday    09:00-10:00 ENG2112 (T) English III
-- ============================================================
INSERT INTO courses (course_code,course_name,department,credit_theory,credit_practical,lecturer_id,semester,has_practical) VALUES
('ICT2113','Data Structures & Algorithms',   'ICT',2,1,2,2,1),
('ICT2142','Object Oriented Analysis & Design','ICT',3,0,3,2,0),
('TCS2121','Soft Skills',                    'ICT',2,0,4,2,0),
('ICT2152','E-Commerce Implementation & Security','ICT',3,0,5,2,0),
('ICT2122','Object Oriented Programming',    'ICT',3,0,6,2,0),
('ICT2132','OOP Practicum',                  'ICT',0,2,2,2,1),
('TCS2112','Business Economics',             'ICT',1,0,4,2,0),
('ENG2112','English III',                    'ICT',1,0,5,2,0);

-- ============================================================
-- TIMETABLE
-- ============================================================
INSERT INTO timetables (course_id,day_of_week,start_time,end_time,room,department) VALUES
(1,'Monday',   '08:00','10:00','LH1','ICT'),
(1,'Monday',   '10:00','12:00','Lab1','ICT'),
(2,'Tuesday',  '09:00','11:00','LH2','ICT'),
(3,'Wednesday','08:00','10:00','LH3','ICT'),
(4,'Wednesday','11:00','13:00','LH1','ICT'),
(5,'Thursday', '09:00','11:00','LH2','ICT'),
(6,'Thursday', '11:00','13:00','Lab2','ICT'),
(6,'Thursday', '14:00','16:00','Lab2','ICT'),
(7,'Friday',   '08:00','09:00','LH3','ICT'),
(8,'Friday',   '09:00','10:00','LH1','ICT');

-- ============================================================
-- ENROL ALL 20 STUDENTS IN ALL 8 COURSES
-- ============================================================
INSERT INTO student_courses (student_id, course_id)
SELECT u.id, c.id FROM users u, courses c WHERE u.role='student';

-- ============================================================
-- ATTENDANCE DATA
-- Using course_id=1 (ICT2113 DSA) as primary example
-- Scenarios covered across students:
--   stu001 (id=11): >80% theory, no medical
--   stu002 (id=12): >80% practical, no medical
--   stu003 (id=13): exactly 80%
--   stu004 (id=14): <80% no medical
--   stu005 (id=15): >80% with approved medicals
--   stu006 (id=16): <80% with medicals
-- ============================================================

-- stu001 (id=11) DSA Theory: 14/15 = 93% > 80%
INSERT INTO attendance (student_id,course_id,session_number,session_type,status,session_date) VALUES
(11,1,1,'theory','present','2025-01-06'),(11,1,2,'theory','present','2025-01-13'),
(11,1,3,'theory','present','2025-01-20'),(11,1,4,'theory','present','2025-01-27'),
(11,1,5,'theory','present','2025-02-03'),(11,1,6,'theory','present','2025-02-10'),
(11,1,7,'theory','present','2025-02-17'),(11,1,8,'theory','present','2025-02-24'),
(11,1,9,'theory','present','2025-03-03'),(11,1,10,'theory','present','2025-03-10'),
(11,1,11,'theory','present','2025-03-17'),(11,1,12,'theory','present','2025-03-24'),
(11,1,13,'theory','present','2025-03-31'),(11,1,14,'theory','present','2025-04-07'),
(11,1,15,'theory','absent','2025-04-14');

-- stu001 DSA Practical: 13/15 = 87% > 80%
INSERT INTO attendance (student_id,course_id,session_number,session_type,status,session_date) VALUES
(11,1,1,'practical','present','2025-01-06'),(11,1,2,'practical','present','2025-01-13'),
(11,1,3,'practical','present','2025-01-20'),(11,1,4,'practical','present','2025-01-27'),
(11,1,5,'practical','present','2025-02-03'),(11,1,6,'practical','present','2025-02-10'),
(11,1,7,'practical','present','2025-02-17'),(11,1,8,'practical','present','2025-02-24'),
(11,1,9,'practical','present','2025-03-03'),(11,1,10,'practical','present','2025-03-10'),
(11,1,11,'practical','present','2025-03-17'),(11,1,12,'practical','present','2025-03-24'),
(11,1,13,'practical','present','2025-03-31'),(11,1,14,'practical','absent','2025-04-07'),
(11,1,15,'practical','absent','2025-04-14');

-- stu003 (id=13) DSA Theory: exactly 12/15 = 80%
INSERT INTO attendance (student_id,course_id,session_number,session_type,status,session_date) VALUES
(13,1,1,'theory','present','2025-01-06'),(13,1,2,'theory','present','2025-01-13'),
(13,1,3,'theory','present','2025-01-20'),(13,1,4,'theory','present','2025-01-27'),
(13,1,5,'theory','present','2025-02-03'),(13,1,6,'theory','present','2025-02-10'),
(13,1,7,'theory','present','2025-02-17'),(13,1,8,'theory','present','2025-02-24'),
(13,1,9,'theory','present','2025-03-03'),(13,1,10,'theory','present','2025-03-10'),
(13,1,11,'theory','present','2025-03-17'),(13,1,12,'theory','present','2025-03-24'),
(13,1,13,'theory','absent','2025-03-31'),(13,1,14,'theory','absent','2025-04-07'),
(13,1,15,'theory','absent','2025-04-14');

-- stu004 (id=14) DSA Theory: 9/15 = 60% < 80% no medical
INSERT INTO attendance (student_id,course_id,session_number,session_type,status,session_date) VALUES
(14,1,1,'theory','present','2025-01-06'),(14,1,2,'theory','present','2025-01-13'),
(14,1,3,'theory','present','2025-01-20'),(14,1,4,'theory','present','2025-01-27'),
(14,1,5,'theory','present','2025-02-03'),(14,1,6,'theory','present','2025-02-10'),
(14,1,7,'theory','present','2025-02-17'),(14,1,8,'theory','present','2025-02-24'),
(14,1,9,'theory','present','2025-03-03'),(14,1,10,'theory','absent','2025-03-10'),
(14,1,11,'theory','absent','2025-03-17'),(14,1,12,'theory','absent','2025-03-24'),
(14,1,13,'theory','absent','2025-03-31'),(14,1,14,'theory','absent','2025-04-07'),
(14,1,15,'theory','absent','2025-04-14');

-- stu005 (id=15) DSA Theory: 11/15 present + 3 approved medicals = 14/15 = 93% >80%
INSERT INTO attendance (student_id,course_id,session_number,session_type,status,session_date) VALUES
(15,1,1,'theory','present','2025-01-06'),(15,1,2,'theory','present','2025-01-13'),
(15,1,3,'theory','present','2025-01-20'),(15,1,4,'theory','present','2025-01-27'),
(15,1,5,'theory','present','2025-02-03'),(15,1,6,'theory','present','2025-02-10'),
(15,1,7,'theory','present','2025-02-17'),(15,1,8,'theory','present','2025-02-24'),
(15,1,9,'theory','present','2025-03-03'),(15,1,10,'theory','present','2025-03-10'),
(15,1,11,'theory','present','2025-03-17'),(15,1,12,'theory','absent','2025-03-24'),
(15,1,13,'theory','absent','2025-03-31'),(15,1,14,'theory','absent','2025-04-07'),
(15,1,15,'theory','present','2025-04-14');

INSERT INTO medicals (student_id,course_id,session_num,session_date,reason,approved) VALUES
(15,1,12,'2025-03-24','Fever - hospital certificate',1),
(15,1,13,'2025-03-31','Fever - hospital certificate',1),
(15,1,14,'2025-04-07','Fever - hospital certificate',1);

-- stu006 (id=16) DSA Theory: 9/15 + 1 medical = 10/15 = 66% < 80% still
INSERT INTO attendance (student_id,course_id,session_number,session_type,status,session_date) VALUES
(16,1,1,'theory','present','2025-01-06'),(16,1,2,'theory','present','2025-01-13'),
(16,1,3,'theory','present','2025-01-20'),(16,1,4,'theory','present','2025-01-27'),
(16,1,5,'theory','present','2025-02-03'),(16,1,6,'theory','present','2025-02-10'),
(16,1,7,'theory','present','2025-02-17'),(16,1,8,'theory','present','2025-02-24'),
(16,1,9,'theory','present','2025-03-03'),(16,1,10,'theory','absent','2025-03-10'),
(16,1,11,'theory','absent','2025-03-17'),(16,1,12,'theory','absent','2025-03-24'),
(16,1,13,'theory','absent','2025-03-31'),(16,1,14,'theory','absent','2025-04-07'),
(16,1,15,'theory','absent','2025-04-14');

INSERT INTO medicals (student_id,course_id,session_num,session_date,reason,approved) VALUES
(16,1,10,'2025-03-10','Hospital admission',1);

-- Add attendance for remaining students for ICT2122 OOP (course_id=5) as extra data
-- stu007-stu015 get varied attendance on OOP theory
INSERT INTO attendance (student_id,course_id,session_number,session_type,status,session_date) VALUES
(17,5,1,'theory','present','2025-01-09'),(17,5,2,'theory','present','2025-01-16'),
(17,5,3,'theory','present','2025-01-23'),(17,5,4,'theory','present','2025-01-30'),
(17,5,5,'theory','present','2025-02-06'),(17,5,6,'theory','present','2025-02-13'),
(17,5,7,'theory','present','2025-02-20'),(17,5,8,'theory','present','2025-02-27'),
(17,5,9,'theory','present','2025-03-06'),(17,5,10,'theory','present','2025-03-13'),
(17,5,11,'theory','present','2025-03-20'),(17,5,12,'theory','present','2025-03-27'),
(17,5,13,'theory','present','2025-04-03'),(17,5,14,'theory','absent','2025-04-10'),
(17,5,15,'theory','absent','2025-04-17'),
(18,5,1,'theory','present','2025-01-09'),(18,5,2,'theory','present','2025-01-16'),
(18,5,3,'theory','absent','2025-01-23'),(18,5,4,'theory','present','2025-01-30'),
(18,5,5,'theory','present','2025-02-06'),(18,5,6,'theory','absent','2025-02-13'),
(18,5,7,'theory','present','2025-02-20'),(18,5,8,'theory','present','2025-02-27'),
(18,5,9,'theory','absent','2025-03-06'),(18,5,10,'theory','present','2025-03-13'),
(18,5,11,'theory','present','2025-03-20'),(18,5,12,'theory','present','2025-03-27'),
(18,5,13,'theory','present','2025-04-03'),(18,5,14,'theory','present','2025-04-10'),
(18,5,15,'theory','present','2025-04-17');

-- ============================================================
-- MARKS (ICT2113 DSA and ICT2122 OOP)
-- ============================================================
INSERT INTO marks (student_id,course_id,exam_type,mark,entered_by) VALUES
-- stu001 DSA
(11,1,'CA1',72,2),(11,1,'CA2',68,2),(11,1,'assignment',80,2),(11,1,'final',75,2),
-- stu002 DSA
(12,1,'CA1',60,2),(12,1,'CA2',55,2),(12,1,'assignment',65,2),(12,1,'final',58,2),
-- stu003 DSA
(13,1,'CA1',50,2),(13,1,'CA2',48,2),(13,1,'assignment',55,2),(13,1,'final',52,2),
-- stu004 DSA – CA below threshold (not eligible)
(14,1,'CA1',30,2),(14,1,'CA2',35,2),(14,1,'assignment',38,2),
-- stu005 DSA
(15,1,'CA1',65,2),(15,1,'CA2',70,2),(15,1,'assignment',75,2),(15,1,'final',68,2),
-- stu006 DSA
(16,1,'CA1',35,2),(16,1,'CA2',38,2),(16,1,'assignment',40,2),
-- OOP marks for stu007-stu009
(17,5,'CA1',78,6),(17,5,'CA2',72,6),(17,5,'assignment',80,6),(17,5,'final',76,6),
(18,5,'CA1',55,6),(18,5,'CA2',60,6),(18,5,'assignment',58,6),(18,5,'final',57,6),
(19,5,'CA1',42,6),(19,5,'CA2',45,6),(19,5,'assignment',48,6),(19,5,'final',44,6);

-- ============================================================
-- NOTICES
-- ============================================================
INSERT INTO notices (title,content,created_by) VALUES
('Welcome to Semester 2 – 2025','All students please check your timetables on the student portal.',1),
('Medical Submission Deadline','Submit all medical certificates before 30th April 2025.',1),
('Final Exam Schedule','Final exams scheduled from 20th May to 1st June 2025.',1),
('Lab Allocation – OOP Practicum','Lab 2 allocated for ICT2132 Thursday sessions.',1);
