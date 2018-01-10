package org.smarti18n.api;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.smarti18n.exceptions.ApiException;
import org.smarti18n.models.ApiError;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ApiExceptionHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean hasError(final ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(final ClientHttpResponse response) throws IOException {
        final String responseBody = IOUtils.toString(response.getBody(), Charset.defaultCharset());

        final ApiError apiError = this.objectMapper.readValue(responseBody, ApiError.class);

        final Class<? extends ApiException> exceptionClass = apiError.getExceptionClass();

        try {
            throw exceptionClass.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
