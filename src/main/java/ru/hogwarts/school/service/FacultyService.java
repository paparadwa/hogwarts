package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long id = 0;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(id++);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFaculty(long id) {
        return faculties.get(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            return null;
        }
        return faculties.put(faculty.getId(), faculty);
    }

    public Collection<Faculty> findFacultiesByColor(String color){
        color = color.toLowerCase();
        ArrayList<Faculty> foundFaculties = new ArrayList<>();
        for (Faculty faculty : faculties.values()) {
            if ((faculty.getColor().toLowerCase()).equals(color)) {
                foundFaculties.add(faculty);
            }
        }
        return foundFaculties;
    }
}
