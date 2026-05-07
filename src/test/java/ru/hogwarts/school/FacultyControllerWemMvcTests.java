package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import org.json.JSONObject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWemMvcTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void addFacultyTest() throws Exception {
        String name = "Гриффиндор";
        String color = "Красный";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void editFacultyTest() throws Exception {
        String name = "Гриффиндор";
        String color = "Красный";
        long id = 1L;

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", "Слизерин");
        facultyObject.put("color", "Зелёный");

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("Слизерин");
        faculty.setColor("Зелёный");

        when(facultyRepository.findById(id)).thenReturn(Optional.of(new Faculty(id, name, color, null)));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Слизерин"))
                .andExpect(jsonPath("$.color").value("Зелёный"));
    }

    @Test
    public void getAllFacultiesTest() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Гриффиндор");
        faculty1.setColor("Красный");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Слизерин");
        faculty2.setColor("Зелёный");

        List<Faculty> faculties = List.of(faculty1, faculty2);

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("Красный"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Слизерин"))
                .andExpect(jsonPath("$[1].color").value("Зелёный"));
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        long id = 1L;
        when(facultyRepository.findById(id)).thenReturn(Optional.of(new Faculty(id, "Гриффиндор", "Красный", null)));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void tryToFindNotExistedFacultyTest() throws Exception {
        long id = 777L;
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findFacultiesByColorTest() throws Exception {
        String color = "Красный";

        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Гриффиндор");
        faculty1.setColor("Красный");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Когтевран");
        faculty2.setColor("Красный");

        List<Faculty> faculties = List.of(faculty1, faculty2);

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/find/{color}", color)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("Красный"))
                .andExpect(jsonPath("$[1].name").value("Когтевран"))
                .andExpect(jsonPath("$[1].color").value("Красный"));
    }
}
