package com.example.goldenraspberryawards;

import com.example.goldenraspberryawards.model.Movie;
import com.example.goldenraspberryawards.model.dto.MinMaxIntervalProducerDTO;
import com.example.goldenraspberryawards.model.dto.ProducerIntervalDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MovieStatisticsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final Faker faker = new Faker();
    private final String API_PATH = "/api/v1/movies";

    @Test
    public void createFourMovies_CompareMinInterval() throws Exception {
        Movie movie = createSameMovieByYears(Lists.newArrayList(1999, 2008, 2009, 2014));
        mockMvc.perform(get(API_PATH + "/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min", hasSize(1)))
                .andExpect(jsonPath("$.min[0].producer").value(movie.getProducers()))
                .andExpect(jsonPath("$.min[0].interval").value(1))
                .andExpect(jsonPath("$.min[0].previousWin").value(2008))
                .andExpect(jsonPath("$.min[0].followingWin").value(2009));
    }


    @Test
    public void createFourMovies_CompareMaxIntervalConsecutive() throws Exception {
        Movie movie = createSameMovieByYears(Lists.newArrayList(1993, 1995, 1999, 2001));
        mockMvc.perform(get(API_PATH + "/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.max", hasSize(1)))
                .andExpect(jsonPath("$.max[0].producer").value(movie.getProducers()))
                .andExpect(jsonPath("$.max[0].interval").value(4))
                .andExpect(jsonPath("$.max[0].previousWin").value(1995))
                .andExpect(jsonPath("$.max[0].followingWin").value(1999));

    }

    @Test
    public void createMovies_CompareMinAndMaxIntervalConsecutive() throws Exception {
        Movie firstMin = createSameMovieByYears(Lists.newArrayList(1990, 2000, 2008, 2009));
        Movie secondtMin = createSameMovieByYears(Lists.newArrayList(2001, 2018, 2019));

        Movie firstMaxThirdMin = createSameMovieByYears(Lists.newArrayList(1900, 1999, 2000));
        Movie secondtMax = createSameMovieByYears(Lists.newArrayList(2000, 2099, 2110));

        MvcResult result = mockMvc.perform(get(API_PATH + "/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min", hasSize(3)))
                .andExpect(jsonPath("$.max", hasSize(2)))
                .andReturn();

        MinMaxIntervalProducerDTO minMaxInterval = objectMapper.readValue(result.getResponse().getContentAsString(), MinMaxIntervalProducerDTO.class);
        Assertions.assertThat(minMaxInterval.getMin())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("producer")
                .containsExactlyInAnyOrder(
                        ProducerIntervalDTO.builder()
                                .producer(firstMin.getProducers())
                                .interval(1)
                                .previousWin(2008)
                                .followingWin(2009)
                                .build(),
                        ProducerIntervalDTO.builder()
                                .producer(secondtMin.getProducers())
                                .interval(1)
                                .previousWin(2018)
                                .followingWin(2019)
                                .build(),
                        ProducerIntervalDTO.builder()
                                .producer(firstMaxThirdMin.getProducers())
                                .interval(1)
                                .previousWin(1999)
                                .followingWin(2000)
                                .build()
                );

        Assertions.assertThat(minMaxInterval.getMax())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("producer")
                .containsExactlyInAnyOrder(
                        ProducerIntervalDTO.builder()
                                .producer(firstMaxThirdMin.getProducers())
                                .interval(99)
                                .previousWin(1900)
                                .followingWin(1999)
                                .build(),
                        ProducerIntervalDTO.builder()
                                .producer(secondtMax.getProducers())
                                .interval(99)
                                .previousWin(2000)
                                .followingWin(2099)
                                .build()
                );

    }

    @Test
    public void createMovies_ComparedMaxIntervalNotConsecutive() throws Exception {
        Movie firstMax = createSameMovieByYears(Lists.newArrayList(1900, 1999, 2000));

        MvcResult result = mockMvc.perform(get(API_PATH + "/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.max", hasSize(1)))
                .andExpect(jsonPath("$.max[0].producer").value(firstMax.getProducers())).andReturn();

        MinMaxIntervalProducerDTO minMaxInterval = objectMapper.readValue(result.getResponse().getContentAsString(), MinMaxIntervalProducerDTO.class);
        Assertions.assertThat(minMaxInterval.getMax())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("producer")
                .doesNotContain( ProducerIntervalDTO.builder()
                        .producer(firstMax.getProducers())
                        .interval(100)
                        .previousWin(1900)
                        .followingWin(2000)
                        .build());
    }

    private Movie createSameMovieByYears(List<Integer> years) throws Exception {
        Movie movie = new Movie();
        movie.setTitle(faker.book().title());
        movie.setStudios(faker.company().name());
        movie.setProducers(faker.name().name());
        for (Integer year : years) {
            movie.setReleasedOn(year);
            saveMovie(movie);
            movie.setReleasedOn(movie.getReleasedOn() + 1);
        }
        return movie;
    }

    private void saveMovie(Movie movie) throws Exception {
        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated());
    }


}
