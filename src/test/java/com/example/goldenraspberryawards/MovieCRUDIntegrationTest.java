package com.example.goldenraspberryawards;

import com.example.goldenraspberryawards.model.Movie;
import com.example.goldenraspberryawards.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieCRUDIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final Faker faker = new Faker();
    private final String API_PATH = "/api/v1/movies";

    @Test
    public void createMovie_ReturnWithIdNotEmpty() throws Exception {
        Movie movie = getValidMovieWithouId();
        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void createTwoMoviesAndGetAllMovies_ReturnTwo() throws Exception {

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getValidMovieWithouId())));

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getValidMovieWithouId())));

        mockMvc.perform(get(API_PATH + "?page=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(equalTo(2))));
    }

    @Test
    public void createMovieAndGetById_ReturnSameTitleAndReleasedOn() throws Exception {
        Movie movie = getValidMovieWithouId();
        MvcResult result = mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)))
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(get(API_PATH + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.releasedOn").value(movie.getReleasedOn()));
    }

    @Test
    public void createMovieAndUpdateProducers_ReturnMovieWithNewValue() throws Exception {
        Movie movie = getValidMovieWithouId();
        MvcResult result = mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getValidMovieWithouId())))
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        String name = faker.name().name();
        movie.setProducers(name);
        mockMvc.perform(put(API_PATH + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.producers").value(movie.getProducers()));
    }

    @Test
    public void createMovieAndDelete_ReturnNoContent() throws Exception {
        MvcResult result = mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getValidMovieWithouId())))
                .andReturn();
        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        mockMvc.perform(delete(API_PATH + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Movie getValidMovieWithouId() {
        Movie movie = new Movie();
        movie.setTitle(faker.book().title());
        movie.setReleasedOn(Util.randBetween(1900, LocalDate.now().getYear()));
        movie.setStudios(faker.company().name());
        movie.setProducers(faker.name().name());
        return movie;
    }
}
