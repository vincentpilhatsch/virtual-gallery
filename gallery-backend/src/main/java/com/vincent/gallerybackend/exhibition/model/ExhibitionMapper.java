package com.vincent.gallerybackend.exhibition.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExhibitionMapper {

    Exhibition exhibitionDAOtoDTO(ExhibitionEntity exhibitionEntity);

    ExhibitionEntity exhibitionDTOtoDAO(Exhibition exhibition);

    List<Exhibition> exhibitionDAOtoDTO(List<ExhibitionEntity> exhibitionEntities);

    @Mapping(target = "id", source = "oldExhibitionEntity.id")
    @Mapping(target = "title", source = "newExhibition.title")
    @Mapping(target = "description", source = "newExhibition.description")
    @Mapping(target = "location", source = "newExhibition.location")
    @Mapping(target = "startDate", source = "newExhibition.startDate")
    @Mapping(target = "endDate", source = "newExhibition.endDate")
    @Mapping(target = "image", source = "newExhibition.image")
    ExhibitionEntity updateExhibitionDTOtoDAO(ExhibitionEntity oldExhibitionEntity, Exhibition newExhibition);

}
