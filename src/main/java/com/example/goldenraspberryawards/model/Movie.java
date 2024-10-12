package com.example.goldenraspberryawards.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "released_on")
    private Integer releasedOn;
    @NotNull
    @Min(value = 2, message = "Title should not be less than 2")
    @Max(value = 255, message = "Title should not be greater than 255")
    private String title;
    @NotNull
    @Min(value = 2, message = "Studios should not be less than 2")
    @Max(value = 255, message = "Studios should not be greater than 255")
    private String studios;
    @NotNull
    @Min(value = 2, message = "Producers should not be less than 2")
    @Max(value = 255, message = "Producers should not be greater than 255")
    private String producers;
    private String winner;

    public void updateValues(Movie newValues){
        this.releasedOn = newValues.getReleasedOn();
        this.title = newValues.getTitle();
        this.studios = newValues.getStudios();
        this.producers = newValues.getProducers();
        this.winner = newValues.getWinner();
    }
}
