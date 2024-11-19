package com.vincent.gallerybackend.exhibition.service;

import com.vincent.gallerybackend.exhibition.model.Exhibition;
import com.vincent.gallerybackend.exhibition.model.ExhibitionEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ExhibitionService {

    List<Exhibition> getExhibitions();

    Exhibition getExhibitionById(long id);

    ExhibitionEntity createExhibition(Exhibition exhibition, MultipartFile multipartFile);

    ExhibitionEntity updateExhibition(Exhibition exhibition, MultipartFile multipartFile);

    void deleteExhibition(long id);

}
