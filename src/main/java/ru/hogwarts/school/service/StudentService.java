package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;


import java.util.*;
import java.util.List;

/**
 * Сервисный класс для сущности "студент" с методами CRUD
 */
@Service
public class StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private Long count = 0L;

    public StudentService() {
    }

    public Map<Long, Student> getStudentMap() {
        return studentMap;
    }

    public Long getCount() {
        return count;
    }

    public Long addStudent(@NotNull Student student) {
        count++;
        student.setId(count);
        studentMap.put(student.getId(), student);
        return student.getId();
    }

    public Student getStudent(Long id) {
        if (studentMap.containsKey(id)) {
            return studentMap.get(id);
        } else throw new NoSuchElementException("Студент с указанным id отсутствует");
    }

    public void removeStudent(Long id) {
        if (studentMap.containsKey(id)) {
            studentMap.remove(id);
        } else throw new NoSuchElementException("Студент с указанным id отсутствует");
    }

    public Student updateStudent(Long id, Student student) {
        if (studentMap.containsKey(id)) {
            student.setId(id);
            studentMap.put(id, student);
            return student;
        } else throw new NoSuchElementException("Студент с указанным id отсутствует");
    }

    public List<Student> findStudentsByAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        } else return studentMap.values().stream()
                .filter(student -> student.getAge() == age)
                .toList();
    }

    @Override
    public String toString() {
        return "StudentService{" + "studentMap=" + studentMap + ", count=" + count + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudentService that = (StudentService) o;
        return Objects.equals(studentMap, that.studentMap) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentMap, count);
    }
}
