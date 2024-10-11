package com.example.goldenraspberryawards.repository;

import com.example.goldenraspberryawards.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m ORDER BY m.producers ASC, m.releasedOn ASC")
    List<Movie> findAllMoviesOrderedByProducerAndReleaseDate();
}
