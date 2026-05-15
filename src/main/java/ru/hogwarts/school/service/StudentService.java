package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for add student");
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Was invoked method for get student");
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> findStudentsByAge(int age) {
        logger.info("Was invoked method for find students by age {}", age);
        ArrayList<Student> foundStudents = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            if (student.getAge() == age) {
                foundStudents.add(student);
            }
        }
        return foundStudents;
    }

    public Collection<Student> findStudentsBetweenAge(int ageMin, int ageMax) {
        logger.info("Was invoked method for find students between age {} and {}", ageMin, ageMax);
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    public Faculty getStudentFaculty(Long id) {
        logger.info("Was invoked method for get student faculty");
        return studentRepository.findById(id).get().getFaculty();
    }

    @Transactional
    public Avatar findAvatar(Long id) {
        logger.info("Was invoked method for find avatar of student {}", id);
        return avatarRepository.findByStudentId(id).get();
    }

    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload student's avatar");
        Student student = getStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setData(file.getBytes());
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());

        avatarRepository.save(avatar);
    }

    private String getExtension(String filename) {
        logger.info("Was invoked method for get extension");
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public Integer getStudentsQuantity() {
        logger.info("Was invoked method for get quantity of students");
        return studentRepository.getStudentsQuantity();
    }

    public Float getStudentsAverageAge() {
        logger.info("Was invoked method for get average age of students");
        return studentRepository.getStudentsAverageAge();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.getLastFiveStudents();
    }
}
