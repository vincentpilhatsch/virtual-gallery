package com.vincent.gallerybackend.exhibition.repository;

import com.vincent.gallerybackend.exhibition.model.ExhibitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<ExhibitionEntity, Long> {

    ExhibitionEntity findTopByOrderByStartDateDesc();

}
