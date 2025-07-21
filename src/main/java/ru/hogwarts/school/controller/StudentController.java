package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    // Шаг 1: Имена студентов на 'A' в верхнем регистре
    @GetMapping("/names-starting-with-a")
    public ResponseEntity<List<String>> getNamesStartingWithA() {
        logger.info("Запрос имен студентов, начинающихся на 'A'");
        List<String> names = studentService.getStudentNamesStartingWithA();
        logger.debug("Найдено {} имен, начинающихся на 'A'", names.size());
        return ResponseEntity.ok(names);
    }
    // Шаг 2: Средний возраст студентов (через findAll)
    @GetMapping("/average-age-with-find-all")
    public ResponseEntity<Double> getAverageAgeWithFindAll() {
        logger.info("Запрос среднего возраста студентов (через findAll)");
        double average = studentService.getAverageAgeWithFindAll();
        logger.debug("Средний возраст через findAll: {}", average);
        return ResponseEntity.ok(average);
    }
    // Шаг 4: Оптимизированная сумма
    @GetMapping("/sum")
    public ResponseEntity<Long> calculateSum() {
        logger.info("Запрос вычисления суммы");
        long sum = studentService.calculateSum();
        logger.debug("Вычисленная сумма: {}", sum);
        return ResponseEntity.ok(sum);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        logger.info("Запрос студента по ID: {}", id);
        try {
            Student student = studentService.getStudent(id);
            logger.debug("Найден студент: {}", student);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            logger.error("Ошибка при получении студента: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        logger.info("Запрос на создание студента: {}", student);
        try {
            Student savedStudent = studentService.addStudent(student);
            logger.info("Создан новый студент: {}", savedStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (Exception e) {
            logger.error("Ошибка создания студента: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        logger.info("Запрос на обновление студента ID: {}", id);
        try {
            Student updatedStudent = studentService.updateStudent(id, student);
            logger.info("Обновлен студент: {}", updatedStudent);
            return ResponseEntity.ok(updatedStudent);
        } catch (Exception e) {
            logger.error("Ошибка обновления студента: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeStudent(@PathVariable Long id) {
        logger.info("Запрос на удаление студента ID: {}", id);
        try {
            studentService.removeStudent(id);
            logger.info("Студент с ID {} удален", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Ошибка удаления студента: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with id: " + id);
        }
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        logger.info("Запрос факультета студента ID: {}", id);
        try {
            Faculty faculty = studentService.getStudentFaculty(id);
            logger.debug("Найден факультет: {}", faculty);
            return ResponseEntity.ok(faculty);
        } catch (Exception e) {
            logger.error("Ошибка получения факультета: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/by-age")
    public ResponseEntity<List<Student>> findStudentsByAge(@RequestParam int age) {
        logger.info("Запрос студентов возраста: {}", age);
        List<Student> students = studentService.findStudentsByAge(age);
        logger.debug("Найдено {} студентов возраста {}", students.size(), age);
        return students.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students);
    }

    @GetMapping("/by-age-between")
    public ResponseEntity<List<Student>> findStudentsByAgeBetween(
            @RequestParam int minAge,
            @RequestParam int maxAge) {
        logger.info("Запрос студентов в диапазоне: {} - {}", minAge, maxAge);
        List<Student> students = studentService.findStudentsByAgeBetween(minAge, maxAge);
        logger.debug("Найдено {} студентов в диапазоне {} - {}", students.size(), minAge, maxAge);
        return students.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getTotalCount() {
        logger.info("Запрос общего количества студентов");
        int count = studentService.getTotalCountOfStudents();
        logger.debug("Общее количество студентов: {}", count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        logger.info("Запрос среднего возраста студентов");
        double average = studentService.getAverageAge();
        logger.debug("Средний возраст: {}", average);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        logger.info("Запрос последних 5 студентов");
        List<Student> students = studentService.findLastFiveStudents();
        logger.debug("Получено {} последних студентов", students.size());
        return ResponseEntity.ok(students);
    }
}