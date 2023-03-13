package org.ohours.userAPI.controller.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.ohours.userAPI.controller.exception.UserException;
import org.ohours.userAPI.controller.exception.UserIsMinorException;
import org.ohours.userAPI.controller.exception.UserNotFoundException;
import org.ohours.userAPI.controller.exception.UsernameTakenException;
import org.ohours.userAPI.model.Country;
import org.ohours.userAPI.model.Gender;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class UserErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorEntity> errors = new ArrayList<ErrorEntity>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorEntity(error.getField(), error.getDefaultMessage()));
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(new ErrorEntity(error.getObjectName(), error.getDefaultMessage()));
        }
        ErrorsResponseEntity errorsResponse = new ErrorsResponseEntity(errors);
        return handleExceptionInternal(ex, errorsResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorEntity> errors = new ArrayList<ErrorEntity>();
        Throwable rootError = ex.getRootCause();
        if (rootError instanceof DateTimeParseException)
            errors.add(new ErrorEntity("birthdate", ((DateTimeParseException) rootError).getParsedString()));
        else if (rootError instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) rootError;
            String fieldName = ife.getPath().get(0).getFieldName();
            String errorMessage = ife.getValue().toString();
            if (Country.class.getName().equals(ife.getTargetType().getName()))
                errorMessage = "Only French residents allowed, found " + errorMessage;
            else if (Gender.class.getName().equals(ife.getTargetType().getName()))
                errorMessage = "Gender found " + errorMessage + ", allowed from " + Arrays.toString(Gender.values());
            errors.add(new ErrorEntity(fieldName, errorMessage));
        } else errors.add(new ErrorEntity("NotReadable", "Error reading the request"));
        ErrorsResponseEntity errorsResponse = new ErrorsResponseEntity(errors);
        return handleExceptionInternal(ex, errorsResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorEntity> errors = new ArrayList<ErrorEntity>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new ErrorEntity(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        ErrorsResponseEntity errorsResponse = new ErrorsResponseEntity(errors);
        return new ResponseEntity(errorsResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        List<ErrorEntity> errors = new ArrayList<ErrorEntity>();
        errors.add(new ErrorEntity(ex.getTitle(), ex.getMessage()));
        ErrorsResponseEntity errorsResponse = new ErrorsResponseEntity(errors);
        return new ResponseEntity(errorsResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UsernameTakenException.class, UserIsMinorException.class})
    protected ResponseEntity<Object> handleOtherUserException(UserException ex) {
        List<ErrorEntity> errors = new ArrayList<ErrorEntity>();
        errors.add(new ErrorEntity(ex.getTitle(), ex.getMessage()));
        ErrorsResponseEntity errorsResponse = new ErrorsResponseEntity(errors);
        return new ResponseEntity(errorsResponse, HttpStatus.BAD_REQUEST);
    }

}
