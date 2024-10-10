package com.example.goldenraspberryawards.repository;

import com.example.goldenraspberryawards.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
