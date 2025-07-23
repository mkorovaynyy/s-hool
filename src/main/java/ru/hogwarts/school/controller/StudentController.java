package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        logger.info("Запрос студента по ID: {}", id);
        return studentService.getStudent(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@RequestBody Student student) {
        logger.info("Запрос на создание студента: {}", student);
        return studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        logger.info("Запрос на обновление студента ID: {}", id);
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void removeStudent(@PathVariable Long id) {
        logger.info("Запрос на удаление студента ID: {}", id);
        studentService.removeStudent(id);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getStudentFaculty(@PathVariable Long id) {
        logger.info("Запрос факультета студента ID: {}", id);
        return studentService.getStudentFaculty(id);
    }

    @GetMapping("/by-age")
    public List<Student> findStudentsByAge(@RequestParam int age) {
        logger.info("Запрос студентов возраста: {}", age);
        return studentService.findStudentsByAge(age);
    }

    @GetMapping("/by-age-between")
    public List<Student> findStudentsByAgeBetween(
            @RequestParam int minAge,
            @RequestParam int maxAge) {
        logger.info("Запрос студентов в диапазоне: {} - {}", minAge, maxAge);
        return studentService.findStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/count")
    public int getTotalCount() {
        logger.info("Запрос общего количества студентов");
        return studentService.getTotalCountOfStudents();
    }

    @GetMapping("/average-age")
    public double getAverageAge() {
        logger.info("Запрос среднего возраста студентов");
        return studentService.getAverageAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        logger.info("Запрос последних 5 студентов");
        return studentService.findLastFiveStudents();
    }

    // Новые методы для задания 4.5
    @GetMapping("/names-starting-with-a")
    public List<String> getNamesStartingWithA() {
        logger.info("Запрос имен студентов, начинающихся на 'A'");
        return studentService.getStudentNamesStartingWithA();
    }

    @GetMapping("/average-age-with-find-all")
    public double getAverageAgeWithFindAll() {
        logger.info("Запрос среднего возраста студентов (через findAll)");
        return studentService.getAverageAgeWithFindAll();
    }

    @GetMapping("/sum")
    public long calculateSum() {
        logger.info("Запрос вычисления суммы");
        return studentService.calculateSum();
    }
}