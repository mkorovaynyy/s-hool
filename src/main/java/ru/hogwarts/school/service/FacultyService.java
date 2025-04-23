package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
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

    public long addFaculty(@NotNull Faculty faculty) {
        facultyRepository.save(faculty);
        return faculty.getId();
    }

    public Faculty getFaculty(Long id) {
     return facultyRepository.findById(id).get();
    }

    public void removeFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findAllFacultiesByColor(String color) {
      return facultyRepository.findByColor(color);
    }
}
