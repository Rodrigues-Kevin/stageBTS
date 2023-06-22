package com.example.demo.controllers;

import com.example.demo.dao.GenreRepository;
import com.example.demo.data.Genre;
import com.example.demo.dto.GenrePayload;
import com.example.demo.services.GenreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreManager genreManager;

    //GET :
    @GetMapping
    public List<Genre> getGenre(@RequestParam(required = false) String name)
    {
        if (name == null) return genreRepository.findAll();
        else return genreRepository.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id)
    {
        return genreRepository.findById(id).orElse(null);
    }

    //POST :
    @PostMapping
    public ResponseEntity<Genre> addGenre(@RequestBody GenrePayload genre) {
        try {
            Genre g = genreManager.addGenre(genre);
            return ResponseEntity.ok(g);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //DELETE :
    @DeleteMapping("/{id}")
    public void delGenre(@RequestBody Genre genre) { genreRepository.delete(genre); }

    //UPDATE :
    @PatchMapping
    public ResponseEntity<Genre> updateGenre(@RequestBody GenrePayload genre) {
        try {
            Genre g = genreManager.updateGenre(genre);
            return ResponseEntity.ok(g);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
