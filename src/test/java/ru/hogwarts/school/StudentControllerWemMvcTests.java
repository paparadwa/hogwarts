package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWemMvcTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void addStudentTest() throws Exception {
        String name = "Bob";
        int age = 18;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setAge(age);
        student.setName(name);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void editStudentTest() throws Exception {
        String name = "Bob";
        int age = 18;
        long id = 1L;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", "Евлампий");
        studentObject.put("age", 20);

        Student student = new Student();
        student.setId(id);
        student.setName("Евлампий");
        student.setAge(20);

        when(studentRepository.findById(id)).thenReturn(Optional.of(new Student(id, name, age, null)));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Евлампий"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    public void getAllStudentsTest() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Bob");
        student1.setAge(18);
        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Alice");
        student2.setAge(20);
        List<Student> students = List.of(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Bob"))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Alice"))
                .andExpect(jsonPath("$[1].age").value(20));
    }

    @Test
    public void deleteStudentTest() throws Exception {
        long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.of(new Student(id, "Bob", 18, null)));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void tryToFindNotExistedStudentTest() throws Exception {
        long id = 777L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findStudentsBetweenAgeTest() throws Exception {
        int minAge = 10;
        int maxAge = 30;
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Bob");
        student1.setAge(18);
        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Alice");
        student2.setAge(25);
        List<Student> students = List.of(student1, student2);
        when(studentRepository.findByAgeBetween(minAge, maxAge)).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/between_age/")
                        .param("ageMin", String.valueOf(minAge))
                        .param("ageMax", String.valueOf(maxAge))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bob"))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[1].name").value("Alice"))
                .andExpect(jsonPath("$[1].age").value(25));
    }
}
