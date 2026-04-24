

USE faculty_system;

-- ============================================================
-- USERS: 1 Admin, 5 Lecturers, 4 Tech Officers, 20 Students
-- ============================================================

INSERT INTO users (username, password, full_name, email, phone, role, department) VALUES
-- Admin
('admin01',  'admin123',   'Dr. Nimal Perera',      'nimal@ruh.ac.lk',     '0771111111', 'admin',        'Administration'),

-- Lecturers (5)
('lec01',    'lec123',     'Dr. Saman Fernando',    'saman@ruh.ac.lk',     '0772222201', 'lecturer',     'ICT'),
('lec02',    'lec123',     'Dr. Amali Jayawardena', 'amali@ruh.ac.lk',     '0772222202', 'lecturer',     'ICT'),
('lec03',    'lec123',     'Mr. Ruwan Silva',       'ruwan@ruh.ac.lk',     '0772222203', 'lecturer',     'ICT'),
('lec04',    'lec123',     'Ms. Dilini Rathnayake', 'dilini@ruh.ac.lk',    '0772222204', 'lecturer',     'ICT'),
('lec05',    'lec123',     'Dr. Kasun Wickrama',    'kasun@ruh.ac.lk',     '0772222205', 'lecturer',     'ICT'),

-- Tech Officers (4)
('tech01',   'tech123',    'Mr. Pradeep Bandara',   'pradeep@ruh.ac.lk',   '0773333301', 'tech_officer', 'ICT'),
('tech02',   'tech123',    'Ms. Sanduni Herath',    'sanduni@ruh.ac.lk',   '0773333302', 'tech_officer', 'ICT'),
('tech03',   'tech123',    'Mr. Chamara Kumara',    'chamara@ruh.ac.lk',   '0773333303', 'tech_officer', 'ICT'),
('tech04',   'tech123',    'Ms. Ishara Mendis',     'ishara@ruh.ac.lk',    '0773333304', 'tech_officer', 'ICT'),

-- Students (20) — includes repeat/batch-missed students
('stu001',   'stu123',     'Ashan Perera',          'ashan@stu.ruh.ac.lk', '0774444401', 'student',      'ICT'),
('stu002',   'stu123',     'Bimsara Karunarathna',  'bims@stu.ruh.ac.lk',  '0774444402', 'student',      'ICT'),
('stu003',   'stu123',     'Chathuri Dissanayake',  'chat@stu.ruh.ac.lk',  '0774444403', 'student',      'ICT'),
('stu004',   'stu123',     'Dinusha Weerasekara',   'dinu@stu.ruh.ac.lk',  '0774444404', 'student',      'ICT'),
('stu005',   'stu123',     'Erandi Gunasekara',     'eran@stu.ruh.ac.lk',  '0774444405', 'student',      'ICT'),
('stu006',   'stu123',     'Fathima Rameez',        'fath@stu.ruh.ac.lk',  '0774444406', 'student',      'ICT'),
('stu007',   'stu123',     'Gayan Samaraweera',     'gaya@stu.ruh.ac.lk',  '0774444407', 'student',      'ICT'),
('stu008',   'stu123',     'Hasini Liyanage',       'hasi@stu.ruh.ac.lk',  '0774444408', 'student',      'ICT'),
('stu009',   'stu123',     'Ishan Rajapaksha',      'isha@stu.ruh.ac.lk',  '0774444409', 'student',      'ICT'),
('stu010',   'stu123',     'Janitha Seneviratne',   'jani@stu.ruh.ac.lk',  '0774444410', 'student',      'ICT'),
('stu011',   'stu123',     'Kasun Madusanka',       'kasu@stu.ruh.ac.lk',  '0774444411', 'student',      'ICT'),
('stu012',   'stu123',     'Lasantha Jayasuriya',   'lasa@stu.ruh.ac.lk',  '0774444412', 'student',      'ICT'),
('stu013',   'stu123',     'Malsha Wickramasinghe', 'mals@stu.ruh.ac.lk',  '0774444413', 'student',      'ICT'),
('stu014',   'stu123',     'Nadeesha Kumari',       'nade@stu.ruh.ac.lk',  '0774444414', 'student',      'ICT'),
('stu015',   'stu123',     'Oshadha Peiris',        'osha@stu.ruh.ac.lk',  '0774444415', 'student',      'ICT'),
('stu016',   'stu123',     'Piumal Gamage',         'pium@stu.ruh.ac.lk',  '0774444416', 'student',      'ICT'),
('stu017',   'stu123',     'Ravindu Rathnasiri',    'ravi@stu.ruh.ac.lk',  '0774444417', 'student',      'ICT'),
('stu018',   'stu123',     'Sachini Vithanage',     'sach@stu.ruh.ac.lk',  '0774444418', 'student',      'ICT'),
('stu019',   'stu123',     'Tharaka Senanayake',    'thar@stu.ruh.ac.lk',  '0774444419', 'student',      'ICT'),
('stu020',   'stu123',     'Uthpala Nanayakkara',   'uth@stu.ruh.ac.lk',   '0774444420', 'student',      'ICT');

-- ============================================================
-- COURSES (based on typical ICT Semester curriculum)
-- ============================================================
INSERT INTO courses (course_code, course_name, department, credit_theory, credit_practical, lecturer_id, semester) VALUES
('ICT2132', 'Object Oriented Programming',     'ICT', 2, 1, 7, 2),
('ICT2112', 'Data Structures and Algorithms',  'ICT', 3, 0, 8, 2),
('ICT2122', 'Database Management Systems',     'ICT', 2, 1, 9, 2),
('ICT2142', 'Computer Networks',               'ICT', 2, 1, 10, 2),
('ICT2152', 'Software Engineering',            'ICT', 3, 0, 11, 2);

-- ============================================================
-- ENROLL ALL 20 STUDENTS IN ALL 5 COURSES
-- ============================================================
INSERT INTO student_courses (student_id, course_id)
SELECT u.id, c.id
FROM users u, courses c
WHERE u.role = 'student';

-- ============================================================
-- ATTENDANCE DATA (15 sessions each, theory + practical)
-- Scenarios covered:
--   stu001..005 = >80% attendance
--   stu006      = exactly 80%
--   stu007..010 = <80% without medicals
--   stu011..015 = >80% with medicals
--   stu016..020 = <80% with medicals
-- ============================================================



-- STUDENT stu001 (id=12) - Theory sessions OOP: 13/15 present = 86.7% > 80%
INSERT INTO attendance (student_id, course_id, session_number, session_type, status) VALUES
                                                                                         (12,1,1,'theory','present'),(12,1,2,'theory','present'),(12,1,3,'theory','present'),
                                                                                         (12,1,4,'theory','present'),(12,1,5,'theory','present'),(12,1,6,'theory','present'),
                                                                                         (12,1,7,'theory','present'),(12,1,8,'theory','present'),(12,1,9,'theory','present'),
                                                                                         (12,1,10,'theory','present'),(12,1,11,'theory','present'),(12,1,12,'theory','present'),
                                                                                         (12,1,13,'theory','present'),(12,1,14,'theory','absent'),(12,1,15,'theory','absent');

-- STUDENT stu006 (id=17) - Theory sessions OOP: 12/15 = 80% exactly
INSERT INTO attendance (student_id, course_id, session_number, session_type, status) VALUES
                                                                                         (17,1,1,'theory','present'),(17,1,2,'theory','present'),(17,1,3,'theory','present'),
                                                                                         (17,1,4,'theory','present'),(17,1,5,'theory','present'),(17,1,6,'theory','present'),
                                                                                         (17,1,7,'theory','present'),(17,1,8,'theory','present'),(17,1,9,'theory','present'),
                                                                                         (17,1,10,'theory','present'),(17,1,11,'theory','present'),(17,1,12,'theory','present'),
                                                                                         (17,1,13,'theory','absent'),(17,1,14,'theory','absent'),(17,1,15,'theory','absent');

-- STUDENT stu007 (id=18) - Theory sessions OOP: 10/15 = 66.7% < 80% (no medical)
INSERT INTO attendance (student_id, course_id, session_number, session_type, status) VALUES
                                                                                         (18,1,1,'theory','present'),(18,1,2,'theory','present'),(18,1,3,'theory','present'),
                                                                                         (18,1,4,'theory','present'),(18,1,5,'theory','present'),(18,1,6,'theory','present'),
                                                                                         (18,1,7,'theory','present'),(18,1,8,'theory','present'),(18,1,9,'theory','present'),
                                                                                         (18,1,10,'theory','present'),(18,1,11,'theory','absent'),(18,1,12,'theory','absent'),
                                                                                         (18,1,13,'theory','absent'),(18,1,14,'theory','absent'),(18,1,15,'theory','absent');

-- STUDENT stu011 (id=22) - Theory: 11/15 = 73% but has approved medicals = effectively >80%
INSERT INTO attendance (student_id, course_id, session_number, session_type, status) VALUES
                                                                                         (22,1,1,'theory','present'),(22,1,2,'theory','present'),(22,1,3,'theory','present'),
                                                                                         (22,1,4,'theory','present'),(22,1,5,'theory','present'),(22,1,6,'theory','present'),
                                                                                         (22,1,7,'theory','present'),(22,1,8,'theory','present'),(22,1,9,'theory','present'),
                                                                                         (22,1,10,'theory','present'),(22,1,11,'theory','present'),(22,1,12,'theory','absent'),
                                                                                         (22,1,13,'theory','absent'),(22,1,14,'theory','absent'),(22,1,15,'theory','absent');

INSERT INTO medicals (student_id, course_id, session_num, reason, approved) VALUES
                                                                                (22,1,12,'Fever - medical certificate attached',1),
                                                                                (22,1,13,'Fever - medical certificate attached',1),
                                                                                (22,1,14,'Fever - medical certificate attached',1);
-- After medicals: (11+3)/15 = 93.3% > 80%

-- STUDENT stu016 (id=27) - Theory: 9/15 = 60% < 80% even with 1 medical
INSERT INTO attendance (student_id, course_id, session_number, session_type, status) VALUES
                                                                                         (27,1,1,'theory','present'),(27,1,2,'theory','present'),(27,1,3,'theory','present'),
                                                                                         (27,1,4,'theory','present'),(27,1,5,'theory','present'),(27,1,6,'theory','present'),
                                                                                         (27,1,7,'theory','present'),(27,1,8,'theory','present'),(27,1,9,'theory','present'),
                                                                                         (27,1,10,'theory','absent'),(27,1,11,'theory','absent'),(27,1,12,'theory','absent'),
                                                                                         (27,1,13,'theory','absent'),(27,1,14,'theory','absent'),(27,1,15,'theory','absent');

INSERT INTO medicals (student_id, course_id, session_num, reason, approved) VALUES
    (27,1,10,'Hospital admission - certificate attached',1);
-- After medical: (9+1)/15 = 66.7% still < 80%

-- ============================================================
-- MARKS DATA (for OOP course, all exam types out of 100)
-- CA eligibility: CA average >= 40 to sit final exam
-- ============================================================
INSERT INTO marks (student_id, course_id, exam_type, mark, entered_by) VALUES
-- stu001 (id=12): Good student
(12,1,'CA1',72.00,7),(12,1,'CA2',68.00,7),(12,1,'assignment',80.00,7),(12,1,'final',75.00,7),
-- stu006 (id=17): Average student
(17,1,'CA1',50.00,7),(17,1,'CA2',45.00,7),(17,1,'assignment',55.00,7),(17,1,'final',52.00,7),
-- stu007 (id=18): Struggling student - CA avg = 35 (not eligible!)
(18,1,'CA1',30.00,7),(18,1,'CA2',38.00,7),(18,1,'assignment',37.00,7),
-- stu011 (id=22): Good student with medical
(22,1,'CA1',65.00,7),(22,1,'CA2',70.00,7),(22,1,'assignment',75.00,7),(22,1,'final',68.00,7),
-- stu016 (id=27): Below CA threshold
(27,1,'CA1',35.00,7),(27,1,'CA2',33.00,7),(27,1,'assignment',40.00,7);

-- ============================================================
-- NOTICES
-- ============================================================
INSERT INTO notices (title, content, created_by) VALUES
                                                     ('Welcome to Semester 2 2026',  'All students please check your timetables on the portal.', 1),
                                                     ('Exam Schedule Released',       'Final exams will be held from 20th May to 1st June 2026.',  1),
                                                     ('Medical Submission Deadline',  'Submit all medical certificates before 30th April 2026.',   1),
                                                     ('Lab Allocation Notice',        'Lab sessions for OOP practicum assigned. Check timetable.', 1);

-- ============================================================
-- TIMETABLE
-- ============================================================
INSERT INTO timetables (course_id, day_of_week, start_time, end_time, room, department) VALUES
                                                                                            (1,'Monday',    '08:00','10:00','LH1','ICT'),
                                                                                            (1,'Wednesday', '14:00','16:00','Lab3','ICT'),
                                                                                            (2,'Tuesday',   '08:00','11:00','LH2','ICT'),
                                                                                            (3,'Thursday',  '08:00','10:00','LH1','ICT'),
                                                                                            (3,'Friday',    '14:00','16:00','Lab2','ICT'),
                                                                                            (4,'Monday',    '13:00','15:00','LH3','ICT'),
                                                                                            (5,'Friday',    '08:00','11:00','LH2','ICT');
