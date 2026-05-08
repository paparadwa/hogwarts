package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTemplateTests {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void addStudentTest() {
        Student student = new Student();
        student.setId(1L);
        student.setAge(18);
        student.setName("Иннокентий");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotNull();
    }

    @Test
    void editStudentTest() {
        Student student = new Student();
        student.setAge(17);
        student.setName("Вячеслав");
        Student createdStudent = restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        createdStudent.setName("Вячеслав Измененный");
        createdStudent.setAge(20);
        restTemplate.put("http://localhost:" + port + "/student", createdStudent);
        Student editedStudent = restTemplate.getForObject("http://localhost:" + port + "/student/" + createdStudent.getId(), Student.class);
        Assertions.assertThat(editedStudent).isNotNull();
        Assertions.assertThat(editedStudent.getName()).isEqualTo("Вячеслав Измененный");
        Assertions.assertThat(editedStudent.getAge()).isEqualTo(20);
    }

    @Test
    void getAllStudentsTest() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/list", String.class))
                .isNotNull();
    }

    @Test
    void deleteStudentTest() {
        Student student = new Student();
        student.setAge(20);
        student.setName("Удалить");
        Student createdStudent = restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        restTemplate.delete("http://localhost:" + port + "/student/{id}", createdStudent.getId());
    }

    @Test
    void tryToFindNotExistedStudentTest() throws Exception {
        long id = 777;
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, String.class))
                .isNull();
    }

    @Test
    void findStudentsBetweenAgeTest() {
        int minAge = 10;
        int maxAge = 30;
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/between_age/?ageMin={minAge}&ageMax={maxAge}", String.class, minAge, maxAge))
                .isNotNull();
    }
}
