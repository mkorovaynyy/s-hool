package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
@Service
@Transactional
public class AvatarService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
}
