package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.*;
import java.util.List;

/**
 * Сервисный класс для сущности "студент" с методами CRUD
 */
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student addStudent(Student student) {
        studentRepository.save(student);
        return student;
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
    }

    @Transactional
    public void removeStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public Student updateStudent(Long id, Student student) {
        student.setId(id);
        return studentRepository.save(student);
    }

    public List<Student> findStudentsByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    @Override
    public String toString() {
        return "StudentService{" +
                "studentRepository=" + studentRepository +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudentService that = (StudentService) o;
        return Objects.equals(studentRepository, that.studentRepository);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(studentRepository);
    }
}
