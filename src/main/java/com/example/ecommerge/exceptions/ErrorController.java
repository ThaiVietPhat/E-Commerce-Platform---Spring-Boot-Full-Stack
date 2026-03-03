package com.example.ecommerge.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorController {
    // Tối ưu 1: Xử lý lỗi từ Tầng DTO (Khi @Valid ở Controller thất bại)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors().stream()
                .map(fieldError -> Map.of(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errorList);
    }

    // Tối ưu 2: Xử lý lỗi từ Tầng Entity (Khi lưu xuống DB thất bại)
    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<?> handleJPAViolations(TransactionSystemException exception) {
        // Dùng getRootCause để lấy đúng lỗi ConstraintViolationException mà không bị NullPointer
        Throwable cause = exception.getRootCause();

        if (cause instanceof ConstraintViolationException cve) {
            List<Map<String, String>> errors = cve.getConstraintViolations().stream()
                    .map(v -> Map.of(v.getPropertyPath().toString(), v.getMessage()))
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        // Luôn đảm bảo trả về một Body (dù là lỗi chung) để tránh lỗi crash Test JSON Path
        return ResponseEntity.badRequest().body(List.of(Map.of("error", "Transaction failed")));
    }
}
