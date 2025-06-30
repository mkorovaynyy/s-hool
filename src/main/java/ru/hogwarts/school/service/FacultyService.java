package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

/**
 * Сервисный класс для сущности "факультет" с методами CRUD
 */
@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Transactional
    public Faculty addFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
        return faculty;
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));
    }

    @Transactional
    public void removeFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    @Transactional
    public Faculty updateFaculty(Long id, Faculty faculty) {
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findAllFacultiesByColor(String color) {
        return facultyRepository.findByColor(color);
    }
    public List<Faculty> searchFaculties(String query) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(query, query);
    }
    public List<Student> getFacultyStudents(Long id) {
        Faculty faculty = getFaculty(id);
        return faculty.getStudents();
    }
}
