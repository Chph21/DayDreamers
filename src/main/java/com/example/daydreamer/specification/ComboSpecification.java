package com.example.daydreamer.specification;

import com.example.daydreamer.entity.Combo;
import org.springframework.data.jpa.domain.Specification;


public class ComboSpecification {

    public static Specification<Combo> hasStudioId(String studioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("studio").get("id"), studioId);
    }

    public static Specification<Combo> hasNumOfEditedPhotos(Integer numOfEditedPhotos) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("editedPhotos"), numOfEditedPhotos);
    }

    public static Specification<Combo> hasNumOfDownloadablePhotos(Integer numOfDownloadablePhotos) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("downloadablePhotos"), numOfDownloadablePhotos);
    }

    public static Specification<Combo> hasDuration(Integer duration) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("duration"), duration);
    }

    public static Specification<Combo> hasPrice(Double price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("price"), price);
    }

    public static Specification<Combo> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }
}
