package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping({"id"})
    public ResponseEntity<Student> getStudent(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(studentService.getStudent(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        studentService.addStudent(student);
        return ResponseEntity.ok(student);
    }
    @PutMapping({"id"})
    public ResponseEntity<Student> updateStudent(@RequestParam Long id, @RequestBody Student student) {
        try {
            studentService.updateStudent(id, student);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return  ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping({"id"})
    public ResponseEntity<Student> removeStudent(@RequestParam Long id) {
        try {
            studentService.removeStudent(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Student>> findStudentsByAge(@RequestParam(required = false) int age) {
         try {
             return ResponseEntity.ok(studentService.findStudentsByAge(age));
         } catch (Exception e) {
             return ResponseEntity.notFound().build();
         }
    }
}
