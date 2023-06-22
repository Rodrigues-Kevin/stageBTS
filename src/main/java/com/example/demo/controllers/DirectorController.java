package com.example.demo.controllers;

import com.example.demo.dao.DirectorRepository;
import com.example.demo.data.Director;
import com.example.demo.dto.DirectorPayload;
import com.example.demo.services.DirectorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/directors")
public class DirectorController {
    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private DirectorManager directorManager;

    //GET :
    @GetMapping
    public List<Director> getDirectors(@RequestParam(required = false) String firstName)
    {
        if (firstName == null) return directorRepository.findAll();
        else return directorRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id)
    {
        return directorRepository.findById(id).orElse(null);
    }

    //POST :
    @PostMapping
    public ResponseEntity<Director> addDirector(@RequestBody DirectorPayload director) {
        try {
            Director d = directorManager.addDirector(director);
            return ResponseEntity.ok(d);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //DELETE :
    @DeleteMapping("/{id}")
    public void delDirector(@RequestBody Director director) { directorRepository.delete(director); }

    //UPDATE :
    @PatchMapping
    public ResponseEntity<Director> updateDirector(@RequestBody DirectorPayload director) {
        try {
            Director d = directorManager.updateDirector(director);
            return ResponseEntity.ok(d);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
