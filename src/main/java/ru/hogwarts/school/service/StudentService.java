package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student addStudent(Student student) {
        try {
            if (student.getName() == null || student.getName().isBlank()) {
                throw new IllegalArgumentException("Student name cannot be empty");
            }
            if (student.getAge() <= 0) {
                throw new IllegalArgumentException("Age must be positive");
            }
            return studentRepository.save(student);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating student: " + e.getMessage()
            );
        }
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    @Transactional
    public void removeStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public Student updateStudent(Long id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found");
        }
        student.setId(id);
        return studentRepository.save(student);
    }

    public List<Student> findStudentsByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public List<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }
}