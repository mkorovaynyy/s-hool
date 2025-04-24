package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
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
        return studentRepository.save(student);
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
}