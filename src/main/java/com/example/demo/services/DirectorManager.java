package com.example.demo.services;

import com.example.demo.dao.DirectorRepository;
import com.example.demo.data.Director;
import com.example.demo.dto.DirectorPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectorManager {
    @Autowired
    private DirectorRepository directorRepository;

    public Director addDirector(DirectorPayload director) throws Exception {
        Director d = new Director(-1, director.getFirstName(), director.getLastName());
        return directorRepository.save(d);
    }

    public Director updateDirector(DirectorPayload director) throws Exception {
        Director d = directorRepository.findById(director.getId()).orElseThrow(() -> new Exception("Id not found"));
        d.setFirstName(director.getFirstName());
        d.setLastName(director.getLastName());
        return directorRepository.save(d);
    }
}
