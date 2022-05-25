
package eu.hopu.web;

import eu.hopu.utils.BlankResponseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProxyController {

    @Value("${proxy.schema}")
    private String schema;

    @Value("${proxy.host}")
    private String host;

    @Value("${proxy.port}")
    private int port;

    private final RestTemplate restTemplate;

    @Autowired
    public ProxyController() {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new BlankResponseErrorHandler());
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    @RequestMapping(value = "/**", produces = "application/json", method = {GET, DELETE, HEAD, OPTIONS})
    public ResponseEntity<String> proxyRequestWithoutBody(HttpMethod method, HttpServletRequest request) throws URISyntaxException {
        return restTemplate.exchange(buildUri(request), method, new HttpEntity<String>(copyHeaders(request)), String.class);
    }

    @RequestMapping(value = "/**", produces = "application/json", method = {POST, PUT, PATCH})
    public ResponseEntity<String> proxyRequest(@RequestBody String body, HttpMethod method, HttpServletRequest request) throws URISyntaxException {
        return restTemplate.exchange(buildUri(request), method, new HttpEntity<>(body, copyHeaders(request)), String.class);
    }

    private URI buildUri(HttpServletRequest request) throws URISyntaxException {
        return new URI(schema, null, host, port, request.getRequestURI(), request.getQueryString(), null);
    }

    private HttpHeaders copyHeaders(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (String headerName : Collections.list(request.getHeaderNames())) {
            if (!headerName.equals("host"))
                httpHeaders.add(headerName, request.getHeader(headerName));
        }
        return httpHeaders;
    }

}
