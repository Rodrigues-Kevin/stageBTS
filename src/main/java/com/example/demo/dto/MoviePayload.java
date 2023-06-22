package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MoviePayload {
    private Integer id;

    private String name;

    private LocalDate releaseDate;

    private String description;

    private Integer duration;

    private Integer genre;

    private Integer director;
}
