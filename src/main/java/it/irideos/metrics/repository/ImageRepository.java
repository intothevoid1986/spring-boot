package it.irideos.metrics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.irideos.metrics.models.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {

    @Query(value = "SELECT i.id, i.image_ref, i.service FROM image i WHERE i.image_ref = :image_ref", nativeQuery = true)
    List<ImageModel> findByImageModels(@Param("image_ref") String img_ref);
}
