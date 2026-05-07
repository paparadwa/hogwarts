package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTemplateTests {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void addFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Тестовый факультет");
        faculty.setColor("Зелёный");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isNotNull();
    }

    @Test
    void editFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setId(999L);
        faculty.setName("Тестовый факультет");
        faculty.setColor("Зелёный");
        Faculty createdFaculty = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        createdFaculty.setName("Измененный факультет");
        createdFaculty.setColor("Красный");
        restTemplate.put("http://localhost:" + port + "/faculty", createdFaculty);
        Faculty editedFaculty = restTemplate.getForObject("http://localhost:" + port + "/faculty/" + createdFaculty.getId(), Faculty.class);
        Assertions.assertThat(editedFaculty).isNotNull();
        Assertions.assertThat(editedFaculty.getName()).isEqualTo("Измененный факультет");
        Assertions.assertThat(editedFaculty.getColor()).isEqualTo("Красный");
    }

    @Test
    void getAllFacultiesTest() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/list", String.class))
                .isNotNull();
    }

    @Test
    void deleteFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Удаленный факультет");
        faculty.setColor("Чёрный");
        Faculty createdFaculty = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        restTemplate.delete("http://localhost:" + port + "/faculty/{id}", createdFaculty.getId());
    }

    @Test
    void tryToFindNotExistedFacultyTest() throws Exception {
        long id = 777;
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + id, String.class))
                .isNull();
    }

    @Test
    void findFacultiesByColorTest() {
        String color = "Красный";
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/find/color={color}", String.class, color))
                .isNotNull();
    }
}
