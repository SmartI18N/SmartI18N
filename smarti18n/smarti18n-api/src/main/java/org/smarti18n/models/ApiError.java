package org.smarti18n.models;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import org.smarti18n.exceptions.ApiException;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ApiError implements Serializable {

    private HttpStatus httpStatus;
    private Class<? extends ApiException> exceptionClass;
    private String message;

    public ApiError() {
    }

    public ApiError(final HttpStatus httpStatus, final Class<? extends ApiException> exceptionClass, final String message) {
        this.httpStatus = httpStatus;
        this.exceptionClass = exceptionClass;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Class<? extends ApiException> getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(final Class<? extends ApiException> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
