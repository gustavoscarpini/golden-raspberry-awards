package com.example.goldenraspberryawards.model.dto;

import com.example.goldenraspberryawards.model.Movie;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProducerDTO {
    String name;
    List<Movie> movies;
}
