package ru.epa.epabackend.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.epa.epabackend.exception.exceptions.WrongFullNameException;
import ru.epa.epabackend.util.Constants;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class,
            MethodArgumentNotValidException.class,
            InvalidParameterException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            WrongFullNameException.class,
            MethodArgumentNotValidException.class, BadRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequest(final Exception e) {
        return new ErrorMessage(
                e.getClass().getSimpleName(),
                e.getMessage(),
                "",
                "BAD_REQUEST",
                Constants.DATE_TIME_SPACE.format(LocalDateTime.now()));
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDoubleData(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse(e.getMessage());
    }
}