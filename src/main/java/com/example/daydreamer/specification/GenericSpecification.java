package com.example.daydreamer.specification;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification {

    public static <T> Specification<T> hasEqual(String attribute, Object value) {
        return (root, query, criteriaBuilder) -> {
            Path<Object> path = getPath(root, attribute);
            return criteriaBuilder.equal(path, value);
        };
    }

    public static <T> Specification<T> hasLike(String attribute, String value) {
        return (root, query, criteriaBuilder) -> {
            Path<String> path = getPath(root, attribute);
            return criteriaBuilder.like(path, "%" + value + "%");
        };
    }

    public static <T, Y extends Comparable<? super Y>> Specification<T> hasGreaterThan(String attribute, Y value) {
        return (root, query, criteriaBuilder) -> {
            Path<Y> path = getPath(root, attribute);
            return criteriaBuilder.greaterThan(path, value);
        };
    }

    public static <T, Y extends Comparable<? super Y>> Specification<T> hasLessThan(String attribute, Y value) {
        return (root, query, criteriaBuilder) -> {
            Path<Y> path = getPath(root, attribute);
            return criteriaBuilder.lessThan(path, value);
        };
    }

    public static <T, U> Specification<T> addSpecification(Specification<T> spec, U value, String attribute, String operator) {
        if (value != null && !value.toString().isEmpty()) {
            return switch (operator) {
                case "equal" -> spec.and(GenericSpecification.hasEqual(attribute, value));
                case "like" -> spec.and(GenericSpecification.hasLike(attribute, value.toString()));
                case "greaterThan" -> spec.and(GenericSpecification.hasGreaterThan(attribute, (Comparable) value));
                case "lessThan" -> spec.and(GenericSpecification.hasLessThan(attribute, (Comparable) value));
                default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
            };
        }
        return spec;
    }

    private static <T> Path<T> getPath(Path<?> root, String attribute) {
        Path<?> path = root;
        for (String part : attribute.split("\\.")) {
            path = path.get(part);
        }
        return (Path<T>) path;
    }
}