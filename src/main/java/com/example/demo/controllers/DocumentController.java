package com.example.demo.controllers;

import com.example.demo.services.Documentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doc")
public class DocumentController {
    @Autowired
    private Documentation documentation;

    @GetMapping
    public void produceDocument() {
        try {
            documentation.createDocument2();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
