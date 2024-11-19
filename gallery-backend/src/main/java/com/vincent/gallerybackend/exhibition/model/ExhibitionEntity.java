package com.vincent.gallerybackend.exhibition.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "gallery_exhibition")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExhibitionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty()
    private String title;

    private String description;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    @Lob
    private byte[] image;

}
