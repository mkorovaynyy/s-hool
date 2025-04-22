package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;

/**
 * Сервисный класс для сущности "факультет" с методами CRUD
 */
@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private Long count = 0L;

    public FacultyService() {
    }

    public Map<Long, Faculty> getFacultyMap() {
        return facultyMap;
    }

    public Long getCount() {
        return count;
    }

    public void addFaculty(@NotNull Faculty faculty) {
        count++;
        faculty.setId(count);
        facultyMap.put(faculty.getId(), faculty);
    }

    public Faculty getFaculty(Long id) {
        if (facultyMap.containsKey(id)) {
            return facultyMap.get(id);
        } else throw new NoSuchElementException("Факультет с указанным id отсутствует");
    }

    public void removeFaculty(Long id) {
        if (facultyMap.containsKey(id)) {
            facultyMap.remove(id);
        } else throw new NoSuchElementException("Факультет с указанным id отсутствует");
    }

    public void updateFaculty(Long id, Faculty faculty) {
        if (facultyMap.containsKey(id)) {
            faculty.setId(id);
            facultyMap.put(id, faculty);
        } else throw new NoSuchElementException("Факультет с указанным id отсутствует");
    }

    public List<Faculty> findAllFacultiesByColor(String color) {
        return facultyMap.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .toList();
    }

    @Override
    public String toString() {
        return "FacultyService{" +
                "facultyMap=" + facultyMap +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FacultyService that = (FacultyService) o;
        return Objects.equals(facultyMap, that.facultyMap) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facultyMap, count);
    }
}
