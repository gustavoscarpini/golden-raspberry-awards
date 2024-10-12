package com.example.goldenraspberryawards.resource;

import com.example.goldenraspberryawards.model.Movie;
import com.example.goldenraspberryawards.model.dto.MinMaxIntervalProducerDTO;
import com.example.goldenraspberryawards.model.dto.ProducerIntervalDTO;
import com.example.goldenraspberryawards.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieResource {

    private final MovieService movieService;

    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Get all movies", description = "Retrieve all movies with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movies retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "404", description = "Movies not found")
    })
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Page<Movie> movies = movieService.getAll(PageRequest.of(page, size));
        return !movies.isEmpty() ? ResponseEntity.ok(movies.getContent()) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get movie by ID", description = "Retrieve a movie by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getById(id);
        return movie != null ? ResponseEntity.ok(movie) : ResponseEntity.notFound().build();
    }


    @Operation(summary = "Create a new movie", description = "Adds a new movie to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movie created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody @Valid Movie movie) {
        Movie savedMovie = movieService.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @Operation(summary = "Update movie", description = "Update an existing movie by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "404", description = "Movie not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movieDetails) {
        Movie updatedMovie = movieService.update(id, movieDetails);
        return updatedMovie != null ? ResponseEntity.ok(updatedMovie) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete movie", description = "Delete a movie by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movie deleted"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        boolean isDeleted = movieService.delete(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get movie statistics", description = "Retrieve the movies with the highest and lowest nominations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MinMaxIntervalProducerDTO.class)))
    })
    @GetMapping("/statistics")
    public ResponseEntity<MinMaxIntervalProducerDTO> getStatistics() {
        List<ProducerIntervalDTO> producerWithMaxInterval = movieService.getProducersWithMaxInterval();
        List<ProducerIntervalDTO> producerWithMinInterval = movieService.getProducersWithMinInterval();
        return ResponseEntity.ok(MinMaxIntervalProducerDTO.builder().max(producerWithMaxInterval).min(producerWithMinInterval).build());
    }
}
