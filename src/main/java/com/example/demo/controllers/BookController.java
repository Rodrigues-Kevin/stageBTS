package com.example.demo.controllers;

import com.example.demo.dao.*;
import com.example.demo.data.*;
import com.example.demo.dto.BookPayload;
import com.example.demo.services.BookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookManager bookManager;

    //GET :
    @GetMapping
    public List<Book> getBooks(@RequestParam(required = false) String name)
    {
        if (name == null) return bookRepository.findAll();
        else return bookRepository.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Integer id)
    {
        return bookRepository.findById(id).orElse(null);
    }

    //POST :
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody BookPayload book) {
        try {
            Book b = bookManager.addBook(book);
            return ResponseEntity.ok(b);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //DELETE :
    @DeleteMapping("/{id}")
    public void delBook(@RequestBody Book book) { bookRepository.delete(book); }

    //UPDATE :
    @PatchMapping
    public ResponseEntity<Book> updateBook(@RequestBody BookPayload book) {
        try {
            Book b = bookManager.updateBook(book);
            return ResponseEntity.ok(b);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}