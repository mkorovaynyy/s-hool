package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Transactional
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Вызван метод создания факультета");
        Faculty addedFaculty = facultyRepository.save(faculty);
        logger.debug("Создан факультет: {}", addedFaculty);
        return addedFaculty;
    }

    public Faculty getFaculty(Long id) {
        logger.debug("Вызван метод получения факультета по ID: {}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Факультет с ID {} не найден", id);
                    return new EntityNotFoundException("Факультет не найден");
                });
    }

    @Transactional
    public void removeFaculty(Long id) {
        logger.info("Вызван метод удаления факультета по ID: {}", id);
        if (!facultyRepository.existsById(id)) {
            logger.warn("Попытка удаления несуществующего факультета с ID: {}", id);
            throw new EntityNotFoundException("Факультет не найден");
        }
        facultyRepository.deleteById(id);
        logger.debug("Факультет с ID {} удален", id);
    }

    @Transactional
    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.info("Вызван метод обновления факультета по ID: {}", id);
        if (!facultyRepository.existsById(id)) {
            logger.warn("Попытка обновления несуществующего факультета с ID: {}", id);
            throw new EntityNotFoundException("Факультет не найден");
        }
        faculty.setId(id);
        Faculty updatedFaculty = facultyRepository.save(faculty);
        logger.debug("Обновлен факультет: {}", updatedFaculty);
        return updatedFaculty;
    }

    public List<Faculty> findAllFacultiesByColor(String color) {
        logger.info("Вызван метод поиска факультетов по цвету: {}", color);
        List<Faculty> faculties = facultyRepository.findByColor(color);
        logger.debug("Найдено {} факультетов цвета {}", faculties.size(), color);
        return faculties;
    }

    public List<Faculty> searchFaculties(String query) {
        logger.info("Вызван метод поиска факультетов по запросу: {}", query);
        List<Faculty> faculties = facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(query, query);
        logger.debug("Найдено {} факультетов по запросу '{}'", faculties.size(), query);
        return faculties;
    }

    public List<Student> getFacultyStudents(Long id) {
        logger.info("Вызван метод получения студентов факультета по ID: {}", id);
        Faculty faculty = getFaculty(id);
        List<Student> students = faculty.getStudents();
        logger.debug("Найдено {} студентов на факультете {}", students.size(), faculty.getName());
        return students;
    }

    // Шаг 3: Самое длинное название факультета
    public String getLongestFacultyName() {
        logger.info("Поиск самого длинного названия факультета");
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow(() -> {
                    logger.warn("Факультеты не найдены");
                    return new RuntimeException("No faculties found");
                });
    }
}