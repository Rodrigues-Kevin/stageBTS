package com.example.demo.services;

import com.example.demo.dao.*;
import com.example.demo.data.*;
import com.example.demo.dto.MoviePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieManager {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private DirectorRepository directorRepository;

    public Movie addMovie(MoviePayload movie) throws Exception {
        Genre genre = genreRepository.findById(movie.getGenre()).orElseThrow(() -> new Exception("Genre not found"));
        Director director = directorRepository.findById(movie.getDirector()).orElseThrow(() -> new Exception("Director not found"));
        Movie m = new Movie(-1, movie.getName(), movie.getReleaseDate(), movie.getDescription(), movie.getDuration(), genre, director);
        return movieRepository.save(m);
    }

    public Movie updateMovie(MoviePayload movie) throws Exception {
        Movie m = movieRepository.findById(movie.getId()).orElseThrow(() -> new Exception("Id not found"));
        Genre genre = genreRepository.findById(movie.getGenre()).orElseThrow(() -> new Exception("Genre not found"));
        Director director = directorRepository.findById(movie.getDirector()).orElseThrow(() -> new Exception("Director not found"));
        m.setName(movie.getName());
        m.setGenre(genre);
        m.setDirector(director);
        m.setDescription(movie.getDescription());
        m.setReleaseDate(movie.getReleaseDate());
        m.setDuration(movie.getDuration());
        return movieRepository.save(m);
    }
}
