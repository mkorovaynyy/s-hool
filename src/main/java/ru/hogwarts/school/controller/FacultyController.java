package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    // Шаг 3: Самое длинное название факультета
    @GetMapping("/longest-name")
    public ResponseEntity<String> getLongestFacultyName() {
        logger.info("Запрос самого длинного названия факультета");
        String longestName = facultyService.getLongestFacultyName();
        logger.debug("Самое длинное название: {}", longestName);
        return ResponseEntity.ok(longestName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacultyById(@PathVariable Long id) {
        logger.info("Запрос факультета по ID: {}", id);
        try {
            Faculty faculty = facultyService.getFaculty(id);
            logger.debug("Найден факультет: {}", faculty);
            return ResponseEntity.ok(faculty);
        } catch (Exception e) {
            logger.error("Ошибка получения факультета: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Faculty not found with id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        logger.info("Запрос на создание факультета: {}", faculty);
        Faculty addedFaculty = facultyService.addFaculty(faculty);
        logger.info("Создан новый факультет: {}", addedFaculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFaculty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFaculty(@PathVariable Long id,
                                           @RequestBody Faculty faculty) {
        logger.info("Запрос на обновление факультета ID: {}", id);
        try {
            Faculty updatedFaculty = facultyService.updateFaculty(id, faculty);
            logger.info("Обновлен факультет: {}", updatedFaculty);
            return ResponseEntity.ok(updatedFaculty);
        } catch (Exception e) {
            logger.error("Ошибка обновления факультета: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Faculty not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFaculty(@PathVariable Long id) {
        logger.info("Запрос на удаление факультета ID: {}", id);
        try {
            facultyService.removeFaculty(id);
            logger.info("Факультет с ID {} удален", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Ошибка удаления факультета: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Faculty not found with id: " + id);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Faculty>> findAllFacultiesByColor(@RequestParam String color) {
        logger.info("Запрос факультетов по цвету: {}", color);
        List<Faculty> faculties = facultyService.findAllFacultiesByColor(color);
        logger.debug("Найдено {} факультетов цвета {}", faculties.size(), color);
        return faculties.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(faculties);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Faculty>> searchFaculties(@RequestParam String query) {
        logger.info("Поиск факультетов по запросу: {}", query);
        List<Faculty> faculties = facultyService.searchFaculties(query);
        logger.debug("Найдено {} факультетов по запросу '{}'", faculties.size(), query);
        return faculties.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getFacultyStudents(@PathVariable Long id) {
        logger.info("Запрос студентов факультета ID: {}", id);
        try {
            List<Student> students = facultyService.getFacultyStudents(id);
            logger.debug("Найдено {} студентов факультета", students.size());
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            logger.error("Ошибка получения студентов факультета: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
