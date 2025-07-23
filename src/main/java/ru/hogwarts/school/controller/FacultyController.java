package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Long id) {
        logger.info("Запрос факультета по ID: {}", id);
        return facultyService.getFaculty(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        logger.info("Запрос на создание факультета: {}", faculty);
        return facultyService.addFaculty(faculty);
    }

    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        logger.info("Запрос на обновление факультета ID: {}", id);
        return facultyService.updateFaculty(id, faculty);
    }

    @DeleteMapping("/{id}")
    public void removeFaculty(@PathVariable Long id) {
        logger.info("Запрос на удаление факультета ID: {}", id);
        facultyService.removeFaculty(id);
    }

    @GetMapping()
    public List<Faculty> findAllFacultiesByColor(@RequestParam String color) {
        logger.info("Запрос факультетов по цвету: {}", color);
        return facultyService.findAllFacultiesByColor(color);
    }

    @GetMapping("/search")
    public List<Faculty> searchFaculties(@RequestParam String query) {
        logger.info("Поиск факультетов по запросу: {}", query);
        return facultyService.searchFaculties(query);
    }

    @GetMapping("/{id}/students")
    public List<Student> getFacultyStudents(@PathVariable Long id) {
        logger.info("Запрос студентов факультета ID: {}", id);
        return facultyService.getFacultyStudents(id);
    }

    // Новый метод для задания 4.5
    @GetMapping("/longest-name")
    public String getLongestFacultyName() {
        logger.info("Запрос самого длинного названия факультета");
        return facultyService.getLongestFacultyName();
    }
}
