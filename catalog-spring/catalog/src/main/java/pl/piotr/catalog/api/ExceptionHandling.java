package pl.piotr.catalog.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.piotr.catalog.domain.exception.DomainException;
import pl.piotr.catalog.domain.exception.ExceptionCode;
import pl.piotr.types.api.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ExceptionHandling {
    private static final String ERR0R = "error";

    private final Clock clock;
    private final DateTimeFormatter formatter;
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);

    public ExceptionHandling(Clock clock, DateTimeFormatter formatter) {
        this.clock = clock;
        this.formatter = formatter;
    }

    @ExceptionHandler({
            ServletRequestBindingException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleParsingExceptions(HttpServletRequest request, Exception exception) {
        logger.error(ERR0R, exception);

        return response(
                request.getRequestURI(),
                ExceptionCode.ERROR_PARSING_REQUEST.getDetailsPattern(),
                ExceptionCode.ERROR_PARSING_REQUEST.getCode(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            DomainException.class
    })
    public ResponseEntity<ErrorResponse> handleDomainExceptions(HttpServletRequest request, DomainException domainException) {
        logger.error(ERR0R, domainException);

        HttpStatus statusCode = HttpStatus.valueOf(domainException.getExceptionCode().getHttpStatus());
        return response(
                request.getRequestURI(),
                domainException.getMessage(),
                domainException.getExceptionCode().getCode(),
                statusCode);
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<ErrorResponse> handleExceptions(HttpServletRequest request, Exception exception) {
        logger.error(ERR0R, exception);

        return response(
                request.getRequestURI(),
                ExceptionCode.INTERNAL_SERVER.getDetailsPattern(),
                ExceptionCode.INTERNAL_SERVER.getCode(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> response(String requestURI, String message, String exceptionCode, HttpStatus statusCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setPath(requestURI);
        errorResponse.setTimestamp(formatter.format(clock.instant()));
        errorResponse.setCode(exceptionCode);

        return new ResponseEntity<>(
                errorResponse,
                statusCode
        );
    }
}
