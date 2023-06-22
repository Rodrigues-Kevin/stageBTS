package com.example.demo.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "genreUnique", columnNames = "name")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(nullable = false)
        @NotBlank
        @Size(min = 2)
        private String name;
}