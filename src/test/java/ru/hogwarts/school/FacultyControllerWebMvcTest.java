package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @Test
    void getFacultyById_shouldReturnFacultyWhenExists() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        when(facultyService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void addFaculty_shouldCreateNewFaculty() throws Exception {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Ravenclaw");
        newFaculty.setColor("Blue");

        Faculty createdFaculty = new Faculty();
        createdFaculty.setId(2L);
        createdFaculty.setName("Ravenclaw");
        createdFaculty.setColor("Blue");

        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(createdFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFaculty)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Ravenclaw"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    void updateFaculty_shouldUpdateExistingFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(1L);
        updatedFaculty.setName("Slytherin Updated");
        updatedFaculty.setColor("Green");

        when(facultyService.updateFaculty(eq(1L), any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Slytherin Updated"))
                .andExpect(jsonPath("$.color").value("Green"));
    }

    @Test
    void findAllFacultiesByColor_shouldReturnFaculties() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(3L);
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");

        when(facultyService.findAllFacultiesByColor("Yellow")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty?color=Yellow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Hufflepuff"))
                .andExpect(jsonPath("$[0].color").value("Yellow"));
    }

    @Test
    void searchFaculties_shouldReturnMatchingFaculties() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(4L);
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");

        when(facultyService.searchFaculties("claw")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/search?query=claw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[0].name").value("Ravenclaw"))
                .andExpect(jsonPath("$[0].color").value("Blue"));
    }

    @Test
    void getFacultyStudents_shouldReturnStudentsForFaculty() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Harry Potter");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Hermione Granger");
        student2.setAge(17);

        when(facultyService.getFacultyStudents(1L)).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Hermione Granger"));
    }

    @Test
    void removeFaculty_shouldDeleteFaculty() throws Exception {

        doNothing().when(facultyService).removeFaculty(1L);

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).removeFaculty(1L);
    }
}