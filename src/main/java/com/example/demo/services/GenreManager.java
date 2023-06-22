package com.example.demo.services;

import com.example.demo.dao.GenreRepository;
import com.example.demo.data.Genre;
import com.example.demo.dto.GenrePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreManager {
    @Autowired
    private GenreRepository genreRepository;

    public Genre addGenre(GenrePayload genre) throws Exception {
        Genre g = new Genre(-1, genre.getName());
        return genreRepository.save(g);
    }

    public Genre updateGenre(GenrePayload genre) throws Exception {
        Genre g = genreRepository.findById(genre.getId()).orElseThrow(() -> new Exception("Id not found"));
        g.setName(genre.getName());
        return genreRepository.save(g);
    }
}
