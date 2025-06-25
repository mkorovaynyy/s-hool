package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void getFacultyById_shouldReturnFacultyWhenExists() {

        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        Faculty savedFaculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/" + savedFaculty.getId(), Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedFaculty.getId(), response.getBody().getId());
        assertEquals("Gryffindor", response.getBody().getName());
        assertEquals("Red", response.getBody().getColor());
    }

    @Test
    void getFacultyById_shouldReturnNotFoundWhenNotExists() {

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/faculty/999", String.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Faculty not found"));
    }

    @Test
    void addFaculty_shouldCreateNewFaculty() {

        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");


        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "/faculty", faculty, Faculty.class);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Faculty createdFaculty = response.getBody();
        assertNotNull(createdFaculty);
        assertNotNull(createdFaculty.getId());
        assertEquals("Ravenclaw", createdFaculty.getName());
        assertEquals("Blue", createdFaculty.getColor());
    }

    @Test
    void updateFaculty_shouldUpdateExistingFaculty() {

        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("Green");
        Faculty savedFaculty = facultyRepository.save(faculty);

        savedFaculty.setColor("Silver");


        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty/" + savedFaculty.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(savedFaculty),
                Faculty.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty updatedFaculty = response.getBody();
        assertNotNull(updatedFaculty);
        assertEquals("Silver", updatedFaculty.getColor());
        assertEquals("Slytherin", updatedFaculty.getName());
    }

    @Test
    void updateFaculty_shouldReturnNotFoundWhenNotExists() {

        Faculty faculty = new Faculty();
        faculty.setId(999L);
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");


        ResponseEntity<String> response = restTemplate.exchange(
                "/faculty/999",
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                String.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Faculty not found"));
    }

    @Test
    void removeFaculty_shouldDeleteFaculty() {

        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");
        Faculty savedFaculty = facultyRepository.save(faculty);


        ResponseEntity<Void> response = restTemplate.exchange(
                "/faculty/" + savedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(facultyRepository.existsById(savedFaculty.getId()));
    }

    @Test
    void findAllFacultiesByColor_shouldReturnFaculties() {

        Faculty faculty1 = new Faculty();
        faculty1.setName("Gryffindor");
        faculty1.setColor("Red");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Ravenclaw");
        faculty2.setColor("Blue");
        facultyRepository.save(faculty2);


        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "/faculty?color=Blue", Faculty[].class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty[] faculties = response.getBody();
        assertNotNull(faculties);
        assertEquals(1, faculties.length);
        assertEquals("Ravenclaw", faculties[0].getName());
        assertEquals("Blue", faculties[0].getColor());
    }


    @Test
    void getFacultyStudents_shouldReturnNotFoundForInvalidFaculty() {

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/faculty/999/students", String.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}