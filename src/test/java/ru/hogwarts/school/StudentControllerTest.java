package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    void cleanDatabase() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void getStudentById_shouldReturnStudentWhenExists() {
        // Arrange
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);

        ResponseEntity<Student> responseEntity = restTemplate.postForEntity(
                "/student", student, Student.class);
        Student created = responseEntity.getBody();

        // Act
        ResponseEntity<Student> response = restTemplate.getForEntity(
                "/student/" + created.getId(), Student.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(created.getId(), response.getBody().getId());
    }

    @Test
    void addStudent_shouldCreateNewStudent() {

        Student student = new Student();
        student.setName("New Student");
        student.setAge(21);


        ResponseEntity<Student> response = restTemplate.postForEntity(
                "/student", student, Student.class);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void updateStudent_shouldUpdateExistingStudent() {

        Student student = new Student();
        student.setName("Original Name");
        student.setAge(22);
        ResponseEntity<Student> responseEntity = restTemplate.postForEntity(
                "/student", student, Student.class);
        Student created = responseEntity.getBody();

        created.setName("Updated Name");


        ResponseEntity<Student> response = restTemplate.exchange(
                "/student/" + created.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(created),
                Student.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    void removeStudent_shouldDeleteStudent() {

        Student student = new Student();
        student.setName("To be deleted");
        student.setAge(23);
        ResponseEntity<Student> responseEntity = restTemplate.postForEntity(
                "/student", student, Student.class);
        Student created = responseEntity.getBody();


        ResponseEntity<Void> response = restTemplate.exchange(
                "/student/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void findStudentsByAge_shouldReturnStudentsWithGivenAge() {

        Student student = new Student();
        student.setName("Age Test");
        student.setAge(25);
        restTemplate.postForEntity("/student", student, Student.class);


        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "/student/by-age?age=25", Student[].class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void findStudentsByAgeBetween_shouldReturnStudentsInAgeRange() {

        Student student1 = new Student();
        student1.setName("Age Range 1");
        student1.setAge(18);
        restTemplate.postForEntity("/student", student1, Student.class);

        Student student2 = new Student();
        student2.setName("Age Range 2");
        student2.setAge(22);
        restTemplate.postForEntity("/student", student2, Student.class);


        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "/student/by-age-between?minAge=20&maxAge=25", Student[].class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
        assertEquals(22, response.getBody()[0].getAge());
    }
}
