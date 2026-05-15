package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for add faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for get faculty");
        return facultyRepository.findById(id).orElse(null);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public Collection<Faculty> findFacultiesByColor(String color) {
        logger.info("Was invoked method for find faculties by color: {}", color);
        color = color.toLowerCase();
        ArrayList<Faculty> foundFaculties = new ArrayList<>();
        for (Faculty faculty : facultyRepository.findAll()) {
            if ((faculty.getColor().toLowerCase()).equals(color)) {
                foundFaculties.add(faculty);
            }
        }
        return foundFaculties;
    }

    public Collection<Faculty> findByNameOrColor(String name, String color) {
        logger.info("Was invoked method for find faculties by name or color: {} or {}", color, name);
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }

    public Collection<Student> getFacultyStudents(Long id) {
        logger.info("Was invoked method for get students of faculty");
        return facultyRepository.findById(id).get().getStudents();
    }
}
