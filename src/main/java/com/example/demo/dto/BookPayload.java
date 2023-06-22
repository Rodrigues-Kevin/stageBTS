package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookPayload {
    private Integer id;

    private String name;

    private LocalDate releaseDate;

    private String description;

    private Integer genre;

    private Integer author;
}
