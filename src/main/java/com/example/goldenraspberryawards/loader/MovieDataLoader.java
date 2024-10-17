package com.example.goldenraspberryawards.loader;

import com.example.goldenraspberryawards.model.Movie;
import com.example.goldenraspberryawards.repository.MovieRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileReader;
import java.io.IOException;

@Component
public class MovieDataLoader implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        loadMovieData();
    }

    private void loadMovieData() {
        String fileName = "src/main/resources/migration/movielist.csv";
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(fileName))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] values;
            csvReader.readNext();
            while ((values = csvReader.readNext()) != null) {
                Movie movie = new Movie();
                movie.setReleasedOn(Integer.parseInt(values[0].trim()));
                movie.setTitle(values[1].trim());
                movie.setStudios(values[2].trim());
                movie.setProducers(values[3].trim());
                for (int i = 4; i < values.length; i++) {
                    if (values[i].trim().equalsIgnoreCase("yes") || values[i].trim().equalsIgnoreCase("no")) {
                        movie.setWinner(values[i].trim().equalsIgnoreCase("yes") ? "Yes" : "No");
                        break;
                    }
                }
                movieRepository.save(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
