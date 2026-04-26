package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).get();
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Collection<Faculty> findFacultiesByColor(String color){
        color = color.toLowerCase();
        ArrayList<Faculty> foundFaculties = new ArrayList<>();
        for (Faculty faculty : facultyRepository.findAll()) {
            if ((faculty.getColor().toLowerCase()).equals(color)) {
                foundFaculties.add(faculty);
            }
        }
        return foundFaculties;
    }

    public Collection<Faculty> findByNameOrColor(String name, String color){
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }

    public Collection<Student> getFacultyStudents(Long id){
        return facultyRepository.findById(id).get().getStudents();
    }
}
