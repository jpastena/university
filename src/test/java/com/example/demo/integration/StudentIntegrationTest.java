package com.example.demo.integration;

import com.example.demo.student.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.reactive.function.BodyInserters;

@RunWith(SpringRunner.class)
//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:before.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:after.sql")
})
public class StudentIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected State state;

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAll_sucess() throws Exception {
        MvcResult res = this.mockMvc.perform(get("/students"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String resJson = res.getResponse().getContentAsString();
        Student[] resStudent = this.objectMapper.readValue(resJson, Student[].class);

        Assert.assertNotNull(resStudent);
        Assert.assertEquals(this.state.students.size(), resStudent.length);
        this.assertStudentEquals(this.state.students.get(resStudent[0]), resStudent[0]);
        this.assertStudentEquals(this.state.students.get(resStudent[1]), resStudent[1]);
        this.assertStudentEquals(this.state.students.get(resStudent[2]), resStudent[2]);
    }

    @Test
    public void getById_sucess() throws Exception {
        int id = 2; // studentId present in the default state
        MvcResult res = this.mockMvc.perform(get("/students/" + id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String resJson = res.getResponse().getContentAsString();
        Student resStudent = this.objectMapper.readValue(resJson, Student.class);

        this.assertStudentEquals(this.state.getMockStudent(id), resStudent);
    }

    @Test
    public void getById_missing() throws Exception {
        int id = 99999; // studentId NOT present in the default state
        this.mockMvc.perform(get("/students/" + id))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void add_sucess() throws Exception {
        Student student = this.state.getMockStudent(4); // there are 3 students in the default state
        String studentJson = this.objectMapper.writeValueAsString(student);

        RequestBuilder rb = post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson);

        MvcResult res = this.mockMvc.perform(rb)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String resJson = res.getResponse().getContentAsString();
        Student resStudent = this.objectMapper.readValue(resJson, Student.class);

        this.assertStudentEquals(student, resStudent);
    }

    @Test
    public void update_sucess() throws Exception {
        int id = 1;  // studentId present in default state
        Student student = this.state.getMockStudent(1);
        student.setFirstName("new-firstName");
        student.setFirstName("new-lastName");
        student.setFirstName("new-email");
        String studentJson = this.objectMapper.writeValueAsString(student);

        RequestBuilder rb = put("/students/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson);

        MvcResult res = this.mockMvc.perform(rb)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String resJson = res.getResponse().getContentAsString();
        Student resStudent = this.objectMapper.readValue(resJson, Student.class);

        this.assertStudentEquals(student, resStudent);
    }

    @Test
    public void update_missing() throws Exception {
        int id = 9999;  // studentId NOT present in default state
        Student student = this.state.getMockStudent(1);
        student.setFirstName("new-firstName");
        student.setFirstName("new-lastName");
        student.setFirstName("new-email");

        this.webTestClient.put()
                .uri("/students/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(student))
                .exchange()
                .expectStatus().is5xxServerError();
    }
    
    @Test
    public void delete_sucess() {
        int id = 1; // studentId present in default state
        
        // delete one student
        this.webTestClient.delete()
                .uri("/students/" + id)
                .exchange()
                .expectStatus().is2xxSuccessful();
        
        // get all students should return one less student
        this.webTestClient.get()
                .uri("/students")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Student[].class)
                .consumeWith(response -> Assert.assertEquals(2, response.getResponseBody().length));
    }
    
    @Test
    public void delete_missing() {
        int id = 9999; // studentId NOT present in default state
        
        this.webTestClient.delete()
                .uri("/students/" + id)
                .exchange()
                .expectStatus().is5xxServerError();
    }
    
    private void assertStudentEquals(Student a, Student b) {
        Assert.assertEquals(a.getId(), b.getId());
        Assert.assertEquals(a.getFirstName(), b.getFirstName());
        Assert.assertEquals(a.getLastName(), b.getLastName());
        Assert.assertEquals(a.getEmail(), b.getEmail());
    }
}
