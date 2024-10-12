package com.example.goldenraspberryawards.service;

import com.example.goldenraspberryawards.model.Movie;
import com.example.goldenraspberryawards.model.dto.ProducerDTO;
import com.example.goldenraspberryawards.model.dto.ProducerIntervalDTO;
import com.example.goldenraspberryawards.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public Page<Movie> getAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie getById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Transactional
    public Movie update(Long id, Movie movieDetails) {
        return movieRepository.findById(id).map(movie -> {
            movie.updateValues(movieDetails);
            return movieRepository.save(movie);
        }).orElse(null);
    }

    @Transactional
    public boolean delete(Long id) {
        return movieRepository.findById(id).map(movie -> {
            movieRepository.delete(movie);
            return true;
        }).orElse(false);
    }

    public List<ProducerIntervalDTO> getProducersWithMinInterval() {
        int minInterval = Integer.MAX_VALUE;
        List<ProducerIntervalDTO> result = new ArrayList<>();
        for (ProducerDTO producerDTO : getAllMoviesGroupByProducer()) {
            for (int i = 1; i < producerDTO.getMovies().size(); i++) {
                int interval = producerDTO.getMovies().get(i).getReleasedOn() - producerDTO.getMovies().get(i - 1).getReleasedOn();
                if (interval > 0) {
                    minInterval = getMaxInterval(minInterval, result, producerDTO.getName(), producerDTO.getMovies(), i, interval, interval < minInterval);
                }
            }
        }
        return result;
    }

    public List<ProducerIntervalDTO> getProducerWithMaxInterval() {
        int maxInterval = 0;
        List<ProducerIntervalDTO> result = new ArrayList<>();
        for (ProducerDTO producerDTO : getAllMoviesGroupByProducer()) {
            for (int i = 1; i < producerDTO.getMovies().size(); i++) {
                int interval = producerDTO.getMovies().get(i).getReleasedOn() - producerDTO.getMovies().get(i - 1).getReleasedOn();
                maxInterval = getMaxInterval(maxInterval, result, producerDTO.getName(), producerDTO.getMovies(), i, interval, interval > maxInterval);
            }
        }
        return result;
    }

    private int getMaxInterval(int maxInterval, List<ProducerIntervalDTO> result, String producer, List<Movie> producerMovies, int i, int interval, boolean adicionar) {
        if (adicionar) {
            maxInterval = interval;
            result.clear();
        }
        if (interval == maxInterval || adicionar) {
            result.add(ProducerIntervalDTO.builder()
                    .producer(producer)
                    .interval(maxInterval)
                    .previousWin(producerMovies.get(i - 1).getReleasedOn())
                    .followingWin(producerMovies.get(i).getReleasedOn())
                    .build());
        }
        return maxInterval;
    }

    private List<ProducerDTO> getAllMoviesGroupByProducer() {
        Map<String, List<Movie>> producerMoviesMap = new HashMap<>();
        for (Movie movie : movieRepository.findAllMoviesOrderedByProducerAndReleaseDate()) {
            producerMoviesMap
                    .computeIfAbsent(movie.getProducers(), k -> new ArrayList<>())
                    .add(movie);
        }
        return producerMoviesMap.entrySet().stream().map(m -> new ProducerDTO(m.getKey(), m.getValue())).toList();
    }

}
