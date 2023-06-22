package com.example.demo.controllers;

import com.example.demo.dao.*;
import com.example.demo.data.*;
import com.example.demo.dto.MoviePayload;
import com.example.demo.services.MovieManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieManager movieManager;

    //GET :
    @GetMapping
    public List<Movie> getMovies(@RequestParam(required = false) String name)
    {
        if (name == null) return movieRepository.findAll();
        else return movieRepository.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Integer id)
    {
        return movieRepository.findById(id).orElse(null);
    }

    //POST :
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody MoviePayload movie) {
        try {
            Movie m = movieManager.addMovie(movie);
            return ResponseEntity.ok(m);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //DELETE :
    @DeleteMapping("/{id}")
    public void delMovie(@PathVariable Integer id) { movieRepository.deleteById(id); }

    //UPDATE :
    @PatchMapping
    public ResponseEntity<Movie> updateMovie(@RequestBody MoviePayload movie) {
        try {
            Movie m = movieManager.updateMovie(movie);
            return ResponseEntity.ok(m);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
