package com.vincent.gallerybackend.exhibition.service;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.config.FileValidationService;
import com.vincent.gallerybackend.exceptions.ImageFileException;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.exhibition.model.Exhibition;
import com.vincent.gallerybackend.exhibition.model.ExhibitionEntity;
import com.vincent.gallerybackend.exhibition.model.ExhibitionMapper;
import com.vincent.gallerybackend.exhibition.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionMapper exhibitionMapper;
    private final FileValidationService fileValidationService;

    @Transactional(readOnly = true)
    @Override
    public List<Exhibition> getExhibitions() {
        List<ExhibitionEntity> exhibitionEntities = exhibitionRepository.findAll();
        return exhibitionMapper.exhibitionDAOtoDTO(exhibitionEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public Exhibition getLatestExhibition() {
        ExhibitionEntity exhibitionEntities = exhibitionRepository.findTopByOrderByStartDateDesc();
        return exhibitionMapper.exhibitionDAOtoDTO(exhibitionEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public Exhibition getExhibitionById(long id) {
        ExhibitionEntity exhibitionEntity = findById(id);
        return exhibitionMapper.exhibitionDAOtoDTO(exhibitionEntity);
    }

    private ExhibitionEntity findById(long id) {
        return exhibitionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exhibition not found"));
    }

    @Transactional()
    @Override
    public Exhibition createExhibition(Exhibition exhibition, MultipartFile imageFile) {
        ExhibitionEntity exhibitionEntity = exhibitionMapper.exhibitionDTOtoDAO(exhibition);
        setExhibitionImage(exhibitionEntity, imageFile);
        exhibitionRepository.save(exhibitionEntity);
        return exhibitionMapper.exhibitionDAOtoDTO(exhibitionEntity);
    }

    @Transactional()
    @Override
    public Exhibition updateExhibition(Exhibition newExhibition) {
        ExhibitionEntity oldExhibition = findById(newExhibition.getId());
        ExhibitionEntity updatedExhibitionEntity = exhibitionMapper.updateExhibitionDTOtoDAO(oldExhibition, newExhibition);
        exhibitionRepository.save(updatedExhibitionEntity);
        return exhibitionMapper.exhibitionDAOtoDTO(updatedExhibitionEntity);
    }

    @Transactional()
    @Override
    public Exhibition updateExhibition(Exhibition newExhibition, MultipartFile imageFile) {
        ExhibitionEntity oldExhibition = findById(newExhibition.getId());
        ExhibitionEntity updatedExhibitionEntity = exhibitionMapper.updateExhibitionDTOtoDAO(oldExhibition, newExhibition);
        setExhibitionImage(updatedExhibitionEntity, imageFile);
        exhibitionRepository.save(updatedExhibitionEntity);
        return exhibitionMapper.exhibitionDAOtoDTO(updatedExhibitionEntity);
    }

    private void setExhibitionImage(ExhibitionEntity exhibitionEntity, MultipartFile imageFile) {
        fileValidationService.validateFile(imageFile);
        fileValidationService.safeImageInEntity(exhibitionEntity, imageFile);
    }


    @Transactional()
    @Override
    public void deleteExhibition(long id) {
        ExhibitionEntity exhibitionEntity = findById(id);
        exhibitionRepository.delete(exhibitionEntity);
    }

}
