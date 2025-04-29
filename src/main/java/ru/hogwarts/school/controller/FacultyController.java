package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacultyById(@PathVariable Long id) {
        try {
            Faculty faculty = facultyService.getFaculty(id);
            return ResponseEntity.ok(faculty);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Faculty not found with id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        Faculty addedFaculty = facultyService.addFaculty(faculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFaculty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFaculty(@PathVariable Long id,
                                           @RequestBody Faculty faculty) {
        try {
            Faculty updatedFaculty = facultyService.updateFaculty(id, faculty);
            return ResponseEntity.ok(updatedFaculty);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Faculty not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFaculty(@PathVariable Long id) {
        try {
            facultyService.removeFaculty(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Faculty not found with id: " + id);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Faculty>> findAllFacultiesByColor(@RequestParam String color) {
        List<Faculty> faculties = facultyService.findAllFacultiesByColor(color);
        return faculties.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(faculties);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Faculty>> searchFaculties(@RequestParam String query) {
        List<Faculty> faculties = facultyService.searchFaculties(query);
        return faculties.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getFacultyStudents(@PathVariable Long id) {
        try {
            List<Student> students = facultyService.getFacultyStudents(id);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
