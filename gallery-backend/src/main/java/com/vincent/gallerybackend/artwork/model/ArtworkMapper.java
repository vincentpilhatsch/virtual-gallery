    package com.vincent.gallerybackend.artwork.model;

    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;

    @Mapper(componentModel = "spring")
    public interface ArtworkMapper {

        Artwork artworkDAOtoDTO(ArtworkEntity userEntity);

        ArtworkEntity artworkDTOtoDAO(Artwork artwork);

        @Mapping(target = "id", source = "oldArtworkEntity.id")
        @Mapping(target = "title", source = "newArtwork.title")
        @Mapping(target = "width", source = "newArtwork.width")
        @Mapping(target = "height", source = "newArtwork.height")
        @Mapping(target = "yearOfCreation", source = "newArtwork.yearOfCreation")
        @Mapping(target = "uploadedAt", source = "newArtwork.uploadedAt")
        @Mapping(target = "technic", source = "newArtwork.technic")
        @Mapping(target = "price", source = "newArtwork.price")
        @Mapping(target = "sold", source = "newArtwork.sold")
        @Mapping(target = "image", source = "newArtwork.image")
        ArtworkEntity updateArtworkDTOtoDAO(ArtworkEntity oldArtworkEntity, Artwork newArtwork);

    }
