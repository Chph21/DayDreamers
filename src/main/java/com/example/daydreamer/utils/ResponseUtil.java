package com.example.daydreamer.utils;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model._ResponseModel.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

            // Check if the field in response has the "classname + Id" format
            if (value != null && isRelationshipField(value)) {
                setRelationshipField(response, entityField, value);
            } else {
                setDirectField(response, entityField, value);
            }
        }
    }

    // Method to check if the field is a relationship by looking for an "id" field
    private static boolean isRelationshipField(Object value) {
        try {
            value.getClass().getDeclaredField("id");
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private static <R extends GenericResponse> void setRelationshipField(R response, Field entityField, Object value) throws IllegalAccessException {
        try {
            Field responseField = response.getClass().getDeclaredField(entityField.getName() + "Id");
            responseField.setAccessible(true);

            // Use the getter method for the 'id' field
            Method idGetter = value.getClass().getMethod("getId"); // Assuming 'getId' follows naming conventions
            Object relatedEntityId = idGetter.invoke(value); // Invoke the getter to retrieve the ID

            responseField.set(response, relatedEntityId);
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Ignore if no matching field or method
        }
    }


    // Sets direct fields from entity to response if types match
    private static <R extends GenericResponse> void setDirectField(R response, Field entityField, Object value) throws IllegalAccessException {
        try {
            if (value instanceof List<?> entityList && !entityList.isEmpty()) {
                Field responseField = response.getClass().getDeclaredField(entityField.getName() + "Ids");
                responseField.setAccessible(true);
                List<String> idList = extractIdsFromEntityList(entityList);
                responseField.set(response, idList);
            } else {
                Field responseField = response.getClass().getDeclaredField(entityField.getName());
                responseField.setAccessible(true);
                if (responseField.getType().isAssignableFrom(entityField.getType())) {
                    responseField.set(response, value);
                }
            }
        } catch (NoSuchFieldException e) {
            // Ignore if no matching field in response
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