package com.vincent.gallerybackend.artworkRequest.model;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtworkMapper {

    Artwork artworkDAOtoDTO(ArtworkEntity artworkEntity);

    ArtworkEntity artworkDTOtoDAO(Artwork artwork);

    List<Artwork> artworksDAOtoDTO(List<ArtworkEntity> artworkEntity);

}
