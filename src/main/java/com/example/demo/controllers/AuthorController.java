package com.example.demo.controllers;

import com.example.demo.dao.AuthorRepository;
import com.example.demo.data.Author;
import com.example.demo.dto.AuthorPayload;
import com.example.demo.services.AuthorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorManager authorManager;

    //GET :
    @GetMapping
    public List<Author> getAuthors(@RequestParam(required = false) String firstName)
    {
        if (firstName == null) return authorRepository.findAll();
        else return authorRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Integer id)
    {
        return authorRepository.findById(id).orElse(null);
    }

    //POST :
    @PostMapping
    public ResponseEntity<Author> addAuthor(@RequestBody AuthorPayload author) {
        try {
            Author a = authorManager.addAuthor(author);
            return ResponseEntity.ok(a);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //DELETE :
    @DeleteMapping("/{id}")
    public void delAuthor(@RequestBody Author author) { authorRepository.delete(author); }

    //UPDATE :
    @PatchMapping
    public ResponseEntity<Author> updateAuthor(@RequestBody AuthorPayload author) {
        try {
            Author a = authorManager.updateAuthor(author);
            return ResponseEntity.ok(a);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
