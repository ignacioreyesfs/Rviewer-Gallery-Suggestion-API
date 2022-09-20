package com.rviewer.skeletons.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rviewer.skeletons.exceptions.APIError;
import com.rviewer.skeletons.exceptions.AppException;
import com.rviewer.skeletons.exceptions.ErrorDTO;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorDTO> handleAppException(AppException ex){
		return ResponseEntity.status(ex.getStatus())
				.body(new ErrorDTO(ex.getMessage(), ex.getReasons()));
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> reasons = new ArrayList<>();
		
		for(FieldError error: ex.getBindingResult().getFieldErrors()) {
			reasons.add(String.format("%s - %s", error.getField(), error.getDefaultMessage()));
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorDTO(APIError.VALIDATION_ERROR.getMessage(), reasons));
	}
}
