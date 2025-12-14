package ural.ba.project.UralBA.exepction;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ural.ba.project.UralBA.dto.ExceptionResponseDTO;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для REST-контроллеров
 *
 * @author Daria
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка ошибоки несуществующего пути
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleNotFound(NoHandlerFoundException ex,
                                                               HttpServletRequest request) {
        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.NOT_FOUND,
                "Путь не найден",
                request.getRequestURI(),
                "PATH_NOT_FOUND"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Обработка ошибок доступа
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAccessDeniedException(AccessDeniedException ex,
                                                                            HttpServletRequest request
    ) {

        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.FORBIDDEN,
                "Доступ запрещен",
                request.getRequestURI(),
                "ACCESS_DENIED"
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * Обработка исключений неудачной авторизации
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAuthException(AuthException ex,
                                                                    HttpServletRequest request) {
        String message = ex.getMessage();

        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.UNAUTHORIZED,
                message,
                request.getRequestURI(),
                "UNAUTHORIZED"
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


    /**
     * Обработка исключений ненайденных сущностей
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleResourceNotFound(ResourceNotFoundException ex,
                                                                       HttpServletRequest request) {
        String message = ex.getMessage();

        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.NOT_FOUND,
                message,
                request.getRequestURI(),
                "ENTITY_NOT_FOUND"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Обработка бизнес-логических исключений аргументов
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDTO> handleBadRequestArgument(IllegalArgumentException ex,
                                                                         HttpServletRequest request) {
        String message = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : "Некорректные параметры запроса";

        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI(),
                "INVALID_INPUT"
        );

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Обработка бизнес-логических исключений состояний сущности
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponseDTO> handleBadRequestState(RuntimeException ex,
                                                                      HttpServletRequest request) {
        String message = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : "Недопустимое состояние объекта";

        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI(),
                "ILLEGAL_STATE"
        );

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Обработка всех неперехваченных исключений
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleAllUncaught(Exception ex,
                                                                  HttpServletRequest request) {
        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера",
                request.getRequestURI(),
                "INTERNAL_ERROR"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Обработка исключений валидации входных данных в dto
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex,
                                                                       HttpServletRequest request) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " +
                        fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.BAD_REQUEST,
                "Ошибка валидации данных: " + errorMessage,
                request.getRequestURI(),
                "VALIDATION_FAILED"
        );

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Обработка исключений валидации параметров методов
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleHandlerMethodValidation(HandlerMethodValidationException ex,
                                                                              HttpServletRequest request) {
        String errorMessage = ex.getParameterValidationResults()
                .stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(error -> Objects.toString(error.getDefaultMessage(), "Ошибка валидации"))
                .findFirst()
                .orElse("Ошибка валидации параметров");

        ExceptionResponseDTO error = new ExceptionResponseDTO(
                HttpStatus.BAD_REQUEST,
                "Некорректные параметры метода: " + errorMessage,
                request.getRequestURI(),
                "VALIDATION_FAILED"
        );

        return ResponseEntity.badRequest().body(error);
    }
}
