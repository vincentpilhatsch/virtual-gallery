package com.vincent.gallerybackend.artworkrequest.model;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtworkRequestMapper {

    ArtworkRequest artworkRequestDAOtoDTO(ArtworkRequestEntity artworkRequestEntity);

    ArtworkRequestEntity artworkRequestDTOtoDAO(ArtworkRequest artworkRequest);

    List<ArtworkRequest> artworkRequestsDAOtoDTO(List<ArtworkRequestEntity> artworkRequestEntity);

}
