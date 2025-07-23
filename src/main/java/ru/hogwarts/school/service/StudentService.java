package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final Object lock = new Object(); // Объект для синхронизации

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student addStudent(Student student) {
        logger.info("Вызван метод создания студента");
        try {
            if (student.getName() == null || student.getName().isBlank()) {
                logger.warn("Попытка создания студента без имени");
                throw new IllegalArgumentException("Student name cannot be empty");
            }
            if (student.getAge() <= 0) {
                logger.warn("Попытка создания студента с некорректным возрастом: {}", student.getAge());
                throw new IllegalArgumentException("Age must be positive");
            }
            Student savedStudent = studentRepository.save(student);
            logger.debug("Создан студент: {}", savedStudent);
            return savedStudent;
        } catch (Exception e) {
            logger.error("Ошибка при создании студента: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating student: " + e.getMessage());
        }
    }

    public Student getStudent(Long id) {
        logger.debug("Вызван метод получения студента по ID: {}", id);
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("Студент с ID {} не найден", id);
            return new EntityNotFoundException("Student not found");
        });
    }

    @Transactional
    public void removeStudent(Long id) {
        logger.info("Вызван метод удаления студента по ID: {}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Попытка удаления несуществующего студента с ID: {}", id);
            throw new EntityNotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
        logger.debug("Студент с ID {} удален", id);
    }

    @Transactional
    public Student updateStudent(Long id, Student student) {
        logger.info("Вызван метод обновления студента по ID: {}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Попытка обновления несуществующего студента с ID: {}", id);
            throw new EntityNotFoundException("Student not found");
        }
        student.setId(id);
        Student updatedStudent = studentRepository.save(student);
        logger.debug("Обновлен студент: {}", updatedStudent);
        return updatedStudent;
    }

    public List<Student> findStudentsByAge(int age) {
        logger.info("Вызван метод поиска студентов по возрасту: {}", age);
        List<Student> students = studentRepository.findAllByAge(age);
        logger.debug("Найдено {} студентов возраста {}", students.size(), age);
        return students;
    }

    public List<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Вызван метод поиска студентов в возрастном диапазоне: {} - {}", minAge, maxAge);
        List<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
        logger.debug("Найдено {} студентов в диапазоне {} - {}", students.size(), minAge, maxAge);
        return students;
    }

    public Faculty getStudentFaculty(Long id) {
        logger.info("Вызван метод получения факультета студента по ID: {}", id);
        Student student = getStudent(id);
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.warn("У студента с ID {} не указан факультет", id);
        }
        return faculty;
    }

    public int getTotalCountOfStudents() {
        logger.info("Вызван метод получения общего количества студентов");
        int count = studentRepository.getTotalCountOfStudents();
        logger.debug("Общее количество студентов: {}", count);
        return count;
    }

    public double getAverageAge() {
        logger.info("Вызван метод получения среднего возраста студентов");
        double average = studentRepository.getAverageAge();
        logger.debug("Средний возраст студентов: {}", average);
        return average;
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Вызван метод получения последних 5 студентов");
        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Найдено {} последних студентов", students.size());
        return students;
    }

    // Шаг 1: Имена студентов на 'A' в верхнем регистре
    public List<String> getStudentNamesStartingWithA() {
        logger.info("Получение имен студентов, начинающихся на 'A'");
        return studentRepository.findAll().stream().map(Student::getName).filter(name -> name.toUpperCase().startsWith("A")).map(String::toUpperCase).sorted().collect(Collectors.toList());
    }

    // Шаг 2: Средний возраст через findAll
    public double getAverageAgeWithFindAll() {
        logger.info("Вычисление среднего возраста через findAll");
        return studentRepository.findAll().stream().mapToInt(Student::getAge).average().orElse(0.0);
    }

    // Шаг 4: Оптимизированная сумма
    public long calculateSum() {
        logger.info("Вычисление суммы чисел");

        // Оптимизация 1: Использование параллельных стримов
        long startTime = System.currentTimeMillis();
        long parallelSum = LongStream.rangeClosed(1, 1_000_000).parallel().sum();
        long parallelTime = System.currentTimeMillis() - startTime;

        logger.debug("Параллельное вычисление: {} мс", parallelTime);

        // Оптимизация 2: Использование формулы
        startTime = System.currentTimeMillis();
        long formulaSum = (long) 1_000_000 * (1_000_000 + 1) / 2;
        long formulaTime = System.currentTimeMillis() - startTime;

        logger.debug("Формульное вычисление: {} мс", formulaTime);

        return formulaSum; // Возвращаем самый быстрый вариант
    }

    public List<Student> getFirstSixStudents() {
        logger.info("Получение первых шести студентов");
        return studentRepository.findAll().stream().limit(6).collect(Collectors.toList());
    }

    public void printStudentsParallel(List<Student> students) {
        if (students.size() < 6) {
            logger.warn("Недостаточно студентов для вывода (требуется 6, найдено {})", students.size());
            return;
        }

        // Основной поток: первые два студента
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        // Поток 1: третий и четвертый студент
        new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }).start();

        // Поток 2: пятый и шестой студент
        new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }).start();
    }

    public void printStudentsSynchronized(List<Student> students) {
        if (students.size() < 6) {
            logger.warn("Недостаточно студентов для вывода (требуется 6, найдено {})", students.size());
            return;
        }

        // Основной поток: первые два студента
        synchronizedPrint(students.get(0).getName());
        synchronizedPrint(students.get(1).getName());

        // Поток 1: третий и четвертый студент
        new Thread(() -> {
            synchronizedPrint(students.get(2).getName());
            synchronizedPrint(students.get(3).getName());
        }).start();

        // Поток 2: пятый и шестой студент
        new Thread(() -> {
            synchronizedPrint(students.get(4).getName());
            synchronizedPrint(students.get(5).getName());
        }).start();
    }

    private void synchronizedPrint(String message) {
        synchronized (lock) {
            System.out.println(message);
        }
    }
}