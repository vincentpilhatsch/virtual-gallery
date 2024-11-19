package com.vincent.gallerybackend.exhibition.model;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Exhibition {

    private long id;
    private String title;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private byte[] image;

}
