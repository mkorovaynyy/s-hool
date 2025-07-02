package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entities.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);
    List<Student> findByAgeBetween(int minAge, int maxAge);
    List<Student> findAllByFacultyId(Long facultyId);
    // Количество всех студентов
    @Query("SELECT COUNT(s) FROM Student s")
    int getTotalCountOfStudents();

    // Средний возраст студентов
    @Query("SELECT AVG(s.age) FROM Student s")
    double getAverageAge();

    // 5 последних студентов (с наибольшими ID)
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}
