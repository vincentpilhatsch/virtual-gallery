package com.vincent.gallerybackend.artworkrequest.model;

import com.vincent.gallerybackend.artwork.model.Artwork;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ArtworkAndArtworkRequest {

    private Artwork artwork;
    private ArtworkRequest artworkRequest;

}
