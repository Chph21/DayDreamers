package com.example.daydreamer.utils;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model._ResponseModel.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseUtil {
    public static ResponseEntity<ResponseDTO> getObject(Object result, HttpStatus status, String response) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .details(ExceptionUtils.getResponseString(response))
                        .content(result)
                        .statusCode(status.value())
                        .build()
                , status
        );
    }

    public static ResponseEntity<?> getCollection(Object result, HttpStatus status, String response, MetaDataDTO metaData) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .statusCode(status.value())
                        .details(ExceptionUtils.getResponseString(response))
                        .content(result)
                        .metaDataDTO(metaData)
                        .build()
                , status
        );
    }

    public static ResponseEntity<?> error(String error, String message, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .details(ExceptionUtils.getError(error))
                        .statusCode(status.value())
                        .build()
                , status
        );
    }

    public static <T, R extends GenericResponse> R generateResponse(T entity, Class<R> responseClass) {
        try {
            R response = responseClass.getDeclaredConstructor().newInstance();
            copyGenericResponseFields(entity, response);
            copyEntityFields(entity, response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate response", e);
        }
    }

    private static <T, R extends GenericResponse> void copyGenericResponseFields(T entity, R response) throws IllegalAccessException {
        for (Field genericField : GenericResponse.class.getDeclaredFields()) {
            genericField.setAccessible(true);
            try {
                Field entityField = entity.getClass().getDeclaredField(genericField.getName());
                entityField.setAccessible(true);
                Object value = entityField.get(entity);
                if (genericField.getType().isAssignableFrom(entityField.getType())) {
                    genericField.set(response, value);
                }
            } catch (NoSuchFieldException e) {
                // Field not found in entity class, ignore
            }
        }
    }

    private static <T, R extends GenericResponse> void copyEntityFields(T entity, R response) throws IllegalAccessException {
        for (Field entityField : entity.getClass().getDeclaredFields()) {
            entityField.setAccessible(true);
            Object value = entityField.get(entity);
            try {
                Field responseField = response.getClass().getDeclaredField(entityField.getName());
                responseField.setAccessible(true);
                if (responseField.getType().isAssignableFrom(entityField.getType())) {
                    if (value instanceof List<?> entityList && !entityList.isEmpty()) {
                        List<String> idList = extractIdsFromEntityList(entityList);
                        responseField.set(response, idList);
                    } else {
                        responseField.set(response, value);
                    }
                }
            } catch (NoSuchFieldException e) {
                // Field not found in entity class, ignore
            }
        }
    }

    private static List<String> extractIdsFromEntityList(List<?> entityList) {
        return entityList.stream()
                .map(e -> {
                    try {
                        Field idField = e.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        return (String) idField.get(e);
                    } catch (Exception ex) {
                        throw new RuntimeException("Failed to get ID from entity", ex);
                    }
                })
                .collect(Collectors.toList());
    }
}