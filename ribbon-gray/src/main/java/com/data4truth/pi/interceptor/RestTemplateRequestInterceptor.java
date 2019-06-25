package com.data4truth.pi.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * @author lindj
 * @date 2019/6/22 0022
 * @description RestTemplate调用，version拦截器
 */
public class RestTemplateRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);

        String hystrixVer = HeaderInterceptor.version.get();
        LOGGER.info("restTemplate version:{} ", hystrixVer);
        requestWrapper.getHeaders().add(HeaderInterceptor.HEADER_VERSION, hystrixVer);
        return execution.execute(requestWrapper, body);
    }
}