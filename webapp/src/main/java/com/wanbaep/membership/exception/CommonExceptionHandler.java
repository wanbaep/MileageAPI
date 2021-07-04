package com.wanbaep.membership.exception;

import com.wanbaep.membership.dto.ApiResponseDto;
import com.wanbaep.membership.dto.ErrorResponse;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<?> handleBadRequestException() {
        ErrorResponse error = new ErrorResponse();
        error.setMessage("Bad Request");
        error.setStatus(400);
        ApiResponseDto responseDto = new ApiResponseDto(false, null, error);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFoundException() {
        ErrorResponse error = new ErrorResponse();
        error.setMessage("Not Found");
        error.setStatus(404);
        ApiResponseDto responseDto = new ApiResponseDto(false, null, error);
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            MissingPathVariableException.class,
            ConversionNotSupportedException.class,
            HttpMessageNotWritableException.class
    })
    public ResponseEntity<?> handleInternalServerErrorException() {
        ErrorResponse error = new ErrorResponse();
        error.setMessage("Internal Server Error");
        error.setStatus(500);
        ApiResponseDto responseDto = new ApiResponseDto(false, null, error);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(e.getMessage());
        error.setStatus(e.getStatus().value());
        ApiResponseDto responseDto = new ApiResponseDto(false, null, error);
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateMembershipException.class)
    public ResponseEntity<?> handleDuplicateMembershipException(DuplicateMembershipException e) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(e.getMessage());
        error.setStatus(e.getStatus().value());
        ApiResponseDto responseDto = new ApiResponseDto(false, null, error);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
