package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        Collection<Faculty> faculties = facultyService.getAllFaculties();
        if (faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculties);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty tempFaculty = facultyService.editFaculty(faculty);
        if (tempFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tempFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/{color}")
    public ResponseEntity<Collection<Faculty>> findFacultiesByColor(@PathVariable String color){
        if (color.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(facultyService.findFacultiesByColor(color));
    }
}
