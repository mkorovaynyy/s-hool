package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entities.Avatar;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AvatarRepository avatarRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    public AvatarService(StudentRepository studentRepository,
                         FacultyRepository facultyRepository,
                         AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Вызван метод загрузки аватара для студента ID: {}", studentId);
        Student student = studentRepository.getById(studentId);
        Path filePath = Path.of(avatarDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = new FileOutputStream(filePath.toFile());
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(os);
            logger.debug("Аватар сохранен по пути: {}", filePath);
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateDataForDB(filePath));
        avatarRepository.save(avatar);
        logger.info("Аватар для студента ID: {} успешно загружен", studentId);
    }

    private byte[] generateDataForDB(Path filePath) {
        logger.debug("Генерация превью для аватара: {}", filePath);
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            BufferedImage image = ImageIO.read(bis);
            if (image == null) {
                logger.error("Не удалось прочитать изображение: {}", filePath);
                throw new RuntimeException("Invalid image file");
            }

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image, 0, 0, 100, height, null);
            graphics2D.dispose();

            String extension = getExtensions(filePath.getFileName().toString());
            ImageIO.write(preview, extension, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error("Ошибка генерации превью: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Avatar findAvatar(Long studentId) {
        logger.debug("Поиск аватара для студента ID: {}", studentId);
        return avatarRepository.findById(studentId).orElse(new Avatar());
    }

    private String getExtensions(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Вызван метод получения аватаров. Страница: {}, Размер: {}", page, size);
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Avatar> avatars = avatarRepository.findAll(pageRequest);
        logger.debug("Получено {} аватаров", avatars.getNumberOfElements());
        return avatars;
    }
}