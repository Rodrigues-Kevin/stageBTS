package com.example.demo.services;

import com.example.demo.dao.AuthorRepository;
import com.example.demo.data.Author;
import com.example.demo.dto.AuthorPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorManager {
    @Autowired
    private AuthorRepository authorRepository;

    public Author addAuthor(AuthorPayload author) throws Exception {
        Author a = new Author(-1, author.getFirstName(), author.getLastName());
        return authorRepository.save(a);
    }

    public Author updateAuthor(AuthorPayload author) throws Exception {
        Author a = authorRepository.findById(author.getId()).orElseThrow(() -> new Exception("Id not found"));
        a.setFirstName(author.getFirstName());
        a.setLastName(author.getLastName());
        return authorRepository.save(a);
    }
}
