package com.example.demo.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "movieUnique", columnNames = {"name", "releaseDate"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 2)
    private String name;

    @Column(nullable = false)
    @Past
    private LocalDate releaseDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String description;

    @Column(nullable = false)
    @Positive
    private Integer duration;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Genre genre;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Director director;
}