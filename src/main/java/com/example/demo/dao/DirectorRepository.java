package com.example.demo.dao;

import com.example.demo.data.Director;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public interface DirectorRepository extends JpaRepository<Director, Integer> {
    List<Director> findByFirstNameContainingIgnoreCase(String firstName);
    Director findByFirstNameAndLastName(String firstName, String lastName);
}