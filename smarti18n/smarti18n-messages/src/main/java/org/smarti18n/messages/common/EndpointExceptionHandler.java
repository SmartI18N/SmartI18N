package org.smarti18n.messages.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.smarti18n.exceptions.ApiException;
import org.smarti18n.models.ApiError;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@ControllerAdvice
public class EndpointExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<Object> handleConflict(final ApiException ex, final WebRequest request) {
        final ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getClass(),
                ex.getMessage()
        );

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
