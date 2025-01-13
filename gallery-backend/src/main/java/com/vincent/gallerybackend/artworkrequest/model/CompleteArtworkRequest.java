package com.vincent.gallerybackend.artworkrequest.model;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.user.model.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CompleteArtworkRequest {

    private ArtworkRequest artworkRequest;
    private Artwork artwork;
    private User user;

}
