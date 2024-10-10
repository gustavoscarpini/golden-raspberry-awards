package com.example.goldenraspberryawards.service;

import com.example.goldenraspberryawards.model.Movie;
import com.example.goldenraspberryawards.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    public Movie getById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public Movie update(Long id, Movie movieDetails) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(movieDetails.getTitle());
            movie.setReleasedOn(movieDetails.getReleasedOn());
            movie.setStudios(movieDetails.getStudios());
            movie.setProducers(movieDetails.getProducers());
            movie.setWinner(movieDetails.getWinner());
            return movieRepository.save(movie);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return movieRepository.findById(id).map(movie -> {
            movieRepository.delete(movie);
            return true;
        }).orElse(false);
    }
}
