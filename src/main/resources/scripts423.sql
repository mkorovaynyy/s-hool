-- 1. Студенты с названиями факультетов
SELECT s.name AS student_name,
       s.age,
       f.name AS faculty_name
FROM student s
         LEFT JOIN faculty f ON s.faculty_id = f.id;

-- 2. Студенты с аватарками
SELECT s.name AS student_name,
       s.age
FROM student s
         INNER JOIN avatar a ON s.id = a.student_id;