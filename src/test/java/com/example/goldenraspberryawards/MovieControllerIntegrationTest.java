package com.example.goldenraspberryawards;

import com.example.goldenraspberryawards.model.Movie;
import com.example.goldenraspberryawards.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testCreateMovie() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setReleasedOn(2010);
        movie.setStudios("Warner Bros.");
        movie.setProducers("Emma Thomas");
        movie.setWinner("Yes");

        mockMvc.perform(post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.releasedOn").value(2010));
    }

    @Test
    public void testGetAllMovies() throws Exception {
        // Adiciona filmes de teste ao banco de dados
        Movie movie1 = new Movie();
        movie1.setTitle("Interstellar");
        movie1.setReleasedOn(2014);
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("The Dark Knight");
        movie2.setReleasedOn(2008);
        movieRepository.save(movie2);

        mockMvc.perform(get("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    public void testGetMovieById() throws Exception {
        // Adiciona um filme de teste ao banco de dados
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setReleasedOn(2010);
        movieRepository.save(movie);

        mockMvc.perform(get("/api/v1/movies/{id}", movie.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.releasedOn").value(2010));
    }

    @Test
    public void testUpdateMovie() throws Exception {
        // Adiciona um filme de teste ao banco de dados
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setReleasedOn(2010);
        movieRepository.save(movie);

        // Dados atualizados
        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("Inception (Updated)");
        updatedMovie.setReleasedOn(2010);

        mockMvc.perform(put("/api/v1/movies/{id}", movie.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception (Updated)"));
    }

    @Test
    public void testDeleteMovie() throws Exception {
        // Adiciona um filme de teste ao banco de dados
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setReleasedOn(2010);
        movieRepository.save(movie);

        mockMvc.perform(delete("/api/v1/movies/{id}", movie.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
