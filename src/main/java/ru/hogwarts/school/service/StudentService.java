package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long id = 0;

    public Student addStudent(Student student) {
        student.setId(id++);
        students.put(student.getId(), student);
        return student;
    }

    public Student getStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        return students.put(student.getId(), student);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    public Collection<Student> findStudentsByAge(int age){
        ArrayList<Student> foundStudents = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getAge() == age) {
                foundStudents.add(student);
            }
        }
        return foundStudents;
    }
}
