package com.example.demo.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "bookUnique", columnNames = {"name", "releaseDate"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Genre genre;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Author author;
}