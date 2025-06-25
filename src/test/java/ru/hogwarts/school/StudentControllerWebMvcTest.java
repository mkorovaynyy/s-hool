package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void getStudent_shouldReturnStudentWhenExists() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(17);

        when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void addStudent_shouldCreateNewStudent() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("Hermione Granger");
        newStudent.setAge(17);

        Student createdStudent = new Student();
        createdStudent.setId(2L);
        createdStudent.setName("Hermione Granger");
        createdStudent.setAge(17);

        when(studentService.addStudent(any(Student.class))).thenReturn(createdStudent);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Hermione Granger"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void updateStudent_shouldUpdateExistingStudent() throws Exception {
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setName("Harry Potter Updated");
        updatedStudent.setAge(18);

        when(studentService.updateStudent(eq(1L), any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter Updated"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void removeStudent_shouldDeleteStudent() throws Exception {
        // Arrange
        // Правильный способ мокирования void-метода
        doNothing().when(studentService).removeStudent(1L);

        // Act & Assert
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());

        // Дополнительная проверка, что метод был вызван
        verify(studentService, times(1)).removeStudent(1L);
    }

    @Test
    void getStudentFaculty_shouldReturnFacultyWhenExists() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        when(studentService.getStudentFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void findStudentsByAge_shouldReturnStudents() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Harry Potter");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Ron Weasley");
        student2.setAge(17);

        when(studentService.findStudentsByAge(17)).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/student/by-age?age=17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Ron Weasley"));
    }

    @Test
    void findStudentsByAgeBetween_shouldReturnStudentsInRange() throws Exception {
        Student student = new Student();
        student.setId(3L);
        student.setName("Neville Longbottom");
        student.setAge(16);

        when(studentService.findStudentsByAgeBetween(15, 17)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student/by-age-between?minAge=15&maxAge=17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Neville Longbottom"))
                .andExpect(jsonPath("$[0].age").value(16));
    }
}