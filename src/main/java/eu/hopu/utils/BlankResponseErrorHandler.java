package eu.hopu.utils;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class BlankResponseErrorHandler implements ResponseErrorHandler {

    public boolean hasError(ClientHttpResponse clientHttpResponse) {
        return false;
    }

    public void handleError(ClientHttpResponse clientHttpResponse) {

    }


}
