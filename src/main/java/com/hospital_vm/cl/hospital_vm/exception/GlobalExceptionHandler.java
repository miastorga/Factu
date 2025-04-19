package com.hospital_vm.cl.hospital_vm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      // Comprobar si es un FieldError o un ObjectError
      if (error instanceof FieldError) {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      } else {
        // Si es un error global (ObjectError)
        String objectName = error.getObjectName();
        String errorMessage = error.getDefaultMessage();
        // Puedes usar el nombre del objeto o una clave genérica para errores globales
        errors.put(objectName + "_error", errorMessage);
      }
    });
    return ResponseEntity.badRequest().body(errors);
  }

  // Opcional: Añadir un manejador genérico para *cualquier* otra excepción no
  // prevista
  // Esto es útil para que al menos devuelvas un JSON de error consistente en caso
  // de otros 500
  /*
   * @ExceptionHandler(Exception.class)
   * 
   * @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   * public ResponseEntity<Map<String, String>>
   * handleAllUncaughtException(Exception ex) {
   * Map<String, String> errors = new HashMap<>();
   * errors.put("general_error",
   * "Ocurrió un error interno en el servidor. Por favor, inténtelo de nuevo más tarde."
   * );
   * // !!! Es FUNDAMENTAL loggear la excepción COMPLETA en el servidor para
   * depurar !!!
   * System.err.println("Excepción no controlada:");
   * ex.printStackTrace(System.err); // Imprime el stack trace al stderr
   * 
   * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
   * }
   */
}