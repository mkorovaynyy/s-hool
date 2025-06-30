package ru.hogwarts.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entities.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudentId(Long id);
    Page<Avatar> findAll(Pageable pageable); // Исправленный импорт Page
}