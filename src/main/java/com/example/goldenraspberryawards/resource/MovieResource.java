package com.example.goldenraspberryawards.resource;

import com.example.goldenraspberryawards.model.Movie;
import com.example.goldenraspberryawards.service.MovieService;
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

    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody Movie movie) {
        Movie savedMovie = movieService.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getById(id);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        Movie updatedMovie = movieService.update(id, movieDetails);
        if (updatedMovie != null) {
            return ResponseEntity.ok(updatedMovie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        boolean isDeleted = movieService.delete(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
