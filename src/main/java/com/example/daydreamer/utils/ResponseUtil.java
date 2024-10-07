package com.example.daydreamer.utils;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model._ResponseModel.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;

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
            for (Field entityField : entity.getClass().getDeclaredFields()) {
                entityField.setAccessible(true);
                Object value = entityField.get(entity);

                try {
                    Field responseField = responseClass.getDeclaredField(entityField.getName());
                    responseField.setAccessible(true);
                    responseField.set(response, value);
                } catch (NoSuchFieldException e) {
                    // Field not found in response class, ignore
                }
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate response", e);
        }
    }
}
