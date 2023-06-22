package com.example.demo.dao;

import com.example.demo.data.Author;
import com.example.demo.data.Director;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByFirstNameContainingIgnoreCase(String firstName);
    Author findByFirstNameAndLastName(String firstName, String lastName);
}
