package com.example.demo.services;

import com.example.demo.dao.*;
import com.example.demo.data.*;
import com.example.demo.dto.BookPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookManager {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Book addBook(BookPayload book) throws Exception {
        Genre genre = genreRepository.findById(book.getGenre()).orElseThrow(() -> new Exception("Genre not found"));
        Author author = authorRepository.findById(book.getAuthor()).orElseThrow(() -> new Exception("Author not found"));
        Book b = new Book(-1, book.getName(), book.getReleaseDate(), book.getDescription(), genre, author);
        return bookRepository.save(b);
    }

    public Book updateBook(BookPayload book) throws Exception {
        Book b = bookRepository.findById(book.getId()).orElseThrow(() -> new Exception("Id not found"));
        Genre genre = genreRepository.findById(book.getGenre()).orElseThrow(() -> new Exception("Genre not found"));
        Author author = authorRepository.findById(book.getAuthor()).orElseThrow(() -> new Exception("Author not found"));
        b.setName(book.getName());
        b.setGenre(genre);
        b.setAuthor(author);
        b.setDescription(book.getDescription());
        b.setReleaseDate(book.getReleaseDate());
        return bookRepository.save(b);
    }
}
